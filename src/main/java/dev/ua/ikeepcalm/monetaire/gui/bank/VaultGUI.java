package dev.ua.ikeepcalm.monetaire.gui.bank;

import dev.ua.ikeepcalm.monetaire.entities.User;
import dev.ua.ikeepcalm.monetaire.entities.transactions.SystemTx;
import dev.ua.ikeepcalm.monetaire.entities.transactions.source.ActionType;
import dev.ua.ikeepcalm.monetaire.gui.bank.items.BackItem;
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
import static dev.ua.ikeepcalm.monetaire.Monetaire.systemTxDao;

public class VaultGUI {

    ItemStack[] inventoryToRestore;
    ItemStack[] savedInventory;
    VirtualInventory virtualInventory;

    public void openVault(Player player) {
        User foundUser = playerDao.findByNickname(player);
        TextComponent windowComponent = Component.text("Економіка (?)").color(TextColor.color(255, 8, 131));
//
        inventoryToRestore = player.getInventory().getContents();
        virtualInventory = new VirtualInventory(21);
        int diamondOreAmount = Math.toIntExact(foundUser.getCard().getBalance());
        ItemStack diamondOre = new ItemStack(Material.DEEPSLATE_DIAMOND_ORE, diamondOreAmount);
        virtualInventory.addItem(UpdateReason.SUPPRESSED, diamondOre);
        virtualInventory.setPreUpdateHandler(new Consumer<ItemPreUpdateEvent>() {
            @Override
            public void accept(ItemPreUpdateEvent itemPreUpdateEvent) {
                ItemStack newItem = itemPreUpdateEvent.getNewItem();
                if (newItem != null) {
                    if (!(newItem.getType() == Material.DIAMOND_ORE || newItem.getType() == Material.DEEPSLATE_DIAMOND_ORE)) {
                        itemPreUpdateEvent.setCancelled(true);
                    } else {
                        savedInventory = itemPreUpdateEvent.getInventory().getItems();
                    }
                }
            }
        });
//

        Item border = new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE));
        Item back = new BackItem(this);

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
                .addCloseHandler(new Runnable() {
                    @Override
                    public void run() {
                        int amount = 0;
                        for (ItemStack itemStack : virtualInventory.getItems()) {
                            if (itemStack != null) {
                                if (itemStack.getType() == Material.DEEPSLATE_DIAMOND_ORE || itemStack.getType() == Material.DIAMOND_ORE) {
                                    amount += itemStack.getAmount();
                                }
                            }
                        }
                        User foundUser = playerDao.findByNickname(player);
                        if (amount > foundUser.getCard().getBalance()) {
                            SystemTx systemTx = new SystemTx();
                            systemTx.setAmount(amount);
                            systemTx.setSender(foundUser.getNickname());
                            systemTx.setSuccessful(true);
                            systemTx.setActionType(ActionType.DEPOSIT);
                            systemTx.setMomentBalance("MainBalance: " + foundUser.getCard().getBalance()
                                    + " | Credits: "+ foundUser.getCard().getLoan() +" | Fines: " + foundUser.getCard().getFine());
                            systemTxDao.save(systemTx);
                        } else if (!(amount == foundUser.getCard().getBalance())) {
                            SystemTx systemTx = new SystemTx();
                            systemTx.setAmount(amount);
                            systemTx.setSender(foundUser.getNickname());
                            systemTx.setSuccessful(true);
                            systemTx.setActionType(ActionType.WITHDRAW);
                            systemTx.setMomentBalance("MainBalance: " + foundUser.getCard().getBalance()
                                    + " | Credits: "+ foundUser.getCard().getLoan() +" | Fines: " + foundUser.getCard().getFine());
                            systemTxDao.save(systemTx);
                        }
                        foundUser.getCard().setBalance((long) amount);
                        playerDao.save(foundUser);
                    }
                })
                .build();
        window.open();
    }
}
