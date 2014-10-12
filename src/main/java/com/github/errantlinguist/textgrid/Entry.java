package com.github.errantlinguist.textgrid;

import com.github.errantlinguist.time.DoubleDuration;
import com.github.errantlinguist.time.Durative;

/**
 * A single entry in the TextGrid file, representing either an interval or a
 * point on a tier.
 * 
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * @version 2014-02-06
 * @since 2011-04-15
 *
 * @param <D>
 *            The object type representing the entry data.
 * 
 */
public interface Entry<D> extends Durative<Double> {

	/**
	 * @return The annotation data the entry represents.
	 */
	D getData();

	/**
	 * @return The duration of the entry.
	 */
	@Override
	DoubleDuration getDuration();

}
