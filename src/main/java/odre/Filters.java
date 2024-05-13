package odre;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;

class Filters {

	// Restriction
	private static final String RESTRICTIONS_QUERY_ARG = "rule_id";
	private static final String RESTRICTIONS_QUERY_STRING = "SELECT DISTINCT ?rule_id WHERE {?policy ?rule_relation  ?rule_id . VALUES ?rule_relation {   <http://www.w3.org/ns/odrl/2/permission>   <http://www.w3.org/ns/odrl/2/obligation>   <http://www.w3.org/ns/odrl/2/prohibition> } }";
	private static final Query RESTRICTIONS_QUERY = QueryFactory.create(RESTRICTIONS_QUERY_STRING);

	// Constraints
	private static final String CONSTRAINTS_QUERY_REPLACEMENT = "#RULE_ID#";
	private static final String RESTRICTIONS_QUERY_ARG_OPERATOR = "operator";
	private static final String RESTRICTIONS_QUERY_ARG_LEFTOPERAND = "left_operand";
	private static final String RESTRICTIONS_QUERY_ARG_RIGHTOPERAND = "right_operand";
	private static final String CONSTRAINTS_QUERY_STRING = "SELECT DISTINCT ?operator ?left_operand ?right_operand WHERE { #RULE_ID# <http://www.w3.org/ns/odrl/2/operator>  ?operator;  <http://www.w3.org/ns/odrl/2/leftOperand> ?left_operand ;  <http://www.w3.org/ns/odrl/2/rightOperand> ?right_operand . }";

	// Actions
	private static final String QUERY_ACTION = "SELECT DISTINCT ?action WHERE { #RULE_ID# <http://www.w3.org/ns/odrl/2/action>  ?action . }";

	
	private Filters() {
		// empty
	}
	
	public static List<RDFNode> restrictions(Model model) {
		List<RDFNode> restrictions = new ArrayList<>();
	    QueryExecution qe = QueryExecutionFactory.create(RESTRICTIONS_QUERY, model);
	    ResultSet rs = qe.execSelect();
	    // Gather restrictions
	    while(rs.hasNext()){
	        RDFNode retriction = rs.nextSolution().get(RESTRICTIONS_QUERY_ARG); 
	        restrictions.add(retriction);
	    }
	    qe.close(); 
	    return restrictions;
	}
	
	public static List<RDFNode[]> constraints(Model model, RDFNode restriction) throws EnforceException {
		List<RDFNode[]> restrictions = new ArrayList<>();
		// Instantiate CONSTRAINTS QUEY
		String constraintsQueryInstantiated = CONSTRAINTS_QUERY_STRING.replace(CONSTRAINTS_QUERY_REPLACEMENT, restriction.toString());
		
		Query constraintsQuery = QueryFactory.create(constraintsQueryInstantiated);
	    QueryExecution qe = QueryExecutionFactory.create(constraintsQuery, model);
	    ResultSet rs = qe.execSelect();
	    // Gather constraints
	    while(rs.hasNext()){
	    	QuerySolution querySolution = rs.nextSolution();
	        RDFNode operator = querySolution.get(RESTRICTIONS_QUERY_ARG_OPERATOR); 
	        RDFNode leftOperand = querySolution.get(RESTRICTIONS_QUERY_ARG_LEFTOPERAND); 
	        RDFNode rightOperand = querySolution.get(RESTRICTIONS_QUERY_ARG_RIGHTOPERAND); 

	        restrictions.add(new RDFNode[]{operator, leftOperand, rightOperand});
	    }
	    qe.close(); 
	    if(restrictions.isEmpty())
	    	throw EnforceException.create(model);
	    return restrictions;
	}
}
