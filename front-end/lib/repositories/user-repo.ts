// ============================================================
// User Repository — UserRepositoryImpl.java 완전 이식
// Prisma ORM 사용 (Spring의 JdbcTemplate 대응)
// ============================================================

import { prisma } from "@/lib/prisma";

export interface UserRecord {
  id: bigint;
  username: string;
  password: string;
  email: string;
  role: string;
  createdAt: Date;
  updatedAt: Date | null;
}

/** username으로 유저 조회 */
export async function findByUsername(username: string): Promise<UserRecord | null> {
  return prisma.user.findUnique({ where: { username } }) as Promise<UserRecord | null>;
}

/** email로 유저 조회 */
export async function findByEmail(email: string): Promise<UserRecord | null> {
  return prisma.user.findUnique({ where: { email } }) as Promise<UserRecord | null>;
}

/** username 중복 확인 */
export async function existsByUsername(username: string): Promise<boolean> {
  const count = await prisma.user.count({ where: { username } });
  return count > 0;
}

/** email 중복 확인 */
export async function existsByEmail(email: string): Promise<boolean> {
  const count = await prisma.user.count({ where: { email } });
  return count > 0;
}

/** 유저 저장 → ID 반환 */
export async function saveUser(data: {
  username: string;
  password: string;
  email: string;
  role?: string;
}): Promise<bigint> {
  const user = await prisma.user.create({
    data: {
      username: data.username,
      password: data.password,
      email: data.email,
      role: data.role ?? "USER",
    },
  });
  return user.id;
}
