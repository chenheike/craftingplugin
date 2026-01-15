package com.yourname.craftingplugin.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import com.yourname.craftingplugin.CustomCraftingPlugin;

public class CraftingListener implements Listener {
    
    private final CustomCraftingPlugin plugin;
    
    public CraftingListener(CustomCraftingPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // 如果启用了欢迎消息并且玩家有权限，发送提示
        if (plugin.getConfig().getBoolean("settings.welcome-message", true) &&
            player.hasPermission("customcrafting.use")) {
            
            int recipeCount = plugin.getRecipeManager().getRecipeCount();
            player.sendMessage(ChatColor.YELLOW + "本服务器有 " + ChatColor.GREEN + 
                recipeCount + ChatColor.YELLOW + " 个自定义合成配方!");
            player.sendMessage(ChatColor.GRAY + "使用 " + ChatColor.WHITE + 
                "/customcraft list" + ChatColor.GRAY + " 查看列表");
        }
    }
    
    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        // 这里可以添加合成事件的特殊处理
        // 例如：记录合成日志、发送通知等
        
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            String itemName = event.getRecipe().getResult().getType().toString();
            
            plugin.getLogger().info("玩家 " + player.getName() + " 合成了: " + itemName);
        }
    }
}