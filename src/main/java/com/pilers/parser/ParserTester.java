package com.pilers.parser;

import com.pilers.scanner.*;
import com.pilers.ast.Program;
import com.pilers.environment.*;
import com.pilers.emitter.Emitter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A class for testing Parser
 * 
 * @author Gloria Zhu
 */
public class ParserTester
{

    /**
     * Main tester method
     * @param args arguments from the command line
     */
    public static void main(String[] args) 
    {

        String[] testFiles = new String[] {
            // "tests/parserTest10.txt",
            // "tests/parserTest11.txt",
            // "tests/parserTest12.txt",
            // "tests/parserTest13.txt"
            "tests/staticVariables.txt"
        };

        String[] compileDests = new String[] {
            // "parserTest10.asm",
            // "parserTest11.asm",
            // "parserTest12.asm",
            // "parserTest13.asm"
            "printSquares.asm"
        };
        
        // controls
        boolean onMyComputer = true;

        boolean interpretCode = true;
        boolean compileCode = false;

        try 
        {
            for (int i = 0; i < testFiles.length; i++)
            {
                System.out.println("\nNow testing " + testFiles[i]);

                Scanner scanner;
                if (onMyComputer)
                    scanner = new Scanner(Parser.class.getResourceAsStream(testFiles[i]));
                else
                {
                    Path currentDir = Paths.get(testFiles[i]);
                    scanner = new Scanner(new FileInputStream(new File(
                        currentDir.toAbsolutePath().toString())));
                }

                Parser pa = new Parser(scanner);
                
                try 
                {
                    Program prog = pa.parseProgram(args);

                    if (interpretCode)
                    {
                        InterpreterEnvironment env = InterpreterEnvironment.newGlobalEnv();
                        prog.exec(env);
                    }

                    if (compileCode)
                    {
                        SemanticAnalysisEnvironment env = 
                                SemanticAnalysisEnvironment.newGlobalEnv();
                        prog.analyze(env);

                        Emitter e = new Emitter(compileDests[i], env.getVariableInfo());
                        prog.compile(e);
                    }


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
