package dev.ua.ikeepcalm.monetaire.gui.shop.items;

import dev.ua.ikeepcalm.monetaire.entities.Advertiser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class BalanceItem extends AbstractItem {

    private Advertiser advertiser;

    public BalanceItem(Advertiser advertiser) {
        this.advertiser = advertiser;
    }

    @Override
    public ItemProvider getItemProvider() {
        ItemBuilder builder = new ItemBuilder(Material.PIGLIN_HEAD);
        TextComponent balance = Component.text("Баланс:").color(TextColor.color(255, 70, 131));
        TextComponent value = Component.text("$" + advertiser.getBalance()).color(TextColor.color(190, 70, 131));
        builder.setDisplayName(new AdventureComponentWrapper(balance)).addLoreLines(new AdventureComponentWrapper(value));
        return builder;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
    }
}
