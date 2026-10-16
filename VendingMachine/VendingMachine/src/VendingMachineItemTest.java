import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class VendingMachineItemTest {

	@Test
	void createsItemWithTypicalValues() {
		VendingMachineItem item = new VendingMachineItem("Chips", 1.75);

		assertAll(
				() -> assertEquals("Chips", item.getName()),
				() -> assertEquals(1.75, item.getPrice()));
	}

	@Test
	void acceptsZeroPrice() {
		VendingMachineItem item = new VendingMachineItem("Free sample", 0.0);

		assertEquals(0.0, item.getPrice());
	}

	@Test
	void rejectsNegativePrice() {
		VendingMachineException exception = assertThrows(
				VendingMachineException.class,
				() -> new VendingMachineItem("Invalid item", -0.01));

		assertEquals("Price cannot be less than zero", exception.getMessage());
	}
}
