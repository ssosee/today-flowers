package quokka.todayflowers.web.request;

import lombok.Data;

@Data
public class FlowerLikeForm {
    private Long flowerId;
    private Boolean like;
}
