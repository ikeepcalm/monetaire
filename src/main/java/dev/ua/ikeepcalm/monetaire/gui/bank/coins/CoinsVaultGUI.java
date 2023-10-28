package dev.ua.ikeepcalm.monetaire.gui.bank.coins;

import dev.ua.ikeepcalm.monetaire.entities.EcoUser;
import dev.ua.ikeepcalm.monetaire.entities.transactions.SystemTx;
import dev.ua.ikeepcalm.monetaire.entities.transactions.source.ActionType;
import dev.ua.ikeepcalm.monetaire.gui.bank.coins.items.CoinsBackItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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

public class CoinsVaultGUI {

    ItemStack[] inventoryToRestore;
    ItemStack[] savedInventory;
    VirtualInventory virtualInventory;

    public void openVault(Player player) {
        EcoUser foundEcoUser = ecoPlayerDao.findByNickname(player);
        TextComponent windowComponent = Component.text("Сховище Аурів").color(TextColor.color(255, 8, 131));
//
        inventoryToRestore = player.getInventory().getContents();
        virtualInventory = new VirtualInventory(21);
        int coinsAmount = Math.toIntExact(foundEcoUser.getCard().getCoins());
        ItemStack coin = new ItemStack(Material.CLOCK);
        coin.setAmount(coinsAmount);
        ItemMeta itemMeta = coin.getItemMeta();
        itemMeta.addEnchant(Enchantment.DURABILITY, 1, false);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.setDisplayName(ChatColor.RESET + "" + ChatColor.YELLOW + "Аур");
        itemMeta.setCustomModelData(1633212000);
        coin.setItemMeta(itemMeta);
        virtualInventory.addItem(UpdateReason.SUPPRESSED, coin);
        virtualInventory.setPreUpdateHandler(new Consumer<ItemPreUpdateEvent>() {
            @Override
            public void accept(ItemPreUpdateEvent itemPreUpdateEvent) {
                ItemStack newItem = itemPreUpdateEvent.getNewItem();
                if (newItem != null) {
                    if (!(newItem.getType() == Material.CLOCK && newItem.getItemMeta().hasCustomModelData())) {
                        itemPreUpdateEvent.setCancelled(true);
                    } else {
                        savedInventory = itemPreUpdateEvent.getInventory().getItems();
                    }
                }
            }
        });
//

        Item border = new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE));
        Item back = new CoinsBackItem(this);

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
                                if (itemStack.getType() == Material.CLOCK && itemStack.getItemMeta().hasCustomModelData()) {
                                    amount += itemStack.getAmount();
                                }
                            }
                        }
                        EcoUser foundEcoUser = ecoPlayerDao.findByNickname(player);
                        if (amount > foundEcoUser.getCard().getCoins()) {
                            SystemTx systemTx = new SystemTx();
                            systemTx.setAmount(amount);
                            systemTx.setSender(foundEcoUser.getNickname());
                            systemTx.setSuccessful(true);
                            systemTx.setActionType(ActionType.DEPCOIN);
                            systemTx.setMomentBalance("MainBalance: " + foundEcoUser.getCard().getBalance()
                                    + " | Credits: "+ foundEcoUser.getCard().getLoan() +" | Fines: " + foundEcoUser.getCard().getFine());
                            systemTxDao.save(systemTx);
                        } else if (!(amount == foundEcoUser.getCard().getCoins())) {
                            SystemTx systemTx = new SystemTx();
                            systemTx.setAmount(amount);
                            systemTx.setSender(foundEcoUser.getNickname());
                            systemTx.setSuccessful(true);
                            systemTx.setActionType(ActionType.WITHCOIN);
                            systemTx.setMomentBalance("MainBalance: " + foundEcoUser.getCard().getBalance()
                                    + " | Credits: "+ foundEcoUser.getCard().getLoan() +" | Fines: " + foundEcoUser.getCard().getFine());
                            systemTxDao.save(systemTx);
                        }
                        foundEcoUser.getCard().setCoins((long) amount);
                        ecoPlayerDao.save(foundEcoUser);
                    }
                })
                .build();
        window.open();
    }
}
