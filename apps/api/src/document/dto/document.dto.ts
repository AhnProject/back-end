import { z } from "zod";

export const CreateDocumentSchema = z.object({
  title: z.string().min(1).max(255),
  content: z.string().min(1),
  embedding: z.array(z.number()).optional().nullable(),
});
export type CreateDocumentDto = z.infer<typeof CreateDocumentSchema>;

export const UpdateDocumentSchema = CreateDocumentSchema;
export type UpdateDocumentDto = z.infer<typeof UpdateDocumentSchema>;

export const SearchDocumentSchema = z.object({
  embedding: z.array(z.number()),
  limit: z.number().int().min(1).max(20).optional().default(10),
  threshold: z.number().min(0).max(1).optional(),
});
export type SearchDocumentDto = z.infer<typeof SearchDocumentSchema>;
