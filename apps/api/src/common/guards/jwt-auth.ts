import { jwtVerify } from "jose";
import { AuthError } from "../errors/app.error";

export async function verifyJwt(request: Request): Promise<{ sub: string; role: string }> {
  const authHeader = request.headers.get("authorization");
  if (!authHeader?.startsWith("Bearer ")) throw AuthError.unauthorized();

  const token = authHeader.slice(7);
  const secret = new TextEncoder().encode(process.env.JWT_SECRET ?? "");

  try {
    const { payload } = await jwtVerify(token, secret);
    return payload as { sub: string; role: string };
  } catch {
    throw AuthError.invalidToken();
  }
}
