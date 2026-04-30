import { IsString, IsNumber, IsInt, Min, Max, MinLength, MaxLength } from "class-validator";
import { ApiProperty, ApiPropertyOptional } from "@nestjs/swagger";

export class RecommendDto {
  @ApiProperty()
  @IsString()
  @MinLength(1)
  @MaxLength(500)
  query!: string;

  @ApiPropertyOptional()
  @IsInt()
  @Min(1)
  @Max(20)
  topK: number = 5;

  @ApiPropertyOptional()
  @IsNumber()
  @Min(0)
  @Max(1)
  threshold: number = 0.5;
}
