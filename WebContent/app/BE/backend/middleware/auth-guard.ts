// ============================================================
// Auth Guard ??Spring Security JwtAuthenticationFilter ?댁떇
// Route Handler?먯꽌 吏곸젒 ?몄텧?섎뒗 ?⑥닔??媛??
// .NET??[Authorize] ?댄듃由щ럭??/ Spring??@SecurityRequirement ???
// ============================================================

import { NextRequest } from "next/server";
import { extractBearerToken, verifyToken } from "@/app/BE/backend/utils/jwt";
import { AuthError } from "@/app/BE/backend/utils/app-error";

export interface AuthContext {
  username: string;
  role?: string;
}

/**
 * ?붿껌?먯꽌 JWT瑜?寃利앺븯怨??몄쬆 而⑦뀓?ㅽ듃瑜?諛섑솚
 * Java: JwtAuthenticationFilter.doFilterInternal() ???
 *
 * @throws AppError (401/403) ?좏겙???녾굅???좏슚?섏? ?딆쓣 ??
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



