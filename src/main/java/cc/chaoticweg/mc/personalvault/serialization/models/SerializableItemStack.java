package cc.chaoticweg.mc.personalvault.serialization.models;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SerializableItemStack implements SerializableModel<ItemStack> {

    private String material;
    private int amount;

    private SerializableItemStack(ItemStack stack) {
        this.material = stack.getType().name();
        this.amount = stack.getAmount();
    }

    @Override
    public ItemStack deserialize() {
        return new ItemStack(Material.valueOf(this.material), this.amount);
    }

    static SerializableItemStack serialize(ItemStack stack) {
        return stack == null ? null : new SerializableItemStack(stack);
    }
}
