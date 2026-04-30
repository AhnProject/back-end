import { IsEmail, IsString, MaxLength, MinLength } from "class-validator";
import { ApiProperty } from "@nestjs/swagger";

export class SignupDto {
  @ApiProperty()
  @IsString()
  @MinLength(3)
  @MaxLength(50)
  username!: string;

  @ApiProperty()
  @IsString()
  @MinLength(8)
  @MaxLength(100)
  password!: string;

  @ApiProperty()
  @IsEmail()
  @MaxLength(100)
  email!: string;
}

export class LoginDto {
  @ApiProperty()
  @IsString()
  @MinLength(1)
  @MaxLength(50)
  username!: string;

  @ApiProperty()
  @IsString()
  @MinLength(1)
  @MaxLength(100)
  password!: string;
}
