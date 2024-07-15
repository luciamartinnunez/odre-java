package odre;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFFormat;

public class EnforceException extends Exception {

	public EnforceException(String string) {
		super(string);
	}

	private static final long serialVersionUID = 1L;
	private static final String EXCEPTION_MSG = "Policy contains no constraints to evaluate, those contained are not correctly expressed, or they do not belong to a valid namespace. Have a peek to the policy:\n";
	
	public static EnforceException create(Model model) {
		StringBuilder msg = new StringBuilder(EXCEPTION_MSG);
		StringWriter writer = new StringWriter();
		model.write(writer, RDFFormat.TURTLE.toString());
		msg.append(writer.toString());
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new EnforceException(msg.toString());
		
	}

}
