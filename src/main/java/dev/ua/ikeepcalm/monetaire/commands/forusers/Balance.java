package dev.ua.ikeepcalm.monetaire.commands.forusers;

import dev.jorel.commandapi.annotations.*;
import dev.ua.ikeepcalm.monetaire.entities.User;
import dev.ua.ikeepcalm.monetaire.utils.ChatUtil;
import org.bukkit.entity.Player;

import static dev.ua.ikeepcalm.monetaire.Monetaire.playerDao;

@Command("balance")
@Alias({"myinfo"})
@Permission("monetaire.balance")
@Help("Використання: /balance; /myinfo")
public class Balance {

    @Default
    public static void balance(Player player) {
        User foundUser = playerDao.findByNickname(player);
        if (foundUser.getCard() == null) {
            ChatUtil.sendMessage(player,
                    "У вас немає картки!",
                    "Для отримання пройдіть у банк ➜ 41, 65, -17 ( Спавн )");
        } else {
            ChatUtil.sendMessage(player,
                    "Інформація о рахунках:",
                    "Рахунок: <#55FFFF>" + foundUser.getCard().getBalance() + " ДР",
                    "Дійсні борги: <#55FFFF>" + foundUser.getCard().getLoan() + " ДР",
                    "Дійсні штрафи: <#55FFFF>" + foundUser.getCard().getFine() + " ДР"
            );
        }
    }
}
