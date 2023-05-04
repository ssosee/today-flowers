package quokka.todayflowers.web.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FlowerLikeResponse {
    private String message;
    private Long totalLikeCount;
    private Boolean like;

    @Builder
    public FlowerLikeResponse(String message, Long totalLikeCount, Boolean like) {
        this.message = message;
        this.totalLikeCount = totalLikeCount;
        this.like = like;
    }
}
