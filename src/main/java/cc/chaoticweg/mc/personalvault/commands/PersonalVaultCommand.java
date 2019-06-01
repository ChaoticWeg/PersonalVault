package cc.chaoticweg.mc.personalvault.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public abstract class PersonalVaultCommand implements CommandExecutor {

    private HashMap<String, CommandExecutor> subcommands = new HashMap<>();

    private PersonalVaultCommand parent;
    private String name;

    PersonalVaultCommand(@NotNull String name, @Nullable PersonalVaultCommand parent) {
        this.name = Objects.requireNonNull(name);
        this.parent = parent;
    }

    void registerSubcommand(@NotNull PersonalVaultCommand sub) {
        this.registerSubcommand(sub.getShortName(), sub);
    }

    private void registerSubcommand(@NotNull String sub, @NotNull CommandExecutor subcommand) {
        this.subcommands.put(Objects.requireNonNull(sub).toUpperCase(), Objects.requireNonNull(subcommand));
    }

    CommandExecutor getSubcommand(@NotNull String sub) {
        return this.subcommands.get(Objects.requireNonNull(sub.toUpperCase()));
    }

    private boolean hasSubcommand(@NotNull String name) {
        return this.subcommands.containsKey(Objects.requireNonNull(name).toUpperCase());
    }

    @SuppressWarnings("unchecked")
    static <T> T[] popFront(T[] arr) {
        return arr.length < 2
                ? (T[]) Array.newInstance(arr.getClass().getComponentType(), 0)
                : Arrays.copyOfRange(arr, 1, arr.length);
    }

    @Nullable
    private PersonalVaultCommand getParent() {
        return this.parent;
    }

    @NotNull
    private String getShortName() {
        return this.name;
    }

    @NotNull
    public String getLongName() {
        ArrayList<String> stack = new ArrayList<>();

        PersonalVaultCommand thisCmd = this;
        while (thisCmd != null) {
            stack.add(0, thisCmd.getShortName());
            thisCmd = thisCmd.getParent();
        }

        return String.join(" ", stack);
    }

    protected abstract boolean execute(@NotNull CommandSender s, @NotNull Command c, @NotNull String n,
            @NotNull String[] a);

    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command c, @NotNull String n, @NotNull String[] a) {
        if (a.length > 0 && this.hasSubcommand(a[0])) {
            return this.getSubcommand(a[0]).onCommand(s, c, n, popFront(a));
        }

        return this.execute(s, c, n, a);
    }

    boolean errorNoPermissions(@NotNull CommandSender s) {
        s.sendMessage(ChatColor.RED + "You don't have permission to do that." + ChatColor.RESET);
        return true;
    }
}
