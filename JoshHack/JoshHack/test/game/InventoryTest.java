package game;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;

import org.junit.jupiter.api.Test;

class InventoryTest {
    @Test
    void addUsesFirstAvailableSlotAndIgnoresItemsWhenFull() {
        Inventory inventory = new Inventory(2);
        Item first = item("first");
        Item second = item("second");

        inventory.add(first);
        inventory.add(second);
        inventory.add(item("overflow"));

        assertSame(first, inventory.get(0));
        assertSame(second, inventory.get(1));
        assertTrue(inventory.isFull());
    }

    @Test
    void containsMatchesTheStoredItemAndRejectsOtherItems() {
        Inventory inventory = new Inventory(2);
        Item stored = item("sword");
        inventory.add(stored);

        assertTrue(inventory.contains(stored));
        assertFalse(inventory.contains(item("sword")));
    }

    @Test
    void getReturnsTheItemAtTheRequestedIndex() {
        Inventory inventory = new Inventory(2);
        Item stored = item("potion");
        inventory.add(stored);

        assertSame(stored, inventory.get(0));
        assertNull(inventory.get(1));
    }

    @Test
    void getItemsReturnsTheInventorySlots() {
        Inventory inventory = new Inventory(2);
        Item stored = item("shield");
        inventory.add(stored);

        Item[] items = inventory.getItems();
        assertEquals(2, items.length);
        assertSame(items, inventory.getItems());
        assertArrayEquals(new Item[] { stored, null }, items);
    }

    @Test
    void isFullDistinguishesPartiallyFilledAndFullInventories() {
        Inventory inventory = new Inventory(2);
        inventory.add(item("apple"));
        assertFalse(inventory.isFull());

        inventory.add(item("bread"));
        assertTrue(inventory.isFull());
    }

    @Test
    void removeDeletesOnlyTheMatchingItemAndLeavesOtherItemsIntact() {
        Inventory inventory = new Inventory(2);
        Item stored = item("axe");
        Item remaining = item("bow");
        inventory.add(stored);
        inventory.add(remaining);

        inventory.remove(item("axe"));
        assertSame(stored, inventory.get(0));
        inventory.remove(stored);

        assertNull(inventory.get(0));
        assertSame(remaining, inventory.get(1));
        assertFalse(inventory.contains(stored));
    }

    @Test
    void zeroCapacityInventoryHasNoSlotsAndIsFull() {
        Inventory inventory = new Inventory(0);

        inventory.add(item("unused"));
        inventory.remove(item("absent"));

        assertEquals(0, inventory.getItems().length);
        assertTrue(inventory.isFull());
        assertFalse(inventory.contains(item("absent")));
    }

    private Item item(String name) {
        return new Item(name.charAt(0), Color.WHITE, name, name);
    }
}
