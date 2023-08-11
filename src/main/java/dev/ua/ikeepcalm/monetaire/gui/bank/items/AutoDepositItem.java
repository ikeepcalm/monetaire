package dev.ua.ikeepcalm.monetaire.gui.bank.items;

import dev.ua.ikeepcalm.monetaire.entities.User;
import dev.ua.ikeepcalm.monetaire.gui.bank.SettingsGUI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import static dev.ua.ikeepcalm.monetaire.Monetaire.playerDao;

public class AutoDepositItem extends AbstractItem {

    boolean isEnabled;

    public AutoDepositItem(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    @Override
    public ItemProvider getItemProvider() {
        if (isEnabled){
            TextComponent autoDepositName = Component.text("Авто-поповнення").color(TextColor.color(8, 255, 131));
            TextComponent autoDepositLore1 = Component.text("Автоматично переносить здобуті Діри у Сховище").color(TextColor.color(230, 200, 230));
            TextComponent autoDepositLore2 = Component.text("Для використання необхідне кайло із Шовковим дотиком").color(TextColor.color(230, 200, 230));
            return new ItemBuilder(Material.GREEN_DYE)
                    .setDisplayName(new AdventureComponentWrapper(autoDepositName))
                    .addLoreLines(new AdventureComponentWrapper(autoDepositLore1))
                    .addLoreLines(new AdventureComponentWrapper(autoDepositLore2));
        } else {
            TextComponent autoDepositName = Component.text("Авто-поповнення").color(TextColor.color(255, 8, 131));
            TextComponent autoDepositLore1 = Component.text("Автоматично переносить здобуті Діри у Сховище").color(TextColor.color(230, 200, 230));
            TextComponent autoDepositLore2 = Component.text("Для використання необхідне кайло із Шовковим дотиком").color(TextColor.color(230, 200, 230));
            return new ItemBuilder(Material.RED_DYE)
                    .setDisplayName(new AdventureComponentWrapper(autoDepositName))
                    .addLoreLines(new AdventureComponentWrapper(autoDepositLore1))
                    .addLoreLines(new AdventureComponentWrapper(autoDepositLore2));
        }
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        if (clickType.isRightClick() || clickType.isLeftClick()) {
            if (player.hasPermission("monetaire.autodeposit")){
                User foundUser = playerDao.findByNickname(player);
                foundUser.setAutoDeposit(!isEnabled);
                playerDao.save(foundUser);
                new SettingsGUI().openSettings(player);
            } else {
                MiniMessage mm = MiniMessage.miniMessage();
                StringBuilder sb = new StringBuilder();
                sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
                sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Ця функція доступна тільки для гравців із\n");
                sb.append("<bold><#5555FF>BANK</bold> <#1c99ff>> Wealth-3 <#FFFFFF>преміум-підпискою!\n");
                sb.append("<bold><#5555FF>-----------------------------------------</bold>");
                Component parsed = mm.deserialize(sb.toString());
                player.sendMessage(parsed);
            }
        }
    }
}
