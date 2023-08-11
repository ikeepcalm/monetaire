package dev.ua.ikeepcalm.monetaire.gui.bank;


import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Help;
import dev.jorel.commandapi.annotations.Permission;
import dev.ua.ikeepcalm.monetaire.entities.User;
import dev.ua.ikeepcalm.monetaire.gui.bank.items.BalanceItem;
import dev.ua.ikeepcalm.monetaire.gui.bank.items.SettingsItem;
import dev.ua.ikeepcalm.monetaire.gui.bank.items.VaultItem;
import dev.ua.ikeepcalm.monetaire.utils.ChatUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import static dev.ua.ikeepcalm.monetaire.Monetaire.playerDao;

@Command("bank")
@Permission("monetaire.gui")
@Help("Використання: /bank")
public class MenuGUI {

    @Default
    public static void openMenu(Player player) {
        User depositUser = playerDao.findByNickname(player);
        if (depositUser.getCard() == null) {
            ChatUtil.sendMessage(player,
                    "У вас немає картки!",
                    "Для отримання пройдіть у банк ➜ 41, 65, -17 ( Спавн )");
        } else {
            TextComponent creatorComponent = Component.text("Автор: ikeepcalm").color(TextColor.color(255, 8, 131));
            TextComponent windowComponent = Component.text("Економіка (?)").color(TextColor.color(255, 8, 131));
            Item border = new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE));
            Item filler = new SimpleItem(new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE));
            Item balance = new BalanceItem();
            Item vault = new VaultItem();
            Item creator = new SimpleItem(new ItemBuilder(Material.PAPER).setDisplayName(new AdventureComponentWrapper(creatorComponent)));
            Item settings = new SettingsItem();
            Gui gui = Gui.normal()
                    .setStructure(
                            "@ @ @ @ @ @ @ @ @",
                            "@ . . . . . . . @",
                            "@ . . b . v . . @",
                            "@ s . . . . . s @",
                            "@ @ @ @ c @ @ @ @")
                    .addIngredient('@', border)
                    .addIngredient('.', filler)
                    .addIngredient('b', balance)
                    .addIngredient('v', vault)
                    .addIngredient('s', settings)
                    .addIngredient('c', creator)
                    .build();
            Window window = Window.single()
                    .setViewer(player)
                    .setGui(gui)
                    .setTitle(new AdventureComponentWrapper(windowComponent))
                    .build();
            window.open();
        }
    }
}
