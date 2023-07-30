package dev.ua.ikeepcalm.monetaire.gui.shop.utils;

import dev.ua.ikeepcalm.monetaire.Monetaire;
import org.bukkit.NamespacedKey;

public class KeyUtil {
    private static final NamespacedKey WATERMARK_KEY;

    static {
        WATERMARK_KEY = new NamespacedKey(Monetaire.getPlugin(Monetaire.class), "watermark");
    }

    public static NamespacedKey getWatermarkKey() {
        return WATERMARK_KEY;
    }
}
