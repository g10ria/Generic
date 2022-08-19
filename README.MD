# Project Info
A program that compiles (into MIPS Assembly) or interprets code written in a generic programming language.

Written using Maven. A tester file can be located at `src/main/java/com/pilers/tester/Tester.java`.

Steps:
1. The preprocessor handles macros (i.e. #include) and pipes the input program as a stream of bytes to the scanner.
2. Lexical analysis: the scanner scans the stream of bytes into a stream of tokens - entities that store their own type (Integer, String, or Boolean), value as a string, and line number. It transfers this stream of tokens to the parser.
3. Syntax analysis: the parser parses the stream of tokens into an abstract syntax tree (AST). It passes this tree to an environment, which acts as a manager for variable and function values. The object nature of Environments allows the creation of sub-Environments, which allows the usage of local variables inside a function that don't affect variables of the same name outside of the function. Note that the parser returns a Program object which acts as the root node ofthe AST.
4. If the program is set to interpret, see step 5. If the program is set to compile, see steps 6 and 7.
5. The program is executed using an interpreter environment, which essentially acts as a map of variable values.
6. The program is analyzed using a semantic analysis environment, which performs actions such as type checking, seeing if a variable exists when it is referenced, etc.
7. The program is compiled into MIPS Assembly code using an Emitter in a specified filepath.