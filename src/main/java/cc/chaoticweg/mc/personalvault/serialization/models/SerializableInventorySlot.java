package cc.chaoticweg.mc.personalvault.serialization.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * A serializable inventory slot. Note that this model does not represent any Bukkit class, but rather is a
 * convenience model created to represent a non-empty inventory slot.
 */
class SerializableInventorySlot {

    @SerializedName("slotNumber")
    @Expose
    Integer slotNumber;

    @SerializedName("slot")
    @Expose
    Map<String, Object> item;

    SerializableInventorySlot(int slotNumber, ItemStack item) {
        this.slotNumber = slotNumber;
        this.item = item.serialize();
    }

}
