import { IsString, IsNumber, IsArray, IsOptional, MaxLength, MinLength, IsInt, Min, Max } from "class-validator";
import { ApiProperty, ApiPropertyOptional } from "@nestjs/swagger";

export class CreateDocumentDto {
  @ApiProperty()
  @IsString()
  @MinLength(1)
  @MaxLength(255)
  title!: string;

  @ApiProperty()
  @IsString()
  @MinLength(1)
  content!: string;

  @ApiPropertyOptional()
  @IsArray()
  @IsNumber({}, { each: true })
  @IsOptional()
  embedding?: number[] | null;
}

export class UpdateDocumentDto extends CreateDocumentDto {}

export class SearchDocumentDto {
  @ApiProperty()
  @IsArray()
  @IsNumber({}, { each: true })
  embedding!: number[];

  @ApiPropertyOptional()
  @IsInt()
  @Min(1)
  @Max(20)
  @IsOptional()
  limit?: number = 10;

  @ApiPropertyOptional()
  @IsNumber()
  @Min(0)
  @Max(1)
  @IsOptional()
  threshold?: number;
}
