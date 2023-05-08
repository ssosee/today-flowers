package quokka.todayflowers.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quokka.todayflowers.domain.entity.FlowerLike;
import quokka.todayflowers.domain.repository.FlowerLikeRepository;
import quokka.todayflowers.web.response.FlowerListForm;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FlowerLikeServiceImpl implements FlowerLikeService {

    private final FlowerLikeRepository flowerLikeRepository;

    @Override
    public List<FlowerListForm> getFlowerListForm(List<FlowerLike> flowerLikeList) {

       return flowerLikeList.stream()
                .map(flowerLike -> FlowerListForm.builder()
                        .id(flowerLike.getFlower().getId())
                        .hits(flowerLike.getFlower().getHits())
                        .name(flowerLike.getFlower().getName())
                        .lang(flowerLike.getFlower().getFlowerLang())
                        .path(flowerLike.getFlower().getFlowerPhotos().get(0).getPath())
                        .totalLike(flowerLike.getFlower().getTotalLike())
                        .build())
                .collect(Collectors.toList());
    }
}
