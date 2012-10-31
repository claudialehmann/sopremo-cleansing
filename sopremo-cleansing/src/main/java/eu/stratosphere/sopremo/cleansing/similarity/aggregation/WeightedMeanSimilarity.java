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

package eu.stratosphere.sopremo.cleansing.similarity.aggregation;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleIterator;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;

import java.util.Arrays;

import eu.stratosphere.sopremo.EvaluationContext;
import eu.stratosphere.sopremo.cleansing.similarity.Similarity;

/**
 * <code>HarmonicMeanSimilarity</code> calculates the similarities of each collected Similarity and returns the weighted
 * harmonic mean of them.
 * 
 * @author Arvid Heise
 */
public class WeightedMeanSimilarity extends AggregationSimilarity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8627665157209879569L;

	private DoubleArrayList weights = new DoubleArrayList();

	public final static double ARITHMETIC_MEAN = 1, HARMONIC_MEAN = -1, GEOMETRIC_MEAN = 0.001, QUADRATIC_MEAN = 2,
			MAXIMUM = 1000, MINIMUM = -1000;

	private double m = ARITHMETIC_MEAN, weightSum = 0;

	public double getM() {
		return this.m;
	}

	public void setM(double m) {
		this.m = m;
	}

	/**
	 * Initializes WeightedMeanSimilarity.
	 */
	public WeightedMeanSimilarity() {
	}

	public WeightedMeanSimilarity(Similarity<?>... similarities) {
		super(similarities);
		this.weights.size(similarities.length);
		Arrays.fill(this.weights.elements(), 1);
		this.weightSum = similarities.length;
	}

	public WeightedMeanSimilarity(Object2DoubleMap<Similarity<?>> similarities) {
		super(similarities.keySet());
		final DoubleIterator weightIter = similarities.values().iterator();
		while (weightIter.hasNext()) {
			final double weight = weightIter.nextDouble();
			this.weights.add(weight);
			this.weightSum += weight;
		}
	}

	/**
	 * Adds a {@link Similarity} with a weight of 1.
	 * 
	 * @param similarity
	 *        The <code>Similarity</code> that shall be added.
	 */
	@Override
	public void add(Similarity<?> similarity) {
		this.add(similarity, 1);
	}

	/**
	 * Adds a {@link Similarity} with the given weight.
	 * 
	 * @param similarity
	 *        The <code>Similarity</code> that shall be added.
	 * @param weight
	 *        The weight of the similarity
	 */
	public void add(Similarity<?> similarity, double weight) {
		super.add(similarity);
		this.weights.add(weight);
		this.weightSum += weight;
	}

	/*
	 * (non-Javadoc)
	 * @see eu.stratosphere.sopremo.cleansing.similarity.aggregation.AggregationSimilarity#aggregateSimilarity(double[],
	 * eu.stratosphere.sopremo.EvaluationContext)
	 */
	@Override
	protected double aggregateSimilarity(double[] individualSimilarities, EvaluationContext context) {
		double mean = 0;
		for (int index = 0; index < individualSimilarities.length; index++)
			mean += Math.pow(individualSimilarities[index] * this.weights.get(index), this.m);
		return Math.pow(mean / this.weightSum, 1 / this.m);
	}
}
