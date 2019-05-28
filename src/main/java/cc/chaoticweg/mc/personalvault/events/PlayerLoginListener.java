package cc.chaoticweg.mc.personalvault.events;

import cc.chaoticweg.mc.personalvault.VaultManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PlayerLoginListener implements Listener {

    private final VaultManager vaults;

    public PlayerLoginListener(@NotNull VaultManager vaults) {
        this.vaults = Objects.requireNonNull(vaults);
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        // check that inventory exists for player
        this.vaults.check(event.getPlayer());
    }

}
