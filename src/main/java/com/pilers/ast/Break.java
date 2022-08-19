package com.pilers.ast;

import com.pilers.environment.*;
import com.pilers.errors.CompileException;

/**
 * AST Break Class
 * 
 * @author Gloria Zhu
 */
public class Break extends Statement
{
    /**
     * A Break statement breaks out of the for and while control blocks
     * 
     * @param env the execution environment
     * @throws BreakException when encountered
     */
    public void exec(InterpreterEnvironment env) throws BreakException
    {
        throw new BreakException();
    }

    /**
     * Performs semantic analysis on this
     * (todo) Checks that the break statement is inside a looping construct
     * 
     * @throws CompileException if the above condition is not met
     * @param env the current env
     */
    public void analyze(SemanticAnalysisEnvironment env) throws CompileException
    {
        // TODO check that the break is inside a looping construct
    }
}