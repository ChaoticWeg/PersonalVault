package cc.chaoticweg.mc.personalvault;

import cc.chaoticweg.mc.personalvault.events.PlayerLoginListener;
import cc.chaoticweg.mc.personalvault.serialization.PVIO;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class PersonalVaultPlugin extends JavaPlugin {

    private final Logger logger;
    private final PVIO pvio;

    public PersonalVaultPlugin() {
        super();

        this.logger = this.getLogger();
        this.pvio = new PVIO(this.getDataFolder(), this.getLogger());
    }

    @Override
    public void onEnable() {
        this.logger.info("Enabling PV");
        this.pvio.initialize();

        this.getServer().getPluginManager().registerEvents(new PlayerLoginListener(this.pvio), this);
    }

    @Override
    public void onDisable() {
        this.logger.info("Disabling PV");
    }

}
