package dev.ua.ikeepcalm.monetaire.commands.common.coins;


import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.arguments.AIntegerArgument;
import dev.ua.ikeepcalm.monetaire.entities.User;
import dev.ua.ikeepcalm.monetaire.entities.transactions.SystemTx;
import dev.ua.ikeepcalm.monetaire.entities.transactions.source.ActionType;
import dev.ua.ikeepcalm.monetaire.utils.ChatUtil;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static dev.ua.ikeepcalm.monetaire.Monetaire.playerDao;
import static dev.ua.ikeepcalm.monetaire.Monetaire.systemTxDao;

@Command("unconvert")
@Permission("monetaire.player")
public class Unconvert {

    @Default
    public static void unconvert(Player player, @AIntegerArgument int amount) {
        User convertingUser = playerDao.findByNickname(player);
        if (convertingUser.getCard() == null) {
            ChatUtil.sendMessage(player,
                    "У вас немає картки!",
                    "Спочатку виконайте ➜ /card");
        } else {
            if (convertingUser.getCard().getFine() > 0) {
                ChatUtil.sendMessage(player,
                        "Ви маєте сплатити штраф!",
                        "Інакше ви не зможете повноцінно користуватися банківською системою!",
                        "Сума штрафів: <#55FFFF>" + convertingUser.getCard().getFine() + " ДР"
                );
            } else {
                if (convertingUser.getCard().getCoins() >= amount) {
                    convertingUser.getCard().setCoins(convertingUser.getCard().getCoins() - amount);
                    convertingUser.getCard().setBalance(convertingUser.getCard().getBalance() + amount * 10L);
                    SystemTx systemTx = new SystemTx();
                    systemTx.setActionType(ActionType.UNCONVERT);
                    systemTx.setSuccessful(true);
                    systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                    systemTx.setSender(player.getName());
                    systemTx.setAmount(Math.toIntExact(convertingUser.getCard().getFine()));
                    systemTx.setMomentBalance("MainBalance: " + convertingUser.getCard().getBalance()
                            + " | Credits: " + convertingUser.getCard().getLoan() + " | Fines: " + convertingUser.getCard().getFine());
                    systemTxDao.save(systemTx);
                    playerDao.save(convertingUser);
                    ChatUtil.sendMessage(player,
                            "Успішно конвертовано вказану к-ість AUR у ДР!");

                } else {
                    SystemTx systemTx = new SystemTx();
                    systemTx.setActionType(ActionType.UNCONVERT);
                    systemTx.setSuccessful(false);
                    systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                    systemTx.setSender(player.getName());
                    systemTx.setAmount(Math.toIntExact(convertingUser.getCard().getFine()));
                    systemTx.setMomentBalance("MainBalance: " + convertingUser.getCard().getBalance()
                            + " | Credits: " + convertingUser.getCard().getLoan() + " | Fines: " + convertingUser.getCard().getFine());
                    systemTxDao.save(systemTx);
                    ChatUtil.sendMessage(player,
                            "Недостатньо коштів на балансі!",
                            "Поповніть свій рахунок і спробуйте ще раз!");
                }
            }
        }
    }
}
