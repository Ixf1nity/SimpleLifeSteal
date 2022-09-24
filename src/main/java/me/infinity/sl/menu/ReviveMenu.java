package me.infinity.sl.menu;

import io.github.rysefoxx.content.IntelligentItem;
import io.github.rysefoxx.content.InventoryProvider;
import io.github.rysefoxx.pagination.InventoryContents;
import io.github.rysefoxx.pagination.Pagination;
import io.github.rysefoxx.pagination.RyseInventory;
import io.github.rysefoxx.pattern.ContentPattern;
import me.infinity.sl.SimpleLifeSteal;
import me.infinity.sl.database.profile.Profile;
import me.infinity.sl.manager.util.CC;
import me.lucko.helper.item.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ReviveMenu implements InventoryProvider {

  private final SimpleLifeSteal plugin = SimpleLifeSteal.getInstance();
  private Pagination pagination;

  @Override
  public void init(Player player, InventoryContents contents) {
    ItemStack borderPane = ItemStackBuilder.of(Material.LIME_STAINED_GLASS_PANE).name("").lore("").breakable(false).enchant(Enchantment.DURABILITY).flag(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS).build();
    ItemStack nextPage = ItemStackBuilder.of(Material.LIME_DYE).name(CC.translate("&a&lNext Page")).breakable(false).build();
    ItemStack previousPage = ItemStackBuilder.of(Material.LIME_DYE).name(CC.translate("&a&lPrevious Page")).breakable(false).build();

    RyseInventory inventory = RyseInventory.builder().manager(plugin.getInventoryManager())
            .title(CC.translate("&c&lRevive Interface"))
            .rows(6)
            .provider(this).build(plugin);

    pagination = new Pagination(inventory);
    pagination.setItemsPerPage(28);

    ContentPattern contentPattern = contents.contentPattern();
    contentPattern.define("XXXXXXXXX", "XOOOOOOOX", "XOOOOOOOX", "XOOOOOOOX", "XOOOOOOOX", "PXXXXXXXN");
    contentPattern.set('X', IntelligentItem.of(borderPane, event -> event.setCancelled(true)));

    if (pagination.lastPage() == pagination.page()) {
      contentPattern.set('N', IntelligentItem.of(borderPane, event -> event.setCancelled(true)));
    } else {
      contentPattern.set('N', IntelligentItem.of(nextPage, event -> {
        Player clicked = (Player) event.getWhoClicked();
        event.setCancelled(true);
        clicked.playSound(clicked.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
        pagination.next();
        pagination.inventory().updateTitle(player, "&c&lRevive Interface &7(Page " + pagination.page() + "&7)");
      }));
    }

    if (pagination.isFirst()) {
      contentPattern.set('P', IntelligentItem.of(borderPane, event -> event.setCancelled(true)));
    } else {
      contentPattern.set('P', IntelligentItem.of(previousPage, event -> {
        Player clicked = (Player) event.getWhoClicked();
        event.setCancelled(true);
        clicked.playSound(clicked.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
        pagination.previous();
        pagination.inventory().updateTitle(player, "&c&lRevive Interface &7(Page " + pagination.page() + "&7)");
      }));
    }

    try {
      List<Profile> deaths = Profile.getDao().queryForEq("ELIMINATED", true);
      deaths.forEach(profile -> {
        ItemStack head = ItemStackBuilder.of(Material.SKELETON_SKULL)
                .breakable(false)
                .name(CC.translate("&a&l" + profile.getUsername()))
                .lore(CC.translate("&6&lSHIFT-LEFT CLICK &8to bring &b&l" + profile.getUsername() + "&8 to life!"))
                .build();

        pagination.addItem(IntelligentItem.of(head, event -> {
          if (event.getClick() != ClickType.SHIFT_LEFT) return;
          Player inventoryBrowser = (Player) event.getWhoClicked();
          String toRevive = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
          try {
            Optional<Profile> toReviveProfile = Profile.getDao().queryForEq("USERNAME", toRevive).stream().findFirst();
            if (toReviveProfile.isPresent()) {
              toReviveProfile.get().setEliminated(false);
              pagination.inventory().close(inventoryBrowser);
              inventoryBrowser.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1.0F, 1.0F);
              inventoryBrowser.sendMessage(CC.translate("&aYou have successfully revived &6" + toRevive));
            } else {
              pagination.inventory().close(inventoryBrowser);
              inventoryBrowser.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 1.0F, 1.0F);
              inventoryBrowser.sendMessage(CC.translate("&7(&c&l!&7) &cFailed to revive &6" + toRevive));
            }
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        }));
      });
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public ReviveMenu(Player player) {
    this.pagination.inventory().open(player);
  }
}