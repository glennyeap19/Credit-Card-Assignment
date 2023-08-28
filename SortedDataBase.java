/**
 *  Implemention for CCDataBase that verison 1 which is the SortedArray.
 *  @author Glenn Yeap
 *
 */
public class SortedDataBase implements CCDatabase {
    int notfound = -1;
    int tablesize = 250000;
    Account[] accounts = new Account[tablesize];

    /**
     * We made a account class to hold/use the variables needed to complete the
     * following code.
     * @author Glenn
     *
     */
    private static class Account {
        long accountNumber;
        private String name;
        private String address;
        private double creditlimit;
        private double balance;
    }

    @Override
    public boolean createAccount(long accountNumber, String name, String address, double creditLimit, double balance) {

        if (accounts[0] == null) {
            accounts[0] = new Account();
            accounts[0].accountNumber = accountNumber;
            accounts[0].name = name;
            accounts[0].address = address;
            accounts[0].creditlimit = creditLimit;
            accounts[0].balance = balance;

            return true;
        } else {
            if (search(0, endofarray(), accountNumber) != notfound) {
                for (int i = endofarray(); i >= 0 && i >= search(0, endofarray(), accountNumber); i--) {
                    accounts[i + 1] = accounts[i];
                }

                int index = search(0, endofarray(), accountNumber);
                accounts[index] = new Account();
                accounts[index].accountNumber = accountNumber;
                accounts[index].name = name;
                accounts[index].address = address;
                accounts[index].creditlimit = creditLimit;
                accounts[index].balance = balance;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteAccount(long accountNumber) {

        if (search2(0, endofarray(), accountNumber) != notfound)
        {
            for (int index = search2(0, endofarray(), accountNumber); index <= endofarray(); index++)
            {
                accounts[index] = accounts[index + 1];
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean adjustCreditLimit(long accountNumber, double newLimit) {

        if (search2(0, endofarray(), accountNumber) == notfound) {
            return false;
        } else {
            int index = search2(0, endofarray(), accountNumber);
            accounts[index].creditlimit = newLimit;
            return true;
        }
        // TODO Auto-generated method stub
    }

    @Override
    public String getAccount(long accountNumber) {

        if (search2(0, endofarray(), accountNumber) != notfound) {
            int index = search2(0, endofarray(), accountNumber);
            String accountdetail = Long.toString(accountNumber) + "\n"
                    + accounts[index].name + "\n"
                    + accounts[index].address + "\n"
                    + accounts[index].creditlimit + "\n"
                    + accounts[index].balance;
            return accountdetail;
        }

        return null;

    }

    @Override
    public boolean makePurchase(long accountNumber, double price) throws Exception {

        if (search2(0, endofarray(), accountNumber) == notfound) {
            return false;
        } else {
            int index = search2(0, endofarray(), accountNumber);
            if (accounts[index].balance + price > accounts[index].creditlimit) {
                throw new Exception("You can not purchase this item ----- Balance is over the card limit");
            } else {
                accounts[index].balance = accounts[index].balance + price;
                return true;
            }

        }
    }

    /**
     * This method is a search functions that allows us to figure out the location
     * of the account in the array and then to return if it is found or not. But in
     * this search we did a binary search which returns left at the end if the
     * element is not found.
     * @param accountnumber account number it searches for.
     * @param right right most position used in the sorted array.
     * @param left start of array.
     * @return notfound the account number was not found.
     * @return left this is the index that is in the sorted array.
     */
    private int search(int left, int right, long accountNumber) {

        if (accounts[0] == null) {
            return notfound;
        } else {
            while (left <= right) {

                int mid = left + (right - left) / 2;

                // Check if x is present at mid
                if (accounts[mid].accountNumber == accountNumber) {
                    return notfound;
                }
                // If x greater, ignore left half
                else if (accounts[mid].accountNumber < accountNumber) {
                    left = mid + 1;
                }
                // If x is smaller,
                // element is on left side
                // so ignore right half
                else {
                    right = mid - 1;
                }
            }
            // If we reach here,
            // element is not present
        }
        return left;
    }

    /**
     * This is also another binary search to find the element in the array as it
     * will return not found (-1) if the element is not in the array and if it is
     * found we will rather search left or right depending on where the element
     * lies.
     * @param accountnumber account number it searches for.
     * @param right right most position used in the sorted array.
     * @param left start of array.
     * @return mid index where the account is in the array
     */
    private int search2(int left, int right, long accountNumber) {

        while (left <= right) {

            int mid = left + (right - left) / 2;

            // Check if x is present at mid
            if (accounts[mid].accountNumber == accountNumber) {
                return mid;
            }
            // If x greater, ignore left half
            else if (accounts[mid].accountNumber < accountNumber) {
                left = mid + 1;
            }
            // If x is smaller,
            // element is on left side
            // so ignore right half
            else {
                right = mid - 1;
            }

            // If we reach here,
            // element is not present
        }
        return notfound;
    }

    /**
     * This method is to find the end of the array to allow us to use this method in
     * our search.
     * @return lastindex the last index of the array.
     */
    private int endofarray() {
        int lastindex = -1;
        for (int i = 0; i < accounts.length && accounts[i] != null; i++) {
            lastindex = i;
        }
        return lastindex;
    }

}
