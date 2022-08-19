package com.pilers.tester;

import com.pilers.ast.Program;
import com.pilers.emitter.Emitter;
import com.pilers.environment.InterpreterEnvironment;
import com.pilers.environment.SemanticAnalysisEnvironment;
import com.pilers.parser.Parser;
import com.pilers.preprocessor.Preprocessor;
import com.pilers.scanner.Scanner;

/**
 * Overarching Tester class for the entire compiler/interpreter :)
 * 
 * @author Gloria Zhu
 * @version 8/19/22
 */
public class Tester
{

    /**
     * Main method that tests the compiler/interpreter
     * 
     * @param args arguments from the cmd line
     */
    public static void main(String[] args)
    {
        // controls
        boolean onMyComputer = true;
        boolean interpretCode = false;
        boolean compileCode = true;

        String[] testFiles = new String[] {
                "demo.txt",
                "all.txt" };

        String[] compileDestinations = new String[] {
                "demo.asm",
                "all.asm" };

        for(int i=0;i<testFiles.length;i++)
        {
            try
            {
                // preprocessing phase
                Preprocessor pre = new Preprocessor(testFiles[i], onMyComputer, false);
                pre.process();

                // lexical analysis (scanning) phase
                Scanner sc = new Scanner(pre.getProcessedInputStream());

                // syntax analysis (parsing) phase
                Parser pa = new Parser(sc);
                Program prog = pa.parseProgram(args);

                // interpreting
                if (interpretCode)
                {
                    InterpreterEnvironment env = InterpreterEnvironment.newGlobalEnv();
                    prog.exec(env);
                }

                // compiling
                if (compileCode)
                {
                    // semantic analysis phase
                    SemanticAnalysisEnvironment env = SemanticAnalysisEnvironment.newGlobalEnv();
                    prog.analyze(env);

                    // assembly code generation phase
                    Emitter e = new Emitter(compileDestinations[i], env.getVariableInfo());
                    prog.compile(e);
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
                System.out.println("There was an error with file "+testFiles[i]);
            }
        }       

        
    }
}