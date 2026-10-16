import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class VendingMachineTest {

	@Test
	void testConstructor_initializesEmptyMachine() {
		// Arrange
		VendingMachine machine = new VendingMachine();

		// Act / Assert
		assertEquals(0.0, machine.getBalance(), 0.000001);
		assertNull(machine.getItem(VendingMachine.A_CODE));
		assertNull(machine.getItem(VendingMachine.B_CODE));
		assertNull(machine.getItem(VendingMachine.C_CODE));
		assertNull(machine.getItem(VendingMachine.D_CODE));
	}

	@Test
	void testAddItem_validSlot_storesItem() {
		// Arrange
		VendingMachine machine = new VendingMachine();
		VendingMachineItem item = new VendingMachineItem("Chips", 1.75);

		// Act
		machine.addItem(item, VendingMachine.A_CODE);

		// Assert
		assertSame(item, machine.getItem(VendingMachine.A_CODE));
	}

	@Test
	void testAddItem_occupiedSlot_throwsException() {
		// Arrange
		VendingMachine machine = new VendingMachine();
		VendingMachineItem existingItem = new VendingMachineItem("Chips", 1.75);
		machine.addItem(existingItem, VendingMachine.A_CODE);

		// Act
		VendingMachineException exception = assertThrows(
				VendingMachineException.class,
				() -> machine.addItem(new VendingMachineItem("Candy", 1.25), VendingMachine.A_CODE));

		// Assert
		assertEquals("Slot A already occupied", exception.getMessage());
		assertSame(existingItem, machine.getItem(VendingMachine.A_CODE));
	}

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "E", "a", "", "AA", "1", " " })
	void testAddItem_invalidSlotCode_throwsException(String code) {
		// Arrange
		VendingMachine machine = new VendingMachine();
		VendingMachineItem item = new VendingMachineItem("Chips", 1.75);

		// Act
		VendingMachineException exception = assertThrows(
				VendingMachineException.class,
				() -> machine.addItem(item, code));

		// Assert
		assertEquals("Invalid code for vending machine item", exception.getMessage());
	}

	@Test
	void testGetItem_invalidSlotCode_throwsException() {
		// Arrange
		VendingMachine machine = new VendingMachine();

		// Act
		VendingMachineException exception = assertThrows(
				VendingMachineException.class,
				() -> machine.getItem("Z"));

		// Assert
		assertEquals("Invalid code for vending machine item", exception.getMessage());
	}

	@Test
	void testRemoveItem_populatedSlot_returnsItemAndEmptiesSlot() {
		// Arrange
		VendingMachine machine = new VendingMachine();
		VendingMachineItem item = new VendingMachineItem("Chips", 1.75);
		machine.addItem(item, VendingMachine.D_CODE);

		// Act
		VendingMachineItem removedItem = machine.removeItem(VendingMachine.D_CODE);

		// Assert
		assertSame(item, removedItem);
		assertNull(machine.getItem(VendingMachine.D_CODE));
	}

	@Test
	void testRemoveItem_emptySlot_throwsException() {
		// Arrange
		VendingMachine machine = new VendingMachine();

		// Act
		VendingMachineException exception = assertThrows(
				VendingMachineException.class,
				() -> machine.removeItem(VendingMachine.B_CODE));

		// Assert
		assertEquals("Slot B is empty -- cannot remove item", exception.getMessage());
	}

	@Test
	void testRemoveItem_invalidSlotCode_throwsException() {
		// Arrange
		VendingMachine machine = new VendingMachine();

		// Act
		VendingMachineException exception = assertThrows(
				VendingMachineException.class,
				() -> machine.removeItem("Z"));

		// Assert
		assertEquals("Invalid code for vending machine item", exception.getMessage());
	}

	@Test
	void testInsertMoney_validAmount_increasesBalance() {
		// Arrange
		VendingMachine machine = new VendingMachine();

		// Act
		machine.insertMoney(5.0);

		// Assert
		assertEquals(5.0, machine.getBalance(), 0.000001);
	}

	@ParameterizedTest
	@CsvSource({
			"0.0, 0.0",
			"0.01, 0.01",
			"0.99, 0.99",
			"1.0, 1.0",
			"1.01, 1.01",
			"20.0, 20.0"
	})
	void testInsertMoney_nonnegativeAmounts_increaseBalance(double amount, double expectedBalance) {
		// Arrange
		VendingMachine machine = new VendingMachine();

		// Act
		machine.insertMoney(amount);

		// Assert
		assertEquals(expectedBalance, machine.getBalance(), 0.000001);
	}

	@Test
	void testInsertMoney_negativeAmount_throwsException() {
		// Arrange
		VendingMachine machine = new VendingMachine();

		// Act
		VendingMachineException exception = assertThrows(
				VendingMachineException.class,
				() -> machine.insertMoney(-0.01));

		// Assert
		assertEquals("Invalid amount.  Amount must be >= 0", exception.getMessage());
		assertEquals(0.0, machine.getBalance(), 0.000001);
	}

	@Test
	void testMakePurchase_exactBalance_completesPurchase() {
		// Arrange
		VendingMachine machine = new VendingMachine();
		VendingMachineItem item = new VendingMachineItem("Chips", 2.5);
		machine.addItem(item, VendingMachine.A_CODE);
		machine.insertMoney(2.5);

		// Act
		boolean purchased = machine.makePurchase(VendingMachine.A_CODE);

		// Assert
		assertTrue(purchased);
		assertEquals(0.0, machine.getBalance(), 0.000001);
		assertNull(machine.getItem(VendingMachine.A_CODE));
	}

	@Test
	void testMakePurchase_greaterBalance_returnsRemainder() {
		// Arrange
		VendingMachine machine = new VendingMachine();
		machine.addItem(new VendingMachineItem("Chips", 2.5), VendingMachine.A_CODE);
		machine.insertMoney(3.0);

		// Act
		boolean purchased = machine.makePurchase(VendingMachine.A_CODE);

		// Assert
		assertTrue(purchased);
		assertEquals(0.5, machine.getBalance(), 0.000001);
	}

	@Test
	void testMakePurchase_insufficientBalance_preservesState() {
		// Arrange
		VendingMachine machine = new VendingMachine();
		VendingMachineItem item = new VendingMachineItem("Chips", 2.5);
		machine.addItem(item, VendingMachine.A_CODE);
		machine.insertMoney(2.49);

		// Act
		boolean purchased = machine.makePurchase(VendingMachine.A_CODE);

		// Assert
		assertFalse(purchased);
		assertEquals(2.49, machine.getBalance(), 0.000001);
		assertSame(item, machine.getItem(VendingMachine.A_CODE));
	}

	@Test
	void testMakePurchase_emptySlot_returnsFalse() {
		// Arrange
		VendingMachine machine = new VendingMachine();

		// Act
		boolean purchased = machine.makePurchase(VendingMachine.C_CODE);

		// Assert
		assertFalse(purchased);
		assertEquals(0.0, machine.getBalance(), 0.000001);
	}

	@Test
	void testMakePurchase_invalidSlotCode_throwsException() {
		// Arrange
		VendingMachine machine = new VendingMachine();

		// Act / Assert
		assertThrows(VendingMachineException.class, () -> machine.makePurchase("Z"));
	}

	@Test
	void testReturnChange_returnsBalanceAndResetsMachine() {
		// Arrange
		VendingMachine machine = new VendingMachine();
		machine.insertMoney(4.25);

		// Act
		double returnedChange = machine.returnChange();

		// Assert
		assertEquals(4.25, returnedChange, 0.000001);
		assertEquals(0.0, machine.getBalance(), 0.000001);
		assertEquals(0.0, machine.returnChange(), 0.000001);
	}
}
