package odre;

public class Filters {

	private Filters() {
		//empty
	}
	
	private static final String QUERY_RESTRICTIONS = "SELECT DISTINCT ?rule_id WHERE {?policy ?rule_relation  ?rule_id . VALUES ?rule_relation {   <http://www.w3.org/ns/odrl/2/permission>   <http://www.w3.org/ns/odrl/2/obligation>   <http://www.w3.org/ns/odrl/2/prohibition> } .";
	private static final String TOKEN_CONSTRAINT = "#RULE_ID#";
	private static final String QUERY_CONSTRAINTS = "SELECT DISTINCT ?operator ?left_operand ?right_operand WHERE { #RULE_ID# <http://www.w3.org/ns/odrl/2/operator>  ?operator;  <http://www.w3.org/ns/odrl/2/leftOperand> ?left_operand ;  <http://www.w3.org/ns/odrl/2/rightOperand> ?right_operand .";
	private static final String QUERY_ACTION = "SELECT DISTINCT ?action WHERE { #RULE_ID# <http://www.w3.org/ns/odrl/2/action>  ?action . }";

	public static restrictions(Model model) {
		
	}
}
