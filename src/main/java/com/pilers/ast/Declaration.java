package com.pilers.ast;

import com.pilers.emitter.Emitter;
import com.pilers.environment.*;
import com.pilers.errors.ErrorString;
import com.pilers.errors.InterpretException;
import com.pilers.errors.CompileException;

/**
* AST Declaration class
 * 
 * @author Gloria Zhu
 */
public class Declaration extends Statement
{
    private String type;
    private String var;

    /**
     * Constructs an Assignment object
     * 
     * @param type the type of the variable
     * @param var  a string
     * @throws TypeErrorException
     */
    public Declaration(String type, String var)
    {
        this.type = type;
        this.var = var;
    }

    /**
     * Executes the declaration
     * 
     * @param env the execution environment
     * @throws BreakException    if a break statement is executed
     * @throws ContinueException if a continue statement is executed
     * @throws InterpretException if there is a run time error (the variable already exists)
     */
    public void exec(InterpreterEnvironment env) 
        throws BreakException, ContinueException, InterpretException
    {
        env.declareVariable(type, var);
    }

    /**
     * Performs semantic analysis on this (todo)
     * Checks that the variable does not already exist
     * 
     * @throws CompileException if the above condition is not met
     * @param env the current env
     */
    public void analyze(SemanticAnalysisEnvironment env) throws CompileException
    {
        String vartype = env.getVariableType(var);
        if (vartype != null) throw new CompileException(ErrorString.duplicateIdentifier(var));

        env.declareVariable(type, var);
    }

    /**
     * Compiles the declaration
     * 
     * @param e given emitter
     */
    public void compile(Emitter e) 
    {
        // if e is not compiling a procedure, then this is a global variable and
        // will be declared in .data

        if (e.isCompilingProcedure())
        {
            e.addLocalVariable(var);
            e.emit("li $v0 0 # Declaring local variable "+var);
            // default value 0 for all variables
            e.emitPush("$v0");
        }
    }

}