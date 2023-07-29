package dev.ua.ikeepcalm.monetaire.gui.shop.items;

import org.bukkit.Material;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.PageItem;

public class BackItem extends PageItem {

    public BackItem() {
        super(false);
    }

    @Override
    public ItemProvider getItemProvider(PagedGui<?> gui) {
        ItemBuilder builder = new ItemBuilder(Material.RED_STAINED_GLASS_PANE);
        builder.setDisplayName("§7Назад")
                .addLoreLines(gui.hasPreviousPage()
                        ? gui.getCurrentPage() + "§7/§e" + gui.getPageAmount()
                        : "§cХ");
        return builder;
    }

}
