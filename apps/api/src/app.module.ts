import { Module } from "@nestjs/common";
import { ConfigModule } from "@nestjs/config";
import { AuthModule } from "./auth/auth.module";
import { DocumentModule } from "./document/document.module";
import { RecommendModule } from "./recommend/recommend.module";
import { PrismaModule } from "./prisma/prisma.module";

@Module({
  imports: [
    ConfigModule.forRoot({ isGlobal: true }),
    PrismaModule,
    AuthModule,
    DocumentModule,
    RecommendModule,
  ],
})
export class AppModule {}
