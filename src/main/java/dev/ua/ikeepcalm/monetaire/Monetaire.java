package dev.ua.ikeepcalm.monetaire;

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.jdbc.db.MariaDbDatabaseType;
import com.j256.ormlite.jdbc.db.MysqlDatabaseType;
import com.j256.ormlite.logger.Level;
import com.j256.ormlite.logger.Logger;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.ua.ikeepcalm.monetaire.commands.admin.Setcredit;
import dev.ua.ikeepcalm.monetaire.commands.admin.Setfine;
import dev.ua.ikeepcalm.monetaire.commands.common.*;
import dev.ua.ikeepcalm.monetaire.commands.common.coins.Convert;
import dev.ua.ikeepcalm.monetaire.commands.common.coins.Depcoin;
import dev.ua.ikeepcalm.monetaire.commands.common.coins.Unconvert;
import dev.ua.ikeepcalm.monetaire.commands.common.coins.Withcoin;
import dev.ua.ikeepcalm.monetaire.commands.common.diamonds.*;
import dev.ua.ikeepcalm.monetaire.commands.common.reverence.Rep;
import dev.ua.ikeepcalm.monetaire.dao.*;
import dev.ua.ikeepcalm.monetaire.entities.*;
import dev.ua.ikeepcalm.monetaire.entities.transactions.EcoPlayerTx;
import dev.ua.ikeepcalm.monetaire.entities.transactions.SystemTx;
import dev.ua.ikeepcalm.monetaire.gui.shop.ShopGUI;
import dev.ua.ikeepcalm.monetaire.listeners.BlockListener;
import dev.ua.ikeepcalm.monetaire.listeners.ItemListener;
import dev.ua.ikeepcalm.monetaire.listeners.LoginListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;

public final class Monetaire extends JavaPlugin {

    public static EcoPlayerDao ecoPlayerDao;
    public static MinfinDao minfinDao;
    public static SystemTxDao systemTxDao;
    public static EcoPlayerTxDao ecoPlayerTxDao;
    public static AdvertiserDao advertiserDao;
    public static CardDao cardDao;
    public static RepPlayerDao repPlayerDao;

    public File configFile = new File(getDataFolder() + File.separator + "config.yml");

    @Override
    public void onEnable() {
        if (!configFile.exists()){
            saveDefaultConfig();
        } try {
            JdbcPooledConnectionSource ecoSource = new JdbcPooledConnectionSource(
                    getConfig().getString("eco_db_url"),
                    getConfig().getString("eco_db_user"),
                    getConfig().getString("eco_db_password"),
                    new MariaDbDatabaseType());

            JdbcPooledConnectionSource repSource = new JdbcPooledConnectionSource(
                    getConfig().getString("rep_db_url"),
                    getConfig().getString("rep_db_user"),
                    getConfig().getString("rep_db_password"),
                    new MysqlDatabaseType());

            repSource.initialize();

            ecoPlayerDao = DaoManager.createDao(ecoSource, EcoUser.class);
            minfinDao = DaoManager.createDao(ecoSource, MinFin.class);
            systemTxDao = DaoManager.createDao(ecoSource, SystemTx.class);
            ecoPlayerTxDao = DaoManager.createDao(ecoSource, EcoPlayerTx.class);
            advertiserDao = DaoManager.createDao(ecoSource, Advertiser.class);
            cardDao = DaoManager.createDao(ecoSource, Card.class);
            repPlayerDao = DaoManager.createDao(repSource, RepUser.class);

            Configuration.configuration = getConfig();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } CommandAPI.onEnable();
        getServer().getPluginManager().registerEvents(new LoginListener(), this);
        getServer().getPluginManager().registerEvents(new BlockListener(), this);
        getServer().getPluginManager().registerEvents(new ItemListener(), this);
        Logger.setGlobalLogLevel(Level.OFF);
        int ticksPerDay = 20 * 60 * 60 * 24;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                repPlayerDao.updateCredits();
            }
        }, 0, ticksPerDay);
        getLogger().info("Successfully launched Monetaire (UAPROJECT SPEC.)");
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
        getLogger().info("Successfully shut down Monetaire (UAPROJECT SPEC.)");
    }

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).verboseOutput(true));
        CommandAPI.registerCommand(Balance.class);
        CommandAPI.registerCommand(Employ.class);
        CommandAPI.registerCommand(Bank.class);
        CommandAPI.registerCommand(Deposit.class);
        CommandAPI.registerCommand(Withdraw.class);
        CommandAPI.registerCommand(Sponsor.class);
        CommandAPI.registerCommand(Transfer.class);
        CommandAPI.registerCommand(Payfine.class);
        CommandAPI.registerCommand(Setfine.class);
        CommandAPI.registerCommand(Setcredit.class);
        CommandAPI.registerCommand(Paycredit.class);
        CommandAPI.registerCommand(ShopGUI.class);
        CommandAPI.registerCommand(CardIssue.class);
        CommandAPI.registerCommand(Withcoin.class);
        CommandAPI.registerCommand(Depcoin.class);
        CommandAPI.registerCommand(Convert.class);
        CommandAPI.registerCommand(Unconvert.class);
        CommandAPI.registerCommand(Rep.class);
    }
}
