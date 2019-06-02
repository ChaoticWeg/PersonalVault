package cc.chaoticweg.mc.personalvault.commands;

import cc.chaoticweg.mc.personalvault.VaultManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class VaultAccessCommand extends PersonalVaultCommand {

    private final VaultManager vaults;

    VaultAccessCommand(@NotNull VaultManager vaults, @NotNull PVGlobalCommand parent) {
        super("access", parent);
        this.vaults = Objects.requireNonNull(vaults);
    }

    @Override
    public boolean execute(@NotNull CommandSender _sender, @NotNull Command command, @NotNull String alias,
            @NotNull String[] args) {
        CommandSender sender = Objects.requireNonNull(_sender);

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can access vaults");
            return true;
        }

        if (args.length > 0) {
            sender.sendMessage("Usage: /pv access");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("pv.access")) {
            player.sendMessage("You don't have permission to access your vault.");
            return true;
        }

        this.vaults.open(player);
        return true;
    }

}
