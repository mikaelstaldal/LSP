# -*- coding: ISO-8859-1 -*-

from ZeroTemplate import ZeroTemplate
import os
import sys

"""
ustr = u"Räksmörgås"
print ustr
print >>sys.stdout, ustr
sys.stdout.write(ustr)
"""

ZeroTemplate.setPageResolver(lambda (pageName): file(pageName+'.zt'))
os.chdir('../testsuite/ztPages')
doc = ZeroTemplate.loadPage('test')
doc.render(sys.stdout, None)
