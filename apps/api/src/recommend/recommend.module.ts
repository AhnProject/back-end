import { Module } from "@nestjs/common";
import { JwtModule } from "@nestjs/jwt";
import { ConfigModule, ConfigService } from "@nestjs/config";
import { RecommendController } from "./recommend.controller";
import { RecommendService } from "./recommend.service";
import { DocumentRepository } from "../document/document.repository";
import { AiService } from "../ai/ai.service";

@Module({
  imports: [
    JwtModule.registerAsync({
      imports: [ConfigModule],
      inject: [ConfigService],
      useFactory: (config: ConfigService) => ({
        secret: config.getOrThrow("JWT_SECRET"),
      }),
    }),
  ],
  controllers: [RecommendController],
  providers: [RecommendService, DocumentRepository, AiService],
})
export class RecommendModule {}
