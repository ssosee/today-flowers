package quokka.todayflowers.web.client;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "root")
public class FlowerRootData {

    private String resultCode;

    private String resultMsg;

    private String repcategory;

    private FlowerResultData result;
}
