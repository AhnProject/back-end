import { ApiReference } from "@scalar/nextjs-api-reference";

export const GET = ApiReference({
  spec: {
    url: "/docs",
  },
  theme: "kepler",
  darkMode: false,
  defaultOpenAllTags: true,
  metaData: {
    title: "Next.js Migrated API Docs",
  },
});
