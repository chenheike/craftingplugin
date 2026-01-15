package com.yourname.craftingplugin.crafting;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yourname.craftingplugin.CustomCraftingPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

public class RecipeLoader {
    
    private final CustomCraftingPlugin plugin;
    private final Gson gson;
    private final File recipesFile;
    
    public RecipeLoader(CustomCraftingPlugin plugin) {
        this.plugin = plugin;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.recipesFile = new File(plugin.getDataFolder(), "recipes.json");
    }
    
    public void loadRecipesFromJson() {
        if (!recipesFile.exists()) {
            plugin.getLogger().info("recipes.json 文件不存在，跳过加载");
            return;
        }
        
        try (FileReader reader = new FileReader(recipesFile)) {
            RecipeCollection collection = gson.fromJson(reader, RecipeCollection.class);
            plugin.getLogger().info("从 JSON 加载了 " + collection.getRecipes().length + " 个配方");
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "加载 recipes.json 时出现错误", e);
        }
    }
    
    public void saveRecipesToJson() {
        try {
            if (!recipesFile.exists()) {
                recipesFile.getParentFile().mkdirs();
                recipesFile.createNewFile();
            }
            
            // 创建示例数据
            RecipeCollection collection = new RecipeCollection();
            collection.setRecipes(new JsonRecipe[]{
                createExampleRecipe()
            });
            
            try (FileWriter writer = new FileWriter(recipesFile)) {
                gson.toJson(collection, writer);
                plugin.getLogger().info("示例配方已保存到 recipes.json");
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "保存 recipes.json 时出现错误", e);
        }
    }
    
    private JsonRecipe createExampleRecipe() {
        JsonRecipe recipe = new JsonRecipe();
        recipe.setKey("example_recipe");
        recipe.setEnabled(true);
        recipe.setResult("DIAMOND");
        recipe.setAmount(2);
        recipe.setShape(new String[]{"DDD", "D D", "DDD"});
        
        JsonIngredients ingredients = new JsonIngredients();
        ingredients.setD("DIRT");
        recipe.setIngredients(ingredients);
        recipe.setPermission("customcrafting.use");
        
        return recipe;
    }
    
    // JSON 映射类
    public static class RecipeCollection {
        private JsonRecipe[] recipes;
        
        public JsonRecipe[] getRecipes() { return recipes; }
        public void setRecipes(JsonRecipe[] recipes) { this.recipes = recipes; }
    }
    
    public static class JsonRecipe {
        private String key;
        private boolean enabled;
        private String result;
        private int amount;
        private String[] shape;
        private JsonIngredients ingredients;
        private String permission;
        
        public String getKey() { return key; }
        public void setKey(String key) { this.key = key; }
        
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        
        public String getResult() { return result; }
        public void setResult(String result) { this.result = result; }
        
        public int getAmount() { return amount; }
        public void setAmount(int amount) { this.amount = amount; }
        
        public String[] getShape() { return shape; }
        public void setShape(String[] shape) { this.shape = shape; }
        
        public JsonIngredients getIngredients() { return ingredients; }
        public void setIngredients(JsonIngredients ingredients) { this.ingredients = ingredients; }
        
        public String getPermission() { return permission; }
        public void setPermission(String permission) { this.permission = permission; }
    }
    
    public static class JsonIngredients {
        private String D;
        
        public String getD() { return D; }
        public void setD(String d) { D = d; }
    }
}