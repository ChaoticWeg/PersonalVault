package cc.chaoticweg.mc.personalvault;

import cc.chaoticweg.mc.personalvault.commands.PVGlobalCommand;
import cc.chaoticweg.mc.personalvault.events.InventoryCloseListener;
import cc.chaoticweg.mc.personalvault.events.PlayerLoginListener;
import cc.chaoticweg.mc.personalvault.events.PlayerQuitListener;
import cc.chaoticweg.mc.personalvault.serialization.PVIO;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.logging.Logger;

public class PersonalVaultPlugin extends JavaPlugin {

    private static PersonalVaultPlugin Instance;

    public static PersonalVaultPlugin getInstance() {
        return Instance;
    }

    public static final String MessagePrefix = "[PV]";

    private final Logger logger;
    private final PVIO pvio;

    private final VaultManager vaults;
    private final MetadataManager metadata;

    public PersonalVaultPlugin() {
        super();
        Instance = this;

        this.logger = this.getLogger();
        this.pvio = new PVIO(this);

        this.metadata = new MetadataManager();
        this.vaults = new VaultManager();
    }

    @Override
    public void onLoad() {
        this.pvio.initialize();
    }

    @Override
    public void onEnable() {
        // register events
        this.getServer().getPluginManager().registerEvents(new PlayerLoginListener(this.vaults), this);
        this.getServer().getPluginManager().registerEvents(new InventoryCloseListener(this.vaults), this);
        this.getServer().getPluginManager().registerEvents(new PlayerQuitListener(this.vaults), this);

        // register command
        Objects.requireNonNull(this.getCommand("pv")).setExecutor(new PVGlobalCommand(this));
    }

    @Override
    public void onDisable() {
        this.logger.info("Saving all vaults");
        this.vaults.saveAll();
    }

    @NotNull
    public VaultManager getVaultManager() {
        return this.vaults;
    }

    @NotNull
    public MetadataManager getMetadataManager() {
        return this.metadata;
    }

    @NotNull
    public PVIO getPVIO() {
        return this.pvio;
    }

    public void sendMessage(@NotNull CommandSender player, String message, ChatColor color) {
        player.sendMessage(color + String.format("%s %s", MessagePrefix, message) + ChatColor.RESET);
    }

    public void sendError(@NotNull CommandSender player, String message) {
        this.sendMessage(player, message, ChatColor.RED);
    }

}
