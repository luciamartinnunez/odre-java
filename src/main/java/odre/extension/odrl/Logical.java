package odre.extension.odrl;

import java.util.List;
import org.apache.jena.atlas.lib.Lib;
import org.apache.jena.query.QueryBuildException;
import org.apache.jena.sparql.ARQInternalErrorException;
import org.apache.jena.sparql.expr.ExprList;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase;

public class Logical extends FunctionBase {

	private String lastOperation;
	public static final String ODRL_EQ = "http://www.w3.org/ns/odrl/2/eq";
	public static final String ODRL_NTEQ = "http://www.w3.org/ns/odrl/2/nteq";
	public static final String ODRL_NEG = "http://www.w3.org/ns/odrl/2/neg"; // TODO: remove when issue
																				// https://github.com/w3c/poe/issues/311
	public static final String ODRL_GT = "http://www.w3.org/ns/odrl/2/gt";
	public static final String ODRL_GTEQ = "http://www.w3.org/ns/odrl/2/gteq";
	public static final String ODRL_LT = "http://www.w3.org/ns/odrl/2/lt";
	public static final String ODRL_LTEQ = "http://www.w3.org/ns/odrl/2/lteq";

	

	@Override
	public NodeValue exec(List<NodeValue> args) {
		boolean expression = false;
		try {
			int comparison = NodeValue.compare(args.get(0), args.get(1));
			if (isEq()) {
				expression = comparison == 0;
			} else if (isNteq()) {
				expression = comparison != 0;
			} else if (isGt()) {
				expression = comparison > 0;
			} else if (isGteq()) {
				expression = comparison >= 0;
			} else if (isLt()) {
				expression = comparison < 0;
			} else if (isLteq()) {
				expression = comparison <= 0;
			} else {
				throw new ARQInternalErrorException("Provided operand is not supported: " + lastOperation);
			}
		} catch (Exception e) {
			throw new UnsupportedOperationException(e.getMessage());
		}

		return NodeValue.makeBoolean(expression);
	}

	@Override
	public void checkBuild(String uri, ExprList args) {
		if (args.size() != 2)
			throw new QueryBuildException("Function '" + Lib.className(this)
					+ "' implements logical operators odrl:eq, odrl:nteq, odrl:lt, odrl:lteq, odrl:gt, ordl:gteq which take two arguments");
		lastOperation = uri;
	}

	private boolean isEq() {
		return lastOperation.equals(ODRL_EQ);
	}

	private boolean isNteq() {
		return lastOperation.equals(ODRL_NTEQ) || lastOperation.equals(ODRL_NEG); // TODO: remove when issue
																					// https://github.com/w3c/poe/issues/311
	}

	private boolean isGt() {
		return lastOperation.equals(ODRL_GT);
	}

	private boolean isGteq() {
		return lastOperation.equals(ODRL_GTEQ);
	}

	private boolean isLt() {
		return lastOperation.equals(ODRL_LT);
	}

	private boolean isLteq() {
		return lastOperation.equals(ODRL_LTEQ);
	}

}
