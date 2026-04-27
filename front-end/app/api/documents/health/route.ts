import { NextResponse } from "next/server";
import { ok } from "@/lib/utils/api-response";

export async function GET() {
  return NextResponse.json(ok("OK", "Document API is running"));
}
