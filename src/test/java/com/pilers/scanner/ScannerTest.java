package com.pilers.scanner;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for simple Scanner
 * 
 * @author Gloria Zhu
 */
public class ScannerTest 
{
    /**
     * Testing methods isDigit(), isLetter(), and isWhitespace()
     */
    @Test
    public void validateIsDigitIsLetterIsWhitespace()
    {
        final char[] tests = new char[] { '0', '5', 'c', ' ', ')', 'u' };

        boolean[] isDigitExpectedOutputs = new boolean[] 
        { true, true, false, false, false, false };
        boolean[] isLetterExpectedOutputs = new boolean[] 
        { false, false, true, false, false, true };
        boolean[] isWhitespaceExpectedOutputs = new boolean[] 
        { false, false, false, true, false, false };

        for (int i = 0; i < tests.length; i++)
        {
            Assert.assertEquals(isDigitExpectedOutputs[i], Scanner.isDigit(tests[i]));
            Assert.assertEquals(isLetterExpectedOutputs[i], Scanner.isLetter(tests[i]));
            Assert.assertEquals(isWhitespaceExpectedOutputs[i], Scanner.isWhitespace(tests[i]));
        }
    }
}
