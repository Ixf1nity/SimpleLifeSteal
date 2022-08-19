package me.infinity.lapata.manager.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CC {

  public String translate(String string) {
    return ChatColor.translateAlternateColorCodes('&', string);
  }

  public List<String> translate(List<String> strings) {
    List<String> toReturn = new ArrayList<>();
    strings.forEach(string -> {
      translate(string);
      toReturn.add(string);
    });
    return toReturn;
  }

}
