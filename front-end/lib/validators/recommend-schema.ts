import { z } from "zod";

export const signupRequestSchema = z.object({
  username: z.string().min(3).max(50),
  password: z.string().min(8).max(100),
  email: z.string().email().max(100),
});

export const loginRequestSchema = z.object({
  username: z.string().min(1).max(50),
  password: z.string().min(1).max(100),
});

export const documentInputSchema = z.object({
  title: z.string().min(1).max(255),
  content: z.string().min(1),
  embedding: z.array(z.number()).optional().nullable(),
});

export const documentSearchSchema = z.object({
  embedding: z.array(z.number()).min(1),
  limit: z.number().int().min(1).max(20).default(10),
  threshold: z.number().min(0).max(1).optional(),
});

export const recommendRequestSchema = z.object({
  query: z.string().min(1).max(500),
  topK: z.number().int().min(1).max(20).default(5),
  threshold: z.number().min(0).max(1).default(0.5),
});

export type SignupRequest = z.infer<typeof signupRequestSchema>;
export type LoginRequest = z.infer<typeof loginRequestSchema>;
export type DocumentInput = z.infer<typeof documentInputSchema>;
export type DocumentSearchRequest = z.infer<typeof documentSearchSchema>;
export type RecommendRequest = z.infer<typeof recommendRequestSchema>;
