# JoshHack Inventory Coverage

Coverage was measured with JaCoCo 0.8.13 and the bundled JUnit Platform Console
1.14.4. Baseline is the original `InventoryTest` suite of six empty test
methods; final is the seven behavior-focused tests in `test/game/InventoryTest.java`.

| Metric for `game.Inventory` | Baseline | Final |
| --- | ---: | ---: |
| Instructions (statement-level proxy) | 0/108 (0%) | 108/108 (100%) |
| Lines | 0/24 (0%) | 24/24 (100%) |
| Branches | 0/18 (0%) | 18/18 (100%) |

## Tests added

- Verify insertion order, first-available-slot behavior, and ignored additions
  when full.
- Verify `contains` matches the stored item and rejects a distinct item.
- Verify indexed access, including an unused slot, and the item-array accessor.
- Verify `isFull` for partially filled and full inventories.
- Verify removal only removes the matching object and leaves other entries
  intact.
- Verify zero-capacity inventory behavior, including add, remove, contains,
  and `isFull`.

## Remaining uncovered behavior

None in `Inventory.java`: JaCoCo reports all 24 executable lines and all 18
branches covered. The instruction count is used as a statement-level proxy;
JaCoCo does not report source-language statement counts directly.
