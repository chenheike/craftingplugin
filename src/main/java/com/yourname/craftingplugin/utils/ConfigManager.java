package com.yourname.craftingplugin.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.yourname.craftingplugin.CustomCraftingPlugin;

import java.util.List;
import java.util.Map;

public class ConfigManager {
    
    private final CustomCraftingPlugin plugin;
    
    public ConfigManager(CustomCraftingPlugin plugin) {
        this.plugin = plugin;
    }
    
    public Material getMaterialFromString(String materialName) {
        try {
            return Material.valueOf(materialName.toUpperCase());
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("无效的材料名称: " + materialName);
            return Material.AIR;
        }
    }
    
    public boolean validateRecipeConfig(String recipeKey, Map<String, Object> recipeData) {
        if (!recipeData.containsKey("result")) {
            plugin.getLogger().warning("配方 " + recipeKey + " 缺少 result 字段");
            return false;
        }
        
        if (!recipeData.containsKey("shape")) {
            plugin.getLogger().warning("配方 " + recipeKey + " 缺少 shape 字段");
            return false;
        }
        
        if (!recipeData.containsKey("ingredients")) {
            plugin.getLogger().warning("配方 " + recipeKey + " 缺少 ingredients 字段");
            return false;
        }
        
        // 验证形状
        List<String> shape = (List<String>) recipeData.get("shape");
        if (shape.size() != 3) {
            plugin.getLogger().warning("配方 " + recipeKey + " 的形状必须是3行");
            return false;
        }
        
        for (String row : shape) {
            if (row.length() != 3) {
                plugin.getLogger().warning("配方 " + recipeKey + " 的每一行必须是3个字符");
                return false;
            }
        }
        
        return true;
    }
    
    public String getRecipePermission(String recipeKey) {
        FileConfiguration config = plugin.getConfig();
        String path = "recipes." + recipeKey + ".permission";
        
        if (config.contains(path)) {
            return config.getString(path);
        }
        
        return "customcrafting.use";
    }
    
    public boolean isRecipeEnabled(String recipeKey) {
        FileConfiguration config = plugin.getConfig();
        String path = "recipes." + recipeKey + ".enabled";
        
        return config.getBoolean(path, true);
    }
}