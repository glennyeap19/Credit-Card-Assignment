import java.io.File;
import java.nio.file.ReadOnlyFileSystemException;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;

import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * this is the main class.
 * @author glenn
 */

public class Main {
    /***
     * the main function.
     * @param args is the string.
     */
    public static void main(String[] args) {
        int filenumber = 20;

        CCDatabase database3;
        CCDatabase database2;
        CCDatabase database1;
        System.out.println("          WELCOME TO DATABASE RUN TESTER!         ");
        while (filenumber <= 200000) {

            database3 = new RobinHoodDataBase();
            database2 = new HashTableDataBase();
            database1 = new SortedDataBase();

            DecimalFormat dformat = new DecimalFormat();
            String filename = String.format("C:\\Users\\glenn\\OneDrive\\Desktop\\OPSFILES\\ops-%d.txt ",
                    filenumber);
            Duration sorteddatabaseduration = runTests(database1, filename);
            Duration robindatabaseduration = runTests(database3, filename);
            Duration hashtableduration = runTests(database2, filename);

            System.out.println("--------------------------------------------------------");
            System.out.println("Run Test for ops-" + filenumber + ".txt");
            System.out.println(
                    "\nRobin DataBase: " + dformat.format(robindatabaseduration.toNanos()) + " Nanoseconds " + "\n");
            System.out.println("HashTable DataBase: " + dformat.format(hashtableduration.toNanos())
                + " Nanoseconds" + "\n");
            System.out.println(
                    "Sorted DataBase: " + dformat.format(sorteddatabaseduration.toNanos()) + " Nanoseconds" + "\n");
            filenumber *= 10;
        }
    }
    /**
     * runs the test to know how fast each database is.
     * @param database database we are using for example hashtables.
     * @param filename file name that we are testing through.
     * @return duration between start and end which is the time it took.
     */

    private static Duration runTests(CCDatabase database, String filename) {

        File fileinput = new File(filename);

        if (!fileinput.exists()) {
            System.out.println("File not found! ");
        } else {
            try (Scanner readFile = new Scanner(fileinput)) {
                Instant start = null;
                Instant end = null;

                long cardNumber;
                String name;
                String address;
                double creditLimit;
                double balance;

                double adjustLimit;
                double purAmount;

                while (readFile.hasNextLine()) {
                    String currentString = readFile.nextLine();

                    if (currentString.equals("start")) {
                        start = Instant.now();
                    } else if (currentString.equals("stop")) {
                        end = Instant.now();
                    } else if (currentString.equals("cre")) {
                        currentString = readFile.nextLine();
                        cardNumber = Long.parseLong(currentString);

                        currentString = readFile.nextLine();
                        name = currentString;

                        currentString = readFile.nextLine();
                        address = currentString;

                        currentString = readFile.nextLine();
                        creditLimit = Double.parseDouble(currentString);

                        currentString = readFile.nextLine();
                        balance = Double.parseDouble(currentString);

                        database.createAccount(cardNumber, name, address, creditLimit, balance);
                    } else if (currentString.equals("del")) {
                        currentString = readFile.nextLine();
                        cardNumber = Long.parseLong(currentString);

                        database.deleteAccount(cardNumber);
                    } else if (currentString.equals("lim")) {
                        currentString = readFile.nextLine();
                        cardNumber = Long.parseLong(currentString);

                        currentString = readFile.nextLine();
                        adjustLimit = Double.parseDouble(currentString);

                        database.adjustCreditLimit(cardNumber, adjustLimit);
                    } else if (currentString.equals("pur")) {
                        currentString = readFile.nextLine();
                        cardNumber = Long.parseLong(currentString);

                        currentString = readFile.nextLine();
                        purAmount = Double.parseDouble(currentString);

                        try {
                            database.makePurchase(cardNumber, purAmount);
                        } catch (Exception ex) {
                            //handles error
                        }
                    }
                }
                return Duration.between(start, end);
            } catch (Exception ex) {
              //handles error
            }
        }
        return null;
    }

}