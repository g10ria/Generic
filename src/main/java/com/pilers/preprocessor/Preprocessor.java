package com.pilers.preprocessor;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import com.pilers.errors.ErrorString;
import com.pilers.errors.PreprocessorException;

/**
 * Preprocessor class
 * 
 * Takes in a "base" file and pipes all output to a stream of bytes
 * Currently supports: 
 * #include - include other files (inline replacement)
 * #define - define macros
 * #ifdef - if a macro is defined, include the following code up until endif or eof
 * #endif - ends an ifdef
 * #undef - undefines a macro
 * 
 * #requestFile - makes a GET request to a specified URL
 * allows including online files in the project
 * 
 * @author Gloria Zhu
 * @version 8/19/22
 */
public class Preprocessor
{

    private String filepath;

    private BufferedReader in;

    private HashMap<String, String> definitions;

    ArrayList<String> includedFiles;

    ByteArrayOutputStream out;

    private boolean onMyComputer;

    /**
     * Preprocessor constructor for constructing a "child" preprocessor
     * 
     * @param filepath file path (a URL if it's online)
     * @param onMyComputer (different file structures on mine vs instructor computer)
     * @param isOnline if the requested file is online not local
     * @param out the existing output stream
     * @param includedFiles files that have already been piped to the stream
     * @param definitions variables
     * 
     * @throws IOException if there is an issue with file I/O
     */
    public Preprocessor(
            String filepath, boolean onMyComputer,
            boolean isOnline,
            ByteArrayOutputStream out,
            ArrayList<String> includedFiles,
            HashMap<String, String> definitions) throws IOException
    {
        if (isOnline)
        {
            try
            {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request =
                        HttpRequest.newBuilder().uri(
                        URI.create(filepath)).build();

                HttpResponse<String> response = 
                        client.send(request, HttpResponse.BodyHandlers.ofString());

                in = new BufferedReader(new StringReader(response.body()));
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            if (onMyComputer)
                in = new BufferedReader(
                    new InputStreamReader(Preprocessor.class.getResourceAsStream(filepath)));
            else
            {
                Path currentDir = Paths.get(filepath);
                in = new BufferedReader(
                        new InputStreamReader(
                            new FileInputStream(new File(currentDir.toAbsolutePath().toString()))));
            }
        }
        
        this.filepath = filepath;
        this.includedFiles = includedFiles;
        this.out = out;
        this.onMyComputer = onMyComputer;
        this.definitions = definitions;
    }

    /**
     * Preprocessor constructor for constructing the "base" preprocessor
     * 
     * @param filepath file name
     * @param onMyComputer (different file structures on mine vs instructor computer)
     * @param isOnline if the requested file is online not local
     * @throws IOException if there is a problem with file I/O
     */
    public Preprocessor(String filepath, boolean onMyComputer, boolean isOnline) throws IOException
    {
        
        if (isOnline)
        {
            try
            {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder().uri(URI.create(filepath)).build();

                HttpResponse<String> response = client.send(
                        request, HttpResponse.BodyHandlers.ofString());

                in = new BufferedReader(new StringReader(response.body()));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            if (onMyComputer)
                in = new BufferedReader(
                    new InputStreamReader(
                        Preprocessor.class.getResourceAsStream(filepath)));
            else
            {
                Path currentDir = Paths.get(filepath);
                in = new BufferedReader(
                        new InputStreamReader(
                            new FileInputStream(
                                new File(currentDir.toAbsolutePath().toString()))));
            }
        }

        this.filepath = filepath;
        out = new ByteArrayOutputStream();
        includedFiles = new ArrayList<String>();
        this.onMyComputer = onMyComputer;
        definitions = new HashMap<String, String>();
    }

    /**
     * Processes the file and pipes output to a stream of bytes
     * 
     * @throws IOException if there is an issue with File I/O
     * @throws PreprocessorException if there is an unknown directive
     */
    public void process() throws IOException, PreprocessorException
    {
        if (includedFiles.contains(filepath)) return;

        includedFiles.add(filepath);
        String line = in.readLine();

        // used for false #endif directives
        boolean isWriting = true;

        while (line != null)
        {   
            if (line.length() > 0 && line.charAt(0) == '#')
            {
                String[] args = line.split(" ");
                String directive = args[0];

                switch (directive)
                {
                    case ("#include"):
                        new Preprocessor(
                            args[1], onMyComputer,
                            false, out, includedFiles, definitions)
                            .process();
                        break;
                    case ("#define"):
                        String defVar = args[1];
                        String defVal = args[2];
                        if(definitions.get(defVar) != null) throw new
                        PreprocessorException(ErrorString.duplicateDefine(defVar));

                        definitions.put(defVar, defVal);
                        break;

                    case("#ifdef"):
                        String defVarCond = args[1];
                        isWriting = definitions.get(defVarCond) != null;

                        break;

                    case("#endif"):
                        isWriting = true;
                        break;

                    case("#undef"):
                        definitions.put(args[1], null);
                        break;

                    case("#requestFile"):
                        new Preprocessor(args[1], onMyComputer, 
                        true, out, includedFiles, definitions).process();
                        break;

                    default:
                        throw new PreprocessorException(ErrorString.unknownDirective(args[1]));
                }
            }
            else if (isWriting)
            {                
                String[] split = line.split(" ");
                for(int i=0;i<split.length;i++)
                {
                    String val = definitions.get(split[i]);
                    if (val!=null) split[i] = val;

                    for(char c : split[i].toCharArray())
                        out.write(c);
                    out.write(' ');
                }
                
                out.write('\n');
            }

            line = in.readLine();
        }
    }

    /**
     * @return the output stream converted to an input stream (for feeding
     *  into the scanner)
     */
    public InputStream getProcessedInputStream()
    {
        return new ByteArrayInputStream(out.toByteArray());
    }
}