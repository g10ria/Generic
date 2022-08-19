package com.pilers.errors;

/**
 * ScanErrorException is a sub class of Exception and is thrown during the
 * scanning phase in order to indicate a scan error.
 * 
 * Usually, the scanning error is the result of an illegal character in the
 * input stream. The error is also thrown when the expected value of the
 * character stream does not match the actual value, or if a 
 * block comment is never closed in the code.
 * 
 * @author Mr. Page
 * @author Gloria Zhu
 */
public class ScanErrorException extends Exception
{
    
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor for ScanErrorObjects, without a reason
     */
    public ScanErrorException()
    {
        super();
    }

    /**
     * Constructor for ScanErrorObjects that includes a reason for the error
     * 
     * @param reason the reason for the ScanErrorException
     */
    public ScanErrorException(String reason)
    {
        super(reason);
    }

    // Static factory methods for some common errors

     /**
      * Returns an illegalCharacter ScanErrorException

      * @param expected the expected character
      * @param actual the actual character
      * @return the ScanErrorException
      */
    public static ScanErrorException illegalCharacter(char expected, char actual)
    {
        return new ScanErrorException("Illegal character: expected " + expected + 
                                    ", got " + actual + " instead.");
    }

    /**
     * Returns an illegalLexeme ScanErrorException
     * 
     * @param expectedType the type that was expected
     * @param illegalLexeme the illegal lexeme that did not match the expected type
     * @return the ScanErrorException
     */
    public static ScanErrorException illegalLexeme(String expectedType, String illegalLexeme)
    {
        return new ScanErrorException("Illegal lexeme: expected lexeme of type " + 
                                expectedType + ", got " + illegalLexeme + " instead.");
    }

    /**
     * Returns a blockCommentNotClosed ScanErrorException (thrown when
     * a block comment is not closed before the EOF)
     * 
     * @return the ScanErrorException
     */
    public static ScanErrorException blockCommentNotClosed()
    {
        return new ScanErrorException("Block comment not closed");
    }

}
