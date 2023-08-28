/**
 * Holds all of the information for one hash table bucket when open addressing
 * (or probing) is used.
 *
 * @author Jason Heard
 */
public class BucketOpen {

    private long accountNumber;
    private String name;
    private String address;
    private double creditlimit;
    private double balance;

    /**
     * The data for this bucket.
     */
    private int data;

    /**
     * Flag that indicates if this bucket has been deleted and its data should be
     * ignored.
     */
    private boolean deleted;

    /**
     * Constructs a bucket with the given data.
     * @param name   is the name
     * @param address  is the address
     * @param creditlimit  is the credit limit
     * @param balance  is the balance
     */
    public BucketOpen(long accountNumber, String name, String address, double creditlimit, double balance) {
        this.accountNumber = accountNumber;
        this.setName(name);
        this.setAddress(address);
        this.setCreditlimit(creditlimit);
        this.setBalance(balance);

    }

    /**
     * Gets the data in this bucket.
     *
     * @return the data in this bucket.
     */
    public String getData() {
        return this.accountNumber + "\n" + this.name + "\n" + this.address + "\n" + this.creditlimit + "\n"
                + this.balance;
    }

    /**
     * Determines if this bucket has been deleted.
     *
     * @return {@code true} if this bucket is deleted; {@code false} otherwise.
     */
    public boolean isDeleted() {
        return this.deleted;
    }

    /**
     * Updates the data in this bucket to the given value.
     *
     * @param data the new data for this bucket.
     */
    public void setData(int data) {
        this.data = data;
    }

    /**
     * Updates the deleted flag to the given value.
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * gets the account number in the bucket.
     * @return the account number
     */
    public long getAccountNumber() {

        return this.accountNumber;

    }

    /**
    * gets the name in the bucket.
    * @return the name.
    */
    public String getName() {
        return name;
    }

    /**
     * sets the name in the bucket.
     * @param name that we need to set it to
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * gets the address in the bucket.
     * @return gets the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * gets the account number in the bucket.
     * @param address we need to set it to
     */
    public void setAddress(String address) {
        this.address = address;
    }
    /**
     * gets the bucket account credit limit.
     * @return gets the credit limit
     */

    public double getCreditlimit() {
        return creditlimit;
    }
    /**
     * sets the bucket account credit limit.
     * @param creditlimit is set to it
     */

    public void setCreditlimit(double creditlimit) {
        this.creditlimit = creditlimit;
    }
    /**
     * gets the bucket account balance.
     * @return gets the balance
     */

    public double getBalance() {
        return balance;
    }

    /**
     * sets the bucket account.
     * @param balance is set to it
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

}
