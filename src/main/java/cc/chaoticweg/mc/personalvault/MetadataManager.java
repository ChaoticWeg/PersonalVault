package cc.chaoticweg.mc.personalvault;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * Manages player metadata for the plugin.
 */
class MetadataManager {

    private static final String VIEWING_KEY = "pv.viewing";

    private final PersonalVaultPlugin plugin;
    private final Logger logger;

    MetadataManager(@NotNull PersonalVaultPlugin plugin) {
        this.plugin = Objects.requireNonNull(plugin);
        this.logger = plugin.getLogger();
    }

    /**
     * Set a metadata flag that represents that a player is currently viewing their PV.
     *
     * @param player The player whose metadata we are setting
     */
    void setViewing(@NotNull Player player) {
        this.logger.info("Setting " + player.getName() + " viewing metadata");
        player.setMetadata(VIEWING_KEY, new FixedMetadataValue(this.plugin, true));
    }

    /**
     * Remove the "viewing" metadata flag.
     *
     * @param player The player whose metadata we are setting
     */
    void removeViewing(@NotNull Player player) {
        this.logger.info("Removing " + player.getName() + " viewing metadata");
        player.removeMetadata(VIEWING_KEY, this.plugin);
    }

    /**
     * Retrieves the metadata flag that represents whether a player is viewing their vault currently.
     *
     * @param player The player whose metadata we are checking
     * @return Whether the player is viewing their vault
     */
    boolean isViewing(@NotNull Player player) {
        this.logger.info("Checking viewing metadata for player " + player.getName());
        return player.hasMetadata(VIEWING_KEY);
    }

}
