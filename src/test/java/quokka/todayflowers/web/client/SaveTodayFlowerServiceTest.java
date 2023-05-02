package quokka.todayflowers.web.client;

import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SaveTodayFlowerServiceTest {
    @Autowired
    SaveTodayFlowerService saveTodayFlowerService;

    @Test
    @DisplayName("오늘의 꽃 상세보기 api 테스트")
    void getTodayFlowerDetailViewTest() throws JAXBException {
        FlowerDocumentData nihhsTodayFlowerInfo01 = saveTodayFlowerService.saveFlowerDetailData();
    }

}