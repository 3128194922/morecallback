# More Callback

Minecraft Forge 1.20.1 战斗事件回传模组，面向 KubeJS 脚本提供 Forge 事件。

## 功能

- 回传 Apothic Attributes 的暴击事件：`com.morecallback.event.ApothicCriticalHitEvent`
- 回传 Apothic Attributes 的闪避事件：`com.morecallback.event.ApothicDodgeEvent`
- 回传 Minecraft 原版跳劈事件：`com.morecallback.event.VanillaCriticalHitEvent`
- 通过 Mixin 与 Apothic Attributes 联动；未安装 Apothic Attributes 时不会强制依赖它

## 暴击优先级配置

配置文件：`config/morecallback-common.toml`

```toml
# VANILLA：原版跳劈优先；APOTHIC：Apothic Attributes 暴击优先
criticalHitPriority = "VANILLA"
```

默认值为 `VANILLA`。当玩家同时拥有 Apothic 暴击属性时，同一次攻击只会让选中的暴击系统生效。选择 `VANILLA` 时，原版事件的 `isSuppressed()` 为 `false`，Apothic 暴击不会重复执行；选择 `APOTHIC` 时，Apothic 暴击事件正常回传，原版事件的 `isSuppressed()` 为 `true`。

## KubeJS 用法

将脚本放入 `kubejs/startup_scripts/`，使用 `ForgeEvents.onEvent` 监听：

```js
ForgeEvents.onEvent('com.morecallback.event.VanillaCriticalHitEvent', event => {
  if (!event.isSuppressed()) {
    console.log(`原版跳劈：${event.getPlayer().getName().getString()}`)
  }
})

ForgeEvents.onEvent('com.morecallback.event.ApothicCriticalHitEvent', event => {
  console.log(`Apothic 暴击倍率：${event.getDamageMultiplier()}`)
})

ForgeEvents.onEvent('com.morecallback.event.ApothicDodgeEvent', event => {
  console.log(`Apothic 闪避，远程攻击：${event.isProjectile()}`)
})
```

事件常用字段：

- `VanillaCriticalHitEvent`：`getPlayer()`、`getTarget()`、`getDamageModifier()`、`isSuppressed()`
- `ApothicCriticalHitEvent`：`getAttacker()`、`getTarget()`、`getDamageSource()`、`getDamageMultiplier()`
- `ApothicDodgeEvent`：`getTarget()`、`getDamageSource()`、`getAttackEntity()`、`isProjectile()`

## 开发构建

```text
./gradlew build
```

模组 ID：`morecallback`；Java 包路径：`com.morecallback`。
