'''
Created on Feb 4, 2011

@author: rca
'''

import logging
import traceback
import sys
import imp
import os

class Handler(object):
    '''Handler objects are responsible for passing messages from the MontySolr
    bridge towards the real method that knows what to do with them. Because
    the handler is potentially expensive to create, they are always singletons.

    Of course, this is the basic class
    '''
    def __init__(self):
        self._db = {}
        self.log = logging
        self.init()

    def init(self):
        raise NotImplemented("This method must be overriden")


    def handle_message(self, message):
        '''Receives the messages, finds the target of the message
        and calls it, passing it the message instance'''
        target = self.get_target(message)
        if target:
            target(message)


    def get_target(self, message):
        """Must return only a callables that receive
        a PythonMessage object"""
        recipient = message.getReceiver()
        sender = message.getSender()

        message_id = (sender or '') + ':' + recipient
        if message_id in self._db:
            return self._db[message_id]
        else:
            self.log.error("Unknown target; sender=%s, recipient=%s, message_id=%s" %
                           (sender, recipient, message_id))

    def discover_targets(self, places):
        '''Queries the different objects for existence of the callable
        called montysolr_targets. If that callable is present, it will
        get from it the MontySolrTarget instances, which represent the
        message_id and target -- for the (PythonMessage) objects
        @var places: (list) must be a list of either strings
             example:
                'package.module'
                    package.module has a method 'montysolr_targets'
                '/tmp/package/module/someothermodule.py'
                    we create a new anonymous module and call its
                   'montysolr_targets' method
            or the object may be a python objects that has a
            callble method 'montysolr_targets'
        '''
        if not isinstance(places, list):
            raise Exception("The argument must be a list")

        for place in places:
            if isinstance(place, basestring):
                if os.path.exists(place): # it is a module
                    try:
                        obj = self.create_module(place)
                        self.retrieve_targets(obj)
                    except:
                        self.log.error(traceback.format_exc())
                else:
                    obj = self.import_module(place)
                    self.retrieve_targets(obj)
            else:
                self.retrieve_targets(place)

    def import_module(self, module_name):
        """Import workflow module
        @var workflow: string as python import, eg: merkur.workflow.load_x"""
        mod = __import__(module_name)
        components = module_name.split('.')
        for comp in components[1:]:
            mod = getattr(mod, comp)
        return mod

    def create_module(self, file, anonymous=False, fail_onerror=True):
        """ Initializes module into a separate object (not included in sys) """
        name = 'MontySolrTmpModule<%s>' % os.path.basename(file)
        x = imp.new_module(name)
        x.__file__ = file
        x.__id__ = name
        x.__builtins__ = __builtins__

        # XXX - chdir makes our life difficult, especially when
        # one workflow wrap another wf and relative paths are used
        # in the config. In such cases, the same relative path can
        # point to different locations just because location of the
        # workflow (parts) are different
        # The reason why I was using chdir is because python had
        # troubles to import files that containes non-ascii chars
        # in their filenames. That is important for macros, but not
        # here.

        # old_cwd = os.getcwd()

        try:
            #filedir, filename = os.path.split(file)
            #os.chdir(filedir)
            if anonymous:
                execfile(file, x.__dict__)
            else:
                #execfile(file, globals(), x.locals())
                exec open(file).read()
        except Exception, excp:
            if fail_onerror:
                raise Exception(excp)
            else:
                self.log.error(excp)
                self.log.error(traceback.format_exc())
                return
        return x

    def retrieve_targets(self, obj):
        if hasattr(obj, 'montysolr_targets'):
            db = self._db
            for t in obj.montysolr_targets():
                message_id = t.getMessageId()
                target = t.getTarget()
                if message_id in db:
                    raise Exception("The message with id '%s' already has a target:" %
                                    (message_id, db[message_id]))
                db[message_id] = target
        else:
            self.log.error("The %s has no method 'montysolr_targets'" % obj)

        if ':diagnostic_test' not in db:
            db[':diagnostic_test'] = self._diagnostic_target()


    def _diagnostic_target(self):
        def diagnostic_target(message):
            out = []
            out.append("id(sys) %s" % id(sys))
            out.append("sys.path: %s" % "\n  ".join(sys.path))
            out.append("sys.argv: %s" % "\n  ".join(sys.argv))
            out.append("PYTHONPATH: %s" % os.getenv("PATH"))
            out.append("PYTHONHOME: %s" % os.getenv("PYTHONHOME"))
            out.append("PATH: %s" % os.getenv("PATH"))
            out.append("LD_LIBRARY_PATH: %s" % os.getenv("LD_LIBRARY_PATH"))

            out.append('---')
            out.append('handler: %s' % self)


            out.append('---')
            out.append('current targets: \n%s' % "                 \n".join(map(lambda x: '%s --> %s' % x, self._db.items())))

            out.append('---')
            out.append('running diagnostic tests from the targets (if available):')
            for k,v in self._db.items():
                if 'diagnostic_test' in k and k != ':diagnostic_test':
                    out.append('===================')
                    out.append(k)
                    try:
                        v(message)
                        res = message.getResults()
                        out.append(str(res))
                    except:
                        out.append(traceback.format_exc())
                    out.append('===================')

            message.setResults('\n'.join(out))

        return diagnostic_target




