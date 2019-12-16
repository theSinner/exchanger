package com.ajorloo.exchanger;

import java.util.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;
import java.util.Map;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

public class ExchangeService {

	public static Map<String, Double> getInfo() throws IOException {

		String data = Redis.get("exchage_rate");
		Gson gson = new Gson();
		if (data == null) {
			URL url = new URL("https://api.exchangeratesapi.io/latest");
			String readLine = null;
			HttpURLConnection conection = (HttpURLConnection) url.openConnection();
			conection.setRequestMethod("GET");
			int responseCode = conection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
				StringBuffer response = new StringBuffer();
				while ((readLine = in.readLine()) != null) {
					response.append(readLine);
				}
				in.close();
				Map map = gson.fromJson(response.toString(), Map.class);
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, 1);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				Integer howMany = (int) (cal.getTimeInMillis() - System.currentTimeMillis());
				data = JsonUtil.toJson(map.get("rates"));
				Redis.set("exchage_rate", data, howMany);

				Type type = new TypeToken<Map<String, Double>>() {
				}.getType();
				return gson.fromJson(data, type);

			} else {
				System.out.println("GET NOT WORKED");
			}
		} else {
			Map map = gson.fromJson(data, Map.class);
			return map;
		}

		return null;
	}

	public Double convert(ExchangeRequest payload) {

		try {
			Map<String, Double> data = getInfo();
			Double result = payload.getAmount();
			if (!payload.getSourceUnit().equals("EUR")) {
				result = result / data.get(payload.getSourceUnit());
			}
			if (!payload.getTargetUnit().equals("EUR")) {
				result = result * data.get(payload.getTargetUnit());
			}
			return result;
		} catch (IOException exception) {
			throw new RuntimeException("Something went wrong. Please try later.");
		}
	}
}
