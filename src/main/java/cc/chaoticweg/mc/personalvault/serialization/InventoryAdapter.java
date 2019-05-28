package cc.chaoticweg.mc.personalvault.serialization;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class InventoryAdapter {

    private static final int SLOTS_PER_ROW = 9; // CANNOT BE CHANGED OR BUKKIT WILL DIE
    private static final int NUM_ROWS = 6; // this can be changed, though probably don't do that
    private static final int DEFAULT_NUM_SLOTS = SLOTS_PER_ROW * NUM_ROWS;

    public static Inventory createInventory(int size) {
        return Bukkit.getServer().createInventory(null, size);
    }
    static Inventory createInventory() {
        return createInventory(DEFAULT_NUM_SLOTS);
    }

}
