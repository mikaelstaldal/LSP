from HTMLParser import HTMLParser

EMPTY_ELEMENTS = frozenset(("area", "base", "br", "col", "hr", "img", "input", "link", "meta", "basefont", "frame", "isindex", "param"))

PageCache = {}
PageResolver = None


def setPageResolver(pageResolver):
    global PageResolver
    PageResolver = pageResolver


def renderPage(pageName, context):
    page = getPage(pageName)
    if not page: 
        raise LookupError
    # do rendering here...


def getPage(pageName):
    if pageName not in PageCache:
        page = loadPage(pageName)
        if page:
            PageCache[pageName] = page
        return page
    else:
        return PageCache[pageName]

        
def loadPage(pageName):
    data = PageResolver(pageName)
    if not data:
        return None
    try:
        parser = MyHTMLParser()
        for line in data:
            parser.feed(line)
        parser.close()
    finally:
        data.close()
    # do parsing here
    return parser.getDocument()


class Node(object):
    def __init__(self, parent=None):
        self.parent = parent
        
    def render(self, out, context):
        pass        


class NodeWithChildren(Node):
    def __init__(self):
        Node.__init__(self)
        self.children = []
        
    def render(self, out, context):
        for child in self.children:
            child.render(out, context)

    def appendChild(self, child):
        self.children.append(child)
        child.parent = self


class Document(NodeWithChildren):
    pass


class Text(Node):
    def __init__(self, text):
        Node.__init__(self)
        self.text = text

    def render(self, out, context):
        out.write(self.text.encode('UTF-8'))
    

class EntityRef(Node):
    def __init__(self, name):
        Node.__init__(self)
        self.name = name

    def render(self, out, context):
        out.write("&%s;" % self.name.encode('UTF-8'))


class Element(NodeWithChildren):
    def __init__(self, tag, attrs):
        NodeWithChildren.__init__(self)
        self.tag = tag
        self.attrs = attrs

    def render(self, out, context):
        if not self.children and self.tag in EMPTY_ELEMENTS:
            out.write("<%s />" % self.tag.encode('UTF-8'))
        else:
            out.write("<%s>" % self.tag.encode('UTF-8'))
            NodeWithChildren.render(self, out, context)
            out.write("</%s>" % self.tag.encode('UTF-8'))
        

class MyHTMLParser(HTMLParser):    
    def __init__(self):
        HTMLParser.__init__(self)
        self.textBuffer = u""
        self.currentElement = Document()

    def _fixText(self):
        if self.textBuffer:
            self.currentElement.appendChild(Text(self.textBuffer))
            self.textBuffer = u""

    def handle_starttag(self, tag, attrs):
        self._fixText()
        el = Element(tag, attrs)
        self.currentElement.appendChild(el)
        self.currentElement = el

    def handle_endtag(self, tag):
        self._fixText()
        self.currentElement = self.currentElement.parent

    def handle_data(self, data):
        s = data.decode("UTF-8")
        self.textBuffer += s

    def handle_charref(self, name):
        if name[0] == 'x' or name[0] == 'X':
            codepoint = int(name[1:], 16)
        else:
            codepoint = int(name, 10)
        self.textBuffer += unichr(codepoint)
    
    def handle_entityref(self, name):
        self._fixText()
        self.currentElement.appendChild(EntityRef(name))
        
    def getDocument(self):
        return self.currentElement



"""
def getAtomPubCollection(atomPubURL, username, password):
    # Establish password for HTTPblogConfig.username, blogConfig.password
    passman = urllib2.HTTPPasswordMgrWithDefaultRealm()   
    passman.add_password(None, atomPubURL, username, password)
    authhandler = urllib2.HTTPBasicAuthHandler(passman)
    opener = urllib2.build_opener(authhandler)
    urllib2.install_opener(opener)
    
    # Fetch and parse Atom Service
    atom = urlopen(atomPubURL)
    atomTree = xml.etree.ElementTree.parse(atom)
    atom.close()

    collectionURL = None
    # Use Atom
    for workspace in atomTree.findall("{http://www.w3.org/2007/app}workspace"):
        for collection in workspace.findall("{http://www.w3.org/2007/app}collection"):
            accepts = collection.findall("{http://www.w3.org/2007/app}accept")
            if (len(accepts) == 0):
                collectionURL = collection.get("href")
            for accept in accepts:
                if accept.text == "application/atom+xml;type=entry":
                    collectionURL = collection.get("href")
                    break

    if collectionURL:
        passman.add_password(None, collectionURL, username, password)
    return collectionURL


def ping(blogName, blogURL):
    if blogURL not in config.allowedBlogs:
        syslog.syslog(syslog.LOG_WARNING, "%s is not allowed to ping" % blogURL)
        return { "flerror": True,
                 "message": "Your blog is not allowed to ping" }

    blogConfig = config.allowedBlogs[blogURL]

    if not blogConfig["atomURL"]:
        # Fetch blog
        try:
            blog = urlopen(blogURL)
            if blog.geturl()[:5].lower() == 'file:':
                syslog.syslog(syslog.LOG_WARNING, "%s uses file: URL:s" % blogURL)
                raise ValueError, "file: URL:s are not premitted here"                
            blogData = blog.read()        
            blog.close()
        except URLError, err: 
            syslog.syslog(syslog.LOG_WARNING, "%s: Unable to fetch blog URL: %s" % (blogURL, err))
            return { "flerror": True,
                     "message": "Unable to fetch blog URL" }
        except ValueError: 
            syslog.syslog(syslog.LOG_WARNING, "%s: Unknown protocol" % blogURL)
            return { "flerror": True,
                     "message": "Unknown blog URL protocol" }

        # Parse blog
        try:
            linkHTMLParser = LinkHTMLParser()
            linkHTMLParser.feed(blogData)
            linkHTMLParser.close()
            atomURL = linkHTMLParser.getAtomURL()
        except HTMLParseError, err:
            syslog.syslog(syslog.LOG_WARNING, "%s: Unable to parse blog: %s" % (blogURL, err))
            return { "flerror": True,
                     "message": "Cannot parse blog" }        

        if not atomURL:
            syslog.syslog(syslog.LOG_WARNING, "%s: No link to Atom feed in blog" % blogURL)
            return { "flerror": True,
                     "message": "No link to Atom feed in blog" }
    else:
        atomURL = blogConfig["atomURL"]

    # Fetch and parse Atom
    try:
        atom = urlopen(atomURL)
        if atom.geturl()[:5].lower() == 'file:':
            syslog.syslog(syslog.LOG_WARNING, "%s uses file: URL:s" % atomURL)
            raise ValueError, "file: URL:s are not premitted here"                
        atomTree = xml.etree.ElementTree.parse(atom)
        atom.close()

    except URLError, err: 
        syslog.syslog(syslog.LOG_WARNING, "%s: Unable to fetch atom URL: %s" % (atomURL, err))
        return { "flerror": True,
                 "message": "Unable to fetch atom URL" }
    except ValueError: 
        syslog.syslog(syslog.LOG_WARNING, "%s: Unknown protocol" % atomURL)
        return { "flerror": True,
                 "message": "Unknown atom URL protocol" }
    except xml.parsers.expat.ExpatError: 
        syslog.syslog(syslog.LOG_WARNING, "%s: Atom is not well-formed XML" % atomURL)
        return { "flerror": True,
                 "message": "Atom is not well-formed XML" }

    atomPostURL = getAtomPubCollection(config.atomServiceURL, 
                                       blogConfig["username"], blogConfig["password"])

    db = MySQLdb.connect(user="wordpress",passwd="wordpress",db="wordpress")

    # Use Atom
    for entry in atomTree.findall("{http://www.w3.org/2005/Atom}entry"):
        useThis = False
        for category in entry.findall("{http://www.w3.org/2005/Atom}category"):
            if blogConfig["category"] == category.get("term"): 
                useThis = True
                if blogConfig["removeCategory"]: entry.remove(category)
        if useThis:
            theId = entry.find("{http://www.w3.org/2005/Atom}id").text
            c = db.cursor()
            c.execute("SELECT * FROM bloggregeringen_posts WHERE id=%s", (theId,))
            useThis = not c.fetchall()
            c.close()
            if useThis:
                for link in entry.findall("{http://www.w3.org/2005/Atom}link"): entry.remove(link)
                
                try: 
                    c = db.cursor()
                    c.execute("INSERT INTO bloggregeringen_posts(id) VALUES(%s)", (theId,))
                    c.close()
                    db.commit()
                    syslog.syslog(syslog.LOG_INFO, "About to post: %s" % theId)
                    print "Should post: %s" % theId
                    # print "Should post: %s" % xml.etree.ElementTree.tostring(entry, "UTF-8")
                    atomPost = urlopen(urllib2.Request(atomPostURL, 
                       xml.etree.ElementTree.tostring(entry, "UTF-8"),
                       { "Content-Type": "application/atom+xml;type=entry" }))
                    syslog.syslog(syslog.LOG_WARNING, "Unable to post: 200")
                    print "Unable to post: 200"
                    atomPost.close()

                except HTTPError, httpError:   
                    if httpError.code != 201:
                        syslog.syslog(syslog.LOG_WARNING, "Unable to post: %s %s" % (httpError.code, httpError.msg))
                        print "Unable to post: %s %s" % (httpError.code, httpError.msg)
                    else:
                        syslog.syslog(syslog.LOG_INFO, "Posted successfully: %s %s" % (httpError.code, httpError.msg))
                        print "Posted successfully: %s %s" % (httpError.code, httpError.msg)
                    httpError.close()
            else:
                break

    db.close()

    return { "flerror": False,
             "message": "Success" }
"""
