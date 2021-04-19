package cc.chaoticweg.mc.personalvault;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Manages player metadata for the plugin.
 */
class MetadataManager {

    private static final String VIEWING_KEY = "pv.viewing";
    private static final String VIEWING_TARGET = "pv.viewing.target";

    private final PersonalVaultPlugin plugin;
    private final Logger logger;

    MetadataManager() {
        this(Objects.requireNonNull(PersonalVaultPlugin.getInstance()));
    }

    MetadataManager(@NotNull PersonalVaultPlugin plugin) {
        this.plugin = Objects.requireNonNull(plugin);
        this.logger = plugin.getLogger();
    }

    /**
     * Set a metadata flag that represents whether a player is viewing their vault currently.
     *
     * @param player The player whose metadata we are setting
     * @param target Whose inventory the player is now viewing
     */
    void setViewing(@NotNull Player player, @NotNull OfflinePlayer target) {
        String targetUuidStr = target.getUniqueId().toString();
        this.logger.fine("Setting " + player.getName() + " viewing metadata to " + targetUuidStr);
        player.setMetadata(VIEWING_KEY, new FixedMetadataValue(this.plugin, true));
        player.setMetadata(VIEWING_TARGET, new FixedMetadataValue(this.plugin, targetUuidStr));
    }

    /**
     * Clear metadata flags for player, i.e. has closed the inventory they were viewing
     *
     * @param player The player who has stopped viewing an inventory
     */
    void setNotViewing(@NotNull Player player) {
        this.logger.fine("Setting " + player.getName() + " viewing metadata to NULL");
        player.setMetadata(VIEWING_KEY, new FixedMetadataValue(this.plugin, false));
        player.removeMetadata(VIEWING_TARGET, this.plugin);
    }

    /**
     * Retrieves the metadata flag that represents whether a player is viewing their vault currently.
     *
     * @param player The player whose metadata we are checking
     * @return Whether the player is viewing their vault
     */
    boolean isViewing(@NotNull Player player) {
        return player.hasMetadata(VIEWING_KEY) && player.getMetadata(VIEWING_KEY).get(0).asBoolean();
    }

    /**
     * Get the UUID of the player whose inventory the player is viewing
     *
     * @param player Inventory viewer
     * @return UUID of target, or null if none
     */
    UUID getViewingTarget(@NotNull Player player) {
        if (!this.isViewing(player) || !player.hasMetadata(VIEWING_TARGET)) {
            return null;
        }

        String targetUuidStr = player.getMetadata(VIEWING_TARGET).get(0).asString();
        return UUID.fromString(targetUuidStr);
    }

}
