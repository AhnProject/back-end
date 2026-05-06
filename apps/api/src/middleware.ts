import { NextResponse, type NextRequest } from "next/server";

const ALLOWED_ORIGINS = process.env.ALLOWED_ORIGINS?.split(",").map((o) => o.trim()) ?? [
  "http://localhost:3000",
];

const ALLOWED_SUFFIXES = process.env.ALLOWED_ORIGIN_SUFFIXES?.split(",").map((s) => s.trim()) ?? [];

function isAllowed(origin: string): boolean {
  if (ALLOWED_ORIGINS.includes(origin)) return true;
  if (ALLOWED_SUFFIXES.some((suffix) => origin.endsWith(suffix))) return true;
  return false;
}

export function middleware(request: NextRequest) {
  const origin = request.headers.get("origin") ?? "";
  const allowed = isAllowed(origin);

  if (request.method === "OPTIONS") {
    if (!allowed) return new NextResponse(null, { status: 403 });
    return new NextResponse(null, {
      status: 204,
      headers: {
        "Access-Control-Allow-Origin": origin,
        "Access-Control-Allow-Methods": "GET,POST,PUT,DELETE,OPTIONS",
        "Access-Control-Allow-Headers": "Content-Type, Authorization",
        "Access-Control-Allow-Credentials": "true",
      },
    });
  }

  const response = NextResponse.next();
  if (allowed) {
    response.headers.set("Access-Control-Allow-Origin", origin);
    response.headers.set("Access-Control-Allow-Credentials", "true");
  }
  return response;
}

export const config = {
  matcher: "/api/:path*",
};
