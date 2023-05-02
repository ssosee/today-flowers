package quokka.todayflowers.web.client;

import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import quokka.todayflowers.external.api.SaveTodayFlowerService;
import quokka.todayflowers.external.api.response.FlowerDocumentData;

@SpringBootTest
class SaveTodayFlowerServiceTest {
    @Autowired
    SaveTodayFlowerService saveTodayFlowerService;

    @Test
    @DisplayName("오늘의 꽃 상세보기 api 테스트")
    @Commit
    void getTodayFlowerDetailViewTest() throws JAXBException {
        saveTodayFlowerService.saveFlowerDetailData();
        //FlowerDocumentData test = saveTodayFlowerService.getTest();
        //System.out.println(test);
    }

}