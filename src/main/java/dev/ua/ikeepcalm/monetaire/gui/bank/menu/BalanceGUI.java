package dev.ua.ikeepcalm.monetaire.gui.bank.menu;

import dev.ua.ikeepcalm.monetaire.entities.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.CommandItem;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import static dev.ua.ikeepcalm.monetaire.Monetaire.playerDao;

public class BalanceGUI {
    public void openBalance(Player player) {
        User foundUser = playerDao.findByNickname(player);
        TextComponent drComponent = Component.text("Діри: " + foundUser.getCard().getBalance() + " ДР").color(TextColor.color(255, 8, 131));
        TextComponent fineComponent = Component.text("Дійсні штрафи: " + foundUser.getCard().getFine() + " ДР").color(TextColor.color(255, 8, 131));
        TextComponent loanComponent = Component.text("Заборгованість: " + foundUser.getCard().getLoan() + " ДР").color(TextColor.color(255, 8, 131));
        TextComponent aurComponent = Component.text("Аури: " + foundUser.getCard().getCoins() + " AUR").color(TextColor.color(255, 8, 131));
        TextComponent backComponent = Component.text("Назад").color(TextColor.color(8, 255, 131));
        TextComponent windowComponent = Component.text("Рахунки").color(TextColor.color(255, 8, 131));

        Item border = new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE));
        Item filler = new SimpleItem(new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE));
        Item dr = new SimpleItem(new ItemBuilder(Material.DEEPSLATE_DIAMOND_ORE).setDisplayName(new AdventureComponentWrapper(drComponent)));
        Item fine = new SimpleItem(new ItemBuilder(Material.DEEPSLATE_COAL_ORE).setDisplayName(new AdventureComponentWrapper(fineComponent)));
        Item loan = new SimpleItem(new ItemBuilder(Material.DEEPSLATE_COPPER_ORE).setDisplayName(new AdventureComponentWrapper(loanComponent)));
        Item aur = new SimpleItem(new ItemBuilder(Material.CLOCK).setDisplayName(new AdventureComponentWrapper(aurComponent)).setCustomModelData(1633212000));
        Item back = new CommandItem(new ItemBuilder(Material.NETHER_STAR).setDisplayName(new AdventureComponentWrapper(backComponent)), "/bank");
        Gui gui = Gui.normal()
                .setStructure(
                        "@ @ @ @ @ @ @ @ @",
                        "@ . b . . . s . @",
                        "@ . . . . . . . @",
                        "@ . f . . . l . @",
                        "@ @ @ @ e @ @ @ @")
                .addIngredient('@', border)
                .addIngredient('.', filler)
                .addIngredient('b', dr)
                .addIngredient('f', fine)
                .addIngredient('l', loan)
                .addIngredient('s', aur)
                .addIngredient('e', back)
                .build();
        Window window = Window.single()
                .setViewer(player)
                .setGui(gui)
                .setTitle(new AdventureComponentWrapper(windowComponent))
                .build();
        window.open();
    }
}
