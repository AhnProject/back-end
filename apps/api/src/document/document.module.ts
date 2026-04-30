import { Module } from "@nestjs/common";
import { JwtModule } from "@nestjs/jwt";
import { ConfigModule, ConfigService } from "@nestjs/config";
import { DocumentController } from "./document.controller";
import { DocumentService } from "./document.service";
import { DocumentRepository } from "./document.repository";
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
  controllers: [DocumentController],
  providers: [DocumentService, DocumentRepository, AiService],
})
export class DocumentModule {}
