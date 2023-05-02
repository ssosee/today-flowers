package quokka.todayflowers.web.client;

import jakarta.xml.bind.JAXBException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import quokka.todayflowers.domain.repository.FlowerPhotoRepository;
import quokka.todayflowers.domain.repository.FlowerRepository;



@Component
@RequiredArgsConstructor
public class SaveTodayFlowerService {
    private final FlowerRepository flowerRepository;
    private final FlowerPhotoRepository flowerPhotoRepository;
    private final RestTemplate restTemplate;

    public FlowerDocumentData saveFlowerDetailData() throws JAXBException {
        String uri = "https://apis.data.go.kr/1390804/NihhsTodayFlowerInfo01/selectTodayFlowerView01?servicekey=SxiFBkwFdvX0KlFjfLEWi6HYwn67iYE40MaNnxIA58ZigkRDrLljf1XLGnQb0Hh01BFX2kaw7PMJCvLlazNFBA==&dataNo=1";

        FlowerDocumentData documentData = restTemplate.getForObject(uri, FlowerDocumentData.class);

        return documentData;
    }
}
