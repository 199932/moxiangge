# 墨香阁 · 完整使用指南

> 墨香阁是一个可以让任何人都能在线写文、看文的小平台。
> 采用「网站 + Android APP 壳」架构，方便日后扩展。

---

## 📁 目录结构

```
/workspace/
├── server.js              # 后端（Node.js，支持本地和云端部署）
├── package.json           # 依赖声明
├── render.yaml          # Render 云平台部署配置
├── Procfile           # 备用：Heroku 类平台兼容
├── README.md          # 本文
├── public/
│   ├── index.html   # 主页（文章列表）
│   ├── write.html   # 写文章页
│   ├── read.html    # 阅读页
│   └── css/style.css  # 全站样式（古典书卷风格）
└── android-app/
    ├── app/build/outputs/apk/release/app-release.apk
    └── ...            # APP 源码与构建文件
```

---

## 🚀 第一步：本地试一下

先在本机跑起来，确认功能正常。

```bash
cd /workspace
npm install          # 装依赖
node server.js        # 启动服务
```

浏览器打开：http://localhost:3000/

功能：
- 看文章列表
- 点击「提笔写作」写一篇文章
- 所有文章自动保存在 data/articles.json

---

## 🌐 第二步：把网站部署到公网上（方案一：Render 免费版）

### 前置条件
- 一个 GitHub 账号（免费注册）
- 一个 Render 账号（免费注册）

### 具体步骤

1. 登录 GitHub → 右上角 `+` → `New repository`
2. Repository name 填：`moxiangge`
3. 点 `Create repository`
4. 进入仓库后，点击 `Upload files`
5. 把以下文件/文件夹拖进去：
   - `server.js`
   - `package.json`
   - `render.yaml`
   - `Procfile`
   - 整个 `public/` 文件夹
6. 页面底部 `Commit changes`
7. 打开 https://render.com → 用 GitHub 登录
8. 控制台点 `New +` → `Web Service`
9. 选你刚上传的 `moxiangge` 仓库 → `Connect`
10. 填表单：
    - Name: `moxiangge`（会成为你的网址前缀）
    - Region: Singapore
    - Branch: `main`
    - Runtime: Node
    - Build Command: `npm install`
    - Start Command: `npm start`
    - Plan: Free
11. 点 `Create Web Service`，等 2-5 分钟
12. 页面顶部会出现你的公网网址，类似：`https://moxiangge.onrender.com`

### ⚠️ 关于免费版限制
- 首次打开会有冷启动（约 10-30 秒），之后正常。
- 文章存在本地，如需要长期持久化，建议升级付费版（约 7 美元/月）

---

## 📱 第三步：把 APP 改成打开你的网址

APP 已打包好，下载地址：`/workspace/moxiangge-app-v1.1.apk`

目前 APP 默认打开 `https://moxiangge.onrender.com`（示例地址）。

如果你的部署地址不同，告诉我实际网址，我可以重新编译一份新的 APK。

---

## 📦 下载与分享

- APK 下载：`/workspace/moxiangge-app-v1.1.apk`

---

## 📋 常见问题

**Q: 如何在 Android 安装 APK？**
A: 手机上打开 APK，手机上打开文件管理器，找到该文件后点击安装。第一次可能需要在「设置 → 安全」中允许「未知来源」。

**Q: 我改了代码，如何让网站同步更新？**
A: 重新上传文件到 GitHub，Render 会自动重新部署。

**Q: 能不能换一个国内服务器？**
A: 可以。任何支持 Node.js 的平台都可以（阿里云、腾讯云等）。

**Q: 我的文章数据怎么办？**
A: 目前文章保存在服务器的 `data/articles.json 文件中。如果希望持久存储，可升级为数据库（SQLite 或云数据库）。

---

墨香阁 · v1.1 · 2026-06
