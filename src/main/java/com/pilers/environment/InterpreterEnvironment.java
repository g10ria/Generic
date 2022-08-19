package com.pilers.environment;

import java.util.HashMap;

import com.pilers.ast.*;
import com.pilers.errors.*;

/**
 * The InterpreterEnvironment serves as a runtime environment,
 * to be used when interpreting a program. It is passed into
 * every execution and evaluation, and essentially serves
 * as a variable table.
 * 
 * @author Gloria Zhu
 * @version 8/19/22
 */
public class InterpreterEnvironment extends Environment
{

    /**
     * Creates a new environment with a parent env
     * @param parentEnv the parent env
     */
    public InterpreterEnvironment(InterpreterEnvironment parentEnv)
    {
        super(parentEnv);
        varmapVal = new HashMap<String, Value>();
    }
    
    /**
     * Factory method to return a new global environment (parent is null)
     * @return the new env
     */
    public static InterpreterEnvironment newGlobalEnv()
    {
        return new InterpreterEnvironment(null);
    }

    /**
     * Declares a variable in the variable map Calls the helper method and passes
     * in true; the true flag tells the environemnt to give this variable a default
     * value in the value hashmap.
     * 
     * @param type the type of the variable
     * @param name the name of the variable
     * @throws InterpretException if the variable has already been declared
     */
    public void declareVariable(String type, String name) throws InterpretException
    {
        if (!declareVariable(type, name, true))
            throw new InterpretException(ErrorString.duplicateIdentifier(name));
    }

    /**
     * Sets a variable in the variable maps
     * 
     * @param name the name of the variable
     * @param value the value of the variable
     * @throws CompileException if the variable has not been declared yet
     * @throws CompileException if the assigned value does not have the correct type
     */
    public void setVariable(String name, Value value) throws InterpretException
    {
        Environment current = this;
        String var = current.varmapType.get(name);

        while (current.parentEnv != null && var == null)
        {
            current = current.parentEnv;
            var = current.varmapType.get(name);
        }
        
        if (var == null) throw new InterpretException(ErrorString.unknownIdentifier(name));
        else if (!var.equals(value.getType()))
            throw new InterpretException(ErrorString.typeAssignment(var, value.getType()));
        
        current.varmapVal.put(name, value);
    }

    /**
     * Gets a variable's value
     * Searches up through the environments until it either
     * finds the variable's value or reaches the "null"
     * environment (goes past the global environment)
     * 
     * @param name the name of the variable
     * @return the value of the variable, or null if not found
     */
    public Value getVariable(String name)
    {
        Environment current = this;
        Value varValue = null;
        while(current != null && varValue == null)
        {
            varValue = current.varmapVal.get(name);
            current = current.parentEnv;
        }
        return varValue;
    }
}