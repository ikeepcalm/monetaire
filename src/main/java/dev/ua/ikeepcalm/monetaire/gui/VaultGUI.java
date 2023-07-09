package dev.ua.ikeepcalm.monetaire.gui;

import dev.ua.ikeepcalm.monetaire.gui.items.BackItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Consumer;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.inventory.VirtualInventory;
import xyz.xenondevs.invui.inventory.event.ItemPreUpdateEvent;
import xyz.xenondevs.invui.inventory.event.UpdateReason;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import static dev.ua.ikeepcalm.monetaire.Monetaire.playerDao;

public class VaultGUI {

    public void openVault(Player player) {
        dev.ua.ikeepcalm.monetaire.entities.Player foundPlayer = playerDao.findByNickname(player);
        TextComponent windowComponent = Component.text("Економіка (?)").color(TextColor.color(255, 8, 131));
//
        VirtualInventory virtualInventory = new VirtualInventory(21);
        int diamondOreAmount = Math.toIntExact(foundPlayer.getBalance());
        ItemStack diamondOre = new ItemStack(Material.DEEPSLATE_DIAMOND_ORE, diamondOreAmount);
        virtualInventory.addItem(UpdateReason.SUPPRESSED, diamondOre);
        virtualInventory.setPreUpdateHandler(new Consumer<ItemPreUpdateEvent>() {
            @Override
            public void accept(ItemPreUpdateEvent itemPreUpdateEvent) {
                ItemStack newItem = itemPreUpdateEvent.getNewItem();
                if (newItem != null) {
                    if (!(newItem.getType() == Material.DIAMOND_ORE || newItem.getType() == Material.DEEPSLATE_DIAMOND_ORE)) {
                        itemPreUpdateEvent.setCancelled(true);
                    }
                }
            }
        });
//

        Item border = new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE));
        Item back = new BackItem();

        Gui gui = Gui.normal()
                .setStructure(
                        "@ @ @ @ @ @ @ @ @",
                        "@ . . . . . . . @",
                        "@ . . . . . . . @",
                        "@ . . . . . . . @",
                        "@ @ @ @ b @ @ @ @")
                .addIngredient('@', border)
                .addIngredient('.', virtualInventory)
                .addIngredient('b', back)
                .build();
        Window window = Window.single()
                .setViewer(player)
                .setGui(gui)
                .setTitle(new AdventureComponentWrapper(windowComponent))
                .build();
        window.open();
    }

}
