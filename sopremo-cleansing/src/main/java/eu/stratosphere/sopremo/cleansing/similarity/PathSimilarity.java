/***********************************************************************************************************************
 *
 * Copyright (C) 2010 by the Stratosphere project (http://stratosphere.eu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 **********************************************************************************************************************/
package eu.stratosphere.sopremo.cleansing.similarity;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import eu.stratosphere.sopremo.EvaluationContext;
import eu.stratosphere.sopremo.expressions.CachingExpression;
import eu.stratosphere.sopremo.expressions.EvaluationExpression;
import eu.stratosphere.sopremo.type.IJsonNode;

/**
 * @author Arvid Heise
 */
public class PathSimilarity<NodeType extends IJsonNode> extends AbstractSimilarity<IJsonNode> implements
		CompoundSimilarity<IJsonNode, Similarity<NodeType>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3168384409276920868L;

	private final CachingExpression<NodeType> leftExpression, rightExpression;

	private final Similarity<NodeType> actualSimilarity;

	public PathSimilarity(EvaluationExpression leftExpression, Similarity<NodeType> actualSimilarity,
			EvaluationExpression rightExpression) {
		this.leftExpression = CachingExpression.of(leftExpression, actualSimilarity.getExpectedType());
		this.actualSimilarity = actualSimilarity;
		this.rightExpression = CachingExpression.of(rightExpression, actualSimilarity.getExpectedType());
	}

	/*
	 * (non-Javadoc)
	 * @see eu.stratosphere.sopremo.cleansing.similarity.Similarity#getExpectedType()
	 */
	@Override
	public Class<IJsonNode> getExpectedType() {
		return IJsonNode.class;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Similarity<NodeType>> iterator() {
		return Collections.singleton(this.actualSimilarity).iterator();
	}

	/*
	 * (non-Javadoc)
	 * @see eu.stratosphere.sopremo.cleansing.similarity.CompoundSimilarity#getSubsimilarities()
	 */
	@Override
	public List<Similarity<NodeType>> getSubsimilarities() {
		return Collections.singletonList(this.actualSimilarity);
	}

	/**
	 * Returns the leftExpression.
	 * 
	 * @return the leftExpression
	 */
	public CachingExpression<NodeType> getLeftExpression() {
		return this.leftExpression;
	}

	/**
	 * Returns the rightExpression.
	 * 
	 * @return the rightExpression
	 */
	public CachingExpression<NodeType> getRightExpression() {
		return this.rightExpression;
	}

	/**
	 * Returns the actualSimilarity.
	 * 
	 * @return the actualSimilarity
	 */
	public Similarity<NodeType> getActualSimilarity() {
		return this.actualSimilarity;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * eu.stratosphere.sopremo.cleansing.similarity.Similarity#getSimilarity(eu.stratosphere.sopremo.type.IJsonNode,
	 * eu.stratosphere.sopremo.type.IJsonNode)
	 */
	@Override
	public double getSimilarity(IJsonNode node1, IJsonNode node2, EvaluationContext context) {
		final NodeType left = this.leftExpression.evaluate(node1, context);
		if (left.isMissing())
			return 0;
		// two missing nodes are still not similar in contrast to two null nodes
		final NodeType right = this.rightExpression.evaluate(node2, context);
		if (right.isMissing())
			return 0;
		return this.actualSimilarity.getSimilarity(left, right, context);
	}
}
