package com.yourname.craftingplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import com.yourname.craftingplugin.CustomCraftingPlugin;

public class CraftingCommand implements CommandExecutor {
    
    private final CustomCraftingPlugin plugin;
    
    public CraftingCommand(CustomCraftingPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "reload":
                return reloadCommand(sender);
                
            case "list":
                return listCommand(sender);
                
            case "help":
                sendHelp(sender);
                return true;
                
            default:
                sender.sendMessage(ChatColor.RED + "未知命令。使用 /customcraft help 查看帮助");
                return true;
        }
    }
    
    private boolean reloadCommand(CommandSender sender) {
        if (!sender.hasPermission("customcrafting.admin")) {
            sender.sendMessage(ChatColor.RED + "你没有权限执行此命令!");
            return true;
        }
        
        try {
            plugin.reloadPluginConfig();
            plugin.getRecipeManager().reloadRecipes();
            sender.sendMessage(ChatColor.GREEN + "配置和配方重载成功!");
            sender.sendMessage(ChatColor.GREEN + "当前已加载 " + 
                plugin.getRecipeManager().getRecipeCount() + " 个配方");
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "重载配置时出现错误: " + e.getMessage());
            plugin.getLogger().severe("重载配置时出现错误: " + e.getMessage());
        }
        
        return true;
    }
    
    private boolean listCommand(CommandSender sender) {
        if (!sender.hasPermission("customcrafting.use")) {
            sender.sendMessage(ChatColor.RED + "你没有权限查看配方列表!");
            return true;
        }
        
        int count = plugin.getRecipeManager().getRecipeCount();
        sender.sendMessage(ChatColor.YELLOW + "=== 自定义合成配方列表 (" + count + " 个) ===");
        
        plugin.getRecipeManager().getRecipeKeys().forEach(key -> {
            sender.sendMessage(ChatColor.GREEN + "- " + key);
        });
        
        return true;
    }
    
    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + "=== 自定义合成插件帮助 ===");
        sender.sendMessage(ChatColor.GOLD + "/customcraft reload " + ChatColor.WHITE + "- 重载配置和配方");
        sender.sendMessage(ChatColor.GOLD + "/customcraft list " + ChatColor.WHITE + "- 查看所有配方");
        sender.sendMessage(ChatColor.GOLD + "/customcraft help " + ChatColor.WHITE + "- 显示此帮助");
    }
}