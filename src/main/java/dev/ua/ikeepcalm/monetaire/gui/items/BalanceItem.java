package dev.ua.ikeepcalm.monetaire.gui.items;

import dev.ua.ikeepcalm.monetaire.gui.BalanceGUI;
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

    @Override
    public ItemProvider getItemProvider() {
        TextComponent balanceComponent = Component.text("Рахунки").color(TextColor.color(255, 8, 131));
        return new ItemBuilder(Material.ENDER_CHEST).setDisplayName(new AdventureComponentWrapper(balanceComponent));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        if (clickType.isRightClick() || clickType.isLeftClick()){
            new BalanceGUI().openBalance(player);
        }
    }
}
