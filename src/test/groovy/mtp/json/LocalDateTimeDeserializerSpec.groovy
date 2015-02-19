package mtp.json

import java.time.LocalDateTime;

import java.time.Month;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;

import spock.lang.Specification

/**
 * This test is needed because dates with uppercase month cannot be parsed
 * by standard parsers and a custom parser has been implemented
 * 
 * @author carlo
 *
 */
class LocalDateTimeDeserializerSpec extends Specification {

	def "should convert a date like 12-FEB-15 10:20:30 to a LocalDateTime"(){
		setup:
		def deserializer = new LocalDateTimeDeserializer()
		def jp = Mock(JsonParser)
		def ctxt = Mock(DeserializationContext)
		jp.getText() >> timestamp
		expect:
		LocalDateTime date = deserializer.deserialize(jp,ctxt)
		date.month == month
		date.dayOfMonth == day
		date.year == year
		date.hour == hour
		where:
		timestamp            || day | month          | year | hour
		"01-JAN-15 10:20:30" ||  1  | Month.JANUARY  | 2015 | 10
		"28-FEB-01 12:20:30" || 28  | Month.FEBRUARY | 2001 | 12
		"30-MAR-00 13:20:30" || 30  | Month.MARCH    | 2000 | 13
		"31-MAY-99 00:20:30" || 31  | Month.MAY      | 2099 | 00
	}
}
