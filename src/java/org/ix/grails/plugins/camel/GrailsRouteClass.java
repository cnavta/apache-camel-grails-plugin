package org.ix.grails.plugins.camel;

import org.codehaus.groovy.grails.commons.InjectableGrailsClass;
import groovy.lang.Closure;

/**
 * Created by IntelliJ IDEA.
 * User: navtach
 * Date: Mar 16, 2009
 * Time: 3:51:13 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GrailsRouteClass extends InjectableGrailsClass {
	Closure getConfiguration();
}
