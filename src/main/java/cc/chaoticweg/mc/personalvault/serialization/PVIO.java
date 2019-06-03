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
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Handles {@link File} I/O for personal vaults.
 */
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

    /**
     * Initialize the PVIO instance to ensure that the proper directory structure exists
     */
    public void initialize() {
        logger.fine("Initializing PVIO");

        if (this.inventoriesFolder.mkdirs()) {
            logger.warning("Created new inventories folder at " + this.inventoriesFolder.getAbsolutePath());
        }
    }

    /**
     * Initialize a player's vault by creating a new one and saving it to file before returning it.
     *
     * @param player The player whose vault we are initializing
     * @return An empty {@link Inventory} after it is saved to file
     */
    private Inventory initializeInventory(@NotNull OfflinePlayer player) {
        OfflinePlayer owner = Objects.requireNonNull(player);

        logger.info("Initializing inventory for " + owner.getName());

        Inventory result = InventoryAdapter.createInventory();
        saveInventory(owner, result);
        return result;
    }

    /**
     * Check that a player's inventory exists, writing a new one to file if it does not.
     *
     * @param player The player for whom we are ensuring an inventory exists
     */
    private void checkInventory(@NotNull OfflinePlayer player) {
        OfflinePlayer owner = Objects.requireNonNull(player);
        File inventoryFile = this.getInventoryFile(owner.getUniqueId());

        logger.fine("Checking that inventory exists for " + owner.getName());

        if (!inventoryFile.exists()) {
            this.initializeInventory(owner);
        }
    }

    /**
     * Save an {@link Inventory} to file with path formatted as {@code <INV_DIR>/<UUID>.json}.
     *
     * @param nnUuid A non-null {@link UUID} for the player who owns this inventory
     * @param src A non-null {@link Inventory} owned by the players whose UUID this is
     */
    private void saveInventory(@NotNull UUID nnUuid, @NotNull Inventory src) {
        Inventory inv = Objects.requireNonNull(src);
        UUID uuid = Objects.requireNonNull(nnUuid);

        SerializableInventory inventory = new SerializableInventory(inv);
        File dataFile = this.getInventoryFile(uuid);

        // try to write to file
        try (FileWriter out = new FileWriter(dataFile)) {
            String json = gson.toJson(inventory);
            out.write(json);
        }
        catch (Exception ex) {
            this.logger.severe(String.format("Unable to save vault %s to file (%s): %s", uuid,
                    ex.getClass().getSimpleName(), ex.getMessage()));
        }
    }

    /**
     * Save an {@link Inventory} to file with path formatted as {@code <INV_DIR>/<UUID>.json}. Explicitly calls
     * {@link PVIO#saveInventory(UUID, Inventory)} with the player's {@link UUID}.
     *
     * @param player The player who owns this inventory
     * @param inv The inventory to save to the player's vault file
     */
    public void saveInventory(@NotNull OfflinePlayer player, @NotNull Inventory inv) {
        OfflinePlayer owner = Objects.requireNonNull(player);
        logger.fine("Saving vault for " + owner.getName());
        this.saveInventory(owner.getUniqueId(), Objects.requireNonNull(inv));
    }

    /**
     * Load the player's inventory from file.
     *
     * @param player The player whose inventory we are loading
     * @return The player's saved inventory if it exists, or an empty one if it does not (or if there is any error)
     */
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
            return this.initializeInventory(owner);
        }
    }

    /**
     * Get the {@link File} that should represent the inventory for the player whose UUID this is.
     *
     * @param uuid The {@link UUID} of the player whose file we want
     * @return The {@link File} that represents this player's inventory
     */
    @NotNull
    private File getInventoryFile(@NotNull UUID uuid) {
        String filename = Objects.requireNonNull(uuid).toString() + ".json";
        return new File(this.inventoriesFolder, filename);
    }

}
