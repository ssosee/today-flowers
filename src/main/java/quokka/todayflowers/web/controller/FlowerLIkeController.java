package quokka.todayflowers.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import quokka.todayflowers.domain.service.FlowerService;
import quokka.todayflowers.global.constant.ConstFlower;
import quokka.todayflowers.web.request.FlowerLikeForm;
import quokka.todayflowers.web.response.FlowerLikeResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/flower")
public class FlowerLIkeController {

    private final FlowerService flowerService;

    // 좋아요
    @PostMapping("/like")
    public ResponseEntity<FlowerLikeResponse> flowerLike(FlowerLikeForm form) {
        String totalFlowerLikeCount = flowerService.likeFlower(form.getFlowerId(), form.getLike());

        return new ResponseEntity<>(new FlowerLikeResponse(ConstFlower.FLOWER_FOUND, totalFlowerLikeCount), HttpStatus.OK);
    }
}
