package me.infinity.sl.manager;

import me.infinity.sl.SimpleLifeSteal;
import me.infinity.sl.manager.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.persistence.PersistentDataType;

public class ItemManager {

  private final SimpleLifeSteal instance;

  public ItemManager(SimpleLifeSteal instance) {
    this.instance = instance;
    this.createHeartRecipe();
    this.createReviverRecipe();
  }

  public ItemStack getHeart() {
    ItemStack builder = new ItemBuilder(Material.RED_DYE)
            .setDisplayName("&c&lHeart")
            .setLore("&7Right-Click on the item to gain a heart.")
            .setGlow(true)
            .build();
    builder.getItemMeta().getPersistentDataContainer().set(new NamespacedKey(instance, "lapatasmp_heart"), PersistentDataType.STRING, "true");
    return builder;
  }

  public ItemStack getReviveBook() {
    ItemStack builder = new ItemBuilder(Material.BOOK)
            .setDisplayName("&b&lReviver")
            .setLore("&7Right-Click on the item to gain a heart.")
            .setGlow(true)
            .build();
    builder.getItemMeta().getPersistentDataContainer().set(new NamespacedKey(instance, "lapatasmp_reviver"), PersistentDataType.STRING, "true");
    return builder;
  }

  private void createHeartRecipe() {
    ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey(instance, "lapatasmp_heart_recipe"), this.getHeart());
    shapedRecipe.shape("DRD", "DSD", " N ");
    shapedRecipe.setIngredient('D', Material.DIAMOND);
    shapedRecipe.setIngredient('R', Material.RED_DYE);
    shapedRecipe.setIngredient('N', Material.NETHERITE_INGOT);
    shapedRecipe.setIngredient('S', Material.NETHER_STAR);

    instance.getServer().addRecipe(shapedRecipe);
  }

  private void createReviverRecipe() {
    ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey(instance, "lapatasmp_reviver_recipe"), this.getReviveBook());
    shapedRecipe.shape("NDN", "DDD", "DDD");
    shapedRecipe.setIngredient('N', Material.NETHERITE_BLOCK);
    shapedRecipe.setIngredient('D', Material.RED_DYE);

    ShapelessRecipe shapelessRecipe = new ShapelessRecipe(new NamespacedKey(instance, "lapatasmp_reviver_recipe_2"), this.getReviveBook());
    shapelessRecipe.addIngredient(1, Material.DRAGON_EGG);

    instance.getServer().addRecipe(shapelessRecipe);
    instance.getServer().addRecipe(shapedRecipe);
  }
}
