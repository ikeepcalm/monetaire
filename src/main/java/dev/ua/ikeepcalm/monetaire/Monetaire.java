package dev.ua.ikeepcalm.monetaire;

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.logger.Level;
import com.j256.ormlite.logger.Logger;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.ua.ikeepcalm.monetaire.commands.*;
import dev.ua.ikeepcalm.monetaire.dao.*;
import dev.ua.ikeepcalm.monetaire.entities.Advertiser;
import dev.ua.ikeepcalm.monetaire.entities.MinFin;
import dev.ua.ikeepcalm.monetaire.entities.Player;
import dev.ua.ikeepcalm.monetaire.entities.transactions.PlayerTx;
import dev.ua.ikeepcalm.monetaire.entities.transactions.SystemTx;
import dev.ua.ikeepcalm.monetaire.gui.bank.MenuGUI;
import dev.ua.ikeepcalm.monetaire.gui.shop.ShopGUI;
import dev.ua.ikeepcalm.monetaire.listeners.BlockListener;
import dev.ua.ikeepcalm.monetaire.listeners.ItemListener;
import dev.ua.ikeepcalm.monetaire.listeners.LoginListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;

public final class Monetaire extends JavaPlugin {

    public static PlayerDao playerDao;
    public static MinfinDao minfinDao;
    public static SystemTxDao systemTxDao;
    public static PlayerTxDao playerTxDao;
    public static AdvertiserDao advertiserDao;

    public File configFile = new File(getDataFolder() + File.separator + "config.yml");

    @Override
    public void onEnable() {
        if (!configFile.exists()){
            saveDefaultConfig();
        } try {
            JdbcPooledConnectionSource source = new JdbcPooledConnectionSource(
                    getConfig().getString("db_url"),
                    getConfig().getString("db_user"),
                    getConfig().getString("db_password"));
            playerDao = DaoManager.createDao(source, Player.class);
            minfinDao = DaoManager.createDao(source, MinFin.class);
            systemTxDao = DaoManager.createDao(source, SystemTx.class);
            playerTxDao = DaoManager.createDao(source, PlayerTx.class);
            advertiserDao = DaoManager.createDao(source, Advertiser.class);
            Configuration.configuration = getConfig();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } CommandAPI.onEnable();
        getServer().getPluginManager().registerEvents(new LoginListener(), this);
        getServer().getPluginManager().registerEvents(new BlockListener(), this);
        getServer().getPluginManager().registerEvents(new ItemListener(), this);
        Logger.setGlobalLogLevel(Level.OFF);
        getLogger().info("Whats up? Monetaire is up to you now!");
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
        getLogger().info("Cya! Monetaire is on his way to have some rest!");
    }

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).verboseOutput(true));
        CommandAPI.registerCommand(Balance.class);
        CommandAPI.registerCommand(Employ.class);
        CommandAPI.registerCommand(MenuGUI.class);
        CommandAPI.registerCommand(Deposit.class);
        CommandAPI.registerCommand(Withdraw.class);
        CommandAPI.registerCommand(Sponsor.class);
        CommandAPI.registerCommand(Transfer.class);
        CommandAPI.registerCommand(Payfine.class);
        CommandAPI.registerCommand(Setfine.class);
        CommandAPI.registerCommand(ShopGUI.class);
    }
}
