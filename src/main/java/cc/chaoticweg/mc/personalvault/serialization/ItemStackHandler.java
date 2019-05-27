package cc.chaoticweg.mc.personalvault.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;
import java.util.Objects;

public class ItemStackHandler implements JsonSerializer<ItemStack> {

    @Override
    public JsonElement serialize(ItemStack src, Type type, JsonSerializationContext context) {
        JsonObject json = new JsonObject();

        // none of these SHOULD ever be null, but some CAN be. whew.
        try {
            json.addProperty("amount", src.getAmount());
            json.addProperty("material", Objects.requireNonNull(src.getData()).getItemType().ordinal());
        } catch (NullPointerException e) {
            return null;
        }

        return json;
    }

}
