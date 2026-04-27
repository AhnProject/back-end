import { SignJWT, jwtVerify, type JWTPayload } from "jose";
import { AppError, AuthError } from "@/app/BE/backend/utils/app-error";

function getJwtSecret(): string {
  const secret = process.env.JWT_SECRET;
  if (!secret || secret.trim().length < 32) {
    throw new AppError(
      "JWT_SECRET must be set and at least 32 characters long",
      "JWT_SECRET_INVALID",
      500
    );
  }
  return secret;
}

function getJwtExpirationSeconds(): number {
  const raw = process.env.JWT_EXPIRATION ?? "86400000";
  const milliseconds = Number.parseInt(raw, 10);
  if (!Number.isFinite(milliseconds) || milliseconds <= 0) {
    throw new AppError(
      "JWT_EXPIRATION must be a positive integer in milliseconds",
      "JWT_EXPIRATION_INVALID",
      500
    );
  }
  return Math.floor(milliseconds / 1000);
}

function getSigningKey(): Uint8Array {
  return new TextEncoder().encode(getJwtSecret());
}

export async function generateToken(
  username: string,
  role: string = "USER"
): Promise<string> {
  return new SignJWT({ role })
    .setProtectedHeader({ alg: "HS256" })
    .setSubject(username)
    .setIssuedAt()
    .setExpirationTime(`${getJwtExpirationSeconds()}s`)
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
