package dev.ua.ikeepcalm.monetaire.commands.forusers;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.arguments.AIntegerArgument;
import dev.jorel.commandapi.annotations.arguments.APlayerArgument;
import dev.ua.ikeepcalm.monetaire.entities.User;
import dev.ua.ikeepcalm.monetaire.entities.transactions.PlayerTx;
import dev.ua.ikeepcalm.monetaire.entities.transactions.source.ActionType;
import dev.ua.ikeepcalm.monetaire.utils.ChatUtil;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static dev.ua.ikeepcalm.monetaire.Monetaire.playerDao;
import static dev.ua.ikeepcalm.monetaire.Monetaire.playerTxDao;

@Command("transfer")
@Permission("monetaire.transfer")
public class Transfer {

    @Default
    public static void transfer(Player sender, @APlayerArgument Player recipient, @AIntegerArgument int amount) {
        if (amount > 0 && amount < 3000) {
            User senderUser = playerDao.findByNickname(sender);
            if (senderUser.getCard() == null) {
                ChatUtil.sendMessage(sender,
                        "У вас немає картки!",
                        "Для отримання пройдіть у банк ➜ 41, 65, -17 ( Спавн )");
            }
            if (!sender.getName().equals(recipient.getName())) {
                if (recipient.isOnline()) {
                    if (senderUser.getCard().getFine() > 0) {
                        ChatUtil.sendMessage(sender,
                                "Ви маєте сплатити штраф!",
                                "Інакше ви не зможете повноцінно користуватися банківською системою!",
                                "Сума штрафів: <#55FFFF>" + senderUser.getCard().getFine() + " ДР"
                        );
                    } else {
                        if (senderUser.getCard().getBalance() >= amount) {
                            User recipientUser = playerDao.findByNickname(recipient);
                            if ((recipientUser.getCard().getBalance() + amount) > 1344) {
                                ChatUtil.sendMessage(sender,
                                        "У отримувача недостатньо місця в сховищі",
                                        "Ви не можете здійснити йому переказ!");
                            } else {
                                senderUser.getCard().setBalance(senderUser.getCard().getBalance() - amount);
                                recipientUser.getCard().setBalance(recipientUser.getCard().getBalance() + amount);
                                playerDao.save(senderUser);
                                playerDao.save(recipientUser);
                                PlayerTx playerTx = new PlayerTx();
                                playerTx.setAmount(amount);
                                playerTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                                playerTx.setSender(sender.getName());
                                playerTx.setRecipient(recipient.getName());
                                playerTx.setActionType(ActionType.TRANSFER);
                                playerTx.setSuccessful(true);
                                playerTxDao.save(playerTx);
                                ChatUtil.sendMessage(sender,
                                        "Ви переказали <#55FFFF>" + amount + " ДР<#FFFFFF> гравцю<#AAAAAA> " + recipient.getName(),
                                        "Ваш рахунок після операції: <#55FFFF>" + senderUser.getCard().getBalance() + " ДР");

                                ChatUtil.sendMessage(recipient,
                                        "Вам було здійснено переказ на суму <#55FFFF>" + amount + " ДР<#FFFFFF> гравцем<#AAAAAA> " + sender.getName(),
                                        "Ваш рахунок після операції: <#55FFFF>" + recipientUser.getCard().getBalance() + " ДР");
                            }
                        } else {
                            PlayerTx playerTx = new PlayerTx();
                            playerTx.setAmount(amount);
                            playerTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                            playerTx.setSender(sender.getName());
                            playerTx.setRecipient(recipient.getName());
                            playerTx.setSuccessful(false);
                            playerTx.setActionType(ActionType.TRANSFER);
                            ChatUtil.sendMessage(recipient,
                                    "У вас недостатньо коштів!",
                                    "Актуальний рахунок: <#55FFFF>" + senderUser.getCard().getBalance() + " ДР");
                            playerTxDao.save(playerTx);
                        }
                    }
                } else {
                    ChatUtil.sendMessage(recipient,
                            "Ви не можете переказувати гроші гравцям не в мережі!");
                }
            } else {
                ChatUtil.sendMessage(recipient,
                        "Ви не можете переказувати гроші самі собі!");
            }
        } else {
            ChatUtil.sendMessage(recipient,
                    "Сума переказу не може бути меньше 1-го, та більше 3000 ДР! Спробуйте ще раз, але з правильними даними!");
        }
    }

}
