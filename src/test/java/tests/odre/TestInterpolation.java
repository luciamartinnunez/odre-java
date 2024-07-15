package tests.odre;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.jena.sparql.function.FunctionRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import odre.EnforceException;
import odre.ODRE;



@DisplayName("Test cases for ODRE enforcing algorithm and its ODRL support")
public class TestInterpolation {

	private static StringBuilder dir = new StringBuilder("./src/test/resources/odrl-interpolation/");
	
	private static String readPolicy(String name) {
		StringBuilder contentBuilder = new StringBuilder();
		try (Stream<String> stream = Files.lines(Paths.get(dir+name), StandardCharsets.UTF_8)){
		  stream.forEach(s -> contentBuilder.append(s).append("\n"));
		}catch (IOException e){
		  e.printStackTrace();
		}
		return  contentBuilder.toString();
	}
	

	@DisplayName("Test 01: testing interpolation engine using a freemarker native function")
	@Test
	public void test01() throws EnforceException {
		String policy = readPolicy("interopolate-variables-1.json");
		Map<String,String> output = ODRE.enforce(policy, null);
		assertTrue(ODRE.enforce(policy, null).keySet().contains("http://www.w3.org/ns/odrl/2/display"));
	}
	
	@DisplayName("Test 02: testing interpolation engine using a variable with value")
	@Test
	public void test02() throws EnforceException {
		String policy = readPolicy("interopolate-variables-2.json");
		Map<String, Object> variables = new HashMap<>();
		variables.put("datetime", "2024-07-04");
		assertTrue(ODRE.enforce(policy, variables).keySet().contains("http://www.w3.org/ns/odrl/2/display"));

	}
	
	@DisplayName("Test 03: testing interpolation engine using a java class instance")
	@Test
	public void test03() throws EnforceException {
		String policy = readPolicy("interopolate-variables-3.json");
		Map<String, Object> variables = new HashMap<>();
		variables.put("datetime", new SampleClass());
		assertTrue(ODRE.enforce(policy, variables).keySet().contains("http://www.w3.org/ns/odrl/2/display"));
	}
	
	public class SampleClass {
		public String show() {
			return "2024-07-03";
		}
	}
	
}
