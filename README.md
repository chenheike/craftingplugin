# CustomCraftingPlugin

## 项目概述

**CustomCraftingPlugin** 是一个基于 Bukkit/Spigot 平台的 Minecraft 服务器插件，允许服务器管理员和开发者轻松创建、管理和自定义游戏内的合成配方。该插件提供了灵活的配置系统、实时重载功能和完整的命令接口，为 Minecraft 服务器提供了强大的合成系统扩展能力。

## 核心功能

- **灵活的配方配置**：支持通过 YAML 和 JSON 文件定义自定义合成配方
- **实时重载机制**：支持手动和自动重载配方，无需重启服务器
- **权限控制**：可为每个配方设置独立的权限节点
- **完整的命令系统**：提供配方管理、重载、列表查看等命令
- **事件监听**：全面的合成事件处理，支持自定义合成逻辑
- **多格式配方支持**：同时支持 YAML 和 JSON 两种配方定义格式
- **自动重载配置**：可配置自动重载间隔，实时更新配方

## 技术架构

### 项目结构

```
├── src/main/java/com/yourname/craftingplugin/
│   ├── commands/          # 命令执行器
│   │   └── CraftingCommand.java
│   ├── crafting/          # 合成核心逻辑
│   │   ├── CustomRecipe.java
│   │   ├── RecipeLoader.java
│   │   └── RecipeManager.java
│   ├── listeners/         # 事件监听器
│   │   └── CraftingListener.java
│   ├── utils/             # 工具类
│   │   └── ConfigManager.java
│   └── CustomCraftingPlugin.java  # 插件主类
└── src/main/resources/
    ├── config.yml         # 主配置文件
    ├── plugin.yml         # 插件元信息
    └── recipes.json       # JSON 配方示例
```

### 核心组件

1. **CustomCraftingPlugin**：插件主类，负责初始化和管理插件生命周期
2. **RecipeManager**：配方管理核心，负责配方的加载、注册和卸载
3. **RecipeLoader**：配方加载器，支持从 YAML 和 JSON 文件加载配方
4. **CustomRecipe**：自定义配方对象，封装配方的所有属性和逻辑
5. **CraftingCommand**：命令执行器，处理玩家和控制台的命令请求
6. **CraftingListener**：事件监听器，处理合成相关事件
7. **ConfigManager**：配置管理器，负责配置文件的加载和管理

## 安装与配置

### 安装步骤

1. 下载最新版本的 `CustomCraftingPlugin.jar` 文件
2. 将 JAR 文件放入服务器的 `plugins` 目录
3. 启动或重启 Minecraft 服务器
4. 插件会自动生成配置文件和示例配方
5. 编辑配置文件以自定义插件行为
6. 编辑配方文件以添加或修改自定义合成配方

### 配置文件说明

#### config.yml

```yaml
# 自定义合成配方配置
settings:
  # 是否启用配方自动重载
  auto-reload: true
  # 重载检查间隔(秒)
  reload-interval: 30
  # 是否在玩家加入时发送提示消息
  welcome-message: true

# 自定义合成配方示例
recipes:
  enchanted_golden_apple:
    enabled: true
    result: ENCHANTED_GOLDEN_APPLE
    amount: 1
    shape:
      - "GGG"
      - "GAG"
      - "GGG"
    ingredients:
      'G': GOLD_BLOCK
      'A': APPLE
    permission: customcrafting.use
```

#### plugin.yml

定义插件元信息、命令和权限节点。

## 使用指南

### 命令系统

| 命令 | 权限 | 描述 |
|------|------|------|
| `/customcraft reload` | customcrafting.admin | 重载所有自定义配方 |
| `/customcraft list` | customcrafting.admin | 列出所有已加载的配方 |

### 配方定义格式

#### YAML 格式（推荐）

```yaml
recipes:
  配方ID:
    enabled: true/false           # 是否启用该配方
    result: 物品ID                # 合成结果物品
    amount: 数量                  # 合成结果数量
    shape:                       # 合成形状（3x3网格）
      - "ABC"
      - "DEF"
      - "GHI"
    ingredients:                 # 材料映射
      'A': 物品ID1
      'B': 物品ID2
      permission: 权限节点        # 可选，使用该配方所需权限
```

#### JSON 格式

```json
{
  "recipes": [
    {
      "id": "配方ID",
      "enabled": true,
      "result": "物品ID",
      "amount": 1,
      "shape": ["ABC", "DEF", "GHI"],
      "ingredients": {
        "A": "物品ID1",
        "B": "物品ID2"
      },
      "permission": "权限节点"
    }
  ]
}
```

### 权限节点

| 权限节点 | 描述 | 默认值 |
|----------|------|--------|
| `customcrafting.*` | 所有自定义合成权限 | OP |
| `customcrafting.admin` | 管理员权限，可执行命令 | OP |
| `customcrafting.use` | 使用自定义合成配方 | 所有人 |

## 开发指南

### 编译与构建

该项目使用 Maven 进行构建，确保您已安装 Java 11+ 和 Maven 3.6+。

1. 克隆或下载项目源码
2. 进入项目根目录
3. 执行构建命令：
   ```bash
   mvn clean package
   ```
4. 构建成功后，在 `target` 目录下生成 JAR 文件

### 自定义开发

1. **添加新配方**：直接编辑 `config.yml` 或 `recipes.json` 文件
2. **扩展功能**：继承或修改现有类，实现自定义合成逻辑
3. **添加命令**：在 `CraftingCommand.java` 中添加新的命令处理逻辑
4. **添加事件监听**：在 `CraftingListener.java` 中添加新的事件处理

## 性能与优化

- 插件采用了高效的配方注册机制，只注册启用的配方
- 支持自动重载，但建议在生产环境中谨慎使用
- 配方加载和处理采用异步方式，减少对主线程的影响
- 优化的权限检查机制，减少性能开销

## 兼容性

| Minecraft 版本 | 兼容状态 |
|---------------|----------|
| 1.20+ | ✅ 完全兼容 |
| 1.19.x | ✅ 完全兼容 |
| 1.18.x | ✅ 完全兼容 |
| 1.17.x | ✅ 完全兼容 |
| 1.16.x | ✅ 兼容 |
| 1.15.x | ⚠️ 部分功能可能受限 |
| 1.14.x 及以下 | ❌ 不兼容 |

## 故障排除

### 常见问题

1. **问题**：配方不生效
   **解决方案**：检查配方配置是否正确，确保 `enabled` 为 `true`，检查权限设置

2. **问题**：插件无法加载
   **解决方案**：检查服务器日志，确保 Java 版本符合要求，检查依赖是否完整

3. **问题**：自动重载不工作
   **解决方案**：检查 `config.yml` 中的 `auto-reload` 设置，确保设置为 `true`

4. **问题**：命令执行失败
   **解决方案**：检查是否拥有正确的权限，检查命令格式是否正确

## 许可协议

**CustomCraftingPlugin** 的详细使用许可协议请查看项目根目录下的 [LICENSE](LICENSE) 文件。

### 许可协议摘要

**版权所有 © 2024-2026 chenheike**

1. **非商业用途**：允许免费用于非商业用途
2. **商业用途**：需获得作者书面授权（商用需向作者申请握手券）
3. **改编修改**：允许改编但需事先与作者协商，并保留原作者信息
4. **保留权利**：禁止将本插件作为独立产品进行销售
5. **责任限制**：插件按 "原样" 提供，作者不承担任何责任

完整的许可条款请参阅 [LICENSE](LICENSE) 文件。

## 更新日志

### v1.0.0 (2026-01-15)

- ✨ 初始版本发布
- ✨ 支持 YAML 和 JSON 配方格式
- ✨ 实现配方自动重载功能
- ✨ 完整的命令系统
- ✨ 权限控制支持
- ✨ 事件监听系统
- ✨ 多版本兼容

## 贡献指南

欢迎社区贡献！如果您想为该项目做出贡献，请遵循以下步骤：

1. Fork 本项目
2. 创建您的特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交您的更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开一个 Pull Request

## 致谢

- 感谢 Bukkit/Spigot 团队提供的优秀开发平台
- 感谢 Minecraft 社区的支持和反馈
- 感谢所有为该项目做出贡献的开发者

## 联系方式

- **作者**：chenheike
- **作者QQ**：1124301783

## 免责声明

本插件与 Mojang Studios、Microsoft Corporation 或任何其他相关实体无关联。Minecraft 是 Mojang Studios 的商标。

---

**© 2024-2026 chenheike. 保留所有权利。**
