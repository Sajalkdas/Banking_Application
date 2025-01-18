package testassignment;

import java.io.*;
import java.util.Scanner;
import javax.swing.*;

public class MenuRecords {

    private Record record;
    private RandomAccessFile file;
    private double balance;
    private Scanner kbd = new Scanner(System.in);
    private static final int LENGTH = 15;
    JFrame frame = new JFrame();

    public MenuRecords() {
        frame.setAlwaysOnTop(true);
        record = new Record();
        try {
            file = new RandomAccessFile("rand.dat", "rw");
        } catch (IOException e) {
            System.err.println("File not opened properly\n" + e.toString());
            System.exit(1);
        }
    }

    public void menu() {
        System.out.printf("%nChoose an Option%n");
        System.out.printf("0 - Display All%n");
        System.out.printf("1 - Display by Name%n");
        System.out.printf("2 - Display by Balance%n");
        System.out.printf("3 - Add Record%n");
        System.out.printf("4 - Delete Record%n");
        System.out.printf("5 - File Dump%n");
        System.out.printf("6 - Print to Textfile%n");
        System.out.printf("7 - Display Record by it's Position (0 is first)%n");
        System.out.printf("8 - Quit%n");
    }

    public void runMenu() throws IOException {
        do {
            menu();
            System.out.printf("%s", "\nEnter you Choice (0-8): ");
            String input = kbd.nextLine();
            int choice = Integer.parseInt(input);
            System.out.printf("%n");
            switch (choice) {
                case 0:
                    displayAll();
                    break;
                case 1:
                    displayByName();
                    break;
                case 2:
                    displayByBalance();
                    break;
                case 3:
                    addRecord();
                    break;
                case 4:
                    deleteRecord();
                    break;
                case 5:
                    fileDump();
                    break;
                case 6:
                    printToTextfile();
                    break;
                case 7:
                    displayByPosition();
                    break;
                case 8:
                    return;
            }
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException ie) {
            }
        } while (true);
    }
// To display the record based on the usr input lastName, if it's not deleted already.

    public void displayByName() {
        String lastName = JOptionPane.showInputDialog(frame, "Enter last name to find");
        System.out.println("Entered lastName = " + lastName);
        boolean found = false;
        try {
            long end = file.length();
            file.seek(0L);
            while (!(file.getFilePointer() >= end)) {
                record.read(file);
                if (!record.getFirstName().contains("VOID")
                        && record.getLastName().toLowerCase().contains(lastName.toLowerCase())) {
                    System.out.println(record);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No records for \"" + lastName + "\" found in the file.");
            }
        } catch (IOException ioe) {
        }
    }
// To display the record based on the user input position, if the record is not deleted or the position is in Eof.

    public void displayByPosition() throws IOException {
        String pos = JOptionPane.showInputDialog(frame, "Enter position (0 is 1st record, 1 is second ...)");
        int position = Integer.parseInt(pos);
        System.out.println("Displaying the record at position: " + position + ".");
        int filePos = Integer.parseInt(pos);
        long filePointer = 0L;
        long end = file.length();
        try {
            file.seek(filePointer);
            for (int i = 0; i <= filePos && !(file.getFilePointer() >= end); i++) {
                filePointer = file.getFilePointer();
                record.read(file);
            }
            if (file.getFilePointer() == end) {
                System.out.println("Its end of the file, no Records to display.");
            } else if (record.getFirstName().contains("VOID")) {
                System.out.println("This records was deleted, can't be display.");
            } else {
                file.seek(filePointer);
                System.out.println(record);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
// To display all records that are not deleted from the file.

    public void displayAll() {
        System.out.printf("%-15s%-15s%-9s%-10s%n", "First", "Last", "Account",
                "Balance");
        System.out.printf("%-15s%-15s%-9s%-10s%n", "-----", "----", "-------",
                "-------");
        try {
            long end = file.length();
            file.seek(0L);
            while (!(file.getFilePointer() >= end)) {
                record.read(file);
                if (!record.getFirstName().contains("VOID")) {
                    System.out.println(record);
                }
            }
            System.out.println("\nAll records displayed");
        } catch (IOException ioe) {
        }
    }
// To write all the records from the read file to an output file.

    public void printToTextfile() {
        PrintWriter out = null;
        String filename = JOptionPane.showInputDialog(frame, "Please Enter the Filename");
        try {
            out = new PrintWriter(filename);
        } catch (IOException ioe) {
        }
        out.printf("%-15s%-15s%-9s%-10s%n", "First", "Last", "Account", "Balance");
        out.printf("%-15s%-15s%-9s%-10s%n", "-----", "----", "-------", "-------");
        try {
            long end = file.length();
            file.seek(0L);
            while (file.getFilePointer() != end) {
                record.read(file);
                if (!record.getFirstName().contains("VOID")) {
                    out.println(record);
                }
            }
            out.println("\nAll records Printed above.");
            out.close();
            System.out.println("All recoreds written to " + filename + " file");
        } catch (IOException ioe) {
        }
    }
//To display all teh records including the deleted records.

    public void fileDump() {
        System.out.printf("%-15s%-15s%-9s%-10s%n", "First", "Last", "Account",
                "Balance");
        System.out.printf("%-15s%-15s%-9s%-10s%n", "-----", "----", "-------",
                "-------");
        boolean found = false;
        try {
            long end = file.length();
            file.seek(0L);
            while (!(file.getFilePointer() >= end)) {
                record.read(file);
                System.out.println(record);
                found = true;
            }
            if (!found) {
                System.out.println("No records found to DUMP.");
            } else {
                System.out.println("\nAll records displayed including deleted names.");
            }
        } catch (IOException ioe) {
        }
    }
//To display all the records with the balance that fall below the user input balance.

    public void displayByBalance() {
        String bal = JOptionPane.showInputDialog(frame, "Enter Maximum Balance");
        double balance = Double.parseDouble(bal);
        System.out.println("Showing all records with balance below $" + balance
                + ".");
        boolean found = false;
        try {
            long end = file.length();
            file.seek(0L);
            while (!(file.getFilePointer() >= end)) {
                record.read(file);
                if (!record.getFirstName().contains("VOID")
                        && record.getBalance() < balance) {
                    do {
                        System.out.printf("%-15s%-15s%-9s%-10s%n", "First", "Last",
                                "Account",
                                "Balance");
                        System.out.printf("%-15s%-15s%-9s%-10s%n", "-----", "----",
                                "-------",
                                "-------");
                    } while (false);
                    System.out.println(record);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No records found below the balance of " + balance);
            } else {
                System.out.println("All records displayed");
            }
        } catch (IOException ioe) {
        }
    }
//To add a record at the deleted record position or at the end of the file.

    public void addRecord() throws IOException {
        String lastName = JOptionPane.showInputDialog(frame, "Enter Last Name: ");
        String firstName = JOptionPane.showInputDialog(frame, "Enter First Name: ");
        int account = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter Account Number: "));
        double balance
                = Double.parseDouble(JOptionPane.showInputDialog(frame, "Enter Account Balance: "));
        Record record = new Record();
        record.setFirstName(record.setLength(firstName, LENGTH));
        record.setLastName(record.setLength(lastName, LENGTH));
        record.setAccount(account);
        record.setBalance(balance);
        findNewRecordLocation();
        record.write(file);
        System.out.println("New record of " + firstName + " " + lastName + " has been added to the file.");
    }
//To find a available position to add a new record.

    public void findNewRecordLocation() {
        try {
            long end = file.length();
            file.seek(0L);
            while (file.getFilePointer() != end) {
                long loc = file.getFilePointer();
                record.read(file);
                if (record.getFirstName().contains("VOID")) {
                    file.seek(loc);
                    return;
                }
            }
        } catch (IOException ioe) {
            System.out.println("Input output error: " + ioe);
        }
    }
//To delete a record by the user input last name that is not deleted already.

    public void deleteRecord() {
        String lastName = JOptionPane.showInputDialog(frame, "Enter last name of the record: ");
        try {
            long end = file.length();
            file.seek(0L);
            long curLocation;
            boolean found = false;
            while (!(file.getFilePointer() >= end)) {
                curLocation = file.getFilePointer();
                record.read(file);
                if (record.getLastName().toLowerCase().contains(lastName.toLowerCase())) {
                    if (record.getFirstName().contains("VOID")) {
                        System.out.println("This record is already deleted.");
                    } else {
                        file.seek(curLocation);
                        record.setFirstName(record.setLength("VOID", LENGTH));
                        found = true;
                    }
                    break;
                }
            }
            record.write(file);
            if (found) {
                System.out.println("The records of " + lastName + " has been deleted.");
            } else {
                System.out.println("No such a record found.");
            }
        } catch (IOException ioe) {
        }
    }
//Main method to create a default constructor of MenuRecords and execute runMenu methods of MenuRecord.

    public static void main(String args[]) throws IOException {
        MenuRecords menuProg = new MenuRecords();
        menuProg.runMenu();
        System.out.println("Program Complete");
    }
}
