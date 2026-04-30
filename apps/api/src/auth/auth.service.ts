import { Injectable } from "@nestjs/common";
import { JwtService } from "@nestjs/jwt";
import * as bcrypt from "bcryptjs";
import { UserRepository } from "./user.repository";
import { AuthError } from "../common/errors/app.error";
import type { AuthResponse } from "@reel-trip/types";
import type { SignupDto, LoginDto } from "./dto/auth.dto";

@Injectable()
export class AuthService {
  constructor(
    private readonly userRepository: UserRepository,
    private readonly jwtService: JwtService
  ) {}

  async signup(dto: SignupDto): Promise<AuthResponse> {
    if (await this.userRepository.existsByUsername(dto.username)) {
      throw AuthError.usernameAlreadyExists(dto.username);
    }
    if (await this.userRepository.existsByEmail(dto.email)) {
      throw AuthError.emailAlreadyExists(dto.email);
    }

    const hashedPassword = await bcrypt.hash(dto.password, 10);
    const role = "USER";

    await this.userRepository.create({
      username: dto.username,
      password: hashedPassword,
      email: dto.email,
      role,
    });

    return {
      accessToken: await this.jwtService.signAsync({ sub: dto.username, role }),
      tokenType: "Bearer",
      username: dto.username,
      email: dto.email,
      role,
    };
  }

  async login(dto: LoginDto): Promise<AuthResponse> {
    const user = await this.userRepository.findByUsername(dto.username);
    if (!user) throw AuthError.invalidCredentials();

    const match = await bcrypt.compare(dto.password, user.password);
    if (!match) throw AuthError.invalidCredentials();

    return {
      accessToken: await this.jwtService.signAsync({ sub: user.username, role: user.role }),
      tokenType: "Bearer",
      username: user.username,
      email: user.email,
      role: user.role,
    };
  }
}
