import { useEffect, useState } from "react";
import { View, Text, TouchableOpacity, StyleSheet } from "react-native";
import { useRouter } from "expo-router";
import { getUsername } from "@/lib/auth-store";

export default function HomeScreen() {
  const router = useRouter();
  const [username, setUsername] = useState("");

  useEffect(() => {
    getUsername().then((name) => setUsername(name ?? ""));
  }, []);

  return (
    <View style={styles.container}>
      <Text style={styles.greeting}>안녕하세요, {username}님</Text>
      <Text style={styles.subtitle}>오늘은 어떤 여행을 계획하고 있나요?</Text>

      <TouchableOpacity style={styles.card} onPress={() => router.push("/(tabs)/recommend")}>
        <Text style={styles.cardIcon}>✈️</Text>
        <Text style={styles.cardTitle}>여행지 추천</Text>
        <Text style={styles.cardDesc}>AI가 나에게 맞는 여행지를 추천해드려요</Text>
      </TouchableOpacity>

      <TouchableOpacity style={styles.card} onPress={() => router.push("/(tabs)/profile")}>
        <Text style={styles.cardIcon}>👤</Text>
        <Text style={styles.cardTitle}>프로필</Text>
        <Text style={styles.cardDesc}>계정 정보 및 로그아웃</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, padding: 24, backgroundColor: "#f8fafc" },
  greeting: { fontSize: 26, fontWeight: "bold", marginTop: 16, marginBottom: 6 },
  subtitle: { fontSize: 14, color: "#64748b", marginBottom: 32 },
  card: {
    backgroundColor: "#fff",
    borderRadius: 16,
    padding: 20,
    marginBottom: 16,
    borderWidth: 1,
    borderColor: "#e2e8f0",
  },
  cardIcon: { fontSize: 32, marginBottom: 10 },
  cardTitle: { fontSize: 18, fontWeight: "bold", marginBottom: 4 },
  cardDesc: { fontSize: 13, color: "#64748b" },
});
