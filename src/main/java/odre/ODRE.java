package odre;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.riot.RDFParser;
import org.tinylog.Logger;



public class ODRE {

	private ODRE() {
		// empty
	}
	
	private static Lang policySerialization = Lang.JSONLD11;
	private static TemplatingEngine engine = new FreemarkerEngine();
	
	
	public static void setTemplatingEngine(TemplatingEngine engine) {
		ODRE.engine = engine;
	}
	
	public static void setPolicySerialization(String serialization) {
		String cleanSerialization = serialization.toLowerCase();
		policySerialization = RDFLanguages.nameToLang(cleanSerialization);
	}
	
	private static Model parsePolicy(String policy) {
		Model model = ModelFactory.createDefaultModel();
		RDFParser.source(new ByteArrayInputStream(policy.getBytes()))
	    .forceLang(policySerialization)
	    .parse(model);
		return model;
	}
	
	
	public static Map<String,String> enforce(String policy, Map<String, Object> interpolation) throws EnforceException {
		Map<String,String> usageDecision = new HashMap<>();
		// Simplify policy to A level
		policy = engine.reduce(policy, interpolation, interpolation);
		// Enforce policy
		Model policyA = parsePolicy(policy);
		List<RDFNode> restrictions = Filters.restrictions(policyA);
		for(int index=0; index < restrictions.size(); index++) {
			RDFNode restriction = restrictions.get(index);
			List<RDFNode[]> constraints = Filters.constraints(policyA, restriction);
			String interpretablePolicy = Interpreter.transformPolicy(constraints);
			Logger.info("interpretable policy: {} ", interpretablePolicy);
			boolean enforceDecision = (boolean) Interpreter.evaluate(interpretablePolicy);
			Logger.info("enforcement decision: {} ", enforceDecision);
			if(enforceDecision) {
				String action = Filters.action(policyA, restriction);
				Logger.info("action: {} ", enforceDecision);
				String actionOutput = action;
				if(Extensions.isSupported(action)) {
					String interpretableAction = Interpreter.transformAction(action);
					Logger.info("interpretable action: "+interpretableAction);
					actionOutput = Interpreter.evaluate(interpretableAction).toString();
				}
				usageDecision.put(action, actionOutput);
				Logger.info("action decision: {} ", actionOutput);

			}
		}
		
		return usageDecision;	
	}
	
}
