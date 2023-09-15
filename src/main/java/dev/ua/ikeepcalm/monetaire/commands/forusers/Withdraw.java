package dev.ua.ikeepcalm.monetaire.commands.forusers;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
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

@Command("withdraw")
@Permission("monetaire.withdraw")
public class Withdraw {

    @Default
    public static void withdraw(Player player, @AIntegerArgument int amount) {
        if (amount> 0 && amount < 3000){
            User withdrawUser = playerDao.findByNickname(player);
            if (withdrawUser.getCard() == null) {
                ChatUtil.sendMessage(player,
                        "У вас немає картки!",
                        "Для отримання пройдіть у банк ➜ 41, 65, -17 ( Спавн )");
            }
            if (withdrawUser.getCard().getFine() > 0) {
                ChatUtil.sendMessage(player,
                        "Ви маєте сплатити штраф!",
                        "Інакше ви не зможете повноцінно користуватися банківською системою!",
                        "Сума штрафів: <#55FFFF>" + withdrawUser.getCard().getFine() + " ДР"
                );
            } else {
                if (withdrawUser.getCard().getBalance() >= amount){
                    if (player.getInventory().firstEmpty() == -1){
                        ChatUtil.sendMessage(player,
                                "Неможливо зняти, немає вільного місця!"
                        );
                    } else {
                        ItemStack itemStack = new ItemStack(Material.DEEPSLATE_DIAMOND_ORE, amount);
                        player.getWorld().dropItemNaturally(player.getLocation().add(0,1,0), itemStack);
                        withdrawUser.getCard().setBalance((withdrawUser.getCard().getBalance() - amount));
                        playerDao.save(withdrawUser);
                        SystemTx systemTx = new SystemTx();
                        systemTx.setActionType(ActionType.WITHDRAW);
                        systemTx.setSender(withdrawUser.getNickname());
                        systemTx.setAmount(amount);
                        systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                        systemTx.setSuccessful(true);
                        systemTx.setMomentBalance("MainBalance: " + withdrawUser.getCard().getBalance()
                                + " | Credits: "+ withdrawUser.getCard().getLoan() +" | Fines: " + withdrawUser.getCard().getFine());
                        systemTxDao.save(systemTx);
                        ChatUtil.sendMessage(player,
                                "Успішне зняття <#55FFFF>" + amount + " ДР!",
                                "Актуальний рахунок: <#55FFFF>" + withdrawUser.getCard().getBalance() + " ДР");
                    }
                }else {
                    SystemTx systemTx = new SystemTx();
                    systemTx.setActionType(ActionType.WITHDRAW);
                    systemTx.setSender(withdrawUser.getNickname());
                    systemTx.setAmount(amount);
                    systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                    systemTx.setSuccessful(false);
                    systemTx.setMomentBalance("MainBalance: " + withdrawUser.getCard().getBalance()
                            + " | Credits: "+ withdrawUser.getCard().getLoan() +" | Fines: " + withdrawUser.getCard().getFine());
                    systemTxDao.save(systemTx);
                    ChatUtil.sendMessage(player,
                            "Недостатньо коштів!",
                            "Актуальний рахунок: <#55FFFF>" + withdrawUser.getCard().getBalance() + " ДР");
                }
            }
        } else {
            ChatUtil.sendMessage(player,
                    "Сума зняття не може бути меньше 1-го, та більше 3000 ДР! Спробуйте ще раз, але з правильними даними!");
        }
    }
}
