package quokka.todayflowers.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quokka.todayflowers.domain.repository.FlowerRepository;
import quokka.todayflowers.web.response.TodayFlowerForm;

@Service
@RequiredArgsConstructor
@Transactional
public class FlowerServiceImpl implements FlowerService {

    private final FlowerRepository flowerRepository;

    @Override
    public TodayFlowerForm findTodayFlower(String month, String day) {


        return null;
    }
}
