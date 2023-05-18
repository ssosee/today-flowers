package quokka.todayflowers.domain.service;

import org.springframework.data.domain.Pageable;
import quokka.todayflowers.domain.entity.FlowerLike;
import quokka.todayflowers.web.response.BasicFlowerForm;
import quokka.todayflowers.web.response.FlowerListForm;

import java.util.List;

public interface FlowerLikeService {

    BasicFlowerForm findFlowerLikeListByMember(Pageable pageable, String userId);
}
