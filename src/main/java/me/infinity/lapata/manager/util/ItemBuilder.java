package me.infinity.lapata.manager.util;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Arrays;
import java.util.List;

public class ItemBuilder {

  private ItemStack item;

  public ItemBuilder(Material material) {
    item = new ItemStack(material);
  }

  public ItemBuilder setDisplayName(String name) {
    ItemMeta meta = item.getItemMeta();
    meta.setDisplayName(CC.translate(name));
    item.setItemMeta(meta);
    return this;
  }

  public ItemBuilder setLore(List<String> lore) {
    ItemMeta meta = item.getItemMeta();
    meta.setLore(CC.translate(lore));
    item.setItemMeta(meta);
    return this;
  }

  public ItemBuilder setLore(String... lore) {
    ItemMeta meta = item.getItemMeta();
    meta.setLore(CC.translate(Arrays.asList(lore)));
    item.setItemMeta(meta);
    return this;
  }

  public ItemBuilder setGlow(boolean glow) {
    ItemMeta meta = item.getItemMeta();
    if (glow) {
      meta.addEnchant(Enchantment.DURABILITY, 1, true);
      meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    } else {
      meta.removeEnchant(Enchantment.DURABILITY);
      meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
    }
    item.setItemMeta(meta);
    return this;
  }

  public ItemBuilder setColor(Color color) {
    LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
    meta.setColor(color);
    item.setItemMeta(meta);
    return this;
  }

  public ItemBuilder setAmount(int amount) {
    item.setAmount(amount);
    return this;
  }

  public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
    ItemMeta meta = item.getItemMeta();
    meta.addEnchant(enchantment, level, true);
    item.setItemMeta(meta);
    return this;
  }

  public ItemStack build() {
    ItemMeta meta = item.getItemMeta();
    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    item.setItemMeta(meta);
    return item;
  }

}

