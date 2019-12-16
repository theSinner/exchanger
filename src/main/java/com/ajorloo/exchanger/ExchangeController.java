package com.ajorloo.exchanger;

import static spark.Spark.*;

import java.util.HashMap;

import static com.ajorloo.exchanger.JsonUtil.*;

public class ExchangeController {

	public ExchangeController(final ExchangeService exchangeService) {

		get("/exchange/:source/:target/:amount", (req, res) -> {
			ExchangeRequest payload = new ExchangeRequest(req.params("source"), req.params("target"),
					Double.parseDouble(req.params("amount")));
			payload.isValid();
			res.status(200);
			HashMap<String, Object> result = new HashMap<>();
			result.put("value", exchangeService.convert(payload));
			result.put("normalized", ExchangeRequest.format(exchangeService.convert(payload)));
			return result;
		}, json());

		after((req, res) -> {
			res.type("application/json");
		});

		exception(IllegalArgumentException.class, (e, req, res) -> {
			res.status(400);
			res.type("application/json");
			res.body(toJson(new ResponseError(e)));
		});
	}
}
