package cc.chaoticweg.mc.personalvault.serialization;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Handles {@link File} I/O for personal vaults.
 */
public class PVIO {

    private final File inventoriesFolder;
    private final Logger logger;

    public PVIO(@NotNull File pluginDataFolder, @NotNull Logger pluginLogger) {
        File dataFolder = Objects.requireNonNull(pluginDataFolder);
        this.inventoriesFolder = new File(dataFolder, "inventories");
        this.logger = Objects.requireNonNull(pluginLogger);
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
     * Initialize a player's vault by creating a new one and saving it to file.
     *
     * @param player The player whose vault we are initializing
     */
    private void initializeInventory(@NotNull OfflinePlayer player) {
        OfflinePlayer owner = Objects.requireNonNull(player);

        logger.info("Initializing inventory for " + owner.getName());

        Inventory result = Bukkit.getServer().createInventory(owner.getPlayer(), InventoryType.CHEST);
        this.saveInventory(owner, result);
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
    public void saveInventory(@NotNull UUID nnUuid, @NotNull Inventory src) {
        Inventory inv = Objects.requireNonNull(src);
        UUID uuid = Objects.requireNonNull(nnUuid);
        File dataFile = this.getInventoryFile(uuid);

        try {
            String data = Base64.serializeInventory(inv);
            PVIO.writeFile(dataFile, data);
        } catch (Exception e) {
            this.logger.warning("Unable to save inventory");
            e.printStackTrace();
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
        logger.fine("Saving inventory for " + owner.getName());
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
        Inventory inv = Bukkit.getServer().createInventory(null, InventoryType.CHEST);

        try {
            File dataFile = this.getInventoryFile(owner.getUniqueId());
            inv = PVIO.readFile(dataFile);
        } catch (IOException e) {
            this.logger.warning("Unexpected IOException occurred while reading the inventory file for " + player.getName());
            e.printStackTrace();
        }

        return inv;
    }

    /**
     * Get the {@link File} that should represent the inventory for the player whose UUID this is.
     *
     * @param uuid The {@link UUID} of the player whose file we want
     * @return The {@link File} that represents this player's inventory
     */
    @NotNull
    private File getInventoryFile(@NotNull UUID uuid) {
        String filename = Objects.requireNonNull(uuid) + "_b64.txt";
        return new File(this.inventoriesFolder, filename);
    }

    private static void writeFile(@NotNull File file, @NotNull String data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(data);
        }
    }

    private static Inventory readFile(@NotNull File file) throws IOException {
        String data = PVIO.readRawFile(file);
        return Base64.deserializeInventory(data);
    }

    private static String readRawFile(@NotNull File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }

}
