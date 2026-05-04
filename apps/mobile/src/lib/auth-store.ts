import * as SecureStore from "expo-secure-store";

const TOKEN_KEY = "reel_trip_token";
const USERNAME_KEY = "reel_trip_username";

export async function saveToken(token: string): Promise<void> {
  await SecureStore.setItemAsync(TOKEN_KEY, token);
}

export async function getToken(): Promise<string | null> {
  return SecureStore.getItemAsync(TOKEN_KEY);
}

export async function removeToken(): Promise<void> {
  await SecureStore.deleteItemAsync(TOKEN_KEY);
}

export async function saveUsername(username: string): Promise<void> {
  await SecureStore.setItemAsync(USERNAME_KEY, username);
}

export async function getUsername(): Promise<string | null> {
  return SecureStore.getItemAsync(USERNAME_KEY);
}

export async function removeUsername(): Promise<void> {
  await SecureStore.deleteItemAsync(USERNAME_KEY);
}

export async function clearAuth(): Promise<void> {
  await Promise.all([removeToken(), removeUsername()]);
}
