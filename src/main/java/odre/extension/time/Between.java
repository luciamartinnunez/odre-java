package odre.extension.time;

import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Between extends FunctionBase2 {

	@Override
	public NodeValue exec(NodeValue v1, NodeValue v2) {
		String s1 = v1.asString();
		String s2 = v2.asString();
		return NodeValue.makeBoolean(isCurrentTimeWithinRange(s1,s2));

	}

	private static boolean isCurrentTimeWithinRange(String startTimeString, String endTimeString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalTime startTime = LocalTime.parse(startTimeString, formatter);
		LocalTime endTime = LocalTime.parse(endTimeString, formatter);
		LocalTime currentTime = LocalTime.now();

		if (startTime.isAfter(endTime)) {
			return !currentTime.isBefore(startTime) || !currentTime.isAfter(endTime);
		} else {
			return !currentTime.isBefore(startTime) && !currentTime.isAfter(endTime);
		}
	}

}
