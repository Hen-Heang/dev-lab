package com.henheang.applying_project.bankmanagmentsystem.utilities;

import com.henheang.applying_project.bankmanagmentsystem.models.Bank;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DataPersistence {

    // The file where all bank data will be saved
    // It will be created in the project's working directory
    private static final String DATA_FILE = "bank_data.dat";

    // Saves the entire Bank object to a binary file using Java Serialization.
    // ObjectOutputStream converts the object into bytes and writes to disk.
    public static void save(Bank bank) {
        // try-with-resources automatically closes the streams after done
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(DATA_FILE))) {

            // writeObject() converts the Bank object (and all its maps/lists inside)
            // into bytes and writes them to the file
            out.writeObject(bank);
            System.out.println(ConsoleColor.green("Data saved successfully."));

        } catch (IOException e) {
            System.out.println(ConsoleColor.red("Failed to save data: " + e.getMessage()));
        }
    }

    // Loads the Bank object back from the binary file.
    // Returns null if no saved file exists (first time running).
    public static Bank load() {

        // Check if the save file exists — if not, this is the first run
        if (!Files.exists(Paths.get(DATA_FILE))) {
            System.out.println(ConsoleColor.yellow("No saved data found. Starting fresh."));
            return null;
        }

        // try-with-resources automatically closes the streams after done
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(DATA_FILE))) {

            // readObject() reads the bytes from file and reconstructs the Bank object
            // we cast it back to Bank because readObject() returns Object type
            Bank bank = (Bank) in.readObject();
            System.out.println(ConsoleColor.green("Data loaded successfully."));
            return bank;

        } catch (IOException | ClassNotFoundException e) {
            System.out.println(ConsoleColor.red("Failed to load data: " + e.getMessage()));
            return null;
        }
    }
}