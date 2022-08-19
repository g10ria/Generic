package com.pilers.emitter;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Emitter class for writing to an assembly file
 * 
 * @author Anu Datar
 * @author Gloria Zhu
 * @version 8/19/22
 */
public class Emitter
{
    private PrintWriter out;

    private int currLabel;

    private Map<String, String> globalVars; // global variables

    private boolean isCompilingProcedure;

    private List<String> procedureParams;
    private String procedureName;
    
    private List<String> procedureVars;

    private boolean isCompilingBinOp;

    // used for calling nested procedures (need to offset stack because of pushed parameters)
    // see parsertest13 for an example
    private int pushedParams;

    /**
     * Creates an emitter for writing to a new file with given name
     * 
     * @param outputFileName the output file name
     * @param globalVars     hashmap of data (name --> type)
     */
    public Emitter(String outputFileName, Map<String, String> globalVars)
    {
        this.globalVars = globalVars;
        currLabel = 0; 
        pushedParams = 0;
        this.procedureVars = new ArrayList<String>();
        try 
        {
            out = new PrintWriter(new FileWriter(outputFileName), true);
        }
        catch (IOException e) 
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the next valid label to use.
     * Labels are numbers prefixed with _ so that they
     * don't collide with subroutines. They start from 0.
     * @return a string of the next valid label
     */
    public String nextLabel()
    {
        currLabel++;
        return "_"+(currLabel-1);
    }

    /**
     * Tells the emitter that it is currently compiling a procedure declaration.
     * This is needed so that the emitter knows, when compiling variable references,
     * whether to load/store words from the stack, or if they are in global scope
     * and should load/store from .data Passes in the names of the parameters as
     * well as the name of the procedure name (to be used as the name of the return
     * value)
     * 
     * @param params   parameter names of the currently-being-compiled procedure
     *                 declaration
     * @param procName name of the procedure currently being compiled
     */
    public void startCompilingProcedureDeclaration(String[] params, String procName)
    {
        this.isCompilingProcedure = true;
        this.procedureParams = Arrays.asList(params);
        this.procedureName = procName;
    }

    /**
     * Adds a "pushed param" (for compiling nested procedure calls)
     */
    public void addPushedParam()
    {
        pushedParams++;
    }

    /**
     * Resets the "pushed param" count
     */
    public void resetPushedParams()
    {
        pushedParams = 0;
    }

    /**
     * Lets the emitter know it's in the middle of compiling a binop
     * This is needed because the binop pushes the first value to
     * the stack, so all references to variables on the stack
     * need to be offset by those 4 bytes
     */
    public void startCompilingBinOp()
    {
        this.isCompilingBinOp = true;
    }

    /**
     * Stops compiling the bin op
     */
    public void stopCompilingBinOp()
    {
        this.isCompilingBinOp = false;
    }

    /**
     * Tells the emitter that it has finished compiling a procedure declaration.
     * Resets the relevant instance variables.
     */
    public void stopCompilingProcedureDeclaration()
    {
        this.isCompilingProcedure = false;
        this.procedureParams = null;
        this.procedureName = "";
        this.procedureVars.clear();
    }

    /**
     * Adds a local variable to the procedureVars list
     * @param name name of the variable
     */
    public void addLocalVariable(String name)
    {
        procedureVars.add(name);
    }

    /**
     * Returns if the variable with the current name is a global variable or not.
     * First sees if the current statements being compiled are in global scope (if so,
     * it must be a global variable). Then, if it is in limited scope (inside a
     * procedure declaration compilation), tests if any of the parameters or if
     * the return value have the same name, and if not, returns true as well.
     * 
     * @param name name of the variable being tested
     * @return if the variable is a global reference or not
     */
    public boolean isGlobalVariable(String name)
    {
        return !isCompilingProcedure || 
            !procedureParams.contains(name) && 
            !procedureName.equals(name) &&
            !procedureVars.contains(name);
    }

    /**
     * @return if the program is currently in the midst of compiling a procedure declaration
     */
    public boolean isCompilingProcedure()
    {
        return isCompilingProcedure;
    }

    /**
     * Returns the index of the variable as a parameter inside the procedure. Allows
     * for retrieval of values from the stack when calling the procedure.
     * Parameters are pushed like this:
     * left to right
     * push a byte for the return value
     * push the return address
     * 
     * then, any local variable declarations are pushed whenever they come up
     * 
     * Therefore the index of the return value is 1 + numLocalVars, and the index of the
     * parameters goes backwards from there.
     * 
     * @precondition the variable with this name is a local reference, not a global one
     * @param name name of the variable in question
     * @return the index of the variable in the parameter list
     */
    public int getLocalVarIndex(String name)
    {
        int offset = isCompilingBinOp ? 1 : 0;
        offset += pushedParams;

        int numLocalVars = procedureVars.size();

        // if it's the return parameter
        if (name.equals(procedureName)) return 1 + numLocalVars + offset;

        // if it's one of the parameters
        if (procedureParams.contains(name))
            return procedureParams.size() - procedureParams.indexOf(name) + 
                1 + numLocalVars + offset;

        // if it's a locally declared variable
        return procedureVars.size() - procedureVars.indexOf(name) - 1 + offset;
    }

    /**
     * Loads a given string into v0. Allows for less repitition between
     * places with string loading.
     * 
     * Space is allocated for strings as such:
     * 4 bytes that store the length of the string
     *      this will (hopefully) allow us to concatenate strings/get str lengths quickly
     *      (in the future, not implemented yet)
     * n bytes that store the actual string
     * 1 byte for a null terminator
     * 
     * @param value the string in question
     */
    public void loadString(String value)
    {
        int len = value.length();

        emit("li $v0 9"); // instruction to allocate space
        // allocate enough space for strlength + str + null terminator
        emit("li $a0 " + (len + 5));
        emit("syscall"); // memory address of allocated space is in v0

        // read in each character of the string byte by byte
        char c;
        char[] cArray = value.toCharArray();

        // load string length into first 4 bytes
        emit("li $t0 " + len);
        emit("sw $t0 ($v0)");

        // load in actual string
        int i = 4;
        for (; i < value.length() + 4; i++)
        {
            c = cArray[i - 4];
            if (c!='\'') emit(String.format("li $t0 '%c'", c));
            else emit("li $t0 '\\''");
            emit(String.format("sb $t0 %d($v0)", i));
        }

        // load in null term
        emit(String.format("li $t0 '%c'", '\0'));
        emit(String.format("sb $t0 %d($v0)", i));
    }

    /**
     * Helper method to push the return address to the stack
     */
    public void pushRA()
    {
        emit("subu $sp $sp 4 # Pushing return address");
        emit("sw $ra ($sp)");
    }

    /**
     * Helper method to pop the return address from the stack
     */
    public void popRA()
    {
        int numLocalVars = procedureVars.size();
        emit("lw $ra "+numLocalVars*4+"($sp)");
    }

    /**
     * Helper method to pop the return value into v0
     * 
     * This doesn't need to consider BinOp offset - return
     * value is only popped at the very end of a procedure,
     * never inside of any other expression (I hope)
     */
    public void popReturnValue()
    {
        
        int index = getLocalVarIndex(procedureName);
        emit("lw $v0 "+(index)*4 +"($sp)");
    }

    /**
     * Clears all procedure variables out to save stack space
     * (Just loads everything into a0)
     */
    public void clearProcedureVarsFromStack()
    {
        // Number of local variables + number of parameters + return value + return address
        int thingsToClear = procedureVars.size() + procedureParams.size() + 2;
        for(;thingsToClear>0;thingsToClear--) 
            emitPop("$a0");
    }

    /**
     * Prints one line of code to file (with non-labels indented)
     * Appends a newline
     * @param code the line of code
     */
    public void emit(String code) 
    {
        if (!code.endsWith(":"))
            code = "\t" + code;
        out.println(code);
    }

    /**
     * Prints an array of lines of code
     * Calls emit for a singular string
     * @param codes the lines of code
     */
    public void emit(String[] codes)
    {
        for(String s : codes) emit(s);
    }

    /**
     * Emits push commands
     * @param reg the register to push from
     */
    public void emitPush(String reg)
    {
        emit("subu $sp $sp 4 # Pushing "+reg); // move stack up 4 bytes
        emit("sw "+reg+" ($sp)"); // push value onto stack
    }

    /**
     * Emits pop commands
     * @param reg the register to pop to
     */
    public void emitPop(String reg)
    {
        emit("lw "+reg+" ($sp) # Popping "+reg); // pop $v0
        emit("addu $sp $sp 4"); // move stack down 4 bytes
    }

    // These are used for compiling relational BinOps
    // For example: 3<4, 5<6, etc.
    // They're kind of like mini subroutines without parameters

    // I did this so there wouldn't be a bunch of repetition when
    // compiling these binops, since the same series of like 5 lines
    // would be repeated many times

    // The first argument is in t0, and the second argument is in v0 always

    /**
     * less than or equal to
     */
    public void emitLEQ()
    {
        emit("_LEQ:");
        emit("ble $t0 $v0 _TRUE");
        emit("j _FALSE");
    }

    /**
     * less than
     */
    public void emitLT()
    {
        emit("_LT:");
        emit("blt $t0 $v0 _TRUE");
        emit("j _FALSE");
    }

    /**
     * greater than or equal to
     */
    public void emitGEQ()
    {
        emit("_GEQ:");
        emit("bge $t0 $v0 _TRUE");
        emit("j _FALSE");
    }

    /**
     * greater than
     */
    public void emitGT()
    {
        emit("_GT:");
        emit("bgt $t0 $v0 _TRUE");
        emit("j _FALSE");
    }

    /**
     * equal to
     */
    public void emitE()
    {
        emit("_E:");
        emit("beq $t0 $v0 _TRUE");
        emit("j _FALSE");
    }

    /**
     * not equal to
     */
    public void emitNE()
    {
        emit("_NE:");
        emit("bne $t0 $v0 _TRUE");
        emit("j _FALSE");
    }

    /**
     * Puts true boolean value into v0,
     * then jumps to return address
     */
    public void emitTrue()
    {
        emit("_TRUE:");
        emit("li $v0 1");
        emit("jr $ra");   
    }

    /**
     * Puts false boolean value into v0,
     * then jumps to return address
     */
    public void emitFalse()
    {
        emit("_FALSE:");
        emit("li $v0 0");
        emit("jr $ra");
    }

    /**
     * TODO print "TRUE" or "FALSE" for booleans and not 1 and 0
     */
    public void emitPrintTRUE()
    {
        emit("TRUE:");
        emitPush("$a0");
        emitPush("$v0");
        emit("li $a0 1");
        emit("li $v0 1");
        emit("syscall");
        emitPop("$v0");
        emitPop("$a0");
        emit("jr $ra");
    }

    /**
     * Private helper method for getting the MIPS representation of types
     * @param in our type representation
     * @return the MIPS representation
     */
    private String formatType(String in)
    {
        return "word"; // for now
    }

    /**
     * @param type type of the variable
     * @return the default value of the given variable type
     */
    private String getDefaultValue(String type)
    {
        return "0"; // for now
    }

    /**
     * Emits all (pre-allocated) program data
     * (for the .data section)
     * Appends "_data_" to the beginning of their names
     */
    public void emitProgramData()
    {
        for (Map.Entry<String, String> entry : globalVars.entrySet())
        { 
            String varname = entry.getKey();
            String vartype = entry.getValue();

            String defaultVal = getDefaultValue(vartype);
            vartype = formatType(vartype);            

            emit(String.format("%s: .%s %s", "_data_"+varname, vartype, defaultVal));
        }
    }

    /**
     * Closes the file, should be called after all calls to emit
     */
    public void close()
    {
        out.close();
    }
}