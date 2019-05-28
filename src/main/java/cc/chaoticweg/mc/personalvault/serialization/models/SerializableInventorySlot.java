package cc.chaoticweg.mc.personalvault.serialization.models;

import org.bukkit.inventory.ItemStack;

/**
 * A serializable inventory slot. Note that this model does not represent any Bukkit class, but rather is a
 * convenience model created to represent a non-empty inventory slot.
 */
class SerializableInventorySlot {

    int slotNumber;
    SerializableItemStack item;

    SerializableInventorySlot(int slotNumber, ItemStack item) {
        this.slotNumber = slotNumber;
        this.item = SerializableItemStack.serialize(item);
    }

}
