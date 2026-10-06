# Full Stack Project

前后端分离项目：

- `frontend`：Vue 3 + Vite 前端
- `backend`：Node.js + Express 后端

## 启动

```powershell
cd frontend
npm install
npm run dev
```

```powershell
cd backend
npm install
npm run dev
```

前端默认地址：`http://localhost:5173`  
后端默认地址：`http://localhost:3000`

## 开发约定

### Commit 规范

本项目采用约定式提交（Conventional Commits v1.0.0），统一使用小写英文类型、可选英文范围和中文描述：

```text
<type>[optional scope][!]: <description>
```

常用示例：

```text
feat(frontend): 新增登录页面
fix(backend): 修正 CORS 预检请求失败
docs: 补充项目启动说明
build(deps): 升级依赖版本
```

提交要求：

- 类型使用 `feat`、`fix`、`docs`、`style`、`refactor`、`perf`、`test`、`build`、`ci`、`chore` 或 `revert`
- 描述使用祈使句，不加句号，首行建议不超过 50 个字符、硬上限 72 个字符
- 一次提交只做一件事；前后端独立改动尽量拆分
- 破坏性变更使用 `!` 或 `BREAKING CHANGE:` 明确标记
- 不提交 `node_modules/`、`.env`、`dist/`、`target/` 等文件

完整规则见 [docs/commit-convention.md](./docs/commit-convention.md)。

### 分支与合并

功能分支建议使用 `feat/user-login`、`fix/cors-preflight` 这类与提交类型对应的命名。合并时建议使用 Squash Merge，并由维护者按本规范整理提交信息。

## Vibe Coding

使用 AI 辅助开发时，请遵循 [VIBE-CODING.md](./VIBE-CODING.md) 中的工作方式：先明确目标和验收标准，再小步修改、检查 diff、运行验证，最后使用符合本项目规范的 commit 提交。
