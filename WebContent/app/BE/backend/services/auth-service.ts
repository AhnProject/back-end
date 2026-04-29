import bcrypt from "bcryptjs";
import { generateToken } from "@/app/BE/backend/utils/jwt";
import { AuthError } from "@/app/BE/backend/utils/app-error";
import type {
  AuthResponse,
  LoginInput,
  SignupInput,
} from "@/app/BE/backend/models/auth-model";
import {
  createUser,
  existsByEmail,
  existsByUsername,
  findByUsername,
} from "@/app/BE/backend/repositories/user-repository";

export async function signup(input: SignupInput): Promise<AuthResponse> {
  if (await existsByUsername(input.username)) {
    throw AuthError.usernameAlreadyExists(input.username);
  }

  if (await existsByEmail(input.email)) {
    throw AuthError.emailAlreadyExists(input.email);
  }

  const hashedPassword = await bcrypt.hash(input.password, 10);
  const role = "USER";

  await createUser({
    username: input.username,
    password: hashedPassword,
    email: input.email,
    role,
  });

  return {
    accessToken: await generateToken(input.username, role),
    tokenType: "Bearer",
    username: input.username,
    email: input.email,
    role,
  };
}

export async function login(input: LoginInput): Promise<AuthResponse> {
  const user = await findByUsername(input.username);
  if (!user) {
    throw AuthError.invalidCredentials();
  }

  const passwordMatch = await bcrypt.compare(input.password, user.password);
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


