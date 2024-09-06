package odre.extension.time;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.jena.atlas.lib.Lib;
import org.apache.jena.sparql.ARQInternalErrorException;
import org.apache.jena.sparql.expr.ExprList;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase;
import org.apache.jena.vocabulary.XSD;

public class Time extends FunctionBase {

	private SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

	private String toXsdDateTime() {
		StringBuilder buff = new StringBuilder();
		Date date = new Date();
		buff.append(format.format(date));
		return buff.toString();
	}

	@Override
	public NodeValue exec(List<NodeValue> args) {
		NodeValue result = null;
		try {
			if (args != null && args.isEmpty()) {
				result = NodeValue.makeNode(toXsdDateTime(), null, XSD.time.toString());
			}else if(args!=null && args.size()==1) {
				result = NodeValue.makeNode(args.get(0).toString(), null, XSD.time.toString());
			}else {
				throw new UnsupportedOperationException("Too many arguments were provided to time");
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
