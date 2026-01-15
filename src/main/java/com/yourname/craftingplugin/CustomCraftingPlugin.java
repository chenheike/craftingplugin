package com.yourname.craftingplugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

import com.yourname.craftingplugin.commands.CraftingCommand;
import com.yourname.craftingplugin.listeners.CraftingListener;
import com.yourname.craftingplugin.crafting.RecipeManager;

public class CustomCraftingPlugin extends JavaPlugin {
    
    private static CustomCraftingPlugin instance;
    private RecipeManager recipeManager;
    private FileConfiguration config;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // 保存默认配置文件
        saveDefaultConfig();
        this.config = getConfig();
        
        // 初始化配方管理器
        this.recipeManager = new RecipeManager(this);
        
        // 加载所有配方
        recipeManager.loadAllRecipes();
        
        // 注册命令执行器
        getCommand("customcraft").setExecutor(new CraftingCommand(this));
        
        // 注册事件监听器
        getServer().getPluginManager().registerEvents(new CraftingListener(this), this);
        
        // 发送启用消息
        getLogger().info("§a自定义合成插件已启用!");
        getLogger().info("§a已加载 " + recipeManager.getRecipeCount() + " 个自定义配方");
        
        // 如果启用自动重载，设置定时任务
        if (config.getBoolean("settings.auto-reload", false)) {
            int interval = config.getInt("settings.reload-interval", 30) * 20;
            getServer().getScheduler().runTaskTimer(this, () -> {
                recipeManager.reloadRecipes();
                getLogger().info("§a已自动重载自定义配方");
            }, interval, interval);
        }
    }
    
    @Override
    public void onDisable() {
        // 清除所有自定义配方
        if (recipeManager != null) {
            recipeManager.unregisterAllRecipes();
        }
        
        getLogger().info("§c自定义合成插件已禁用");
    }
    
    public static CustomCraftingPlugin getInstance() {
        return instance;
    }
    
    public RecipeManager getRecipeManager() {
        return recipeManager;
    }
    
    public void reloadPluginConfig() {
        reloadConfig();
        this.config = getConfig();
    }
}