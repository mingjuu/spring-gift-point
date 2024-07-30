package gift.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "위시 응답 DTO")
public class WishResponseDto {
    @Schema(description = "위시 고유 id")
    private final Long id;
    @Schema(description = "위시리스트에 추가된 상품의 id")
    private final Long productId;
    @Schema(description = "해당 위시 요청하는 사람의 token 값")
    private final String tokenValue;

    public WishResponseDto(Long id, Long productId, String tokenValue) {
        this.id = id;
        this.productId = productId;
        this.tokenValue = tokenValue;
    }

    public WishResponseDto(Long productId, String tokenValue) {
        this(null, productId, tokenValue);
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getTokenValue() {
        return tokenValue;
    }

}