package dev.ua.ikeepcalm.monetaire.gui.shop;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Help;
import dev.jorel.commandapi.annotations.Permission;
import dev.ua.ikeepcalm.monetaire.entities.Advertiser;
import dev.ua.ikeepcalm.monetaire.gui.shop.items.BackItem;
import dev.ua.ikeepcalm.monetaire.gui.shop.items.BalanceItem;
import dev.ua.ikeepcalm.monetaire.gui.shop.items.ForwardItem;
import dev.ua.ikeepcalm.monetaire.gui.shop.items.materials.*;
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
        if (advertiserDao.findByNickname(player) != null) {
            Advertiser advertiser = advertiserDao.findByNickname(player);
            Item border = new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("§r"));
            TextComponent windowComponent = Component.text("Фантомний магазин").color(TextColor.color(255, 8, 131));
            List<Item> items = new ArrayList<>();
            addBuildingBlocks(items, advertiser);
            addDecorationBlocks(items, advertiser);
            addSaplings(items, advertiser);
            addSlabsAndWalls(items, advertiser);
            addFlowers(items, advertiser);
            addSponge(items, advertiser);
            addCarpets(items, advertiser);
            addLogs(items, advertiser);
            addBookshelf(items, advertiser);
            addBeacon(items, advertiser);
            addLight(items, advertiser);
            addShulkers(items, advertiser);
            addFans(items, advertiser);
            addHeads(items, advertiser);
            addNetherStar(items, advertiser);
            addFireworks(items, advertiser);
            addTemplates(items, advertiser);
            addElytra(items, advertiser);
            addMusicDiscs(items, advertiser);

            Gui gui = PagedGui.items()
                    .setStructure(
                            "# # # # p # # # #",
                            "# x x x x x x x #",
                            "# x x x x x x x #",
                            "# # # < # > # # #")
                    .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                    .addIngredient('#', border)
                    .addIngredient('<', new BackItem())
                    .addIngredient('>', new ForwardItem())
                    .addIngredient('p', new BalanceItem(advertiser))
                    .setContent(items)
                    .build();

            Window window = Window.single()
                    .setViewer(player)
                    .setGui(gui)
                    .setTitle(new AdventureComponentWrapper(windowComponent))
                    .build();
            window.open();
        }
    }

    public static void addBuildingBlocks(List<Item> origin, Advertiser advertiser) {
        List<Material> includedMaterials = Arrays.asList(Material.STONE,
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
                Material.GLOWSTONE);
        for (Material material : includedMaterials) {
            origin.add(new Building(advertiser, material));
        }
    }

    public static void addDecorationBlocks(List<Item> origin, Advertiser advertiser) {
        List<Material> includedMaterials = Arrays.asList(Material.FLOWER_POT,
                Material.CAKE,
                Material.LANTERN,
                Material.SOUL_LANTERN,
                Material.TORCH,
                Material.SOUL_TORCH,
                Material.ITEM_FRAME,
                Material.BELL,
                Material.ARMOR_STAND,
                Material.SHROOMLIGHT,
                Material.CRYING_OBSIDIAN,
                Material.BONE_BLOCK,
                Material.MELON,
                Material.PUMPKIN,
                Material.HAY_BLOCK,
                Material.JACK_O_LANTERN,
                Material.CARVED_PUMPKIN);
        for (Material material : includedMaterials) {
            origin.add(new Decoration(advertiser, material));
        }
    }

    public static void addSaplings(List<Item> origin, Advertiser advertiser) {
        List<Material> includedMaterials = Arrays.asList(
                Material.ACACIA_SAPLING,
                Material.BIRCH_SAPLING,
                Material.CHERRY_SAPLING,
                Material.JUNGLE_SAPLING,
                Material.OAK_SAPLING,
                Material.DARK_OAK_SAPLING,
                Material.SPRUCE_SAPLING);
        for (Material material : includedMaterials) {
            origin.add(new Sapling(advertiser, material));
        }
    }

    public static void addSlabsAndWalls(List<Item> origin, Advertiser advertiser) {
        List<Material> includedSlabs = Arrays.asList(
                Material.ACACIA_SLAB,
                Material.ANDESITE_SLAB,
                Material.BIRCH_SLAB,
                Material.BRICK_SLAB,
                Material.COBBLESTONE_SLAB,
                Material.CRIMSON_SLAB,
                Material.CUT_RED_SANDSTONE_SLAB,
                Material.CUT_SANDSTONE_SLAB,
                Material.DARK_OAK_SLAB,
                Material.DARK_PRISMARINE_SLAB,
                Material.DIORITE_SLAB,
                Material.END_STONE_BRICK_SLAB,
                Material.GRANITE_SLAB,
                Material.JUNGLE_SLAB,
                Material.MOSSY_COBBLESTONE_SLAB,
                Material.MOSSY_STONE_BRICK_SLAB,
                Material.NETHER_BRICK_SLAB,
                Material.OAK_SLAB,
                Material.PETRIFIED_OAK_SLAB,
                Material.POLISHED_ANDESITE_SLAB,
                Material.POLISHED_BLACKSTONE_BRICK_SLAB,
                Material.POLISHED_BLACKSTONE_SLAB,
                Material.POLISHED_DIORITE_SLAB,
                Material.POLISHED_GRANITE_SLAB,
                Material.PRISMARINE_BRICK_SLAB,
                Material.PRISMARINE_SLAB,
                Material.PURPUR_SLAB,
                Material.QUARTZ_SLAB,
                Material.RED_NETHER_BRICK_SLAB,
                Material.RED_SANDSTONE_SLAB,
                Material.SANDSTONE_SLAB,
                Material.SMOOTH_QUARTZ_SLAB,
                Material.SMOOTH_RED_SANDSTONE_SLAB,
                Material.SMOOTH_SANDSTONE_SLAB,
                Material.SMOOTH_STONE_SLAB,
                Material.SPRUCE_SLAB,
                Material.STONE_BRICK_SLAB,
                Material.WARPED_SLAB);
        for (Material material : includedSlabs) {
            origin.add(new Slab(advertiser, material));
        }

        List<Material> includedWalls = Arrays.asList(
                Material.ANDESITE_WALL,
                Material.BRICK_WALL,
                Material.COBBLESTONE_WALL,
                Material.DIORITE_WALL,
                Material.END_STONE_BRICK_WALL,
                Material.GRANITE_WALL,
                Material.MOSSY_COBBLESTONE_WALL,
                Material.MOSSY_STONE_BRICK_WALL,
                Material.NETHER_BRICK_WALL,
                Material.PRISMARINE_WALL,
                Material.RED_NETHER_BRICK_WALL,
                Material.RED_SANDSTONE_WALL,
                Material.SANDSTONE_WALL,
                Material.STONE_BRICK_WALL,
                Material.ACACIA_FENCE,
                Material.BIRCH_FENCE,
                Material.DARK_OAK_FENCE,
                Material.JUNGLE_FENCE,
                Material.OAK_FENCE,
                Material.SPRUCE_FENCE
        );
        for (Material material : includedWalls) {
            origin.add(new Wall(advertiser, material));
        }
    }

    public static void addCarpets(List<Item> origin, Advertiser advertiser) {
        List<Material> includedMaterials = Arrays.asList(
                Material.BLACK_CARPET,
                Material.BLUE_CARPET,
                Material.BROWN_CARPET,
                Material.CYAN_CARPET,
                Material.GRAY_CARPET,
                Material.GREEN_CARPET,
                Material.LIGHT_BLUE_CARPET,
                Material.LIGHT_GRAY_CARPET,
                Material.LIME_CARPET,
                Material.MAGENTA_CARPET,
                Material.ORANGE_CARPET,
                Material.PINK_CARPET,
                Material.PURPLE_CARPET,
                Material.RED_CARPET,
                Material.WHITE_CARPET,
                Material.YELLOW_CARPET);
        for (Material material : includedMaterials) {
            origin.add(new Carpet(advertiser, material));
        }
    }

    public static void addLogs(List<Item> origin, Advertiser advertiser) {
        List<Material> includedMaterials = Arrays.asList(
                Material.ACACIA_LOG,
                Material.BIRCH_LOG,
                Material.DARK_OAK_LOG,
                Material.JUNGLE_LOG,
                Material.OAK_LOG,
                Material.SPRUCE_LOG,
                Material.CRIMSON_STEM,
                Material.WARPED_STEM);
        for (Material material : includedMaterials) {
            origin.add(new Log(advertiser, material));
        }
    }

    public static void addSponge(List<Item> origin, Advertiser advertiser) {
        List<Material> includedMaterials = Arrays.asList(
                Material.SPONGE,
                Material.WET_SPONGE);
        for (Material material : includedMaterials) {
            origin.add(new Sponge(advertiser, material));
        }
    }

    public static void addFlowers(List<Item> origin, Advertiser advertiser) {
        List<Material> includedMaterials = Arrays.asList(
                Material.ALLIUM,
                Material.AZURE_BLUET,
                Material.BLUE_ORCHID,
                Material.CORNFLOWER,
                Material.DANDELION,
                Material.LILY_OF_THE_VALLEY,
                Material.ORANGE_TULIP,
                Material.OXEYE_DAISY,
                Material.PINK_TULIP,
                Material.POPPY,
                Material.RED_TULIP,
                Material.WHITE_TULIP,
                Material.WITHER_ROSE);
        for (Material material : includedMaterials) {
            origin.add(new Flower(advertiser, material));
        }
    }

    public static void addBookshelf(List<Item> origin, Advertiser advertiser) {
        List<Material> includedMaterials = Arrays.asList(
                Material.BOOKSHELF,
                Material.CHISELED_BOOKSHELF);
        for (Material material : includedMaterials) {
            origin.add(new Bookshelf(advertiser, material));
        }
    }

    public static void addBeacon(List<Item> origin, Advertiser advertiser) {
        List<Material> includedMaterials = Arrays.asList(
                Material.BEACON);
        for (Material material : includedMaterials) {
            origin.add(new Beacon(advertiser, material));
        }
    }

    public static void addLight(List<Item> origin, Advertiser advertiser) {
        List<Material> includedMaterials = Arrays.asList(
                Material.LIGHT);
        for (Material material : includedMaterials) {
            origin.add(new Beacon(advertiser, material));
        }
    }

    public static void addShulkers(List<Item> origin, Advertiser advertiser) {
        List<Material> includedMaterials = Arrays.asList(
                Material.SHULKER_BOX,
                Material.WHITE_SHULKER_BOX,
                Material.ORANGE_SHULKER_BOX,
                Material.MAGENTA_SHULKER_BOX,
                Material.LIGHT_BLUE_SHULKER_BOX,
                Material.YELLOW_SHULKER_BOX,
                Material.LIME_SHULKER_BOX,
                Material.PINK_SHULKER_BOX,
                Material.GRAY_SHULKER_BOX,
                Material.LIGHT_GRAY_SHULKER_BOX,
                Material.CYAN_SHULKER_BOX,
                Material.PURPLE_SHULKER_BOX,
                Material.BLUE_SHULKER_BOX,
                Material.BROWN_SHULKER_BOX,
                Material.GREEN_SHULKER_BOX,
                Material.RED_SHULKER_BOX,
                Material.BLACK_SHULKER_BOX);
        for (Material material : includedMaterials) {
            origin.add(new Shulker(advertiser, material));
        }
    }

    public static void addFans(List<Item> origin, Advertiser advertiser) {
        List<Material> includedMaterials = Arrays.asList(
                Material.SCAFFOLDING,
                Material.PISTON,
                Material.STICKY_PISTON,
                Material.BELL,
                Material.SMOKER,
                Material.BLAST_FURNACE,
                Material.FURNACE,
                Material.CAMPFIRE,
                Material.SOUL_CAMPFIRE);
        for (Material material : includedMaterials) {
            origin.add(new Fan(advertiser, material));
        }
    }

    public static void addHeads(List<Item> origin, Advertiser advertiser) {
        List<Material> includedMaterials = Arrays.asList(
                Material.CREEPER_HEAD,
                Material.DRAGON_HEAD,
                Material.PLAYER_HEAD,
                Material.ZOMBIE_HEAD,
                Material.SKELETON_SKULL,
                Material.WITHER_SKELETON_SKULL);
        for (Material material : includedMaterials) {
            origin.add(new Head(advertiser, material));
        }
    }

    public static void addNetherStar(List<Item> origin, Advertiser advertiser) {
        List<Material> includedMaterials = Arrays.asList(
                Material.NETHER_STAR);
        for (Material material : includedMaterials) {
            origin.add(new NetherStar(advertiser, material));
        }
    }

    public static void addFireworks(List<Item> origin, Advertiser advertiser) {
        List<Material> includedMaterials = Arrays.asList(
                Material.FIREWORK_ROCKET,
                Material.FIREWORK_STAR);
        for (Material material : includedMaterials) {
            origin.add(new Firework(advertiser, material));
        }
    }

    public static void addTemplates(List<Item> origin, Advertiser advertiser) {
        List<Material> includedMaterials = Arrays.asList(
                Material.FILLED_MAP,
                Material.MAP);
        for (Material material : includedMaterials) {
            origin.add(new Template(advertiser, material));
        }
    }

    public static void addElytra(List<Item> origin, Advertiser advertiser) {
        List<Material> includedMaterials = Arrays.asList(
                Material.ELYTRA);
        for (Material material : includedMaterials) {
            origin.add(new Elytra(advertiser, material));
        }
    }

    public static void addMusicDiscs(List<Item> origin, Advertiser advertiser) {
        List<Material> includedMaterials = Arrays.asList(
                Material.MUSIC_DISC_11,
                Material.MUSIC_DISC_13,
                Material.MUSIC_DISC_BLOCKS,
                Material.MUSIC_DISC_CAT,
                Material.MUSIC_DISC_CHIRP,
                Material.MUSIC_DISC_FAR,
                Material.MUSIC_DISC_MALL,
                Material.MUSIC_DISC_MELLOHI,
                Material.MUSIC_DISC_PIGSTEP,
                Material.MUSIC_DISC_STAL,
                Material.MUSIC_DISC_STRAD,
                Material.MUSIC_DISC_WAIT,
                Material.MUSIC_DISC_WARD);
        for (Material material : includedMaterials) {
            origin.add(new MusicDisc(advertiser, material));
        }
    }
}
