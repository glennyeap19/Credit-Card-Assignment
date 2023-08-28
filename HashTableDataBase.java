/**
 * Implemention for CCDataBase that verison 2 which is the HashTable.
 * @author Glenn Yeap
 */

public class HashTableDataBase implements CCDatabase {
    int notfound = -1;
    int tablesize = 101;
    int useraccounts = 0;
    BucketOpen[] accounts = new BucketOpen[tablesize];

    /**
     * This is a hash function that will get the account number and split each 4
     * digit value to be a short. We the formula given from the specs to get a new
     * number that will be the hash key.
     * @param accountnumber - account to search for
     * @return Final % table size - which is the hash key
     */

    private int getindex(long accountnumber) {
        final short c4 = (short) (accountnumber % 10000);
        accountnumber = accountnumber / 1000;
        short c3 = (short) (accountnumber % 10000);
        accountnumber = accountnumber / 1000;
        short c2 = (short) (accountnumber % 10000);
        accountnumber = accountnumber / 1000;
        short c1 = (short) (accountnumber % 10000);

        int hashvalue = (int) (17 * c1 + Math.pow(17, 2) * c2 + Math.pow(17, 3) * c3 + Math.pow(17, 4) * c4);

        return hashvalue % tablesize;

    }

    @Override
    public boolean createAccount(long accountNumber, String name, String address, double creditLimit, double balance) {
        if (search(accountNumber) == notfound) { // sees if the account is found.
            if ((useraccounts + 1) > (tablesize * (60.0f / 100.0f))) //checks if we need to resize.
            {
                accounts = resizetable();
            }
            int index = getindex(accountNumber); // makes index our hash key which is the 4 digit short account number
            while (accounts[index] != null && !accounts[index].isDeleted()) {
                index = (index + 1) % tablesize; // linear probing as it will loop around to 0 if needed.
            }
            BucketOpen account = new BucketOpen(accountNumber, name, address, creditLimit, balance);
            // creates a account
            accounts[index] = account;
            useraccounts++; // increments when a account is made
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteAccount(long accountNumber) {

        if (search(accountNumber) == notfound) // checks if account is found.
        {
            return false;
        } else {
            BucketOpen newAccounts = accounts[search(accountNumber)]; // deletes the account.
            newAccounts.setDeleted(true); // we set a boolean to true when we delete the account
            return true;
        }
        // TODO Auto-generated method stub
    }

    @Override
    public boolean adjustCreditLimit(long accountNumber, double newLimit) {

        if (search(accountNumber) == notfound) {
            return false;
        } else {
            accounts[search(accountNumber)].setCreditlimit(newLimit); // sets the old credit to the new credit limit
            return true;
        }
        // TODO Auto-generated method stub
    }

    @Override
    public String getAccount(long accountNumber) {

        if (search(accountNumber) == notfound) {
            return null;
        } else if (accounts[search(accountNumber)] != null) {
            return accounts[search(accountNumber)].getData(); // displays the data of the account we are looking for
        }
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean makePurchase(long accountNumber, double price) throws Exception {

        if (search(accountNumber) == notfound) {
            return false;
        } else {
            int index = search(accountNumber); // checks if the account has enough funds or not to make the purchase.
            if (accounts[index].getBalance() + price > accounts[index].getCreditlimit()) {
                throw new Exception("You can not purchase this item ----- Balance is over the card limit");
            } else {
                // we set the new balance to be whatever they bought plus the old balance
                accounts[index].setBalance(accounts[index].getBalance() + price);
                return true;
            }
        }
        // TODO Auto-generated method stub
    }

    /**
     * This is also  binary search to find the element in the table as it
     * will return not found (-1) if the element is not in the table and if it is
     * found we will return where the hash key is hashed in the table.
     * @param accountnumber account number it searches for.
     * @return notfound when the item is not in the table.
     * @return hashkey where the account hashes in the table.
     */
    private int search(long accountNumber) {

        int hashkey = getindex(accountNumber);

        if (accounts[hashkey] == null) {
            return notfound;
        } else {
            while (accounts[hashkey] != null) {
                if (accounts[hashkey].getAccountNumber() == accountNumber && !accounts[hashkey].isDeleted()) {
                    return hashkey;
                }
                hashkey = (hashkey + 1) % tablesize; // linear probing
            }
            return notfound;
        }
    }

    /**
     * This method will help us resize the bucket as it will return a boolean to see
     * if size of the bucket is at a prime number or not.
     * got from geeksforgeeks.
     * URL: https://www.geeksforgeeks.org/program-to-find-the-next-prime-number
     * /#:~:text=First%20of%20all%2C%20take%20a%20boolean%20variable%20found,
     * until%20you%20will%20get%20the%20next%20prime%20number.
     * @param number - gets the number to see if its a prime
     * @return true - if its a prime
     * @return false - if its not a prime
     */
    private boolean isPrime(int number)
    {
        // Corner cases
        if (number <= 1)
        {
            return false;
        }
        if (number <= 3)
        {
            return true;
        }
        // This is checked so that we can skip
        // middle five numbers in below loop
        if (number % 2 == 0 || number % 3 == 0)
        {
            return false;
        }
        for (int i = 5; i * i <= number; i = i + 6)
        {
            if (number % i == 0 || number % (i + 2) == 0)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * This method also helps us resize the bucket as it finds the next prime number
     * to change the bucket size too when the resizing happens.
     * @param checkprime - sees if the next number is prime.
     * @return prime: the next prime number.
     */
    private int nextPrime(int checkprime) {

        // Base case
        if (checkprime <= 1)
        {
            return 2;
        }
        int prime = checkprime;
        boolean found = false;

        // Loop continuously until isPrime returns
        // true for a number greater than n
        while (!found) {
            prime++;

            if (isPrime(prime)) {
                found = true;
            }
        }
        return prime;
    }

    /**
     * This method resizes the bucket when the table is full or hits a certain
     * amount.
     * @return arrayBucket - which returns the new bucketopen array
     */
    private BucketOpen[] resizetable() {

        int lasttablesize = tablesize;
        tablesize = nextPrime(tablesize * 2); // sets the table size to double and to get the next prime
        BucketOpen[] arrayBucket = new BucketOpen[tablesize]; // make a new bucket

        for (int i = 0; i < lasttablesize; i++) {
            if (accounts[i] != null && !accounts[i].isDeleted()) {
                int hashkey = getindex(accounts[i].getAccountNumber());
                while (arrayBucket[hashkey] != null) {
                    hashkey = (hashkey + 1) % tablesize;
                }
                arrayBucket[hashkey] = accounts[i];
            }
        }
        return arrayBucket;
    }
}
