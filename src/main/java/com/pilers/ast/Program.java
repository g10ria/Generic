package com.pilers.ast;

import com.pilers.environment.*;
import com.pilers.errors.InterpretException;
import com.pilers.errors.CompileException;

import com.pilers.emitter.Emitter;

/**
 * Root node of every parsed program
 * 
 * Consists of 0 or more statements (including procedure declarations and
 * calls). Slightly different from the documentation - allows arbitrary order of
 * procedure declarations / executed statements.
 * 
 * @author Gloria Zhu
 */
public class Program extends Statement
{
    private String[] args; // command line arguments
    private Statement[] stmts;

    /**
     * Note: this WILL override the current values in $v0 and $a0
     */
    // private final String[] macroPrintString = new String[] 
    // { ".macro printString(%str)", "li $v0 4", "la $a0 %str",
    //         "syscall", "li $a0 1", ".endmacro" };

    /**
     * Note: this requires that the newline variable be defined in .data - IMPORTANT
     */
    private final String[] macroPrintln = new String[] 
    { ".macro println()", "la $a0 newline", "li $v0 4", "syscall",
            ".end_macro" };

    /**
     * Constructs a Statement objec
     * 
     * @param stmts the statements in the program
     * @param args  command line arguments
     */
    public Program(Statement[] stmts, String[] args)
    {
        this.stmts = stmts;
        this.args = args;
    }

    /**
     * Evaluates the program by executing all the statements in it
     * 
     * @param env the execution environment
     * @throws BreakException     if a break statement is executed, let it bubble
     * @throws ContinueException  if a continue statement is executed, let it bubble
     * @throws InterpretException if any errors are thrown during execution
     */
    public void exec(InterpreterEnvironment env)
        throws BreakException, ContinueException, InterpretException
    {
        // do something with the command line arguments
        System.out.println("Executing interpretation with " + args.length + " argument(s)\n");

        for(Statement s : stmts) s.exec(env);
    }

    /**
     * Performs semantic analysis for the top level program (for each
     * statement in the program)
     * 
     * @throws CompileException if any compile exceptions are thrown during execution
     * @param env the current env
     */
    public void analyze(SemanticAnalysisEnvironment env) throws CompileException
    {
        for(Statement s : stmts) s.analyze(env);
    }

    /**
     * Compiles the program into a MIPS assembly file
     * @param e the given emitter
     */
    public void compile(Emitter e)
    {
        e.emit(".text");
        e.emit(".globl main");

        // macros
        e.emit("_macros:");
        e.emit(macroPrintln);

        e.emit("main:");

        // actual program
        for(Statement s : stmts) s.compile(e);
        e.emit("li $v0 10");
        e.emit("syscall");

        // program-defined helper subroutines
        e.emitTrue();   // puts 1 (true) into v0
        e.emitFalse();  // puts 0 (false) into v0
        e.emitLEQ();
        e.emitLT();
        e.emitGEQ();
        e.emitGT();
        e.emitE();
        e.emitNE();

        // data
        e.emit(".data");
        e.emitProgramData();

        e.emit("newline: .asciiz \"\\n\"");
    }
}