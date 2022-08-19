package com.pilers.ast;

import com.pilers.environment.*;
import com.pilers.errors.ErrorString;
import com.pilers.errors.InterpretException;
import com.pilers.errors.CompileException;
import com.pilers.emitter.*;

/**
 * AST BinOp class
 * Represents a binary operation between two values
 * 
 * BinOp sets type during semantic analysis
 * 
 * @author Gloria Zhu
 */
public class BinOp extends Expression
{
    private String op;
    private Expression exp1;
    private Expression exp2;

    /**
     * Constructs a BinOp object
     * 
     * Deduces the resulting type of the BinOp from the two types
     * of the expressions as well as the operand. If the combination
     * of types/operand is invalid, ignores it for now - it will either
     * be caught in semantic analysis (in the case of compiling) or
     * it will throw a runtime exception (in the case of interpreting).
     * (Just set the type to an empty string)
     * 
     * @param op   operator in string form
     * @param exp1 left expression
     * @param exp2 right expression
     */
    public BinOp(String op, Expression exp1, Expression exp2)
    {
        this.op = op;
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    /**
     * Evaluates the binary operation according to the types of its values and what
     * its operation is. Not all type combinations are valid, and not all operations
     * are defined for all type combinations.
     * 
     * For example, Integer << Integer is a valid binary operation, but String <<
     * Integer is not.
     * 
     * If a set of values and an operation doesn't satisfy any of the valid
     * permutations, an error should be thrown (it currently isn't)
     * 
     * @param env the execution environment
     * @return the Value of the binary operation
     * @throws BreakException    if a break statement is executed
     * @throws ContinueException if a continue statement is executed
     * @throws InterpretException  if there is a runtime exception in execution
     */
    public Value eval(InterpreterEnvironment env) 
        throws BreakException, ContinueException, InterpretException
    {
        Value value1 = exp1.eval(env); // evaluate the two values
        Value value2 = exp2.eval(env);

        String type1 = value1.type;
        String type2 = value2.type;
        
        switch(type1+" "+type2) // the type of output depends on the type of input
        {

            /**
             * Standard operations: 
             * mathematical (Integer result)
             * relational (Boolean result)
             * bitwise (Integer result)
             */
            case("Integer Integer"):
                int case1Val1 = Integer.parseInt(value1.getValue());
                int case1Val2 = Integer.parseInt(value2.getValue());

                switch(op)
                {
                    // arithmetic operators
                    case ("+"): 
                        type = "Integer";
                        return new Value(case1Val1 + case1Val2);
                    case ("-"): 
                        type = "Integer";
                        return new Value(case1Val1 - case1Val2);
                    case ("*"): 
                        type = "Integer";
                        return new Value(case1Val1 * case1Val2);
                    case ("/"): 
                        type = "Integer";
                        return new Value(case1Val1 / case1Val2);
                    case ("%"): 
                        type = "Integer";
                        return new Value(case1Val1 % case1Val2);

                    // relational operators
                    case("<="): 
                        type = "Boolean";
                        return new Value(case1Val1 <= case1Val2);
                    case(">="): 
                        type = "Boolean";
                        return new Value(case1Val1 >= case1Val2);
                    case("<"): 
                        type = "Boolean";
                        return new Value(case1Val1 < case1Val2);
                    case(">"): return new Value(case1Val1 > case1Val2);
                    case("!="): 
                        type = "Boolean";
                        return new Value(case1Val1 != case1Val2);
                    case("=="): 
                        type = "Boolean";
                        return new Value(case1Val1 == case1Val2);

                    // bitwise operators
                    case("&"): 
                        type = "Integer";
                        return new Value(case1Val1 & case1Val2);
                    case("|"): 
                        type = "Integer";
                        return new Value(case1Val1 | case1Val2);
                    case("<<"): 
                        type = "Integer";
                        return new Value(case1Val1 << case1Val2);
                    case(">>"): 
                        type = "Integer";
                        return new Value(case1Val1 >> case1Val2);
                    case("^"): 
                        type = "Integer";
                        return new Value(case1Val1 ^ case1Val2);
                }

                /**
                 * String + Integer concatenates the integer and returns a string
                 * String * Integer returns the String repeated Integer amount of times
                 */
            case("String Integer"):
                String case2Val1 = value1.getValue();
                int case2Val2 = Integer.parseInt(value2.getValue());
                type = "String";
                switch(op)
                {
                    case("+"): return new Value(case2Val1 + case2Val2, "String");
                    case("*"):
                        String ret = "";
                        for(int i=0;i<case2Val2;i++) ret += case2Val1;
                        return new Value(ret, "String");
                }
            
                /**
                 * Same as above
                 */    
            case("Integer String"):
                int case3Val1 = Integer.parseInt(value1.getValue());
                String case3Val2 = value2.getValue();
                type = "String";
                switch(op)
                {
                    case("+"): return new Value("" + case3Val1 + case3Val2);
                    case("*"):
                        String ret = "";
                        for(int i=0;i<case3Val1;i++) ret += case3Val2;
                        return new Value(ret, "String");
                }

                /**
                 * String + String returns the String concatenation
                 * String = String returns a Boolean if they are equal
                 */
            case ("String String"):
                String case4Val1 = value1.getValue();
                String case4Val2 = value2.getValue();
                switch(op)
                {
                    case("+"): 
                        type = "String";
                        return new Value(case4Val1 + case4Val2);
                    case("=="): 
                        type = "Boolean";
                        return new Value(case4Val1.equals(case4Val2));
                }

                /**
                 * Standard logical operators
                 */
            case ("Boolean Boolean"):
                boolean case5Val1 = value1.getValue().equals("TRUE");
                boolean case5Val2 = value2.getValue().equals("TRUE");
                switch(op)
                {
                    case("&&"): 
                        type = "Boolean";
                        return new Value(case5Val1 && case5Val2);
                    case("||"): 
                        type = "Boolean";
                        return new Value(case5Val1 || case5Val2);
                    case("^"): 
                        type = "Boolean";
                        return new Value(case5Val1 ^ case5Val2);
                }

            default:    // this should throw an error eventually
                return null;
        }
    }

    /**
     * Performs semantic analysis on this
     * Checks that the combination of types and operand is valid
     * 
     * @throws CompileException if the above condition is broken
     * @param env the current env
     */
    public void analyze(SemanticAnalysisEnvironment env) throws CompileException
    {
        exp1.analyze(env);
        exp2.analyze(env);

        String type1 = exp1.type;
        String type2 = exp2.type;

        switch (type1 + " " + type2)
        {
            case ("Integer Integer"):
                switch (op)
                {
                    case ("+"):
                    case ("-"):
                    case ("*"):
                    case ("/"):
                    case ("%"):
                    case ("&"):
                    case ("|"):
                    case ("<<"):
                    case (">>"):
                    case ("^"):
                        type = "Integer";
                        break;

                    case ("<="):
                    case (">="):
                    case ("<"):
                    case (">"):
                    case ("!="):
                    case ("=="):
                        type = "Boolean";
                        break;

                    default:
                        throw new CompileException(
                            ErrorString.invalidBinOperation(exp1.type, exp2.type, op));
                }
                break;

            case ("Integer String"):
            case ("String Integer"):
                switch (op)
                {
                    case ("+"):
                    case ("*"):
                        type = "String";
                        break;
                    default:
                        throw new CompileException(
                            ErrorString.invalidBinOperation(exp1.type, exp2.type, op));
                }
                break;

            case ("String String"):
                switch (op)
                {
                    case ("+"):
                        type = "String";
                        break;
                    case ("=="):
                        type = "Boolean";
                        break;

                    default:
                        throw new CompileException(
                            ErrorString.invalidBinOperation(exp1.type, exp2.type, op));
                }
                break;

            case ("Boolean Boolean"):
                switch (op)
                {
                    case ("&&"):
                    case ("||"):
                    case ("^"):
                        type = "Boolean";
                        break;

                    default:
                        throw new CompileException(
                            ErrorString.invalidBinOperation(exp1.type, exp2.type, op));
                }
                break;

            default:
                throw new CompileException(
                    ErrorString.invalidBinOperation(exp1.type, exp2.type, op));
        }
            
    }

    /**
     * Compiles the BinOp
     * 
     * NOTE: This assumes no overflow for integer multiplication, and
     * it will also floor integer divide results
     * 
     * @param e the given emitter
     */
    public void compile(Emitter e)
    {
        e.emit("# Compiling BinOp components");
        exp1.compile(e);

        e.emitPush("$v0");

        e.startCompilingBinOp();

        exp2.compile(e);

        e.emitPop("$t0");

        e.stopCompilingBinOp();

        // first argument in t0, second argument in v0

        String type1 = exp1.type;
        String type2 = exp2.type;

        e.emit("# Beginning BinOp compilation");

        switch(type1+" "+type2) // the type of output depends on the type of input
        {

            /**
             * Standard operations: 
             * mathematical (Integer result)
             * relational (Boolean result)
             * bitwise (Integer result)
             */
            case("Integer Integer"):
                switch(op)
                {
                    // arithmetic operators
                    case ("+"): e.emit("addu $v0 $t0 $v0"); break;
                    case ("-"): e.emit("subu $v0 $t0 $v0"); break;
                    case ("*"):
                        e.emit("multu $v0 $t0");
                        e.emit("mflo $v0");
                        break;
                    case ("/"):
                        e.emit("divu $v0 $t0");
                        e.emit("mflo $v0");
                        break;
                    case ("%"):
                        e.emit("divu $v0 $t0");
                        e.emit("mfhi $v0");
                        break;

                    // relational operators - TODO
                    case("<="): e.emit("jal _LEQ"); break;
                    case(">="): e.emit("jal _GEQ"); break;
                    case("<"): e.emit("jal _LT"); break;
                    case(">"): e.emit("jal _GT"); break;
                    case("!="): e.emit("jal _NE"); break;
                    case("=="): e.emit("jal _E"); break;

                    // bitwise operators
                    case("&"): e.emit("and $v0 $t0 $v0"); break;
                    case("|"): e.emit("or $v0 $t0 $v0"); break;
                    case("<<"): e.emit("sllv $v0 $t0 $v0"); break;
                    case(">>"): e.emit("srlv $v0 $t0 $v0"); break;
                    case("^"): e.emit("xor $v0 $t0 $v0"); break;
                }
                break;

            
            /**
            * Same as below (just swap places before fall-through)
            */    
            case("Integer String"):
                // todo
                // e.emit("move $t1 $v0");
                // e.emit("move $v0 $t0");
                // e.emit("move $t0 $t1");
                break;

                /**
                 * String + Integer concatenates the integer and returns a string
                 * String * Integer returns the String repeated Integer amount of times
                 */
            case("String Integer"):
                // todo
                // switch(op)
                // {
                //     case("+"): // todo
                //         // find the number of digits in the integer

                //         e.emit("li $t1 0"); // t1 is the counter

                //         // basically a while loop
                //         String whileStart = e.nextLabel();
                //         String whileStop = e.nextLabel();
                //         e.emit(whileStart+": ");
                //         //e.emit("")
                //         break;

                //     case("*"): // todo
                //         e.emit("li $t1 ($v0)");     // length of old string in t1
                //         e.emit("multu $t0 $t1");
                //         e.emit("mflo $t2");         // length of new string in t2
                //         e.emit("li $t3 1");         // t3 = 1 (used for decrementing)
                //         e.emit("move $t5 $v0");     // old string's address in t5
                //         // string address in v0
                //         // use t0 as the counter (for # of times to "add" the string)
                //         // use t4 as the internal counter (to iterate over each character)

                //         // allocate space for string
                //         e.emit("li $v0 9");
                //         // allocate enough space for strlength + str + null terminator
                //         e.emit("addiu $a0 $t2 5");
                //         e.emit("syscall"); // memory address of newly allocated space is in v0

                //         String keepGoingLabel = e.nextLabel();
                //         String stopLabel = e.nextLabel();

                //         String keepGoingLabelNested = e.nextLabel();
                //         String stopLabelNested = e.nextLabel();
                        

                //         e.emit(keepGoingLabel + ": ");
                //         e.emit("beq $t0 0 " + stopLabel); // stop when t0 hits 0

                //         // concat the string
                //         e.emit("li $t4 0"); // t4 is the counter
                //         e.emit(keepGoingLabelNested + ": ");
                //         e.emit("beq $t4 $t1 " + stopLabelNested); // stop when t4==t1
                //         e.emit("multu $t0 $t1");
                //         e.emit("mflo $t6");
                //         e.emit("addu $t6 $t6 4");   // need to add 4 because of offset
                //         e.emit("sb $t6($t5) $t6($v0)");
                //         e.emit("addiu $t4 1");
                //         // concat the current character and decrement t4
                //         e.emit("j " + keepGoingLabelNested);
                //         e.emit(stopLabelNested+":");


                //         e.emit("subu $t0 $t0 $t3"); // decrement t0
                //         e.emit("j " + keepGoingLabel);
                //         e.emit(stopLabel + ":");
                // }
                break;
            
                

                /**
                 * String + String returns the String concatenation
                 * String = String returns a Boolean if they are equal
                 */
            case ("String String"):
                // todo
                break;

                /**
                 * Standard logical operators
                 */
            case ("Boolean Boolean"):
                switch(op)
                {
                    case ("&&"): e.emit("and $v0 $t0 $v0"); break;
                    case ("||"): e.emit("or $v0 $t0 $v0"); break;
                    case ("^"): e.emit("xor $v0 $t0 $v0"); break;
                }
                break;
        }
    }
}