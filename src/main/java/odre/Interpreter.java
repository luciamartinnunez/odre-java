package odre;

import java.util.ArrayList;
import java.util.List;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.util.PrintUtil;

class Interpreter {

	// Transform
	private static final String TRANSFORM_JOIN = " && ";
	private static final String BIND_ARG = "?bind";
	static {
		PrintUtil.removePrefix("xsd");
	}
	
	public static String transformAction(String action) {
		String interpretableAction = Wrapper.wrapURINode(ResourceFactory.createResource(action), true);
		return Wrapper.wrapQuery(interpretableAction);
	}

	public static String transformPolicy(List<RDFNode[]> constraints) throws EnforceException {
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
		
		Extensions.checkSupport(operatorNode);
		Extensions.checkSupport(leftOperandNode);
		Extensions.checkSupport(rightOperandNode);
		
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

	
	
	public static Object evaluate(String interpretablePolicy) {
		Model model = ModelFactory.createDefaultModel();
		QueryExecution qe = QueryExecutionFactory.create(interpretablePolicy, model);
		ResultSet rs = qe.execSelect();
		// Gather enforcement result
		Object enforced = null;
		while (rs.hasNext()) {
			RDFNode retriction = rs.nextSolution().get(BIND_ARG);
			if(retriction!=null)
				enforced = retriction.asLiteral().getValue();
		}
		qe.close();
		//TODO: si enforced es null lanzar excepción, lo mas probable es que un argumento de la politica sea incorrecto
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
