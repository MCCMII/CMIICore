# ExNihiloCreatio → CMIICore 方块迁移方案

## 一、概述

将 [ExNihiloCreatio-1.12](https://github.com/BloodWorkXRaw/ExNihiloCreatio) 项目中**除筛矿体系外的所有方块**迁移到 CMIICore 项目。

- **源项目**：`d:\github\ExNihiloCreatio-1.12`，基于 ForgeGradle 2.3，Java 8 + Kotlin 1.2
- **目标项目**：`d:\github\CMIICore`，基于 Cleanroom + Unimined，Java 25，Mixin 0.8
- **策略**：直接复制源码 → 改包名 → 改注册方式 → 排除不需要的代码 → Kotlin 转 Java

---

## 二、迁移范围

### 2.1 ✅ 保留的内容

#### 方块（8个）

| 类名 | 注册名 | 说明 |
|------|--------|------|
| `BlockBaseFalling` | `block_dust` | 尘土（仅保留 dust，不保留 5 个粉碎方块） |
| `BlockBarrel` | `block_barrel0` | 木桶 |
| `BlockBarrel` | `block_barrel1` | 石桶 |
| `BlockCrucibleStone` | `block_crucible` | 石坩埚（旧版，熔炼后变砖） |
| `BlockCrucibleWood` | `block_crucible_wood` | 木坩埚 |
| `BlockInfestingLeaves` | `block_infesting_leaves` | 感染中的树叶 |
| `BlockInfestedLeaves` | `block_infested_leaves` | 已感染的树叶 |
| `BlockFluidWitchwater` | `block_witchwater` | 巫水流体方块 |

#### TileEntity（7个）

| 类名 | 说明 |
|------|------|
| `TileBarrel` | 木桶逻辑（含模式系统） |
| `TileCrucibleStone` | 石坩埚逻辑 |
| `TileCrucibleWood` | 木坩埚逻辑 |
| `TileInfestingLeaves` | 感染中树叶逻辑 |
| `TileInfestedLeaves` | 已感染树叶逻辑 |
| `BaseTileEntity` | TileEntity 基类 |
| `FluidTankBase` | 流体罐基类 |

#### TileEntity 辅助类（3个）

| 类名 | 说明 |
|------|------|
| `CrucibleItemHandler` | 坩埚物品处理器 |
| `ITileLeafBlock` | 树叶方块接口 |
| `BarrelFluidHandler` + `BarrelItemHandler` | 桶的流体/物品 Capability |

#### 桶模式系统（Barrel Modes）（全部 11 个文件）

| 类名 | 说明 |
|------|------|
| `IBarrelMode` | 桶模式接口 |
| `BarrelModeBlock` | 方块存储模式 |
| `BarrelItemHandlerBlock` | 方块模式物品处理器 |
| `BarrelModeCompost` | 堆肥模式 |
| `BarrelItemHandlerCompost` | 堆肥模式物品处理器 |
| `BarrelModeFluid` | 流体存储模式 |
| `BarrelItemHandlerFluid` | 流体模式物品处理器 |
| `BarrelModeMobSpawn` | 生物生成模式 |
| `BarrelModeFluidTransform` | 流体转换模式 |

#### 物品（~17 个类）

| 类名 | 注册名 | 说明 |
|------|--------|------|
| `HammerBase` ×5 | `hammer_wood/stone/iron/diamond/gold` | 锤子 |
| `CrookBase` ×6 | `crook_wood/bone/blaze/clay/clay_uncooked/iron` | 钩子 |
| `ItemResource` | `item_resource` | 资源物品（陶瓷粘土、蚕、线等，去掉石齿轮） |
| `ItemDoll` | `item_doll` | 玩偶 |
| `ItemCookedSilkworm` | `item_cooked_silkworm` | 熟蚕 |
| `ItemSeedBase` ×10 | `seed_*` | 种子（橡木/云杉/白桦/丛林/金合欢/深色橡木/仙人掌/甘蔗/胡萝卜/土豆） |
| `ItemRubberSeed` | `seed_rubber` | 橡胶种子 |

#### 工具接口/枚举（4个）

| 类名 | 说明 |
|------|------|
| `IHammer` | 锤子接口 |
| `ICrook` | 钩子接口 |
| `EnumCrook` | 钩子类型枚举 |
| `EnumPebbleSubtype` | ~~排除，石子不需要~~ |

#### 流体（2个）

| 类名 | 说明 |
|------|------|
| `FluidWitchWater` | 巫水流体定义 |
| `FluidStateMapper` | 流体 StateMapper |

#### 处理器（2个）

| 类名 | 说明 |
|------|------|
| `HandlerHammer` | 锤子方块破坏事件 |
| `HandlerCrook` | 钩子树叶破坏事件 |

#### 附魔（3个）

| 类名 | 注册名 | 说明 |
|------|--------|------|
| `EnchantmentEfficiency` | `sieve_efficiency` | 效率附魔（改名适配锤子） |
| `EnchantmentFortune` | `sieve_fortune` | 时运附魔（改名适配锤子） |
| `EnchantmentLuckOfTheSea` | `sieve_luck_of_the_sea` | 海之眷顾附魔（改名适配钩子） |

#### Capability 系统（4个）

| 类名 | 说明 |
|------|------|
| `ICapabilityHeat` | 热值能力接口 |
| `CapabilityHeat` | 热值能力实现 |
| `CapabilityHeatManager` | 热值能力管理器 |
| `ENCapabilities` | 能力注册入口 |

#### 网络包（6个信使 + 1个处理器）

| 类名 | 说明 |
|------|------|
| `PacketHandler` | 网络包注册和发送 |
| `MessageBarrelModeUpdate` | 桶模式同步 |
| `MessageCompostUpdate` | 堆肥进度同步 |
| `MessageFluidLevelUpdate` | 流体等级同步 |
| `MessageFluidUpdate` | 流体类型同步 |
| `MessageNBTUpdate` | 通用 NBT 同步 |
| `MessageCheckLight` | 光照检查（Kotlin→Java） |

#### 注册表系统（~15 个注册表类 + 7 个类型类）

| 保留 | 排除 |
|------|------|
| `CompostRegistry` | `SieveRegistry` |
| `CrookRegistry` | `HammerRegistry` |
| `HammerRegistry`（保留，锤子系统需要） | `OreRegistry` |
| `HeatRegistry` | |
| `BarrelLiquidBlacklistRegistry` | |
| `BarrelModeRegistry` | |
| `FluidOnTopRegistry` | |
| `FluidTransformRegistry` | |
| `FluidBlockTransformerRegistry` | |
| `FluidItemFluidRegistry` | |
| `CrucibleRegistry`（石+木） | |
| `MilkEntityRegistry` | |
| `WitchWaterWorldRegistry` | |

#### JEI 兼容层（保留的）

| 类名 | 说明 |
|------|------|
| `CompatJEI` | JEI 插件入口 |
| `CompostRecipe` + `CompostRecipeCategory` | 堆肥 JEI |
| `FluidOnTopRecipe` + `FluidOnTopRecipeCategory` | 流体置顶 JEI |
| `FluidTransformRecipe` + `FluidTransformRecipeCategory` | 流体转换 JEI |
| `FluidItemTransformRecipe` + `FluidItemTransformRecipeCategory` | 流体物品转换 JEI |
| `CrookRecipe` + `CrookRecipeCategory` | 钩子 JEI |
| `CrucibleRecipe` + `CrucibleRecipeCategory` | 坩埚配方 JEI |
| `CrucibleHeatSourceRecipeCategory` + `HeatSourcesRecipe` | 坩埚热源 JEI |
| `IgnoreNBTandMetaInterpreter` | NBT 忽略解释器（Kotlin→Java） |

#### 渲染层（保留的）

| 类名 | 说明 |
|------|------|
| `RenderBarrel` | 桶渲染 |
| `RenderCrucible` | 坩埚渲染 |
| `RenderInfestingLeaves` | 感染树叶渲染 |
| `InfestedLeavesBakedModel` | 已感染树叶 BakedModel |
| `RenderUtils` | 渲染工具类 |
| `ModColorManager` | 颜色管理器 |
| `ColorGetter/ColorThief/ColorUtil/MMCQ` | 树叶颜色提取 |
| `RenderEvent` | 渲染事件 |

#### JSON 配置系统（全部 Kotlin→Java，~10 个文件）

| 类名 | 说明 |
|------|------|
| `CustomBlockInfoJson` | 方块信息 JSON |
| `CustomColorJson` | 颜色 JSON |
| `CustomCompostableJson` | 可堆肥物 JSON |
| `CustomEntityInfoJson` | 实体信息 JSON |
| `CustomFluidBlockTransformerJson` | 流体方块转换 JSON |
| `CustomFluidJson` | 流体 JSON |
| `CustomHammerRewardJson` | 锤子奖励 JSON |
| `CustomIngredientJson` | 原料 JSON |
| `CustomItemInfoJson` | 物品信息 JSON |
| `CustomItemStackJson` | 物品堆 JSON |
| `CustomMeltableJson` | 可熔化 JSON |
| `CustomWitchWaterWorld` | 巫水世界 JSON |

#### 工具类（5个）

| 类名 | 说明 |
|------|------|
| `Data` | 注册列表容器 |
| `LogUtil` | 日志工具 |
| `IHasModel` | 模型注册接口 |
| `IHasSpecialRegistry` | 特殊注册标记接口 |
| `JsonHelper` | JSON 辅助类 |

#### 命令（2个）

| 类名 | 说明 |
|------|------|
| `CommandReloadConfig` | 重载配置命令 |
| `CommandHandNBT` | 手持物品 NBT 查看命令 |

#### 入口类

| 类名 | 说明 |
|------|------|
| `ModBlocks` | 方块注册 |
| `ModItems` | 物品注册 |
| `ModFluids` | 流体注册 |
| `ModEnchantments` | 附魔注册 |
| `ModEntities` | 实体注册（简化） |
| `Recipes` | 配方注册 |
| `CreativeTab` | 创造模式标签 |
| `CommonProxy` | 通用代理 |
| `ClientProxy` | 客户端代理 |
| `ServerProxy` | 服务端代理（空类） |

### 2.2 ❌ 排除的内容

#### 方块（8个）

| 类名 | 注册名 | 排除原因 |
|------|--------|----------|
| `BlockSieve` | `block_sieve` | 筛矿体系 |
| `BlockAutoSifter` | `block_auto_sifter` | 筛矿自动化 |
| `BlockGrinder` | `block_grinder` | 研磨机（机械动力） |
| `BlockWaterwheel` | `block_waterwheel` | 水车（机械动力） |
| `BlockStoneAxle` | `block_axle_stone` | 石轴（机械动力） |
| `BlockEndCake` | `block_end_cake` | 末地蛋糕 |
| `BlockBaseFalling` ×5 | `block_netherrack_crushed` 等 | 粉碎方块 |

#### TileEntity（5个）

`TileSieve`、`TileAutoSifter`、`TileGrinder`、`TileWaterwheel`、`TileStoneAxle`

#### TileEntity 辅助（2个）

`ItemHandlerAutoSifter`、`ItemHandlerGrinder`

#### 物品（~5个类）

`ItemMesh`（筛网）、`ItemOre`（矿石物品）、`ItemPebble`（石子）、`EnumOreSubtype`、`EnumPebbleSubtype`

#### 实体（2个）

`ProjectileStone`（投掷石子）、`ENEntities`

#### 注册表（3个）

`SieveRegistry`、`OreRegistry`、相关的 DefaultRecipeProvider

#### 渲染器（5个）

`RenderSieve`、`RenderAutoSifter`、`RenderGrinder`、`RenderWaterwheel`、`RenderStoneAxel`、`RenderOrePiece`、`RenderProjectileStone`

#### JEI 兼容（4个）

`SieveRecipe` + `SieveRecipeCategory`、`HammerRecipe` + `HammerRecipeCategory`

#### CraftTweaker（6个）

`Sieve.kt`、`Hammer.kt`、`Ore.kt`、`CrTIntegration.java`、`ENCRemoveAll.kt`（本次不迁移）

#### TOP/WAILA 兼容（3个）

`CompatTOP.java`、`CompatWaila.java`、`ITOPInfoProvider.java`（本次不迁移）

#### 模块/兼容（10+个）

`Forestry`、`TinkersConstruct`、`MooFluidsEtc`、`OreBerries`、`Magneticraft`、`AppliedEnergistics2` 等（本次不迁移）

#### 配方默认值（12+个）

`recipe/defaults/` 下所有文件（本次不迁移，后续按需添加）

---

## 三、Kotlin → Java 转换清单

需要从 `.kt` 改写为 `.java` 的文件共 **约 28 个**：

### 3.1 API 注册表接口（10个）

| Kotlin 文件 | 改写要点 |
|------------|---------|
| `ICompostRegistry.kt` | data class → POJO, 默认参数 → 方法重载 |
| `ICrookRegistry.kt` | 同上 |
| `ICrucibleRegistry.kt` | 同上 |
| `IFluidBlockTransformerRegistry.kt` | 同上 |
| `IFluidItemFluidRegistry.kt` | 同上 |
| `IFluidOnTopRegistry.kt` | 同上 |
| `IFluidTransformRegistry.kt` | 同上 |
| `IHammerRegistry.kt` | 同上 |
| `IHeatRegistry.kt` | 同上 |
| `IMilkEntityRegistry.kt` | 同上 |
| `IOreRegistry.kt` | ~~排除~~ |
| `ISieveRegistry.kt` | ~~排除~~ |
| `IWitchWaterWorldRegistry.kt` | 同上 |

### 3.2 注册表类型（7个）

| Kotlin 文件 | 改写要点 |
|------------|---------|
| `Compostable.kt` | data class → POJO |
| `CrookReward.kt` | data class → POJO |
| `FluidBlockTransformer.kt` | data class → POJO |
| `FluidFluidBlock.kt` | data class → POJO |
| `FluidItemFluid.kt` | data class → POJO |
| `FluidTransformer.kt` | data class → POJO |
| `HammerReward.kt` | data class → POJO |
| `Meltable.kt` | data class → POJO |
| `Milkable.kt` | data class → POJO |
| `Siftable.kt` | ~~排除~~ |
| `WitchWaterWorld.kt` | data class → POJO |

### 3.3 JSON 配置类（10个）

| Kotlin 文件 | 改写要点 |
|------------|---------|
| `CustomBlockInfoJson.kt` | Gson 反序列化 → 手动解析 |
| `CustomColorJson.kt` | 同上 |
| `CustomCompostableJson.kt` | 同上 |
| `CustomEntityInfoJson.kt` | 同上 |
| `CustomFluidBlockTransformerJson.kt` | 同上 |
| `CustomFluidJson.kt` | 同上 |
| `CustomHammerRewardJson.kt` | 同上 |
| `CustomIngredientJson.kt` | 同上 |
| `CustomItemInfoJson.kt` | 同上 |
| `CustomItemStackJson.kt` | 同上 |
| `CustomMeltableJson.kt` | 同上 |
| `CustomOreJson.kt` | ~~排除~~ |
| `CustomWitchWaterWorld.kt` | 同上 |

### 3.4 其他（3个）

| Kotlin 文件 | 说明 |
|------------|------|
| `ExNihiloCreatioAPI.kt` | API 入口，→ Java |
| `MessageCheckLight.kt` | 网络包，→ Java |
| `IDefaultRecipeProviders.kt` | 默认配方接口，→ Java |
| `BlockFluidWitchwater.kt` | 流体方块，→ Java |
| `IgnoreNBTandMetaInterpreter.kt` | JEI 类型解释器，→ Java |

---

## 四、包结构规划

ExNihiloCreatio 使用 `exnihilocreatio.*` 包名，CMIICore 使用 `com.cmii.cmiicore.*`。

目标包结构如下：

```
com.cmii.cmiicore
├── CMIICore.java                          (主 Mod 类，已存在)
├── Reference.java                         (常量，已存在)
│
├── exnihilo                               (新增：ExNihilo 内容根包)
│   ├── CreativeTabExNihilo.java
│   ├── ModBlocks.java
│   ├── ModItems.java
│   ├── ModFluids.java
│   ├── ModEnchantments.java
│   ├── ModEntities.java
│   ├── Recipes.java
│   │
│   ├── api/
│   │   ├── ExNihiloAPI.java               (Kotlin→Java)
│   │   └── registries/
│   │       ├── IRegistry.java
│   │       ├── IRegistryList.java
│   │       ├── IRegistryMap.java
│   │       ├── IRegistryMappedList.java
│   │       ├── ICompostRegistry.java      (Kotlin→Java)
│   │       ├── ICrookRegistry.java        (Kotlin→Java)
│   │       ├── ICrucibleRegistry.java     (Kotlin→Java)
│   │       ├── IFluidBlockTransformerRegistry.java (Kotlin→Java)
│   │       ├── IFluidItemFluidRegistry.java       (Kotlin→Java)
│   │       ├── IFluidOnTopRegistry.java           (Kotlin→Java)
│   │       ├── IFluidTransformRegistry.java       (Kotlin→Java)
│   │       ├── IHammerRegistry.java               (Kotlin→Java)
│   │       ├── IHeatRegistry.java                 (Kotlin→Java)
│   │       ├── IMilkEntityRegistry.java           (Kotlin→Java)
│   │       └── IWitchWaterWorldRegistry.java      (Kotlin→Java)
│   │
│   ├── barrel/
│   │   ├── IBarrelMode.java
│   │   ├── BarrelFluidHandler.java
│   │   ├── BarrelItemHandler.java
│   │   └── modes/
│   │       ├── block/
│   │       │   ├── BarrelModeBlock.java
│   │       │   └── BarrelItemHandlerBlock.java
│   │       ├── compost/
│   │       │   ├── BarrelModeCompost.java
│   │       │   └── BarrelItemHandlerCompost.java
│   │       ├── fluid/
│   │       │   ├── BarrelModeFluid.java
│   │       │   └── BarrelItemHandlerFluid.java
│   │       ├── mobspawn/
│   │       │   └── BarrelModeMobSpawn.java
│   │       └── transform/
│   │           └── BarrelModeFluidTransform.java
│   │
│   ├── blocks/
│   │   ├── BlockBase.java
│   │   ├── BlockBaseFalling.java
│   │   ├── BlockBarrel.java
│   │   ├── BlockCrucibleBase.java
│   │   ├── BlockCrucibleStone.java
│   │   ├── BlockCrucibleWood.java
│   │   ├── BlockInfestingLeaves.java
│   │   ├── BlockInfestedLeaves.java
│   │   ├── BlockFluidWitchwater.java       (Kotlin→Java)
│   │   └── ItemBlockCrucible.java
│   │
│   ├── capabilities/
│   │   ├── ICapabilityHeat.java
│   │   ├── CapabilityHeat.java
│   │   ├── CapabilityHeatManager.java
│   │   └── ENCapabilities.java
│   │
│   ├── client/
│   │   ├── color/
│   │   │   ├── ColorGetter.java
│   │   │   ├── ColorThief.java
│   │   │   ├── ColorUtil.java
│   │   │   └── MMCQ.java
│   │   ├── models/
│   │   │   ├── InfestedLeavesBakedModel.java
│   │   │   ├── ModColorManager.java
│   │   │   ├── ModelVertex.java
│   │   │   └── event/
│   │   │       └── RenderEvent.java
│   │   └── renderers/
│   │       ├── RenderBarrel.java
│   │       ├── RenderCrucible.java
│   │       ├── RenderInfestingLeaves.java
│   │       └── RenderUtils.java
│   │
│   ├── command/
│   │   ├── CommandHandNBT.java
│   │   └── CommandReloadConfig.java
│   │
│   ├── compatibility/
│   │   └── jei/
│   │       ├── CompatJEI.java
│   │       ├── IgnoreNBTandMetaInterpreter.java   (Kotlin→Java)
│   │       ├── barrel/
│   │       │   ├── compost/
│   │       │   │   ├── CompostRecipe.java
│   │       │   │   └── CompostRecipeCategory.java
│   │       │   ├── fluiditemtransform/
│   │       │   │   ├── FluidItemTransformRecipe.java
│   │       │   │   └── FluidItemTransformRecipeCategory.java
│   │       │   ├── fluidontop/
│   │       │   │   ├── FluidOnTopRecipe.java
│   │       │   │   └── FluidOnTopRecipeCategory.java
│   │       │   └── fluidtransform/
│   │       │       ├── FluidTransformRecipe.java
│   │       │       └── FluidTransformRecipeCategory.java
│   │       ├── crook/
│   │       │   ├── CrookRecipe.java
│   │       │   └── CrookRecipeCategory.java
│   │       └── crucible/
│   │           ├── CrucibleRecipe.java
│   │           ├── CrucibleRecipeCategory.java
│   │           ├── CrucibleHeatSourceRecipeCategory.java
│   │           └── HeatSourcesRecipe.java
│   │
│   ├── config/
│   │   └── ModConfig.java                   (适配 Cleanroom 配置系统)
│   │
│   ├── enchantments/
│   │   ├── EnchantmentEfficiency.java
│   │   ├── EnchantmentFortune.java
│   │   └── EnchantmentLuckOfTheSea.java
│   │
│   ├── fluid/
│   │   ├── FluidStateMapper.java
│   │   └── FluidWitchWater.java
│   │
│   ├── handlers/
│   │   ├── HandlerHammer.java
│   │   └── HandlerCrook.java
│   │
│   ├── items/
│   │   ├── ItemBlockMeta.java
│   │   ├── ItemCookedSilkworm.java
│   │   ├── ItemDoll.java
│   │   ├── ItemResource.java
│   │   ├── seeds/
│   │   │   ├── ItemRubberSeed.java
│   │   │   └── ItemSeedBase.java
│   │   └── tools/
│   │       ├── CrookBase.java
│   │       ├── EnumCrook.java
│   │       ├── HammerBase.java
│   │       ├── ICrook.java
│   │       └── IHammer.java
│   │
│   ├── json/
│   │   ├── JsonHelper.java
│   │   ├── CustomBlockInfoJson.java         (Kotlin→Java)
│   │   ├── CustomColorJson.java             (Kotlin→Java)
│   │   ├── CustomCompostableJson.java       (Kotlin→Java)
│   │   ├── CustomEntityInfoJson.java        (Kotlin→Java)
│   │   ├── CustomFluidBlockTransformerJson.java (Kotlin→Java)
│   │   ├── CustomFluidJson.java             (Kotlin→Java)
│   │   ├── CustomHammerRewardJson.java      (Kotlin→Java)
│   │   ├── CustomIngredientJson.java        (Kotlin→Java)
│   │   ├── CustomItemInfoJson.java          (Kotlin→Java)
│   │   ├── CustomItemStackJson.java         (Kotlin→Java)
│   │   ├── CustomMeltableJson.java          (Kotlin→Java)
│   │   └── CustomWitchWaterWorld.java       (Kotlin→Java)
│   │
│   ├── networking/
│   │   ├── PacketHandler.java
│   │   ├── MessageBarrelModeUpdate.java
│   │   ├── MessageCheckLight.java           (Kotlin→Java)
│   │   ├── MessageCompostUpdate.java
│   │   ├── MessageFluidLevelUpdate.java
│   │   ├── MessageFluidUpdate.java
│   │   └── MessageNBTUpdate.java
│   │
│   ├── registries/
│   │   ├── RegistryReloadedEvent.java
│   │   ├── ingredient/
│   │   │   ├── IngredientUtil.java
│   │   │   └── OreIngredientStoring.java
│   │   ├── manager/
│   │   │   ├── ExNihiloDefaultRecipes.java
│   │   │   ├── ExNihiloRegistryManager.java
│   │   │   └── IDefaultRecipeProviders.java (Kotlin→Java)
│   │   ├── registries/
│   │   │   ├── prefab/
│   │   │   │   ├── BaseRegistry.java
│   │   │   │   ├── BaseRegistryList.java
│   │   │   │   └── BaseRegistryMap.java
│   │   │   ├── BarrelLiquidBlacklistRegistry.java
│   │   │   ├── BarrelModeRegistry.java
│   │   │   ├── CompostRegistry.java
│   │   │   ├── CrookRegistry.java
│   │   │   ├── CrucibleRegistry.java
│   │   │   ├── FluidBlockTransformerRegistry.java
│   │   │   ├── FluidItemFluidRegistry.java
│   │   │   ├── FluidOnTopRegistry.java
│   │   │   ├── FluidTransformRegistry.java
│   │   │   ├── HammerRegistry.java
│   │   │   ├── HeatRegistry.java
│   │   │   ├── MilkEntityRegistry.java
│   │   │   └── WitchWaterWorldRegistry.java
│   │   └── types/
│   │       ├── Compostable.java             (Kotlin→Java)
│   │       ├── CrookReward.java             (Kotlin→Java)
│   │       ├── FluidBlockTransformer.java   (Kotlin→Java)
│   │       ├── FluidFluidBlock.java         (Kotlin→Java)
│   │       ├── FluidItemFluid.java          (Kotlin→Java)
│   │       ├── FluidTransformer.java        (Kotlin→Java)
│   │       ├── HammerReward.java            (Kotlin→Java)
│   │       ├── Meltable.java                (Kotlin→Java)
│   │       ├── Milkable.java                (Kotlin→Java)
│   │       └── WitchWaterWorld.java         (Kotlin→Java)
│   │
│   ├── tiles/
│   │   ├── BaseTileEntity.java
│   │   ├── CrucibleItemHandler.java
│   │   ├── FluidTankBase.java
│   │   ├── ITileLeafBlock.java
│   │   ├── TileBarrel.java
│   │   ├── TileCrucibleBase.java
│   │   ├── TileCrucibleStone.java
│   │   ├── TileCrucibleWood.java
│   │   ├── TileInfestedLeaves.java
│   │   └── TileInfestingLeaves.java
│   │
│   └── util/
│       ├── Data.java
│       ├── IHasModel.java
│       ├── IHasSpecialRegistry.java
│       └── LogUtil.java
│
├── proxy/                                  (已存在，需扩展)
│   ├── IProxy.java
│   ├── CommonProxy.java
│   └── ClientProxy.java
│
├── api/                                    (已存在，不动)
│   ├── LoonuimExtraProducts.java
│   ├── SubTilePurifyingFlower.java
│   └── TC6FluxHelper.java
│
└── mixin/                                  (已存在，不动)
    └── botania/
        ├── MixinBotaniaAPI.java
        ├── MixinGardenOfGlass.java
        └── MixinSubTileLoonuim.java
```

---

## 五、分阶段迁移计划（13 个 Phase）

### Phase 0：基础工具层（~5 文件，纯 Java，零依赖）

| 文件 | 改造内容 |
|------|---------|
| `Data.java` | 包名 `exnihilocreatio.util` → `com.cmii.cmiicore.exnihilo.util` |
| `LogUtil.java` | 同上，内部硬编码 `"Ex Nihilo Creatio"` → `Reference.MOD_NAME` |
| `IHasModel.java` | 包名替换，无需改动逻辑 |
| `IHasSpecialRegistry.java` | 包名替换 |
| `JsonHelper.java` | 包名 `exnihilocreatio.json` → `com.cmii.cmiicore.exnihilo.json` |

### Phase 1：配置系统（1 文件，需适配）

| 文件 | 改造内容 |
|------|---------|
| `ModConfig.java` | **关键改造**：ExNihilo 使用 Forge `@Config` 注解系统。需要评估 Cleanroom 是否支持 `@Config`。如不支持，改用 Cleanroom 的配置方案或手动 JSON 配置。移除 `Sieve`、`Ore`、`Compatibility` 等不需要的内部类。修改 `ConfigurationHolder` 中的 `ExNihiloCreatio.MODID` → `Reference.MOD_ID` |

### Phase 2：注册表接口层（~16 文件，含 10 个 KT→Java）

| 类别 | 文件 |
|------|------|
| Java 直接复制 | `IRegistry.java`, `IRegistryList.java`, `IRegistryMap.java`, `IRegistryMappedList.java` |
| KT→Java | `ICompostRegistry`, `ICrookRegistry`, `ICrucibleRegistry`, `IFluidBlockTransformerRegistry`, `IFluidItemFluidRegistry`, `IFluidOnTopRegistry`, `IFluidTransformRegistry`, `IHammerRegistry`, `IHeatRegistry`, `IMilkEntityRegistry`, `IWitchWaterWorldRegistry` |
| 排除 | `IOreRegistry.kt`, `ISieveRegistry.kt` |

**KT→Java 改写要点：**
- Kotlin `data class` → Java POJO（手动写 getter/setter/equals/hashCode/toString）
- Kotlin 默认参数 → Java 方法重载（多个同名方法，不同参数）
- Kotlin `List<X>` 扩展函数 → Java `Collections` 工具类或手动遍历
- Kotlin `companion object` → Java `static` 字段和方法

### Phase 3：注册表管理器 + 类型（~25 文件，含 18 个 KT→Java）

| 类别 | 文件 |
|------|------|
| Java 直接复制 | `BaseRegistry.java`, `BaseRegistryList.java`, `BaseRegistryMap.java`, `RegistryReloadedEvent.java`, `IngredientUtil.java`, `OreIngredientStoring.java` |
| Java（去除不必要内容） | `ExNihiloRegistryManager.java`（移除 `SIEVE_REGISTRY`、`ORE_REGISTRY` 及对应 DefaultProvider），`ExNihiloDefaultRecipes.java`（仅注册保留的默认配方） |
| KT→Java | `IDefaultRecipeProviders.kt` → 排除 Ore/Sieve 提供者 |
| KT→Java（types） | `Compostable`, `CrookReward`, `FluidBlockTransformer`, `FluidFluidBlock`, `FluidItemFluid`, `FluidTransformer`, `HammerReward`, `Meltable`, `Milkable`, `WitchWaterWorld` |
| 排除 | `Siftable.kt` |

### Phase 4：物品层（~17 文件，纯 Java）

| 类别 | 文件 | 改造内容 |
|------|------|---------|
| 工具接口 | `IHammer.java`, `ICrook.java` | 包名替换 |
| 工具枚举 | `EnumCrook.java` | 包名替换 |
| 工具实现 | `HammerBase.java` | 包名替换。注意：构造中 64/128/512/4096 是耐久值 |
| 工具实现 | `CrookBase.java` | 包名替换 |
| 资源物品 | `ItemResource.java` | 包名替换，移除 `GEAR_STONE` 相关常量和方法 |
| 玩偶 | `ItemDoll.java` | 包名替换 |
| 熟蚕 | `ItemCookedSilkworm.java` | 包名替换 |
| 种子 | `ItemSeedBase.java`, `ItemRubberSeed.java` | 包名替换 |
| 辅助 | `ItemBlockMeta.java` | 包名替换 |
| 排除 | `ItemMesh.java`, `ItemOre.java`, `ItemPebble.java`, `EnumOreSubtype.kt`, `EnumPebbleSubtype.java` | |

### Phase 5：方块 + TileEntity 层（~30 文件）

| 类别 | 文件 | 改造内容 |
|------|------|---------|
| 基类 | `BlockBase.java` | **关键**：修改 `Data.BLOCKS.add(this)` 为 CMIICore 的注册方式 |
| 基类 | `BlockBaseFalling.java` | **关键**：移除 `setCreativeTab(ExNihiloCreatio.tabExNihilo)`，只保留 `dust` 的实例化 |
| 桶 | `BlockBarrel.java` | **注意**：移除 `ITOPInfoProvider` 接口（TOP 兼容暂不迁移），移除相关 import |
| 坩埚 | `BlockCrucibleBase.java`, `BlockCrucibleStone.java`, `BlockCrucibleWood.java`, `ItemBlockCrucible.java` | 包名替换 |
| 树叶 | `BlockInfestingLeaves.java`, `BlockInfestedLeaves.java` | `BlockInfestingLeaves` 不实现 `ITOPInfoProvider` |
| 流体方块 | `BlockFluidWitchwater.kt` → Java | KT→Java |
| 流体 | `FluidWitchWater.java`, `FluidStateMapper.java` | 包名替换 |
| TileEntity 基类 | `BaseTileEntity.java`, `FluidTankBase.java`, `ITileLeafBlock.java` | 包名替换 |
| 桶 Tile | `TileBarrel.java` | **注意**：移除 `MooFluidsEtc` 相关代码（MooFluids 模块保留但不加载），移除 `lombok.Getter` |
| 坩埚 Tile | `TileCrucibleBase.java`, `TileCrucibleStone.java`, `TileCrucibleWood.java`, `CrucibleItemHandler.java` | 包名替换 |
| 树叶 Tile | `TileInfestingLeaves.java`, `TileInfestedLeaves.java` | 包名替换 |
| 排除 Tile | `TileSieve`, `TileAutoSifter`, `TileGrinder`, `TileWaterwheel`, `TileStoneAxle`, `ItemHandlerAutoSifter`, `ItemHandlerGrinder` | |

### Phase 6：桶模式 + Capability 层（~15 文件，纯 Java）

| 类别 | 文件 |
|------|------|
| 接口 | `IBarrelMode.java` |
| 流体处理 | `BarrelFluidHandler.java`, `BarrelItemHandler.java` |
| 方块模式 | `BarrelModeBlock.java`, `BarrelItemHandlerBlock.java` |
| 堆肥模式 | `BarrelModeCompost.java`, `BarrelItemHandlerCompost.java` |
| 流体模式 | `BarrelModeFluid.java`, `BarrelItemHandlerFluid.java` |
| 生物生成模式 | `BarrelModeMobSpawn.java` |
| 转换模式 | `BarrelModeFluidTransform.java` |
| Capability | `ICapabilityHeat.java`, `CapabilityHeat.java`, `CapabilityHeatManager.java`, `ENCapabilities.java` |

### Phase 7：处理器 + 附魔层（~7 文件，纯 Java）

| 类别 | 文件 | 改造内容 |
|------|------|---------|
| 处理器 | `HandlerHammer.java` | 包名替换 |
| 处理器 | `HandlerCrook.java` | 包名替换 |
| 附魔 | `EnchantmentEfficiency.java` | 包名替换 |
| 附魔 | `EnchantmentFortune.java` | 包名替换 |
| 附魔 | `EnchantmentLuckOfTheSea.java` | 包名替换 |
| 注册 | `ModEnchantments.java` | 包名替换 |

### Phase 8：网络层（~7 文件，含 1 个 KT→Java）

| 类别 | 文件 | 改造内容 |
|------|------|---------|
| 处理器 | `PacketHandler.java` | `ExNihiloCreatio.MODID` → `Reference.MOD_ID` |
| 信使 | `MessageBarrelModeUpdate.java` | 包名替换 |
| 信使 | `MessageCompostUpdate.java` | 包名替换 |
| 信使 | `MessageFluidLevelUpdate.java` | 包名替换 |
| 信使 | `MessageFluidUpdate.java` | 包名替换 |
| 信使 | `MessageNBTUpdate.java` | 包名替换 |
| 信使 | `MessageCheckLight.kt` → Java | KT→Java |

### Phase 9：渲染层（~14 文件，纯 Java）

| 类别 | 文件 | 改造内容 |
|------|------|---------|
| 桶渲染 | `RenderBarrel.java` | 包名替换 |
| 坩埚渲染 | `RenderCrucible.java` | 包名替换 |
| 树叶渲染 | `RenderInfestingLeaves.java` | 包名替换 |
| BakedModel | `InfestedLeavesBakedModel.java` | 包名替换 |
| 工具 | `RenderUtils.java`, `ModelVertex.java` | 包名替换 |
| 颜色 | `ModColorManager.java` | 包名替换 |
| 颜色提取 | `ColorGetter.java`, `ColorThief.java`, `ColorUtil.java`, `MMCQ.java` | 包名替换 |
| 事件 | `RenderEvent.java` | 包名替换 |
| 排除 | `RenderSieve`, `RenderAutoSifter`, `RenderGrinder`, `RenderWaterwheel`, `RenderStoneAxel`, `RenderOrePiece`, `RenderProjectileStone` | |

### Phase 10：JEI 兼容层（~16 文件，含 1 个 KT→Java）

| 类别 | 文件 | 改造内容 |
|------|------|---------|
| 入口 | `CompatJEI.java` | **关键**：移除 `registerHammer()`、`registerSieve()` 调用和对应方法。`ExNihiloCreatio.MODID` → `Reference.MOD_ID`。`ExNihiloCreatio.configsLoaded` → 新的配置加载标记 |
| 堆肥 | `CompostRecipe.java`, `CompostRecipeCategory.java` | 包名替换 |
| 流体转换 | `FluidTransformRecipe.java`, `FluidTransformRecipeCategory.java` | 包名替换 |
| 流体置顶 | `FluidOnTopRecipe.java`, `FluidOnTopRecipeCategory.java` | 包名替换 |
| 流体物品转换 | `FluidItemTransformRecipe.java`, `FluidItemTransformRecipeCategory.java` | 包名替换 |
| 钩子 | `CrookRecipe.java`, `CrookRecipeCategory.java` | 包名替换 |
| 坩埚 | `CrucibleRecipe.java`, `CrucibleRecipeCategory.java`, `CrucibleHeatSourceRecipeCategory.java`, `HeatSourcesRecipe.java` | 包名替换 |
| 解释器 | `IgnoreNBTandMetaInterpreter.kt` → Java | KT→Java |
| 排除 | `SieveRecipe`, `SieveRecipeCategory`, `HammerRecipe`, `HammerRecipeCategory` | |

### Phase 11：JSON 配置层（~12 文件，全部 KT→Java）

| 文件 | 说明 |
|------|------|
| `CustomBlockInfoJson.kt` → Java | Gson 反序列化 POJO |
| `CustomColorJson.kt` → Java | 同上 |
| `CustomCompostableJson.kt` → Java | 同上 |
| `CustomEntityInfoJson.kt` → Java | 同上 |
| `CustomFluidBlockTransformerJson.kt` → Java | 同上 |
| `CustomFluidJson.kt` → Java | 同上 |
| `CustomHammerRewardJson.kt` → Java | 同上 |
| `CustomIngredientJson.kt` → Java | 同上 |
| `CustomItemInfoJson.kt` → Java | 同上 |
| `CustomItemStackJson.kt` → Java | 同上 |
| `CustomMeltableJson.kt` → Java | 同上 |
| `CustomWitchWaterWorld.kt` → Java | 同上 |

### Phase 12：命令 + API + 配方层（~5 文件）

| 类别 | 文件 | 改造内容 |
|------|------|---------|
| 命令 | `CommandReloadConfig.java` | 包名替换，`ExNihiloCreatio.MODID` → `Reference.MOD_ID` |
| 命令 | `CommandHandNBT.java` | 包名替换 |
| API | `ExNihiloCreatioAPI.kt` → Java | KT→Java |
| 配方 | `Recipes.java` | 移除 Sieve/Grinder/EndCake/水车石轴等不需要的配方 |
| 配方默认值 | `ExNihiloDefaultRecipes.java` | 精简，只保留桶、坩埚、堆肥、钩子等配方 |
| 排除 | `recipe/defaults/` 下所有文件（约12个） | |

### Phase 13：入口 + 代理层（~8 文件）

| 类别 | 文件 | 改造内容 |
|------|------|---------|
| 创造标签 | `CreativeTabExNihiloCreatio.java` | **关键**：图标改为保留的方块（如 `ModBlocks.barrelWood`） |
| 方块注册 | `ModBlocks.java` | **关键**：移除排除的方块实例和 TileEntity 注册。注册方式从 `RegistryEvent` 改为适配 CMIICore 的方式 |
| 物品注册 | `ModItems.java` | **关键**：移除排除的物品。移除 `registerItemsLowest()` 中的 OreRegistry 调用。保留 OreDictionary 中非筛矿相关的注册 |
| 流体注册 | `ModFluids.java` | 包名替换 |
| 附魔注册 | `ModEnchantments.java` | 包名替换 |
| 实体注册 | `ModEntities.java` | 简化（无 ProjectileStone） |
| 代理 | `CommonProxy.java`, `ClientProxy.java`, `ServerProxy.java` | **关键**：集成到 CMIICore 现有的代理体系。移除模块加载、TOP/Waila 兼容、CraftTweaker 加载 |
| 主 Mod 类 | `CMIICore.java` | **关键**：在 preInit/init/postInit 中添加方块/物品注册、网络初始化、事件注册 |

---

## 六、关键技术改造要点

### 6.1 注册方式改造

ExNihilo 使用 Forge 的 `RegistryEvent`：

```java
// ExNihilo 方式（CommonProxy.java）
@SubscribeEvent
public static void registerBlocks(RegistryEvent.Register<Block> event) {
    ModBlocks.registerBlocks(event.getRegistry());
}
```

CMIICore 使用 Cleanroom，需要确认注册方式。可能的方案：
1. Cleanroom 仍然支持 `RegistryEvent`（推荐先验证）
2. 使用 `GameRegistry.register()` 手动注册

**处理方案**：在 CommonProxy 或新的 ExNihiloProxy 中添加 `@Mod.EventBusSubscriber` 注解类处理注册事件。

### 6.2 配置系统改造

ExNihilo 使用 Forge `@Config` 注解系统。如果 Cleanroom 不支持：
- **方案 A**：使用 Cleanroom 自带的配置系统
- **方案 B**：使用 Gson 手动 JSON 配置（CMIICore 已有类似的自定义 JSON 注册）
- **方案 C**：保留 `@Config` 注解，测试是否能编译通过

### 6.3 Lombok 依赖

ExNihilo 中 `TileBarrel.java` 使用 `@Getter`（Lombok）。CMIICore 没有 Lombok 依赖。

**处理方案**：为每个 `@Getter` 字段手动生成 public getter 方法。

### 6.4 Kotlin → Java 转换

关键模式对照：

| Kotlin | Java |
|--------|------|
| `data class Foo(val a: Int, val b: String)` | 手动写 POJO + 构造函数 + getter + equals/hashCode/toString |
| `fun foo(a: Int = 0)` | 写 2 个重载方法：`foo(int a)` 和 `foo()` |
| `list.map { ... }.filter { ... }` | `for` 循环 + `if` 判断 + `new ArrayList` |
| `companion object { const val X = "x" }` | `public static final String X = "x";` |
| `object Foo { fun bar() {} }` | `public class Foo { public static void bar() {} }` + 私有构造 |
| `?.let { }` | `if (x != null) { ... }` |

### 6.5 硬编码引用替换

| 原代码 | 替换为 |
|--------|--------|
| `ExNihiloCreatio.MODID` | `Reference.MOD_ID` |
| `ExNihiloCreatio.VERSION` | `Reference.VERSION` |
| `ExNihiloCreatio.tabExNihilo` | `CreativeTabExNihilo.tabExNihilo` |
| `exnihilocreatio` 字符串 | `Reference.MOD_ID` |
| `new File(configDirectory, "xxx.json")` | 保留文件名不变 |

### 6.6 模块系统移除

ExNihilo 有一个 `modules` 系统用于兼容 TinkersConstruct、Forestry 等。本次不迁移模块系统：
- 移除 `ExNihiloCreatio.loadedModules` 相关代码
- 移除 `IExNihiloCreatioModule` 接口及相关类
- `TileBarrel.java` 中的 `MooFluidsEtc` 代码改为始终跳过

---

## 七、风险评估

| 风险 | 等级 | 对策 |
|------|------|------|
| Cleanroom 不支持 Forge `@Config` | 🟡 中 | Phase 1 验证，不通过则用方案 B |
| Cleanroom 不支持 `@Mod.EventBusSubscriber` | 🟢 低 | Cleanroom 是 Forge fork，大概率支持 |
| KT→Java 转换引入 bug | 🟡 中 | 逐 Phase 验证编译，对比原逻辑 |
| 渲染器在 Cleanroom 下不兼容 | 🟡 中 | Phase 9 时先编译通过，运行测试 |
| JEI API 版本不匹配 | 🟢 低 | JEI 是稳定 API |
| 原项目 `libs/` 中的 jar 依赖丢失 | 🟢 低 | 排除筛矿体系后，关键的 jar 依赖（CodeChickenLib等）主要影响的是筛矿兼容，不影响核心方块 |
| 资源文件（模型JSON、材质）缺失 | 🔴 高 | **本次文档只涉及 Java 代码迁移，资源文件（textures/models/lang）需要单独迁移** |

---

## 八、资源文件迁移（后续任务）

ExNihiloCreatio 的资源文件位于 `src/main/resources/assets/exnihilocreatio/` 下，包括：

```
assets/exnihilocreatio/
├── blockstates/       (方块状态 JSON)
├── models/
│   ├── block/         (方块模型 JSON)
│   └── item/          (物品模型 JSON)
├── textures/
│   ├── blocks/        (方块材质 PNG)
│   └── items/         (物品材质 PNG)
├── lang/              (语言文件 .lang)
└── recipes/           (合成配方 JSON)
```

这些需要复制到 `src/main/resources/assets/cmiicore/` 并排除筛矿相关资源。

---

## 九、Phase 0 启动检查清单

开始 Phase 0 前需确认：

- [ ] Cleanroom 是否支持 `@Config` 注解？（编译 ExNihilo 的 ModConfig 测试）
- [ ] Cleanroom 是否支持 `RegistryEvent` 注册方式？
- [ ] CMIICore 的 `gradle.properties` 是否需要修改以支持更多依赖（如 JEI）？
- [ ] 新包 `com.cmii.cmiicore.exnihilo` 是否被 Mixin 配置覆盖？

---

**文档版本**：v1.0
**生成日期**：2026-05-18