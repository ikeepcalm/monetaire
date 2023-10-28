package dev.ua.ikeepcalm.monetaire.commands.common.coins;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.arguments.AIntegerArgument;
import dev.ua.ikeepcalm.monetaire.entities.EcoUser;
import dev.ua.ikeepcalm.monetaire.entities.transactions.SystemTx;
import dev.ua.ikeepcalm.monetaire.entities.transactions.source.ActionType;
import dev.ua.ikeepcalm.monetaire.utils.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static dev.ua.ikeepcalm.monetaire.Monetaire.ecoPlayerDao;
import static dev.ua.ikeepcalm.monetaire.Monetaire.systemTxDao;

@Command("depcoin")
@Permission("monetaire.player")
public class Depcoin {

    @Default
    public static void deposit(Player player, @AIntegerArgument int amount) {
        EcoUser depositEcoUser = ecoPlayerDao.findByNickname(player);
        if (depositEcoUser.getCard() == null) {
            ChatUtil.sendMessage(player,
                    "У вас немає картки!",
                    "Спочатку виконайте ➜ /card");
        } else {
            ItemStack itemStack = new ItemStack(Material.CLOCK, amount);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setCustomModelData(1633212000);
            itemMeta.addEnchant(Enchantment.DURABILITY, 1, false);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.setDisplayName(ChatColor.RESET + "" + ChatColor.YELLOW + "Аур");
            itemStack.setItemMeta(itemMeta);
            if (player.getInventory().containsAtLeast(itemStack, amount)) {
                itemStack.setAmount(amount);
                player.getInventory().removeItemAnySlot(itemStack);
                depositEcoUser.getCard().setCoins((depositEcoUser.getCard().getCoins() + itemStack.getAmount()));
                ecoPlayerDao.save(depositEcoUser);
                SystemTx systemTx = new SystemTx();
                systemTx.setActionType(ActionType.DEPCOIN);
                systemTx.setSender(depositEcoUser.getNickname());
                systemTx.setAmount(itemStack.getAmount());
                systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                systemTx.setMomentBalance("MainCoins: " + depositEcoUser.getCard().getCoins()
                        + " | Credits: " + depositEcoUser.getCard().getLoan() + " | Fines: " + depositEcoUser.getCard().getFine());
                systemTx.setSuccessful(true);
                systemTxDao.save(systemTx);
                ChatUtil.sendMessage(player,
                        "Успішне поповнення на <#55FFFF>" + itemStack.getAmount() + " AUR",
                        "Ваш рахунок після операції: <#55FFFF>" + depositEcoUser.getCard().getCoins() + " AUR"
                );
            } else {
                ChatUtil.sendMessage(player, "Не вдалося розпізнати відбиток AUR!" , "Переконайтесь, що у вашому інвентарі наявна", "вказана сума для поповнення рахунку");
            }
        }
    }
}
