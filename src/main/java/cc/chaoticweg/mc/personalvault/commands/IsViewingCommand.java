package cc.chaoticweg.mc.personalvault.commands;

import cc.chaoticweg.mc.personalvault.VaultManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;

public class IsViewingCommand extends PersonalVaultCommand {

    private final VaultManager vaults;

    IsViewingCommand(@NotNull VaultManager vaults, @Nullable PersonalVaultCommand parent) {
        super("isviewing", parent);
        this.vaults = Objects.requireNonNull(vaults);
    }

    @Override
    protected boolean execute(@NotNull CommandSender s, @NotNull Command c, @NotNull String n,
            @NotNull String[] a) {

        if (!(s instanceof ConsoleCommandSender)) {
            s.sendMessage("[PV] Unknown command.");
            return true;
        }

        if (a.length != 1) {
            s.sendMessage("Usage: /" + this.getLongName());
            return true;
        }

        Player player = this.getPlayerByName(a[0]);
        if (player == null) {
            s.sendMessage("No player online by the name of " + a[0]);
            return true;
        }

        boolean isViewing = this.vaults.isViewing(player);
        s.sendMessage(String.format("%s is %s their vault right now", player.getName(),
                isViewing ? "viewing" : "NOT viewing"));
        return true;
    }

    private Player getPlayerByName(String name) {
        Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();
        for (Player p : players) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }

        return null;
    }

}
