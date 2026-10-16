# Coverage and Test Sensitivity

## Coverage results

The first injected fault changed the constructor loop condition from
`i < NUM_SLOTS` to `i <= NUM_SLOTS`. The array has valid indices from `0` to
`NUM_SLOTS - 1`, so construction failed on the final iteration with an
`ArrayIndexOutOfBoundsException`. In the initial full-suite run, 26 tests
failed during machine construction and the 5 item/exception tests passed.
Coverage could not meaningfully exercise the vending-machine operations with
that fault in place.

The second injected fault changed `insertMoney` to throw for positive amounts
(`amount > 0`). In the `VendingMachineTest` run with this fault, 11 tests
failed and 17 passed. The failure report showed the expected positive-money
operations throwing `VendingMachineException`, and the negative-amount test
also detected that the expected exception was no longer thrown.

After both faults were fixed, the complete suite passed: **33/33 tests**.
Current production-code coverage:

| Class | Statements | Branches | Declarations |
|---|---:|---:|---:|
| `VendingMachine` | 45/45 (100%) | 22/22 (100%) | 9/9 (100%) |
| `VendingMachineItem` | 8/8 (100%) | 2/2 (100%) | 3/3 (100%) |
| `VendingMachineException` | 4/4 (100%) | N/A | 2/2 (100%) |

Coverage figures above are from the focused JUnit coverage run after the
injected faults were fixed.

## Tests added to improve coverage

- Added invalid-slot tests for `getItem` and `removeItem`, complementing
  `addItem`'s invalid-code cases (including a null code).
- Exercised zero and positive nonnegative amounts in `insertMoney`, as well as
  a negative amount, to cover its valid and invalid behavior.
- Existing purchase tests cover exact funds, excess funds, insufficient funds,
  an empty slot, and an invalid code.

## Condition-coverage analysis

In `VendingMachine.makePurchase`, the compound decision is:

```java
(item != null) && (this.balance >= item.getPrice())
```

Its individual conditions have both outcomes in the tests:

| Condition | True | False |
|---|---|---|
| `item != null` | `testMakePurchase_exactBalance_completesPurchase`, `testMakePurchase_greaterBalance_returnsRemainder`, `testMakePurchase_insufficientBalance_preservesState` | `testMakePurchase_emptySlot_returnsFalse` |
| `this.balance >= item.getPrice()` | Exact-balance and greater-balance tests | `testMakePurchase_insufficientBalance_preservesState` |

The second condition is short-circuited when `item != null` is false, so the
empty-slot case evaluates only the first condition.

## Test-sensitivity experiment

The positive-amount fault in `insertMoney` was detected by the tests. Calls to
`insertMoney` with positive values unexpectedly threw, causing tests that
expect successful deposits and purchases to fail. The negative-amount test
also failed because the injected implementation did not throw for a negative
value. The run recorded **11 failures and 17 passes** in `VendingMachineTest`,
confirming the suite is sensitive to that fault. With the fault removed, the
complete suite passes.
