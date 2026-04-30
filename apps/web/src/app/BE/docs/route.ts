import { NextResponse } from "next/server";
import { openApiSpec } from "@/app/BE/backend/openapi-spec";

export async function GET(): Promise<NextResponse> {
  return NextResponse.json(openApiSpec, {
    headers: {
      "Access-Control-Allow-Origin": "*",
      "Content-Type": "application/json",
    },
  });
}
