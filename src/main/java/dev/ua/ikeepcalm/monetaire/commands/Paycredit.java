package dev.ua.ikeepcalm.monetaire.commands;


import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Help;
import dev.jorel.commandapi.annotations.Permission;
import dev.ua.ikeepcalm.monetaire.entities.MinFin;
import dev.ua.ikeepcalm.monetaire.entities.transactions.SystemTx;
import dev.ua.ikeepcalm.monetaire.entities.transactions.source.ActionType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static dev.ua.ikeepcalm.monetaire.Monetaire.*;

@Command("paycredit")
@Permission("monetaire.paycredit")
@Help("Використання: /paycredit")
public class Paycredit {

    @Default
    public static void paycredit(Player player) {
        dev.ua.ikeepcalm.monetaire.entities.Player foundFined = playerDao.findByNickname(player);
        if (foundFined.getLoan() == 0){
            MiniMessage mm = MiniMessage.miniMessage();
            StringBuilder sb = new StringBuilder();
            sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
            sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Нічого виплачувати, у вас немає заборгованності! \n");
            sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
            Component parsed = mm.deserialize(sb.toString());
            player.sendMessage(parsed);
        } else {
            if (foundFined.getBalance()>=foundFined.getLoan()){
                MinFin minFin = minfinDao.getMinfin();
                minFin.setBalance(minFin.getBalance() + foundFined.getLoan());
                minFin.setWaitCredits(minFin.getWaitCredits() - foundFined.getLoan());
                foundFined.setBalance(foundFined.getBalance() - foundFined.getLoan());
                foundFined.setLoan(0L);
                minfinDao.save(minFin);
                playerDao.save(foundFined);
                SystemTx systemTx = new SystemTx();
                systemTx.setActionType(ActionType.PAYCREDIT);
                systemTx.setSuccessful(true);
                systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                systemTx.setSender(player.getName());
                systemTx.setAmount(Math.toIntExact(foundFined.getFine()));
                systemTxDao.save(systemTx);
                MiniMessage mm = MiniMessage.miniMessage();
                StringBuilder sb = new StringBuilder();
                sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
                sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Ви успішно позбулися своєї заборгованності! \n");
                sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
                Component parsed = mm.deserialize(sb.toString());
                player.sendMessage(parsed);
            } else {
                SystemTx systemTx = new SystemTx();
                systemTx.setActionType(ActionType.PAYCREDIT);
                systemTx.setSuccessful(false);
                systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                systemTx.setSender(player.getName());
                systemTx.setAmount(Math.toIntExact(foundFined.getFine()));
                systemTxDao.save(systemTx);
                MiniMessage mm = MiniMessage.miniMessage();
                StringBuilder sb = new StringBuilder();
                sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
                sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Недостатньо коштів на балансі! \n");
                sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Поповніть свій рахунок і спробуйте ще раз! \n");
                sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
                Component parsed = mm.deserialize(sb.toString());
                player.sendMessage(parsed);
            }
        }
    }
}
