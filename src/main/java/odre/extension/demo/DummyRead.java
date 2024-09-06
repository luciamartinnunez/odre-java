package odre.extension.demo;


import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase0;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DummyRead extends FunctionBase0 {

	@Override
	public NodeValue exec() {
		String output = "";
		try {
		output = sendGetRequest("https://jsonplaceholder.typicode.com/users/1");
		}catch(Exception e) {
			e.printStackTrace();
		}
		return NodeValue.makeString(output);
	}

	private static String sendGetRequest(String urlString) throws IOException {
		
		
		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");

		int responseCode = connection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			return response.toString();
		} else {
			throw new IOException("GET request not worked, Response Code: " + responseCode);
		}
	}
}
