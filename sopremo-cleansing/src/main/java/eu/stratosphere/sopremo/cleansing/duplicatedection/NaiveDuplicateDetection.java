package eu.stratosphere.sopremo.cleansing.duplicatedection;

import java.util.List;

import eu.stratosphere.nephele.configuration.Configuration;
import eu.stratosphere.sopremo.EvaluationContext;
import eu.stratosphere.sopremo.operator.InputCardinality;
import eu.stratosphere.sopremo.operator.Operator;
import eu.stratosphere.sopremo.operator.OutputCardinality;
import eu.stratosphere.sopremo.pact.JsonCollector;
import eu.stratosphere.sopremo.pact.SopremoCross;
import eu.stratosphere.sopremo.type.IJsonNode;

@InputCardinality(min = 1, max = 2)
@OutputCardinality(1)
public class NaiveDuplicateDetection extends CompositeDuplicateDetectionAlgorithm<NaiveDuplicateDetection> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6613384360765672866L;

	/*
	 * (non-Javadoc)
	 * @see
	 * eu.stratosphere.sopremo.cleansing.duplicatedection.CompositeDuplicateDetectionAlgorithm#addDuplicateImplementation
	 * (java.util.List, eu.stratosphere.sopremo.cleansing.duplicatedection.CandidateComparison,
	 * eu.stratosphere.sopremo.EvaluationContext)
	 */
	@Override
	protected Operator<?> getImplementation(List<Operator<?>> inputs, CandidateComparison comparison,
			EvaluationContext context) {
		return new DirectNaiveDuplicateDetection().withComparison(comparison).withInputs(inputs);
	}

	@InputCardinality(2)
	public static class DirectNaiveDuplicateDetection extends
			ElementaryDuplicateDetectionAlgorithm<DirectNaiveDuplicateDetection> {
		/**
		 * 
		 */
		private static final long serialVersionUID = -6773714594681291683L;

		public static class Implementation extends SopremoCross {
			private CandidateComparison comparison;

			/*
			 * (non-Javadoc)
			 * @see
			 * eu.stratosphere.sopremo.pact.SopremoCross#open(eu.stratosphere
			 * .nephele.configuration.Configuration)
			 */
			@Override
			public void open(Configuration parameters) throws Exception {
				super.open(parameters);
				this.comparison.setContext(getContext());
			}

			@Override
			protected void cross(IJsonNode left, IJsonNode right, JsonCollector collector) {
				this.comparison.process(left, right, collector);
			}
		}
	}
}
