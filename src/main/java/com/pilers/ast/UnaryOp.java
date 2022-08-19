package com.pilers.ast;

import com.pilers.emitter.Emitter;
import com.pilers.environment.*;
import com.pilers.errors.ErrorString;
import com.pilers.errors.InterpretException;
import com.pilers.errors.CompileException;

/**
 * AST UnaryOp Class
 * 
 * A unary operation is one that operates on one value only (vs a binary
 * operation that operates on two)
 * 
 * examples: !, -, ~
 * 
 * Assigns type during semantic analysis
 * 
 * @author Gloria Zhu
 */

public class UnaryOp extends Expression
{
    private String op;
    private Expression expr;

    /**
     * Constructs a UnaryOp object
     * 
     * @param op   the operation
     * @param expr the expression
     */
    public UnaryOp(String op, Expression expr)
    {
        this.op = op;
        this.expr = expr;
    }

    /**
     * Performs semantic analysis for the unary operation
     * Checks that the combination of expression and operation is valid
     * 
     * Note: this assumes that a unary operation will never change
     * the type of its operand
     * 
     * @throws CompileException if the above condition is broken
     * @param env the current env
     */
    public void analyze(SemanticAnalysisEnvironment env) throws CompileException
    {
        expr.analyze(env);
        type = expr.type;

        switch(type)
        {
            /**
             * A note: this only supports ++x and --x, not x++ and x-- at the moment. In
             * addition, ++1 does not actually increment x, it just returns the value x+1 -
             * known bug, to do: fix
             */
            case ("Integer"):
                switch (op)
                {
                    case ("-"):
                    case ("++"):
                    case ("--"):
                    case ("~"):
                        break;
                    default:
                        throw new CompileException(
                            ErrorString.invalidUnaryOperation(type, op));
                }
                break;

            case ("Boolean"):
                switch (op)
                {
                    case ("!"):
                        break;
                    default:
                        throw new CompileException(
                            ErrorString.invalidUnaryOperation(type, op));
                }
                break;

            default:
                throw new CompileException(ErrorString.invalidUnaryOperation(type, op));
        }
    }

    /**
     * Evaluates the operation according to the operation and the Value of the
     * Expression
     * 
     * For example, -Integer returns a negated integer Not all operations are
     * defined, for example -Boolean does not work
     * 
     * If nothing matches, it should throw an interpret exception
     * 
     * @param env evaluation environment
     * @return Value the final value
     * @throws BreakException     if a break statement is executed
     * @throws ContinueException  if a continue statement is executed
     * @throws InterpretException if there is an error during execution
     */
    public Value eval(InterpreterEnvironment env)
        throws BreakException, ContinueException, InterpretException
    {
        Value val = expr.eval(env); // evaluate the expression

        switch(val.getType())
        {
            /**
             * A note: this only supports ++x and --x, not
             * x++ and x-- at the moment. In addition, ++1
             * does not actually increment x, it just returns
             * the value x+1 - known bug, to do: fix
             */
            case("Integer"):
                int intVal = Integer.parseInt(val.getValue());
                switch(op)
                {
                    case("-"): return new Value(-1 * intVal);
                    case("++"): return new Value(intVal + 1);
                    case("--"): return new Value(intVal - 1);
                    case("~"): return new Value(~intVal);
                    default: 
                        throw new InterpretException(
                            ErrorString.invalidUnaryOperation(type, op));
                }
            
            case("Boolean"):
                boolean boolVal = val.getValue().equals("TRUE");
                switch(op)
                {
                    case("!"): return new Value(!boolVal);
                    default:
                        throw new InterpretException(
                            ErrorString.invalidUnaryOperation(type, op));
                }
            
            default:
                throw new InterpretException(
                    ErrorString.invalidUnaryOperation(type, op));
        }
    }

    /**
     * Compiles the unary operand
     * 
     * @param e the emitter
     */
    public void compile(Emitter e)
    {
        expr.compile(e);

        String type = expr.type;

        switch (type) // the type of output depends on the type of input
        {
            /**
             * A note: this only supports ++x and --x, not x++ and x-- at the moment. In
             * addition, ++1 does not actually increment x, it just returns the value x+1 -
             * known bug, to do: fix
             */
            case ("Integer"):
                switch (op)
                {
                    case ("-"):
                        e.emit("subu $v0 $zero $v0");
                        break;
                    case ("++"):
                        e.emit("addiu $v0 1");
                        break;
                    case ("--"):
                        e.emit("li $t0 1");
                        e.emit("subu $v0 $v0 $t0");
                        break;
                    case ("~"):
                        e.emit("nor $v0 $v0 $v0");
                        break;
                }
                break;

            case ("Boolean"):
                switch (op)
                {
                    case ("!"):
                        e.emit("nor $v0 $v0 $v0");
                        break;
                }
                break;
        }
    }
}