package cc.chaoticweg.mc.personalvault.events;

import cc.chaoticweg.mc.personalvault.serialization.PVIO;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PlayerLoginListener implements Listener {

    private final PVIO pvio;

    public PlayerLoginListener(@NotNull PVIO io) {
        this.pvio = Objects.requireNonNull(io);
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        // check that inventory exists for player
        this.pvio.checkInventory(event.getPlayer());
    }

}
