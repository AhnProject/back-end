import { ok } from "@/common/filters/response";

export async function GET() {
  return ok({ status: "ok" }, "Documents API is healthy");
}
