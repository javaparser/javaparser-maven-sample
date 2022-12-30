package com.tmax.ast.service;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.printer.DotPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class OutputService {
    public void dotPrinter(String fileName, CompilationUnit cu) throws IOException {
        // Now comes the inspection code:
        DotPrinter printer = new DotPrinter(true);
        try (FileWriter fileWriter = new FileWriter(fileName+".dot");
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.print(printer.output(cu));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // ProcessBuilder
        ProcessBuilder processBuilder = new ProcessBuilder();
        String command = "dot -Tpng " + fileName + ".dot" + " > " + fileName + ".png";
        processBuilder.command("bash", "-c", command);
        Process process = processBuilder.start();
    }
}
