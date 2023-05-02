package quokka.todayflowers.external.api.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "result")
public class FlowerResultData {
    @JacksonXmlProperty(localName = "dataNo")
    private Integer dataNo;
    @JacksonXmlProperty(localName = "fMonth")
    private Integer fMonth;
    @JacksonXmlProperty(localName = "fDay")
    private Integer fDay;
    @JacksonXmlProperty(localName = "flowNm")
    private String flowNm;
    @JacksonXmlProperty(localName = "fSctNm")
    private Integer fSctNm;
    @JacksonXmlProperty(localName = "fEngNm")
    private String fEngNm;
    @JacksonXmlProperty(localName = "flowLang")
    private String flowLang;
    @JacksonXmlProperty(localName = "fContent")
    private String fContent;
    @JacksonXmlProperty(localName = "fUse")
    private String fUse;
    @JacksonXmlProperty(localName = "fGrow")
    private String fGrow;
    @JacksonXmlProperty(localName = "fType")
    private String fType;
    @JacksonXmlProperty(localName = "fileName1")
    private String fileName1;
    @JacksonXmlProperty(localName = "fileName2")
    private String fileName2;
    @JacksonXmlProperty(localName = "fileName3")
    private String fileName3;
    @JacksonXmlProperty(localName = "imgUrl1")
    private String imgUrl1;
    @JacksonXmlProperty(localName = "imgUrl2")
    private String imgUrl2;
    @JacksonXmlProperty(localName = "imgUrl3")
    private String imgUrl3;
    @JacksonXmlProperty(localName = "publishOrg")
    private String publishOrg;
}
