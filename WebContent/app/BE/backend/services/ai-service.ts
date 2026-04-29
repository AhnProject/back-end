import OpenAI from "openai";
import type { ParsedQuery } from "@/app/BE/backend/models/recommend-model";
import { AppError } from "@/app/BE/backend/utils/app-error";
import { VECTOR_DIM } from "@/app/BE/backend/utils/vector-utils";

function getOpenAIClient(): OpenAI {
  const apiKey = process.env.OPENAI_API_KEY;

  if (!apiKey) {
    throw new AppError("OPENAI_API_KEY is required", "OPENAI_API_KEY_MISSING", 500);
  }

  return new OpenAI({ apiKey });
}

export async function parseAndEmbed(userInput: string): Promise<ParsedQuery> {
  const openai = getOpenAIClient();
  const chatResponse = await openai.chat.completions.create({
    model: "gpt-4o",
    messages: [
      {
        role: "system",
        content:
          "Extract keywords and return a refined search query as JSON with keys keywords and refinedQuery.",
      },
      {
        role: "user",
        content: userInput,
      },
    ],
    temperature: 0.2,
    response_format: { type: "json_object" },
  });

  const rawJson = chatResponse.choices[0]?.message?.content ?? "{}";
  let parsed: {
    keywords?: string[];
    refinedQuery?: string;
  };

  try {
    parsed = JSON.parse(rawJson) as {
      keywords?: string[];
      refinedQuery?: string;
    };
  } catch {
    throw new AppError("Failed to parse OpenAI response JSON", "AI_RESPONSE_PARSE_ERROR", 502);
  }

  const refinedQuery = parsed.refinedQuery?.trim() || userInput;
  const embedding = await createEmbedding(refinedQuery);

  return {
    originalText: userInput,
    keywords: parsed.keywords ?? [],
    refinedQuery,
    embedding,
  };
}

export async function createEmbedding(text: string): Promise<number[]> {
  const openai = getOpenAIClient();
  const response = await openai.embeddings.create({
    model: "text-embedding-3-small",
    input: text,
    dimensions: VECTOR_DIM,
  });

  return response.data[0]?.embedding ?? [];
}

