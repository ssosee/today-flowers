package quokka.todayflowers.external.api.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "root")
public class FlowerRootData {

    private String resultCode;

    private String resultMsg;

    private String repcategory;

    private FlowerResultData result;
}
