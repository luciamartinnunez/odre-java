package odre.functions;

import java.text.SimpleDateFormat;

import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase0;

public class Date extends FunctionBase0 {

	private SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss.SSS");

	@Override
	public NodeValue exec() {
		try {
			String value = toXsdDateTime();
			System.out.println(value);
			NodeValue v = NodeValue.makeDateTime(value);
			System.out.println("Date.class > " + v);
			return v;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	private String toXsdDateTime() {
		StringBuilder buff = new StringBuilder();
		java.util.Date date = new java.util.Date();
		buff.append(format1.format(date));
		// buff.append('T').append(format2.format(date)).append('Z');
		return buff.toString();
	}

}
