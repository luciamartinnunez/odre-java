package odre.functions;

import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;

public class Lt extends FunctionBase2{

	@Override
	public NodeValue exec(NodeValue v1, NodeValue v2) {
		boolean isLessOrEq = false;
		try {			 
			isLessOrEq =  NodeValue.compare(v1, v2) < 0 ;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return NodeValue.makeBoolean(isLessOrEq);
	}

}
