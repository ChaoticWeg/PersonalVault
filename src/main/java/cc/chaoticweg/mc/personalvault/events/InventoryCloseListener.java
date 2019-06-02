package cc.chaoticweg.mc.personalvault.events;

import cc.chaoticweg.mc.personalvault.VaultManager;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * Listens for an inventory to be closed, determines whether it's a vault, and saves it if need be.
 */
public class InventoryCloseListener implements Listener {

    private final VaultManager vaults;
    private final Logger logger;

    public InventoryCloseListener(@NotNull VaultManager vaults, @Nullable Logger logger) {
        this.vaults = Objects.requireNonNull(vaults);
        this.logger = logger != null ? logger : Logger.getLogger("PV");
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        HumanEntity human = event.getPlayer();
        if (!(human instanceof Player)) {
            // this isn't a player closing an inventory, so ignore it
            return;
        }

        Player player = (Player) human;
        this.logger.info(String.format("%s is closing an inventory", player.getName()));

        if (!this.vaults.isViewing(player)) {
            this.logger.info(String.format("%s is not viewing an inventory (per metadata?), ignoring event",
                    player.getName()));
            return;
        }

        Inventory inv = event.getInventory();
        this.vaults.close(player, inv);
    }

}
