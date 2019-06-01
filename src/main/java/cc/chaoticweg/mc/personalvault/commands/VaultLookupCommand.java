package cc.chaoticweg.mc.personalvault.commands;

import cc.chaoticweg.mc.personalvault.VaultManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Look up a player's vault file by their name.
 *
 * /pv whois [name]
 */
public class VaultLookupCommand extends PersonalVaultCommand {

    private final VaultManager vaults;

    VaultLookupCommand(@NotNull VaultManager vaults, @NotNull PVGlobalCommand parent) {
        super("whois", parent);
        this.vaults = Objects.requireNonNull(vaults);
    }

    @Override
    protected boolean execute(@NotNull PVCommandUser s, @NotNull Command c, @NotNull String n, @NotNull String[] a) {
        if (a.length > 1) {
            return false;
        }

        if (s.isPlayer() && !s.getCommandSender().hasPermission("pv.lookup")) {
            return this.errorNoPermissions(s);
        }

        String playerName;

        if (a.length < 1) {
            if (!s.isPlayer()) {
                // console can't have a vault, lookup self makes no sense
                s.sendMessage("Usage: /pv whois [name] - Lookup vault file by player name");
                return true;
            }

            playerName = s.getAsPlayer().getName();
        }

        else {
            playerName = a[0];
        }

        OfflinePlayer player = this.lookupPlayerByName(playerName);
        if (player == null) {
            s.sendError("Player not found: " + playerName);
            return true;
        }

        String filename = this.vaults.lookupFilename(player);
        s.sendMessage(String.format("%s: %s", player.getName(), filename));
        return true;
    }

    @Nullable
    private OfflinePlayer lookupPlayerByName(String name) {
        // misnomer: Server#getOfflinePlayers() returns online players as well
        OfflinePlayer[] players = Bukkit.getServer().getOfflinePlayers();

        for (OfflinePlayer player : players) {
            if (Objects.requireNonNull(player.getName()).equalsIgnoreCase(name)) {
                return player;
            }
        }

        return null;
    }

}
