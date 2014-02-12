package com.github.errantlinguist.textgrid;

import com.github.errantlinguist.time.DoubleDuration;
import com.github.errantlinguist.time.Durative;

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