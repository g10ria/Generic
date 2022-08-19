package com.pilers.ast;

import com.pilers.emitter.Emitter;
import com.pilers.environment.*;
import com.pilers.errors.ErrorString;
import com.pilers.errors.InterpretException;
import com.pilers.errors.CompileException;

/**
 * AST If class
 * 
 * @author Gloria Zhu
 */
public class If extends Statement
{
    private Expression condition;
    private Statement stmt;

    /**
     * Constructs an If object (does not check that the expression evaluates to a
     * boolean, that's the job of the execution)
     * 
     * @param condition the condition
     * @param stmt      the body statement
     */
    public If(Expression condition, Statement stmt)
    {
        this.condition = condition;
        this.stmt = stmt;
    }

    /**
     * Performs semantic analysis on this
     * Checks that the expression is of a boolean type
     * 
     * @throws CompileException if the above condition is broken
     * @param env the current env
     */
    public void analyze(SemanticAnalysisEnvironment env) throws CompileException
    {
        condition.analyze(env);
        stmt.analyze(env);
        
        if (!condition.type.equals("Boolean"))
            throw new CompileException(ErrorString.ifLoopConditionInvalid());

        // todo give a warning if the if block is empty?
    }

    /**
     * Evaluates the expression, if it is type boolean and evaluates to true,
     * executes the body statement
     * 
     * @param env the execution environment
     * @throws BreakException     if a break statement is executed, let it bubble
     * @throws ContinueException  if a continue statement is executed, let it bubble
     * @throws InterpretException if an error is thrown during execution
     */
    public void exec(InterpreterEnvironment env)
        throws BreakException, ContinueException, InterpretException
    {
        Value val = condition.eval(env);
        if (!(val.getType().equals("Boolean")))
        {
            // todo throw an error
        }
        else if (val.getValue().equals("TRUE")) stmt.exec(env);
    }

    /**
     * Compiles the if statement
     * 
     * @param e the emitter
     */
    public void compile(Emitter e)
    {
        condition.compile(e);

        String label = e.nextLabel();

        e.emit("beq $v0 0 "+label); // if false, go to label to skip the if statement

        stmt.compile(e);

        e.emit(label+":");

    }
}