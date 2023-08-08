package dev.ua.ikeepcalm.monetaire.commands;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Help;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.arguments.AIntegerArgument;
import dev.jorel.commandapi.annotations.arguments.APlayerArgument;
import dev.jorel.commandapi.annotations.arguments.AStringArgument;
import dev.ua.ikeepcalm.monetaire.entities.transactions.SystemTx;
import dev.ua.ikeepcalm.monetaire.entities.transactions.source.ActionType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static dev.ua.ikeepcalm.monetaire.Monetaire.playerDao;
import static dev.ua.ikeepcalm.monetaire.Monetaire.systemTxDao;

@Command("setfine")
@Permission("monetaire.setcredit")
@Help("Використання: /setfine <гравець> <к-сть діамантової руди> <тариф>")
public class Setcredit {

    @Default
    public static void setfine(Player finer, @APlayerArgument Player fined, @AIntegerArgument int amount, @AStringArgument String tarif) {
        if (amount > 0){
            if (tarif.equals("oneweek") || tarif.equals("twoweek") || tarif.equals("threeweek")){
                dev.ua.ikeepcalm.monetaire.entities.Player foundFined = playerDao.findByNickname(fined);
                switch (tarif){
                    case "oneweek" -> {
                        long finalAmount = (long) (amount + amount * 0.02);
                        foundFined.setLoan(finalAmount);
                    }
                    case "twoweek" -> {
                        long finalAmount = (long) (amount + amount * 0.03);
                        foundFined.setLoan(finalAmount);
                    }
                    case "threeweek" -> {
                        long finalAmount = (long) (amount + amount * 0.04);
                        foundFined.setLoan(finalAmount);
                    }
                }
                playerDao.save(foundFined);
                SystemTx systemTx = new SystemTx();
                systemTx.setActionType(ActionType.SETCREDIT);
                systemTx.setSuccessful(true);
                systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                systemTx.setSender(fined.getName());
                systemTx.setAmount(amount);
                systemTxDao.save(systemTx);
                MiniMessage mm = MiniMessage.miniMessage();
                StringBuilder sb = new StringBuilder();
                sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
                sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Ви надали позику на суму <#55FFFF>").append(foundFined.getLoan()).append(" ДР <#FFFFFF>гравцю <#FFAA00>").append(fined.getName()).append("\n");
                sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
                Component parsed = mm.deserialize(sb.toString());
                finer.sendMessage(parsed);
            } else {
                MiniMessage mm = MiniMessage.miniMessage();
                StringBuilder sb = new StringBuilder();
                sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
                sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Помилка у написанні тарифу! <#55FFFF>").append("\n");
                sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
                Component parsed = mm.deserialize(sb.toString());
                finer.sendMessage(parsed);
            }
        } else {
            MiniMessage mm = MiniMessage.miniMessage();
            StringBuilder sb = new StringBuilder();
            sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
            sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Позика не може бути від'ємною! <#55FFFF>").append("\n");
            sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
            Component parsed = mm.deserialize(sb.toString());
            finer.sendMessage(parsed);
        }
    }
}
