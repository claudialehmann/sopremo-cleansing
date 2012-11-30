/*
 * DuDe - The Duplicate Detection Toolkit
 * 
 * Copyright (C) 2010  Hasso-Plattner-Institut für Softwaresystemtechnik GmbH,
 *                     Potsdam, Germany 
 *
 * This file is part of DuDe.
 * 
 * DuDe is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DuDe is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DuDe.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package eu.stratosphere.sopremo.cleansing.similarity.text;

import uk.ac.shef.wit.simmetrics.similaritymetrics.NeedlemanWunch;

/**
 * <code>NeedlemanWunchDistanceSimilarity</code> compares two {@link IJsonNode} s based on the Needleman Wunch Distance
 * attribute.
 * 
 * @author Arvid Heise
 */
public class NeedlemanWunchSimilarity extends SimmetricsSimilarity<NeedlemanWunch> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4099957931347604677L;

	/**
	 * Initializes a <code>NeedlemanWunchDistanceSimilarity</code>.
	 */
	public NeedlemanWunchSimilarity() {
		super(new NeedlemanWunch());
	}
}