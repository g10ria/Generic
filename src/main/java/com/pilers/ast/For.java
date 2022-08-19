package com.pilers.ast;

import com.pilers.emitter.Emitter;
import com.pilers.environment.*;
import com.pilers.errors.ErrorString;
import com.pilers.errors.InterpretException;
import com.pilers.errors.CompileException;

/**
 * A For block uses the following syntax:
 * 
 * FOR(ASSIGNMENT STATEMENT;BOOLEAN-VALUED EXPRESSION;ANY STATEMENT;) STATEMENT;
 * 
 * Note the 3 semicolons instead of 2 inside the parentheses
 * 
 * Example:
 * 
 * FOR( i:=0 ; i<3 ; i = i + 1) WRITELN(i);
 * 
 * @author Gloria Zhu
 */
public class For extends Statement
{
    private Statement declaration;
    private Expression condition;
    private Statement incrementation;
    private Statement stmt;

    /**
     * Constructs a For object Note: this part doesn't check if the first statement
     * is an assignment and that the expression evaluates to a boolean, that's
     * checked when the for statement is run
     * 
     * @param decl the initial declaration (assignment) statement
     * @param cond some expression that evaluates to a boolean Value
     * @param incr some statement (could be anything) but should probably be
     *             incrementation
     * @param stmt the body statement to be executed
     */
    public For(Statement decl, Expression cond, Statement incr, Statement stmt)
    {
        declaration = decl;
        condition = cond;
        incrementation = incr;
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
        declaration.analyze(env);
        condition.analyze(env);
        incrementation.analyze(env);
        stmt.analyze(env);
        
        if (!condition.type.equals("Boolean"))
            throw new CompileException(ErrorString.forLoopConditionInvalid());
    }

    /**
     * Executes the for statement Double checks that the first statement is an
     * assignment and that the expression evaluates to a boolean; if not, it should
     * eventually throw an error (but it currently does not)
     * 
     * @param env the execution environment
     * @throws BreakException     if a break statement is encountered, terminate
     *                            execution of the rest of the for loop
     * @throws ContinueException  if a continue statement is encountered, let it
     *                            bubble
     * @throws InterpretException if there is a run time exception when running
     */
    public void exec(InterpreterEnvironment env) throws ContinueException, InterpretException
    {
        try
        {
            declaration.exec(env);
            Value condVal = condition.eval(env);

            if (!condVal.getType().equals("Boolean"))
            {
                // todo throw an error eventually
            }
            else
            {
                while(condVal.getValue().equals("TRUE"))
                {
                    stmt.exec(env);                 // execute statement
                    incrementation.exec(env);       // execute incrementation
                    condVal = condition.eval(env);  // evaluate condition again
                    
                }
            }
        }
        catch (BreakException e)
        {
            // stop execution
        }
    }

    /**
     * Compiles the for statement
     * 
     * @param e the emitter
     */
    public void compile(Emitter e)
    {
        declaration.compile(e);
        condition.compile(e);

        String keepGoingLabel = e.nextLabel();
        String stopLabel = e.nextLabel();

        e.emit(keepGoingLabel + ":");
        e.emit("beq $v0 0 " + stopLabel); // if false, stop

        stmt.compile(e);
        incrementation.compile(e);

        condition.compile(e); // recompile the condition
        e.emit("j " + keepGoingLabel);

        e.emit(stopLabel + ":");
    }
}