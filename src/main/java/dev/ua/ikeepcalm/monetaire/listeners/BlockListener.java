package dev.ua.ikeepcalm.monetaire.listeners;

import dev.ua.ikeepcalm.monetaire.entities.EcoUser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import static dev.ua.ikeepcalm.monetaire.Monetaire.ecoPlayerDao;

public class BlockListener implements Listener {

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("monetaire.autodeposit")) {
            Block block = event.getBlock();
            if (block.getType() == Material.DIAMOND_ORE || block.getType() == Material.DEEPSLATE_DIAMOND_ORE) {
                ItemStack item = player.getInventory().getItemInMainHand();
                if (item.getType() == Material.DIAMOND_PICKAXE || item.getType() == Material.NETHERITE_PICKAXE) {
                    if (item.containsEnchantment(Enchantment.SILK_TOUCH)) {
                        EcoUser foundEcoUser = ecoPlayerDao.findByNickname(player);
                        if (foundEcoUser.getAutoDeposit()) {
                            if (foundEcoUser.getCard().getBalance() >= 1344){
                                TextComponent noFreeSpace = Component.text("Недостатньо місця в сховищі! Звільніть місце, або отримайте збільшений ліміт").color(TextColor.color(140, 200, 230));
                                player.sendMessage(noFreeSpace);
                            } else {
                                foundEcoUser.getCard().setBalance(foundEcoUser.getCard().getBalance() + 1);
                                ecoPlayerDao.save(foundEcoUser);
                                event.setDropItems(false);
                            }
                        }
                    }
                }
            }
        }
    }
}