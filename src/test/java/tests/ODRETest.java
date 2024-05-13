package tests;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.jena.sparql.function.FunctionRegistry;
import org.junit.jupiter.api.Test;

import odre.EnforceException;
import odre.ODRE;
import odre.casts.CustomTemperature;
import odre.casts.Date;
import odre.functions.*;

public class ODRETest {

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
	
	@Test
	public void test01() throws EnforceException {
		FunctionRegistry.get().put("http://www.w3.org/ns/odrl/2/dateTime", DateTime.class);
		FunctionRegistry.get().put("http://www.w3.org/ns/odrl/2/lteq", Lteq.class);
		FunctionRegistry.get().put("http://www.w3.org/2001/XMLSchema#date", Date.class);
		FunctionRegistry.get().put("http://www.w3.org/2001/XMLSchema#dateTime", odre.casts.DateTime.class);
		String policy = readPolicy("date-1.json");
		System.out.println(policy);
		System.out.println("-----test-----");
		ODRE.enforce(policy, null);
	}
	
}
