
from montysolr import handler


class Handler(handler.Handler):
    '''Simple handler that just calls the methods sequentially
    '''

    def init(self):
        self.discover_targets(["monty_invenio.tests.fake_query.targets"])



#Handler = Handler()
