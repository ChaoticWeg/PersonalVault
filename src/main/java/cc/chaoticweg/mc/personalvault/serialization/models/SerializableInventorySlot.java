package cc.chaoticweg.mc.personalvault.serialization.models;

import org.bukkit.inventory.ItemStack;

class SerializableInventorySlot {

    int slotNumber;
    SerializableItemStack item;

    SerializableInventorySlot(int slotNumber, ItemStack item) {
        this.slotNumber = slotNumber;
        this.item = SerializableItemStack.serialize(item);
    }

}
