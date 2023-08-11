package dev.ua.ikeepcalm.monetaire.commands.forusers;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.arguments.AIntegerArgument;
import dev.ua.ikeepcalm.monetaire.entities.MinFin;
import dev.ua.ikeepcalm.monetaire.entities.User;
import dev.ua.ikeepcalm.monetaire.entities.transactions.SystemTx;
import dev.ua.ikeepcalm.monetaire.entities.transactions.source.ActionType;
import dev.ua.ikeepcalm.monetaire.utils.ChatUtil;
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
            User sponsorUser = playerDao.findByNickname(player);
            if (sponsorUser.getCard() == null) {
                ChatUtil.sendMessage(player,
                        "У вас немає картки!",
                        "Для отримання пройдіть у банк ➜ 41, 65, -17 ( Спавн )");
            } else {
                if (sponsorUser.getCard().getFine() > 0){
                    ChatUtil.sendMessage(player,
                            "Ви маєте сплатити штраф!",
                            "Інакше ви не зможете повноцінно користуватися банківською системою!",
                            "Сума штрафів: <#55FFFF>" + sponsorUser.getCard().getFine() + " ДР"
                            );
                } else {
                    if (sponsorUser.getCard().getBalance() >= amount){
                        sponsorUser.getCard().setBalance((sponsorUser.getCard().getBalance() - amount));
                        sponsorUser.getCard().setSponsored(sponsorUser.getCard().getSponsored()+amount);
                        playerDao.save(sponsorUser);
                        SystemTx systemTx = new SystemTx();
                        systemTx.setActionType(ActionType.SPONSOR);
                        systemTx.setSender(sponsorUser.getNickname());
                        systemTx.setAmount(amount);
                        systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                        systemTx.setSuccessful(true);
                        systemTxDao.save(systemTx);
                        MinFin minFin = minfinDao.getMinfin();
                        minFin.setBalance(minFin.getBalance()+amount);
                        minfinDao.save(minFin);
                        ChatUtil.broadcastMessage(
                                "Гравець <#FFAA00>" + player.getName() + "<#FFFFFF> проспонсорував МінФін на <#55FFFF>" + amount +" ДР!"
                        );
                    } else {
                        SystemTx systemTx = new SystemTx();
                        systemTx.setActionType(ActionType.SPONSOR);
                        systemTx.setSender(sponsorUser.getNickname());
                        systemTx.setAmount(amount);
                        systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                        systemTx.setSuccessful(false);
                        systemTxDao.save(systemTx);
                        ChatUtil.sendMessage(player,
                                "Недостатня кількість коштів на балансі!",
                                "Актуальний рахунок: <#55FFFF>" + sponsorUser.getCard().getBalance() + " ДР"
                        );
                    }
                }
            }
        } else {
            ChatUtil.sendMessage(player,
                    "Сума спонсорування не може бути меньше 1-го, та більше 2500 ДР за один раз ДР!!"
            );
        }

    }
}
