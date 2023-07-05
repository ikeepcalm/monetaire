package dev.ua.ikeepcalm.monetaire.commands;

import dev.jorel.commandapi.annotations.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import static dev.ua.ikeepcalm.monetaire.Monetaire.playerDao;

@Command("balance")
@Alias({"myinfo"})
@Permission("monetaire.balance")
@Help("Використання: /balance; /myinfo")
public class Balance {

    @Default
    public static void balance(Player player) {
        dev.ua.ikeepcalm.monetaire.entities.Player foundPlayer = playerDao.findByNickname(player);
        MiniMessage mm = MiniMessage.miniMessage();
        StringBuilder sb = new StringBuilder();
        sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
        sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Інформація о рахунках \n");
        sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
        sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Актуальний рахунок: <#55FFFF>").append(foundPlayer.getBalance()).append(" ДР \n");
        sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Заборгованність: <#55FFFF>").append(foundPlayer.getLoan()).append(" ДР \n");
        sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Сума штрафів: <#55FFFF>").append(foundPlayer.getFine()).append(" ДР \n");
        sb.append("<bold><#5555FF>-----------------------------------------</bold>");
        Component parsed = mm.deserialize(sb.toString());
        player.sendMessage(parsed);
    }
}
