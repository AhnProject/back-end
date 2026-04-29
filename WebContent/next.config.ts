import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  // Vercel 배포 최적화 설정
  // .NET의 appsettings.json 환경별 설정과 유사하게, 환경변수로 분기 가능
  experimental: {
    // Server Actions 활성화 (Controller의 Action 메서드와 유사)
    serverActions: {
      allowedOrigins: ["localhost:3000"],
    },
  },
  async rewrites() {
    return [
      { source: "/api/:path*", destination: "/BE/api/:path*" },
      { source: "/docs", destination: "/BE/docs" },
      { source: "/api-docs", destination: "/BE/api-docs" },
      { source: "/test-ui", destination: "/FE/test-ui" },
    ];
  },
};

export default nextConfig;
