package com.pilers.parser;

import com.pilers.scanner.*;
import com.pilers.ast.*;
import com.pilers.ast.Value;
import com.pilers.errors.*;

import java.io.*;
import java.util.ArrayList;

/**
 * A Parser is responsible for reading the token stream from the scanner, one
 * token at a time, and processing it into a parse tree according to a grammar.
 * 
 * @author Gloria Zhu
 * @version 8/19/22
 */
public class Parser 
{
    private Scanner sc;
    private Token currTok;

    /**
     * Default constructor for a parser. Reads the first token in from the stream
     * and stores it in currTok.
     * 
     * @param scan the input scanner
     */
    public Parser(Scanner scan)
    {
        sc = scan;

        try
        {
            currTok = sc.nextToken();
        }
        catch (IOException e)
        { // if IOException, print the error and terminate
            System.out.println(e);
            System.exit(-1);
        }
        catch (ScanErrorException e)
        { // if ScanErrorException, print the error but don't terminate
            System.out.println(e);
        }
    }

    /**
     * @return true if the Parser still has more tokens to parse
     */
    public boolean hasNext() 
    {
        return currTok.getType() != Token.END;
    }

    /**
     * Same as eat, just allows a Token parameter instead of string
     * 
     * @param expected the expected string (wrapped in a token's value)
     */
    private void eat(Token expected) 
    {
        this.eat(expected.getValue());
    }

    /**
     * Same as eat, just allows a character parameter as well
     * 
     * @param expected the expected string (in char form)
     */
    private void eat(char expected) 
    {
        eat("" + expected);
    }

    /**
     * If the current token matches the passed in expected string, eats it and moves
     * to the next token.
     * 
     * @param expected the expected string
     * @throws IllegalArgumentException if the string & current token don't match
     */
    private void eat(String expected) 
    {
        if (currTok.getValue().equals(expected)) 
        { // got the expected value, read the next token

            try 
            {
                currTok = sc.nextToken();
            } 
            catch (IOException e) // print the error and terminate
            {
                System.out.println(e);
                System.exit(-1);
            } 
            catch (ScanErrorException e) // print the error but don't terminate
            {
                System.out.println(e);
            }

        } 
        else // did not get the expected value, throw an error
        {
            throw new IllegalArgumentException(
                String.format("Expected %s, but got %s %s",
                expected, currTok.getTypeAsString(), currTok.getValue()));
        }
    }

    /**
     * Parses a value
     * 
     * @precondition current token is a number, string, or boolean
     * @postcondition Value token has been eaten
     * @return a Value object
     */
    private Value parseValue() 
    {
        String val = currTok.getValue();
        String type = currTok.getTypeAsNiceString();
        eat(currTok);
        return new Value(val, type);

    }

    /**
     * Parses an expression. An expression consists of a series of factors connected
     * by operands.
     * 
     * In order to account for operator precedence, the operators are stored in a 2D
     * array (in the Token class), with each subsequent array having higher
     * precedence in the previous one.
     * 
     * Then, the parseExpression method takes in a parameter, which is the level of
     * precedence. If parseExpression is called from an external method, it should
     * always be given 0 as the starting level of precedence.
     * 
     * It searches the operators at the given level of precedence for matches and
     * recursively calls itself with levelOfPrecedence+1. The base case is when the
     * level of precedence is equal to the length of the 2D array of operators, at
     * which point it just returns parseFactor().
     * 
     * @precondition current token is the beginning of a factor
     * @postcondition expression has been parsed and eaten
     * @param levelOfPrecedence the level of operator precedence to parse at
     * @return an Expression object
     */
    private Expression parseExpression(int levelOfPrecedence)
    {
        // base case
        if (levelOfPrecedence == Token.OPERATORS_ORDERED_BY_PRECEDENCE.length)
            return parseFactor();

        Expression val = parseExpression(levelOfPrecedence + 1);

        String oper = currTok.getValue();

        String[] validOpers = Token.OPERATORS_ORDERED_BY_PRECEDENCE[levelOfPrecedence];
        while (strArrayContains(validOpers, oper)) 
        {
            eat(oper);
            val = new BinOp(oper, val, parseExpression(levelOfPrecedence + 1));
            oper = currTok.getValue();
        }
        return val;
    }

    /**
     * Helper method for testing if a string array contains a string
     * 
     * @param arr string array
     * @param str string
     * @return if the string array contains the string
     */
    private boolean strArrayContains(String[] arr, String str) 
    {
        for (int i = 0; i < arr.length; i++)
            if (arr[i].equals(str))
                return true;
        return false;
    }

    /**
     * Parses a factor.
     * 
     * A factor consists of either: another factor and a unary operator a variable
     * name an expression in parentheses a raw Value (integer, string, boolean)
     * 
     * @precondition current token is a unary operator, identifier, open
     *               parentheses, or beginning of a Value
     * @postcondition factor has been parsed and eaten
     * @return an Expression object
     */
    private Expression parseFactor() 
    {
        if (strArrayContains(Token.UNARY_OPERATORS, currTok.getValue())) 
        {
            String op = currTok.getValue();
            eat(op);
            return new UnaryOp(op, parseFactor());
        } 
        else if (currTok.getType() == Token.OPEN_PAREN) 
        {
            eat(Token.OPEN_PAREN_CHAR);
            Expression val = parseExpression(0);
            eat(Token.CLOSE_PAREN_CHAR);
            return val;
        } 
        else if (currTok.getType() == Token.IDENTIFIER) 
        {
            String id = currTok.getValue();
            eat(id);

            if (currTok.equals(Token.OPEN_PAREN_CHAR)) 
            {
                eat(Token.OPEN_PAREN_CHAR);
                Expression[] params = parseParamsList();
                eat(Token.CLOSE_PAREN_CHAR);
                return new ProcedureCallExpression(id, params);
            }

            return new VariableReference(id);
        }   
        return parseValue();
    }

    /**
     * Parses a list of parameter values separated by commas
     * 
     * @return the array of parameters (Expressions)
     */
    private Expression[] parseParamsList() 
    {
        ArrayList<Expression> params = new ArrayList<Expression>();
        while (!(currTok.equals(Token.CLOSE_PAREN_CHAR))) 
        {
            params.add(parseExpression(0));
            if (currTok.equals(Token.COMMA_CHAR))
                eat(Token.COMMA_CHAR);

        }
        Expression[] temp = new Expression[0];

        return params.toArray(temp); // cast from Object[] --> Expression[]
    }

    /**
     * Parses a list of parameter types and names, separated by commas
     * 
     * @return a 2D array that contains 2 String arrays, types and names
     */
    private String[][] parseParamsDeclarationList() 
    {
        ArrayList<String> paramTypes = new ArrayList<String>();
        ArrayList<String> paramNames = new ArrayList<String>();
        while (!(currTok.equals(Token.CLOSE_PAREN_CHAR))) 
        {
            paramTypes.add(currTok.getValue());
            eat(currTok);
            paramNames.add(currTok.getValue());
            eat(currTok);

            if (currTok.equals(Token.COMMA_CHAR))
                eat(Token.COMMA_CHAR);

        }
        String[] temp1 = new String[0];
        String[] temp2 = new String[0];

        return new String[][] { paramTypes.toArray(temp1), paramNames.toArray(temp2) };
    }

    /**
     * Parses a program
     * A program consists of 0 or more statements Order of
     * procedure declarations / executed statements is arbitrary - this part is
     * different from the documentation.
     * 
     * @param args array of command line arguments
     * @return the parsed program
     */
    public Program parseProgram(String[] args) 
    {
        ArrayList<Statement> stmts = new ArrayList<Statement>();
        while (hasNext())
            stmts.add(parseStatement());

        Statement[] temp = new Statement[0];

        return new Program(stmts.toArray(temp), args); // cast from Object[] --> Statement[]
    }

    /**
     * Parses a statement, printing and reading i/o as needed.
     * 
     * @precondition current token is one of the following: "WRITELN" "READLN"
     *               "BEGIN" of type identifier
     * @postcondition statement has been parsed and eaten, including the line
     *                terminator
     * 
     * @return parsed statement
     * @throws CompileException
     */
    public Statement parseStatement()
    {
        switch (currTok.getType())
        {
            case (Token.KEYWORD):

                switch (currTok.getValue())
                {
                    case "writeln": // write line

                        eat("writeln");
                        eat(Token.OPEN_PAREN_CHAR);
                        Expression exp = parseExpression(0);
                        eat(Token.CLOSE_PAREN_CHAR);
                        eat(Token.LINE_TERMINATOR_CHAR);

                        return new Writeln(exp);

                    case "readInteger":
                        // read integer from user input

                        eat("readInteger");
                        eat(Token.OPEN_PAREN_CHAR);
                        String userId = currTok.getValue();
                        eat(userId);
                        eat(Token.CLOSE_PAREN_CHAR);
                        eat(Token.LINE_TERMINATOR_CHAR);

                        return new ReadInteger(userId);

                    case "if": // begin an if block
                        eat("if");
                        eat(Token.OPEN_PAREN_CHAR);
                        Expression ifExpr = parseExpression(0);
                        eat(Token.CLOSE_PAREN_CHAR);
                        Statement ifStmt = parseStatement();

                        return new If(ifExpr, ifStmt);
                        
                    case "while": // begin a while block
                        eat("while");
                        eat(Token.OPEN_PAREN_CHAR);
                        Expression whileExpr = parseExpression(0);
                        eat(Token.CLOSE_PAREN_CHAR);
                        Statement whileStmt = parseStatement();

                        return new While(whileExpr, whileStmt);

                    case "for": // begin a for block
                        eat("for");
                        eat(Token.OPEN_PAREN_CHAR);
                        Statement forDecl = parseStatement(); // should be an assignment
                        Expression forCond = parseExpression(0); // should be a boolean
                        eat(";");
                        Statement forIncr = parseStatement();
                        eat(Token.CLOSE_PAREN_CHAR);

                        Statement forStmt = parseStatement();

                        return new For(forDecl, forCond, forIncr, forStmt);

                    case "break": // break statement
                        eat("break");
                        eat(Token.LINE_TERMINATOR_CHAR);

                        return new Break();

                    case "continue": // break statement
                        eat("continue");
                        eat(Token.LINE_TERMINATOR_CHAR);

                        return new Continue();

                }
                break;

            case(Token.OPERATOR):
                if (currTok.getValue().equals("{"))
                {
                    eat("{");
                    ArrayList<Statement> stmts = new ArrayList<Statement>();
                    while (!currTok.equals("}"))
                    {
                        stmts.add(parseStatement());
                    }
                    eat("}");

                    return new Block(stmts);
                }
                break;
                // todo: throw an error

            default: // not of keyword type

                // if the token does not match any keywords,
                // check if it is an identifier 
                // (this indicates a procedure call or variable assignment)
                if (currTok.getType() == Token.IDENTIFIER)
                {
                    String id = currTok.getValue();
                    eat(id);

                    switch(currTok.getValue())
                    {
                        // variable assignment (no declaration)
                        case(""+Token.ASSIGNMENT_OPERATOR): 
                            eat(Token.ASSIGNMENT_OPERATOR);
                            Expression value = parseExpression(0);
                            eat(Token.LINE_TERMINATOR_CHAR);

                            return new Assignment(id, value);

                        // procedure call
                        case(""+Token.OPEN_PAREN_CHAR): 

                            // for later
                            eat(Token.OPEN_PAREN_CHAR);
                            Expression[] params = parseParamsList();
                            eat(Token.CLOSE_PAREN_CHAR);
                            eat(Token.LINE_TERMINATOR_CHAR);

                            return new ProcedureCallStatement(id, params);
                        default:
                            // todo: throw an error
                    }
                    
                }
                // either a variable declaration, variable declaration+assignment,
                // or procedure declaration
                else if (currTok.getType() == Token.TYPE) 
                {
                    String type = currTok.getValue();
                    eat(type);

                    String id = currTok.getValue();
                    eat(id);

                    switch(currTok.getValue())
                    {
                        // has assignment
                        case(Token.ASSIGNMENT_OPERATOR):
                            eat(Token.ASSIGNMENT_OPERATOR);

                            Expression expr = parseExpression(0);

                            eat(Token.LINE_TERMINATOR_CHAR);

                            return new DeclareAssignment(type, id, expr);

                        // no assignment, just a declaration
                        case(""+Token.LINE_TERMINATOR_CHAR):
                            eat(Token.LINE_TERMINATOR_CHAR);

                            return new Declaration(type, id);

                        // procedure declaration
                        case(""+Token.OPEN_PAREN_CHAR):
                            eat(Token.OPEN_PAREN_CHAR);

                            String[][] params = parseParamsDeclarationList();
                            String[] paramTypes = params[0];
                            String[] paramNames = params[1];

                            eat(Token.CLOSE_PAREN_CHAR);

                            Statement stmt = parseStatement();

                            return new ProcedureDeclaration(id, stmt, 
                            type, paramTypes, paramNames);
                        
                        default:
                            // todo: throw an error
                            
                    }
                    
                }
                break;
        }

        return null;

    }
}