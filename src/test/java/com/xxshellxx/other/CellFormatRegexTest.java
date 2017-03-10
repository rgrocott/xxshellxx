package com.xxshellxx.other;

import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

public class CellFormatRegexTest {
	
	// Possible decimal format strings
	// 3dp = "0.000"
	// 0dp = "0"
	// 3dp with commas every 1000 = "_-* #,##0.00_-;\-* #,##0.00_-;_-* "-"??_-;_-@_-"
	// 0dp with commas every 1000 = "_-* #,##0_-;\-* #,##0_-;_-* "-"??_-;_-@_-"
	private static final String DECIMAL_FORMAT_PATTERN = "^0+.*|.*#0.*"; 

	private Pattern decimalFormatPattern = Pattern.compile(DECIMAL_FORMAT_PATTERN);
	
    @Test
    public void testCommaSeparatedNumberFormat() throws Exception {
        String formatString = "_-* #,##0.00_-;\\-* #,##0.00_-;_-* \"-\"??_-;_-@_-";
        Assert.assertTrue(decimalFormatPattern.matcher(formatString).matches());
    }
    
    @Test
    public void testSimpleDecimal() throws Exception {
        String formatString = "0.00";
        Assert.assertTrue(decimalFormatPattern.matcher(formatString).matches());
    }
}
