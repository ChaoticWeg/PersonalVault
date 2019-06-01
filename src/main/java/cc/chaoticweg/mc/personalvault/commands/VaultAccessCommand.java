package cc.chaoticweg.mc.personalvault.commands;

import cc.chaoticweg.mc.personalvault.VaultManager;
import org.bukkit.command.Command;
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
    public boolean execute(@NotNull PVCommandUser sender, @NotNull Command command, @NotNull String alias,
            @NotNull String[] args) {

        if (!sender.isPlayer()) {
            return this.errorNoConsole(sender);
        }

        if (args.length > 0) {
            sender.sendMessage("Usage: /pv");
            return true;
        }

        Player player = sender.getAsPlayer();
        if (!player.hasPermission("pv.access.self")) {
            return this.errorNoPermissions(sender);
        }

        this.vaults.open(player);
        return true;
    }

}
