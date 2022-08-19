# Project Info
A program that compiles (into MIPS Assembly) or interprets code written in a generic programming language. Written using Maven. A sample file that this program can compile/ interpret is below:

```
(*
Tests the full capabilities of this program!
*)


// Declaring subroutines
String getSign(Integer x) {
    getSign = "Positive";
    if (x==0) getSign = "Zero";
    if (x<0) getSign = "Negative";
}
Integer max(Integer x, Integer y) {
    max = x;
    if (y>x) max = y;

    x = -1;
    y = -1; // will not affect global variables
}

writeln("Now testing 'getSign' subroutine");
writeln(getSign(-1));   // should print "Negative"
writeln(getSign(0));    // should print "Zero"
writeln(getSign(100));  // should print "Positive"

writeln("Now testing 'max' subroutine");
Integer x = 3;
Integer y = 4;
writeln(max(x, y)); // should print 4
writeln(x);         // should print 3 not -1

writeln("Now testing for/while loops and user input");
Integer input;

writeln("Enter a number for the for loop: ");
readInteger(input);
for(Integer i = 0;i<input;i = i + 1;) {
    writeln(i+1);
}
writeln("Enter a number for the while loop: ");
readInteger(input);
Integer index = 0;
while(index<input) {
    index = index+1;
    writeln(index);
}

writeln("Now testing if statements");
writeln("Enter a number: ");
readInteger(input);
if (input % 7 == 0) writeln("Your number is divisible by 7");
if (input % 7 != 0) writeln("Your number is not divisible by 7");


writeln("Now testing types more thoroughly");
Boolean a = TRUE;
Integer b = 4;
a = b == 5; // a is false
Boolean c = a || (b-4)==0;  // c is true
writeln(c); // should print TRUE (or 1 in MIPS)
```

# Compilation Steps
1. The preprocessor handles macros (i.e. #include) and pipes the input program as a stream of bytes to the scanner.
2. Lexical analysis: the scanner scans the stream of bytes into a stream of tokens - entities that store their own type (Integer, String, or Boolean), value as a string, and line number. It transfers this stream of tokens to the parser.
3. Syntax analysis: the parser parses the stream of tokens into an abstract syntax tree (AST). It passes this tree to an environment, which acts as a manager for variable and function values. The object nature of Environments allows the creation of sub-Environments, which allows the usage of local variables inside a function that don't affect variables of the same name outside of the function. Note that the parser returns a Program object which acts as the root node ofthe AST.
4. If the program is set to interpret, see step 5. If the program is set to compile, see steps 6 and 7.
5. The program is executed using an interpreter environment, which essentially acts as a map of variable values.
6. The program is analyzed using a semantic analysis environment, which performs actions such as type checking, seeing if a variable exists when it is referenced, etc.
7. The program is compiled into MIPS Assembly code using an Emitter in a specified filepath.
