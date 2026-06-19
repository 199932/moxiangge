// ============================================
// 墨香阁 - 后端服务 (支持本地 + 云平台部署)
// ============================================
const express = require('express');
const fs = require('fs');
const path = require('path');

const app = express();
const PORT = process.env.PORT || 3000;          // 云平台自动给 PORT
const DATA_FILE = process.env.DATA_FILE
  || path.join(__dirname, 'data', 'articles.json');

// 初始化文章数据文件
function ensureDataFile() {
  const dir = path.dirname(DATA_FILE);
  if (!fs.existsSync(dir)) fs.mkdirSync(dir, { recursive: true });
  if (!fs.existsSync(DATA_FILE)) {
    const initialData = {
      articles: [
        {
          id: 'intro-' + Date.now(),
          title: '墨香阁序',
          author: '阁主',
          category: '散文',
          content: '欢迎来到墨香阁。\n\n这里是一方笔墨留香之地，文人墨客汇聚之所。\n\n在这里，你可以挥毫泼墨，写下心中的故事；也可以漫步书海，品读他人的笔墨精华。\n\n愿你在此，墨香四溢，笔下生花。',
          views: 0,
          createdAt: Date.now() - 100000
        },
        {
          id: 'demo-' + Date.now(),
          title: '春夜洛城闻笛',
          author: '李白',
          category: '诗词',
          content: '谁家玉笛暗飞声，散入春风满洛城。\n\n此夜曲中闻折柳，何人不起故园情。',
          views: 0,
          createdAt: Date.now()
        }
      ]
    };
    fs.writeFileSync(DATA_FILE, JSON.stringify(initialData, null, 2));
  }
}

function readData() {
  ensureDataFile();
  try {
    return JSON.parse(fs.readFileSync(DATA_FILE, 'utf8'));
  } catch (e) {
    return { articles: [] };
  }
}

function writeData(data) {
  fs.writeFileSync(DATA_FILE, JSON.stringify(data, null, 2));
}

function generateId() {
  return 'article-' + Date.now() + '-' + Math.random().toString(36).substr(2, 5);
}

// ============================================
// 中间件
// ============================================
app.use(express.json({ limit: '10mb' }));
app.use(express.urlencoded({ extended: true, limit: '10mb' }));
app.use(express.static(path.join(__dirname, 'public')));

// ============================================
// API
// ============================================
app.get('/api/articles', (req, res) => {
  const data = readData();
  const articles = [...data.articles]
    .sort((a, b) => b.createdAt - a.createdAt)
    .map(a => ({
      id: a.id,
      title: a.title,
      author: a.author,
      category: a.category,
      content: a.content,
      preview: (a.content || '').substring(0, 100),
      views: a.views || 0,
      createdAt: a.createdAt
    }));
  res.json({ success: true, articles });
});

app.get('/api/articles/:id', (req, res) => {
  const data = readData();
  const article = data.articles.find(a => a.id === req.params.id);
  if (!article) return res.status(404).json({ success: false, error: '文章不存在' });
  article.views = (article.views || 0) + 1;
  writeData(data);
  res.json({ success: true, article });
});

app.post('/api/articles', (req, res) => {
  const { title, author, category, content } = req.body || {};
  if (!title || !content) return res.status(400).json({ success: false, error: '标题和内容不能为空' });
  const data = readData();
  const article = {
    id: generateId(),
    title: String(title).trim(),
    author: String(author || '匿名').trim(),
    category: String(category || '随笔').trim(),
    content: String(content),
    views: 0,
    createdAt: Date.now()
  };
  data.articles.push(article);
  writeData(data);
  res.json({ success: true, article: { id: article.id, title: article.title, author: article.author, category: article.category } });
});

app.delete('/api/articles/:id', (req, res) => {
  const data = readData();
  const idx = data.articles.findIndex(a => a.id === req.params.id);
  if (idx === -1) return res.status(404).json({ success: false, error: '文章不存在' });
  data.articles.splice(idx, 1);
  writeData(data);
  res.json({ success: true });
});

// ============================================
// 首页路由 (访问根路径 / 时返回主页)
// ============================================
app.get('/', (req, res) => {
  res.sendFile(path.join(__dirname, 'public', 'index.html'));
});

// ============================================
// 启动
// ============================================
ensureDataFile();
app.listen(PORT, '0.0.0.0', () => {
  console.log('墨香阁已启动');
  console.log('本地访问: http://localhost:' + PORT + '/');
  console.log('数据文件: ' + DATA_FILE);
});
