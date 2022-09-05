package me.infinity.sl;

import lombok.Getter;
import me.infinity.sl.database.HikariDatabase;
import me.infinity.sl.database.profile.Profile;
import me.infinity.sl.database.profile.ProfileListener;
import me.infinity.sl.listener.GameListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

@Getter
public final class SimpleLifeSteal extends JavaPlugin {

  @Getter
  private static SimpleLifeSteal instance;
  private HikariDatabase hikariDatabase;

  @Override
  public void onEnable() {
    instance = this;
    this.hikariDatabase = new HikariDatabase(this);

    new ProfileListener(this);
    new GameListener(this);
  }

  @Override
  public void onDisable() {
    for (Profile profile : Profile.getCache().values()) {
      try {
        profile.save();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
    this.hikariDatabase.disconnect();
  }
}
