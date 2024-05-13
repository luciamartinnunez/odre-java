package odre.casts;

import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase1;

public class Date extends FunctionBase1 {

	@Override
	public NodeValue exec(NodeValue v) {
		System.out.println("DateCast.class");
		try {
			System.out.println("DateCast.class" + v);
			return NodeValue.makeDateTime(v.asString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
