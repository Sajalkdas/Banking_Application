package testassignment;

import java.io.*;

public class Record {

    private int account;
    private String lastName;
    private String firstName;
    private double balance;
    private static final int LENGTH = 15;

    public Record() {
    }

    public Record(String firstName, String lastName, int account, double balance) {
        StringBuffer buf = new StringBuffer();
        buf.setLength(LENGTH);
        for (int i = 0; i < firstName.length(); i++) {
            buf.setCharAt(i, firstName.charAt(i));
        }
        for (int i = firstName.length(); i < LENGTH; i++) {
            buf.setCharAt(i, ' ');
        }
        this.setFirstName(buf.toString());
        buf.setLength(LENGTH);
        for (int i = 0; i < lastName.length(); i++) {
            buf.setCharAt(i, lastName.charAt(i));
        }
        for (int i = lastName.length(); i < LENGTH; i++) {
            buf.setCharAt(i, ' ');
        }
        this.setLastName(buf.toString());
        this.account = account;
        this.balance = balance;
    }

    public String setLength(String name, int length) {
        StringBuffer buf = new StringBuffer();
        buf.setLength(length);
        for (int i = 0; i < name.length(); i++) {
            buf.setCharAt(i, name.charAt(i));
        }
        for (int i = name.length(); i < length; i++) {
            buf.setCharAt(i, ' ');
        }
        return buf.toString();
    }

    public void read(RandomAccessFile file) throws IOException //object of Record is going to receive a RandomAccessFile(file)
    {
        account = file.readInt(); //binary transfer of 4 Bytes from the file pointer location into the attribute account
        firstName = file.readUTF(); //unicode text format reads 15 character length string or a object
        lastName = file.readUTF();
        balance = file.readDouble();//8 Bytes balance
    }
//We can not create a binary file manually, we can either type or read and send to a
// read computer object then write a binary file from it

    public void write(RandomAccessFile file) throws IOException {
        file.writeInt(account);
        StringBuffer buf = new StringBuffer();
        buf.setLength(LENGTH);
        for (int i = 0; i < firstName.length(); i++) {
            buf.setCharAt(i, firstName.charAt(i));
        }
        for (int i = firstName.length(); i < LENGTH; i++) {
            buf.setCharAt(i, ' ');
        }
        file.writeUTF(buf.toString());
        for (int i = 0; i < lastName.length(); i++) {
            buf.setCharAt(i, lastName.charAt(i));
        }
        for (int i = lastName.length(); i < LENGTH; i++) {
            buf.setCharAt(i, ' ');
        }
        buf.setLength(LENGTH);
        file.writeUTF(buf.toString());
        file.writeDouble(balance);
    }

    public void setAccount(int a) {
        account = a;
    }

    public int getAccount() {
        return account;
    }

    public void setFirstName(String f) {
        firstName = new String(f);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String l) {
        lastName = new String(l);
    }

    public String getLastName() {
        return lastName;
    }

    public void setBalance(double b) {
        balance = b;
    }

    public double getBalance() {
        return balance;
    }

    public String toString() {
        return String.format("%-15s", firstName) + String.format("%-15s", lastName)
                + String.format("%-8d", account) + " " + String.format("$%,10.2f", balance);
    }
// NOTE: This method contains a hard coded value for the
// size of a record of information.

    public static int size() {
        return 46;
    }
}
