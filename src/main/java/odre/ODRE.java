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

public class ODRE {

	private Lang policySerialization = Lang.JSONLD11;
	private TemplatingEngine engine = new FreemarkerEngine();

	public ODRE() {
		// empty
		engine = new FreemarkerEngine();
		policySerialization = Lang.JSONLD11;

	}

	public void setTemplatingEngine(TemplatingEngine engine) {
		this.engine = engine;
	}

	public void setPolicySerialization(String serialization) {
		String cleanSerialization = serialization.toLowerCase();
		policySerialization = RDFLanguages.nameToLang(cleanSerialization);
	}

	private Model parsePolicy(String policy) throws EnforceException {
		Model model = ModelFactory.createDefaultModel();
		// JSON-LD to RDF
		policy = policy.replace("\"http://www.w3.org/ns/odrl.jsonld\"", Context.ODRL_CONTEXT);
		try {
			RDFParser.source(new ByteArrayInputStream(policy.getBytes())).forceLang(policySerialization).parse(model);
		} catch (Exception e) {
			throw new EnforceException(e.getMessage());
		}
		// model.write(System.out, "NT");
		return model;
	}

	public Map<String, String> enforce(String policy, Map<String, Object> interpolation) throws EnforceException {
		Map<String, String> usageDecision = new HashMap<>();
		// Simplify policy to A level
		policy = engine.reduce(policy, interpolation, interpolation);
		// Enforce policy
		Model policyA = parsePolicy(policy);
		List<RDFNode> restrictions = Filters.restrictions(policyA);
		for (int index = 0; index < restrictions.size(); index++) {
			RDFNode restriction = restrictions.get(index);
			List<RDFNode[]> constraints = Filters.constraints(policyA, restriction);
			String interpretablePolicy = Interpreter.transformPolicy(constraints);
			boolean enforceDecision = (boolean) Interpreter.evaluate(interpretablePolicy);
			if (enforceDecision) {
				String action = Filters.action(policyA, restriction);
				String actionOutput = action;
				if (Extensions.isSupported(action)) {
					String interpretableAction = Interpreter.transformAction(action);
					actionOutput = Interpreter.evaluate(interpretableAction).toString();
				}
				usageDecision.put(action, actionOutput);
			}
		}

		return usageDecision;
	}

}
