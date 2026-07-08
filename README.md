# ThePitUltimate

![Java 21](https://img.shields.io/badge/Java-21-orange)
![Minecraft 1.8.8](https://img.shields.io/badge/Minecraft-1.8.8-brightgreen)
![Gradle](https://img.shields.io/badge/Build-Gradle-blue)

一个基于 Bukkit/Spigot 1.8.8 服务端的 ThePit 小游戏插件。

## 环境要求

| 项目   | 要求                                          |
|------|---------------------------------------------|
| Java | JDK 21                                      |
| 服务端  | Bukkit / Spigot / Paper / PandaSpigot 1.8.8 |
| 必需依赖 | LuckPerms、ProtocolLib                       |
| 可选依赖 | PlayerPoints、WorldEdit、FastAsyncWorldEdit   |

首次启动时插件会通过运行时注入下载部分第三方库，请确保服务器能访问 Maven Central。

## 构建

```bash
# Windows
./gradlew.bat --no-daemon clean build
# Linux / macOS
./gradlew --no-daemon clean build
```

产物位于 `core/build/libs/` 下，与前置依赖一并放入服务端 `plugins/` 目录即可。

> 注意：`base/build/libs/` 下的 jar 是基础模块，不是插件入口。当前构建默认带有 `-dev` 后缀，不影响运行。

## 插件功能

- 完整的神话物品与附魔系统（重要）
- 丰富的连杀与事件系统（重要）
- 诸多玩法NPC与菜单系统
- 玩家排行榜与计分板系统
- 可自定义的Tab列表与音效系统

## 目录结构

```
.
├── base/                  # 基础模块、公共数据结构、Bukkit 入口、运行时依赖加载
├── core/                  # 玩法主体：命令、事件、菜单、物品、附魔、监听器
├── gradle/                # Gradle version catalog 与 wrapper 配置
├── libs/                  # 本地编译依赖
└── .github/workflows/     # 自动构建与 Release 工作流
```

## 项目背景

本项目基于 [Patcher0/ThePitUltimate](https://github.com/Patcher0/ThePitUltimate)
继续维护，该仓库为 [ThePitCommunity/ThePitPremiumOldVersion](https://github.com/ThePitCommunity/ThePitPremiumOldVersion)
的优化分支，两者均已停止维护。本人将尽力长期维护本仓库，让代码能够继续被使用和改进，
正如原README.md所说：

> _代码的生命不在于商业存活，而在于技术精神的延续。_

## 贡献

欢迎提交 Issue 或 Pull Request，尤其是以下方向：

- JDK 21 与 1.8.8 服务端的兼容性问题
- 依赖仓库失效、运行时依赖下载失败或构建失败
- 可复现的玩法逻辑缺陷
- 安全问题、异常网络行为或历史遗留代码审计

提交 Issue 时请附上服务端核心类型、Java 版本、完整异常堆栈以及复现步骤。