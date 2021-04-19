package cc.chaoticweg.mc.personalvault.events;

import cc.chaoticweg.mc.personalvault.VaultManager;
import org.bukkit.GameMode;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Listens for an inventory to be closed, determines whether it's a vault, and saves it if need be.
 */
public class InventoryCloseListener implements Listener {

    private final VaultManager vaults;

    public InventoryCloseListener(@NotNull VaultManager vaults) {
        this.vaults = Objects.requireNonNull(vaults);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        HumanEntity human = event.getPlayer();
        if (!(human instanceof Player)) {
            // this isn't a player closing an inventory, so ignore it
            return;
        }

        Player player = (Player) human;
        if (!this.vaults.isViewing(player)) {
            // the player is not viewing their vault, ignore
            return;
        }

        Inventory inv = event.getInventory();
        this.vaults.close(player, inv);
    }

}
