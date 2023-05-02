package quokka.todayflowers.web.client;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "result")
public class FlowerResultData {

    private String dataNo;

    private String fMonth;

    private String fDay;

    private String flowNm;

    private String fSctNm;

    private String fEngNm;

    private String flowLang;

    private String fContent;

    private String fUse;

    private String fGrow;

    private String fType;

    private String fileName1;

    private String fileName2;

    private String fileName3;

    private String imgUrl1;

    private String imgUrl2;

    private String imgUrl3;

    private String publishOrg;
}
