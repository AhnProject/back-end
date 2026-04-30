import { Module } from "@nestjs/common";
import { JwtModule } from "@nestjs/jwt";
import { ConfigModule, ConfigService } from "@nestjs/config";
import { AuthController } from "./auth.controller";
import { AuthService } from "./auth.service";
import { UserRepository } from "./user.repository";

@Module({
  imports: [
    JwtModule.registerAsync({
      imports: [ConfigModule],
      inject: [ConfigService],
      useFactory: (config: ConfigService) => ({
        secret: config.getOrThrow("JWT_SECRET"),
        signOptions: {
          expiresIn: `${Math.floor(Number(config.get("JWT_EXPIRATION", "86400000")) / 1000)}s`,
        },
      }),
    }),
  ],
  controllers: [AuthController],
  providers: [AuthService, UserRepository],
  exports: [JwtModule],
})
export class AuthModule {}
