import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class VendingMachineTest {

	private static final double DELTA = 0.000001;

	@Test
	void startsWithZeroBalanceAndEmptySlots() {
		VendingMachine machine = new VendingMachine();

		assertEquals(0.0, machine.getBalance(), DELTA);
		assertFalse(machine.makePurchase(VendingMachine.A_CODE));
		assertFalse(machine.makePurchase(VendingMachine.B_CODE));
		assertFalse(machine.makePurchase(VendingMachine.C_CODE));
		assertFalse(machine.makePurchase(VendingMachine.D_CODE));
	}

	@ParameterizedTest
	@ValueSource(strings = { "A", "B", "C", "D" })
	void addItemStoresItemInEachValidSlot(String code) {
		VendingMachine machine = new VendingMachine();
		VendingMachineItem item = new VendingMachineItem("Chips", 1.25);

		machine.addItem(item, code);

		assertSame(item, machine.removeItem(code));
	}

	@ParameterizedTest
	@ValueSource(strings = { "E", "a", "" })
	void addItemRejectsInvalidCodesWithoutChangingValidSlots(String code) {
		VendingMachine machine = new VendingMachine();
		VendingMachineItem item = new VendingMachineItem("Chips", 1.25);

		assertThrows(VendingMachineException.class, () -> machine.addItem(item, code));
		machine.addItem(item, VendingMachine.A_CODE);
		assertSame(item, machine.removeItem(VendingMachine.A_CODE));
	}

	@Test
	void addItemRejectsAnOccupiedSlotAndPreservesItsItem() {
		VendingMachine machine = new VendingMachine();
		VendingMachineItem original = new VendingMachineItem("Chips", 1.25);
		VendingMachineItem replacement = new VendingMachineItem("Candy", 0.75);
		machine.addItem(original, VendingMachine.A_CODE);

		assertThrows(VendingMachineException.class,
				() -> machine.addItem(replacement, VendingMachine.A_CODE));

		assertSame(original, machine.removeItem(VendingMachine.A_CODE));
	}

	@Test
	void removeItemReturnsAndRemovesTheItem() {
		VendingMachine machine = new VendingMachine();
		VendingMachineItem item = new VendingMachineItem("Chips", 1.25);
		machine.addItem(item, VendingMachine.C_CODE);

		assertSame(item, machine.removeItem(VendingMachine.C_CODE));
		assertFalse(machine.makePurchase(VendingMachine.C_CODE));
	}

	@Test
	void removeItemRejectsAnEmptySlot() {
		VendingMachine machine = new VendingMachine();

		assertThrows(VendingMachineException.class,
				() -> machine.removeItem(VendingMachine.A_CODE));
	}

	@Test
	void removeItemRejectsAnInvalidCodeWithoutRemovingOtherItems() {
		VendingMachine machine = new VendingMachine();
		VendingMachineItem item = new VendingMachineItem("Chips", 1.25);
		machine.addItem(item, VendingMachine.A_CODE);

		assertThrows(VendingMachineException.class, () -> machine.removeItem("E"));

		assertSame(item, machine.removeItem(VendingMachine.A_CODE));
	}

	@ParameterizedTest
	@CsvSource({ "0.0, 0.0", "0.01, 0.01", "2.50, 2.50" })
	void insertMoneyAcceptsAmountsAtOrAboveZeroAndUpdatesBalance(
			double amount, double expectedBalance) {
		VendingMachine machine = new VendingMachine();

		machine.insertMoney(amount);

		assertEquals(expectedBalance, machine.getBalance(), DELTA);
	}

	@Test
	void insertMoneyAddsToTheExistingBalance() {
		VendingMachine machine = new VendingMachine();
		machine.insertMoney(1.25);

		machine.insertMoney(0.75);

		assertEquals(2.0, machine.getBalance(), DELTA);
	}

	@Test
	void insertMoneyRejectsNegativeAmountsWithoutChangingBalance() {
		VendingMachine machine = new VendingMachine();
		machine.insertMoney(1.25);

		assertThrows(VendingMachineException.class, () -> machine.insertMoney(-0.01));

		assertEquals(1.25, machine.getBalance(), DELTA);
	}

	@Test
	void getBalanceReturnsTheCurrentBalanceWithoutChangingIt() {
		VendingMachine machine = new VendingMachine();
		machine.insertMoney(2.35);

		assertEquals(2.35, machine.getBalance(), DELTA);
		assertEquals(2.35, machine.getBalance(), DELTA);
	}

	@Test
	void purchaseFromAnEmptySlotReturnsFalseAndPreservesBalance() {
		VendingMachine machine = new VendingMachine();
		machine.insertMoney(2.00);

		assertFalse(machine.makePurchase(VendingMachine.A_CODE));

		assertEquals(2.00, machine.getBalance(), DELTA);
	}

	@Test
	void purchaseWithInsufficientBalanceReturnsFalseAndPreservesItemAndBalance() {
		VendingMachine machine = new VendingMachine();
		VendingMachineItem item = new VendingMachineItem("Chips", 1.25);
		machine.addItem(item, VendingMachine.B_CODE);
		machine.insertMoney(1.24);

		assertFalse(machine.makePurchase(VendingMachine.B_CODE));

		assertEquals(1.24, machine.getBalance(), DELTA);
		assertSame(item, machine.removeItem(VendingMachine.B_CODE));
	}

	@Test
	void purchaseWithExactBalanceSucceedsAndEmptiesTheSlot() {
		VendingMachine machine = new VendingMachine();
		machine.addItem(new VendingMachineItem("Chips", 1.25), VendingMachine.C_CODE);
		machine.insertMoney(1.25);

		assertTrue(machine.makePurchase(VendingMachine.C_CODE));

		assertEquals(0.0, machine.getBalance(), DELTA);
		assertFalse(machine.makePurchase(VendingMachine.C_CODE));
	}

	@Test
	void purchaseWithExcessBalanceKeepsTheRemainderAsChange() {
		VendingMachine machine = new VendingMachine();
		machine.addItem(new VendingMachineItem("Chips", 1.25), VendingMachine.D_CODE);
		machine.insertMoney(2.00);

		assertTrue(machine.makePurchase(VendingMachine.D_CODE));

		assertEquals(0.75, machine.getBalance(), DELTA);
		assertFalse(machine.makePurchase(VendingMachine.D_CODE));
	}

	@Test
	void returnChangeReturnsTheBalanceAndResetsItToZero() {
		VendingMachine machine = new VendingMachine();
		machine.insertMoney(2.35);

		assertEquals(2.35, machine.returnChange(), DELTA);
		assertEquals(0.0, machine.getBalance(), DELTA);
		assertEquals(0.0, machine.returnChange(), DELTA);
	}
}
