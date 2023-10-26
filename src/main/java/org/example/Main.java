package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            new FileProcessing().run(ProgramArgs.parsingArgs(args));
        } catch (IOException | AnExeption ex) {
            System.err.println(ex.getMessage());
        }
    }
}