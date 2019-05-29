package cc.chaoticweg.mc.personalvault.serialization.models;

import cc.chaoticweg.mc.personalvault.serialization.InventoryAdapter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link SerializableModel} to represent an {@link Inventory}.
 */
public class SerializableInventory implements SerializableModel<Inventory> {

    private int size;
    private List<SerializableInventorySlot> slots;

    public SerializableInventory(Inventory inv) {
        this.size = inv.getSize();
        this.slots = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            ItemStack item = inv.getItem(i);
            if (item != null) {
                this.slots.add(new SerializableInventorySlot(i, item));
            }
        }
    }

    @NotNull
    @Override
    public Inventory deserialize() {
        Inventory inv = InventoryAdapter.createInventory(this.size);
        for (SerializableInventorySlot slot : this.slots) {
            inv.setItem(slot.slotNumber, ItemStack.deserialize(slot.item));
        }
        return inv;
    }
}
