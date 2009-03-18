package org.ix.grails.plugins.camel;

import org.springframework.beans.factory.FactoryBean;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: navtach
 * Date: Mar 16, 2009
 * Time: 5:22:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class GrailsRouteBuilderFactoryBean implements FactoryBean {

	private GrailsRouteClass routeClass;

	private static final Logger log = Logger.getLogger(GrailsRouteBuilderFactoryBean.class);

	@Override
	public Object getObject() throws Exception {
		log.debug("RouteClass: " + routeClass);
		return new GrailsRouteBuilder(routeClass.getConfiguration());
	}

	@Override
	public Class getObjectType() {
		return GrailsRouteBuilder.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	public void setRouteClass(GrailsRouteClass routeClass) {
		log.debug("Hit GrailsRouteBuilderFactoryBean.setRouteClass");
		this.routeClass = routeClass;
	}
}
