package cc.chaoticweg.mc.personalvault.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PVCommandUser {

    private final CommandSender sender;

    PVCommandUser(@NotNull CommandSender sender) {
        this.sender = Objects.requireNonNull(sender);
    }

    void sendError(@NotNull String error) {
        this.sender.sendMessage(
                String.format("%s%s%s", ChatColor.RED, Objects.requireNonNull(error), ChatColor.RESET)
        );
    }

    void sendMessage(@NotNull String message) {
        this.sender.sendMessage(message);
    }

    boolean isPlayer() {
        return this.sender instanceof Player;
    }

    public boolean isConsole() {
        return this.sender instanceof ConsoleCommandSender;
    }

    CommandSender getCommandSender() {
        return this.sender;
    }

    Player getAsPlayer() {
        return (Player) this.sender;
    }

    public ConsoleCommandSender getAsConsole() {
        return (ConsoleCommandSender) this.sender;
    }

}
