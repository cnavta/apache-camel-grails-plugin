package org.ix.grails.plugins.camel;

import org.codehaus.groovy.grails.commons.AbstractInjectableGrailsClass;
import groovy.lang.Closure;

/**
 * Created by IntelliJ IDEA.
 * User: navtach
 * Date: Mar 16, 2009
 * Time: 3:51:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultGrailsRouteClass  extends AbstractInjectableGrailsClass implements GrailsRouteClass, GrailsRouteClassProperty
{
	public DefaultGrailsRouteClass(Class clazz)
	{
		super(clazz, ROUTE);
	}

	public DefaultGrailsRouteClass(Class clazz, String trailingName) {
		super(clazz, trailingName);
	}

	@Override
	public Closure getConfiguration() {
		return (Closure) getMetaClass().getProperty(this.getReference().getWrappedInstance(),CONFIGURE);
	}
}
