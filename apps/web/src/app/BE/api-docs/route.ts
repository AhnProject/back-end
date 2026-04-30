import { ApiReference } from "@scalar/nextjs-api-reference";

export const GET = ApiReference({
  url: "/BE/docs",
  theme: "kepler",
  darkMode: false,
  defaultOpenAllTags: true,
  pageTitle: "Next.js Migrated API Docs",
});
