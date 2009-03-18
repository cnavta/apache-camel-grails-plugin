import org.apache.camel.spring.CamelContextFactoryBean
import org.ix.grails.plugins.camel.*
import org.ix.test.*
import grails.util.GrailsNameUtils
import org.apache.camel.model.ProcessorType
import org.apache.camel.model.ChoiceType
import org.apache.camel.language.groovy.CamelGroovyMethods
import org.springframework.beans.factory.config.MethodInvokingFactoryBean
import org.apache.log4j.Logger

class CamelGrailsPlugin {

	private static final Logger log = Logger.getLogger('org.ix.grails.plugins.camel.CamelGrailsPlugin')

    // the plugin version
    def version = "0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.1 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def loadAfter = ['controllers','services']
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]
    def watchedResources = [
            "file:./grails-app/routes/**/*Route.groovy",
            "file:./plugins/*/grails-app/routes/**/*Route.groovy",
            "file:./grails-app/controllers/**/*Controller.groovy",
            "file:./grails-app/services/**/*Service.groovy"
    ]


    def artefacts = [new RouteArtefactHandler()]

    def author = "Chris Navta"
    def authorEmail = "chris@ix-n.com"
    def title = "Integration with Apache Camel"
    def description = '''\\
An integration with Apache Camel, giving Controllers and Services a 'sendMessage' method that will send
a message to a given endpoint.

Also adds a 'Route' artifact that allows configuration of Camel routing using the Java DSL. New Routes can be
added with the 'grails create-route MyRouteName' command.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/Camel+Plugin"

    def doWithSpring = {
    	init()

		xmlns camel:'http://activemq.apache.org/camel/schema/spring'

		def routeClasses = application.routeClasses

		log.debug "Configuring Routes"
		routeClasses.each { routeClass ->
			configureRouteBeans.delegate = delegate
			configureRouteBeans(routeClass)
		}

		camel {
			camelContext(id:'camelContext')
			{
				routeClasses.each { routeClass ->
					routeBuilderRef(ref:"${routeClass.fullName}")
				}
				template(id:'producerTemplate')
			}
		}
    }

    def doWithApplicationContext = { applicationContext ->
    }

    def doWithWebDescriptor = { xml ->
    }

    def doWithDynamicMethods = { ctx ->
    	this.addMethods(application.controllerClasses,ctx);
    	this.addMethods(application.serviceClasses,ctx);
    }

    def onChange = { event ->
    	def artifactName = "${event.source.name}"
    	log.debug "Detected a change in ${artifactName}"
        if (artifactName.endsWith('Controller') || artifactName.endsWith('Service'))
        {
			def artifactType = (artifactName.endsWith('Controller')) ? 'controller' : 'service'
			log.debug "It's a ${artifactType}"
			def grailsClass = application."${artifactType}Classes".find { it.fullName == artifactName }
			this.addMethods([grailsClass],event.ctx)
		}
    }

    def onConfigChange = { event ->

    }

    private init() {
    	log.debug "Adding Groovy-ish methods to RouteBuilder Helpers"
    	ProcessorType.metaClass.filter = {filter ->
			if (filter instanceof Closure) {
				filter = CamelGroovyMethods.toExpression(filter)
			}
			delegate.filter(filter);
		}

		ChoiceType.metaClass.when = {filter ->
			if (filter instanceof Closure) {
				filter = CamelGroovyMethods.toExpression(filter)
			}
			delegate.when(filter);
		}

		ProcessorType.metaClass.process = {filter ->
			if (filter instanceof Closure) {
				filter = new ClosureProcessor(filter)
			}
			delegate.process(filter);
		}
	}


	private configureRouteBeans = { routeClazz ->
		def fullName = routeClazz.fullName

		"${fullName}RouteClass"(MethodInvokingFactoryBean) {
			targetObject = ref("grailsApplication",true)
			targetMethod = "getArtefact"
			arguments = [RouteArtefactHandler.TYPE, fullName]
		}

		"${fullName}"(GrailsRouteBuilderFactoryBean)
		{
			routeClass = ref("${fullName}RouteClass")
		}
	}

	private addMethods(artifacts,ctx) {
		log.debug "Adding dynamic methods to ${artifacts}"
		def template = ctx.getBean('producerTemplate')

        artifacts.each { artifact ->
        	artifact.metaClass.sendMessage = { endpoint,message ->
        		template.sendBody(endpoint,message)
			}
			artifact.metaClass.requestMessage = { endpoint,message ->
        		template.requestBody(endpoint,message)
			}
		}
	}
}
