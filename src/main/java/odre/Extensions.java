package odre;

import org.apache.jena.sparql.function.FunctionRegistry;

import odre.actions.Display;
import odre.actions.Distribute;
import odre.operands.Date;
import odre.operands.DateTime;
import odre.operands.Time;
import odre.operators.Logical;


class Extensions {

	static {
		// Operators
		register("http://www.w3.org/ns/odrl/2/eq", Logical.class);
		register("http://www.w3.org/ns/odrl/2/nteq", Logical.class);
		register("http://www.w3.org/ns/odrl/2/gt", Logical.class);
		register("http://www.w3.org/ns/odrl/2/gteq", Logical.class);
		register("http://www.w3.org/ns/odrl/2/lt", Logical.class);
		register("http://www.w3.org/ns/odrl/2/lteq", Logical.class);
		// Operands
		register("http://www.w3.org/ns/odrl/2/dateTime", DateTime.class);
		// Actions
		register("http://www.w3.org/ns/odrl/2/display", Display.class);
		register("http://www.w3.org/ns/odrl/2/distribute", Distribute.class);
		// Extensions
		register("https://w3id.org/def/odre-time/date", Date.class);
		register("https://w3id.org/def/odre-time/time", Time.class);
		
	}
	
	private Extensions() {
		super();
	}
		
	public static void register(String uri, Class<?> clazz) {	
		FunctionRegistry.get().put(uri, clazz);
	}
	
	protected static void checkSupport(String uri) throws EnforceException {
		if (!isSupported(uri))
			throw new EnforceException("Provided URI "+uri+" (operator or operand) is not supported by any function");
	}
	
	private static final String URI_DELIMITER_OPEN = "<";
	private static final String URI_DELIMITER_CLOSE = ">";
	public static boolean isSupported(String uri) {
		String cleanURI = uri;
		if (uri.startsWith(URI_DELIMITER_OPEN) && uri.contains(URI_DELIMITER_CLOSE))
			cleanURI = uri.substring(1,uri.lastIndexOf(URI_DELIMITER_CLOSE));
		return FunctionRegistry.get().isRegistered(cleanURI);
	}
}
