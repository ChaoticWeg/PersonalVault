package cc.chaoticweg.mc.personalvault;

import cc.chaoticweg.mc.personalvault.serialization.PVIO;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class VaultManager {

    private final Logger logger = Bukkit.getLogger();

    private final MetadataManager metadata;
    private final PVIO pvio;

    VaultManager(@NotNull PVIO io, @NotNull MetadataManager metadata) {
        this.pvio = Objects.requireNonNull(io);
        this.metadata = Objects.requireNonNull(metadata);
    }

    /**
     * Get a player's inventory from the {@link ConcurrentHashMap}, or from file if it isn't known.
     *
     * @param player The player whose inventory we want to get
     * @return The player's {@link Inventory}, either from the map or from file
     */
    private Inventory get(@NotNull OfflinePlayer player) {
        this.logger.info("Loading vault for " + player.getName());
        return this.pvio.loadInventory(player);
    }

    /**
     * Save a player's inventory to the map and to file.
     *
     * @param player The player whose inventory we are saving
     * @param src The inventory to save
     */
    private void save(@NotNull OfflinePlayer player, @NotNull Inventory src) {
        Inventory inv = Objects.requireNonNull(src);

        this.logger.info("Saving vault for " + player.getName());
        this.pvio.saveInventory(player, inv);
    }

    /**
     * Loads the player's vault and opens it for them.
     *
     * @param player The player whose vault we will open
     */
    public void open(@NotNull Player player) {
        Inventory vault = this.get(Objects.requireNonNull(player));
        player.openInventory(vault);
        this.metadata.setViewing(player, true);
    }

    /**
     * Saves the player's inventory and closes it.
     *
     * @param player The player whose inventory we will save
     * @param inv The inventory to save
     */
    public void close(@NotNull Player player, @NotNull Inventory inv) {
        this.save(player, inv);
        this.metadata.setViewing(player, false);
    }

    /**
     * Determine whether the player is viewing their inventory using the metadata value.
     *
     * @param player The player we are checking for viewing status
     * @return Whether the player is viewing their inventory
     */
    public boolean isViewing(@NotNull Player player) {
        return this.metadata.isViewing(Objects.requireNonNull(player));
    }

}
