package com.pilers.errors;

/**
 * The purpose of this class is to format error strings for the
 * CompileException and InterpretException classes. It has been separated
 * into its own class in order to standardize error formatting.
 * 
 * @author Gloria Zhu
 */
public abstract class ErrorString
{
    /**
     * @param varname identifier
     * @return error message for an unknown identifier
     */
    public static String unknownIdentifier(String varname)
    {
        return "Unknown identifier " + varname;
    }

    /**
     * @param procname procedure name
     * @return error message for an unknown procedure
     */
    public static String unknownProcedure(String procname)
    {
        return "Unknown procedure " + procname;
    }

    /**
     * @param directiveName directive name
     * @return error message for an unknown directive (i.e. #hello)
     */
    public static String unknownDirective(String directiveName)
    {
        return "Unknown directive " + directiveName;
    }

    /**
     * @param structname struct name
     * @return error message for a struct that isn't declared globally
     */
    public static String structsMustBeGlobal(String structname)
    {
        return "struct " + structname + " must be declared globally";
    }

    /**
     * @param structname struct name
     * @return error message for a duplicate struct declaration
     */
    public static String duplicateStructName(String structname)
    {
        return "Duplicate struct found for name " + structname;
    }

    /**
     * @param definitionVar variable being defined
     * @return error message for a duplicate defind directive
     */
    public static String duplicateDefine(String definitionVar)
    {
        return "Duplicate define directive for " + definitionVar;
    }

    /**
     * @param expectedType the expected type
     * @param actualType   the actual type
     * @return error message for an invalid type assignemnt
     */
    public static String typeAssignment(String expectedType, String actualType)
    {
        return String.format("Type error: expected %s, got %s", expectedType, actualType);
    }

    /**
     * @param type1   the first type
     * @param type2   the second type
     * @param operand operand represented as a string
     * @return error message for an invalid binary operation (i.e. BOOLEAN *
     *         INTEGER)
     */
    public static String invalidBinOperation(String type1, String type2, String operand)
    {
        return String.format("Cannot perform %s operation between %s and %s",
        operand, type1, type2);
    }

    /**
     * @param type the type of the operand
     * @param operand operand represented as a string
     * @return error message for an invalid unary operation (i.e. BOOLEAN++)
     */
    public static String invalidUnaryOperation(String type, String operand)
    {
        return String.format("Cannot perform %s operation on %s", operand, type);
    }

    /**
     * @param varname the name of the identifier
     * @return error message for a duplicate identifier (i.e. if a variable has
     *         already been declared)
     */
    public static String duplicateIdentifier(String varname)
    {
        return "Duplicate identifier found for name " + varname;
    }

    /**
     * @return error message for if a FOR loop's condition is not a boolean
     */
    public static String forLoopConditionInvalid()
    {
        return "Error: FOR loop condition must evaluate to a boolean";
    }

    /**
     * @return error message for if a WHILE loop's condition is not a boolean
     */
    public static String whileLoopConditionInvalid()
    {
        return "Error: WHILE loop condition must evaluate to a boolean";
    }

    /**
     * @return error message for if a IF loop's condition is not a boolean
     */
    public static String ifLoopConditionInvalid()
    {
        return "Error: IF loop condition must evaluate to a boolean";
    }

    /**
     * @param procedureName name of the procedure
     * @param expectedNumber the expected number of parameters
     * @param actualNumber the actual number of parameters given
     * @return error message for having the wrong number of paramters in a
     *          procedure call
     */
    public static String wrongNumberOfParameters(
            String procedureName, int expectedNumber, int actualNumber)
    {
        return String.format("Wrong number of parameters for procedure %s() call: "+
        "expected %d, got %d", procedureName, expectedNumber, actualNumber);
    }

    /**
     * @param procedureName name of the procedure
     * @param expectedType expected type of the procedure
     * @param actualType the actual type given
     * @param index the index of the parameter (if it's the 1st, 2nd one etc.)
     * @return error message for passing in the wrong type of parameter during 
     *          a procedure call
     */
    public static String invalidParameterType(
            String procedureName, String expectedType, String actualType, int index)
    {
        return String.format("Invalid type for parameter %d for procedure %s() call: "+
        "expected %s, got %s", index, procedureName, expectedType, actualType);
    }

    /**
     * @param procedure the procedure name
     * @param param the param name
     * @return error message for having duplicate parameter names in a procedure declaration
     */
    public static String duplicateParamNames(String procedure, String param)
    {
        return String.format("Duplicate parameter name '%s' in procedure declaration %s()",
        param, procedure);
    }

    /**
     * @param name the procedure/parameter name
     * @return error message for having a parameter with the same name as the procedure
     */
    public static String paramNameEqualsProcedureName(String name)
    {
        return String.format("Parameter name cannot equal name of procedure: "+
        "error %s() declaration", name);
    }
}