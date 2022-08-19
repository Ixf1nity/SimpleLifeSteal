package me.infinity.lapata;

import lombok.Getter;
import me.infinity.lapata.database.HikariDatabase;
import me.infinity.lapata.database.profile.Profile;
import me.infinity.lapata.database.profile.ProfileListener;
import me.infinity.lapata.listener.GameListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

@Getter
public final class LapataSMP extends JavaPlugin {

  @Getter
  private static LapataSMP instance;
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
