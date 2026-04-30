import { Controller, Post, Get, Body, HttpCode, HttpStatus } from "@nestjs/common";
import { ApiTags, ApiOperation } from "@nestjs/swagger";
import { RecommendService } from "./recommend.service";
import { RecommendDto } from "./dto/recommend.dto";

const R = (data: unknown, message: string) => ({
  success: true, data, message, errorCode: null, timestamp: Date.now(),
});

@ApiTags("recommend")
@Controller("recommend")
export class RecommendController {
  constructor(private readonly recommendService: RecommendService) {}

  @Get()
  @ApiOperation({ summary: "Recommend API 상태 확인" })
  health() {
    return R(
      { endpoint: "POST /api/recommend", description: "Natural language recommendation endpoint" },
      "Recommend API is running"
    );
  }

  @Post()
  @HttpCode(HttpStatus.OK)
  @ApiOperation({ summary: "자연어 여행지 추천" })
  async recommend(@Body() dto: RecommendDto) {
    const data = await this.recommendService.recommend(dto);
    return R(data, "Recommend succeeded");
  }
}
