package dev.ua.ikeepcalm.monetaire.commands;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.arguments.AIntegerArgument;
import dev.jorel.commandapi.annotations.arguments.APlayerArgument;
import dev.ua.ikeepcalm.monetaire.entities.transactions.PlayerTx;
import dev.ua.ikeepcalm.monetaire.entities.transactions.source.ActionType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static dev.ua.ikeepcalm.monetaire.Monetaire.playerDao;
import static dev.ua.ikeepcalm.monetaire.Monetaire.playerTxDao;

@Command("transfer")
@Permission("monetaire.transfer")
public class Transfer {

    @Default
    public static void transfer(Player sender, @APlayerArgument Player recipient, @AIntegerArgument int amount){
        if (amount > 0 && amount < 3000){
            if (!sender.getName().equals(recipient.getName())){
                if (recipient.isOnline()){
                    dev.ua.ikeepcalm.monetaire.entities.Player foundSender = playerDao.findByNickname(sender);
                    if (foundSender.getFine() > 0){
                        MiniMessage mm = MiniMessage.miniMessage();
                        StringBuilder sb = new StringBuilder();
                        sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
                        sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> <#FFFFFF>Ви маєте сплатити штраф!").append("\n");
                        sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Інакше ви не зможете повноцінно користуватися системою!").append("\n");
                        sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Сума штрафів: <#55FFFF>").append(foundSender.getFine()).append(" ДР\n");
                        sb.append("<bold><#5555FF>-----------------------------------------</bold>");
                        Component parsed = mm.deserialize(sb.toString());
                        sender.sendMessage(parsed);
                    } else {
                        if (foundSender.getBalance() >= amount){
                            dev.ua.ikeepcalm.monetaire.entities.Player foundRecipient = playerDao.findByNickname(recipient);
                            foundSender.setBalance(foundSender.getBalance() - amount);
                            foundRecipient.setBalance(foundRecipient.getBalance() + amount);
                            playerDao.save(foundSender);
                            playerDao.save(foundRecipient);
                            PlayerTx playerTx = new PlayerTx();
                            playerTx.setAmount(amount);
                            playerTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                            playerTx.setSender(sender.getName());
                            playerTx.setRecipient(recipient.getName());
                            playerTx.setActionType(ActionType.TRANSFER);
                            playerTx.setSuccessful(true);
                            playerTxDao.save(playerTx);
                            MiniMessage mm = MiniMessage.miniMessage();
                            StringBuilder sb = new StringBuilder();
                            sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
                            sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> <#FFFFFF>Ви переказали <#55FFFF>").append(amount).append(" ДР<#FFFFFF> гравцю<#AAAAAA> ").append(recipient.getName()).append("\n");
                            sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Актуальний рахунок: <#55FFFF>").append(foundSender.getBalance()).append(" ДР\n");
                            sb.append("<bold><#5555FF>-----------------------------------------</bold>");
                            Component parsed = mm.deserialize(sb.toString());
                            sender.sendMessage(parsed);

                            MiniMessage mm1 = MiniMessage.miniMessage();
                            StringBuilder sb1 = new StringBuilder();
                            sb1.append("<bold><#5555FF>-----------------------------------------</bold>\n");
                            sb1.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> <#FFFFFF>Вам було здійснено переказ на суму <#55FFFF>").append(amount).append(" ДР<#FFFFFF> гравцем<#AAAAAA> ").append(sender.getName()).append("\n");
                            sb1.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Актуальний рахунок: <#55FFFF>").append(foundRecipient.getBalance()).append(" ДР\n");
                            sb1.append("<bold><#5555FF>-----------------------------------------</bold>");
                            Component parsed1 = mm1.deserialize(sb1.toString());
                            recipient.sendMessage(parsed1);
                        } else {
                            PlayerTx playerTx = new PlayerTx();
                            playerTx.setAmount(amount);
                            playerTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                            playerTx.setSender(sender.getName());
                            playerTx.setRecipient(recipient.getName());
                            playerTx.setSuccessful(false);
                            playerTx.setActionType(ActionType.TRANSFER);
                            playerTxDao.save(playerTx);
                            MiniMessage mm = MiniMessage.miniMessage();
                            StringBuilder sb = new StringBuilder();
                            sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
                            sb.append("<bold><#5555FF>BANK</bold> <#AAAAAA>> <#FF5555>У вас недостатньо коштів!\n");
                            sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Актуальний рахунок: <#55FFFF>").append(foundSender.getBalance()).append(" ДР\n");
                            sb.append("<bold><#5555FF>-----------------------------------------</bold>");
                            Component parsed = mm.deserialize(sb.toString());
                            sender.sendMessage(parsed);
                        }
                    }
                } else {
                    MiniMessage mm = MiniMessage.miniMessage();
                    StringBuilder sb = new StringBuilder();
                    sb.append("<bold><#5555FF>BANK</bold> <#AAAAAA>> <#FF5555>Ви можете переказувати гроші лише гравцям в мережі!");
                    Component parsed = mm.deserialize(sb.toString());
                    sender.sendMessage(parsed);
                }
            }else {
                MiniMessage mm = MiniMessage.miniMessage();
                StringBuilder sb = new StringBuilder();
                sb.append("<bold><#5555FF>BANK</bold> <#AAAAAA>> <#FF5555>Ви не можете переказувати гроші самому собі!");
                Component parsed = mm.deserialize(sb.toString());
                sender.sendMessage(parsed);
            }
        } else {
            MiniMessage mm = MiniMessage.miniMessage();
            StringBuilder sb = new StringBuilder();
            sb.append("<bold><#5555FF>BANK</bold> <#AAAAAA> > <#FF5555>Сума переказу не може бути меньше 1-го, та більше 3000 ДР! Спробуйте ще раз, але з правильними даними!");
            Component parsed = mm.deserialize(sb.toString());
            sender.sendMessage(parsed);
        }
    }

}
