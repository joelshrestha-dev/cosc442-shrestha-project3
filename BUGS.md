# Vending Machine Fault Log

## Null slot code causes `NullPointerException`

- **Observed failure:** The invalid-code test expected `VendingMachineException`
  for a null slot code but received `NullPointerException`.
- **Test that exposed it:** `VendingMachineTest.testAddItem_invalidSlotCode_throwsException`
  with its `null` input.
- **Source-code fault:** `getSlotIndex` called `code.equals(...)`, so null
  failed before the method could report an invalid slot.
- **Diagnosis:** Ran the parameterized invalid-code test by itself. The failure
  output identified the null dereference in `VendingMachine.getSlotIndex` while
  `addItem` processed the code.
- **Correction:** Compare each slot-code constant with the argument
  (`A_CODE.equals(code)`, etc.). Null now follows the invalid-code exception
  path.

## Constructor indexed beyond the slot array

- **Observed failure:** A constructor loop using `i <= NUM_SLOTS` accesses index
  `NUM_SLOTS` in an array of length `NUM_SLOTS`, causing
  `ArrayIndexOutOfBoundsException`.
- **Test that exposed it:** `VendingMachineTest.testConstructor_initializesEmptyMachine`
  constructs the machine and verifies the four slots.
- **Source-code fault:** The loop's inclusive upper bound attempted to initialize
  one index beyond the last valid array index.
- **Diagnosis:** Compared the loop's upper bound with the allocated array length;
  valid indices end at `NUM_SLOTS - 1`.
- **Correction:** Removed the redundant initialization loop. Java initializes
  array elements to `null`.

## `insertMoney` rejected valid amounts below one dollar

- **Observed failure:** Amounts `0.0`, `0.01`, and `0.99` were rejected even
  though the method contract permits any amount greater than or equal to zero.
- **Test that exposed it:** `VendingMachineTest.testInsertMoney_nonnegativeAmounts_increaseBalance`,
  using the zero and fractional values.
- **Source-code fault:** The guard rejected amounts below `1` instead of amounts
  below `0`.
- **Diagnosis:** Compared the guard to the documented precondition
  (`amount >= 0`) and the planned boundary cases.
- **Correction:** Changed the guard to reject only `amount < 0`.

## Test-sensitivity experiment: `returnChange` does not reset the balance

- **Injected fault:** Temporarily replaced `this.balance = 0` in
  `VendingMachine.returnChange()` with `this.balance = change`, marked with
  `// INJECTED FAULT FOR TEST VALIDATION`.
- **Test that failed:** `VendingMachineTest.testReturnChange_returnsBalanceAndResetsMachine`.
- **Relevant JUnit failure:** `AssertionFailedError: expected: <0.0> but was:
  <4.25>` at the assertion verifying the balance after returning change.
- **Why the test detected it:** The test first deposits `$4.25`, calls
  `returnChange()`, checks the returned amount, and then asserts that the
  machine balance is zero. The injected assignment retained the old balance,
  violating the reset behavior.
- **Resolution:** Removed the injected assignment after publishing the failing
  variant and restored `this.balance = 0`.
