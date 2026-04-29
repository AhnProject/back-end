import { prisma } from "@/app/BE/backend/prisma";
import type { CreateUserInput, UserRecord } from "@/app/BE/backend/models/user-model";

export async function findByUsername(username: string): Promise<UserRecord | null> {
  return prisma.user.findUnique({ where: { username } }) as Promise<UserRecord | null>;
}

export async function existsByUsername(username: string): Promise<boolean> {
  const count = await prisma.user.count({ where: { username } });
  return count > 0;
}

export async function existsByEmail(email: string): Promise<boolean> {
  const count = await prisma.user.count({ where: { email } });
  return count > 0;
}

export async function createUser(input: CreateUserInput): Promise<bigint> {
  const user = await prisma.user.create({
    data: {
      username: input.username,
      password: input.password,
      email: input.email,
      role: input.role ?? "USER",
    },
  });

  return user.id;
}


