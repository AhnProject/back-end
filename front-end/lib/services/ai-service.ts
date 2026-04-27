import OpenAI from "openai";

function getOpenAIClient(): OpenAI {
  const apiKey = process.env.OPENAI_API_KEY;

  if (!apiKey) {
    throw new Error("OPENAI_API_KEY is required.");
  }

  return new OpenAI({ apiKey });
}

export interface ParsedQuery {
  originalText: string;
  keywords: string[];
  refinedQuery: string;
  embedding: number[];
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
  const parsed = JSON.parse(rawJson) as {
    keywords?: string[];
    refinedQuery?: string;
  };

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
    dimensions: 1536,
  });

  return response.data[0]?.embedding ?? [];
}
