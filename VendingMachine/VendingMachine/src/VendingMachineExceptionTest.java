import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class VendingMachineExceptionTest {

	@Test
	void defaultConstructor_hasNoMessage() {
		// Arrange / Act
		VendingMachineException exception = new VendingMachineException();

		// Assert
		assertNull(exception.getMessage());
	}

	@Test
	void messageConstructor_preservesMessage() {
		// Arrange
		String message = "Invalid vending machine operation";

		// Act
		VendingMachineException exception = new VendingMachineException(message);

		// Assert
		assertEquals(message, exception.getMessage());
	}
}
