// ============================================================
// ApiResponse — Spring Boot ApiResponse<T> 완전 이식
// 모든 API Route에서 일관된 응답 형식을 보장
// ============================================================

export interface ApiResponse<T> {
  success: boolean;
  data: T | null;
  message: string;
  errorCode: string | null;
  timestamp: number;
}

export function ok<T>(data: T, message = "Success"): ApiResponse<T> {
  return { success: true, data, message, errorCode: null, timestamp: Date.now() };
}

export function error(errorCode: string, message: string): ApiResponse<null> {
  return { success: false, data: null, message, errorCode, timestamp: Date.now() };
}
