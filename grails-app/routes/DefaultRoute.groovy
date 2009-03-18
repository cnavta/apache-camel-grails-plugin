
class DefaultRoute {

	def configure = {
		from('seda:grails.plugin.camel.test').to('stream:out')
	}

}