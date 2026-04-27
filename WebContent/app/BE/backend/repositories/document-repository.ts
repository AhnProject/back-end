import { prisma } from "@/app/BE/backend/prisma";
import type {
  CreateDocumentInput,
  DocumentRecord,
  SimilarDocumentRecord,
  UpdateDocumentInput,
} from "@/app/BE/backend/models/document-model";
import {
  VECTOR_DIM,
  adjustVectorDimension,
  vectorToString,
} from "@/app/BE/backend/utils/vector-utils";

type DocumentRow = {
  id: bigint;
  title: string;
  content: string;
  embedding: string | null;
  created_at: Date;
  updated_at: Date | null;
};

type SearchDocumentRow = DocumentRow & {
  similarity: string;
};

export async function createDocument(input: CreateDocumentInput): Promise<bigint> {
  const adjusted = adjustVectorDimension(input.embedding ?? null, VECTOR_DIM);
  const vector = vectorToString(adjusted);

  const result = (await prisma.$queryRawUnsafe(
    `
      INSERT INTO documents (title, content, embedding, created_at)
      VALUES ($1, $2, $3::vector, now())
      RETURNING id
    `,
    input.title,
    input.content,
    vector
  )) as Array<{ id: bigint }>;

  return result[0]!.id;
}

export async function findDocumentById(id: bigint): Promise<DocumentRecord | null> {
  const rows = (await prisma.$queryRawUnsafe(
    `
      SELECT id, title, content, embedding::text, created_at, updated_at
      FROM documents
      WHERE id = $1
    `,
    id
  )) as DocumentRow[];

  return rows[0] ? mapRow(rows[0]) : null;
}

export async function findAllDocuments(): Promise<DocumentRecord[]> {
  const rows = (await prisma.$queryRawUnsafe(
    `
      SELECT id, title, content, embedding::text, created_at, updated_at
      FROM documents
      ORDER BY created_at DESC
    `
  )) as DocumentRow[];

  return rows.map(mapRow);
}

export async function updateDocument(id: bigint, input: UpdateDocumentInput): Promise<number> {
  const adjusted = adjustVectorDimension(input.embedding ?? null, VECTOR_DIM);
  const vector = vectorToString(adjusted);

  return prisma.$executeRawUnsafe(
    `
      UPDATE documents
      SET title = $1,
          content = $2,
          embedding = $3::vector,
          updated_at = now()
      WHERE id = $4
    `,
    input.title,
    input.content,
    vector,
    id
  );
}

export async function deleteDocumentById(id: bigint): Promise<number> {
  return prisma.$executeRawUnsafe(`DELETE FROM documents WHERE id = $1`, id);
}

export async function searchDocumentsByVector(
  embedding: number[],
  limit: number,
  threshold?: number
): Promise<SimilarDocumentRecord[]> {
  const adjusted = adjustVectorDimension(embedding, VECTOR_DIM);
  const vector = vectorToString(adjusted);

  const query =
    threshold !== undefined
      ? `
      SELECT
        id,
        title,
        content,
        embedding::text,
        created_at,
        updated_at,
        ROUND(CAST(1 - (embedding <=> $1::vector) AS NUMERIC), 4) AS similarity
      FROM documents
      WHERE embedding IS NOT NULL
        AND (1 - (embedding <=> $1::vector)) >= $2
      ORDER BY embedding <=> $1::vector
      LIMIT $3
    `
      : `
      SELECT
        id,
        title,
        content,
        embedding::text,
        created_at,
        updated_at,
        ROUND(CAST(1 - (embedding <=> $1::vector) AS NUMERIC), 4) AS similarity
      FROM documents
      WHERE embedding IS NOT NULL
      ORDER BY embedding <=> $1::vector
      LIMIT $2
    `;

  const rows =
    threshold !== undefined
      ? ((await prisma.$queryRawUnsafe(query, vector, threshold, limit)) as SearchDocumentRow[])
      : ((await prisma.$queryRawUnsafe(query, vector, limit)) as SearchDocumentRow[]);

  return rows.map((row) => ({
    ...mapRow(row),
    similarity: Number.parseFloat(row.similarity),
  }));
}

function mapRow(row: DocumentRow): DocumentRecord {
  const cleaned = row.embedding?.replace("[", "").replace("]", "").trim() ?? "";
  const embedding =
    cleaned.length > 0 ? cleaned.split(",").map((value) => Number(value)) : null;

  return {
    id: row.id,
    title: row.title,
    content: row.content,
    embedding,
    createdAt: row.created_at,
    updatedAt: row.updated_at,
  };
}


