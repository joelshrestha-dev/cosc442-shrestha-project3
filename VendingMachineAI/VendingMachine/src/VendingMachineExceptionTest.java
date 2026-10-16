import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class VendingMachineExceptionTest {

	@Test
	void noArgumentConstructorCreatesExceptionWithoutMessage() {
		VendingMachineException exception = new VendingMachineException();

		assertNull(exception.getMessage());
	}

	@Test
	void messageConstructorPreservesTheMessage() {
		VendingMachineException exception = new VendingMachineException("Invalid item");

		assertEquals("Invalid item", exception.getMessage());
	}
}
