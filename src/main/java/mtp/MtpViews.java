package mtp;

import mtp.dataobjects.Entry;
import mtp.services.Result;
import mtp.services.View;

import org.springframework.context.annotation.Bean;

public class MtpViews {

	@Bean
	public View countries() {
		return new View("countries", (Entry entry) -> new Result(null,
				entry.getOriginatingCountry()), View.COUNT);
	}

	@Bean
	public View amountBought() {
		return new View("amountBought", (Entry entry) -> new Result(
				entry.getAmountBuy(), entry.getCurrencyFrom()), View.SUM);
	}

	@Bean
	public View amountSold() {
		return new View("amountSold", (Entry entry) -> new Result(
				entry.getAmountSell(), entry.getCurrencyTo()), View.SUM);
	}
}
