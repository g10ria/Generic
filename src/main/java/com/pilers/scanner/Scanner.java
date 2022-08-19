package com.pilers.scanner;

import java.io.*;
import java.util.regex.*;

import com.pilers.errors.ScanErrorException;

/**
 * A Scanner is responsible for reading the input stream, one character at a
 * time, and separating the input into tokens: (lexeme, type, line #).
 * 
 * @author Gloria Zhu
 * @version 8/19/22
 */
public class Scanner
{
    private Reader in;
    private char currChar;
    private boolean eof;
    private int currentLine;

    /**
     * Main method that tests the scanner.
     * 
     * @param args arguments from the command line
     */
    public static void main(String[] args)
    {
        boolean scanError = false;
        try
        {
            // for my machine:
            Scanner scanner = new Scanner(
                    Scanner.class.getResourceAsStream("tests/ScannerTest.txt"));


            // for BlueJ:
            // Path currentDir = Paths.get("ScannerTest.txt");            
            // Scanner scanner = new Scanner(new FileInputStream(
            //     new File(currentDir.toAbsolutePath().toString())));

            Token next = null;
            while (scanner.hasNext())
            {
                try
                {
                    next = scanner.nextToken();
                    if (next.getType() == Token.END) scanner.terminateScan(next);

                    // System.out.printf("%-2s %-20s%s\n",
                            // next.getLine(), next.getTypeAsString(), next.getValue());
                }
                catch (ScanErrorException e)    // print the error and continue
                {
                    scanError = true;   // separate variable allows continuation of program
                    System.out.println(e);
                    continue;
                }
            }

            if (next.getType() != Token.END)
                scanner.terminateScan(Token.endToken(scanner.getCurrentLine() + 1));

        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }

        if (scanError) System.exit(-1);
        
    }

    /**
     * Prints out an end token and terminates scanning. This is called when
     * the END OF FILE character is encountered or the end of the file is reached
     * @param endTok the end token
     */
    public void terminateScan(Token endTok)
    {
        // System.out.println(endTok);
        // System.exit(0);
        eof = true;
    }

    /**
     * @return the current line that the scanner is on
     */
    public int getCurrentLine()
    {
        return currentLine;
    }

    /**
     * Scanner constructor for constructing a scanner given an input stream. It sets
     * the end-of-file flag and then reads the first character of the input string
     * into the instance field currentChar. Usage: Scanner lex = new
     * Scanner(input_stream)
     * 
     * @param inStream the input stream
     * @throws IOException if there is an error with file i/o
     */
    public Scanner(InputStream inStream) throws IOException
    {
        in = new BufferedReader(new InputStreamReader(inStream));
        eof = false;
        getNextChar();
        currentLine = 1;
    }

    /**
     * Scanner constructor for constructing a scanner that scans a given input
     * string. It sets the end-of-file flag and then reads the first character of
     * the input string into the instance field currentChar. Usage: Scanner lex =
     * new Scanner(input_string);
     * 
     * @param inString the string to scan
     * @throws IOException if there is an error with file i/o
     */
    public Scanner(String inString) throws IOException
    {
        in = new BufferedReader(new StringReader(inString));
        eof = false;
        getNextChar();
        currentLine = 1;
    }

    /**
     * The getNextChar method attempts to get the next character from the input
     * stream. It sets the eof flag true if the end of file is reached on the input
     * 
     * @postcondition the input stream is advanced one character if it is not at
     * the end of the file, currChar is set to the caracter read from the input
     * stream, eof is set to true IF the input stream is exhausted
     * character if it is not at the end of the file, and the currChar instance
     * field is set to the character read from the input stream. eof is set to true
     * if the input stream is exhausted.
     * @throws IOException if there was an error with file i/o
     */
    private void getNextChar() throws IOException
    {
        int inp = 0;

        try
        {
            inp = in.read();
            if (inp == -1)
            {
                eof = true;
                currChar = Token.END_CHARACTER;
            }
            else
            {
                currChar = (char) inp;
                if (inp == '\n')
                    currentLine++;
            }
        }
        catch (IOException e)
        {
            throw e;
        }
    }

    /**
     * @return if there is still at least one token left to be read
     */
    public boolean hasNext()
    {
        return !eof;
    }

    /**
     * Gets the next token. Tests the current char, if it is an alpha character,
     * eats and gets the entire word, which could be only one character long, then
     * returns a Token with the word and of type WORD. Otherwise, just returns a
     * Token with the current single character and it's corresponding type,
     * 
     * @return the next token
     * @throws ScanErrorException if there was an error while scanning
     * @throws IOException        if there is an error with file i/o
     * @postcondition the next token has been retrieved if an exception was not thrown
     */
    public Token nextToken() throws ScanErrorException, IOException
    {
        try
        {
            if (eof) return Token.endToken(currentLine);

            // trimming whitespace
            while (isWhitespace(currChar))
            {
                eat(currChar);
                if (eof) return Token.endToken(currentLine);
            }

            Token result = null;
            // accounts for multiple comments in a row
            while(result == null && isCommentStarter(currChar))
            {
                result = scanComment(); // returns null if a comment was scanned
                while (isWhitespace(currChar))
                {
                    eat(currChar);
                    if (eof)
                        return Token.endToken(currentLine);
                }
            }

            if (result != null) // there was an unsuccessful attempt at scanning a comment
            {
                String firstChar = result.getValue().substring(0,1);
                if (firstChar.equals("(")) return scanParen(firstChar);
                else return scanOperand(result.getValue().substring(0, 1));
            }

            // trimming whitespace
            while (isWhitespace(currChar))
            {
                eat(currChar);
                if (eof) return Token.endToken(currentLine);
            }
            
            if (isLineTerminator(currChar)) return scanLineTerminator();
            else if (isParen(currChar)) return scanParen("");
            else if (isDigit(currChar)) return scanNumber();
            else if (isLetter(currChar) || currChar==Token.QUOTE_CHAR) return scanString();
            else if (currChar==Token.COMMA_CHAR) return scanComma();
            else return scanOperand("");
        }
        catch (Exception e)
        {
            throw e;
        }
        
    }

    /**
     * "Eats" the current character by making sure that getNextChar() wasn't called
     * anywhere and then calling it. If current character doesn't match the given
     * character, throws an exception.
     * 
     * @param ch input character
     * @throws ScanErrorException if the input char and the current character don't match
     * @throws IOException        if there is an error with file i/o
     * @postcondition the current character in the input stream has been advanced by 1
     */
    private void eat(char ch) throws ScanErrorException, IOException
    {
        if (ch != currChar) throw ScanErrorException.illegalCharacter(currChar, ch);

        if (ch==Token.END_CHARACTER || eof)
            terminateScan(Token.endToken(getCurrentLine()));

        getNextChar();
    }

    // scanning methods

    /**
     * Scans a number and confirms that it matches a number regex
     * 
     * @return the number lexeme in String form
     * @throws ScanErrorException if the scanned lexeme doesn't match the regex
     * @throws IOException        if there is an error with file i/o
     * 
     * @precondition the currChar is a digit
     * @postcondition a number token has been scanned and returned
     */
    private Token scanNumber() throws ScanErrorException, IOException
    {
        String returned = "";

        while (!eof && isDigit(currChar))
        {
            returned += currChar;
            eat(currChar);
        }

        // this regex handles decimals; currently this doesn't work if there
        // is no number before the decimal (i.e. ".5" does not 
        /// work but "0.5" does) because a period triggers an EOF
        if (matchStr(returned, Pattern.compile("[0-9]*")))
        {
            return new Token(returned, Token.NUMBER, currentLine);
        }
            
        else throw ScanErrorException.illegalLexeme("number", returned);
    }

    /**
     * Scans two characters and tests if they constitute a comment starter (either
     * // or /*). If so, it scans and discards until the end of the comment. For an
     * inline comment, that is a newline, and for potentially nested block comments,
     * it is the last closing comment. If not, returns false. Note: this relies on
     * comment openers/closers being only 2 characters long.
     *
     * @return if a comment was successfully scanned and discarded
     * @throws ScanErrorException if there was an error while scanning
     * @throws IOException        if there is an error with file i/o
     */
    private Token scanComment() throws ScanErrorException, IOException
    {
        String commentStarter = "" + currChar;
        eat(currChar);
        commentStarter += currChar;

        if (commentStarter.equals("//")) // process an inline comment
        {
            while (currChar != '\n')
            {
                eat(currChar);
                if (eof) return null;
            }
            return null;
        }
        else if (commentStarter.equals("(*")) // process a block comment
        {
            int nestLevel = 1; // how many "layers" of nested block comments we are in - start at 1
            eat(currChar);
            String currentPair = "" + currChar;

            while (nestLevel != 0)
            {
                if (eof) throw ScanErrorException.blockCommentNotClosed();
                eat(currChar);
                currentPair += currChar;

                // add pair checking later
                if (currentPair.equals("(*"))
                    nestLevel++;
                else if (currentPair.equals("*)"))
                    nestLevel--;

                currentPair = currentPair.substring(1);
            }

            eat(currChar);

            return null;
        }
        else // not a comment
        {
            return Token.dummyToken(commentStarter, currentLine);
        }
    }

    /**
     * Scans an identifier and confirms that it matches a identifier regex
     * 
     * @return the identifier lexeme in String form
     * @throws ScanErrorException if the scanned lexeme doesn't match the regex
     * @throws IOException        if there is an error with file i/o
     */
    private Token scanString() throws ScanErrorException, IOException
    {
        String returned = "";

        if (currChar==Token.QUOTE_CHAR)
        {
            eat(Token.QUOTE_CHAR);
            while(currChar!=Token.QUOTE_CHAR)
            {
                returned += currChar;
                eat(currChar);
            }
            eat(Token.QUOTE_CHAR);

            return new Token(returned, Token.STRING, currentLine);
        }
        else
        {
            // needs modifications for later
            while (!eof && (isLetter(currChar) || isDigit(currChar) || currChar == '_'))
            {
                returned += currChar;
                eat(currChar);
            }

            if (stringArrayContains(Token.KEYWORDS, returned))
                return new Token(returned, Token.KEYWORD, currentLine);
            if (stringArrayContains(Token.TYPES, returned))
                return new Token(returned, Token.TYPE, currentLine);
            else if (returned.equals(Token.TRUE_STRING) || 
                    returned.equals(Token.FALSE_STRING)) 
                    return new Token(returned, Token.BOOLEAN, currentLine);
            else if (matchStr(returned, Pattern.compile("[a-zA-Z]([a-zA-Z]|[0-9])*")))
                return new Token(returned, Token.IDENTIFIER, currentLine);
            
        }

        throw ScanErrorException.illegalLexeme("string", returned);
    }

    /**
     * Scans an operand and confirms that it matches an operand regex
     * 
     * @param previous an optional previous string to use (because of comment
     *                 processing)
     * @return the operand lexeme in String form
     * @throws ScanErrorException if the scanned lexeme doesn't match the regex
     * @throws IOException        if there is an error with file i/o
     */
    private Token scanOperand(String previous) throws ScanErrorException, IOException
    {
        String operatorOneChar = previous;
        if (operatorOneChar.length()==0)
        {
            operatorOneChar += currChar;
            eat(currChar);
        }

        if (operatorOneChar.equals(""+Token.END_CHARACTER)) 
            return Token.endToken(currentLine);

        /*
        Look one character ahead to first check if it is a two-character operator.
        This allows parsing of operators such as += or *=.
        */
        String operatorTwoChar = operatorOneChar + currChar;

        if (stringArrayContains(Token.TWOCHAR_OPERATORS, operatorTwoChar))
        {
            eat(currChar);
            return new Token(operatorTwoChar, Token.OPERATOR, currentLine);
        }

        if (stringArrayContains(Token.ONECHAR_OPERATORS, operatorOneChar))
        {
            return new Token(operatorOneChar, Token.OPERATOR, currentLine);
        }

        if (operatorOneChar.equals(Token.ASSIGNMENT_OPERATOR))
            return new Token(operatorOneChar, Token.OPERATOR, currentLine);

        throw ScanErrorException.illegalLexeme("operand", operatorOneChar);
    }

    /**
     * Scans a line terminator.
     * 
     * @return a line terminator token
     * @throws ScanErrorException if there is an error while scanning
     * @throws IOException        if there is an error with file i/o
     */
    private Token scanLineTerminator() throws ScanErrorException, IOException
    {
        eat(currChar);
        return Token.lineTerminatorToken(currentLine);
    }

    /**
     * Scans a comma
     * 
     * @return a comma token
     * @throws ScanErrorException if there is an error while scanning
     * @throws IOException if there is an error with file i/o
     */
    private Token scanComma() throws ScanErrorException, IOException
    {
        eat(currChar);
        return Token.commaToken(currentLine);
    }

    /**
     * Scans a parentheses (checks if it's open or closed).
     * 
     * @return an open or closed paren token
     * @throws ScanErrorException if there is an error while scanning
     * @throws IOException        if there is an error with file i/o
     */
    private Token scanParen(String previous) throws ScanErrorException, IOException
    {
        if (previous.length()==0) 
        {
            char paren = currChar;
            eat(currChar);
            return paren == '(' ? 
            Token.openParenToken(currentLine) : 
            Token.closeParenToken(currentLine);
        } 
        else 
        {
            return previous.equals("(") ? 
            Token.openParenToken(currentLine) : 
            Token.closeParenToken(currentLine);
        }
        
    }

    // utility methods for comparing strings

    /**
     * Utility method for testing if an array of strings 
     * contains a certain string.
     * 
     * @param arr the array of strings
     * @param val the string to test
     * @return if the array contains it or not
     */
    private static boolean stringArrayContains(String[] arr, String val)
    {
        for(String s : arr)
            if (s.equals(val)) return true;
        return false;
    }

    /**
     * Utility method for matching a string with a given pattern
     * 
     * @param str input string
     * @param pat the regex pattern to use
     * @return if the pattern matches the string or not
     */
    private static boolean matchStr(String str, Pattern pat)
    {
        return pat.matcher(str).find();
    }


    // is_ methods for a single character

    /**
     * @param ch input char
     * @return if the character is a digit
     */
    public static boolean isDigit(char ch)
    {
        return ch>='0' && ch<='9';
    }

    /**
     * @param ch input char
     * @return if the character is a letter
     */
    public static boolean isLetter(char ch)
    {
        return ch>='A' && ch<='z';
    }
    /**
     * @param ch input char
     * @return if the character is whitespace
     */
    public static boolean isWhitespace(char ch)
    {
        for(int i=0;i<Token.WHITESPACE_CHARS.length;i++)
            if (ch==Token.WHITESPACE_CHARS[i]) return true;
        return false;
    }

    /**
     * @param ch input char
     * @return if the character is a (potential) comment starter (in this case, a
     *         forward slash)
     */
    public static boolean isCommentStarter(char ch) 
    {
        return ch == '/' || ch == '(';
    }

    /**
     * Tests if a character is a line terminator
     * @param ch the character
     * @return if the character is a line terminator
     */
    public static boolean isLineTerminator(char ch)
    {
        return ch == Token.LINE_TERMINATOR_CHAR;
    }

    /**
     * Tests if a character is a parentheses
     * @param ch the character
     * @return if the character is a paren
     */
    public static boolean isParen(char ch)
    {
        return ch==Token.OPEN_PAREN_CHAR|| ch==Token.CLOSE_PAREN_CHAR;
    }
}