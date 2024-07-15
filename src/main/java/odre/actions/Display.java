package odre.actions;

import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase0;

public class Display extends FunctionBase0 {

	@Override
	public NodeValue exec() {
		System.out.println("Policy enforcement output true, this is the output of performing odrl:display action");
		return NodeValue.makeBoolean(true);
	}

}
