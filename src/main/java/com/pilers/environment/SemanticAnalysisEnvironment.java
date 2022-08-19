package com.pilers.environment;

import java.util.Map;

import com.pilers.errors.*;

/**
 * The SemanticAnalysisEnvironment is to be used in semantic
 * analysis of the program. This includes things like type checking,
 * seeing if a variable exists, etc.
 * 
 * Potential future additions (to act as a linter):
 * Checking for empty control statements and sending warnings
 * Checking for never-reached code and sending warnings
 * 
 * @author Gloria Zhu
 * @version 8/19/22
 */
public class SemanticAnalysisEnvironment extends Environment 
{
    /**
     * Creates a new environment with a parent env
     * 
     * @param parentEnv the parent env
     */
    public SemanticAnalysisEnvironment(SemanticAnalysisEnvironment parentEnv)
    {
        super(parentEnv);
    }

    /**
     * Factory method to return a new global environment (parent is null)
     * 
     * @return the new env
     */
    public static SemanticAnalysisEnvironment newGlobalEnv()
    {
        return new SemanticAnalysisEnvironment(null);
    }

    /**
     * Declares a space by allocating space for it in the variable map
     * 
     * @param type the type of the variable
     * @param name the name of the variable
     * @throws CompileException if the variable is already defined
     */
    public void declareVariable(String type, String name) throws CompileException
    {
        if (!declareVariable(type, name, false)) throw new CompileException("TODO: WRITE ME");
    }

    /**
     * @param varname the name of the variable
     * @return if the variable with the given name exists at the moment
     */
    public boolean variableExists(String varname)
    {
        return getVariableType(varname) != null;
    }

    /**
     * Gets the type of a variable as a string
     * 
     * @param name the name of the variable
     * @return null if the variable does not exist
     */
    public String getVariableType(String name)
    {
        // Environment current = this;
        // String var = current.varmapType.get(name);

        // while (current.parentEnv != null && var == null)
        // {
        //     current = current.parentEnv;
        //     var = current.varmapType.get(name);
        // }

        // return var;

        return this.varmapType.get(name);
    }

    /**
     * @return the map of variable names to types
     */
    public Map<String, String> getVariableInfo()
    {
        return varmapType;
    }
}