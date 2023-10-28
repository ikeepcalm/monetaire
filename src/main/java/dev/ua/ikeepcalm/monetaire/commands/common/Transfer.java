package dev.ua.ikeepcalm.monetaire.commands.common;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.arguments.AIntegerArgument;
import dev.jorel.commandapi.annotations.arguments.APlayerArgument;
import dev.jorel.commandapi.annotations.arguments.AStringArgument;
import dev.ua.ikeepcalm.monetaire.entities.EcoUser;
import dev.ua.ikeepcalm.monetaire.entities.transactions.EcoPlayerTx;
import dev.ua.ikeepcalm.monetaire.entities.transactions.source.ActionType;
import dev.ua.ikeepcalm.monetaire.utils.ChatUtil;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static dev.ua.ikeepcalm.monetaire.Monetaire.ecoPlayerDao;
import static dev.ua.ikeepcalm.monetaire.Monetaire.ecoPlayerTxDao;

@Command("transfer")
@Permission("monetaire.player")
public class Transfer {

    @Default
    public static void transfer(Player sender, @APlayerArgument Player recipient, @AIntegerArgument int amount, @AStringArgument String currency) {
        if (amount > 0 && amount < 3000) {
            EcoUser senderEcoUser = ecoPlayerDao.findByNickname(sender);
            if (senderEcoUser.getCard() == null) {
                ChatUtil.sendMessage(sender,
                        "У вас немає картки!",
                        "Спочатку виконайте ➜ /card");
            }
            if (!sender.getName().equals(recipient.getName())) {
                if (recipient.isOnline()) {
                    if (senderEcoUser.getCard().getFine() > 0) {
                        ChatUtil.sendMessage(sender,
                                "Ви маєте сплатити штраф!",
                                "Інакше ви не зможете повноцінно користуватися банківською системою!",
                                "Сума штрафів: <#55FFFF>" + senderEcoUser.getCard().getFine() + " ДР"
                        );
                    } else {

                        if (currency.equals("DR") || currency.equals("AUR")){
                            switch (currency){
                                case "DR" -> {
                                    if (senderEcoUser.getCard().getBalance() >= amount) {
                                        EcoUser recipientEcoUser = ecoPlayerDao.findByNickname(recipient);
                                        if (recipientEcoUser.getCard() == null){
                                            ChatUtil.sendMessage(sender,
                                                    "У цього гравця немає картки!");
                                        }
                                        else if ((recipientEcoUser.getCard().getBalance() + amount) > 1344) {
                                            ChatUtil.sendMessage(sender,
                                                    "У отримувача недостатньо місця в сховищі",
                                                    "Ви не можете здійснити йому переказ!");
                                        } else {
                                            senderEcoUser.getCard().setBalance(senderEcoUser.getCard().getBalance() - amount);
                                            recipientEcoUser.getCard().setBalance(recipientEcoUser.getCard().getBalance() + amount);
                                            ecoPlayerDao.save(senderEcoUser);
                                            ecoPlayerDao.save(recipientEcoUser);
                                            EcoPlayerTx ecoPlayerTx = new EcoPlayerTx();
                                            ecoPlayerTx.setAmount(amount);
                                            ecoPlayerTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                                            ecoPlayerTx.setSender(sender.getName());
                                            ecoPlayerTx.setRecipient(recipient.getName());
                                            ecoPlayerTx.setActionType(ActionType.TRANSFER);
                                            ecoPlayerTx.setSuccessful(true);
                                            ecoPlayerTx.setMomentBalance("MainBalance: " + senderEcoUser.getCard().getBalance()
                                                    + " | Credits: "+ senderEcoUser.getCard().getLoan() +" | Fines: " + senderEcoUser.getCard().getFine());
                                            ecoPlayerTxDao.save(ecoPlayerTx);
                                            ChatUtil.sendMessage(sender,
                                                    "Ви переказали <#55FFFF>" + amount + " ДР<#FFFFFF> гравцю<#AAAAAA> " + recipient.getName(),
                                                    "Ваш рахунок після операції: <#55FFFF>" + senderEcoUser.getCard().getBalance() + " ДР");

                                            ChatUtil.sendMessage(recipient,
                                                    "Вам було здійснено переказ на суму <#55FFFF>" + amount + " ДР<#FFFFFF> гравцем<#AAAAAA> " + sender.getName(),
                                                    "Ваш рахунок після операції: <#55FFFF>" + recipientEcoUser.getCard().getBalance() + " ДР");
                                        }
                                    } else {
                                        EcoPlayerTx ecoPlayerTx = new EcoPlayerTx();
                                        ecoPlayerTx.setAmount(amount);
                                        ecoPlayerTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                                        ecoPlayerTx.setSender(sender.getName());
                                        ecoPlayerTx.setRecipient(recipient.getName());
                                        ecoPlayerTx.setSuccessful(false);
                                        ecoPlayerTx.setActionType(ActionType.TRANSFER);
                                        ChatUtil.sendMessage(recipient,
                                                "У вас недостатньо коштів!",
                                                "Актуальний рахунок: <#55FFFF>" + senderEcoUser.getCard().getBalance() + " ДР");
                                        ecoPlayerTxDao.save(ecoPlayerTx);
                                    }
                                }
                                case "AUR" -> {
                                    if (senderEcoUser.getCard().getCoins() >= amount) {
                                        EcoUser recipientEcoUser = ecoPlayerDao.findByNickname(recipient);
                                        if (recipientEcoUser.getCard() == null){
                                            ChatUtil.sendMessage(sender,
                                                    "У цього гравця немає картки!");
                                        } else {
                                            senderEcoUser.getCard().setCoins(senderEcoUser.getCard().getCoins() - amount);
                                            recipientEcoUser.getCard().setCoins(recipientEcoUser.getCard().getCoins() + amount);
                                            ecoPlayerDao.save(senderEcoUser);
                                            ecoPlayerDao.save(recipientEcoUser);
                                            EcoPlayerTx ecoPlayerTx = new EcoPlayerTx();
                                            ecoPlayerTx.setAmount(amount);
                                            ecoPlayerTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                                            ecoPlayerTx.setSender(sender.getName());
                                            ecoPlayerTx.setRecipient(recipient.getName());
                                            ecoPlayerTx.setActionType(ActionType.TRANSFER);
                                            ecoPlayerTx.setSuccessful(true);
                                            ecoPlayerTx.setMomentBalance("MainBalance: " + senderEcoUser.getCard().getBalance()
                                                    + " | Credits: "+ senderEcoUser.getCard().getLoan() +" | Fines: " + senderEcoUser.getCard().getFine());
                                            ecoPlayerTxDao.save(ecoPlayerTx);
                                            ChatUtil.sendMessage(sender,
                                                    "Ви переказали <#55FFFF>" + amount + " AUR<#FFFFFF> гравцю<#AAAAAA> " + recipient.getName(),
                                                    "Ваш рахунок після операції: <#55FFFF>" + senderEcoUser.getCard().getCoins() + " AUR");

                                            ChatUtil.sendMessage(recipient,
                                                    "Вам було здійснено переказ на суму <#55FFFF>" + amount + " AUR<#FFFFFF> гравцем<#AAAAAA> " + sender.getName(),
                                                    "Ваш рахунок після операції: <#55FFFF>" + recipientEcoUser.getCard().getCoins() + " ДР");
                                        }
                                    } else {
                                        EcoPlayerTx ecoPlayerTx = new EcoPlayerTx();
                                        ecoPlayerTx.setAmount(amount);
                                        ecoPlayerTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                                        ecoPlayerTx.setSender(sender.getName());
                                        ecoPlayerTx.setRecipient(recipient.getName());
                                        ecoPlayerTx.setSuccessful(false);
                                        ecoPlayerTx.setActionType(ActionType.TRANSFER);
                                        ChatUtil.sendMessage(sender,
                                                "У вас недостатньо коштів!",
                                                "Актуальний рахунок: <#55FFFF>" + senderEcoUser.getCard().getCoins() + " ДР");
                                        ecoPlayerTxDao.save(ecoPlayerTx);
                                    }
                                }
                            }


                        } else {
                            ChatUtil.sendMessage(sender, "Вказана валюта не знайдена! Можливі варіанти: DR / AUR");
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
