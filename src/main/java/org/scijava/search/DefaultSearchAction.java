
package org.scijava.search;

/**
 * Default implementation of {@link SearchAction}.
 *
 * @author Curtis Rueden
 */
public class DefaultSearchAction implements SearchAction {

	private final String label;
	private final Runnable r;

	public DefaultSearchAction(final String label, final Runnable r) {
		this.label = label;
		this.r = r;
	}

	@Override
	public String toString() {
		return label;
	}

	@Override
	public void run() {
		r.run();
	}
}
