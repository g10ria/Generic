package com.pilers.ast;

import com.pilers.emitter.Emitter;
import com.pilers.environment.*;
import com.pilers.errors.ErrorString;
import com.pilers.errors.InterpretException;
import com.pilers.errors.CompileException;

/**
 * AST VariableReference class
 * This represents a reference to a variable (by invoking its name)
 * 
 * Assigns type during semantic analysis
 * 
 * @author Gloria Zhu
 */
public class VariableReference extends Expression
{
    private String name;

    /**
     * Constructs a VariableReference object
     * 
     * @param name name of variable
     */
    public VariableReference(String name)
    {
        this.name = name;
    }

    /**
     * Performs semantic analysis on this
     * Checks that the variable exists
     * 
     * @throws CompileException if a CompileException is thrown
     * @param env the current env
     */
    public void analyze(SemanticAnalysisEnvironment env) throws CompileException
    {
        String type = env.getVariableType(name);

        if (type==null) throw new CompileException(ErrorString.unknownIdentifier(name));
        
        this.type = type;
    }

    /**
     * Evaluates the variable (fetches its value from the environment)
     * If the variable is null, that means it was undefined
     * 
     * @param env evaluation environment
     * @throws InterpretException if the above condition was broken
     * @return the value of the variable
     */
    public Value eval(InterpreterEnvironment env) throws InterpretException
    {
        Value v = env.getVariable(name);

        if (v==null) throw new InterpretException(ErrorString.unknownIdentifier(name));

        return v;
    }

    /**
     * Compiles a reference to a variable, global or local
     * 
     * @param e the emiter
     */
    public void compile(Emitter e)
    {
        if (e.isGlobalVariable(name)) e.emit("lw $v0 " + "_data_"+name+
        "# Getting global variable "+name);
        else
        {
            int index = e.getLocalVarIndex(name);
            e.emit("lw $v0 "+ index * 4 + "($sp) # Getting local variable "+name);
        }
    }
}