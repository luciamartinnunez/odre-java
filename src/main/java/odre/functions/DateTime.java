package odre.functions;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase0;



public class DateTime extends FunctionBase0 {

	private SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss.SSS");


	@Override
	public NodeValue exec() {
		String value = toXsdDateTime();
		NodeValue v = NodeValue.makeDateTime(value);
		System.out.println("DateTime.class > "+ v);
		return v;

	}

	private String toXsdDateTime() {
		StringBuilder buff = new StringBuilder();
		Date date = new Date();
		buff.append(format1.format(date));
		buff.append('T').append(format2.format(date));
		return buff.append('Z').toString();
	}
}
