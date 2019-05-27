package cc.chaoticweg.mc.personalvault.serialization;

import com.google.gson.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;
import java.util.Objects;

public class ItemStackHandler implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {

    @Override
    public JsonElement serialize(ItemStack src, Type type, JsonSerializationContext context) {
        JsonObject json = new JsonObject();

        // none of these SHOULD ever be null, but some CAN be. whew.
        try {
            json.addProperty("material", Objects.requireNonNull(src.getData()).getItemType().name());
            json.addProperty("amount", src.getAmount());
        }
        catch (NullPointerException e) {
            return null;
        }

        return json;
    }

    @Override
    public ItemStack deserialize(JsonElement elem, Type type, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject obj = elem.getAsJsonObject();

        Material material = Material.valueOf(obj.get("material").getAsString());
        int amount = obj.get("amount").getAsInt();

        return new ItemStack(material, amount);
    }
}
