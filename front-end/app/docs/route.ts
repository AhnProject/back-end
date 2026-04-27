// ============================================================
// GET /docs - OpenAPI JSON 스펙 엔드포인트
//
// Scalar UI가 이 URL에서 스펙을 가져와 렌더링함.
// [.NET 대응] app.UseSwagger() → /swagger/v1/swagger.json 에 대응
// ============================================================

import { NextResponse } from "next/server";
import { openApiSpec } from "@/lib/openapi-spec";

export async function GET() {
  return NextResponse.json(openApiSpec, {
    headers: {
      // CORS 허용 (Scalar UI가 같은 오리진에서 fetch)
      "Access-Control-Allow-Origin": "*",
      "Content-Type": "application/json",
    },
  });
}
