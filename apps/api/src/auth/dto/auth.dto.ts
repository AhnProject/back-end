import { z } from "zod";

export const SignupSchema = z.object({
  username: z.string().min(3).max(50),
  password: z.string().min(8).max(100),
  email: z.string().email().max(100),
});
export type SignupDto = z.infer<typeof SignupSchema>;

export const LoginSchema = z.object({
  username: z.string().min(1).max(50),
  password: z.string().min(1).max(100),
});
export type LoginDto = z.infer<typeof LoginSchema>;
