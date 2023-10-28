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

@Command("withcoin")
@Permission("monetaire.player")
public class Withcoin {

    @Default
    public static void withdraw(Player player, @AIntegerArgument int amount) {
        if (amount> 0 && amount < 3000){
            EcoUser withdrawEcoUser = ecoPlayerDao.findByNickname(player);
            if (withdrawEcoUser.getCard() == null) {
                ChatUtil.sendMessage(player,
                        "У вас немає картки!",
                        "Спочатку виконайте ➜ /card");
            }
            if (withdrawEcoUser.getCard().getFine() > 0) {
                ChatUtil.sendMessage(player,
                        "Ви маєте сплатити штраф!",
                        "Інакше ви не зможете повноцінно користуватися банківською системою!",
                        "Сума штрафів: <#55FFFF>" + withdrawEcoUser.getCard().getFine() + " ДР"
                );
            } else {
                if (withdrawEcoUser.getCard().getCoins() >= amount){
                    if (player.getInventory().firstEmpty() == -1){
                        ChatUtil.sendMessage(player,
                                "Неможливо зняти, немає вільного місця!"
                        );
                    } else {
                        ItemStack itemStack = new ItemStack(Material.CLOCK, amount);
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        itemMeta.setCustomModelData(1633212000);
                        itemMeta.addEnchant(Enchantment.DURABILITY, 1, false);
                        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        itemMeta.setDisplayName(ChatColor.RESET + "" + ChatColor.YELLOW + "Аур");
                        itemStack.setItemMeta(itemMeta);
                        player.getWorld().dropItemNaturally(player.getLocation().add(0,1,0), itemStack);
                        withdrawEcoUser.getCard().setCoins((withdrawEcoUser.getCard().getCoins() - amount));
                        ecoPlayerDao.save(withdrawEcoUser);
                        SystemTx systemTx = new SystemTx();
                        systemTx.setActionType(ActionType.WITHCOIN);
                        systemTx.setSender(withdrawEcoUser.getNickname());
                        systemTx.setAmount(amount);
                        systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                        systemTx.setSuccessful(true);
                        systemTx.setMomentBalance("MainCoins: " + withdrawEcoUser.getCard().getCoins()
                                + " | Credits: "+ withdrawEcoUser.getCard().getLoan() +" | Fines: " + withdrawEcoUser.getCard().getFine());
                        systemTxDao.save(systemTx);
                        ChatUtil.sendMessage(player,
                                "Успішне зняття <#55FFFF>" + amount + " AUR",
                                "Аури: <#55FFFF>" + withdrawEcoUser.getCard().getCoins() + " AUR");
                    }
                }else {
                    SystemTx systemTx = new SystemTx();
                    systemTx.setActionType(ActionType.WITHCOIN);
                    systemTx.setSender(withdrawEcoUser.getNickname());
                    systemTx.setAmount(amount);
                    systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                    systemTx.setSuccessful(false);
                    systemTx.setMomentBalance("MainCoins: " + withdrawEcoUser.getCard().getCoins()
                            + " | Credits: "+ withdrawEcoUser.getCard().getLoan() +" | Fines: " + withdrawEcoUser.getCard().getFine());
                    systemTxDao.save(systemTx);
                    ChatUtil.sendMessage(player,
                            "Недостатньо коштів!",
                            "Аури: <#55FFFF>" + withdrawEcoUser.getCard().getCoins() + " AUR");
                }
            }
        } else {
            ChatUtil.sendMessage(player,
                    "Сума зняття не може бути меньше 1-го, та більше 3000 AUR! Спробуйте ще раз, але з правильними даними!");
        }
    }
}
