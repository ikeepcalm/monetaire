package dev.ua.ikeepcalm.monetaire.commands.common;

import dev.jorel.commandapi.annotations.*;
import dev.ua.ikeepcalm.monetaire.entities.EcoUser;
import dev.ua.ikeepcalm.monetaire.utils.ChatUtil;
import org.bukkit.entity.Player;

import static dev.ua.ikeepcalm.monetaire.Monetaire.ecoPlayerDao;

@Command("balance")
@Alias({"myinfo"})
@Permission("monetaire.player")
@Help("Використання: /balance; /myinfo")
public class Balance {

    @Default
    public static void balance(Player player) {
        EcoUser foundEcoUser = ecoPlayerDao.findByNickname(player);
        if (foundEcoUser.getCard() == null) {
            ChatUtil.sendMessage(player,
                    "У вас немає картки!",
                    "Спочатку виконайте ➜ /card");
        } else {
            ChatUtil.sendMessage(player,
                    "Інформація о рахунках:",
                    "Аури: <#55FFFF>" + foundEcoUser.getCard().getCoins() + " AUR",
                    "Діри: <#55FFFF>" + foundEcoUser.getCard().getBalance() + " ДР",
                    "Дійсні борги: <#55FFFF>" + foundEcoUser.getCard().getLoan() + " ДР",
                    "Дійсні штрафи: <#55FFFF>" + foundEcoUser.getCard().getFine() + " ДР"
            );
        }
    }
}
