package dev.ua.ikeepcalm.monetaire.commands.common.coins;


import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.arguments.AIntegerArgument;
import dev.ua.ikeepcalm.monetaire.entities.EcoUser;
import dev.ua.ikeepcalm.monetaire.entities.transactions.SystemTx;
import dev.ua.ikeepcalm.monetaire.entities.transactions.source.ActionType;
import dev.ua.ikeepcalm.monetaire.utils.ChatUtil;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static dev.ua.ikeepcalm.monetaire.Monetaire.ecoPlayerDao;
import static dev.ua.ikeepcalm.monetaire.Monetaire.systemTxDao;

@Command("convert")
@Permission("monetaire.player")
public class Convert {

    @Default
    public static void convert(Player player, @AIntegerArgument int amount) {
        EcoUser convertingEcoUser = ecoPlayerDao.findByNickname(player);
        if (convertingEcoUser.getCard() == null) {
            ChatUtil.sendMessage(player,
                    "У вас немає картки!",
                    "Спочатку виконайте ➜ /card");
        } else {
            if (convertingEcoUser.getCard().getFine() > 0){
                ChatUtil.sendMessage(player,
                        "Ви маєте сплатити штраф!",
                        "Інакше ви не зможете повноцінно користуватися банківською системою!",
                        "Сума штрафів: <#55FFFF>" + convertingEcoUser.getCard().getFine() + " ДР"
                );
            } else {
                if (convertingEcoUser.getCard().getBalance() >= amount){
                    if (amount % 10 == 0){
                        convertingEcoUser.getCard().setBalance(convertingEcoUser.getCard().getBalance() - amount);
                        convertingEcoUser.getCard().setCoins(convertingEcoUser.getCard().getCoins() + amount/10);
                        SystemTx systemTx = new SystemTx();
                        systemTx.setActionType(ActionType.CONVERT);
                        systemTx.setSuccessful(true);
                        systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                        systemTx.setSender(player.getName());
                        systemTx.setAmount(Math.toIntExact(convertingEcoUser.getCard().getFine()));
                        systemTx.setMomentBalance("MainBalance: " + convertingEcoUser.getCard().getBalance()
                                + " | Credits: "+ convertingEcoUser.getCard().getLoan() +" | Fines: " + convertingEcoUser.getCard().getFine());
                        systemTxDao.save(systemTx);
                        ecoPlayerDao.save(convertingEcoUser);
                        ChatUtil.sendMessage(player,
                                "Успішно конвертовано вказану к-ість ДР у AUR!");
                    } else {
                        ChatUtil.sendMessage(player,
                                "Конвертувати можна лише суми кратні 10-и!");
                    }
                } else {
                    SystemTx systemTx = new SystemTx();
                    systemTx.setActionType(ActionType.CONVERT);
                    systemTx.setSuccessful(false);
                    systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                    systemTx.setSender(player.getName());
                    systemTx.setAmount(Math.toIntExact(convertingEcoUser.getCard().getFine()));
                    systemTx.setMomentBalance("MainBalance: " + convertingEcoUser.getCard().getBalance()
                            + " | Credits: "+ convertingEcoUser.getCard().getLoan() +" | Fines: " + convertingEcoUser.getCard().getFine());
                    systemTxDao.save(systemTx);
                    ChatUtil.sendMessage(player,
                            "Недостатньо коштів на балансі!",
                            "Поповніть свій рахунок і спробуйте ще раз!");
                }
            }
        }
    }
}
