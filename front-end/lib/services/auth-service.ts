import bcrypt from "bcryptjs";
import { generateToken } from "@/lib/utils/jwt";
import * as userRepo from "@/lib/repositories/user-repo";
import { AuthError } from "@/lib/utils/app-error";

export interface AuthResponse {
  accessToken: string;
  tokenType: "Bearer";
  username: string;
  email: string;
  role: string;
}

export async function signup(data: {
  username: string;
  password: string;
  email: string;
}): Promise<AuthResponse> {
  if (await userRepo.existsByUsername(data.username)) {
    throw AuthError.usernameAlreadyExists(data.username);
  }

  if (await userRepo.existsByEmail(data.email)) {
    throw AuthError.emailAlreadyExists(data.email);
  }

  const hashedPassword = await bcrypt.hash(data.password, 10);
  const role = "USER";

  await userRepo.saveUser({
    username: data.username,
    password: hashedPassword,
    email: data.email,
    role,
  });

  return {
    accessToken: await generateToken(data.username, role),
    tokenType: "Bearer",
    username: data.username,
    email: data.email,
    role,
  };
}

export async function login(data: {
  username: string;
  password: string;
}): Promise<AuthResponse> {
  const user = await userRepo.findByUsername(data.username);
  if (!user) {
    throw AuthError.invalidCredentials();
  }

  const passwordMatch = await bcrypt.compare(data.password, user.password);
  if (!passwordMatch) {
    throw AuthError.invalidCredentials();
  }

  return {
    accessToken: await generateToken(user.username, user.role),
    tokenType: "Bearer",
    username: user.username,
    email: user.email,
    role: user.role,
  };
}
