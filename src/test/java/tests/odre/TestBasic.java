package tests.odre;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.AfterClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import odre.EnforceException;
import odre.ODRE;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

@TestMethodOrder(OrderAnnotation.class)
public class TestBasic {

	private static StringBuilder dir = new StringBuilder("./src/test/resources/basic-experiments/");
	private static final String EXPECTED_ACTION = "http://www.w3.org/ns/odrl/2/read";
	private static int numberOfExecutions = 10;
	
	private static Map<String, List<Double>> runningTimeResults = new HashMap<>();
	private static String fileResults = "./results.csv";

	private static boolean experimentalMode = true;
	private static boolean warmup = true;
	
	private static String readPolicy(String name) {
		
		StringBuilder contentBuilder = new StringBuilder();
		try (Stream<String> stream = Files.lines(Paths.get(dir + name), StandardCharsets.UTF_8)) {
			stream.forEach(s -> contentBuilder.append(s).append("\n"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return contentBuilder.toString();
	}

	private Map<String, String> enforce(String policy, String name, Map<String, Object> interpolations)
			throws EnforceException {
		ODRE odre = new ODRE();
		name  = "test_"+name;
		Map<String, String> usage = null;
		if (experimentalMode) {
			for (int index = 0; index < numberOfExecutions; index++) {
				long start = System.currentTimeMillis();
				usage = odre.enforce(policy, interpolations);
				double finish = (System.currentTimeMillis() - start) * 0.001;
				List<Double> results = runningTimeResults.get(name);
				if (results == null)
					results = new ArrayList<>();
				results.add(finish);
				runningTimeResults.put(name, results);
				if(warmup) {
					//skip first result
					index -= 1;
					warmup = false;
					results.remove(0);
				}
			}
		} else {
			usage = odre.enforce(policy, interpolations);
		}
		writeCSV();
		return usage;
	}

	@AfterClass
	public static void writeCSV() {
		File r = (new File(fileResults));
		if(r.exists())
			r.delete();
		int numRows = runningTimeResults.values().iterator().next().size();

		try (FileWriter csvWriter = new FileWriter(fileResults)) {
			for (String key : runningTimeResults.keySet()) {
				csvWriter.append(key).append(",");
			}
			csvWriter.append("\n");

			for (int i = 0; i < numRows; i++) {
				for (String key : runningTimeResults.keySet()) {
					csvWriter.append(String.valueOf(runningTimeResults.get(key).get(i))).append(",");
				}
				csvWriter.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@DisplayName("Test case: using a left and right operands with a constant and testing lt operator")
	@Test
	public void test01() throws EnforceException {
		String policy = readPolicy("1-policy_constant_lt.json");
		assertTrue(enforce(policy, "constant_lt", null).keySet().contains(EXPECTED_ACTION));
	}

	@DisplayName("Test case: using a left and right operands with a constant and testing lteq operator")
	@Test
	public void test02() throws EnforceException {
		String policy = readPolicy("2-policy_constant_lteq.json");
		Map<String, String> usage = enforce(policy, "constant_lteq", null);
		assertTrue(usage.keySet().contains(EXPECTED_ACTION));
	}

	@DisplayName("Test case: using a left and right operands with a constant and testing eq operator")
	@Test
	public void test03() throws EnforceException {
		String policy = readPolicy("3-policy_constant_eq.json");
		Map<String, String> usage = enforce(policy, "constant_eq", null);
		assertNotNull(usage);
		assertTrue(usage.keySet().contains(EXPECTED_ACTION));
	}

	@DisplayName("Test case: using a left and right operands with a constant and testing neq operator")
	@Test
	public void test04() throws EnforceException {
		String policy = readPolicy("4-policy_constant_neq.json");
		Map<String, String> usage = enforce(policy, "constant_neq", null);
		assertNotNull(usage);
		assertTrue(usage.keySet().contains(EXPECTED_ACTION));
	}

	@DisplayName("Test case: using a left and right operands with a constant and testing gteq operator")
	@Test
	public void test05() throws EnforceException {
		String policy = readPolicy("5-policy_constant_gteq.json");
		Map<String, String> usage = enforce(policy, "constant_gteq", null);
		assertNotNull(usage);
		assertTrue(usage.keySet().contains(EXPECTED_ACTION));
	}

	@DisplayName("Test case: using a left and right operands with a constant and testing gt operator")
	@Test
	public void test06() throws EnforceException {
		String policy = readPolicy("6-policy_constant_gt.json");
		Map<String, String> usage = enforce(policy, "constant_gt", null);
		assertNotNull(usage);
		assertTrue(usage.keySet().contains(EXPECTED_ACTION));
	}

	// Operands

	@DisplayName("Test case: using a left operand datetime and a constant right operand with lt operator")
	@Test
	public void test07() throws EnforceException {
		String policy = readPolicy("7-policy_datetime_lt.json");
		Map<String, String> usage = enforce(policy, "datetime_lt", null);
		assertNotNull(usage);
		assertTrue(usage.keySet().contains(EXPECTED_ACTION));
	}

	@DisplayName("Test case: using a left operand datetime and a constant right operand with lteq operator")
	@Test
	public void test08() throws EnforceException {
		String policy = readPolicy("8-policy_datetime_lteq.json");
		Map<String, String> usage = enforce(policy, "datetime_lteq", null);
		assertNotNull(usage);
		assertTrue(usage.keySet().contains(EXPECTED_ACTION));
	}

	@DisplayName("Test case: using a left operand datetime and a constant right operand with eq operator")
	@Test
	public void test09() throws EnforceException {
		String policy = readPolicy("9-policy_datetime_eq.json");
		Map<String, String> usage = enforce(policy, "datetime_eq", null);
		assertNotNull(usage);
		assertTrue(usage.isEmpty());
	}

	@DisplayName("Test case: using a left operand datetime and a constant right operand with neq operator")
	@Test
	public void test10() throws EnforceException {
		String policy = readPolicy("10-policy_datetime_neq.json");
		Map<String, String> usage = enforce(policy, "datetime_neq", null);
		assertNotNull(usage);
		assertTrue(usage.keySet().contains(EXPECTED_ACTION));

	}

	@DisplayName("Test case: using a left operand datetime and a constant right operand with gteq operator")
	@Test
	public void test11() throws EnforceException {
		String policy = readPolicy("11-policy_datetime_gteq.json");
		Map<String, String> usage = enforce(policy, "datetime_gteq", null);
		assertNotNull(usage);
		assertTrue(usage.keySet().contains(EXPECTED_ACTION));

	}

	@DisplayName("Test case: using a left operand datetime and a constant right operand with gt operator")
	@Test
	public void test12() throws EnforceException {
		String policy = readPolicy("12-policy_datetime_gt.json");
		Map<String, String> usage = enforce(policy, "datetime_gt", null);
		assertNotNull(usage);
		assertTrue(usage.keySet().contains(EXPECTED_ACTION));

	}

	// Left Operand Extensions

	@DisplayName("Test case: using a left operand that is an extension of ODRL (otime:time) and a constant right operand with lt operator")
	@Test
	public void test13() throws EnforceException {
		String policy = readPolicy("13-policy_extension_operand_lt.json");
		Map<String, String> usage = enforce(policy, "extension_operand_lt", null);
		assertNotNull(usage);
		assertTrue(usage.keySet().contains(EXPECTED_ACTION));

	}

	@DisplayName("Test case: using a left operand that is an extension of ODRL (otime:time) and a constant right operand with lteq operator")
	@Test
	public void test14() throws EnforceException {
		String policy = readPolicy("14-policy_extension_operand_lteq.json");
		assertTrue(enforce(policy, "extension_operand_lteq", null).keySet().contains(EXPECTED_ACTION));
	}

	@DisplayName("Test case: using a left operand that is an extension of ODRL (otime:time) and a constant right operand with eq operator")
	@Test
	public void test15() throws EnforceException {
		String policy = readPolicy("15-policy_extension_operand_eq.json");
		Map<String, String> usage = enforce(policy, "extension_operand_eq", null);
		assertNotNull(usage);
		assertTrue(!usage.keySet().contains(EXPECTED_ACTION));
	}

	@DisplayName("Test case: using a left operand that is an extension of ODRL (otime:time) and a constant right operand with neq operator")
	@Test
	public void test16() throws EnforceException {
		String policy = readPolicy("16-policy_extension_operand_neq.json");
		Map<String, String> usage = enforce(policy, "extension_operand_neq", null);
		assertNotNull(usage);
		assertTrue(usage.keySet().contains(EXPECTED_ACTION));
	}

	@DisplayName("Test case: using a left operand that is an extension of ODRL (otime:time) and a constant right operand with gteq operator")
	@Test
	public void test17() throws EnforceException {
		String policy = readPolicy("17-policy_extension_operand_gteq.json");
		Map<String, String> usage = enforce(policy, "extension_operand_gteq", null);
		assertNotNull(usage);
		assertTrue(usage.keySet().contains(EXPECTED_ACTION));
	}

	@DisplayName("Test case: using a left operand that is an extension of ODRL (otime:time) and a constant right operand with gt operator")
	@Test
	public void test18() throws EnforceException {
		String policy = readPolicy("18-policy_extension_operand_gt.json");
		Map<String, String> usage = enforce(policy, "extension_operand_gt", null);
		assertNotNull(usage);
		assertTrue(usage.keySet().contains(EXPECTED_ACTION));
	}

	// Operator extensions

	@DisplayName("Test case: using a left and right constant operands with an operator that is an extension of ODRL (otime:between")
	@Test
	public void test19() throws EnforceException {
		String policy = readPolicy("19-policy_extension_operator.json");
		Map<String, String> usage = enforce(policy, "extension_operator", null);
		assertNotNull(usage);
		assertTrue(usage.keySet().contains(EXPECTED_ACTION));
	}

	@DisplayName("Test case: using a left and right constant operands with an operator eq and testing an action that is an extension of ODRL (odemo:dummy_read)")
	@Test
	public void test20() throws EnforceException {
		String expectedAction = "https://w3id.org/def/odre-demo#dummy_read";
		String policy = readPolicy("20-policy_extension_action.json");
		Map<String, String> usage = enforce(policy, "extension_action", null);
		assertNotNull(usage);
		assertTrue(usage.keySet().contains(expectedAction) && !usage.get(expectedAction).isEmpty());
	}

	// Interpolation

	@DisplayName("Test case: interpolate the left operand with a variable and rely on a constant as right operand with eq operator")
	@Test
	public void test21() throws EnforceException {
		String policy = readPolicy("21-freemarker-policy_interpolation_variable_left_eq.json");
		Map<String, Object> variables = new HashMap<>();
		variables.put("datetime", "23:55:00");
		assertTrue(enforce(policy, "interpolation_variable_left_eq", variables).keySet().contains(EXPECTED_ACTION));
	}

	@DisplayName("Test case: interpolate the right operand with a variable  and rely on a constant as left operand with eq operator")
	@Test
	public void test22() throws EnforceException {
		String policy = readPolicy("22-freemarker-policy_interpolation_variable_right_eq.json");
		Map<String, Object> variables = new HashMap<>();
		variables.put("datetime", "23:55:00");
		assertTrue(enforce(policy, "interpolation_variable_right_eq", variables).keySet().contains(EXPECTED_ACTION));
	}

	@DisplayName("Test case: interpolate the left operand with a class and rely on a constant as right operand with eq operator")
	@Test
	public void test23() throws EnforceException {
		String policy = readPolicy("23-freemarker-policy_interpolation_function_left_eq.json");
		Map<String, Object> variables = new HashMap<>();
		variables.put("datetime", new SampleClass());
		assertTrue(enforce(policy, "interpolation_function_left_eq", variables).keySet().contains(EXPECTED_ACTION));
	}

	@DisplayName("Test case: interpolate the right operand with a class  and rely on a constant as left operand with eq operator")
	@Test
	public void test24() throws EnforceException {
		String policy = readPolicy("24-freemarker-policy_interpolation_function_right_eq.json");
		Map<String, Object> variables = new HashMap<>();
		variables.put("datetime", new SampleClass());
		assertTrue(enforce(policy, "interpolation_function_right_eq", variables).keySet().contains(EXPECTED_ACTION));
	}

	public class SampleClass {
		public String show() {
			return "23:55:00";
		}
	}

}
