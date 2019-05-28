package cc.chaoticweg.mc.personalvault.events;

import cc.chaoticweg.mc.personalvault.VaultManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Listens for a player to quit and saves/disposes their vault
 */
public class PlayerQuitListener implements Listener {

    private final VaultManager vaults;

    public PlayerQuitListener(@NotNull VaultManager vaults) {
        this.vaults = Objects.requireNonNull(vaults);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.vaults.dispose(event.getPlayer());
    }

}
