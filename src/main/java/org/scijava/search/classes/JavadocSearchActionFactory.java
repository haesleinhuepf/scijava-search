/*-
 * #%L
 * Search framework for SciJava applications.
 * %%
 * Copyright (C) 2017 - 2018 SciJava developers.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package org.scijava.search.classes;

import java.io.IOException;
import java.net.URL;

import org.scijava.log.LogService;
import org.scijava.platform.PlatformService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.search.DefaultSearchAction;
import org.scijava.search.SearchAction;
import org.scijava.search.SearchActionFactory;
import org.scijava.search.SearchResult;
import org.scijava.search.javadoc.JavadocService;
import org.scijava.ui.DialogPrompt.MessageType;
import org.scijava.ui.UIService;

/**
 * Search action for viewing the javadoc of a Java class.
 *
 * @author Curtis Rueden
 */
@Plugin(type = SearchActionFactory.class)
public class JavadocSearchActionFactory implements SearchActionFactory {

	@Parameter
	private LogService log;

	@Parameter
	private UIService uiService;

	@Parameter
	private PlatformService platformService;

	@Parameter
	private JavadocService javadocService;

	@Override
	public boolean supports(final SearchResult result) {
		return result instanceof ClassSearchResult;
	}

	@Override
	public SearchAction create(final SearchResult result) {
		return new DefaultSearchAction("Javadoc", () -> javadoc(result));
	}

	private void javadoc(final SearchResult result) {
		final String javadocURL = javadocURL(result);
		if (javadocURL == null) {
			uiService.showDialog("Could not discern javadoc URL for class: " +
				((ClassSearchResult) result).clazz(), "Javadoc Search",
				MessageType.ERROR_MESSAGE);
			return;
		}
		try {
			platformService.open(new URL(javadocURL));
		}
		catch (final IOException exc) {
			log.error(exc);
			uiService.showDialog("Error opening javadoc URL: " + javadocURL,
				"Javadoc Search", MessageType.ERROR_MESSAGE);
		}
	}

	private String javadocURL(final SearchResult result) {
		return javadocService.url(((ClassSearchResult) result).clazz());
	}
}
