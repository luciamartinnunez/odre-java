package tests.odre.operators;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.ExprList;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.expr.nodevalue.NodeValueInteger;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import odre.operators.Logical;

public class TestLogical {

	private Logical logical = new Logical();
	private ExprList exp;
	private List<NodeValue> values = new ArrayList<>();
	private NodeValue value3 = new NodeValueInteger(3);
	private NodeValue value2 = new NodeValueInteger(2);
	private NodeValue value1 = new NodeValueInteger(1);

	@Before
	public void setup() {
		List<Expr> arguments = new ArrayList<>();
		Expr arg1 = value3;
		Expr arg2 = new NodeValueInteger(1);
		values.add(new NodeValueInteger(3));
		values.add(new NodeValueInteger(1));
		arguments.add(arg1);
		arguments.add(arg2);
		exp = ExprList.create(arguments);
		
	}
	
	
	
	@Test
	@DisplayName("Test case 01: operatior eq between two xsd:integers")
	public void test01() {
		String eq = logical.ODRL_EQ;
		logical.checkBuild(eq, exp);
		NodeValue result = logical.exec(values);
		System.out.println(result);
	}
}
