package odre.casts;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase1;

public class DateTime extends FunctionBase1 {

	private SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss.SSS");

	
	@Override
	public NodeValue exec(NodeValue v) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		
		System.out.println("DateCast.class");
		try {
			System.out.println("DateCast.class + " + v.getString()+" "+toXsdDateTime(v.getString()));
			LocalDate parsedDate = LocalDate.parse(v.getString());
			String cleanValue = formatter.format(parsedDate);
			System.out.println("DateCast.class * " + cleanValue);
			return NodeValue.makeDateTime(cleanValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String toXsdDateTime(String date) {
		StringBuilder buff = new StringBuilder();

		buff.append(format1.format(date));
		buff.append('T').append(format2.format(date));
		return buff.append('Z').toString();
	}

}
