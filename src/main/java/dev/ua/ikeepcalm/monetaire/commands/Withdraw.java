package dev.ua.ikeepcalm.monetaire.commands;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.arguments.AIntegerArgument;
import dev.ua.ikeepcalm.monetaire.entities.transactions.SystemTx;
import dev.ua.ikeepcalm.monetaire.entities.transactions.source.ActionType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
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
            dev.ua.ikeepcalm.monetaire.entities.Player depositPlayer = playerDao.findByNickname(player.getName());
            if (depositPlayer.getFine() > 0) {
                MiniMessage mm = MiniMessage.miniMessage();
                StringBuilder sb = new StringBuilder();
                sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
                sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> <#FFFFFF>Ви маєте сплатити штраф!").append("\n");
                sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> <#FFFFFF>Інакше ви не зможете повноцінно користуватися системою!").append("\n");
                sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Сума штрафів: <#55FFFF>").append(depositPlayer.getFine()).append(" ДР\n");
                sb.append("<bold><#5555FF>-----------------------------------------</bold>");
                Component parsed = mm.deserialize(sb.toString());
                player.sendMessage(parsed);
            } else {
                if (depositPlayer.getBalance() >= amount){
                    player.getInventory().addItem(new ItemStack(Material.DEEPSLATE_DIAMOND_ORE, amount));
                    depositPlayer.setBalance((depositPlayer.getBalance() - amount));
                    playerDao.save(depositPlayer);
                    SystemTx systemTx = new SystemTx();
                    systemTx.setActionType(ActionType.WITHDRAW);
                    systemTx.setSender(depositPlayer.getNickname());
                    systemTx.setAmount(amount);
                    systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                    systemTx.setSuccessful(true);
                    systemTxDao.save(systemTx);
                    MiniMessage mm = MiniMessage.miniMessage();
                    StringBuilder sb = new StringBuilder();
                    sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
                    sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Успішне зняття <#55FFFF>").append(amount).append(" ДР!\n");
                    sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Актуальний рахунок: <#55FFFF>").append(depositPlayer.getBalance()).append(" ДР\n");
                    sb.append("<bold><#5555FF>-----------------------------------------</bold>");
                    Component parsed = mm.deserialize(sb.toString());
                    player.sendMessage(parsed);
                }else {
                    SystemTx systemTx = new SystemTx();
                    systemTx.setActionType(ActionType.WITHDRAW);
                    systemTx.setSender(depositPlayer.getNickname());
                    systemTx.setAmount(amount);
                    systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                    systemTx.setSuccessful(false);
                    systemTxDao.save(systemTx);
                    MiniMessage mm = MiniMessage.miniMessage();
                    StringBuilder sb = new StringBuilder();
                    sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
                    sb.append("<bold><#5555FF>BANK</bold> <#AAAAAA>> <#FF5555>У вас недостатньо коштів!\n");
                    sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Актуальний рахунок: <#55FFFF>").append(depositPlayer.getBalance()).append(" ДР\n");
                    sb.append("<bold><#5555FF>-----------------------------------------</bold>");
                    Component parsed = mm.deserialize(sb.toString());
                    player.sendMessage(parsed);
                }
            }
        } else {
            MiniMessage mm = MiniMessage.miniMessage();
            StringBuilder sb = new StringBuilder();
            sb.append("<bold><#5555FF>BANK</bold> <#AAAAAA> > <#FF5555>Сума зняття не може бути меньше 1-го, та більше 3000 ДР! Спробуйте ще раз, але з правильними даними!");
            Component parsed = mm.deserialize(sb.toString());
            player.sendMessage(parsed);
        }
    }
}
