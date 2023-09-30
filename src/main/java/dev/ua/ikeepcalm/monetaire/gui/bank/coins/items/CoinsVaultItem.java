package dev.ua.ikeepcalm.monetaire.gui.bank.coins.items;

import dev.ua.ikeepcalm.monetaire.gui.bank.coins.CoinsVaultGUI;
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

public class CoinsVaultItem extends AbstractItem {

    @Override
    public ItemProvider getItemProvider() {
        TextComponent depositComponent = Component.text("Аури").color(TextColor.color(244, 255, 94));
        return new ItemBuilder(Material.CLOCK).setDisplayName(new AdventureComponentWrapper(depositComponent)).setCustomModelData(1633212000);
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        if (clickType.isRightClick() || clickType.isLeftClick()){
            new CoinsVaultGUI().openVault(player);
        }
    }
}
