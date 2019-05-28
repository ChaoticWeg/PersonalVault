package cc.chaoticweg.mc.personalvault;

import cc.chaoticweg.mc.personalvault.commands.VaultAccessCommand;
import cc.chaoticweg.mc.personalvault.events.InventoryCloseListener;
import cc.chaoticweg.mc.personalvault.events.PlayerLoginListener;
import cc.chaoticweg.mc.personalvault.serialization.PVIO;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Logger;

public class PersonalVaultPlugin extends JavaPlugin {

    private final Logger logger;
    private final PVIO pvio;

    @SuppressWarnings("FieldCanBeLocal")
    private final MetadataManager metadata;
    private final VaultManager vaults;

    public PersonalVaultPlugin() {
        super();

        this.logger = this.getLogger();
        this.pvio = new PVIO(this.getDataFolder(), this.getLogger());

        this.metadata = new MetadataManager(this);
        this.vaults = new VaultManager(this.pvio, this.metadata);
    }

    @Override
    public void onLoad() {
        this.pvio.initialize();
    }

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new PlayerLoginListener(this.vaults), this);
        this.getServer().getPluginManager().registerEvents(new InventoryCloseListener(this.vaults), this);

        Objects.requireNonNull(this.getCommand("pv")).setExecutor(new VaultAccessCommand(this.vaults));
    }

    @Override
    public void onDisable() {
        this.logger.info("Saving all vaults");
        this.vaults.saveAll();
    }

}
