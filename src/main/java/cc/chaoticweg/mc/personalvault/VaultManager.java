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

    public void check(@NotNull OfflinePlayer player) {
        UUID uuid = Objects.requireNonNull(player).getUniqueId();
        this.logger.fine("Checking vault for " + player.getName());

        if (!this.vaults.containsKey(uuid)) {
            Inventory vault = this.pvio.loadInventory(player);
            this.vaults.put(uuid, vault);
        }
    }

    private Inventory load(@NotNull OfflinePlayer player) {
        this.logger.fine("Loading vault for " + player.getName());
        this.check(player);
        return this.vaults.get(Objects.requireNonNull(player).getUniqueId());
    }

    private void save(@NotNull OfflinePlayer player, @NotNull Inventory src) {
        UUID uuid = Objects.requireNonNull(player).getUniqueId();
        Inventory inv = Objects.requireNonNull(src);
        this.logger.fine("Saving vault for " + player.getName());

        this.vaults.put(uuid, inv);
        this.pvio.saveInventory(player, inv);
    }

    public void open(@NotNull Player player) {
        Inventory vault = this.load(Objects.requireNonNull(player));
        player.openInventory(vault);
        this.metadata.setViewing(player, true);
    }

    public void close(@NotNull Player player, @NotNull Inventory inv) {
        this.save(player, inv);
        this.metadata.setViewing(player, false);
    }

    public boolean isViewing(@NotNull Player player) {
        return this.metadata.isViewing(Objects.requireNonNull(player));
    }

    void saveAll() {
        Set<Map.Entry<UUID, Inventory>> vaults = this.vaults.entrySet();
        synchronized (this.vaults) {
            for (Map.Entry<UUID, Inventory> vault : vaults) {
                this.pvio.saveInventory(vault.getKey(), vault.getValue());
            }
        }
    }

}
