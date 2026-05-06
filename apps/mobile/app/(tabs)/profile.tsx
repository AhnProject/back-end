import { View, Text, TouchableOpacity, StyleSheet } from "react-native";
import { useRouter } from "expo-router";
import { clearAuth, getUsername } from "@/lib/auth-store";
import { useEffect, useState } from "react";

export default function ProfileScreen() {
  const router = useRouter();
  const [username, setUsername] = useState("");

  useEffect(() => {
    getUsername().then((name) => setUsername(name ?? ""));
  }, []);

  const handleLogout = async () => {
    await clearAuth();
    router.replace("/auth/login");
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>프로필</Text>
      <View style={styles.infoBox}>
        <Text style={styles.label}>아이디</Text>
        <Text style={styles.value}>{username}</Text>
      </View>
      <TouchableOpacity style={styles.button} onPress={handleLogout}>
        <Text style={styles.buttonText}>로그아웃</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, padding: 24, backgroundColor: "#f8fafc" },
  title: { fontSize: 24, fontWeight: "bold", marginBottom: 24 },
  infoBox: { backgroundColor: "#fff", borderRadius: 12, padding: 16, marginBottom: 32, borderWidth: 1, borderColor: "#e2e8f0" },
  label: { fontSize: 12, color: "#94a3b8", marginBottom: 4 },
  value: { fontSize: 16, fontWeight: "600" },
  button: { backgroundColor: "#be123c", borderRadius: 999, padding: 14, alignItems: "center" },
  buttonText: { color: "#fff", fontWeight: "bold", fontSize: 16 },
});
