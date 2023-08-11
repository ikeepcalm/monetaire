package dev.ua.ikeepcalm.monetaire.commands.forusers;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Help;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.arguments.AIntegerArgument;
import dev.ua.ikeepcalm.monetaire.entities.User;
import dev.ua.ikeepcalm.monetaire.entities.transactions.SystemTx;
import dev.ua.ikeepcalm.monetaire.entities.transactions.source.ActionType;
import dev.ua.ikeepcalm.monetaire.utils.ChatUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static dev.ua.ikeepcalm.monetaire.Monetaire.playerDao;
import static dev.ua.ikeepcalm.monetaire.Monetaire.systemTxDao;

@Command("deposit")
@Permission("monetaire.deposit")
@Help("Використання: /deposit <к-сть діамантової руди>")
public class Deposit {

    @Default
    public static void deposit(Player player, @AIntegerArgument int amount) {
        if (amount > 0 && amount < 3000) {
            User depositUser = playerDao.findByNickname(player);
            if (depositUser.getCard() == null) {
                ChatUtil.sendMessage(player,
                        "У вас немає картки!",
                        "Для отримання пройдіть у банк ➜ 41, 65, -17 ( Спавн )");
            } else {
                if (player.getInventory().containsAtLeast(new ItemStack(Material.DIAMOND_ORE), amount)) {
                    if (depositUser.getCard().getBalance() >= 1344) {
                        ChatUtil.sendMessage(player,
                                "Недостатньо місця в сховищі! Звільніть місце, або отримайте збільшений ліміт");
                    } else {
                        player.getInventory().removeItemAnySlot(new ItemStack(Material.DIAMOND_ORE, amount));
                        depositUser.getCard().setBalance((depositUser.getCard().getBalance() + amount));
                        playerDao.save(depositUser);
                        SystemTx systemTx = new SystemTx();
                        systemTx.setActionType(ActionType.DEPOSIT);
                        systemTx.setSender(depositUser.getNickname());
                        systemTx.setAmount(amount);
                        systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                        systemTx.setSuccessful(true);
                        systemTxDao.save(systemTx);
                        ChatUtil.sendMessage(player,
                                "Успішне поповнення на <#55FFFF>" + amount + " ДР",
                                "Ваш рахунок після операції: <#55FFFF>" + depositUser.getCard().getBalance() + " ДР"
                                );
                    }
                } else if (player.getInventory().containsAtLeast(new ItemStack(Material.DEEPSLATE_DIAMOND_ORE), amount)) {
                    if (depositUser.getCard().getBalance() + amount >= 1344) {
                        ChatUtil.sendMessage(player,
                                "У вас недостатньо місця у сховищі!",
                                "Ви не можете поповнити свій рахунок"
                        );
                    } else {
                        player.getInventory().removeItemAnySlot(new ItemStack(Material.DEEPSLATE_DIAMOND_ORE, amount));
                        depositUser.getCard().setBalance((depositUser.getCard().getBalance() + amount));
                        playerDao.save(depositUser);
                        SystemTx systemTx = new SystemTx();
                        systemTx.setActionType(ActionType.DEPOSIT);
                        systemTx.setSender(depositUser.getNickname());
                        systemTx.setAmount(amount);
                        systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                        systemTx.setSuccessful(true);
                        systemTxDao.save(systemTx);
                        ChatUtil.sendMessage(player,
                                "Успішне поповнення на <#55FFFF>" + amount + " ДР",
                                "Ваш рахунок після операції: <#55FFFF>" + depositUser.getCard().getBalance() + " ДР"
                        );
                    }
                } else {
                    SystemTx systemTx = new SystemTx();
                    systemTx.setActionType(ActionType.DEPOSIT);
                    systemTx.setSender(depositUser.getNickname());
                    systemTx.setAmount(amount);
                    systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                    systemTx.setSuccessful(false);
                    systemTxDao.save(systemTx);
                    ChatUtil.sendMessage(player,
                            "Недостатня кількість ДР у інвентарі! Спробуйте із меншою сумою"
                    );
                }
            }
        } else {
            ChatUtil.sendMessage(player,
                    "Сума поповнення не може бути меньше 1-го, та більше 3000 ДР!"
            );
        }
    }
}
