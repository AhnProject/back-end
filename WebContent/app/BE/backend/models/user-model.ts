export interface UserRecord {
  id: bigint;
  username: string;
  password: string;
  email: string;
  role: string;
  createdAt: Date;
  updatedAt: Date | null;
}

export interface CreateUserInput {
  username: string;
  password: string;
  email: string;
  role?: string;
}
