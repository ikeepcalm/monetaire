package dev.ua.ikeepcalm.monetaire.listeners;

import dev.ua.ikeepcalm.monetaire.Monetaire;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static dev.ua.ikeepcalm.monetaire.gui.shop.utils.WatermarkUtil.hasWatermark;

public class ItemListener implements Listener {

    @EventHandler
    public void onPlayerPickingUpPhantomItem(EntityPickupItemEvent event){
        if (event.getEntity() instanceof Player){
            Player player = (org.bukkit.entity.Player) event.getEntity();
            if (!player.hasPermission("monetaire.shop")){
                ItemStack itemStack = event.getItem().getItemStack();
                if (hasWatermark(itemStack)){
                    Monetaire.getPlugin(Monetaire.class).getLogger().warning(player.getName() + " has phantom items not allowed to have!");
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem != null && clickedItem.getType() != Material.AIR) {
            if (hasWatermark(clickedItem) && !player.hasPermission("monetaire.shop")) {
                event.setCancelled(true);
                Monetaire.getPlugin(Monetaire.class).getLogger().warning(player.getName() + " has phantom items not allowed to have!");
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (itemInHand.getType() != Material.AIR) {
            if (hasWatermark(itemInHand) && !player.hasPermission("monetaire.shop")) {
                event.setCancelled(true);
                Monetaire.getPlugin(Monetaire.class).getLogger().warning(player.getName() + " has phantom items not allowed to have!");
            }
        }
    }
}
