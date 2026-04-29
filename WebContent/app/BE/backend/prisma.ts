// ============================================================
// Prisma 싱글톤 클라이언트
// .NET의 DbContext를 DI 컨테이너에 AddDbContext()로 등록하는 것과 유사.
// Next.js는 개발 환경에서 Hot Reload 시 커넥션 풀이 고갈되는 문제가 있어
// global 객체에 캐싱하는 패턴이 표준 관행.
// ============================================================

import { PrismaClient } from "@prisma/client";

// TypeScript global 타입 확장 (module augmentation)
const globalForPrisma = globalThis as unknown as {
  prisma: PrismaClient | undefined;
};

export const prisma =
  globalForPrisma.prisma ??
  new PrismaClient({
    log:
      process.env.NODE_ENV === "development"
        ? ["query", "error", "warn"] // 개발환경: SQL 로깅 활성화
        : ["error"],
  });

if (process.env.NODE_ENV !== "production") {
  globalForPrisma.prisma = prisma;
}
