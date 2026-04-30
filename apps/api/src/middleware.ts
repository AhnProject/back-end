import { NextResponse, type NextRequest } from "next/server";

const ALLOWED_ORIGINS = process.env.ALLOWED_ORIGINS?.split(",") ?? [
  "http://localhost:3000",
];

export function middleware(request: NextRequest) {
  const origin = request.headers.get("origin") ?? "";
  const isAllowed = ALLOWED_ORIGINS.includes(origin);

  if (request.method === "OPTIONS") {
    return new NextResponse(null, {
      status: 204,
      headers: {
        "Access-Control-Allow-Origin": isAllowed ? origin : "",
        "Access-Control-Allow-Methods": "GET,POST,PUT,DELETE,OPTIONS",
        "Access-Control-Allow-Headers": "Content-Type, Authorization",
        "Access-Control-Allow-Credentials": "true",
      },
    });
  }

  const response = NextResponse.next();

  if (isAllowed) {
    response.headers.set("Access-Control-Allow-Origin", origin);
    response.headers.set("Access-Control-Allow-Credentials", "true");
  }

  return response;
}

export const config = {
  matcher: "/api/:path*",
};
