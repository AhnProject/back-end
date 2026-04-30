import { useState } from "react";
import { View, Text, TextInput, TouchableOpacity, FlatList, StyleSheet, ActivityIndicator } from "react-native";
import { apiRequest } from "@/lib/api-client";
import { getToken } from "@/lib/auth-store";
import type { RecommendOutput } from "@reel-trip/types";

export default function RecommendScreen() {
  const [query, setQuery] = useState("");
  const [results, setResults] = useState<RecommendOutput | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleRecommend = async () => {
    if (!query.trim()) return;
    setLoading(true);
    setError("");
    try {
      const token = await getToken();
      const res = await apiRequest<RecommendOutput>(
        "/api/recommend",
        { method: "POST", body: JSON.stringify({ query, topK: 5, threshold: 0.5 }) },
        token ?? undefined
      );
      if (res.success && res.data) {
        setResults(res.data);
      } else {
        setError(res.message);
      }
    } catch {
      setError("요청 중 오류가 발생했습니다.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>여행지 추천</Text>
      <TextInput
        style={styles.input}
        placeholder="어떤 여행을 원하시나요?"
        value={query}
        onChangeText={setQuery}
        multiline
      />
      <TouchableOpacity style={styles.button} onPress={handleRecommend} disabled={loading}>
        {loading ? <ActivityIndicator color="#fff" /> : <Text style={styles.buttonText}>추천 받기</Text>}
      </TouchableOpacity>
      {error ? <Text style={styles.error}>{error}</Text> : null}
      {results && (
        <FlatList
          data={results.results}
          keyExtractor={(item) => item.id}
          renderItem={({ item }) => (
            <View style={styles.card}>
              <Text style={styles.cardTitle}>{item.title}</Text>
              <Text style={styles.cardContent} numberOfLines={3}>{item.content}</Text>
              <Text style={styles.similarity}>유사도: {(item.similarity * 100).toFixed(1)}%</Text>
            </View>
          )}
        />
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, padding: 20, backgroundColor: "#f8fafc" },
  title: { fontSize: 24, fontWeight: "bold", marginBottom: 16 },
  input: { borderWidth: 1, borderColor: "#cbd5e1", borderRadius: 12, padding: 14, marginBottom: 12, minHeight: 80, textAlignVertical: "top", backgroundColor: "#fff" },
  button: { backgroundColor: "#0f172a", borderRadius: 999, padding: 14, alignItems: "center", marginBottom: 12 },
  buttonText: { color: "#fff", fontWeight: "bold", fontSize: 16 },
  error: { color: "#be123c", marginBottom: 12 },
  card: { backgroundColor: "#fff", borderRadius: 16, padding: 16, marginBottom: 12, borderWidth: 1, borderColor: "#e2e8f0" },
  cardTitle: { fontSize: 16, fontWeight: "bold", marginBottom: 6 },
  cardContent: { fontSize: 14, color: "#475569", marginBottom: 8 },
  similarity: { fontSize: 12, color: "#94a3b8" },
});
