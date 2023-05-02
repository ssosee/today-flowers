package quokka.todayflowers.external.api;

import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.JAXBException;

import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import quokka.todayflowers.domain.entity.Flower;
import quokka.todayflowers.domain.entity.FlowerPhoto;
import quokka.todayflowers.domain.repository.FlowerPhotoRepository;
import quokka.todayflowers.domain.repository.FlowerRepository;
import quokka.todayflowers.external.api.response.FlowerDocumentData;
import quokka.todayflowers.external.api.response.FlowerResultData;
import quokka.todayflowers.external.api.response.FlowerRootData;

import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class SaveTodayFlowerService {
    private final FlowerRepository flowerRepository;
    private final FlowerPhotoRepository flowerPhotoRepository;
    private final RestTemplate restTemplate;

    public void saveFlowerDetailData() {

        for(int i = 1; i <= 366; i++) {
            String uri = "https://apis.data.go.kr/1390804/NihhsTodayFlowerInfo01/selectTodayFlowerView01?servicekey=SxiFBkwFdvX0KlFjfLEWi6HYwn67iYE40MaNnxIA58ZigkRDrLljf1XLGnQb0Hh01BFX2kaw7PMJCvLlazNFBA==&dataNo="+i;
            FlowerDocumentData documentData = restTemplate.getForObject(uri, FlowerDocumentData.class);
            FlowerRootData root = documentData.getRoot();
            FlowerResultData result = root.getResult();

            FlowerPhoto flowerPhoto1 = FlowerPhoto.createFlowerPhoto(result.getImgUrl1(), result.getPublishOrg());
            FlowerPhoto flowerPhoto2 = FlowerPhoto.createFlowerPhoto(result.getImgUrl2(), result.getPublishOrg());
            FlowerPhoto flowerPhoto3 = FlowerPhoto.createFlowerPhoto(result.getImgUrl3(), result.getPublishOrg());

            List<FlowerPhoto> flowerPhotos = new ArrayList<>();
            flowerPhotos.add(flowerPhoto1);
            flowerPhotos.add(flowerPhoto2);
            flowerPhotos.add(flowerPhoto3);

            Flower flower = Flower.createFlower(result.getFlowNm(),
                    result.getFlowLang(),
                    result.getFContent(),
                    result.getFMonth(),
                    result.getFDay(),
                    result.getPublishOrg());

            flowerPhotos.stream().forEach(flower::changeFlowerPhoto);

            flowerRepository.save(flower);
            flowerPhotoRepository.saveAll(flowerPhotos);
        }
    }

    public FlowerDocumentData getTest() throws JAXBException {
        String uri = "https://apis.data.go.kr/1390804/NihhsTodayFlowerInfo01/selectTodayFlowerView01?servicekey=SxiFBkwFdvX0KlFjfLEWi6HYwn67iYE40MaNnxIA58ZigkRDrLljf1XLGnQb0Hh01BFX2kaw7PMJCvLlazNFBA==&dataNo=1";
        restTemplate.getMessageConverters().stream().forEach(System.out::println);
        FlowerDocumentData documentData = restTemplate.getForObject(uri, FlowerDocumentData.class);

        return documentData;
    }
}
