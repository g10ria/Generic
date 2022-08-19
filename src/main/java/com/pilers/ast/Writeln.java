package com.pilers.ast;

import com.pilers.environment.*;
import com.pilers.errors.InterpretException;
import com.pilers.errors.CompileException;
import com.pilers.emitter.Emitter;

/**
 * AST Writeln class
 * 
 * @author Gloria Zhu
 */
public class Writeln extends Statement
{
    private Expression exp;

    /**
     * Constructs a Writeln object
     * 
     * @param exp an expression
     */
    public Writeln(Expression exp)
    {
        this.exp = exp;
    }

    /**
     * Excutes the WriteLn statement
     * 
     * @param env execution environment
     * @throws BreakException     if there is a break statement
     * @throws ContinueException  if there is a continue statement
     * @throws InterpretException if there was an error during execution
     */
    public void exec(InterpreterEnvironment env)
        throws BreakException, ContinueException, InterpretException
    {
        System.out.println(exp.eval(env));
    }

    /**
     * Performs semantic analysis on this
     * Just checks the child expression
     * 
     * @throws CompileException if the expression throws an error
     * @param env the current env
     */
    public void analyze(SemanticAnalysisEnvironment env) throws CompileException
    {
        exp.analyze(env);
    }

    /**
     * Compiles the Writeln
     * @param e the given emitter
     */
    public void compile(Emitter e)
    {
        exp.compile(e);

        e.emit("move $a0 $v0");

        if (exp.type.equals("String"))
        {
            e.emit("addu $a0 $a0 4");
            e.emit("li $v0 4");
        }
        else
            e.emit("li $v0 1");
            
        e.emit("syscall");
        e.emit("println()");        
    }
}