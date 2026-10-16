# Test Plan: Vending Machine

## Scope

Verify that `VendingMachineItem` stores and returns its name and price and
enforces the nonnegative-price requirement, and that `VendingMachine` manages
slots, money, purchases, and returned change as documented.

## Test matrix

| Method / Behavior | Valid Case(s) | Exception / Invalid Case(s) | Boundary Case(s) | Oracle / Expected Result | Related JUnit Test(s) |
|---|---|---|---|---|---|
| `VendingMachineException()` constructors preserve exception message behavior | Construct with a message | N/A | No-argument constructor | Default constructor has a null message; message constructor returns supplied message | `VendingMachineExceptionTest.defaultConstructor_hasNoMessage`, `messageConstructor_preservesMessage` |
| `VendingMachineItem(String, double)` constructs and stores item values | Typical name and positive price | Negative price | Price below, at, and above zero | Valid inputs are retained; negative price throws `VendingMachineException` | `VendingMachineItemTest.createsItemWithTypicalValues`, `acceptsZeroPrice`, `rejectsNegativePrice` |
| `VendingMachine()` initializes machine state | Construct a new machine | N/A | Empty machine | Starting balance is zero and all four slots are empty | `VendingMachineTest.testConstructor_initializesEmptyMachine` |
| `addItem(item, code)` stores an item | Add to an available valid slot | Invalid slot code, including `null`; occupied slot | Empty versus occupied slot | Item is retrievable from its slot; invalid or occupied slot throws `VendingMachineException` without replacing the original item | `testAddItem_validSlot_storesItem`, `testAddItem_occupiedSlot_throwsException`, `testAddItem_invalidSlotCode_throwsException` (parameterized, 7 invalid codes) |
| `getItem(code)` retrieves a slot | Retrieve an item from a populated slot | Invalid slot code | Populated versus empty slot | Returns the item in that slot, or `null` when empty; invalid code throws `VendingMachineException` | `testAddItem_validSlot_storesItem`, `testConstructor_initializesEmptyMachine` |
| `removeItem(code)` removes and returns an item | Remove an item from a populated slot | Remove from empty slot; invalid code | Populated versus empty slot | Returns the removed item and leaves the slot empty; invalid/empty removal throws `VendingMachineException` | `testRemoveItem_populatedSlot_returnsItemAndEmptiesSlot`, `testRemoveItem_emptySlot_throwsException` |
| `insertMoney(amount)` updates balance | Insert a positive amount | Negative amount | Zero, fractions around one dollar, one dollar, and a larger amount | Every amount `>= 0` is accepted and added; negative amount throws `VendingMachineException` without changing balance | `testInsertMoney_nonnegativeAmounts_increaseBalance` (parameterized, 6 amounts), `testInsertMoney_negativeAmount_throwsException` |
| `getBalance()` reports current balance | Read balance after inserting money | N/A | Initial zero and updated balance | Returns current balance without changing it | `testConstructor_initializesEmptyMachine`, `testInsertMoney_nonnegativeAmounts_increaseBalance` |
| `makePurchase(code)` processes a purchase | Exact and greater-than-price balances | Invalid slot code | Insufficient, exact, and greater-than-price balances; empty slot | Sufficient balance removes item and subtracts price; insufficient funds or empty slot returns `false` without changing state; invalid code throws `VendingMachineException` | `testMakePurchase_exactBalance_completesPurchase`, `testMakePurchase_greaterBalance_returnsRemainder`, `testMakePurchase_insufficientBalance_preservesState`, `testMakePurchase_emptySlot_returnsFalse`, `testMakePurchase_invalidSlotCode_throwsException` |
| `returnChange()` returns and resets balance | Return a positive balance | N/A | Zero balance after reset | Returns previous balance and sets current balance to zero | `testReturnChange_returnsBalanceAndResetsMachine` |

## Parameterized test strategy

`testInsertMoney_nonnegativeAmounts_increaseBalance` uses `0.0`, `0.01`,
`0.99`, `1.0`, `1.01`, and `20.0`. The values cover the documented lower
boundary, fractional amounts below the previous one-dollar cutoff, that cutoff,
a value immediately above it, and a typical larger amount. All represent the
valid nonnegative input class and must be added to the balance.

`testAddItem_invalidSlotCode_throwsException` uses `null`, `"E"`, `"a"`, `""`,
`"AA"`, `"1"`, and `" "`. These represent missing, out-of-range, wrong-case,
empty, multi-character, numeric, and whitespace code inputs; each must follow
the invalid-code exception path.

## Test classes

Item construction and accessors are tested in
`src/VendingMachineItemTest.java`; exception behavior is tested in
`src/VendingMachineExceptionTest.java`; machine behavior is tested in
`src/VendingMachineTest.java`.
