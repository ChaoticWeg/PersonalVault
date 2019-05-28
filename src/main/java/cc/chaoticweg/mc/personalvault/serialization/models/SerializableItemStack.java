package cc.chaoticweg.mc.personalvault.serialization.models;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link SerializableModel} to represent an {@link ItemStack}.
 */
public class SerializableItemStack implements SerializableModel<ItemStack> {

    private String material;
    private int amount;

    private SerializableItemStack(ItemStack stack) {
        this.material = stack.getType().name();
        this.amount = stack.getAmount();
    }

    @NotNull
    @Override
    public ItemStack deserialize() {
        return new ItemStack(Material.valueOf(this.material), this.amount);
    }

    /**
     * Serialize this {@link ItemStack}. Provided as a static method because this can be null.
     *
     * @param stack The {@link ItemStack} to serialize
     * @return A serialized {@link ItemStack}, or null if the {@link ItemStack} argument is null.
     */
    @Nullable
    static SerializableItemStack serialize(@Nullable ItemStack stack) {
        return stack == null ? null : new SerializableItemStack(stack);
    }
}
