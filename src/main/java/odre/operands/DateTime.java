package odre.operands;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.jena.atlas.lib.Lib;
import org.apache.jena.sparql.ARQInternalErrorException;
import org.apache.jena.sparql.expr.ExprList;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase;
import org.apache.jena.vocabulary.XSD;



public class DateTime extends FunctionBase {

	private SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss.SSS");

	private String toXsdDateTime() {
		StringBuilder buff = new StringBuilder();
		Date date = new Date();
		buff.append(format1.format(date));
		buff.append('T').append(format2.format(date));
		return buff.append('Z').toString();
	}

	@Override
	public NodeValue exec(List<NodeValue> args) {
		NodeValue result = null;
		try {
			if (args != null && args.isEmpty()) {
				result = NodeValue.makeNode(toXsdDateTime(), null, XSD.dateTime.toString());
			}else if(args!=null && args.size()==1) {
				result = NodeValue.makeNode(args.get(0).toString(), null, XSD.dateTime.toString());
			}else {
				throw new UnsupportedOperationException("Too many arguments were provided to dateTime");
			}
		} catch (Exception e) {
			throw new ARQInternalErrorException(Lib.className(this) + ": "+e.getMessage());
		}
		return result;
	}

	@Override
	public void checkBuild(String uri, ExprList args) {
		// TODO: check args and validate them
		//System.out.println("uri>"+uri);
		//System.out.println("args>"+args);

	}
}
