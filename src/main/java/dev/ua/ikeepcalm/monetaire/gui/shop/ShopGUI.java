package dev.ua.ikeepcalm.monetaire.gui.shop;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Help;
import dev.jorel.commandapi.annotations.Permission;
import dev.ua.ikeepcalm.monetaire.entities.Advertiser;
import dev.ua.ikeepcalm.monetaire.gui.shop.items.BackItem;
import dev.ua.ikeepcalm.monetaire.gui.shop.items.ForwardItem;
import dev.ua.ikeepcalm.monetaire.gui.shop.items.materials.BuildingBlocks;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static dev.ua.ikeepcalm.monetaire.Monetaire.advertiserDao;

@Command("shop")
@Permission("monetaire.shop")
@Help("Використання: /shop")
public class ShopGUI {

    @Default
    public static void openMenu(Player player) {
        Advertiser advertiser = advertiserDao.findByNickname(player);
        Item border = new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("§r"));
        TextComponent windowComponent = Component.text("Фантомний магазин").color(TextColor.color(255, 8, 131));
        List<Item> items = new ArrayList<>();
        addBuildingBlocks(items, advertiser);
        Gui gui = PagedGui.items()
                .setStructure(
                        "# # # # # # # # #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# # # < # > # # #")
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('#', border)
                .addIngredient('<', new BackItem())
                .addIngredient('>', new ForwardItem())
                .setContent(items)
                .build();

        Window window = Window.single()
                .setViewer(player)
                .setGui(gui)
                .setTitle(new AdventureComponentWrapper(windowComponent))
                .build();
        window.open();
    }

    public static void addBuildingBlocks(List<Item> origin, Advertiser advertiser){
        List<Material> includedMaterials = Arrays.asList(
                Material.STONE,
                Material.GRANITE,
                Material.POLISHED_GRANITE,
                Material.DIORITE,
                Material.POLISHED_DIORITE,
                Material.ANDESITE,
                Material.POLISHED_ANDESITE,
                Material.COBBLESTONE,
                Material.SAND,
                Material.RED_SAND,
                Material.GRAVEL,
                Material.SANDSTONE,
                Material.CHISELED_SANDSTONE,
                Material.CUT_SANDSTONE,
                Material.RED_SANDSTONE,
                Material.CHISELED_RED_SANDSTONE,
                Material.CUT_RED_SANDSTONE,
                Material.CLAY,
                Material.TERRACOTTA,
                Material.WHITE_TERRACOTTA,
                Material.ORANGE_TERRACOTTA,
                Material.MAGENTA_TERRACOTTA,
                Material.LIGHT_BLUE_TERRACOTTA,
                Material.YELLOW_TERRACOTTA,
                Material.LIME_TERRACOTTA,
                Material.PINK_TERRACOTTA,
                Material.GRAY_TERRACOTTA,
                Material.LIGHT_GRAY_TERRACOTTA,
                Material.CYAN_TERRACOTTA,
                Material.PURPLE_TERRACOTTA,
                Material.BLUE_TERRACOTTA,
                Material.BROWN_TERRACOTTA,
                Material.GREEN_TERRACOTTA,
                Material.RED_TERRACOTTA,
                Material.BLACK_TERRACOTTA,
                Material.WHITE_CONCRETE,
                Material.ORANGE_CONCRETE,
                Material.MAGENTA_CONCRETE,
                Material.LIGHT_BLUE_CONCRETE,
                Material.YELLOW_CONCRETE,
                Material.LIME_CONCRETE,
                Material.PINK_CONCRETE,
                Material.GRAY_CONCRETE,
                Material.LIGHT_GRAY_CONCRETE,
                Material.CYAN_CONCRETE,
                Material.PURPLE_CONCRETE,
                Material.BLUE_CONCRETE,
                Material.BROWN_CONCRETE,
                Material.GREEN_CONCRETE,
                Material.RED_CONCRETE,
                Material.BLACK_CONCRETE,
                Material.QUARTZ_BLOCK,
                Material.CHISELED_QUARTZ_BLOCK,
                Material.BONE_BLOCK,
                Material.OBSIDIAN,
                Material.PRISMARINE,
                Material.DARK_PRISMARINE,
                Material.NETHERRACK,
                Material.END_STONE,
                Material.PURPUR_BLOCK,
                Material.MAGMA_BLOCK,
                Material.GILDED_BLACKSTONE,
                Material.BLACKSTONE,
                Material.POLISHED_BLACKSTONE,
                Material.CHISELED_POLISHED_BLACKSTONE,
                Material.WHITE_WOOL,
                Material.ORANGE_WOOL,
                Material.MAGENTA_WOOL,
                Material.LIGHT_BLUE_WOOL,
                Material.YELLOW_WOOL,
                Material.LIME_WOOL,
                Material.PINK_WOOL,
                Material.GRAY_WOOL,
                Material.LIGHT_GRAY_WOOL,
                Material.CYAN_WOOL,
                Material.PURPLE_WOOL,
                Material.BLUE_WOOL,
                Material.BROWN_WOOL,
                Material.GREEN_WOOL,
                Material.RED_WOOL,
                Material.BLACK_WOOL,
                Material.HAY_BLOCK,
                Material.TERRACOTTA,
                Material.WHITE_GLAZED_TERRACOTTA,
                Material.ORANGE_GLAZED_TERRACOTTA,
                Material.MAGENTA_GLAZED_TERRACOTTA,
                Material.LIGHT_BLUE_GLAZED_TERRACOTTA,
                Material.YELLOW_GLAZED_TERRACOTTA,
                Material.LIME_GLAZED_TERRACOTTA,
                Material.PINK_GLAZED_TERRACOTTA,
                Material.GRAY_GLAZED_TERRACOTTA,
                Material.LIGHT_GRAY_GLAZED_TERRACOTTA,
                Material.CYAN_GLAZED_TERRACOTTA,
                Material.PURPLE_GLAZED_TERRACOTTA,
                Material.BLUE_GLAZED_TERRACOTTA,
                Material.BROWN_GLAZED_TERRACOTTA,
                Material.GREEN_GLAZED_TERRACOTTA,
                Material.RED_GLAZED_TERRACOTTA,
                Material.BLACK_GLAZED_TERRACOTTA,
                Material.WHITE_TERRACOTTA,
                Material.ORANGE_TERRACOTTA,
                Material.MAGENTA_TERRACOTTA,
                Material.LIGHT_BLUE_TERRACOTTA,
                Material.YELLOW_TERRACOTTA,
                Material.LIME_TERRACOTTA,
                Material.PINK_TERRACOTTA,
                Material.GRAY_TERRACOTTA,
                Material.LIGHT_GRAY_TERRACOTTA,
                Material.CYAN_TERRACOTTA,
                Material.PURPLE_TERRACOTTA,
                Material.BLUE_TERRACOTTA,
                Material.BROWN_TERRACOTTA,
                Material.GREEN_TERRACOTTA,
                Material.RED_TERRACOTTA,
                Material.BLACK_TERRACOTTA,
                Material.SLIME_BLOCK,
                Material.HONEY_BLOCK,
                Material.SHROOMLIGHT,
                Material.POLISHED_BASALT,
                Material.CRIMSON_HYPHAE,
                Material.WARPED_HYPHAE,
                Material.STRIPPED_ACACIA_WOOD,
                Material.STRIPPED_BIRCH_WOOD,
                Material.STRIPPED_DARK_OAK_WOOD,
                Material.STRIPPED_JUNGLE_WOOD,
                Material.STRIPPED_OAK_WOOD,
                Material.STRIPPED_SPRUCE_WOOD,
                Material.ACACIA_PLANKS,
                Material.BIRCH_PLANKS,
                Material.DARK_OAK_PLANKS,
                Material.JUNGLE_PLANKS,
                Material.OAK_PLANKS,
                Material.SPRUCE_PLANKS,
                Material.ANDESITE,
                Material.POLISHED_ANDESITE,
                Material.BASALT,
                Material.POLISHED_BASALT,
                Material.COBBLESTONE,
                Material.COBBLED_DEEPSLATE,
                Material.DEEPSLATE,
                Material.COPPER_BLOCK,
                Material.EXPOSED_COPPER,
                Material.WEATHERED_COPPER,
                Material.OXIDIZED_COPPER,
                Material.CUT_COPPER,
                Material.EXPOSED_CUT_COPPER,
                Material.WEATHERED_CUT_COPPER,
                Material.OXIDIZED_CUT_COPPER,
                Material.WAXED_COPPER_BLOCK,
                Material.WAXED_EXPOSED_COPPER,
                Material.WAXED_WEATHERED_COPPER,
                Material.WAXED_OXIDIZED_COPPER,
                Material.WAXED_CUT_COPPER,
                Material.WAXED_EXPOSED_CUT_COPPER,
                Material.WAXED_WEATHERED_CUT_COPPER,
                Material.WAXED_OXIDIZED_CUT_COPPER,
                Material.GLOWSTONE
        );
        for (Material material : includedMaterials){
            origin.add(new BuildingBlocks(advertiser, material));
        }
    }
}
