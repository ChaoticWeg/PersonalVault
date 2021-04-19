package cc.chaoticweg.mc.personalvault.util;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public final class InventoryUtils {

    private InventoryUtils() {
        // Disallowed
    }

    public static Inventory createInventory() {
        return Bukkit.getServer().createInventory(null, InventoryType.CHEST);
    }

}
