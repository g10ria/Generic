package com.pilers.ast;

import java.util.regex.Pattern;

import com.pilers.emitter.Emitter;
import com.pilers.environment.*;
import com.pilers.errors.ErrorString;
import com.pilers.errors.InterpretException;
import com.pilers.errors.CompileException;

/**
 * AST AssignmentRead class
 * 
 * Reads input from cmd line and assigns it to specified variable
 * 
 * Note: currently, this can only read an integer
 * Another note: currently not implemented/tested fully
 * 
 * @author Gloria Zhu
 */
public class ReadInteger extends Statement
{
    private String var;

    /**
     * Constructs an AssignmentRead object
     * 
     * @param var a string
     */
    public ReadInteger(String var)
    {
        this.var = var;
    }

    /**
     * Performs semantic analysis for the assignment read
     * Checks that the variable exists and that its type is an integer
     * 
     * @throws CompileException if the above conditions are broken
     * @param env the current env
     */
    public void analyze(SemanticAnalysisEnvironment env) throws CompileException
    {
        String varType = env.getVariableType(var);
        if (varType == null) 
            throw new CompileException(ErrorString.unknownIdentifier(var));

        // Note: when this handles more types, remove/modify this part
        if (!varType.equals("Integer"))
            throw new CompileException(ErrorString.typeAssignment("Integer", varType));
    }

    /**
     * Executes the assignment
     * 
     * @param env the execution environment
     * @throws InterpretException if there is a runtime exception in execution
     */
    public void exec(InterpreterEnvironment env) throws InterpretException
    {
        String inp = System.console().readLine();

        Pattern p = Pattern.compile("[0-9][0-9]*");

        while (!p.matcher(inp).find()) inp = System.console().readLine();
        
        Value userInp = new Value(Integer.parseInt(inp));
        env.setVariable(var, userInp);
        
    }

    /**
     * Compiles the integer input statement
     * 
     * @param e the emitter
     */
    public void compile(Emitter e)
    {
        e.emit("li $v0 5");
        e.emit("syscall");

        if (e.isGlobalVariable(var))
            e.emit("sw $v0 " + "_data_" + var);
        else
        {
            int index = e.getLocalVarIndex(var);
            e.emit("sw $v0 " + index * 4 + "($sp)");
        }
    }

}