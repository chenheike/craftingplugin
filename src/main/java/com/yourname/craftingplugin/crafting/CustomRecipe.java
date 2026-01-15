package com.yourname.craftingplugin.crafting;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class CustomRecipe {
    private String key;
    private Material result;
    private int amount;
    private List<String> shape;
    private Map<Character, Material> ingredients;
    private String permission;
    private boolean enabled;
    
    public CustomRecipe(String key, Material result, int amount, List<String> shape, 
                       Map<Character, Material> ingredients, String permission, boolean enabled) {
        this.key = key;
        this.result = result;
        this.amount = amount;
        this.shape = shape;
        this.ingredients = ingredients;
        this.permission = permission;
        this.enabled = enabled;
    }
    
    // Getters and Setters
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    
    public Material getResult() { return result; }
    public void setResult(Material result) { this.result = result; }
    
    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
    
    public List<String> getShape() { return shape; }
    public void setShape(List<String> shape) { this.shape = shape; }
    
    public Map<Character, Material> getIngredients() { return ingredients; }
    public void setIngredients(Map<Character, Material> ingredients) { this.ingredients = ingredients; }
    
    public String getPermission() { return permission; }
    public void setPermission(String permission) { this.permission = permission; }
    
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    
    public ItemStack getResultItemStack() {
        return new ItemStack(result, amount);
    }
}