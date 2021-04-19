package cc.chaoticweg.mc.personalvault.serialization;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class Base64 {

    public static String serializeInventory(Inventory inventory) throws IOException {
        try (ByteArrayOutputStream byteOutStr = new ByteArrayOutputStream()) {
            BukkitObjectOutputStream objOutStr = new BukkitObjectOutputStream(byteOutStr);

            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack stack = inventory.getItem(i);
                if (stack != null) {
                    objOutStr.writeInt(i);
                    objOutStr.writeObject(stack);
                }
            }

            objOutStr.close();
            return Base64Coder.encodeLines(byteOutStr.toByteArray());
        } catch (Exception e) {
            throw new IOException("Unable to serialize inventory", e);
        }
    }

    public static Inventory deserializeInventory(String data) throws IOException {
        try (ByteArrayInputStream byteInStr = new ByteArrayInputStream(Base64Coder.decodeLines(data))) {
            BukkitObjectInputStream objInStr = new BukkitObjectInputStream(byteInStr);
            Inventory inventory = Bukkit.getServer().createInventory(null, InventoryType.CHEST);

            while (objInStr.available() > 0) {
                int index = objInStr.readInt();
                ItemStack stack = (ItemStack) objInStr.readObject();
                if (stack != null) {
                    inventory.setItem(index, stack);
                }
            }

            objInStr.close();
            return inventory;
        } catch (Exception e) {
            throw new IOException("Unable to deserialize inventory", e);
        }
    }

}
