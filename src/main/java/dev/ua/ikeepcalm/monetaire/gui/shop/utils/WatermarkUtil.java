package dev.ua.ikeepcalm.monetaire.gui.shop.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class WatermarkUtil {
    public static void addWatermark(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        itemMeta.getPersistentDataContainer().set(KeyUtil.getWatermarkKey(), PersistentDataType.STRING, "phantom-shop");
        itemStack.setItemMeta(itemMeta);
    }

    public static boolean hasWatermark(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return false;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return false;
        }

        return itemMeta.getPersistentDataContainer().has(KeyUtil.getWatermarkKey(), PersistentDataType.STRING);
    }
}
