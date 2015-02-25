package mtp;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import mtp.dataobjects.Entry;
import mtp.services.Result;
import mtp.services.View;

import org.springframework.context.annotation.Bean;

/**
 * Views bean used from DatastoreServiceImpl
 * @author carlo
 *
 */
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

	@Bean
	public View rate() {
		return new View("rate", (Entry entry) -> new Result(entry.getRate(),
				rateKeys(entry)), View.STATS);
	}

	private List<String> rateKeys(Entry entry) {

		LocalDateTime localDateTime = entry.getTimePlaced();

		List<String> keys = new ArrayList<String>();

		keys.add(String.format("%s->%s", entry.getCurrencyFrom(),
				entry.getCurrencyTo()));
		List<String> timeKeys = Arrays
				.asList(ChronoUnit.DAYS, ChronoUnit.HALF_DAYS,
						ChronoUnit.HOURS, ChronoUnit.MINUTES,
						ChronoUnit.SECONDS).stream()
				.map(localDateTime::truncatedTo).map(LocalDateTime::toString)
				.collect(Collectors.toList());
		keys.addAll(timeKeys);
		return keys;
	}

}
