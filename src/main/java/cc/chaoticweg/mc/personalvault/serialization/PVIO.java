package cc.chaoticweg.mc.personalvault.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.logging.Logger;

public class PVIO {

    private File inventoriesFolder;

    private Logger logger;
    private Gson gson;

    public PVIO(File dataFolder, Logger logger) {
        this.inventoriesFolder = new File(dataFolder, "inventories");
        this.logger = logger;

        this.gson = new GsonBuilder()
                .registerTypeAdapter(Inventory.class, new InventoryHandler())
                .create();
    }

    public void initialize() {
        logger.info("Initializing PVIO");

        if (this.inventoriesFolder.mkdirs()) {
            logger.warning("Created new inventories folder at " + this.inventoriesFolder.getAbsolutePath());
        }
    }

    public void saveInventory(Inventory inventory, OfflinePlayer owner) {
        String json = gson.toJson(inventory);
    }

    public Inventory loadInventory(OfflinePlayer owner) {
        String filename = owner.getUniqueId().toString() + ".json";
        File dataFile = new File(this.inventoriesFolder, filename);

        try {
            // if datafile does not exist, try to create a new inventory and save it
            if (!dataFile.exists()) {

                // attempt to create a new inventory JSON file
                if (!dataFile.createNewFile()) {
                    logger.severe("Unable to load or create PV inventory for " + owner.getName());
                    return null;
                }

                // we have created a new JSON file; create inventory, save, and return
                logger.fine("Created new PV inventory for " + owner.getName());
                Inventory result = InventoryHandler.create();
                saveInventory(result, owner);
                return result;
            }

            // if datafile DOES exist, load inventory from it.
            String json = readFile(dataFile);
            return gson.fromJson(json, Inventory.class);
        }
        catch (IOException e) {
            logger.severe("IOException caught while loading inventory for " + owner.getName() + "!");
            e.printStackTrace();
            return null;
        }
    }

    private static String readFile(File file) throws IOException {
        byte[] encoded = Files.readAllBytes(file.toPath());
        return new String(encoded, Charset.defaultCharset());
    }

}
