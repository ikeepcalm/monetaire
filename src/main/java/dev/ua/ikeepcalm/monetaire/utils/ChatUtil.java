package dev.ua.ikeepcalm.monetaire.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ChatUtil {

    public static void sendMessage( Player player, String string){
        MiniMessage mm = MiniMessage.miniMessage();
        StringBuilder sb = new StringBuilder();
        sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
        sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> ").append(string).append("\n");
        sb.append("<bold><#5555FF>-----------------------------------------</bold>");
        Component parsed = mm.deserialize(sb.toString());
        player.sendMessage(parsed);
    }

    public static void sendMessage(Player player, String... strings){
        MiniMessage mm = MiniMessage.miniMessage();
        StringBuilder sb = new StringBuilder();
        sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
        for (String string : strings){
            sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> ").append(string).append("\n");
        }
        sb.append("<bold><#5555FF>-----------------------------------------</bold>");
        Component parsed = mm.deserialize(sb.toString());
        player.sendMessage(parsed);
    }

    public static void broadcastMessage(String string){
        MiniMessage mm = MiniMessage.miniMessage();
        StringBuilder sb = new StringBuilder();
        sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
        sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> ").append(string).append("\n");
        sb.append("<bold><#5555FF>-----------------------------------------</bold>");
        Component parsed = mm.deserialize(sb.toString());
        Bukkit.broadcast(parsed);
    }
    public static void broadcastMessage(String... strings){
        MiniMessage mm = MiniMessage.miniMessage();
        StringBuilder sb = new StringBuilder();
        sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
        for (String string : strings){
            sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> ").append(string).append("\n");
        }
        sb.append("<bold><#5555FF>-----------------------------------------</bold>");
        Component parsed = mm.deserialize(sb.toString());
        Bukkit.broadcast(parsed);
    }
}
