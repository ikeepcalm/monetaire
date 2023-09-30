package dev.ua.ikeepcalm.monetaire.gui.bank.diamonds.items;

import dev.ua.ikeepcalm.monetaire.gui.bank.diamonds.DiamondVaultGUI;
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

public class DiamondVaultItem extends AbstractItem {

    @Override
    public ItemProvider getItemProvider() {
        TextComponent depositComponent = Component.text("Діари").color(TextColor.color(70, 133, 250));
        return new ItemBuilder(Material.DEEPSLATE_DIAMOND_ORE).setDisplayName(new AdventureComponentWrapper(depositComponent));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        if (clickType.isRightClick() || clickType.isLeftClick()){
            new DiamondVaultGUI().openVault(player);
        }
    }
}
