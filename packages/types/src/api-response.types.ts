export interface ApiResponse<T> {
  success: boolean;
  data: T | null;
  message: string;
  errorCode: string | null;
  timestamp: number;
}
