"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { apiRequest } from "@/lib/api-client";
import type { AuthResponse } from "@reel-trip/types";

export default function LoginPage() {
  const router = useRouter();
  const [form, setForm] = useState({ username: "", password: "" });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      const result = await apiRequest<AuthResponse>("/api/auth/login", {
        method: "POST",
        body: JSON.stringify(form),
      });
      if (result.success && result.data) {
        localStorage.setItem("token", result.data.accessToken);
        localStorage.setItem("username", result.data.username);
        router.push("/dashboard");
      } else {
        setError(result.message ?? "로그인 실패");
      }
    } catch {
      setError("서버 연결에 실패했습니다.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <main style={s.page}>
      <div style={s.card}>
        <p style={s.kicker}>ReeL-Trip</p>
        <h1 style={s.title}>로그인</h1>
        <form onSubmit={handleSubmit} style={s.form}>
          <input
            style={s.input}
            placeholder="아이디"
            value={form.username}
            onChange={(e) => setForm((p) => ({ ...p, username: e.target.value }))}
            required
          />
          <input
            style={s.input}
            type="password"
            placeholder="비밀번호"
            value={form.password}
            onChange={(e) => setForm((p) => ({ ...p, password: e.target.value }))}
            required
          />
          {error && <p style={s.error}>{error}</p>}
          <button style={s.button} type="submit" disabled={loading}>
            {loading ? "로그인 중..." : "로그인"}
          </button>
        </form>
        <p style={s.footer}>
          계정이 없으신가요?{" "}
          <a href="/auth/signup" style={s.link}>회원가입</a>
        </p>
      </div>
    </main>
  );
}

const s: Record<string, React.CSSProperties> = {
  page: { minHeight: "100vh", display: "flex", alignItems: "center", justifyContent: "center", background: "radial-gradient(circle at top, #f5d0fe 0%, #f8fafc 40%, #e2e8f0 100%)", fontFamily: "Georgia, serif" },
  card: { background: "rgba(255,255,255,0.85)", backdropFilter: "blur(16px)", border: "1px solid rgba(148,163,184,0.3)", borderRadius: 28, padding: "48px 40px", width: "100%", maxWidth: 400 },
  kicker: { margin: "0 0 8px", letterSpacing: "0.2em", textTransform: "uppercase" as const, color: "#9a3412", fontSize: 12 },
  title: { margin: "0 0 32px", fontSize: 36 },
  form: { display: "flex", flexDirection: "column" as const, gap: 12 },
  input: { padding: "14px 16px", borderRadius: 14, border: "1px solid #cbd5e1", fontSize: 15, outline: "none" },
  button: { padding: "14px", borderRadius: 999, border: "none", background: "#0f172a", color: "#fff", fontSize: 15, cursor: "pointer", marginTop: 4 },
  error: { margin: 0, color: "#be123c", fontSize: 13 },
  footer: { marginTop: 24, textAlign: "center" as const, fontSize: 14, color: "#64748b" },
  link: { color: "#ea580c", textDecoration: "none", fontWeight: 600 },
};
