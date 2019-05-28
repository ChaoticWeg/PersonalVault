package cc.chaoticweg.mc.personalvault.commands;

import cc.chaoticweg.mc.personalvault.VaultManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class VaultAccessCommand implements CommandExecutor {

    private final VaultManager vaults;

    public VaultAccessCommand(@NotNull VaultManager vaults) {
        this.vaults = Objects.requireNonNull(vaults);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender _sender, @NotNull Command command, @NotNull String alias,
            @NotNull String[] args) {
        CommandSender sender = Objects.requireNonNull(_sender);

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use /" + alias);
            return true;
        }

        if (args.length > 0) {
            return sendUsage(sender, alias, command.getUsage());
        }

        Player player = (Player) sender;
        if (!player.hasPermission("pv.access")) {
            player.sendMessage("You don't have permission to access your vault.");
            return true;
        }

        this.vaults.open(player);
        return true;
    }

    private boolean sendUsage(@NotNull CommandSender sender, @NotNull String alias, @NotNull String usage) {
        sender.sendMessage(usage.replace("<alias>", alias));
        return true;
    }

}
