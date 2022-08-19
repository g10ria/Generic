package com.pilers.ast;

import com.pilers.environment.*;
import com.pilers.emitter.*;
import com.pilers.errors.InterpretException;
import com.pilers.errors.CompileException;

/**
 * AST Expression class
 * @author Gloria Zhu
 */
public abstract class Expression
{
    /**
     * The type of the expression
     * To be used primarily during semantic analysis
     */
    protected String type;

    /**
     * Evaluates the expression
     * To be implemented by subclasses
     * 
     * @param env evaluation environment
     * @return the value of the evaluated expression
     * @throws BreakException    if a break statement is executed
     * @throws ContinueException if a continue statement is executed
     * @throws InterpretException if there is a run time error during execution
     */
    public abstract Value eval(InterpreterEnvironment env) 
        throws BreakException, ContinueException, InterpretException;

    /**
     * Performs semantic analysis (checks things like type, scope)
     * To be implemented by subclasses
     * analyze() should be used during the semantic analysis stage
     * 
     * @throws CompileException if any errors are found during analysis
     * @param env analysis environment (SemanticAnalysisEnvironment)
     */
    public abstract void analyze(SemanticAnalysisEnvironment env) throws CompileException;

    /**
     * Compiles the statement with the given emitter
     * 
     * @param e the given emitter
     */
    public void compile(Emitter e)
    {
        throw new Error("Implement me!!!!!");
    }
}