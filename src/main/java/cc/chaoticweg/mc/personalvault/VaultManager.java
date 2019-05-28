package cc.chaoticweg.mc.personalvault;

import cc.chaoticweg.mc.personalvault.serialization.PVIO;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class VaultManager {

    private final ConcurrentHashMap<UUID, Inventory> vaults = new ConcurrentHashMap<>();
    private final Logger logger = Bukkit.getLogger();

    private final MetadataManager metadata;
    private final PVIO pvio;

    VaultManager(@NotNull PVIO io, @NotNull MetadataManager metadata) {
        this.pvio = Objects.requireNonNull(io);
        this.metadata = Objects.requireNonNull(metadata);
    }

    /**
     * Check whether we know about a player's inventory and get it if not.
     *
     * @param player The player whose inventory we are checking for
     */
    public void check(@NotNull OfflinePlayer player) {
        UUID uuid = Objects.requireNonNull(player).getUniqueId();
        this.logger.fine("Checking vault for " + player.getName());

        if (!this.vaults.containsKey(uuid)) {
            Inventory vault = this.pvio.loadInventory(player);
            this.vaults.put(uuid, vault);
        }
    }

    /**
     * Get a player's inventory from the {@link ConcurrentHashMap}, or from file if it isn't known.
     *
     * @param player The player whose inventory we want to get
     * @return The player's {@link Inventory}, either from the map or from file
     */
    private Inventory get(@NotNull OfflinePlayer player) {
        this.logger.fine("Loading vault for " + player.getName());
        this.check(player);
        return this.vaults.get(Objects.requireNonNull(player).getUniqueId());
    }

    /**
     * Save a player's inventory to the map and to file.
     *
     * @param player The player whose inventory we are saving
     * @param src The inventory to save
     */
    private void save(@NotNull OfflinePlayer player, @NotNull Inventory src) {
        UUID uuid = Objects.requireNonNull(player).getUniqueId();
        Inventory inv = Objects.requireNonNull(src);
        this.logger.fine("Saving vault for " + player.getName());

        this.vaults.put(uuid, inv);
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
     * Save a player's inventory and dispose of it, i.e. when a player logs off.
     *
     * @param player The player whose inventory we will dispose
     */
    public void dispose(@NotNull Player player) {
        UUID uuid = Objects.requireNonNull(player).getUniqueId();

        if (this.vaults.containsKey(uuid)) {
            Inventory vault = this.vaults.get(uuid);
            this.pvio.saveInventory(player, vault);
            this.vaults.remove(uuid);
        }
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

    /**
     * Save all currently-tracked vaults.
     */
    void saveAll() {
        Set<Map.Entry<UUID, Inventory>> vaults = this.vaults.entrySet();
        synchronized (this.vaults) {
            for (Map.Entry<UUID, Inventory> vault : vaults) {
                this.pvio.saveInventory(vault.getKey(), vault.getValue());
            }
        }
    }

}
