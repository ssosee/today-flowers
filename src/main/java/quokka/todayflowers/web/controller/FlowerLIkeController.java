package quokka.todayflowers.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    @PostMapping(value = "/like")
    public ResponseEntity<FlowerLikeResponse> flowerLike(@RequestBody FlowerLikeForm form) {
        FlowerLikeResponse flowerLikeResponse = flowerService.likeFlower(form.getFlowerId(), form.getLike());

        return new ResponseEntity<>(flowerLikeResponse, HttpStatus.OK);
    }
}
