package odre;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.sparql.function.FunctionRegistry;
import org.apache.jena.util.PrintUtil;

class Interpreter {

	// Transform
	private static final String TRANSFORM_JOIN = " && ";
	private static final String BIND_ARG = "?bind";
	static {
//		PrintUtil.registerPrefix("odrl", "http://www.w3.org/ns/odrl/2/");
		PrintUtil.removePrefix("xsd");//("xsd", "http://www.w3.org/2001/XMLSchema#");
	}

	public static String transform(List<RDFNode[]> constraints) throws EnforceException {
		List<String> triplePatterns = new ArrayList<>();
		for(int index=0; index < constraints.size(); index++) {
			RDFNode[] constraint = constraints.get(index);
			String triplePattern = transformConstraint(constraint[0], constraint[1], constraint[2]);
			triplePatterns.add(triplePattern);
		}
		String bindOperators = String.join(TRANSFORM_JOIN, triplePatterns);
		return Wrapper.wrapQuery(bindOperators);
	}

	private static String transformConstraint(RDFNode operator, RDFNode leftOperand, RDFNode rightOperand) throws EnforceException {
		String operatorNode = transformNode(operator, false);
		String leftOperandNode = transformNode(leftOperand, true);
		String rightOperandNode = transformNode(rightOperand, true);
		
		Functions.checkSupport(operatorNode);
		Functions.checkSupport(leftOperandNode);
		Functions.checkSupport(rightOperandNode);
		
		return Wrapper.wrapConstraint(operatorNode, leftOperandNode, rightOperandNode);

	}

	private static String transformNode(RDFNode operand, boolean isOperand) {
		String node = null;
		if (operand.isLiteral()) {
			Literal literalOperand = operand.asLiteral();
			Resource datatype = ResourceFactory.createResource(literalOperand.getDatatype().getURI());
			String prettyDatatype = Wrapper.wrapURINode(datatype, false);
			
			node = Wrapper.wrapLiteralNode(prettyDatatype, literalOperand.getString());
		} else {
			node = Wrapper.wrapURINode(operand, isOperand);
		}
		return node;
	}

	public static boolean evaluate(String interpretablePolicy) {
		Model model = ModelFactory.createDefaultModel();
		QueryExecution qe = QueryExecutionFactory.create(interpretablePolicy, model);
		ResultSet rs = qe.execSelect();
		// Gather enforcement result
		boolean enforced = false;
		while (rs.hasNext()) {
			RDFNode retriction = rs.nextSolution().get(BIND_ARG);
			if(retriction!=null)
				enforced = retriction.asLiteral().getBoolean();
		}
		qe.close();
		return enforced;
	}

	private class Wrapper {

		// Transform constraints
		private static final String TRANSFORM_CONSTRAINT_CONSTANT_1 = "(";
		private static final String TRANSFORM_CONSTRAINT_CONSTANT_2 = ", ";
		private static final String TRANSFORM_CONSTRAINT_CONSTANT_3 = ")";
		private static final String TRANSFORM_CONSTRAINT_CONSTANT_4 = "\"";
		private static final String TRANSFORM_CONSTRAINT_CONSTANT_5 = "()";
		// Wrap query
		private static final String QUERY_WRAP_PREAMBLE = "SELECT ?bind { BIND (";
		private static final String QUERY_WRAP_POSTAMBLE = " AS ?bind) }";

		private static String wrapConstraint(String operator, String leftOperand, String rightOperand) {
			StringBuilder queryTriplePattern = new StringBuilder();
			queryTriplePattern.append(operator).append(TRANSFORM_CONSTRAINT_CONSTANT_1);
			queryTriplePattern.append(leftOperand).append(TRANSFORM_CONSTRAINT_CONSTANT_2);
			queryTriplePattern.append(rightOperand).append(TRANSFORM_CONSTRAINT_CONSTANT_3);
			return queryTriplePattern.toString();
		}

		public static String wrapURINode(RDFNode node, boolean isOperand) {
			StringBuilder wrappedNode = new StringBuilder(PrintUtil.print(node)); // Print.print(node)
			if (isOperand)
				wrappedNode.append(TRANSFORM_CONSTRAINT_CONSTANT_5);
			return  wrappedNode.toString();
		}

		private static String wrapLiteralNode(String datatype, String value) {
			StringBuilder operandStr = new StringBuilder();
			// Wraps for casting
			operandStr.append(datatype).append(TRANSFORM_CONSTRAINT_CONSTANT_1);
			operandStr.append(TRANSFORM_CONSTRAINT_CONSTANT_4).append(value);
			operandStr.append(TRANSFORM_CONSTRAINT_CONSTANT_4).append(TRANSFORM_CONSTRAINT_CONSTANT_3);
			return operandStr.toString();
		}

		private static String wrapQuery(String bindOperators) {
			StringBuilder bindPattern = new StringBuilder();
			bindPattern.append(QUERY_WRAP_PREAMBLE).append(bindOperators).append(QUERY_WRAP_POSTAMBLE);
			return bindPattern.toString();
		}

	}
}
