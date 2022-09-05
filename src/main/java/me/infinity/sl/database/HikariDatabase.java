package me.infinity.sl.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import me.infinity.sl.SimpleLifeSteal;
import me.infinity.sl.database.profile.Profile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Getter
public class HikariDatabase {

  private final HikariDataSource hikariDataSource;
  private final Executor hikariExecutor = Executors.newFixedThreadPool(2);
  private Dao<Profile, UUID> profileDao;
  private ConnectionSource connectionSource;

  public HikariDatabase(SimpleLifeSteal instance) {
    HikariConfig hikariConfig = new HikariConfig();

    File database = new File(instance.getDataFolder(), "database.db");
    if (!database.exists()) {
      try {
        database.createNewFile();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    hikariConfig.setJdbcUrl("jdbc:sqlite:" + database);
    hikariConfig.setDriverClassName("org.sqlite.JDBC");
    hikariConfig.addDataSourceProperty("characterEncoding", "utf8");
    hikariConfig.addDataSourceProperty("useUnicode", true);
    hikariConfig.setMaximumPoolSize(10);
    hikariConfig.setPoolName("LifeSteal-Pool");
    hikariDataSource = new HikariDataSource(hikariConfig);
    try {
      this.connectionSource = new DataSourceConnectionSource(hikariDataSource, hikariConfig.getJdbcUrl());
      this.profileDao = DaoManager.createDao(connectionSource, Profile.class);
      TableUtils.createTableIfNotExists(connectionSource, Profile.class);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void disconnect() {
    if (connectionSource != null) connectionSource.closeQuietly();
    if (hikariDataSource != null) hikariDataSource.close();
  }
}
