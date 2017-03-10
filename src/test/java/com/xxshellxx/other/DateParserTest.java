package com.xxshellxx.other;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

public class DateParserTest {
	
	private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS zzz");
	private static DateTimeFormatter dateTimeFormatterUnzoned = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
	private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
    @Test
    public void parseDate() throws Exception {
        String dateWithTimePart = "2015-07-08T00:00:00.000 GMT";
        try {
        	LocalDateTime dateTime = LocalDateTime.parse(dateWithTimePart, dateTimeFormatter);
        } catch (Exception e) {
        	Assert.fail("Failed to parse a correctly formatted datetime with non-static formatter");
        }

    }
    
    @Test
    public void dateToLocalDateTimeToString() throws Exception {
    	Date date = new Date();
    	LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    	try {
    		String localDateTimeStr = localDateTime.format(dateTimeFormatter);
    		Assert.fail("Should have errored trying to parse and format a Java date to String via LocalDateTime with zoned formatter");
    	} catch (Exception e) {
    		// This is expected result
    	}
    	
    	try {
    		String localDateTimeStr = localDateTime.format(dateTimeFormatterUnzoned);
    	} catch (Exception e) {
    		Assert.fail("Failed to parse and format a Java date to String via LocalDateTime with unzoned formatter");
    	}
    }

}
