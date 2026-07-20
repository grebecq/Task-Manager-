// =============================================================
// Task System — фронтенд без сборки, чистый JS.
//
// ВАЖНО: бэкенд для /auth/login ещё не реализован на момент
// написания этого файла. Контракт, который здесь ожидается:
//
//   POST /auth/login
//   body: { "username": "...", "password": "..." }
//   ответ 200: { "token": "<jwt строка>" }
//   ответ 401: если логин/пароль неверные
//
// Когда будете делать SecurityConfig + JwtService на бэкенде —
// сверяйтесь с этим контрактом (или поменяйте здесь под свой).
// =============================================================

const API_BASE = ""; // тот же origin, т.к. фронт отдаётся тем же Spring Boot

const els = {
  loginScreen: document.getElementById("loginScreen"),
  appScreen: document.getElementById("appScreen"),
  loginForm: document.getElementById("loginForm"),
  loginError: document.getElementById("loginError"),
  username: document.getElementById("username"),
  password: document.getElementById("password"),
  whoami: document.getElementById("whoami"),
  logoutBtn: document.getElementById("logoutBtn"),
  taskList: document.getElementById("taskList"),
  emptyState: document.getElementById("emptyState"),
  statusBanner: document.getElementById("statusBanner"),
  newTaskBtn: document.getElementById("newTaskBtn"),
  createModal: document.getElementById("createModal"),
  createForm: document.getElementById("createForm"),
  cancelCreate: document.getElementById("cancelCreate"),
};

// ---------- Хранение токена ----------
// Кладём в localStorage, чтобы токен переживал перезагрузку страницы.
// Компромисс: localStorage доступен через XSS, но для учебного проекта
// это стандартный и самый простой вариант.
const TOKEN_KEY = "task_system_token";

function saveToken(token) {
  localStorage.setItem(TOKEN_KEY, token);
}
function getToken() {
  return localStorage.getItem(TOKEN_KEY);
}
function clearToken() {
  localStorage.removeItem(TOKEN_KEY);
}

// ---------- Обёртка над fetch, которая добавляет токен ----------
async function apiFetch(path, options = {}) {
  const token = getToken();
  const headers = {
    "Content-Type": "application/json",
    ...(options.headers || {}),
  };
  if (token) {
    headers["Authorization"] = "Bearer " + token;
  }

  const response = await fetch(API_BASE + path, { ...options, headers });

  if (response.status === 401) {
    // Токен просрочен или невалиден — возвращаем на логин.
    clearToken();
    showLogin();
    throw new Error("Сессия истекла, войдите заново");
  }

  return response;
}

// ---------- Переключение экранов ----------
function showLogin() {
  els.loginScreen.hidden = false;
  els.appScreen.hidden = true;
}
function showApp() {
  els.loginScreen.hidden = true;
  els.appScreen.hidden = false;
  loadTasks();
}

// ---------- Логин ----------
els.loginForm.addEventListener("submit", async (e) => {
  e.preventDefault();
  els.loginError.hidden = true;

  const username = els.username.value.trim();
  const password = els.password.value;

  try {
    const response = await fetch(API_BASE + "/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password }),
    });

    if (!response.ok) {
      throw new Error("Неверный логин или пароль");
    }

    const data = await response.json();
    saveToken(data.token);
    els.whoami.textContent = username;
    showApp();
  } catch (err) {
    els.loginError.textContent = err.message;
    els.loginError.hidden = false;
  }
});

els.logoutBtn.addEventListener("click", () => {
  clearToken();
  showLogin();
});

// ---------- Загрузка и рендер задач ----------
async function loadTasks() {
  hideBanner();
  try {
    const response = await apiFetch("/tasks");
    if (!response.ok) throw new Error("Не удалось загрузить задачи");
    const tasks = await response.json();
    renderTasks(tasks);
  } catch (err) {
    showBanner(err.message);
  }
}

function renderTasks(tasks) {
  els.taskList.innerHTML = "";

  if (!tasks || tasks.length === 0) {
    els.emptyState.hidden = false;
    return;
  }
  els.emptyState.hidden = true;

  for (const task of tasks) {
    els.taskList.appendChild(renderTaskCard(task));
  }
}

function renderTaskCard(task) {
  const card = document.createElement("div");
  card.className = "task-card";

  const statusClass = "badge-" + task.status.toLowerCase();
  const priorityClass = "priority-" + (task.priority || "medium").toLowerCase();

  card.innerHTML = `
    <div class="task-info">
      <span class="task-id">#${task.id}</span>
      <span class="badge ${statusClass}">${translateStatus(task.status)}</span>
      <div class="task-meta">
        <span class="${priorityClass}">Приоритет: ${translatePriority(task.priority)}</span>
        <span>Исполнитель: ${task.assignedUserId ?? "—"}</span>
        <span>Дедлайн: ${task.deadlineDate ?? "—"}</span>
      </div>
    </div>
    <div class="task-actions"></div>
  `;

  const actions = card.querySelector(".task-actions");

  if (task.status === "CREATED") {
    actions.appendChild(makeActionButton("Начать", () => runAction(`/tasks/${task.id}/start`, "POST")));
  }
  if (task.status === "IN_PROGRESS") {
    actions.appendChild(makeActionButton("Завершить", () => runAction(`/tasks/${task.id}/complete`, "POST")));
  }
  actions.appendChild(makeActionButton("Удалить", () => runAction(`/tasks/${task.id}`, "DELETE"), true));

  return card;
}

function makeActionButton(label, onClick, danger = false) {
  const btn = document.createElement("button");
  btn.className = "btn-sm" + (danger ? " danger" : "");
  btn.textContent = label;
  btn.addEventListener("click", onClick);
  return btn;
}

async function runAction(path, method) {
  hideBanner();
  try {
    const response = await apiFetch(path, { method });
    if (!response.ok) {
      const body = await safeJson(response);
      throw new Error(body?.message || "Действие не выполнено");
    }
    await loadTasks();
  } catch (err) {
    showBanner(err.message);
  }
}

async function safeJson(response) {
  try {
    return await response.json();
  } catch {
    return null;
  }
}

// ---------- Создание задачи ----------
els.newTaskBtn.addEventListener("click", () => {
  els.createModal.hidden = false;
});
els.cancelCreate.addEventListener("click", () => {
  els.createModal.hidden = true;
});

els.createForm.addEventListener("submit", async (e) => {
  e.preventDefault();

  const body = {
    creatorId: Number(document.getElementById("creatorId").value),
    assignedUserId: document.getElementById("assignedUserId").value
      ? Number(document.getElementById("assignedUserId").value)
      : null,
    priority: document.getElementById("priority").value,
    deadlineDate: document.getElementById("deadlineDate").value || null,
    createDateTime: new Date().toISOString(),
  };

  try {
    const response = await apiFetch("/tasks", {
      method: "POST",
      body: JSON.stringify(body),
    });
    if (!response.ok) {
      const errBody = await safeJson(response);
      throw new Error(errBody?.message || "Не удалось создать задачу");
    }
    els.createModal.hidden = true;
    els.createForm.reset();
    await loadTasks();
  } catch (err) {
    showBanner(err.message);
  }
});

// ---------- Баннер ошибок ----------
function showBanner(message) {
  els.statusBanner.textContent = message;
  els.statusBanner.hidden = false;
}
function hideBanner() {
  els.statusBanner.hidden = true;
}

// ---------- Переводы для отображения ----------
function translateStatus(status) {
  return { CREATED: "Создана", IN_PROGRESS: "В работе", DONE: "Завершена" }[status] || status;
}
function translatePriority(priority) {
  return { LOW: "Низкий", MEDIUM: "Средний", HIGH: "Высокий" }[priority] || priority || "—";
}

// ---------- Точка входа ----------
if (getToken()) {
  showApp();
} else {
  showLogin();
}
