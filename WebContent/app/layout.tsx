import type { Metadata } from "next";

export const metadata: Metadata = {
  title: "Next.js API Migration",
  description: "Spring Boot APIs migrated to Next.js for Vercel deployment.",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko">
      <body style={{ margin: 0, padding: 0 }}>{children}</body>
    </html>
  );
}
