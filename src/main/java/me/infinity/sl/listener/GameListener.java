package me.infinity.sl.listener;

import me.infinity.sl.SimpleLifeSteal;
import me.infinity.sl.database.profile.Profile;
import me.infinity.sl.manager.util.CC;
import me.infinity.sl.manager.util.HeartsUtility;
import me.lucko.helper.Events;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class GameListener {

  private final SimpleLifeSteal instance;

  public GameListener(SimpleLifeSteal instance) {
    this.instance = instance;

    Events.subscribe(PlayerDeathEvent.class, EventPriority.MONITOR)
            .filter(event -> !(event.getEntity().getType() == EntityType.PLAYER))
            .handler(event -> {
              Player player = event.getEntity();
              Player player1 = event.getEntity().getKiller();

              if (HeartsUtility.getPlayerHearts(player) == 1) {
                if (player1 == null) {
                  instance.getServer().broadcastMessage(CC.translate("&6" + player.getDisplayName() + " &ahas been &c&lELIMINATED&a from the server!"));
                } else {
                  HeartsUtility.setPlayerHearts(player1, HeartsUtility.getPlayerHearts(player1) + 1);
                  instance.getServer().broadcastMessage(CC.translate("&6" + player.getDisplayName() + " &ahas been &c&lELIMINATED&a by &6" + player1.getDisplayName()));
                }
                Profile profile = Profile.getCache().get(player.getUniqueId());
                profile.setEliminated(true);
                player.kickPlayer(CC.translate("&6" + player.getDisplayName() + " &ahas been &c&lELIMINATED&a from the server!"));
              } else {
                HeartsUtility.transferHearts(player, player1);
              }
            });

    Events.subscribe(PlayerInteractEvent.class, EventPriority.MONITOR)
            .filter(event -> event.getHand() == EquipmentSlot.HAND)
            .filter(event -> event.getAction() == Action.RIGHT_CLICK_AIR)
            .handler(event -> {
              if (event.getItem() == null) return;
              switch (event.getItem().getType()) {
                case BOOK:
                  if (!Objects.requireNonNull(event.getItem().getItemMeta()).getPersistentDataContainer().has(Objects.requireNonNull(NamespacedKey.fromString("lapatasmp_reviver")), PersistentDataType.STRING))
                    return;
                  // TODO: OPEN THE REVIVE MENU
                  break;
                case RED_DYE:
                  if (!Objects.requireNonNull(event.getItem().getItemMeta()).getPersistentDataContainer().has(Objects.requireNonNull(NamespacedKey.fromString("lapatasmp_heart")), PersistentDataType.STRING))
                    return;
                  HeartsUtility.setPlayerHearts(event.getPlayer(), HeartsUtility.getPlayerHearts(event.getPlayer()) + 1);
                  event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                  break;
              }
            });
  }
}