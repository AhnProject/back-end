import {
  getRecommendHealth,
  postRecommend,
} from "@/app/BE/backend/controllers/recommend-controller";

export const POST = postRecommend;
export const GET = getRecommendHealth;


