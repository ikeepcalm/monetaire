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

@Command("payfine")
@Permission("monetaire.payfine")
@Help("Використання: /payfine")
public class Payfine {

    @Default
    public static void payfine(Player player) {
        dev.ua.ikeepcalm.monetaire.entities.Player foundFined = playerDao.findByNickname(player);
        if (foundFined.getFine() == 0){
            MiniMessage mm = MiniMessage.miniMessage();
            StringBuilder sb = new StringBuilder();
            sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
            sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Нічого виплачувати, у вас немає активних штрафів! \n");
            sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
            Component parsed = mm.deserialize(sb.toString());
            player.sendMessage(parsed);
        } else {
            if (foundFined.getBalance()>=foundFined.getFine()){
                foundFined.setBalance(foundFined.getBalance()- foundFined.getFine());
                foundFined.setFine(0L);
                MinFin minFin = minfinDao.getMinfin();
                minFin.setBalance(minFin.getBalance()+ foundFined.getFine());
                minfinDao.save(minFin);
                SystemTx systemTx = new SystemTx();
                systemTx.setActionType(ActionType.PAYFINE);
                systemTx.setSuccessful(true);
                systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                systemTx.setSender(player.getName());
                systemTx.setAmount(Math.toIntExact(foundFined.getFine()));
                systemTxDao.save(systemTx);
                playerDao.save(foundFined);
                MiniMessage mm = MiniMessage.miniMessage();
                StringBuilder sb = new StringBuilder();
                sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
                sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Ви успішно виплатили свій штраф! \n");
                sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
                Component parsed = mm.deserialize(sb.toString());
                player.sendMessage(parsed);
            } else {
                SystemTx systemTx = new SystemTx();
                systemTx.setActionType(ActionType.PAYFINE);
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
