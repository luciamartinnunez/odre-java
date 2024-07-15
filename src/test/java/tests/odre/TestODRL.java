package tests.odre;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.jena.sparql.function.FunctionRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import odre.EnforceException;
import odre.ODRE;



@DisplayName("Test cases for ODRE enforcing algorithm and its ODRL support")
public class TestODRL {

	private static StringBuilder dir = new StringBuilder("./src/test/resources/odrl/");
	
	private static String readPolicy(String name) {
		StringBuilder contentBuilder = new StringBuilder();
		try (Stream<String> stream = Files.lines(Paths.get(dir+name), StandardCharsets.UTF_8)){
		  stream.forEach(s -> contentBuilder.append(s).append("\n"));
		}catch (IOException e){
		  e.printStackTrace();
		}
		return  contentBuilder.toString();
	}
	
	
	@DisplayName("Test case 01")
	@Test
	public void test01() throws EnforceException {
		String policy = readPolicy("dateTime-err2.json");
		assertTrue(ODRE.enforce(policy, null).keySet().contains("http://www.w3.org/ns/odrl/2/display"));
	}
	
	
	
}
