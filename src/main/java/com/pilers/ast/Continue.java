package com.pilers.ast;

import com.pilers.environment.*;
import com.pilers.errors.CompileException;

/**
 * AST Break Class
 * 
 * @author Gloria Zhu
 */
public class Continue extends Statement
{
    /**
     * Executes the continue statement
     * A continue statement breaks out of a block of statements
     * 
     * @param env the execution environment
     * @throws ContinueException when encountered
     */
    public void exec(InterpreterEnvironment env) throws ContinueException
    {
        throw new ContinueException();
    }

    /**
     * Performs semantic analysis on this (todo)
     * Checks that the continue statement is inside a looping construct
     * 
     * @throws CompileException if the above condition is not met
     * @param env the current env
     */
    public void analyze(SemanticAnalysisEnvironment env) throws CompileException
    {
        // TODO check that the continue is inside a looping construct
    }
}