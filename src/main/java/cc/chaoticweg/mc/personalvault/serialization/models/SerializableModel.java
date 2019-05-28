package cc.chaoticweg.mc.personalvault.serialization.models;

import org.jetbrains.annotations.NotNull;

/**
 * A POJO with a {@code deserialize()} method.
 * @param <T> The type of {@link org.bukkit.inventory.Inventory}-related class this model represents
 */
public interface SerializableModel<T> {

    /**
     * Deserialize this model.
     * @return A new instance of the class {@code T} this model represents
     */
    @NotNull
    T deserialize();
}
