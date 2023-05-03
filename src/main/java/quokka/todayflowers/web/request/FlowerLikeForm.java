package quokka.todayflowers.web.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FlowerLikeForm {
    private Long flowerId;
    private Boolean like;

    public FlowerLikeForm(Long flowerId, Boolean like) {
        this.flowerId = flowerId;
        this.like = like;
    }
}
