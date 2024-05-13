package odre;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.sparql.function.FunctionRegistry;

import odre.casts.CustomTemperature;

public class ODRE {

	private static Lang policySerialization = Lang.JSONLD11;
	
	public static void setPolicySerialization(String serialization) {
		String cleanSerialization = serialization.toLowerCase();
		policySerialization = RDFLanguages.nameToLang(cleanSerialization);
	}
	
	private static Model readPolicy(String policy) {
		Model model = ModelFactory.createDefaultModel();
		RDFParser.source(new ByteArrayInputStream(policy.getBytes()))
	    .forceLang(policySerialization)
	    .parse(model);
		return model;
	}
	

	
	
	public static UsageDecision enforce(String policy, Map<String, Object> interpolation) throws EnforceException {
		// Simplify policy to A level
		
		Model policyA = readPolicy(policy);
		// Enforce policy
		List<RDFNode> restrictions = Filters.restrictions(policyA);
		for(int index=0; index < restrictions.size(); index++) {
			RDFNode restriction = restrictions.get(index);
			List<RDFNode[]> constraints = Filters.constraints(policyA, restriction);
			String interpretablePolicy = Interpreter.transform(constraints);
			System.out.println("interpretable policy: "+interpretablePolicy);
			Boolean enforceDecision = Interpreter.evaluate(interpretablePolicy);
			System.out.println("usage decision: "+ enforceDecision);
			if(enforceDecision) {
				
			}
		}
		
		return null;	
	}
	
}
