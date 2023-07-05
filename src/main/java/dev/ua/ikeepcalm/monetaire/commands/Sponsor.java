package dev.ua.ikeepcalm.monetaire.commands;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.arguments.AIntegerArgument;
import dev.ua.ikeepcalm.monetaire.entities.MinFin;
import dev.ua.ikeepcalm.monetaire.entities.transactions.SystemTx;
import dev.ua.ikeepcalm.monetaire.entities.transactions.source.ActionType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static dev.ua.ikeepcalm.monetaire.Monetaire.*;

@Command("sponsor")
@Permission("monetaire.sponsor")
public class Sponsor {

    @Default
    public static void sponsor(Player player, @AIntegerArgument int amount){
        if (amount> 0 && amount < 25000){
            dev.ua.ikeepcalm.monetaire.entities.Player depositPlayer = playerDao.findByNickname(player);
            if (depositPlayer.getFine() > 0){
                MiniMessage mm = MiniMessage.miniMessage();
                StringBuilder sb = new StringBuilder();
                sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
                sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> <#FFFFFF>Ви маєте сплатити штраф!").append("\n");
                sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Інакше ви не зможете повноцінно користуватися системою!").append("\n");
                sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Сума штрафів: <#55FFFF>").append(depositPlayer.getFine()).append(" ДР\n");
                sb.append("<bold><#5555FF>-----------------------------------------</bold>");
                Component parsed = mm.deserialize(sb.toString());
                player.sendMessage(parsed);
            } else {
                if (depositPlayer.getBalance() >= amount){
                    depositPlayer.setBalance((depositPlayer.getBalance() - amount));
                    depositPlayer.setSponsored(depositPlayer.getSponsored()+amount);
                    playerDao.save(depositPlayer);
                    SystemTx systemTx = new SystemTx();
                    systemTx.setActionType(ActionType.SPONSOR);
                    systemTx.setSender(depositPlayer.getNickname());
                    systemTx.setAmount(amount);
                    systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                    systemTx.setSuccessful(true);
                    systemTxDao.save(systemTx);
                    MinFin minFin = minfinDao.getMinfin();
                    minFin.setBalance(minFin.getBalance()+amount);
                    minfinDao.save(minFin);
                    MiniMessage mm = MiniMessage.miniMessage();
                    StringBuilder sb = new StringBuilder();
                    sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
                    sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Ви успішно проспонсорували міністерство фінансів на <#55FFFF>").append(amount).append(" ДР\n");
                    sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
                    Component parsed = mm.deserialize(sb.toString());
                    StringBuilder sb1 = new StringBuilder();
                    sb1.append("<bold><#5555FF>-----------------------------------------</bold>\n");
                    sb1.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Гравець <#FFAA00>").append(player.getName()).append("<#FFFFFF> проспонсорував МінФін на <#55FFFF>").append(amount).append(" ДР!\n");
                    sb1.append("<bold><#5555FF>-----------------------------------------</bold>");
                    Component parsed1 = mm.deserialize(sb1.toString());
                    player.sendMessage(parsed);
                    Bukkit.broadcast(parsed1);
                } else {
                    SystemTx systemTx = new SystemTx();
                    systemTx.setActionType(ActionType.SPONSOR);
                    systemTx.setSender(depositPlayer.getNickname());
                    systemTx.setAmount(amount);
                    systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                    systemTx.setSuccessful(false);
                    systemTxDao.save(systemTx);
                    MiniMessage mm = MiniMessage.miniMessage();
                    StringBuilder sb = new StringBuilder();
                    sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
                    sb.append("<bold><#5555FF>BANK</bold> <#AAAAAA>> <#FF5555>Недостатня кількість коштів на балансі!");
                    sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Актуальний рахунок: <#55FFFF>").append(depositPlayer.getBalance()).append(" ДР\n");
                    sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
                    Component parsed = mm.deserialize(sb.toString());
                    player.sendMessage(parsed);
                }
            }
        } else {
            MiniMessage mm = MiniMessage.miniMessage();
            StringBuilder sb = new StringBuilder();
            sb.append("<bold><#5555FF>BANK</bold> <#AAAAAA> > <#FF5555>Сума спонсорування не може бути меньше 1-го, та більше 2500 ДР за один раз ДР!");
            Component parsed = mm.deserialize(sb.toString());
            player.sendMessage(parsed);
        }

    }
}
