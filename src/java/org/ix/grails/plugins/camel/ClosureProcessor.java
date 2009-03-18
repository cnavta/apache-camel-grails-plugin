package org.ix.grails.plugins.camel;

import org.apache.camel.Processor;
import org.apache.camel.Exchange;
import groovy.lang.Closure;

/**
 * Created by IntelliJ IDEA.
 * User: navtach
 * Date: Mar 13, 2009
 * Time: 4:34:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClosureProcessor implements Processor {

	private Closure target;

	public ClosureProcessor(Closure target) {
		this.target = target;
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		this.target.call(exchange);
	}
}
