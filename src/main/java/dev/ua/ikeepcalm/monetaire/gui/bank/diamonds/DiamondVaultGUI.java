package dev.ua.ikeepcalm.monetaire.gui.bank.diamonds;

import dev.ua.ikeepcalm.monetaire.entities.EcoUser;
import dev.ua.ikeepcalm.monetaire.entities.transactions.SystemTx;
import dev.ua.ikeepcalm.monetaire.entities.transactions.source.ActionType;
import dev.ua.ikeepcalm.monetaire.gui.bank.diamonds.items.DiamondBackItem;
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

import static dev.ua.ikeepcalm.monetaire.Monetaire.ecoPlayerDao;
import static dev.ua.ikeepcalm.monetaire.Monetaire.systemTxDao;

public class DiamondVaultGUI {

    ItemStack[] inventoryToRestore;
    ItemStack[] savedInventory;
    VirtualInventory virtualInventory;

    public void openVault(Player player) {
        EcoUser foundEcoUser = ecoPlayerDao.findByNickname(player);
        TextComponent windowComponent = Component.text("Сховище Діарів").color(TextColor.color(255, 8, 131));
//
        inventoryToRestore = player.getInventory().getContents();
        virtualInventory = new VirtualInventory(21);
        int diamondOreAmount = Math.toIntExact(foundEcoUser.getCard().getBalance());
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
        Item back = new DiamondBackItem(this);

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
                        EcoUser foundEcoUser = ecoPlayerDao.findByNickname(player);
                        if (amount > foundEcoUser.getCard().getBalance()) {
                            SystemTx systemTx = new SystemTx();
                            systemTx.setAmount(amount);
                            systemTx.setSender(foundEcoUser.getNickname());
                            systemTx.setSuccessful(true);
                            systemTx.setActionType(ActionType.DEPOSIT);
                            systemTx.setMomentBalance("MainBalance: " + foundEcoUser.getCard().getBalance()
                                    + " | Credits: "+ foundEcoUser.getCard().getLoan() +" | Fines: " + foundEcoUser.getCard().getFine());
                            systemTxDao.save(systemTx);
                        } else if (!(amount == foundEcoUser.getCard().getBalance())) {
                            SystemTx systemTx = new SystemTx();
                            systemTx.setAmount(amount);
                            systemTx.setSender(foundEcoUser.getNickname());
                            systemTx.setSuccessful(true);
                            systemTx.setActionType(ActionType.WITHDRAW);
                            systemTx.setMomentBalance("MainBalance: " + foundEcoUser.getCard().getBalance()
                                    + " | Credits: "+ foundEcoUser.getCard().getLoan() +" | Fines: " + foundEcoUser.getCard().getFine());
                            systemTxDao.save(systemTx);
                        }
                        foundEcoUser.getCard().setBalance((long) amount);
                        ecoPlayerDao.save(foundEcoUser);
                    }
                })
                .build();
        window.open();
    }
}
