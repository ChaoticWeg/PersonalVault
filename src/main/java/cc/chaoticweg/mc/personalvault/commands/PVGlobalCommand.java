package cc.chaoticweg.mc.personalvault.commands;

import cc.chaoticweg.mc.personalvault.PersonalVaultPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PVGlobalCommand extends PersonalVaultCommand {

    public PVGlobalCommand(@NotNull PersonalVaultPlugin plugin) {
        super("pv", null);

        PersonalVaultPlugin pluginSafe = Objects.requireNonNull(plugin);
        this.registerSubcommand(new VaultAccessCommand(pluginSafe.getVaultManager(), this));
        this.registerSubcommand(new VaultLookupCommand(pluginSafe.getVaultManager(), this));
    }

    @Override
    protected boolean execute(@NotNull CommandSender s, @NotNull Command c, @NotNull String n, @NotNull String[] a) {
        if (a.length == 0) {
            return this.getSubcommand("access").onCommand(s, c, n, popFront(a));
        }

        return false;
    }

}
