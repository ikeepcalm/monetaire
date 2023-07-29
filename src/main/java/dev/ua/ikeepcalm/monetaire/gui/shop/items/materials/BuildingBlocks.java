package dev.ua.ikeepcalm.monetaire.gui.shop.items.materials;

import dev.ua.ikeepcalm.monetaire.Configuration;
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


public class BuildingBlocks extends AbstractItem {

    private Advertiser advertiser;
    private Material material;
    private boolean enoughMoney;

    public BuildingBlocks(Advertiser advertiser, Material materials) {
        this.advertiser = advertiser;
        this.material = materials;
    }

    @Override
    public ItemProvider getItemProvider() {
        int price = Configuration.getConfiguration().getInt("building_block");
        if (advertiser.getBalance() > price){
            TextComponent name = Component.text("Вартість: " + price).color(TextColor.color(8, 255, 131));
            ItemProvider itemProvider = new ItemBuilder(material, 64).setDisplayName(new AdventureComponentWrapper(name));
            enoughMoney = true;
            return itemProvider;
        } else {
            TextComponent name = Component.text("Недостатньо балів").color(TextColor.color(255, 8, 131));
            enoughMoney = false;
            return new ItemBuilder(Material.BARRIER, 1).setDisplayName(new AdventureComponentWrapper(name));
        }
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        if (enoughMoney){
            System.out.println("You can buy this");
        } else {
            System.out.println("You can't buy this");
        }
    }
}
