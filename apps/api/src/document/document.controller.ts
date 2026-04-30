import {
  Controller, Get, Post, Put, Delete, Body, Param, UseGuards, HttpCode, HttpStatus,
} from "@nestjs/common";
import { ApiTags, ApiOperation, ApiBearerAuth } from "@nestjs/swagger";
import { JwtAuthGuard } from "../common/guards/jwt-auth.guard";
import { DocumentService } from "./document.service";
import { CreateDocumentDto, UpdateDocumentDto, SearchDocumentDto } from "./dto/document.dto";

const R = (data: unknown, message: string, status = 200) => ({
  success: true, data, message, errorCode: null, timestamp: Date.now(),
});

@ApiTags("documents")
@Controller("documents")
export class DocumentController {
  constructor(private readonly documentService: DocumentService) {}

  @Get("health")
  @ApiOperation({ summary: "Health check" })
  health() {
    return R("OK", "Document API is running");
  }

  @Post()
  @UseGuards(JwtAuthGuard)
  @ApiBearerAuth()
  @HttpCode(HttpStatus.CREATED)
  @ApiOperation({ summary: "문서 생성" })
  async create(@Body() dto: CreateDocumentDto) {
    const data = await this.documentService.create(dto);
    return R(data, "Document created");
  }

  @Get()
  @UseGuards(JwtAuthGuard)
  @ApiBearerAuth()
  @ApiOperation({ summary: "문서 목록" })
  async findAll() {
    const data = await this.documentService.findAll();
    return R(data, `Returned ${data.length} document(s)`);
  }

  @Get(":id")
  @UseGuards(JwtAuthGuard)
  @ApiBearerAuth()
  @ApiOperation({ summary: "문서 단건 조회" })
  async findOne(@Param("id") id: string) {
    const data = await this.documentService.findById(id);
    return R(data, "Document found");
  }

  @Put(":id")
  @UseGuards(JwtAuthGuard)
  @ApiBearerAuth()
  @ApiOperation({ summary: "문서 수정" })
  async update(@Param("id") id: string, @Body() dto: UpdateDocumentDto) {
    await this.documentService.update(id, dto);
    return R(null, "Document updated");
  }

  @Delete(":id")
  @UseGuards(JwtAuthGuard)
  @ApiBearerAuth()
  @ApiOperation({ summary: "문서 삭제" })
  async remove(@Param("id") id: string) {
    await this.documentService.delete(id);
    return R(null, "Document deleted");
  }

  @Post("search")
  @UseGuards(JwtAuthGuard)
  @ApiBearerAuth()
  @ApiOperation({ summary: "벡터 검색" })
  async search(@Body() dto: SearchDocumentDto) {
    const data = await this.documentService.search(dto);
    return R(data, `Found ${data.length} similar document(s)`);
  }
}
