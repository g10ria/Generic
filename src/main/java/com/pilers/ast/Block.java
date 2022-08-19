package com.pilers.ast;

import java.util.List;
import com.pilers.environment.*;
import com.pilers.errors.InterpretException;
import com.pilers.errors.CompileException;
import com.pilers.emitter.*;

/**
 * AST Block class
 * This represents a chunk of statements
 * 
 * @author Gloria Zhu
 */
public class Block extends Statement
{
    private List<Statement> stmts;

    /**
     * Constructs a Block object
     * 
     * @param stmts a list of statements
     */
    public Block(List<Statement> stmts)
    {
        this.stmts = stmts;
    }

    /**
     * Execute the statements in the block
     * 
     * @param env the execution environment
     * @throws BreakException    if a break statement is encountered, let it keep
     *                           bubbling
     * @throws ContinueException if a continue statement is encountered, stop the
     *                           rest of execution of the block
     * @throws InterpretException if there is as run time error during execution
     */
    public void exec(InterpreterEnvironment env) throws 
        BreakException, ContinueException, InterpretException
    {
        try 
        {
            for (Statement s : stmts) s.exec(env);
        }
        catch (ContinueException e)
        {
            // do nothing
        }   
    }

    /**
     * Performs semantic analysis on this
     * Analyzes each statement 1 by 1
     * 
     * @throws CompileException if any statements throw an error
     * @param env the current env
     */
    public void analyze(SemanticAnalysisEnvironment env) throws CompileException
    {
        for (Statement s : stmts) s.analyze(env);
    }

    /**
     * Compiles the Block
     * @param e the given emitter
     */
    public void compile(Emitter e)
    {
        for (Statement s : stmts) s.compile(e);
    }

}