package dev.ua.ikeepcalm.monetaire.gui.bank;


import dev.ua.ikeepcalm.monetaire.gui.bank.items.AutoDepositItem;
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

public class SettingsGUI {

    public void openSettings(Player player) {
        dev.ua.ikeepcalm.monetaire.entities.Player foundPlayer = playerDao.findByNickname(player);
        TextComponent windowComponent = Component.text("Економіка (?)").color(TextColor.color(255, 8, 131));
        TextComponent comingSoonComponent = Component.text("Скоро...").color(TextColor.color(255, 8, 131));
        TextComponent backComponent = Component.text("Назад").color(TextColor.color(8, 255, 131));
        Item border = new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE));
        Item autodeposit = new AutoDepositItem(foundPlayer.getAutoDeposit());
        Item comingSoon = new SimpleItem(new ItemBuilder(Material.WHITE_DYE).setDisplayName(new AdventureComponentWrapper(comingSoonComponent)));
        Item filler = new SimpleItem(new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE));
        Item back = new CommandItem(new ItemBuilder(Material.NETHER_STAR).setDisplayName(new AdventureComponentWrapper(backComponent)), "/bank");
        Gui gui = Gui.normal()
                .setStructure(
                        "@ @ @ @ @ @ @ @ @",
                        "@ . . . . . . . @",
                        "@ . a . c . c . @",
                        "@ . . . . . . . @",
                        "@ @ @ @ b @ @ @ @")
                .addIngredient('@', border)
                .addIngredient('.', filler)
                .addIngredient('b', back)
                .addIngredient('a', autodeposit)
                .addIngredient('c', comingSoon)
                .build();
        Window window = Window.single()
                .setViewer(player)
                .setGui(gui)
                .setTitle(new AdventureComponentWrapper(windowComponent))
                .build();
        window.open();
    }
}
