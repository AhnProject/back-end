package com.devahn.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 벡터 설정 관리
 */
@Getter
@Component
public class VectorConfig {

    @Value("${vector.dimension:1536}")
    private int dimension;

    @Value("${vector.similarity-threshold:0.7}")
    private float similarityThreshold;

    @Value("${vector.max-search-results:100}")
    private int maxSearchResults;

    @Value("${vector.enable-normalization:true}")
    private boolean enableNormalization;
}
