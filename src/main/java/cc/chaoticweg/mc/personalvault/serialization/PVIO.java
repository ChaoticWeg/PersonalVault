package cc.chaoticweg.mc.personalvault.serialization;

import cc.chaoticweg.mc.personalvault.serialization.models.SerializableInventory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

public class PVIO {

    private File inventoriesFolder;

    private Logger logger;
    private Gson gson;

    public PVIO(@NotNull File pluginDataFolder, @NotNull Logger pluginLogger) {
        File dataFolder = Objects.requireNonNull(pluginDataFolder);
        this.inventoriesFolder = new File(dataFolder, "inventories");

        this.logger = Objects.requireNonNull(pluginLogger);
        this.gson = new GsonBuilder().create();
    }

    public void initialize() {
        logger.info("Initializing PVIO");

        if (this.inventoriesFolder.mkdirs()) {
            logger.warning("Created new inventories folder at " + this.inventoriesFolder.getAbsolutePath());
        }
    }

    private Inventory initializeInventory(@NotNull OfflinePlayer player) {
        logger.info("Initializing inventory for " + player.getName());

        OfflinePlayer owner = Objects.requireNonNull(player);
        Inventory result = InventoryAdapter.createInventory();
        saveInventory(result, owner);
        return result;
    }

    public void checkInventory(@NotNull OfflinePlayer player) {
        logger.info("Checking that inventory exists for " + player.getName());

        OfflinePlayer owner = Objects.requireNonNull(player);
        File inventoryFile = this.getInventoryFile(owner);

        if (!inventoryFile.exists()) {
            this.initializeInventory(owner);
        }
    }

    private void saveInventory(@NotNull Inventory srcInv, @NotNull OfflinePlayer player) {
        logger.info("Saving inventory for " + player.getName());

        Inventory src = Objects.requireNonNull(srcInv);
        OfflinePlayer owner = Objects.requireNonNull(player);

        File dataFile = this.getInventoryFile(owner);

        SerializableInventory inventory = new SerializableInventory(src);

        // try to write to file
        try (FileWriter out = new FileWriter(dataFile)) {
            gson.toJson(inventory, out);
        }
        catch (IOException ex) {
            logger.severe("Unable to save inventory for " + owner.getName() + ": " + ex.getClass().getSimpleName());
            ex.printStackTrace();
        }
    }

    public Inventory loadInventory(@NotNull OfflinePlayer player) {
        OfflinePlayer owner = Objects.requireNonNull(player);
        this.checkInventory(owner);

        logger.info("Loading inventory for " + owner.getName());

        File dataFile = this.getInventoryFile(owner);
        try (FileReader in = new FileReader(dataFile)) {
            SerializableInventory src = gson.fromJson(in, SerializableInventory.class);
            return src.deserialize();
        }

        // we shouldn't catch any NPEs because they are handled down the chain
        // who knows though, my code is shit
        catch (NullPointerException e) {
            logger.warning("Inventory for " + owner.getName() + " is corrupt, creating a new one");
            return this.initializeInventory(owner);
        }

        // caught a different error. hm.
        catch (Exception e) {
            logger.severe("Unable to load inventory for " + owner.getName() + ": " + e.getClass().getSimpleName());
            e.printStackTrace();
            return null;
        }
    }

    @NotNull
    private File getInventoryFile(@NotNull OfflinePlayer player) {
        OfflinePlayer owner = Objects.requireNonNull(player);
        String filename = owner.getUniqueId().toString() + ".json";
        return new File(this.inventoriesFolder, filename);
    }

}
