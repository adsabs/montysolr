from org.jython.monty.interfaces import JythonSimpleClass

class SimpleClass(JythonSimpleClass):

	def __init__(self):
		self.name = 'foo'

	def set_name(self, name):
		self.name = name 

	def get_name(self):
		return self.name