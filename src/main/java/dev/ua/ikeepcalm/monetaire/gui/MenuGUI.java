package dev.ua.ikeepcalm.monetaire.gui;


import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Help;
import dev.jorel.commandapi.annotations.Permission;
import dev.ua.ikeepcalm.monetaire.gui.items.BalanceItem;
import dev.ua.ikeepcalm.monetaire.gui.items.VaultItem;
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

@Command("bank")
@Permission("monetaire.gui")
@Help("Використання: /bank")
public class MenuGUI {

    @Default
    public static void openMenu(Player player) {
        TextComponent transferComponent = Component.text("Здійснити переказ").color(TextColor.color(255, 8, 131));
        TextComponent payfineComponent = Component.text("Ваші штрафи").color(TextColor.color(255, 8, 131));
        TextComponent creatorComponent = Component.text("Автор: ikeepcalm").color(TextColor.color(255, 8, 131));
        TextComponent settingsComponent = Component.text("Налаштування").color(TextColor.color(255, 8, 131));
        TextComponent windowComponent = Component.text("Економіка (?)").color(TextColor.color(255, 8, 131));
        Item border = new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE));
        Item filler = new SimpleItem(new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE));
        Item balance = new BalanceItem();
        Item vault = new VaultItem();
        Item creator = new SimpleItem(new ItemBuilder(Material.PAPER).setDisplayName(new AdventureComponentWrapper(creatorComponent)).addLoreLines(("09.07.2023")));
        Item settings = new CommandItem(new ItemBuilder(Material.ANVIL).setDisplayName(new AdventureComponentWrapper(settingsComponent)), "/bank settings");
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
