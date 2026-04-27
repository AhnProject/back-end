// ============================================================
// Auth Guard — Spring Security JwtAuthenticationFilter 이식
// Route Handler에서 직접 호출하는 함수형 가드
// .NET의 [Authorize] 어트리뷰트 / Spring의 @SecurityRequirement 대응
// ============================================================

import { NextRequest } from "next/server";
import { extractBearerToken, verifyToken } from "@/lib/utils/jwt";
import { AuthError } from "@/lib/utils/app-error";

export interface AuthContext {
  username: string;
  role?: string;
}

/**
 * 요청에서 JWT를 검증하고 인증 컨텍스트를 반환
 * Java: JwtAuthenticationFilter.doFilterInternal() 대응
 *
 * @throws AppError (401/403) 토큰이 없거나 유효하지 않을 때
 */
export async function requireAuth(request: NextRequest): Promise<AuthContext> {
  const authHeader = request.headers.get("authorization");
  const token = extractBearerToken(authHeader);

  if (!token) throw AuthError.unauthorized();

  const payload = await verifyToken(token);
  const username = payload.sub;

  if (!username) throw AuthError.invalidToken();

  return {
    username,
    role: (payload["role"] as string) ?? "USER",
  };
}
