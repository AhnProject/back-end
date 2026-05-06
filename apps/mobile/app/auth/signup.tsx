import { useState } from "react";
import { View, Text, TextInput, TouchableOpacity, StyleSheet, ActivityIndicator } from "react-native";
import { useRouter } from "expo-router";
import { apiRequest } from "@/lib/api-client";
import { saveToken, saveUsername } from "@/lib/auth-store";
import type { AuthResponse } from "@reel-trip/types";

export default function SignupScreen() {
  const router = useRouter();
  const [form, setForm] = useState({ username: "", email: "", password: "" });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleSignup = async () => {
    setLoading(true);
    setError("");
    try {
      const res = await apiRequest<AuthResponse>("/api/auth/signup", {
        method: "POST",
        body: JSON.stringify(form),
      });
      if (res.success && res.data) {
        await saveToken(res.data.accessToken);
        await saveUsername(res.data.username);
        router.replace("/(tabs)");
      } else {
        setError(res.message);
      }
    } catch {
      setError("회원가입 중 오류가 발생했습니다.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>회원가입</Text>
      <TextInput style={styles.input} placeholder="아이디 (3자 이상)" value={form.username}
        onChangeText={(v) => setForm((p) => ({ ...p, username: v }))} autoCapitalize="none" />
      <TextInput style={styles.input} placeholder="이메일" value={form.email}
        onChangeText={(v) => setForm((p) => ({ ...p, email: v }))} keyboardType="email-address" autoCapitalize="none" />
      <TextInput style={styles.input} placeholder="비밀번호 (8자 이상)" value={form.password}
        onChangeText={(v) => setForm((p) => ({ ...p, password: v }))} secureTextEntry />
      {error ? <Text style={styles.error}>{error}</Text> : null}
      <TouchableOpacity style={styles.button} onPress={handleSignup} disabled={loading}>
        {loading ? <ActivityIndicator color="#fff" /> : <Text style={styles.buttonText}>가입하기</Text>}
      </TouchableOpacity>
      <TouchableOpacity onPress={() => router.back()}>
        <Text style={styles.link}>이미 계정이 있으신가요? 로그인</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, padding: 24, justifyContent: "center", backgroundColor: "#f8fafc" },
  title: { fontSize: 28, fontWeight: "bold", marginBottom: 24, textAlign: "center" },
  input: { borderWidth: 1, borderColor: "#cbd5e1", borderRadius: 12, padding: 14, marginBottom: 12, backgroundColor: "#fff" },
  error: { color: "#be123c", marginBottom: 12, textAlign: "center" },
  button: { backgroundColor: "#0f172a", borderRadius: 999, padding: 14, alignItems: "center", marginBottom: 16 },
  buttonText: { color: "#fff", fontWeight: "bold", fontSize: 16 },
  link: { textAlign: "center", color: "#0f172a", textDecorationLine: "underline" },
});
