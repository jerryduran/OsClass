package com.company;


import java.io.FileNotFoundException;

public class Run {
    public static void main(String [] args) throws FileNotFoundException {
        Main main = new Main();
        try {
            main.Run();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
