package odre.functions;

import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;

public class Lteq extends FunctionBase2{

	@Override
	public NodeValue exec(NodeValue v1, NodeValue v2) {
		System.out.println("Lteq class: "+v1+" "+v2);
		boolean isLessOrEq = false;
		try {			 
			isLessOrEq =  NodeValue.compare(v1, v2) <= 0 ;
			System.out.println("Lteq class: >"+isLessOrEq);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return NodeValue.makeBoolean(isLessOrEq);
	}

}
