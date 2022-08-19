package com.pilers.scanner;

/**
 * The Token class represents a (value, type) construct where
 * the value is a lexeme and the type is the type of the lexeme.
 * The types are defined in the class as integer constants.
 * 
 * @author Gloria Zhu
 * @version 8/19/22
 */
public class Token
{
    // Constant keywords, operators, and characters, used for type checking
    
    /**
     * Keywords
     */
    public static final String[] KEYWORDS = new String[] {
            "writeln", "readInteger", 
            "if",
            "while", 
            "for", 
            "break", "continue"
            };

    /**
     * Types
     */
    public static final String[] TYPES = new String[] {
            "Integer", "String", "Boolean"
    };

    /**
     * True
     */
    public static final String TRUE_STRING = "TRUE";
    /**
     * False
     */
    public static final String FALSE_STRING = "FALSE";

    /**
     * Defines the assignment operator
     */
    public static final String ASSIGNMENT_OPERATOR = "=";
    /**
     * Two-character operators
     */
    public static final String[] TWOCHAR_OPERATORS = new String[]
    { ASSIGNMENT_OPERATOR, "<=", ">=", "!=", "&&", "||", ">>", "<<", "!=", "++", "--", "==" };
    /**
     * One-character operators
     */
    public static final String[] ONECHAR_OPERATORS = new String[]
    { "+", "-", "*", "/", "%", ">", "<", "!", "^", "&", "|", "~", "{", "}"};

    /**
     * ALL operators ordered by precedence in a 2D array
     * Arrays are ordered in increasing order
     * 
     * Note: this 2D array does not include the assignment operator, which should
     * never be referenced in the same context as it. If it is, something
     * is seriously wrong.
     * 
     * Precedence example:
     * TRUE | 3 + 4 >= 4
     * 
     * Order of evaluation: +, >=, |
     * 
     * Overall order of precedence:
     * Logical Operators
     * Relational operators
     * Mathematical operators
     */
    public static final String[][] OPERATORS_ORDERED_BY_PRECEDENCE = new String[][]
    {
        new String[]{"||"}, // lowest precedence
        new String[]{"&&"},
        new String[]{"^"},
        new String[]{"<=",">=","==", "!=", ">", "<"},
        new String[]{"+", "-"},
        new String[]{"*", "/", "%", ">>", "<<", "&", "|"}
    };

    /**
     * All unary operators
     */
    public static final String[] UNARY_OPERATORS = new String[]
    {
        "-", "!", "~", "++", "--"
    };

    /**
     * Whitespace characters
     */
    public static final char[] WHITESPACE_CHARS = new char[] { ' ', '\n', '\t', '\r' };
    /**
     * Line terminator char
     */
    public static final char LINE_TERMINATOR_CHAR = ';';
    /**
     * Open paren character
     */
    public static final char OPEN_PAREN_CHAR = '(';
    /**
     * Close paren character
     */
    public static final char CLOSE_PAREN_CHAR = ')';
    /**
     * Quote character
     */
    public static final char QUOTE_CHAR = '"';
    /**
     * Comma character
     */
    public static final char COMMA_CHAR = ',';

    /**
     * The character that ends everything
     */
    public static final char END_CHARACTER = '?';


    /**
     * All the types, in string form
     */
    public static final String[] TYPE_STRINGS = new String[]
    {"END", "IDENTIFIER", "KEYWORD", "NUMBER", "OPERATOR", 
        "LINE_TERMINATOR", "OPEN_PAREN", "CLOSE_PAREN", "STRING", "BOOLEAN", "COMMA", "TYPE" };
    
    /**
     * Type names, but nicer; to be used in semantic analysis for type checking
     * 
     * IMPORTANT: the only types in this list that should ever be used are:
     * "Integer" "String" and "Boolean". The other ones have just been omitted - 
     * if they are used, something is SERIOUSLY WRONG
     */
    public static final String[] NICE_TYPE_STRINGS = new String[]
    { "", "", "", "Integer", "", "", "", "", "String", "Boolean", "" };

    /**
     * the END token "." signifies the end of the input stream
     */
    public static final int END = 0;

    /**
     * the IDENTIFIER token is a string
     */
    public static final int IDENTIFIER = 1;
    /**
     * the KEYWORD token is a string that is apart of
     * a set of keywords predefined in the Scanner.
     */
    public static final int KEYWORD = 2;
    /**
     * the NUMBER token is a string that represents a
     * number. todo: support decimal numbers
     */
    public static final int NUMBER = 3;

    /**
     * the OPERATOR token is a string that represents
     * an operator. it can have one or two characters.
     */
    public static final int OPERATOR = 4;

    /**
     * the LINE_TERMINATOR token is a string that represents
     * the end of the line. in this case, it is a semicolon (;).
     */
    public static final int LINE_TERMINATOR = 5;
    /**
     * the OPEN_PAREN token is a string that represents an open paren.
     */
    public static final int OPEN_PAREN = 6;
    /**
     * the CLOSE_PAREN token is a string that represents a close paren
     */
    public static final int CLOSE_PAREN = 7;
    /**
     * A STRING token represents an actual string
     * (not an identifier or boolean)
     */
    public static final int STRING = 8;
    /**
     * A BOOLEAN token represents a boolean string
     * There is only TRUE_BOOLEAN and FALSE_BOOLEAN (for now)
     */
    public static final int BOOLEAN = 9;
    /**
     * A COMMA token is just a comma :P
     */
    public static final int COMMA = 10;
    /**
     * TYPE token represents a type (see list of types at top of file)
     */
    public static final int TYPE = 11;

    /**
     * the DUMMY token is used by comment scanning to pass strings
     * if comment scanning was unsuccessful.
     */
    public static final int DUMMY = -1;

    /**
     * Number class
     * for checking variable types in the parser
     */
    public static final String NUMBER_CLASS = "java.lang.Integer";
    /**
     * String class
     * for checking variable types in the parser
     */
    public static final String STRING_CLASS = "java.lang.String";
    /**
     * Boolean class
     * for checking variable types in the parser
     */
    public static final String BOOLEAN_CLASS = "java.lang.Boolean";

    private String value;
    private int type;
    private int line;

    /**
     * Constructor for a Token object with a value and a type
     * 
     * @param value the value (as a String)
     * @param type  the type (as an integer - use the constants in the class)
     * @param line  the line of the token
     */
    public Token(String value, int type, int line)
    {
        this.value = value;
        this.type = type;
        this.line = line;
    }

    /**
     * Same constructor as above but takes a character instead Calls other
     * constructor (casts char to string)
     * 
     * @param value   the value (as a char)
     * @param type    the type (as an integer - use the constants in the class)
     * @param line    the line of the token
     */
    public Token(char value, int type, int line)
    {
        this(""+value, type, line);
    }

    /**
     * Sets the type of the token
     * @param newType the new type
     */
    public void setType(int newType)
    {
        type = newType;
    }

    /**
     * Sets the value of the token
     * @param newValue the new value
     */
    public void setValue(String newValue)
    {
        value = newValue;
    }

    /**
     * Sets the line of the token
     * @param newLine the new line
     */
    public void setLine(int newLine) 
    {
        line = newLine;
    }

    /**
     * @return the type of the token (as an integer)
     */
    public int getType()
    {
        return type;
    }

    /**
     * @return the type of the token (as a string)
     */
    public String getTypeAsString()
    {
        return TYPE_STRINGS[type];
    }

    /**
     * This should only reference "Integer" "Boolean" and "String"
     * If it returns something other than that, something is terribly wrong.
     * @return the type of the token VALUE (as a string)
     */
    public String getTypeAsNiceString()
    {
        return NICE_TYPE_STRINGS[type];
    }

    /**
     * @return the value of the token (in String form)
     */
    public String getValue()
    {
        return value;
    }

    /**
     * @return the line of the token
     */
    public int getLine()
    {
        return line;
    }

    // Static factory methods to make token creation less messy in the Scanner.

    /**
     * @param line the line of the token
     * @return an end token
     */
    public static Token endToken(int line)
    {
        return new Token(Token.END_CHARACTER, Token.END, line);
    }

    /**
     * @param line the line of the token
     * @return a line terminator token
     */
    public static Token lineTerminatorToken(int line)
    {
        return new Token(LINE_TERMINATOR_CHAR, Token.LINE_TERMINATOR, line);
    }

    /**
     * @param line the line of the token
     * @return an open parentheses token
     */
    public static Token openParenToken(int line)
    {
        return new Token(OPEN_PAREN_CHAR, Token.OPEN_PAREN, line);
    }

    /**
     * @param line the line of the token
     * @return a close parentheses token
     */
    public static Token closeParenToken(int line)
    {
        return new Token(CLOSE_PAREN_CHAR, Token.CLOSE_PAREN, line);
    }

    /**
     * @param line the line of the token
     * @return a comma token
     */
    public static Token commaToken(int line)
    { 
        return new Token(COMMA_CHAR, Token.COMMA, line);
    }

    /**
     * @return a dummy token
     * @param value the value to be put into the dummy token
     * @param line  the line of the token
     */
    public static Token dummyToken(String value, int line)
    {
        return new Token(value, Token.DUMMY, line);
    }

    /**
     * Overriden equals method
     * @param other the other Object
     */
    @Override
    public boolean equals(Object other)
    {

        if (other instanceof String) return value.equals((String) other);
        else if (other instanceof Character) return value.equals(""+other);
        else if (other instanceof Token) 
        {
            Token casted = (Token)other;
            return line == casted.line && type == casted.type && value == casted.value;
        }

        return false;        
    }

    /**
     * Overriden toString method
     */
    @Override
    public String toString()
    {
        return String.format("%-2s %-20s%s\n", line, this.getTypeAsString(), value);
    }

}