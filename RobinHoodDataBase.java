/**
 * Implemention for CCDataBase that verison 3 which is the RobinHood.
 * @author Glenn
 */
public class RobinHoodDataBase implements CCDatabase {
    int notfound = -1;
    int tablesize = 101;
    int useraccounts = 0;
    RobinHoodBucketOpen[] accounts = new RobinHoodBucketOpen[tablesize];

    /**
     * This is a hash function that will get the account number and split each 4
     * digit value to be a short. We the formula given from the specs to get a new
     * number that will be the hash key.
     * @param accountnumber - account to search for
     * @return Final % table size - which is the hash key
     */

    private int getindex(long accountnumber) {
        final short c4 = (short) (accountnumber % 10000);
        accountnumber = accountnumber / 10000;
        short c3 = (short) (accountnumber % 10000);
        accountnumber = accountnumber / 10000;
        short c2 = (short) (accountnumber % 10000);
        accountnumber = accountnumber / 10000;
        short c1 = (short) (accountnumber % 10000);

        int hashvalue = (int) (17 * c1 + Math.pow(17, 2) * c2 + Math.pow(17, 3) * c3 + Math.pow(17, 4) * c4);

        return hashvalue % tablesize;

    }

    @Override
    public boolean createAccount(long accountNumber, String name, String address, double creditLimit, double balance) {

        if (search(accountNumber) == notfound) {
            if ((useraccounts + 1) > (tablesize * (60.0f / 100.0f))) {
                accounts = resizetable();
            }
            int index = getindex(accountNumber); // makes index our hash key which is the 4 digit short account number
            RobinHoodBucketOpen account =
                    new RobinHoodBucketOpen(accountNumber, name, address, creditLimit, balance); // creates
            insert(account, accounts);
            useraccounts++; // increments when a account is made
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteAccount(long accountNumber) {

        int index = getindex(accountNumber);
        while (accounts[index] != null && accounts[index].getAccountNumber() != accountNumber)
        {
            index = (index + 1) % tablesize; //linear probing
        }
        if (accounts[index] == null) { //checks if accounts isn't null
            return false;
        } else {
            remove(index); // removes the account
            useraccounts--;
            return true;
        }
    }

    @Override
    public boolean adjustCreditLimit(long accountNumber, double newLimit) {

        if (search(accountNumber) == notfound) {
            return false;
        } else {
            accounts[search(accountNumber)].setCreditlimit(newLimit);
            return true;
        }
        // TODO Auto-generated method stub
    }

    @Override
    public String getAccount(long accountNumber) {

        if (search(accountNumber) == notfound) {
            return null;
        } else if (accounts[search(accountNumber)] != null) {
            return accounts[search(accountNumber)].getData();
        }
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean makePurchase(long accountNumber, double price) throws Exception {

        if (search(accountNumber) == notfound) {
            return false;
        } else {
            int index = search(accountNumber);
            if (accounts[index].getBalance() + price > accounts[index].getCreditlimit()) {
                throw new Exception("You can not purchase this item ----- Balance is over the card limit");
            } else {
                accounts[index].setBalance(accounts[index].getBalance() + price);
                return true;
            }
        }
        // TODO Auto-generated method stub
    }

    /**
    * This method resizes the bucket when the table is full or hits a certain
    * amount.
    * @return arrayBucket - which returns the new bucketopen array
    */

    private RobinHoodBucketOpen[] resizetable() {

        int lasttablesize = tablesize;
        tablesize = nextPrime(tablesize * 2);
        RobinHoodBucketOpen[] arrayBucket = new RobinHoodBucketOpen[tablesize];

        for (int i = 0; i < lasttablesize; i++) {
            if (accounts[i] != null) {
                accounts[i].setPSL(0);
                insert(accounts[i], arrayBucket);
            }
        }
        return arrayBucket;
    }
    /**
     * Inserts at the given index. We use this in create account.
     * @param account the robinhoodbucketopen.
     * @param newresizeTable the newbucketopen table that is resized.
     */

    private void insert(RobinHoodBucketOpen account, RobinHoodBucketOpen[] newresizeTable) {
        int index = getindex(account.getAccountNumber());

        while (newresizeTable[index] != null) {
            //compares the psl value of the old table to the new one
            if (account.getPSL() > newresizeTable[index].getPSL()) {
                RobinHoodBucketOpen tempacc = newresizeTable[index]; //swap tables
                newresizeTable[index] = account;
                account = tempacc;
            }
            account.increPsl(); //increments the old table psl
            index = (index + 1) % tablesize; //linear probing
        }
        newresizeTable[index] = account; //makes the new table equal to the new one
    }
    /**
     * removes the account. We use this in remove account.
     * @param index - the index that we check to see if we delete
     * the account or not.
     */

    private void remove(int index) {

        accounts[index] = null;
        int getnextindex = (index + 1) % tablesize; //linear probing but we check if the next element
        // is at the last element of the table
        //base case as we check for null and to see of the next elements psl is greater than 0
        while (accounts[getnextindex] != null && accounts[getnextindex].getPSL() > 0)
        {
            accounts[index] = accounts[getnextindex];
            accounts[getnextindex] = null;
            accounts[index].setPSL(accounts[index].getPSL() - 1); //backward shifting
            index = (index + 1) % tablesize;
            getnextindex = (index + 1) % tablesize;
        }
    }
    /**
     * This is also another binary search to find the element in the table as it
     * will return not found (-1) if the element is not in the table and if it is
     * found we will rather search left or right depending on where the element
     * lies.
     * @param accountnumber account number it searches for.
     * @return notfound when the item is not in the table.
     * @return hashkey index where the account is in the table.
     */

    private int search(long accountNumber) {

        int hashkey = getindex(accountNumber);

        while (accounts[hashkey] != null) {
            if (accounts[hashkey].getAccountNumber() == accountNumber) {
                return hashkey;
            }
            hashkey = (hashkey + 1) % tablesize;
        }
        return notfound;
    }

    /**
     * This method will help us resize the bucket as it will return a boolean to see
     * if size of the bucket is at a prime number or not.
     * got from geeksforgeeks.
     * @author URL: https://www.geeksforgeeks.org/program-to-find-the-next-prime-number
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
     * @author URL: https://www.geeksforgeeks.org/program-to-find-the-next-prime-number
     * /#:~:text=First%20of%20all%2C%20take%20a%20boolean%20variable%20found,
     * until%20you%20will%20get%20the%20next%20prime%20number.
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

}
