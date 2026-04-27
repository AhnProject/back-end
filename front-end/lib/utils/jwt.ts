import { SignJWT, jwtVerify, type JWTPayload } from "jose";
import { AuthError } from "@/lib/utils/app-error";

const JWT_SECRET =
  process.env.JWT_SECRET ?? "change-this-secret-key-for-local-development-1234567890";
const JWT_EXPIRATION_SECONDS = Math.floor(
  Number.parseInt(process.env.JWT_EXPIRATION ?? "86400000", 10) / 1000
);

function getSigningKey(): Uint8Array {
  return new TextEncoder().encode(JWT_SECRET);
}

export async function generateToken(
  username: string,
  role: string = "USER"
): Promise<string> {
  return new SignJWT({ role })
    .setProtectedHeader({ alg: "HS256" })
    .setSubject(username)
    .setIssuedAt()
    .setExpirationTime(`${JWT_EXPIRATION_SECONDS}s`)
    .sign(getSigningKey());
}

export async function verifyToken(token: string): Promise<JWTPayload> {
  try {
    const { payload } = await jwtVerify(token, getSigningKey());
    return payload;
  } catch (error) {
    const message = error instanceof Error ? error.message.toLowerCase() : "";
    if (message.includes("exp")) {
      throw AuthError.tokenExpired();
    }
    throw AuthError.invalidToken();
  }
}

export function extractBearerToken(authHeader: string | null): string | null {
  if (!authHeader?.startsWith("Bearer ")) {
    return null;
  }

  return authHeader.slice(7);
}
