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
import java.util.UUID;
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
        logger.fine("Initializing PVIO");

        if (this.inventoriesFolder.mkdirs()) {
            logger.warning("Created new inventories folder at " + this.inventoriesFolder.getAbsolutePath());
        }
    }

    private Inventory initializeInventory(@NotNull OfflinePlayer player) {
        OfflinePlayer owner = Objects.requireNonNull(player);

        logger.info("Initializing inventory for " + owner.getName());

        Inventory result = InventoryAdapter.createInventory();
        saveInventory(owner, result);
        return result;
    }

    private void checkInventory(@NotNull OfflinePlayer player) {
        OfflinePlayer owner = Objects.requireNonNull(player);
        File inventoryFile = this.getInventoryFile(owner.getUniqueId());

        logger.fine("Checking that inventory exists for " + owner.getName());

        if (!inventoryFile.exists()) {
            this.initializeInventory(owner);
        }
    }

    public void saveInventory(@NotNull UUID nnUuid, @NotNull Inventory src) {
        Inventory inv = Objects.requireNonNull(src);
        UUID uuid = Objects.requireNonNull(nnUuid);

        File dataFile = this.getInventoryFile(uuid);

        SerializableInventory inventory = new SerializableInventory(inv);

        // try to write to file
        try (FileWriter out = new FileWriter(dataFile)) {
            gson.toJson(inventory, out);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void saveInventory(@NotNull OfflinePlayer player, @NotNull Inventory inv) {
        OfflinePlayer owner = Objects.requireNonNull(player);
        logger.fine("Saving inventory for " + owner.getName());
        this.saveInventory(owner.getUniqueId(), Objects.requireNonNull(inv));
    }

    public Inventory loadInventory(@NotNull OfflinePlayer player) {
        OfflinePlayer owner = Objects.requireNonNull(player);
        this.checkInventory(owner);

        logger.fine("Loading inventory for " + owner.getName());

        File dataFile = this.getInventoryFile(owner.getUniqueId());
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
    private File getInventoryFile(@NotNull UUID uuid) {
        String filename = Objects.requireNonNull(uuid).toString() + ".json";
        return new File(this.inventoriesFolder, filename);
    }

}
