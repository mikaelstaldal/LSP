from ZeroTemplate import ZeroTemplate
import os
import sys

ZeroTemplate.setPageResolver(lambda (pageName): file(pageName+'.zt'))
os.chdir('../testsuite/ztPages')
doc = ZeroTemplate.loadPage('unicode')
doc.render(sys.stdout, None)

