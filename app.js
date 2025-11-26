const express = require("express");
const session = require("express-session");
const bcrypt = require("bcryptjs");
const { User, Post, Comment } = require("./models");
require("dotenv").config();

const app = express();

// Middleware
app.use(express.urlencoded({ extended: false }));
app.use(express.json());
app.use(session({
  secret: process.env.SESSION_SECRET,
  resave: false,
  saveUninitialized: false
}));

// Set view engine
app.set("view engine", "ejs");

// Auth middleware
const requireAuth = (req, res, next) => {
  if (req.session.userId) {
    next();
  } else {
    res.redirect("/login");
  }
};

// Routes
app.get("/", async (req, res) => {
  let user = null;
  if (req.session.userId) {
    user = await User.findByPk(req.session.userId);
  }
  res.render("index", { user });
});

// Register routes
app.get("/register", (req, res) => {
  res.render("register", { error: null });
});

app.post("/register", async (req, res) => {
  try {
    const { name, email, password } = req.body;
    const hashedPassword = await bcrypt.hash(password, 10);
    
    const user = await User.create({
      name,
      email,
      password: hashedPassword
    });
    
    req.session.userId = user.id;
    res.redirect("/");
  } catch (error) {
    res.render("register", { error: "Registration failed: " + error.message });
  }
});

// Login routes
app.get("/login", (req, res) => {
  res.render("login", { error: null });
});

app.post("/login", async (req, res) => {
  try {
    const { email, password } = req.body;
    const user = await User.findOne({ where: { email } });
    
    if (user && await bcrypt.compare(password, user.password)) {
      req.session.userId = user.id;
      res.redirect("/");
    } else {
      res.render("login", { error: "Invalid email or password" });
    }
  } catch (error) {
    res.render("login", { error: "Login failed" });
  }
});

// Logout
app.get("/logout", (req, res) => {
  req.session.destroy();
  res.redirect("/");
});

// Posts routes (we'll add these next)
app.get("/posts", async (req, res) => {
  const posts = await Post.findAll({ include: { model: User, as: "User" }, order: [['createdAt', 'DESC']] });
  let user = null;
  if (req.session.userId) {
    user = await User.findByPk(req.session.userId);
  }
  res.render("posts", { posts, user });
});

// Start server
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});

// Create new post (form)
app.get("/posts/new", requireAuth, async (req, res) => {
  const user = await User.findByPk(req.session.userId);
  res.render("new-post", { user });
});

// Create new post (submit)
app.post("/posts", requireAuth, async (req, res) => {
  try {
    await Post.create({
      title: req.body.title,
      content: req.body.content,
      userId: req.session.userId
    });
    res.redirect("/posts");
  } catch (error) {
    res.redirect("/posts/new");
  }
});

// View single post with comments
app.get("/posts/:id", async (req, res) => {
  const post = await Post.findByPk(req.params.id, { 
    include: [{ model: User, as: "User" }, { model: Comment, as: "comments", include: { model: User, as: "User" } }] 
  });
  let user = null;
  if (req.session.userId) {
    user = await User.findByPk(req.session.userId);
  }
  res.render("post", { post, user });
});

// Add comment to post
app.post("/posts/:id/comments", requireAuth, async (req, res) => {
  try {
    await Comment.create({
      content: req.body.content,
      userId: req.session.userId,
      postId: req.params.id
    });
    res.redirect(`/posts/${req.params.id}`);
  } catch (error) {
    res.redirect(`/posts/${req.params.id}`);
  }
});
