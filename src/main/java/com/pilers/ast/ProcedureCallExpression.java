package com.pilers.ast;

import com.pilers.emitter.Emitter;
import com.pilers.environment.*;
import com.pilers.errors.InterpretException;
import com.pilers.errors.CompileException;
import com.pilers.errors.ErrorString;

/**
 * AST ProcedureCallExpression class
 * A procedure call used as an expression
 * 
 * Assigns type during semantic analysis
 * 
 * @author Gloria Zhu
 */
public class ProcedureCallExpression extends Expression
{
    private String name;
    private Expression[] parameters;

    /**
     * Constructs a ProcedureCallExpression objec
     * 
     * @param name       the name of the procedure to call
     * @param parameters the passed in parameters (as expressions)
     */
    public ProcedureCallExpression(String name, Expression[] parameters)
    {
        this.name = name;
        this.parameters = parameters;
    }

    /**
     * Performs semantic analysis on this
     * Uses the helper in the procedure declaration class
     * 
     * @throws CompileException if a CompileException is thrown
     * @param env the current env
     */
    public void analyze(SemanticAnalysisEnvironment env) throws CompileException
    {
        for(Expression e : parameters) e.analyze(env);

        ProcedureDeclaration proc = env.getProcedure(name);

        if (proc==null) throw new CompileException(ErrorString.unknownProcedure(name));

        proc.analyzeProcedureCall(env, parameters);

        this.type = proc.getReturnType();
    }

    /**
     * Evaluates a procedure
     * 
     * @param env the execution environment
     * @throws BreakException    if a break statement is executed, let it bubble
     * @throws ContinueException if a continue statement is executed, let it bubble
     * @return the returned value of the procedure, if any
     * @throws InterpretException if an error is thrown
     */
    public Value eval(InterpreterEnvironment env)
        throws BreakException, ContinueException, InterpretException
    {
        ProcedureDeclaration proc = env.getProcedure(name);

        return proc.callProcedure(env, parameters);
    }

    /**
     * Compiles the procedure call as if it were used as an expression
     * 
     * @param e the emitter
     */
    public void compile(Emitter e)
    {
        
        for (int i = 0; i < parameters.length; i++)
        {
            parameters[i].compile(e);
            e.emitPush("$v0");
            e.addPushedParam();
        }

        e.resetPushedParams();

        e.emit("jal " + name);

        // return value is pushed inside of the procedure for clarity
        // now: return value is in v0
    }
    
}