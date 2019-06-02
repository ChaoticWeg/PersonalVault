package cc.chaoticweg.mc.personalvault;

import cc.chaoticweg.mc.personalvault.commands.PVGlobalCommand;
import cc.chaoticweg.mc.personalvault.events.InventoryCloseListener;
import cc.chaoticweg.mc.personalvault.events.PlayerLoginListener;
import cc.chaoticweg.mc.personalvault.events.PlayerQuitListener;
import cc.chaoticweg.mc.personalvault.serialization.PVIO;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.logging.Logger;

public class PersonalVaultPlugin extends JavaPlugin {

    private final Logger logger;
    private final PVIO pvio;

    private final VaultManager vaults;

    public PersonalVaultPlugin() {
        super();

        this.logger = this.getLogger();
        this.pvio = new PVIO(this.getDataFolder(), this.getLogger());
        this.vaults = new VaultManager(this.pvio, new MetadataManager(this));
    }

    @Override
    public void onLoad() {
        this.pvio.initialize();
    }

    @Override
    public void onEnable() {
        // register events
        this.getServer().getPluginManager().registerEvents(new PlayerLoginListener(this.vaults), this);
        this.getServer().getPluginManager().registerEvents(new InventoryCloseListener(this.vaults, this.logger), this);
        this.getServer().getPluginManager().registerEvents(new PlayerQuitListener(this.vaults), this);

        // register command
        Objects.requireNonNull(this.getCommand("pv")).setExecutor(new PVGlobalCommand(this));
    }

    @NotNull
    public VaultManager getVaultManager() {
        return this.vaults;
    }

}
