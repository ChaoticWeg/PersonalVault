package cc.chaoticweg.mc.personalvault.commands;

import cc.chaoticweg.mc.personalvault.PersonalVaultPlugin;
import cc.chaoticweg.mc.personalvault.VaultManager;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class VaultAccessCommand extends PersonalVaultCommand {

    private final VaultManager vaults;

    VaultAccessCommand(@NotNull PersonalVaultPlugin plugin, @NotNull VaultManager vaults, @NotNull PVGlobalCommand parent) {
        super(plugin, "access", parent);
        this.vaults = Objects.requireNonNull(vaults);
    }

    @Override
    public boolean execute(@NotNull CommandSender _sender, @NotNull Command command, @NotNull String alias,
                           @NotNull String[] args) {
        CommandSender sender = Objects.requireNonNull(_sender);

        // Check that the command came from a player
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use /" + alias);
            return true;
        }

        // Check that we don't have too many args - if so, send usage string to sender and bail out
        if (args.length > 1) {
            return this.sendUsage(sender, alias, command.getUsage());
        }

        // Check that the sender has permission to access vaults
        Player player = (Player) sender;
        if (!player.hasPermission("pv.access")) {
            player.sendMessage(ChatColor.RED + "[PV] You don't have permission to access any vaults." + ChatColor.RESET);
            return true;
        }

        // Check that the player is not in creative mode, which seems to break something.
        // TODO look into why creative mode seems to break PV
        if (player.getGameMode() == GameMode.CREATIVE) {
            player.sendMessage(ChatColor.RED + "[PV] /pv is not available in creative mode." + ChatColor.RESET);
            return true;
        }

        // If no args, attempt to open the player's own vault
        if (args.length == 0) {
            this.vaults.open(player);
            return true;
        }

        // If player name arg given, attempt to look up the OfflinePlayer by name and open their vault
        // Check that the sender has permission to access other players' vaults
        if (!player.hasPermission("pv.admin")) {
            player.sendMessage("You don't have permission to access other players' vaults.");
            return true;
        }

        // Look up target player by name
        // FIXME looking up player directly by name is deprecated; prefer to hash name against UUID and look up against that
        String targetPlayerName = args[0];
        OfflinePlayer target = this.getPlugin().getServer().getOfflinePlayer(targetPlayerName);
        this.vaults.open(player, target);

        return true;
    }

    private boolean sendUsage(@NotNull CommandSender sender, @NotNull String alias, @NotNull String usage) {
        sender.sendMessage(usage.replace("<alias>", alias));
        return true;
    }

}
