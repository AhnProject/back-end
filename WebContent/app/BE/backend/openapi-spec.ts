export const openApiSpec = {
  openapi: "3.1.0",
  info: {
    title: "Next.js Migrated API",
    version: "1.0.0",
    description:
      "Spring Boot endpoints migrated to Next.js Route Handlers for Vercel deployment.",
  },
  servers: [
    {
      url: process.env.NEXT_PUBLIC_APP_URL ?? "http://localhost:3000",
    },
  ],
  components: {
    securitySchemes: {
      bearerAuth: {
        type: "http",
        scheme: "bearer",
        bearerFormat: "JWT",
      },
    },
  },
  paths: {
    "/api/auth/signup": {
      post: {
        tags: ["auth"],
        summary: "Create user and issue JWT",
      },
    },
    "/api/auth/login": {
      post: {
        tags: ["auth"],
        summary: "Login and issue JWT",
      },
    },
    "/api/documents": {
      get: {
        tags: ["documents"],
        summary: "List documents",
        security: [{ bearerAuth: [] }],
      },
      post: {
        tags: ["documents"],
        summary: "Create document",
        security: [{ bearerAuth: [] }],
      },
    },
    "/api/documents/{id}": {
      get: {
        tags: ["documents"],
        summary: "Get document by id",
        security: [{ bearerAuth: [] }],
      },
      put: {
        tags: ["documents"],
        summary: "Update document by id",
        security: [{ bearerAuth: [] }],
      },
      delete: {
        tags: ["documents"],
        summary: "Delete document by id",
        security: [{ bearerAuth: [] }],
      },
    },
    "/api/documents/search": {
      post: {
        tags: ["documents"],
        summary: "Vector search by embedding",
        security: [{ bearerAuth: [] }],
      },
    },
    "/api/documents/health": {
      get: {
        tags: ["documents"],
        summary: "Health check",
      },
    },
    "/api/recommend": {
      get: {
        tags: ["recommend"],
        summary: "Health check",
      },
      post: {
        tags: ["recommend"],
        summary: "Natural-language recommendation",
      },
    },
  },
} as const;
