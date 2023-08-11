package dev.ua.ikeepcalm.monetaire.gui.shop.items.materials;

import dev.ua.ikeepcalm.monetaire.Configuration;
import dev.ua.ikeepcalm.monetaire.entities.Advertiser;
import dev.ua.ikeepcalm.monetaire.gui.shop.ShopGUI;
import dev.ua.ikeepcalm.monetaire.utils.WatermarkUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;
import xyz.xenondevs.inventoryaccess.component.ComponentWrapper;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.util.ArrayList;
import java.util.List;

import static dev.ua.ikeepcalm.monetaire.Monetaire.advertiserDao;


public class Elytra extends AbstractItem {

    private Advertiser advertiser;
    private Material material;
    private boolean enoughMoney;

    public Elytra(Advertiser advertiser, Material materials) {
        this.advertiser = advertiser;
        this.material = materials;
    }

    @Override
    public ItemProvider getItemProvider() {
        int price = Configuration.getConfiguration().getInt("elytra");
        int quantity = Configuration.getConfiguration().getInt("q_elytra");
        if (advertiser.getBalance() >= price){
            TextComponent name = Component.text("Вартість - " + price).color(TextColor.color(8, 255, 131));
            TextComponent copyrightLore = Component.text("© Phantom Shop ©").color(TextColor.color(8, 131, 225));
            List<ComponentWrapper> lore = new ArrayList<>();
            lore.add(new AdventureComponentWrapper(copyrightLore));
            ItemProvider itemProvider = new ItemBuilder(material, quantity).setDisplayName(new AdventureComponentWrapper(name)).setLore(lore);
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
        if (clickType.isLeftClick() || clickType.isRightClick()){
            if (enoughMoney){
                if (hasAvaliableSlot(player)){
                    int price = Configuration.getConfiguration().getInt("elytra");
                    Material material = getItemProvider().get().getType();
                    int quantity = getItemProvider().get().getAmount();
                    ItemStack itemStack = new ItemStack(material,quantity);
                    WatermarkUtil.addWatermark(itemStack);
                    player.getInventory().addItem(itemStack);
                    advertiser.setBalance(advertiser.getBalance() - price);
                    advertiserDao.save(advertiser);
                    ShopGUI.openMenu(player);
                }
            }
        }
    }

    public boolean hasAvaliableSlot(Player player){
        Inventory inv = player.getInventory();
        for (ItemStack item: inv.getContents()) {
            if(item == null) {
                return true;
            }
        }
        return false;
    }
}
