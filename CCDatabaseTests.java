import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * Tests for a lookup queue implementation.
 *
 * @author Jason Heard
 * @version 1.0
 */
public class CCDatabaseTests {

    /**
     * Constructs a lookup queue to test. This should be updated to construct
     * your implementation.
     *
     * @return The lookup queue implementation to test.
     */
    public static CCDatabase makeSortedCCDatabase() {
        return new SortedDataBase();
    }

    /**
     * Constructs a lookup queue to test. This should be updated to construct
     * your implementation.
     *
     * @return The lookup queue implementation to test.
     */
    public static CCDatabase makeSimpleHashCCDatabase() {
        return new HashTableDataBase();
    }

    /**
     * Constructs a lookup queue to test. This should be updated to construct
     * your implementation.
     *
     * @return The lookup queue implementation to test.
     */
    public static CCDatabase makeRobinHoodCCDatabase() {
        return new RobinHoodDataBase();
    }

    /**
     * Tests that the deleteAccount operation works on the sorted array
     * database. This relies on createAccount working as accounts must be
     * created to be deleted.
     */
    @Test
    public void deleteWorksOnSorted() {
        final CCDatabase database1 = makeSortedCCDatabase();
        final CCDatabase database2 = makeSortedCCDatabase();

        assertTrue(database1.createAccount(2829867676960107L, "Jason", "Address 1", 100.0, 0.0));
        assertTrue(database1.createAccount(1654317986047782L, "George", "Address 2", 200.0, 0.0));
        assertTrue(database1.createAccount(6673817496516402L, "Mark", "Address 3", 300.0, 0.0));

        // ensure databases do not share data
        assertFalse(database2.deleteAccount(2829867676960107L));
        assertFalse(database2.deleteAccount(1654317986047782L));
        assertFalse(database2.deleteAccount(6673817496516402L));

        // deleting non-existant accounts returns false
        assertFalse(database1.deleteAccount(9379791064533190L));
        assertFalse(database1.deleteAccount(2807976327083665L));
        assertFalse(database1.deleteAccount(1157133257902599L));

        // deleting existing accounts returns true only the first time
        assertTrue(database1.deleteAccount(2829867676960107L));
        assertFalse(database1.deleteAccount(2829867676960107L));
        assertTrue(database1.deleteAccount(1654317986047782L));
        assertFalse(database1.deleteAccount(1654317986047782L));
        assertTrue(database1.deleteAccount(6673817496516402L));
        assertFalse(database1.deleteAccount(6673817496516402L));
    }

    /**
     * Tests that the adjustCreditLimit operation works on the sorted array
     * database. This relies on createAccount and getAccount working to first
     * create the account and afterwards verify that the credit limits were
     * adjusted.
     */
    @Test
    public void adjustWorksOnSorted() {
        final CCDatabase database1 = makeSortedCCDatabase();
        final CCDatabase database2 = makeSortedCCDatabase();

        assertTrue(database1.createAccount(2829867676960107L, "Jason", "Address 1", 100.0, 0.0));
        assertTrue(database1.createAccount(1654317986047782L, "George", "Address 2", 200.0, 0.0));
        assertTrue(database1.createAccount(6673817496516402L, "Mark", "Address 3", 300.0, 0.0));

        // ensure databases do not share data
        assertFalse(database2.adjustCreditLimit(2829867676960107L, 42.0));
        assertFalse(database2.adjustCreditLimit(1654317986047782L, 101.0));
        assertFalse(database2.adjustCreditLimit(6673817496516402L, 20000.0));

        // adjusting non-existant accounts returns false
        assertFalse(database1.adjustCreditLimit(9379791064533190L, 42.0));
        assertFalse(database1.adjustCreditLimit(2807976327083665L, 101.0));
        assertFalse(database1.adjustCreditLimit(1157133257902599L, 20000.0));

        // adjusting existing accounts returns true
        assertTrue(database1.adjustCreditLimit(2829867676960107L, 42.0));
        assertTrue(database1.adjustCreditLimit(1654317986047782L, 101.0));
        assertTrue(database1.adjustCreditLimit(6673817496516402L, 20000.0));

        // adjusting credit limit should be reflected when getting the account
        assertEquals("2829867676960107\nJason\nAddress 1\n42.0\n0.0", database1.getAccount(2829867676960107L));
        assertEquals("1654317986047782\nGeorge\nAddress 2\n101.0\n0.0", database1.getAccount(1654317986047782L));
        assertEquals("6673817496516402\nMark\nAddress 3\n20000.0\n0.0", database1.getAccount(6673817496516402L));
    }

    /**
     * Tests that the getAccount operation works on the sorted array database.
     * This relies on createAccount working as accounts must be created to be
     * gotten.
     */
    @Test
    public void getWorksOnSorted() {
        final CCDatabase database1 = makeSortedCCDatabase();
        final CCDatabase database2 = makeSortedCCDatabase();

        assertTrue(database1.createAccount(2829867676960107L, "Jason", "Address 1", 100.0, 0.0));
        assertTrue(database1.createAccount(1654317986047782L, "George", "Address 2", 200.0, 0.0));
        assertTrue(database1.createAccount(6673817496516402L, "Mark", "Address 3", 300.0, 0.0));

        // ensure databases do not share data
        assertNull(database2.getAccount(2829867676960107L));
        assertNull(database2.getAccount(1654317986047782L));
        assertNull(database2.getAccount(6673817496516402L));

        // adjusting non-existant accounts returns null
        assertNull(database1.getAccount(9379791064533190L));
        assertNull(database1.getAccount(2807976327083665L));
        assertNull(database1.getAccount(1157133257902599L));

        // get should return the initial details
        assertEquals("2829867676960107\nJason\nAddress 1\n100.0\n0.0", database1.getAccount(2829867676960107L));
        assertEquals("1654317986047782\nGeorge\nAddress 2\n200.0\n0.0", database1.getAccount(1654317986047782L));
        assertEquals("6673817496516402\nMark\nAddress 3\n300.0\n0.0", database1.getAccount(6673817496516402L));
    }

    /**
     * Tests that the makePurchase operation works on the sorted array database.
     * This relies on createAccount working as accounts must be created to be
     * used for a purchase.
     */
    @Test
    public void makePurchaseWorksOnSorted() throws Exception {
        final CCDatabase database1 = makeSortedCCDatabase();
        final CCDatabase database2 = makeSortedCCDatabase();

        assertTrue(database1.createAccount(2829867676960107L, "Jason", "Address 1", 100.0, 0.0));
        assertTrue(database1.createAccount(1654317986047782L, "George", "Address 2", 200.0, 0.0));
        assertTrue(database1.createAccount(6673817496516402L, "Mark", "Address 3", 300.0, 0.0));

        // ensure databases do not share data
        assertFalse(database2.makePurchase(2829867676960107L, 42.0));
        assertFalse(database2.makePurchase(1654317986047782L, 101.0));
        assertFalse(database2.makePurchase(6673817496516402L, 300.0));

        // purchases on non-existant accounts returns false
        assertFalse(database1.makePurchase(9379791064533190L, 42.0));
        assertFalse(database1.makePurchase(2807976327083665L, 101.0));
        assertFalse(database1.makePurchase(1157133257902599L, 300.0));

        // purchases on existing accounts returns true
        assertTrue(database1.makePurchase(2829867676960107L, 42.0));
        assertTrue(database1.makePurchase(1654317986047782L, 101.0));
        assertTrue(database1.makePurchase(6673817496516402L, 300.0));

        // unless they throw an exception...
         try {
            // way too much
            database1.makePurchase(2829867676960107L, 1000.0);
            fail("Expected an exception");
        } catch (Exception ex) {
            // this is expected
        }
        try {
            // 0.01 too much
            database1.makePurchase(1654317986047782L, 99.01);
            fail("Expected an exception");
        } catch (Exception ex) {
            // this is expected
        }

        // valid purchases should be reflected when getting the account
        assertEquals("2829867676960107\nJason\nAddress 1\n100.0\n42.0", database1.getAccount(2829867676960107L));
        assertEquals("1654317986047782\nGeorge\nAddress 2\n200.0\n101.0", database1.getAccount(1654317986047782L));
        assertEquals("6673817496516402\nMark\nAddress 3\n300.0\n300.0", database1.getAccount(6673817496516402L));
    }

    /**
     * Tests that all of the operations work on a large sorted array database.
     */
    @Test(timeout = 1000)
    public void stressTestSorted() {
        final CCDatabase database = makeSortedCCDatabase();
        // sorted array can't do as much
        final int size = 100;

        List<Long> accounts = new ArrayList<>(3 * size);
        for (int i = 0; i < 3 * size; i++) {
            accounts.add(i * 7919L);
        }

        Collections.shuffle(accounts);

        // create lots of accounts
        for (int i = 0; i < 2 * size; i++) {
            assertTrue(database.createAccount(accounts.get(i), "Name", "Address", 1000.0, 0.0));
        }

        // create duplicates
        for (int i = 0; i < 2 * size; i++) {
            assertFalse(database.createAccount(accounts.get(i), "Name", "Address", 1000.0, 0.0));
        }

        // test adjust existing
        for (int i = 0; i < size; i++) {
            assertTrue(database.adjustCreditLimit(accounts.get(i), 2000.0));
        }
        // test adjust missing
        for (int i = 2 * size; i < 3 * size; i++) {
            assertFalse(database.adjustCreditLimit(accounts.get(i), 2000.0));
        }

        // test delete
        for (int i = 0; i < size; i++) {
            assertTrue(database.deleteAccount(accounts.get(i)));
        }

        // add more
        for (int i = 2 * size; i < 3 * size; i++) {
            assertTrue(database.createAccount(accounts.get(i), "Name", "Address", 1000.0, 0.0));
        }

        // test get missing
        for (int i = 0; i < size; i++) {
            assertNull(database.getAccount(accounts.get(i)));
        }
        // test get existing (ignoring actual text)
        for (int i = 1 * size; i < 3 * size; i++) {
            assertNotNull(database.getAccount(accounts.get(i)));
        }
    }

    /**
     * Tests that the deleteAccount operation works on the simple hash table
     * database. This relies on createAccount working as accounts must be
     * created to be deleted.
     */
    @Test
    public void deleteWorksOnSimpleHash() {
        final CCDatabase database1 = makeSimpleHashCCDatabase();
        final CCDatabase database2 = makeSimpleHashCCDatabase();

        assertTrue(database1.createAccount(2829867676960107L, "Jason", "Address 1", 100.0, 0.0));
        assertTrue(database1.createAccount(1654317986047782L, "George", "Address 2", 200.0, 0.0));
        assertTrue(database1.createAccount(6673817496516402L, "Mark", "Address 3", 300.0, 0.0));

        // ensure databases do not share data
        assertFalse(database2.deleteAccount(2829867676960107L));
        assertFalse(database2.deleteAccount(1654317986047782L));
        assertFalse(database2.deleteAccount(6673817496516402L));

        // deleting non-existant accounts returns false
        assertFalse(database1.deleteAccount(9379791064533190L));
        assertFalse(database1.deleteAccount(2807976327083665L));
        assertFalse(database1.deleteAccount(1157133257902599L));

        // deleting existing accounts returns true only the first time
        assertTrue(database1.deleteAccount(2829867676960107L));
        assertFalse(database1.deleteAccount(2829867676960107L));
        assertTrue(database1.deleteAccount(1654317986047782L));
        assertFalse(database1.deleteAccount(1654317986047782L));
        assertTrue(database1.deleteAccount(6673817496516402L));
        assertFalse(database1.deleteAccount(6673817496516402L));
    }

    /**
     * Tests that the adjustCreditLimit operation works on the simple hash table
     * database. This relies on createAccount and getAccount working to first
     * create the account and afterwards verify that the credit limits were
     * adjusted.
     */
    @Test
    public void adjustWorksOnSimpleHash() {
        final CCDatabase database1 = makeSimpleHashCCDatabase();
        final CCDatabase database2 = makeSimpleHashCCDatabase();

        assertTrue(database1.createAccount(2829867676960107L, "Jason", "Address 1", 100.0, 0.0));
        assertTrue(database1.createAccount(1654317986047782L, "George", "Address 2", 200.0, 0.0));
        assertTrue(database1.createAccount(6673817496516402L, "Mark", "Address 3", 300.0, 0.0));

        // ensure databases do not share data
        assertFalse(database2.adjustCreditLimit(2829867676960107L, 42.0));
        assertFalse(database2.adjustCreditLimit(1654317986047782L, 101.0));
        assertFalse(database2.adjustCreditLimit(6673817496516402L, 20000.0));

        // adjusting non-existant accounts returns false
        assertFalse(database1.adjustCreditLimit(9379791064533190L, 42.0));
        assertFalse(database1.adjustCreditLimit(2807976327083665L, 101.0));
        assertFalse(database1.adjustCreditLimit(1157133257902599L, 20000.0));

        // adjusting existing accounts returns true
        assertTrue(database1.adjustCreditLimit(2829867676960107L, 42.0));
        assertTrue(database1.adjustCreditLimit(1654317986047782L, 101.0));
        assertTrue(database1.adjustCreditLimit(6673817496516402L, 20000.0));

        // adjusting credit limit should be reflected when getting the account
        assertEquals("2829867676960107\nJason\nAddress 1\n42.0\n0.0", database1.getAccount(2829867676960107L));
        assertEquals("1654317986047782\nGeorge\nAddress 2\n101.0\n0.0", database1.getAccount(1654317986047782L));
        assertEquals("6673817496516402\nMark\nAddress 3\n20000.0\n0.0", database1.getAccount(6673817496516402L));
    }

    /**
     * Tests that the getAccount operation works on the simple hash table database.
     * This relies on createAccount working as accounts must be created to be
     * gotten.
     */
    @Test
    public void getWorksOnSimpleHash() {
        final CCDatabase database1 = makeSimpleHashCCDatabase();
        final CCDatabase database2 = makeSimpleHashCCDatabase();

        assertTrue(database1.createAccount(2829867676960107L, "Jason", "Address 1", 100.0, 0.0));
        assertTrue(database1.createAccount(1654317986047782L, "George", "Address 2", 200.0, 0.0));
        assertTrue(database1.createAccount(6673817496516402L, "Mark", "Address 3", 300.0, 0.0));

        // ensure databases do not share data
        assertNull(database2.getAccount(2829867676960107L));
        assertNull(database2.getAccount(1654317986047782L));
        assertNull(database2.getAccount(6673817496516402L));

        // adjusting non-existant accounts returns null
        assertNull(database1.getAccount(9379791064533190L));
        assertNull(database1.getAccount(2807976327083665L));
        assertNull(database1.getAccount(1157133257902599L));

        // get should return the initial details
        assertEquals("2829867676960107\nJason\nAddress 1\n100.0\n0.0", database1.getAccount(2829867676960107L));
        assertEquals("1654317986047782\nGeorge\nAddress 2\n200.0\n0.0", database1.getAccount(1654317986047782L));
        assertEquals("6673817496516402\nMark\nAddress 3\n300.0\n0.0", database1.getAccount(6673817496516402L));
    }

    /**
     * Tests that the makePurchase operation works on the simple hash table database.
     * This relies on createAccount working as accounts must be created to be
     * used for a purchase.
     */
    @Test
    public void makePurchaseWorksOnSimpleHash() throws Exception {
        final CCDatabase database1 = makeSimpleHashCCDatabase();
        final CCDatabase database2 = makeSimpleHashCCDatabase();

        assertTrue(database1.createAccount(2829867676960107L, "Jason", "Address 1", 100.0, 0.0));
        assertTrue(database1.createAccount(1654317986047782L, "George", "Address 2", 200.0, 0.0));
        assertTrue(database1.createAccount(6673817496516402L, "Mark", "Address 3", 300.0, 0.0));

        // ensure databases do not share data
        assertFalse(database2.makePurchase(2829867676960107L, 42.0));
        assertFalse(database2.makePurchase(1654317986047782L, 101.0));
        assertFalse(database2.makePurchase(6673817496516402L, 300.0));

        // purchases on non-existant accounts returns false
        assertFalse(database1.makePurchase(9379791064533190L, 42.0));
        assertFalse(database1.makePurchase(2807976327083665L, 101.0));
        assertFalse(database1.makePurchase(1157133257902599L, 300.0));

        // purchases on existing accounts returns true
        assertTrue(database1.makePurchase(2829867676960107L, 42.0));
        assertTrue(database1.makePurchase(1654317986047782L, 101.0));
        assertTrue(database1.makePurchase(6673817496516402L, 300.0));

        // unless they throw an exception...
         try {
            // way too much
            database1.makePurchase(2829867676960107L, 1000.0);
            fail("Expected an exception");
        } catch (Exception ex) {
            // this is expected
        }
        try {
            // 0.01 too much
            database1.makePurchase(1654317986047782L, 99.01);
            fail("Expected an exception");
        } catch (Exception ex) {
            // this is expected
        }

        // valid purchases should be reflected when getting the account
        assertEquals("2829867676960107\nJason\nAddress 1\n100.0\n42.0", database1.getAccount(2829867676960107L));
        assertEquals("1654317986047782\nGeorge\nAddress 2\n200.0\n101.0", database1.getAccount(1654317986047782L));
        assertEquals("6673817496516402\nMark\nAddress 3\n300.0\n300.0", database1.getAccount(6673817496516402L));
    }

    /**
     * Tests that all of the operations work on a large simple hash table database.
     */
    @Test(timeout = 1000)
    public void stressTestSimpleHash() {
        final CCDatabase database = makeSimpleHashCCDatabase();
        // hash tables should be able to handle more
        final int size = 1000;

        List<Long> accounts = new ArrayList<>(3 * size);
        for (int i = 0; i < 3 * size; i++) {
            accounts.add(i * 7919L);
        }

        Collections.shuffle(accounts);

        // create lots of accounts
        for (int i = 0; i < 2 * size; i++) {
            assertTrue(database.createAccount(accounts.get(i), "Name", "Address", 1000.0, 0.0));
        }

        // create duplicates
        for (int i = 0; i < 2 * size; i++) {
            assertFalse(database.createAccount(accounts.get(i), "Name", "Address", 1000.0, 0.0));
        }

        // test adjust existing
        for (int i = 0; i < size; i++) {
            assertTrue(database.adjustCreditLimit(accounts.get(i), 2000.0));
        }
        // test adjust missing
        for (int i = 2 * size; i < 3 * size; i++) {
            assertFalse(database.adjustCreditLimit(accounts.get(i), 2000.0));
        }

        // test delete
        for (int i = 0; i < size; i++) {
            assertTrue(database.deleteAccount(accounts.get(i)));
        }

        // add more
        for (int i = 2 * size; i < 3 * size; i++) {
            assertTrue(database.createAccount(accounts.get(i), "Name", "Address", 1000.0, 0.0));
        }

        // test get missing
        for (int i = 0; i < size; i++) {
            assertNull(database.getAccount(accounts.get(i)));
        }
        // test get existing (ignoring actual text)
        for (int i = 1 * size; i < 3 * size; i++) {
            assertNotNull(database.getAccount(accounts.get(i)));
        }
    }

    /**
     * Tests that the deleteAccount operation works on the Robin Hood hash table
     * database. This relies on createAccount working as accounts must be
     * created to be deleted.
     */
    @Test
    public void deleteWorksOnRobinHood() {
        final CCDatabase database1 = makeRobinHoodCCDatabase();
        final CCDatabase database2 = makeRobinHoodCCDatabase();

        assertTrue(database1.createAccount(2829867676960107L, "Jason", "Address 1", 100.0, 0.0));
        assertTrue(database1.createAccount(1654317986047782L, "George", "Address 2", 200.0, 0.0));
        assertTrue(database1.createAccount(6673817496516402L, "Mark", "Address 3", 300.0, 0.0));

        // ensure databases do not share data
        assertFalse(database2.deleteAccount(2829867676960107L));
        assertFalse(database2.deleteAccount(1654317986047782L));
        assertFalse(database2.deleteAccount(6673817496516402L));

        // deleting non-existant accounts returns false
        assertFalse(database1.deleteAccount(9379791064533190L));
        assertFalse(database1.deleteAccount(2807976327083665L));
        assertFalse(database1.deleteAccount(1157133257902599L));

        // deleting existing accounts returns true only the first time
        assertTrue(database1.deleteAccount(2829867676960107L));
        assertFalse(database1.deleteAccount(2829867676960107L));
        assertTrue(database1.deleteAccount(1654317986047782L));
        assertFalse(database1.deleteAccount(1654317986047782L));
        assertTrue(database1.deleteAccount(6673817496516402L));
        assertFalse(database1.deleteAccount(6673817496516402L));
    }

    /**
     * Tests that the adjustCreditLimit operation works on the Robin Hood hash table
     * database. This relies on createAccount and getAccount working to first
     * create the account and afterwards verify that the credit limits were
     * adjusted.
     */
    @Test
    public void adjustWorksOnRobinHood() {
        final CCDatabase database1 = makeRobinHoodCCDatabase();
        final CCDatabase database2 = makeRobinHoodCCDatabase();

        assertTrue(database1.createAccount(2829867676960107L, "Jason", "Address 1", 100.0, 0.0));
        assertTrue(database1.createAccount(1654317986047782L, "George", "Address 2", 200.0, 0.0));
        assertTrue(database1.createAccount(6673817496516402L, "Mark", "Address 3", 300.0, 0.0));

        // ensure databases do not share data
        assertFalse(database2.adjustCreditLimit(2829867676960107L, 42.0));
        assertFalse(database2.adjustCreditLimit(1654317986047782L, 101.0));
        assertFalse(database2.adjustCreditLimit(6673817496516402L, 20000.0));

        // adjusting non-existant accounts returns false
        assertFalse(database1.adjustCreditLimit(9379791064533190L, 42.0));
        assertFalse(database1.adjustCreditLimit(2807976327083665L, 101.0));
        assertFalse(database1.adjustCreditLimit(1157133257902599L, 20000.0));

        // adjusting existing accounts returns true
        assertTrue(database1.adjustCreditLimit(2829867676960107L, 42.0));
        assertTrue(database1.adjustCreditLimit(1654317986047782L, 101.0));
        assertTrue(database1.adjustCreditLimit(6673817496516402L, 20000.0));

        // adjusting credit limit should be reflected when getting the account
        assertEquals("2829867676960107\nJason\nAddress 1\n42.0\n0.0", database1.getAccount(2829867676960107L));
        assertEquals("1654317986047782\nGeorge\nAddress 2\n101.0\n0.0", database1.getAccount(1654317986047782L));
        assertEquals("6673817496516402\nMark\nAddress 3\n20000.0\n0.0", database1.getAccount(6673817496516402L));
    }

    /**
     * Tests that the getAccount operation works on the Robin Hood hash table database.
     * This relies on createAccount working as accounts must be created to be
     * gotten.
     */
    @Test
    public void getWorksOnRobinHood() {
        final CCDatabase database1 = makeRobinHoodCCDatabase();
        final CCDatabase database2 = makeRobinHoodCCDatabase();

        assertTrue(database1.createAccount(2829867676960107L, "Jason", "Address 1", 100.0, 0.0));
        assertTrue(database1.createAccount(1654317986047782L, "George", "Address 2", 200.0, 0.0));
        assertTrue(database1.createAccount(6673817496516402L, "Mark", "Address 3", 300.0, 0.0));

        // ensure databases do not share data
        assertNull(database2.getAccount(2829867676960107L));
        assertNull(database2.getAccount(1654317986047782L));
        assertNull(database2.getAccount(6673817496516402L));

        // adjusting non-existant accounts returns null
        assertNull(database1.getAccount(9379791064533190L));
        assertNull(database1.getAccount(2807976327083665L));
        assertNull(database1.getAccount(1157133257902599L));

        // get should return the initial details
        assertEquals("2829867676960107\nJason\nAddress 1\n100.0\n0.0", database1.getAccount(2829867676960107L));
        assertEquals("1654317986047782\nGeorge\nAddress 2\n200.0\n0.0", database1.getAccount(1654317986047782L));
        assertEquals("6673817496516402\nMark\nAddress 3\n300.0\n0.0", database1.getAccount(6673817496516402L));
    }

    /**
     * Tests that the makePurchase operation works on the Robin Hood hash table database.
     * This relies on createAccount working as accounts must be created to be
     * used for a purchase.
     */
    @Test
    public void makePurchaseWorksOnRobinHood() throws Exception {
        final CCDatabase database1 = makeRobinHoodCCDatabase();
        final CCDatabase database2 = makeRobinHoodCCDatabase();

        assertTrue(database1.createAccount(2829867676960107L, "Jason", "Address 1", 100.0, 0.0));
        assertTrue(database1.createAccount(1654317986047782L, "George", "Address 2", 200.0, 0.0));
        assertTrue(database1.createAccount(6673817496516402L, "Mark", "Address 3", 300.0, 0.0));

        // ensure databases do not share data
        assertFalse(database2.makePurchase(2829867676960107L, 42.0));
        assertFalse(database2.makePurchase(1654317986047782L, 101.0));
        assertFalse(database2.makePurchase(6673817496516402L, 300.0));

        // purchases on non-existant accounts returns false
        assertFalse(database1.makePurchase(9379791064533190L, 42.0));
        assertFalse(database1.makePurchase(2807976327083665L, 101.0));
        assertFalse(database1.makePurchase(1157133257902599L, 300.0));

        // purchases on existing accounts returns true
        assertTrue(database1.makePurchase(2829867676960107L, 42.0));
        assertTrue(database1.makePurchase(1654317986047782L, 101.0));
        assertTrue(database1.makePurchase(6673817496516402L, 300.0));

        // unless they throw an exception...
         try {
            // way too much
            database1.makePurchase(2829867676960107L, 1000.0);
            fail("Expected an exception");
        } catch (Exception ex) {
            // this is expected
        }
        try {
            // 0.01 too much
            database1.makePurchase(1654317986047782L, 99.01);
            fail("Expected an exception");
        } catch (Exception ex) {
            // this is expected
        }

        // valid purchases should be reflected when getting the account
        assertEquals("2829867676960107\nJason\nAddress 1\n100.0\n42.0", database1.getAccount(2829867676960107L));
        assertEquals("1654317986047782\nGeorge\nAddress 2\n200.0\n101.0", database1.getAccount(1654317986047782L));
        assertEquals("6673817496516402\nMark\nAddress 3\n300.0\n300.0", database1.getAccount(6673817496516402L));
    }

    /**
     * Tests that all of the operations work on a large Robin Hood hash table database.
     */
    @Test(timeout = 1000)
    public void stressTestRobinHood() {
        final CCDatabase database = makeRobinHoodCCDatabase();
        // Robin Hood should be able to handle even more!
        final int size = 10000;

        List<Long> accounts = new ArrayList<>(3 * size);
        for (int i = 0; i < 3 * size; i++) {
            accounts.add(i * 7919L);
        }

        Collections.shuffle(accounts);

        // create lots of accounts
        for (int i = 0; i < 2 * size; i++) {
            assertTrue(database.createAccount(accounts.get(i), "Name", "Address", 1000.0, 0.0));
        }

        // create duplicates
        for (int i = 0; i < 2 * size; i++) {
            assertFalse(database.createAccount(accounts.get(i), "Name", "Address", 1000.0, 0.0));
        }

        // test adjust existing
        for (int i = 0; i < size; i++) {
            assertTrue(database.adjustCreditLimit(accounts.get(i), 2000.0));
        }
        // test adjust missing
        for (int i = 2 * size; i < 3 * size; i++) {
            assertFalse(database.adjustCreditLimit(accounts.get(i), 2000.0));
        }

        // test delete
        for (int i = 0; i < size; i++) {
            assertTrue(database.deleteAccount(accounts.get(i)));
        }

        // add more
        for (int i = 2 * size; i < 3 * size; i++) {
            assertTrue(database.createAccount(accounts.get(i), "Name", "Address", 1000.0, 0.0));
        }

        // test get missing
        for (int i = 0; i < size; i++) {
            assertNull(database.getAccount(accounts.get(i)));
        }
        // test get existing (ignoring actual text)
        for (int i = 1 * size; i < 3 * size; i++) {
            assertNotNull(database.getAccount(accounts.get(i)));
        }
    }

    private static class BadCCDatabase implements CCDatabase {
        @Override
        public boolean createAccount (long accountNumber, String name,
            String address, double creditLimit, double balance) {
            return false;
        }

        @Override
        public boolean deleteAccount (long accountNumber) {
            return false;
        }

        @Override
        public boolean adjustCreditLimit (long accountNumber, double newLimit) {
            return false;
        }

        @Override
        public String getAccount (long accountNumber) {
            return "";
        }

        @Override
        public boolean makePurchase (long accountNumber, double price) throws Exception {
            return false;
        }
    }

}
