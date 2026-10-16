import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class VendingMachineItemTest {

	@ParameterizedTest
	@CsvSource({ "Water, 0.0", "Chips, 1.25", "Candy, 2.50" })
	void constructorStoresNameAndNonnegativePrice(String name, double price) {
		VendingMachineItem item = new VendingMachineItem(name, price);

		assertEquals(name, item.getName());
		assertEquals(price, item.getPrice(), 0.000001);
	}

	@Test
	void constructorRejectsNegativePrice() {
		assertThrows(VendingMachineException.class,
				() -> new VendingMachineItem("Chips", -0.01));
	}

	@Test
	void gettersReturnTheValuesProvidedToTheConstructor() {
		VendingMachineItem item = new VendingMachineItem("Gum", 0.75);

		assertEquals("Gum", item.getName());
		assertEquals(0.75, item.getPrice(), 0.000001);
	}
}
