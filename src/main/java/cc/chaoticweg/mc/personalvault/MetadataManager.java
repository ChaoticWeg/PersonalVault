package cc.chaoticweg.mc.personalvault;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.logging.Logger;

class MetadataManager {

    private static final String VIEWING_KEY = "pv.viewing";

    private final PersonalVaultPlugin plugin;
    private final Logger logger;

    MetadataManager(@NotNull PersonalVaultPlugin plugin) {
        this.plugin = Objects.requireNonNull(plugin);
        this.logger = plugin.getLogger();
    }

    void setViewing(@NotNull Player player, boolean viewing) {
        this.logger.info("Setting " + player.getName() + " viewing metadata to " + viewing);
        player.setMetadata(VIEWING_KEY, new FixedMetadataValue(this.plugin, viewing));
    }

    boolean isViewing(@NotNull Player player) {
        return player.hasMetadata(VIEWING_KEY) && player.getMetadata(VIEWING_KEY).get(0).asBoolean();
    }

}
