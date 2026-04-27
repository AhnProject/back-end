"use client";

import { useState } from "react";

type ApiEnvelope<T> = {
  success: boolean;
  data: T | null;
  message: string;
  errorCode: string | null;
  timestamp: number;
};

type AuthData = {
  accessToken: string;
  tokenType: "Bearer";
  username: string;
  email: string;
  role: string;
};

type DocumentItem = {
  id: string;
  title: string;
  content: string;
  embedding?: number[] | null;
  similarity?: number;
  createdAt: string;
  updatedAt?: string | null;
};

type RecommendData = {
  originalQuery: string;
  refinedQuery: string;
  keywords: string[];
  results: Array<{
    id: string;
    title: string;
    content: string;
    similarity: number;
    createdAt: string;
  }>;
  totalCount: number;
};

async function request<T>(
  path: string,
  init?: RequestInit,
  token?: string
): Promise<ApiEnvelope<T>> {
  const headers = new Headers(init?.headers);
  if (!headers.has("Content-Type") && init?.body) {
    headers.set("Content-Type", "application/json");
  }
  if (token) {
    headers.set("Authorization", `Bearer ${token}`);
  }

  const response = await fetch(path, {
    ...init,
    headers,
  });

  return response.json() as Promise<ApiEnvelope<T>>;
}

export default function TestUiPage() {
  const [token, setToken] = useState("");
  const [signupForm, setSignupForm] = useState({
    username: "",
    email: "",
    password: "",
  });
  const [loginForm, setLoginForm] = useState({
    username: "",
    password: "",
  });
  const [documentForm, setDocumentForm] = useState({
    title: "",
    content: "",
  });
  const [documentId, setDocumentId] = useState("");
  const [searchEmbedding, setSearchEmbedding] = useState("0.1,0.2,0.3");
  const [recommendQuery, setRecommendQuery] = useState("");
  const [log, setLog] = useState("Ready");

  const appendLog = (label: string, payload: unknown) => {
    setLog(`${label}\n${JSON.stringify(payload, null, 2)}`);
  };

  const handleSignup = async () => {
    const result = await request<AuthData>("/api/auth/signup", {
      method: "POST",
      body: JSON.stringify(signupForm),
    });
    if (result.success && result.data) {
      setToken(result.data.accessToken);
    }
    appendLog("signup", result);
  };

  const handleLogin = async () => {
    const result = await request<AuthData>("/api/auth/login", {
      method: "POST",
      body: JSON.stringify(loginForm),
    });
    if (result.success && result.data) {
      setToken(result.data.accessToken);
    }
    appendLog("login", result);
  };

  const handleCreateDocument = async () => {
    const result = await request<{ id: string }>(
      "/api/documents",
      {
        method: "POST",
        body: JSON.stringify(documentForm),
      },
      token
    );
    appendLog("createDocument", result);
  };

  const handleListDocuments = async () => {
    const result = await request<DocumentItem[]>("/api/documents", undefined, token);
    appendLog("listDocuments", result);
  };

  const handleGetDocument = async () => {
    const result = await request<DocumentItem>(
      `/api/documents/${documentId}`,
      undefined,
      token
    );
    appendLog("getDocument", result);
  };

  const handleUpdateDocument = async () => {
    const result = await request<null>(
      `/api/documents/${documentId}`,
      {
        method: "PUT",
        body: JSON.stringify(documentForm),
      },
      token
    );
    appendLog("updateDocument", result);
  };

  const handleDeleteDocument = async () => {
    const result = await request<null>(
      `/api/documents/${documentId}`,
      { method: "DELETE" },
      token
    );
    appendLog("deleteDocument", result);
  };

  const handleSearchDocuments = async () => {
    const embedding = searchEmbedding
      .split(",")
      .map((value) => Number(value.trim()))
      .filter((value) => !Number.isNaN(value));

    const result = await request<DocumentItem[]>(
      "/api/documents/search",
      {
        method: "POST",
        body: JSON.stringify({ embedding, limit: 5, threshold: 0 }),
      },
      token
    );
    appendLog("searchDocuments", result);
  };

  const handleRecommend = async () => {
    const result = await request<RecommendData>("/api/recommend", {
      method: "POST",
      body: JSON.stringify({ query: recommendQuery, topK: 5, threshold: 0.5 }),
    });
    appendLog("recommend", result);
  };

  return (
    <main style={styles.page}>
      <section style={styles.hero}>
        <p style={styles.kicker}>Vercel-ready migration</p>
        <h1 style={styles.title}>Spring Boot API to Next.js</h1>
        <p style={styles.subtitle}>
          Auth, document CRUD, vector search, recommendation, docs, and test UI are
          consolidated here.
        </p>
      </section>

      <section style={styles.grid}>
        <div style={styles.card}>
          <h2 style={styles.cardTitle}>Auth</h2>
          <input
            style={styles.input}
            placeholder="signup username"
            value={signupForm.username}
            onChange={(event) =>
              setSignupForm((prev) => ({ ...prev, username: event.target.value }))
            }
          />
          <input
            style={styles.input}
            placeholder="signup email"
            value={signupForm.email}
            onChange={(event) =>
              setSignupForm((prev) => ({ ...prev, email: event.target.value }))
            }
          />
          <input
            style={styles.input}
            placeholder="signup password"
            type="password"
            value={signupForm.password}
            onChange={(event) =>
              setSignupForm((prev) => ({ ...prev, password: event.target.value }))
            }
          />
          <button style={styles.button} onClick={handleSignup}>
            Signup
          </button>
          <input
            style={styles.input}
            placeholder="login username"
            value={loginForm.username}
            onChange={(event) =>
              setLoginForm((prev) => ({ ...prev, username: event.target.value }))
            }
          />
          <input
            style={styles.input}
            placeholder="login password"
            type="password"
            value={loginForm.password}
            onChange={(event) =>
              setLoginForm((prev) => ({ ...prev, password: event.target.value }))
            }
          />
          <button style={styles.buttonSecondary} onClick={handleLogin}>
            Login
          </button>
          <textarea
            style={styles.textarea}
            value={token}
            onChange={(event) => setToken(event.target.value)}
            placeholder="JWT token"
            rows={5}
          />
        </div>

        <div style={styles.card}>
          <h2 style={styles.cardTitle}>Documents</h2>
          <input
            style={styles.input}
            placeholder="document title"
            value={documentForm.title}
            onChange={(event) =>
              setDocumentForm((prev) => ({ ...prev, title: event.target.value }))
            }
          />
          <textarea
            style={styles.textarea}
            placeholder="document content"
            value={documentForm.content}
            onChange={(event) =>
              setDocumentForm((prev) => ({ ...prev, content: event.target.value }))
            }
            rows={5}
          />
          <div style={styles.row}>
            <button style={styles.button} onClick={handleCreateDocument}>
              Create
            </button>
            <button style={styles.buttonSecondary} onClick={handleListDocuments}>
              List
            </button>
          </div>
          <input
            style={styles.input}
            placeholder="document id"
            value={documentId}
            onChange={(event) => setDocumentId(event.target.value)}
          />
          <div style={styles.row}>
            <button style={styles.buttonSecondary} onClick={handleGetDocument}>
              Get
            </button>
            <button style={styles.buttonSecondary} onClick={handleUpdateDocument}>
              Update
            </button>
            <button style={styles.buttonDanger} onClick={handleDeleteDocument}>
              Delete
            </button>
          </div>
        </div>

        <div style={styles.card}>
          <h2 style={styles.cardTitle}>Search</h2>
          <textarea
            style={styles.textarea}
            placeholder="0.1,0.2,0.3"
            value={searchEmbedding}
            onChange={(event) => setSearchEmbedding(event.target.value)}
            rows={5}
          />
          <button style={styles.button} onClick={handleSearchDocuments}>
            Vector Search
          </button>
          <input
            style={styles.input}
            placeholder="recommend query"
            value={recommendQuery}
            onChange={(event) => setRecommendQuery(event.target.value)}
          />
          <button style={styles.buttonSecondary} onClick={handleRecommend}>
            Recommend
          </button>
          <div style={styles.links}>
            <a href="/api-docs" style={styles.link}>
              API Docs
            </a>
            <a href="/docs" style={styles.link}>
              OpenAPI JSON
            </a>
            <a href="/api/documents/health" style={styles.link}>
              Health
            </a>
          </div>
        </div>
      </section>

      <section style={styles.logCard}>
        <h2 style={styles.cardTitle}>Response</h2>
        <pre style={styles.log}>{log}</pre>
      </section>
    </main>
  );
}

const styles: Record<string, React.CSSProperties> = {
  page: {
    minHeight: "100vh",
    padding: "40px 24px 64px",
    background:
      "radial-gradient(circle at top, #f5d0fe 0%, #f8fafc 30%, #e2e8f0 100%)",
    color: "#111827",
    fontFamily: "Georgia, 'Times New Roman', serif",
  },
  hero: {
    maxWidth: 1100,
    margin: "0 auto 24px",
  },
  kicker: {
    margin: 0,
    letterSpacing: "0.2em",
    textTransform: "uppercase" as const,
    color: "#9a3412",
    fontSize: 12,
  },
  title: {
    margin: "8px 0",
    fontSize: 52,
    lineHeight: 1,
  },
  subtitle: {
    maxWidth: 720,
    fontSize: 18,
    color: "#334155",
  },
  grid: {
    maxWidth: 1100,
    margin: "0 auto",
    display: "grid",
    gridTemplateColumns: "repeat(auto-fit, minmax(280px, 1fr))",
    gap: 16,
  },
  card: {
    background: "rgba(255,255,255,0.8)",
    border: "1px solid rgba(148,163,184,0.35)",
    borderRadius: 24,
    padding: 20,
    backdropFilter: "blur(12px)",
    boxShadow: "0 20px 40px rgba(15,23,42,0.08)",
  },
  logCard: {
    maxWidth: 1100,
    margin: "16px auto 0",
    background: "#0f172a",
    borderRadius: 24,
    padding: 20,
    color: "#e2e8f0",
  },
  cardTitle: {
    margin: "0 0 14px",
    fontSize: 24,
  },
  input: {
    width: "100%",
    marginBottom: 10,
    padding: "12px 14px",
    borderRadius: 14,
    border: "1px solid #cbd5e1",
    boxSizing: "border-box",
  },
  textarea: {
    width: "100%",
    marginBottom: 10,
    padding: "12px 14px",
    borderRadius: 14,
    border: "1px solid #cbd5e1",
    boxSizing: "border-box",
    resize: "vertical" as const,
  },
  row: {
    display: "flex",
    gap: 10,
    flexWrap: "wrap" as const,
    marginBottom: 10,
  },
  button: {
    padding: "12px 16px",
    border: "none",
    borderRadius: 999,
    background: "#0f172a",
    color: "#fff",
    cursor: "pointer",
  },
  buttonSecondary: {
    padding: "12px 16px",
    border: "none",
    borderRadius: 999,
    background: "#ea580c",
    color: "#fff",
    cursor: "pointer",
  },
  buttonDanger: {
    padding: "12px 16px",
    border: "none",
    borderRadius: 999,
    background: "#be123c",
    color: "#fff",
    cursor: "pointer",
  },
  links: {
    display: "flex",
    gap: 12,
    flexWrap: "wrap" as const,
    marginTop: 10,
  },
  link: {
    color: "#7c2d12",
    textDecoration: "none",
    fontWeight: 700,
  },
  log: {
    overflowX: "auto" as const,
    whiteSpace: "pre-wrap" as const,
    wordBreak: "break-word" as const,
    margin: 0,
  },
};
