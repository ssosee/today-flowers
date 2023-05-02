package quokka.todayflowers.external.api.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "document")
public class FlowerDocumentData {
    private FlowerRootData root;
}
