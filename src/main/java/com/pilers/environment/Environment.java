package com.pilers.environment;

import java.util.HashMap;
import java.util.Map;

import com.pilers.ast.*;

/**
 * This is the parent class of both environment classes; it has been abstracted
 * because there is a lot of shared functionality between SemanticAnalysisEnv
 * and InterpreterEnv.
 * 
 * It keeps track of variable names, types, and values; procedure names
 * and values; and its parent environment.
 * 
 * Note: the SemanticAnalysisEnvironment does not ever use the value map (varmapVal),
 * only the InterpreterEnvironment does.
 * 
 * @author Gloria Zhu
 * @version 8/19/22
 */
public abstract class Environment
{

    /**
     * This maps variable names to their types (represented as strings)
     */
    protected Map<String, String> varmapType;
    /**
     * This maps variable names to their values
     */
    protected Map<String, Value> varmapVal;

    /**
     * This maps procedure names to their declarations
     */
    protected Map<String, ProcedureDeclaration> procmap;

    /**
     * This is the parent environment of this current env.
     * Is null if this is the global env.
     */
    protected Environment parentEnv;

    /**
     * Creates a new environment with a parent env
     * 
     * @param parentEnv the parent env
     */
    public Environment(Environment parentEnv)
    {
        this.parentEnv = parentEnv;
        varmapType = new HashMap<String, String>();
        varmapVal = new HashMap<String, Value>();
        procmap = new HashMap<String, ProcedureDeclaration>();
    }

    /**
     * Sets a procedure declaration
     * 
     * @param name      name of the procedure
     * @param procedure value of the procedure
     */
    public void setProcedure(String name, ProcedureDeclaration procedure)
    {
        procmap.put(name, procedure);
    }

    /**
     * @return if this environment is the global env or not
     */
    public boolean isGlobal()
    {
        return this.parentEnv == null;
    }

    /**
     * Gets a procedure's declaration value Searches up through the environments
     * until it either finds the procedure or reaches the "null" environment (goes
     * past the global environment)
     * 
     * @param name name of the procedure
     * @return the value of the procedure, or null if not found
     */
    public ProcedureDeclaration getProcedure(String name)
    {
        Environment current = this;
        ProcedureDeclaration proc = null;
        while (current != null && proc == null)
        {
            proc = current.procmap.get(name);
            current = current.parentEnv;
        }
        return proc;
    }

    /**
     * Declares a variable with a given type and name; if the isInterpreter flag
     * is set to true, also gives the variable a default value in the value map.
     * @param type the type of the variable, represented as a string
     * @param name the name of the variable
     * @param isInterpreter if the environment invoking this is an interpreter,
     *                      give the variable a default value in the value map
     * @return if the variable was successfully declared or not (if the variable
     * already existed, this would fail)
     */
    public boolean declareVariable(String type, String name, boolean isInterpreter)
    {
        if (this.varmapType.get(name)!=null) return false;

        varmapType.put(name, type);
        if (isInterpreter)
        {
            if (type.equals("Integer"))
                varmapVal.put(name, new Value(0));
            else if (type.equals("String"))
                varmapVal.put(name, new Value(""));
            else if (type.equals("Boolean"))
                varmapVal.put(name, new Value(false));
        }

        return true;
    }

}