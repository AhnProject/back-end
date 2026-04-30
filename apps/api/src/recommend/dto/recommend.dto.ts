import { z } from "zod";

export const RecommendSchema = z.object({
  query: z.string().min(1).max(500),
  topK: z.number().int().min(1).max(20).optional().default(5),
  threshold: z.number().min(0).max(1).optional().default(0.5),
});
export type RecommendDto = z.infer<typeof RecommendSchema>;
