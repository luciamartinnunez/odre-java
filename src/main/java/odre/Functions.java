package odre;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.sparql.function.FunctionRegistry;

import odre.functions.Date;


class Functions {

	static {
		registerExtension("http://www.w3.org/ns/odrl/2/date", Date.class);
	}
	
	private Functions() {
		super();
	}
		
	public static void registerExtension(String uri, Class<?> clazz) {	
		FunctionRegistry.get().put(uri, clazz);
	}
	
	protected static void checkSupport(String uri) throws EnforceException {
		String cleanURI = uri;
		if (uri.startsWith("<") && uri.contains(">"))
			cleanURI = uri.substring(1,uri.lastIndexOf(">"));
		Iterator<String> registedFunctions = FunctionRegistry.get().keys();
		boolean exists =  false;
		while(registedFunctions.hasNext()) {
			String registedFunction = registedFunctions.next();
			exists = registedFunction.equals(cleanURI);
			if (exists)
				break;
		}
		System.out.println("exists: "+cleanURI+" "+FunctionRegistry.get().isRegistered(cleanURI)+" "+exists);
		if (!FunctionRegistry.get().isRegistered(cleanURI))
			throw new EnforceException("Provided URI "+uri+" (operator or operand) is not supported by any function");
	}
}
