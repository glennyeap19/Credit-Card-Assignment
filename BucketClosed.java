/**
 * Holds all of the information for one hash table bucket when closed
 * addressing (or chaining) is used.
 *
 * @author Jason Heard
 */
public class BucketClosed {

    /**
     * The data for this bucket.
     */
    private int data;

    /**
     * The next bucket in this chain or {@code null} if this is the last bucket
     * in the chain.
     */
    private BucketClosed next;

    /**
     * Constructs a bucket with the given data. The deleted flag is set to
     * {@code false}.
     *
     * @param data the data for the new bucket.
     */
    public BucketClosed(int data) {
        this.data = data;
        this.next = null;
    }

    /**
     * Gets the data in this bucket.
     *
     * @return the data in this bucket.
     */
    public int getData() {
        return this.data;
    }

    /**
     * Gets the next bucket in this chain.
     *
     * @return the next bucket in this chain or {@code null} if this is the
     * last bucket in the chain.
     */
    public BucketClosed getNext() {
        return this.next;
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
     * Updates the next bucket in this chain to the given value.
     */
    public void setNext(BucketClosed next) {
        this.next = next;
    }

}
