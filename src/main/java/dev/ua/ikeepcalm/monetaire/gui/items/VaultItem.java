package dev.ua.ikeepcalm.monetaire.gui.items;

import dev.ua.ikeepcalm.monetaire.gui.VaultGUI;
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

public class VaultItem extends AbstractItem {

    @Override
    public ItemProvider getItemProvider() {
        TextComponent depositComponent = Component.text("Сховище").color(TextColor.color(255, 8, 131));
        return new ItemBuilder(Material.DEEPSLATE_DIAMOND_ORE).setDisplayName(new AdventureComponentWrapper(depositComponent));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        if (clickType.isRightClick() || clickType.isLeftClick()){
            new VaultGUI().openVault(player);
        }
    }
}
