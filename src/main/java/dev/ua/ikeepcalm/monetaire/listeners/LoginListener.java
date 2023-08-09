package dev.ua.ikeepcalm.monetaire.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static dev.ua.ikeepcalm.monetaire.Monetaire.playerDao;

public class LoginListener implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent event) {
        org.bukkit.entity.Player player = event.getPlayer();
        dev.ua.ikeepcalm.monetaire.entities.Player depositPlayer = playerDao.findByNickname(player);
        if (depositPlayer.getFine()>0){
            MiniMessage mm = MiniMessage.miniMessage();
            StringBuilder sb = new StringBuilder();
            sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
            sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> <#FFFFFF>У вас є активний штраф! Негайно сплатіть його!").append("\n");
            sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Інакше ви не зможете повноцінно користуватися системою!").append("\n");
            sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Сума штрафу: <#55FFFF>").append(depositPlayer.getFine()).append(" ДР\n");
            sb.append("<bold><#5555FF>-----------------------------------------</bold>");
            Component parsed = mm.deserialize(sb.toString());
            player.sendMessage(parsed);
        } else if (depositPlayer.getLoan()>0){
            MiniMessage mm = MiniMessage.miniMessage();
            StringBuilder sb = new StringBuilder();
            sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
            sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> <#FFFFFF>Ви маєте певну заборгованість!").append("\n");
            sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Щоб позбутися і виплатити її - /paycredit").append("\n");
            sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Сума боргу: <#55FFFF>").append(depositPlayer.getLoan()).append(" ДР\n");
            sb.append("<bold><#5555FF>-----------------------------------------</bold>");
            Component parsed = mm.deserialize(sb.toString());
            player.sendMessage(parsed);
        }


    }
}
