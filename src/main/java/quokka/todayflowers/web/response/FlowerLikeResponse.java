package quokka.todayflowers.web.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FlowerLikeResponse {
    private String message;
    private String totalLikeCount;

    @Builder
    public FlowerLikeResponse(String message, String totalLikeCount) {
        this.message = message;
        this.totalLikeCount = totalLikeCount;
    }
}
