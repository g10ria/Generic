package com.pilers.ast;

import com.pilers.emitter.Emitter;
import com.pilers.environment.*;
import com.pilers.errors.InterpretException;
import com.pilers.errors.CompileException;

/**
 * AST DeclareAssignment class
 * Represents a statement of the from 'TYPE IDENTIFER = VALUE'
 * Basically a 2-in-1 package of a declaration and an assignment
 * 
 * @author Gloria Zhu
 */
public class DeclareAssignment extends Statement
{
    private Declaration declaration;
    private Assignment assignment;

    /**
     * Constructs a DeclareAssignment object
     * 
     * @param type the type of the variable
     * @param var  a string
     * @param exp  an expression
     * @throws TypeErrorException
     */
    public DeclareAssignment(String type, String var, Expression exp)
    {
        declaration = new Declaration(type, var);
        assignment = new Assignment(var, exp);
    }

    /**
     * Executes the assignment
     * 
     * @param env the execution environment
     * @throws BreakException     if a break statement is executed
     * @throws ContinueException  if a continue statement is executed
     * @throws InterpretException if there is an error during execution
     */
    public void exec(InterpreterEnvironment env) 
        throws BreakException, ContinueException, InterpretException
    {
        declaration.exec(env);
        assignment.exec(env);
    }

    /**
     * Performs semantic analysis on this
     * Delegates the tasks to the declaration and assignment objs respectively
     * 
     * @throws CompileException if either throws an error
     * @param env the current env
     */
    public void analyze(SemanticAnalysisEnvironment env) throws CompileException
    {
        declaration.analyze(env);
        assignment.analyze(env);
    }

    /**
     * Compiles the declaration/assignment
     * @param e given emitter
     */
    public void compile(Emitter e)
    {
        declaration.compile(e);
        assignment.compile(e);
    }

}