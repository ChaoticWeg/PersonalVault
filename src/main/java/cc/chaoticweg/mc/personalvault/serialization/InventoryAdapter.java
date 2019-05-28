package cc.chaoticweg.mc.personalvault.serialization;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

/**
 * Utility class for creating new {@link Inventory} instances.
 */
public final class InventoryAdapter {

    private static final int SLOTS_PER_ROW = 9; // CANNOT BE CHANGED OR BUKKIT WILL DIE
    private static final int NUM_ROWS = 6; // this can be changed, though probably don't do that
    private static final int DEFAULT_NUM_SLOTS = SLOTS_PER_ROW * NUM_ROWS;

    /**
     * Create a new {@link Inventory} with the given size.
     *
     * @param size The size of this {@link Inventory}
     * @return A new, empty {@link Inventory} with the specified size
     */
    public static Inventory createInventory(int size) {
        return Bukkit.getServer().createInventory(null, size);
    }

    /**
     * Create a new {@link Inventory} with the default size.
     *
     * @return A new, empty {@link Inventory} with the default size
     */
    static Inventory createInventory() {
        return createInventory(DEFAULT_NUM_SLOTS);
    }

}
