package me.infinity.sl.database.profile;

import me.infinity.sl.SimpleLifeSteal;
import me.infinity.sl.manager.util.CC;
import me.lucko.helper.Events;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;

public class ProfileListener {

  private final SimpleLifeSteal instance;

  public ProfileListener(SimpleLifeSteal instance) {
    this.instance = instance;

    Events.subscribe(AsyncPlayerPreLoginEvent.class, EventPriority.LOW).filter(event -> !(event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED)).handler(event -> {
      Profile profile = new Profile(event.getUniqueId());
      try {
        Profile.getCache().putIfAbsent(profile.getUniqueID(), profile.get());
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }

      if (profile.isEliminated())
        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, CC.translate( "☠ &6You have been &c&lELIMINATED &6from the &a&lLapataSMP&7 ☠"));
    });

    Events.subscribe(PlayerQuitEvent.class, EventPriority.LOW).handler(event -> {
      instance.getHikariDatabase().getHikariExecutor().execute(() -> {
        try {
          Profile.getCache().get(event.getPlayer().getUniqueId()).save();
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
      });
    });
  }

}
