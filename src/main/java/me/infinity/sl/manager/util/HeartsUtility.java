package me.infinity.sl.manager.util;

import lombok.experimental.UtilityClass;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.Objects;

@UtilityClass
public class HeartsUtility {

  public double getPlayerHearts(final Player player) {
    return Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue() / 2;
  }

  public void setPlayerHearts(final Player player, final double health) {
    Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(health * 2);
  }

  public void transferHearts(final Player victim, final Player killer) {
    setPlayerHearts(killer, getPlayerHearts(killer) + 1);
    setPlayerHearts(victim, getPlayerHearts(victim) - 1);
  }
}
