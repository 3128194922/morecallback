# More Callback

More Callback 是一个面向 Minecraft Forge 1.20.1 的战斗事件回传模组，向 KubeJS 提供 Apothic Attributes 和 Minecraft 原版战斗事件。

模组不会注册方块、物品或创造标签，只负责事件监听、事件回传和暴击优先级控制。

本MOD由AI编写。
## 功能

- 回传 Apothic Attributes 暴击事件
- 回传 Apothic Attributes 闪避事件
- 回传 Minecraft 原版跳劈事件
- 防止 Apothic 暴击与原版跳劈在同一次攻击中重复触发
- 可配置原版跳劈或 Apothic 暴击优先
- 未安装 Apothic Attributes 时不会强制加载其类

## 环境要求

- Minecraft Java Edition 1.20.1
- Minecraft Forge 47.x
- KubeJS 2001（仅在需要通过 KubeJS 使用事件时需要）
- Apothic Attributes（仅在需要回传其暴击、闪避事件时需要）

More Callback 本身不依赖 KubeJS，事件通过 Forge Event Bus 发布。

## 安装

将构建得到的 `morecallback-*.jar` 放入服务端或客户端的 `mods` 文件夹。

如果使用 Apothic Attributes，请同时安装兼容 Minecraft 1.20.1 的版本。事件回传主要在服务端执行，KubeJS 监听脚本应放入：

```text
kubejs/startup_scripts/
```

## 配置

配置文件：

```text
config/morecallback-common.toml
```

```toml
# 可选值：VANILLA、APOTHIC
# VANILLA：Minecraft 原版跳劈优先
# APOTHIC：Apothic Attributes 暴击优先
criticalHitPriority = "VANILLA"
```

默认值为 `VANILLA`。

### VANILLA

原版跳劈优先。当玩家同时拥有 Apothic 暴击属性时，原版跳劈事件正常回传，Apothic 暴击逻辑不会对同一次攻击重复执行。

### APOTHIC

Apothic Attributes 暴击优先。当两种暴击条件同时满足时，Apothic 暴击事件正常回传，原版跳劈事件仍会回传，但 `isSuppressed()` 为 `true`。

## KubeJS 事件监听

在 `kubejs/startup_scripts/morecallback.js` 中添加：

```js
ForgeEvents.onEvent('com.morecallback.event.VanillaCriticalHitEvent', event => {
  if (!event.isSuppressed()) {
    const player = event.getPlayer()
    const target = event.getTarget()
    console.log(`${player.getName().getString()} 对目标造成原版跳劈`)
  }
})

ForgeEvents.onEvent('com.morecallback.event.ApothicCriticalHitEvent', event => {
  const attacker = event.getAttacker()
  const target = event.getTarget()
  const multiplier = event.getDamageMultiplier()
  console.log(`Apothic 暴击倍率：${multiplier}`)
})

ForgeEvents.onEvent('com.morecallback.event.ApothicDodgeEvent', event => {
  const target = event.getTarget()
  const attackEntity = event.getAttackEntity()
  console.log(`目标成功闪避，是否为远程攻击：${event.isProjectile()}`)
})
```

## 事件 API

### `VanillaCriticalHitEvent`

完整类名：`com.morecallback.event.VanillaCriticalHitEvent`

| 方法 | 返回值 | 说明 |
| --- | --- | --- |
| `getPlayer()` | `Player` | 执行攻击的玩家 |
| `getTarget()` | `Entity` | 被攻击的目标 |
| `getDamageModifier()` | `float` | 原版跳劈伤害倍率 |
| `isSuppressed()` | `boolean` | 事件是否因暴击优先级被抑制 |

### `ApothicCriticalHitEvent`

完整类名：`com.morecallback.event.ApothicCriticalHitEvent`

| 方法 | 返回值 | 说明 |
| --- | --- | --- |
| `getAttacker()` | `LivingEntity` | 发起攻击的实体 |
| `getTarget()` | `LivingEntity` | 被攻击的实体 |
| `getDamageSource()` | `DamageSource` | 本次伤害来源 |
| `getDamageMultiplier()` | `float` | Apothic 暴击实际伤害倍率 |

### `ApothicDodgeEvent`

完整类名：`com.morecallback.event.ApothicDodgeEvent`

| 方法 | 返回值 | 说明 |
| --- | --- | --- |
| `getTarget()` | `LivingEntity` | 成功闪避的实体 |
| `getDamageSource()` | `DamageSource` | 伤害来源；远程投射物事件中可能为 `null` |
| `getAttackEntity()` | `Entity` | 近战攻击实体或远程投射物 |
| `isProjectile()` | `boolean` | 是否为远程投射物攻击 |

这些事件是通知事件，不用于取消或修改原始攻击结果。

## 开发构建

在项目根目录执行：

```text
./gradlew build
```

构建产物位于：

```text
build/libs/morecallback-<version>.jar
```

## 项目信息

- Mod ID：`morecallback`
- Java 包：`com.morecallback`
- 许可证：All Rights Reserved
