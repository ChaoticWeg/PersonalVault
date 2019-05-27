package cc.chaoticweg.mc.personalvault.serialization;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

public class InventoryHandler implements JsonSerializer<Inventory>, JsonDeserializer<Inventory> {

    private final Gson gson;

    InventoryHandler() {
        gson = new GsonBuilder()
                .registerTypeAdapter(ItemStack.class, new ItemStackHandler())
                .create();
    }

    @Override
    public JsonElement serialize(Inventory src, Type type, JsonSerializationContext context) {
        JsonObject json = new JsonObject();

        JsonObject items = new JsonObject();

        for (int slot = 0; slot < src.getSize(); slot++) {
            items.add(String.valueOf(slot), gson.toJsonTree(src.getItem(slot)));
        }

        json.add("size", new JsonPrimitive(src.getSize()));
        json.add("items", items);
        return json;
    }

    @Override
    public Inventory deserialize(JsonElement elem, Type type, JsonDeserializationContext context)
            throws JsonParseException {
        return create();
    }

    private static final int DEFAULT_INVENTORY_SIZE = 54;

    public static Inventory create() {
        return Bukkit.getServer().createInventory(null, DEFAULT_INVENTORY_SIZE);
    }
}
