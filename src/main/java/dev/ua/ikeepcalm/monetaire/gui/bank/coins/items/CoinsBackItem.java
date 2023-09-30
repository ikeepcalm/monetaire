package dev.ua.ikeepcalm.monetaire.gui.bank.coins.items;

import dev.ua.ikeepcalm.monetaire.entities.User;
import dev.ua.ikeepcalm.monetaire.entities.transactions.SystemTx;
import dev.ua.ikeepcalm.monetaire.entities.transactions.source.ActionType;
import dev.ua.ikeepcalm.monetaire.gui.bank.coins.CoinsVaultGUI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import static dev.ua.ikeepcalm.monetaire.Monetaire.playerDao;
import static dev.ua.ikeepcalm.monetaire.Monetaire.systemTxDao;

public class CoinsBackItem extends AbstractItem {

    CoinsVaultGUI coinsVaultGUI;

    public CoinsBackItem(CoinsVaultGUI coinsVaultGUI) {
        this.coinsVaultGUI = coinsVaultGUI;
    }

    @Override
    public ItemProvider getItemProvider() {
        TextComponent depositComponent = Component.text("Назад").color(TextColor.color(8, 255, 131));
        return new ItemBuilder(Material.NETHER_STAR).setDisplayName(new AdventureComponentWrapper(depositComponent));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        if (clickType.isRightClick() || clickType.isLeftClick()) {
            int amount = 0;
            for (ItemStack itemStack : inventoryClickEvent.getClickedInventory().getContents()) {
                if (itemStack != null) {
                    if (!(itemStack.getType() == Material.CLOCK || itemStack.getItemMeta().hasCustomModelData())) {
                        amount += itemStack.getAmount();
                    }
                }
            }
            User foundUser = playerDao.findByNickname(player);
            if (amount > foundUser.getCard().getCoins()){
                SystemTx systemTx = new SystemTx();
                systemTx.setAmount(amount);
                systemTx.setSender(foundUser.getNickname());
                systemTx.setSuccessful(true);
                systemTx.setActionType(ActionType.DEPCOIN);
                systemTxDao.save(systemTx);
            } else if (!(amount == foundUser.getCard().getCoins())){
                SystemTx systemTx = new SystemTx();
                systemTx.setAmount(amount);
                systemTx.setSender(foundUser.getNickname());
                systemTx.setSuccessful(true);
                systemTx.setActionType(ActionType.WITHCOIN);
                systemTxDao.save(systemTx);
            }
            foundUser.getCard().setCoins((long) amount);
            playerDao.save(foundUser);
            player.performCommand("bank");
        }
    }

}
