# Git 提交信息规范（Commit Convention）

> 本项目采用 **约定式提交（Conventional Commits）v1.0.0**
> 规范原文：<https://www.conventionalcommits.org/zh-hans/v1.0.0/>
> 本规范基于 Angular 提交约定，被 Vue、React、Angular 等主流开源项目广泛采用。

---

## 目录

- [1. 为什么要规范提交信息](#1-为什么要规范提交信息)
- [2. 提交信息结构](#2-提交信息结构)
- [3. 类型 type 一览](#3-类型-type-一览)
- [4. 范围 scope](#4-范围-scope)
- [5. 描述 description 怎么写](#5-描述-description-怎么写)
- [6. 正文 body 与脚注 footer](#6-正文-body-与脚注-footer)
- [7. 破坏性变更 BREAKING CHANGE](#7-破坏性变更-breaking-change)
- [8. 完整示例集](#8-完整示例集)
- [9. 反例对照表](#9-反例对照表)
- [10. 与版本号 SemVer / CHANGELOG 的关系](#10-与版本号-semver--changelog-的关系)
- [11. 工具链（可选，自动校验）](#11-工具链可选自动校验)
- [12. 本项目落地约定](#12-本项目落地约定)
- [13. 速查卡](#13-速查卡)

---

## 1. 为什么要规范提交信息

| 收益 | 说明 |
|---|---|
| **可读的历史** | 半年后回看 `git log`，一眼知道每次改动是什么性质 |
| **自动化 CHANGELOG** | 工具（standard-version / release-please）能直接生成发布日志 |
| **自动版本号** | 依据类型自动决定语义化版本该升 `MAJOR` / `MINOR` / `PATCH` |
| **可定位问题** | `git log --grep="^fix"` 能快速筛出所有修复记录，方便 `git bisect` |
| **团队协作** | 统一格式，Code Review 时不用猜"这个提交到底干了什么" |
| **触发 CI/CD** | 依据类型决定是否触发构建、部署、发布流程 |

**核心原则**：一次提交只做一件事。如果你写完描述发现必须用 "和"、"以及"、"顺便" 来连接，说明应该拆成多次提交。

---

## 2. 提交信息结构

```
<type>[optional scope][!]: <description>

[optional body]

[optional footer(s)]
```

中文对照：

```
<类型>[可选范围][!]: <描述>

[可选正文]

[可选脚注]
```

**结构硬性要求**（来自规范原文，MUST 级别）：

1. 类型前缀**必须**是名词（如 `feat`、`fix`）
2. 类型后**可选**跟一个用圆括号包围的范围，**可选**跟一个 `!`
3. 类型/范围之后**必须**是**英文半角冒号 + 一个空格**
4. 描述**必须**紧跟在冒号空格之后，**不换行**
5. 正文若存在，**必须**与描述之间空**一整行**
6. 脚注若存在，**必须**与正文之间空**一整行**

```
┌─ 类型（必填）
│      ┌─ 范围（可选）      ┌─ 破坏性变更标记（可选）
▼      ▼                    ▼
feat   (order)              !  :  新增订单超时自动取消
│                                 └─ 描述（必填，一行内写完）
├─ 空行
├─ 正文（可选）：说明「为什么改」，而非「改了什么」
├─ 空行
└─ 脚注（可选）：BREAKING CHANGE: xxx / Closes #12
```

---

## 3. 类型 type 一览

以下 8 个是 `@commitlint/config-conventional` 官方推荐集合：

| 类型 | 含义 | 典型场景 | 对应 SemVer |
|---|---|---|---|
| `feat` | **新增功能** | 新增接口、新增页面、新增业务能力 | MINOR |
| `fix` | **修复 bug** | 修好一个报错、修正错误逻辑 | PATCH |
| `docs` | **仅文档变更** | 改 README、改注释、改本规范 | 无 |
| `style` | **代码格式**（不影响逻辑） | 缩进、空格、分号、格式化工具结果 | 无 |
| `refactor` | **重构**（不改功能、不修 bug） | 改函数名、抽组件、调整目录结构 | 无 |
| `perf` | **性能优化** | 减少查询次数、加缓存、优化渲染 | 无 |
| `test` | **测试相关** | 新增/修改/删除测试用例 | 无 |
| `build` | **构建系统 / 依赖** | 升级依赖版本、改打包配置、Dockerfile | 无 |
| `ci` | **持续集成配置** | 改 GitHub Actions、Jenkins 流水线 | 无 |
| `chore` | **杂项**（其他都不属于时） | 改 `.gitignore`、改编辑器配置 | 无 |
| `revert` | **回滚某次提交** | 撤销之前的错误提交 | 视情况 |

**易混淆点辨析**：

- `style` vs `refactor`：`style` 只动**空白与格式**（代码语义完全不变）；只要动了**结构、命名、逻辑组织**，就是 `refactor`。
- `fix` vs `refactor`：`refactor` 后行为应该**完全一致**。如果行为变了，要么是 `fix`，要么是 `feat`。
- `build` vs `ci`：`build` 管**怎么构建产物**（依赖、打包器）；`ci` 管**流水线怎么跑**（触发条件、步骤）。
- `chore` 不是垃圾桶：优先判断能否归入上面 7 类，实在不属于再用 `chore`。

**类型大小写**：规范允许大小写，但**同一项目必须一致**。本项目统一用**小写英文**。

---

## 4. 范围 scope

范围是**可选**的，用来标明改动落在哪一块代码，格式为一个名词、用圆括号包围。

本项目的建议范围：

| 范围 | 对应位置 |
|---|---|
| `frontend` | 前端整体改动，跨多个模块时用 |
| `backend` | 后端整体改动，跨多个模块时用 |
| `order` | 订单相关业务模块（示例，按实际模块替换） |
| `auth` | 登录 / 权限相关 |
| `api` | 接口定义、请求封装、数据结构约定 |
| `db` | 数据库脚本、表结构、迁移 |
| `deps` | 依赖升级（配合 `build:`） |
| `docs` | 文档（配合 `docs:` 时通常省略，避免 `docs(docs):`） |

**写法规则**：

- 全小写，用**英文**，模块名尽量短
- 不要写成文件路径（`feat(src/views/order/List.vue):` ❌）
- 一次改动跨多个范围时，**优先拆成多个提交**；实在拆不开就省略范围

---

## 5. 描述 description 怎么写

这是提交信息里最重要的一行，`git log --oneline` 显示的正是它。

**六条要求**：

1. **用祈使句**（动词开头），描述"这次提交**会**做什么"，而不是"我做了什么"
   - ✅ `fix: 修正订单金额重复计算`
   - ❌ `fix: 修正了订单金额的重复计算` / `fixed the bug`
2. **首行不超过 50 个字符**（中文字符按 2 个宽度算，即约 25 个汉字），硬上限 72
3. **结尾不加句号** `.` 或 `。`
4. **首字母/首字不大写**的问题：英文小写开头（`feat: add user login`），中文无此约束
5. **说清"做了什么"，不要写"改了点东西"** 这类无信息量的描述
6. **不要写"WIP"、"临时提交"、"1111"、"update"** 这类内容

**语言选择**：类型与范围用**英文**，描述可以用中文或英文，但**整个项目保持一致**。本项目采用 **英文 type + 中文描述**。

---

## 6. 正文 body 与脚注 footer

### 正文 body（可选）

用于解释**为什么这么改**（`how` 和 `what` 看代码/描述就够了）。

- 与描述之间空**一整行**
- 每行建议不超过 72 字符，便于 `git log` 在终端中展示
- 可分多个段落，段落之间空行

### 脚注 footer（可选）

用于关联 Issue / PR / 记录元信息，格式为 `令牌: 值` 或 `令牌 #值`。

常用令牌：

| 令牌 | 用途 |
|---|---|
| `Closes #12` | 关闭对应 Issue |
| `Fixes #34` | 修复对应 Issue |
| `Refs #56` | 仅引用，不关闭 |
| `Reviewed-by: 张三` | 代码评审人 |
| `Co-authored-by: 李四 <li@example.com>` | 协作提交 |

> 规范要求脚注令牌用 `-` 连接（如 `Reviewed-by`），唯一例外是 `BREAKING CHANGE` 用空格。

---

## 7. 破坏性变更 BREAKING CHANGE

**定义**：会让**已有调用方 / 已上线的客户端 / 已存的数据库数据失效**的改动。例如删掉一个接口、改掉响应字段名、把必填参数改成必填。

**两种标记方式**（满足其一即可）：

**方式一：`!` 标记（推荐，简洁）**

```
feat(api)!: 订单列表接口响应改为分页结构
```

**方式二：脚注 `BREAKING CHANGE:`（推荐，可写清楚迁移方式）**

```
feat(api): 订单列表接口响应改为分页结构

BREAKING CHANGE: `data` 字段由数组改为 `{ list, total, page, size }` 对象，
前端需同步修改 `src/api/order.js` 中的解析逻辑。
```

**规则要点**：

- `BREAKING CHANGE` **必须全大写**（这是规范中唯一强制大小写的地方）
- 用 `!` 时，描述部分仍应说明破坏性内容
- 可以同时使用 `!` 和脚注（两者含义一致，不冲突）
- 出现 `BREAKING CHANGE` 的提交，无论什么类型，对应 SemVer 的 **MAJOR** 版本

---

## 8. 完整示例集

### 8.1 最简形式（日常最常用）

```
feat: 新增订单创建接口
```

```
fix: 修正库存扣减的并发问题
```

```
docs: 补充 README 的启动说明
```

### 8.2 带范围

```
feat(frontend): 新增订单列表页
fix(backend): 修复 CORS 预检请求失败
chore(deps): 升级 express 到 4.21.x
```

### 8.3 带正文（解释为什么）

```
fix(api): 修正订单金额重复计算

运费在 service 层与 controller 层各加了一次，导致总价偏高。
统一由 service 层计算，controller 只做参数校验。
```

### 8.4 带正文 + 脚注

```
feat(auth): 新增 JWT 登录态校验

前端每次请求携带 Authorization 头，后端中间件统一校验。

Closes #12
Reviewed-by: 张三
```

### 8.5 破坏性变更（`!` 简写）

```
feat(api)!: 订单列表接口响应改为分页结构
```

### 8.6 破坏性变更（含迁移说明）

```
refactor(db)!: 订单表主键由自增 ID 改为雪花 ID

BREAKING CHANGE: order 表主键类型由 INT 改为 BIGINT，
已有数据需执行 scripts/migrate-order-id.sql 迁移。
```

### 8.7 回滚提交

```
revert: 回滚「新增订单超时自动取消」

Refs: a1b2c3d
```

### 8.8 本项目的首次提交

```
chore: 初始化项目骨架

- frontend: Vue 3 + Vite
- backend: Node.js + Express
- 添加 README 与 .gitignore

Refs: D1
```

---

## 9. 反例对照表

| ❌ 不规范 | ✅ 规范 | 问题 |
|---|---|---|
| `update` | `docs: 更新 README 启动说明` | 没类型、没信息量 |
| `修改bug` | `fix(order): 修正订单状态流转错误` | 没类型、没范围 |
| `feat 新增订单` | `feat: 新增订单创建接口` | 冒号后缺空格 |
| `Feat: 新增订单` | `feat: 新增订单` | 类型大小写与项目约定不一致 |
| `feat: 新增订单.` | `feat: 新增订单` | 描述结尾多句号 |
| `feat: 新增订单创建接口，同时修复了库存bug、顺手升级了依赖` | 拆成 3 个提交 | 一次提交做多件事 |
| `fix: 修复` | `fix(backend): 修复 CORS 预检请求失败` | 描述太笼统，无法定位 |
| `feat: src/views/order/List.vue 加了个分页` | `feat(order): 订单列表新增分页` | 描述写成文件路径 |
| `WIP` | 不要提交，或用 `git stash` | 无意义提交 |
| `fix: 改了点东西` | `fix(backend): 修正空指针导致的 500` | 零信息量 |

---

## 10. 与版本号 SemVer / CHANGELOG 的关系

约定式提交与[语义化版本 SemVer](https://semver.org/lang/zh-CN/)一一对应，这是它能自动发版的基础：

| 提交中出现 | 版本号变化 | 示例 |
|---|---|---|
| `BREAKING CHANGE` 或 `!` | **MAJOR** `1.0.0 → 2.0.0` | 接口不兼容 |
| `feat` | **MINOR** `1.0.0 → 1.1.0` | 向后兼容的新功能 |
| `fix` | **PATCH** `1.0.0 → 1.0.1` | 向后兼容的修复 |
| 其他（`docs`/`style`/`chore`…） | 不升版本 | 不进入 CHANGELOG（或归入"其他"） |

工具据此自动生成 CHANGELOG：

```markdown
## [1.1.0] - 2025-01-15

### Features
* **order:** 新增订单超时自动取消 (a1b2c3d)

### Bug Fixes
* **api:** 修正订单金额重复计算 (d4e5f6a)
```

---

## 11. 工具链（可选，自动校验）

如果希望**机器强制**拦截不合规的提交，可引入以下工具（本项目暂未强制要求）：

```bash
# 1) 提交信息校验（在 commit-msg 钩子里检查格式）
npm i -D @commitlint/cli @commitlint/config-conventional

# 2) Git 钩子管理（自动安装钩子，团队共享）
npm i -D husky
npx husky init

# 3) 交互式生成合规提交信息（可选，降低记忆成本）
npm i -D commitizen cz-conventional-changelog

# 4) 依据提交记录自动升版本 + 生成 CHANGELOG（可选）
npm i -D standard-version
```

`commitlint.config.js` 示例：

```js
export default {
  extends: ['@commitlint/config-conventional'],
  rules: {
    // 类型白名单
    'type-enum': [
      2,
      'always',
      ['feat', 'fix', 'docs', 'style', 'refactor', 'perf', 'test', 'build', 'ci', 'chore', 'revert'],
    ],
    // 描述不能为空、结尾不加句号
    'subject-empty': [2, 'never'],
    'subject-full-stop': [2, 'never', '.'],
    // 首行长度限制（默认 100，这里收紧到 72）
    'header-max-length': [2, 'always', 72],
    // 范围白名单（按需打开）
    // 'scope-enum': [2, 'always', ['frontend', 'backend', 'api', 'order', 'auth', 'db', 'deps']],
  },
};
```

`.husky/commit-msg` 内容：

```sh
npx --no -- commitlint --edit "$1"
```

启用后，写错格式的提交会被直接拒绝，并给出具体错误原因。

---

## 12. 本项目落地约定

结合当前仓库（`frontend` = Vue 3 + Vite，`backend` = Node.js + Express）：

1. **类型**：统一小写英文，优先使用第 3 节的 8 个标准类型
2. **范围**：使用第 4 节的建议列表；跨端改动若拆不开则省略范围
3. **描述**：英文 type + 中文描述，祈使句，不加句号，首行控制在 50 字符内
4. **提交粒度**：一次提交一件事；前端与后端的独立改动**分两次提交**
5. **分支**：功能分支建议命名为 `feat/order-timeout`、`fix/cors-preflight` 这类与提交类型对齐的形式
6. **禁止提交**：`node_modules/`、`.env`、构建产物（`dist/`、`target/`）——确保 `.gitignore` 已覆盖
7. **合并策略**：建议使用 Squash Merge，合并时的提交信息由维护者按本规范清理

---

## 13. 速查卡

```
feat:     新功能            feat(order): 新增订单超时自动取消
fix:      修 bug            fix(api): 修正订单金额重复计算
docs:     文档              docs: 补充 README 启动说明
style:    格式（不改逻辑）    style: 统一缩进为 2 空格
refactor: 重构（不改行为）    refactor(backend): 抽出统一响应封装
perf:     性能优化           perf(frontend): 列表虚拟滚动优化渲染
test:     测试              test: 补充订单状态流转单测
build:    构建 / 依赖        build(deps): 升级 vite 到 6.x
ci:       CI 配置           ci: 新增 push 时自动跑 lint
chore:    杂项              chore: 更新 .gitignore
revert:   回滚              revert: 回滚「新增订单超时自动取消」
```

**一句话模板**：

```
<类型>(<范围>): <做了什么>

<为什么这么做>

BREAKING CHANGE: <不兼容点与迁移方式>
Closes #<Issue 号>
```

**三条铁律**：

1. 冒号后用**一个空格**，描述**一行写完、不加句号**
2. 一次提交**只做一件事**
3. **破坏性变更必须显式标记**（`!` 或 `BREAKING CHANGE:`）

---

_规范来源：[约定式提交 v1.0.0](https://www.conventionalcommits.org/zh-hans/v1.0.0/) · 采用 [CC BY 3.0](https://creativecommons.org/licenses/by/3.0/) 许可_
