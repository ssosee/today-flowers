package quokka.todayflowers.domain.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import quokka.todayflowers.web.response.FlowerListForm;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FlowerServiceImplTest {
    @Autowired
    FlowerService flowerService;

    @Test
    void test() {
        PageRequest pageRequest = PageRequest.of(0, 5);
        List<FlowerListForm> flowerList = flowerService.findFlowerList(pageRequest);
        assertAll(
                () -> assertFalse(flowerList.isEmpty()),
                () -> assertEquals(flowerList.size(), 5)
        );
    }
}