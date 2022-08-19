package com.pilers.ast;

import com.pilers.emitter.Emitter;
import com.pilers.environment.*;
import com.pilers.errors.ErrorString;
import com.pilers.errors.InterpretException;
import com.pilers.errors.CompileException;

/**
 * AST While object Syntax: WHILE [boolean-valued expression] DO statement
 * 
 * @author Gloria Zhu
 */
public class While extends Statement
{

    private Expression condition;
    private Statement stmt;

    /**
     * Constructs a While object (Does not type-check the condition, the execution
     * handles that)
     * 
     * @param condition the (boolean-valued) expression
     * @param stmt      body statement
     */
    public While(Expression condition, Statement stmt)
    {
        this.condition = condition;
        this.stmt = stmt;
    }

    /**
     * Performs semantic analysis on this
     * Checks that the expression is of boolean value
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
    }

    /**
     * Executes the while statement If a break statement is executed inside, break
     * out of the while loop
     * 
     * @param env execution environment
     * @throws BreakException     if there is an (uncaught) break statement
     * @throws ContinueException  if a continue statement is executed, let it bubble
     * @throws InterpretException if there was an error during execution
     */
    public void exec(InterpreterEnvironment env) 
        throws BreakException, ContinueException, InterpretException
    {
        Value val = condition.eval(env);
        if (!(val.getType().equals("Boolean")))
        {
            // throw an error
        } else
        {
            while (val.getValue().equals("TRUE"))
            {
                try
                {
                    stmt.exec(env);
                    val = condition.eval(env);
                }
                catch (BreakException e)
                {
                    break; // break out of the while loop
                }

            }

        }
    }

    /**
     * Compiles the while block
     * 
     * @param e the emitter
     */
    public void compile(Emitter e)
    {
        condition.compile(e);

        String keepGoingLabel = e.nextLabel();

        String stopLabel = e.nextLabel();

        e.emit(keepGoingLabel + ":");

        e.emit("beq $v0 0 "+stopLabel); // if false, stop
    
        stmt.compile(e);

        condition.compile(e); // recompile the condition
        e.emit("j "+keepGoingLabel);

        e.emit(stopLabel + ":");

    }
    
}