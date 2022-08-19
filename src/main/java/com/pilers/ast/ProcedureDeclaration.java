package com.pilers.ast;

import java.util.HashMap;

import com.pilers.emitter.Emitter;
import com.pilers.environment.*;
import com.pilers.errors.ErrorString;
import com.pilers.errors.InterpretException;
import com.pilers.errors.CompileException;

/**
 * AST ProcedureDeclaration class
 * 
 * The functionality to execute the procedure (including setting
 * variables, creating a new env, and executing the body
 * statement) is included in this class because the ProcedureCall
 * is split up into ProcedureCallExpression and ProcedureCallStatement,
 * and this allows for less code repetition.
 * 
 * @author Gloria Zhu
 */
public class ProcedureDeclaration extends Statement
{
    private String name;
    private Statement stmt;

    private String returnType;

    private String[] paramTypes;
    private String[] paramNames;

    /**
     * Constructs a procedure declaration
     * 
     * @param name the name of the procedure
     * @param stmt the body the procedure
     * @param returnType the return type of the procedure
     * @param paramTypes an array of strings denoting the types of the proc's parameters
     * @param paramNames an array of strings denoting the parameters of the procedure
     */
    public ProcedureDeclaration(
            String name,
            Statement stmt,
            String returnType,
            String[] paramTypes,
            String[] paramNames)
    {
        this.name = name;
        this.stmt = stmt;

        this.returnType = returnType;

        this.paramTypes = paramTypes;
        this.paramNames = paramNames;
    }

    /**
     * Evaluates the expression, if it is type boolean and evaluates to true,
     * executes the body statement
     * 
     * @param env the execution environment
     * @throws BreakException    if a break statement is executed, let it bubble
     * @throws ContinueException if a continue statement is executed, let it bubble
     */
    public void exec(InterpreterEnvironment env) throws BreakException, ContinueException
    {
        env.setProcedure(name, this);
    }

    /**
     * Performs semantic analysis on this
     * Checks that no variables have the same name
     * Checks that no variables have the same name as the "return value" variable
     * 
     * @throws CompileException if the above condition is broken
     * @param env the current env
     */
    public void analyze(SemanticAnalysisEnvironment env) throws CompileException
    {
        // Check that no variables have the same name or have the 
        // same name as the "return value" variable. O(n log n)
        HashMap<String, Boolean> hasOccurred = new HashMap<String, Boolean>();
        for(String p : paramNames)
        {
            if (hasOccurred.get(p) != null) throw new CompileException(
                    ErrorString.duplicateParamNames(name, p));
            if (p.equals(name)) throw new CompileException(
                ErrorString.paramNameEqualsProcedureName(name));
        }

        env.setProcedure(name, this);

        SemanticAnalysisEnvironment childEnv = new SemanticAnalysisEnvironment(env);

        // setting the parameter values in the child environment
        for (int i = 0; i < paramNames.length; i++)
        {
            childEnv.declareVariable(paramTypes[i], paramNames[i]);
        }

        // setting the return value variable (same name as the procedure)
        childEnv.declareVariable(returnType, name);

        stmt.analyze(childEnv);
    }

    /**
     * Performs semantic analysis for a given set of parameters
     * 
     * This is in the declaration class instead of the procedure call classes
     * because it's essentially the same for both, so this avoids repetition.
     * 
     * Checks:
     * parameter list length is the same
     * parameter types match those of the declaration
     * 
     * @param env the current env
     * @param parameters the given parameters
     * @throws CompileException if one of the above conditions is broken
     */
    public void analyzeProcedureCall(
            SemanticAnalysisEnvironment env, Expression[] parameters) throws CompileException
    {
        for(int i=0;i<parameters.length;i++)
        {
            parameters[i].analyze(env);
        }
        
        ProcedureDeclaration proc = env.getProcedure(name);
        String[] expectedTypes = proc.getParamTypes();

        if (expectedTypes.length != parameters.length)
            throw new CompileException(
                ErrorString.wrongNumberOfParameters(name, expectedTypes.length, parameters.length));

        String expected = "";
        String actual = "";
        for (int i = 0; i < expectedTypes.length; i++)
        {
            expected = expectedTypes[i];
            actual = parameters[i].type;
            if (!expected.equals(actual))
                throw new CompileException(
                    ErrorString.invalidParameterType(name, expected, actual, i + 1));
        }
    }

    /**
     * Calls the procedure with some parameters and a parent environment
     * 
     * @param env        the (parent) execution environment
     * @param parameters the passed in parameters from the call
     * @throws BreakException     if a break statement is executed, let it bubble
     * @throws ContinueException  if a continue statement is executed, let it bubble
     * @throws InterpretException if there is an error during execution
     * @return the returned value of the procedure, if any
     */
    public Value callProcedure(InterpreterEnvironment env, Expression[] parameters)
         throws BreakException, ContinueException, InterpretException
    {
        // InterpreterEnvironment childEnv = env.isGlobal() ? env : new InterpreterEnvironment(env);

        // idk why I did that ^
        InterpreterEnvironment childEnv = new InterpreterEnvironment(env);

        // setting the parameter values in the child environment
        for (int i = 0; i < paramNames.length; i++)
        {
            childEnv.declareVariable(paramTypes[i], paramNames[i]);

            childEnv.setVariable(paramNames[i], parameters[i].eval(env));
        }

        // setting the return value variable (same name as the procedure)
        childEnv.declareVariable(returnType, name);

        stmt.exec(childEnv);

        return childEnv.getVariable(name); // return value
    }

    /**
     * @return the body statement of the procedure
     */
    public Statement getStmt()
    {
        return stmt;
    }

    /**
     * @return the array of strings denoting the names of the procedure's parameters
     */
    public String[] getParamNames()
    {
        return paramNames;
    }

    /**
     * @return the array of strings denoting the types of the procedure's parameters
     */
    public String[] getParamTypes()
    {
        return paramTypes;
    }

    /**
     * @return the return type of the procedure, denoted as a string
     */
    public String getReturnType()
    {
        return returnType;
    }

    /**
     * Compiles the procedure declaration
     * 
     * @param e the emitter to use
     */
    public void compile(Emitter e)
    {
        e.startCompilingProcedureDeclaration(paramNames, name);

        e.emit("j _end"+name); // skip procedure during runtime

        e.emit(name+":");

        e.emit("li $v0 0");
        e.emitPush("$v0");

        e.pushRA();

        stmt.compile(e);

        e.popRA();          // return address popped
        e.popReturnValue(); // return value popped into v0

        // now clear out all the parameters/local variables and the return address
        // from the stack
        e.emit("# Clearing parameters/local variables and return address from stack");
        e.clearProcedureVarsFromStack();

        e.emit("jr $ra");

        e.emit("_end"+name+":");

        e.stopCompilingProcedureDeclaration();
    }
}