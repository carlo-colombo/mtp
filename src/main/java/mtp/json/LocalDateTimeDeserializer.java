package mtp.json;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

	public static final DateTimeFormatter DATETIME_PATTERN = DateTimeFormatter
			.ofPattern("dd-MMM-yy HH:mm:ss", Locale.forLanguageTag("en"));

	public static Pattern MONTH_PATTERN = Pattern.compile("(\\w{3})");

	@Override
	public LocalDateTime deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {

		String string = jp.getText();
		Matcher matcher = MONTH_PATTERN.matcher(string);

		StringBuffer sb = new StringBuffer(string.length());

		while (matcher.find()) {
			String month = StringUtils.capitalize(matcher.group(1).toLowerCase());
			matcher.appendReplacement(sb, month);
		}
		matcher.appendTail(sb);

		return LocalDateTime.parse(sb.toString(), DATETIME_PATTERN);
	}
}
