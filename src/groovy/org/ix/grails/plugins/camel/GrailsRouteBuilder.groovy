package org.ix.grails.plugins.camel

import org.apache.camel.builder.RouteBuilder

/**
 * Created by IntelliJ IDEA.
 * User: navtach
 * Date: Mar 13, 2009
 * Time: 12:59:56 PM
 * To change this template use File | Settings | File Templates.
 */

public class GrailsRouteBuilder extends RouteBuilder {

	def confClosure

	public GrailsRouteBuilder(Closure confClosure) {
		this.confClosure = confClosure
	}

	public void configure() {
		this.confClosure.delegate = this
		this.confClosure()
	}
}