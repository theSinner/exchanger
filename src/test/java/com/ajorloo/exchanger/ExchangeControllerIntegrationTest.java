package com.ajorloo.exchanger;

import com.google.gson.Gson;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;
import spark.utils.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ExchangeControllerIntegrationTest {

	@BeforeClass
	public static void beforeClass() {
		Main.main(null);
		Spark.awaitInitialization();
	}

	@AfterClass
	public static void afterClass() {
		Spark.stop();
	}

	@Test
	public void getValid() {
		TestResponse res = request("GET", "/exchange/eur/eur/1");
		Map<String, String> json = res.json();
		assertEquals(200, res.status);
		assertEquals(1.0, json.get("value"));
	}

	@Test
	public void sendInvalidUnit() {
		TestResponse res = request("GET", "/exchange/hello/eur/1");
		assertEquals(400, res.status);
	}

	@Test
	public void sendInvalidAmount() {
		TestResponse res = request("GET", "/exchange/usd/eur/hello");
		assertEquals(400, res.status);
	}

	private TestResponse request(String method, String path) {
		try {
			URL url = new URL("http://localhost:4567" + path);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(method);
			connection.setDoOutput(true);
			connection.connect();
			String body = "";
			try {
				body = IOUtils.toString(connection.getInputStream());
			} catch (IOException getResponseException) {

			}
			return new TestResponse(connection.getResponseCode(), body);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Sending request failed: " + e.getMessage());
			return null;
		}
	}

	private static class TestResponse {

		public final String body;
		public final int status;

		public TestResponse(int status, String body) {
			this.status = status;
			this.body = body;
		}

		public Map<String, String> json() {
			return new Gson().fromJson(body, HashMap.class);
		}
	}
}
