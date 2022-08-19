package com.pilers.ast;

import com.pilers.environment.*;
import com.pilers.emitter.*;

/**
 * AST Value class
 * 
 * A Value class represents any raw value in the program
 * Note: this kind of just treats String Boolean and Integer
 * as primitive values
 * 
 * Strings are stored as such
 * Integers are stored as String.valueOf(int)
 * Booleans are stored as "TRUE" or "FALSE"
 * 
 * Assigns type during construction
 * 
 * @author Gloria Zhu
 */
public class Value extends Expression
{
    private String value;

    /**
     * Constructs a Value object
     * @param value the value in string form
     * @param type the type of the value
     */
    public Value(String value, String type)
    {
        this.value = value;
        this.type = type;
    }

    /**
     * Constructs a Value object from an integer
     * (Converts integer to a string)
     * 
     * @param value the numerical value
     */
    public Value(int value)
    {
        this(""+value, "Integer");
    }

    /**
     * Constructs a Value object from a String
     * 
     * @param value the String value
     */
    public Value(String value)
    {
        this(value, "String");
    }
    
    /**
     * Constructs a Value object from a boolean
     * (Converts to "TRUE" or "FALSE")
     * 
     * @param value the boolean value
     */
    public Value(boolean value)
    {
        this(value ? "TRUE" : "FALSE", "Boolean");
    }

    /**
     * @param env evaluation environment
     * @return the value of the Value (just returns itself) 
     */
    public Value eval(InterpreterEnvironment env)
    {
        return this;
    }

    /**
     * @return the value in string form
     */
    public String toString()
    {
        return value;
    }

    /**
     * @return the value of the Value (in string form)
     */
    public String getValue()
    {
        return value;
    }

    /**
     * @return the type of the Value
     */
    public String getType()
    {
        return type;
    }

    /**
     * Performs semantic analysis on this
     * (Nothing to check here, this should always be semantically correct)
     * 
     * @param env the current env
     */
    public void analyze(SemanticAnalysisEnvironment env)
    {
        // do nothing
    }

    /**
     * Compiles a value
     * For an integer: just loads the integer into v0
     * For a string: allocates 64 bytes (max string length) and stores the
     *              address to that space in v0; loads in each character of
     *              the string byte by byte.
     * For a boolean: loads in 1 (true) or 0 (false) into v0
     * 
     * @postcondition the value is in the $v0 register
     * @param e the given emitter
     */
    public void compile(Emitter e)
    {
        switch(type)
        {
            case("Integer"):
                e.emit("li $v0 " + Integer.parseInt(value));
                break;
            case("String"):
                e.loadString(value);
                break;

            case("Boolean"):
                if (value.equals("TRUE")) e.emit("li $v0 1");
                else e.emit("li $v0 0");
                break;
            default:
                throw new RuntimeException("Compiling not implemented for other types yet!!");
        }
    }
}