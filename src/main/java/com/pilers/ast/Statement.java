package com.pilers.ast;

import com.pilers.environment.*;
import com.pilers.emitter.*;
import com.pilers.errors.InterpretException;
import com.pilers.errors.CompileException;

/**
 * AST Statement class
 * 
 * @author Gloria Zhu
 */
public abstract class Statement 
{
    /**
     * Executes the statement
     * To be implemented by subclasses
     * exec() should be used when interpreting
     * 
     * @param env execution environment (InterpreterEnvironment)
     * @throws BreakException     if a break statement is executed
     * @throws ContinueException  if a continue statement is executed
     * @throws InterpretException if there is a runtime error during execution
     */
    public abstract void exec(InterpreterEnvironment env)
        throws BreakException, ContinueException, InterpretException;

    
    /**
     * Performs semantic analysis (checks things like type, scope)
     * To be implemented by subclasses
     * analyze() should be used during the semantic analysis stage
     * 
     * @throws CompileException for any errors found
     * @param env analysis environment (SemanticAnalysisEnvironment)
     */
    public abstract void analyze(SemanticAnalysisEnvironment env) throws CompileException;

    /**
     * Compiles the statement with the given emitter
     * 
     * @precondition There are no issues with type, undeclared variables, etc.
     *               That should all have been handled in semantic analysis.
     * @param e the given emitter
     */
    public void compile(Emitter e)
    {
        throw new Error("Implement me!!!!!");
    }
}