package com.yourname.craftingplugin.crafting;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.configuration.file.FileConfiguration;

import com.yourname.craftingplugin.CustomCraftingPlugin;
import com.yourname.craftingplugin.utils.ConfigManager;

import java.util.*;
import java.util.logging.Level;

public class RecipeManager {
    
    private final CustomCraftingPlugin plugin;
    private final ConfigManager configManager;
    private final Map<String, ShapedRecipe> registeredRecipes;
    private final Set<String> recipeKeys;
    
    public RecipeManager(CustomCraftingPlugin plugin) {
        this.plugin = plugin;
        this.configManager = new ConfigManager(plugin);
        this.registeredRecipes = new HashMap<>();
        this.recipeKeys = new HashSet<>();
    }
    
    public void loadAllRecipes() {
        unregisterAllRecipes();
        
        // 从config.yml加载配方
        loadRecipesFromConfig();
        
        plugin.getLogger().info("成功加载 " + registeredRecipes.size() + " 个自定义配方");
    }
    
    private void loadRecipesFromConfig() {
        FileConfiguration config = plugin.getConfig();
        
        if (!config.contains("recipes")) {
            plugin.getLogger().warning("配置文件中未找到任何配方!");
            return;
        }
        
        Set<String> recipeKeys = config.getConfigurationSection("recipes").getKeys(false);
        
        for (String recipeKey : recipeKeys) {
            if (!configManager.isRecipeEnabled(recipeKey)) {
                plugin.getLogger().info("配方 " + recipeKey + " 已被禁用，跳过注册");
                continue;
            }
            
            try {
                registerRecipeFromConfig(recipeKey);
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "注册配方 " + recipeKey + " 时出现错误", e);
            }
        }
    }
    
    private void registerRecipeFromConfig(String recipeKey) {
        FileConfiguration config = plugin.getConfig();
        String path = "recipes." + recipeKey;
        
        // 获取配方数据
        String resultMaterialName = config.getString(path + ".result");
        int amount = config.getInt(path + ".amount", 1);
        List<String> shape = config.getStringList(path + ".shape");
        Map<Character, Material> ingredients = new HashMap<>();
        
        // 验证配方配置
        Map<String, Object> recipeData = new HashMap<>();
        recipeData.put("result", resultMaterialName);
        recipeData.put("shape", shape);
        recipeData.put("ingredients", config.getConfigurationSection(path + ".ingredients").getValues(false));
        
        if (!configManager.validateRecipeConfig(recipeKey, recipeData)) {
            plugin.getLogger().warning("配方 " + recipeKey + " 配置无效，跳过注册");
            return;
        }
        
        // 创建结果物品
        Material resultMaterial = configManager.getMaterialFromString(resultMaterialName);
        if (resultMaterial == Material.AIR) {
            plugin.getLogger().warning("配方 " + recipeKey + " 的结果物品无效: " + resultMaterialName);
            return;
        }
        
        ItemStack result = new ItemStack(resultMaterial, amount);
        
        // 创建命名空间键
        NamespacedKey key = new NamespacedKey(plugin, recipeKey.toLowerCase());
        
        // 创建形状配方
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        
        // 设置形状
        recipe.shape(shape.toArray(new String[0]));
        
        // 设置材料映射
        Map<String, Object> ingredientMap = config.getConfigurationSection(path + ".ingredients").getValues(false);
        for (Map.Entry<String, Object> entry : ingredientMap.entrySet()) {
            char symbol = entry.getKey().charAt(0);
            String ingredientMaterialName = entry.getValue().toString();
            Material ingredientMaterial = configManager.getMaterialFromString(ingredientMaterialName);
            
            if (ingredientMaterial == Material.AIR) {
                plugin.getLogger().warning("配方 " + recipeKey + " 的材料 " + ingredientMaterialName + " 无效");
                continue;
            }
            
            recipe.setIngredient(symbol, ingredientMaterial);
            ingredients.put(symbol, ingredientMaterial);
        }
        
        // 注册配方
        Bukkit.addRecipe(recipe);
        registeredRecipes.put(recipeKey, recipe);
        recipeKeys.add(recipeKey);
        
        plugin.getLogger().info("成功注册配方: " + recipeKey + " -> " + resultMaterialName);
    }
    
    public void unregisterRecipe(String recipeKey) {
        if (registeredRecipes.containsKey(recipeKey)) {
            ShapedRecipe recipe = registeredRecipes.get(recipeKey);
            Bukkit.removeRecipe(recipe.getKey());
            registeredRecipes.remove(recipeKey);
            recipeKeys.remove(recipeKey);
            plugin.getLogger().info("已取消注册配方: " + recipeKey);
        }
    }
    
    public void unregisterAllRecipes() {
        int count = registeredRecipes.size();
        
        for (String recipeKey : new ArrayList<>(registeredRecipes.keySet())) {
            unregisterRecipe(recipeKey);
        }
        
        plugin.getLogger().info("已清除所有自定义配方 (" + count + " 个)");
    }
    
    public void reloadRecipes() {
        loadAllRecipes();
    }
    
    public int getRecipeCount() {
        return registeredRecipes.size();
    }
    
    public Set<String> getRecipeKeys() {
        return new HashSet<>(recipeKeys);
    }
    
    public boolean hasRecipe(String recipeKey) {
        return registeredRecipes.containsKey(recipeKey);
    }
}