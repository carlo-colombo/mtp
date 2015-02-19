package mtp.dataobjects;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import mtp.json.LocalDateTimeDeserializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author carlo
 *
 */
public class Entry implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1909706282772873931L;

	private Long userId;

	private String currencyFrom;

	private String currencyTo;

	private Long amountSell;

	private BigDecimal amountBuy;

	private BigDecimal rate;

	private LocalDateTime timePlaced;

	private String originatingCountry;

	public BigDecimal getAmountBuy() {
		return amountBuy;
	}

	public Long getAmountSell() {
		return amountSell;
	}

	public String getCurrencyFrom() {
		return currencyFrom;
	}

	public String getCurrencyTo() {
		return currencyTo;
	}

	public String getOriginatingCountry() {
		return originatingCountry;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public Long getUserId() {
		return userId;
	}

	public void setAmountBuy(BigDecimal amountBuy) {
		this.amountBuy = amountBuy;
	}

	public void setAmountSell(Long amountSell) {
		this.amountSell = amountSell;
	}

	public void setCurrencyFrom(String currencyFrom) {
		this.currencyFrom = currencyFrom;
	}

	public void setCurrencyTo(String currencyTo) {
		this.currencyTo = currencyTo;
	}

	public void setOriginatingCountry(String originatingCountry) {
		this.originatingCountry = originatingCountry;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public LocalDateTime getTimePlaced() {
		return timePlaced;
	}

	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	public void setTimePlaced(LocalDateTime timePlaced) {
		this.timePlaced = timePlaced;
	}
}
