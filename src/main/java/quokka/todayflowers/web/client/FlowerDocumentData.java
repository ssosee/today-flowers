package quokka.todayflowers.web.client;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "document")
public class FlowerDocumentData {
    private FlowerRootData root;
}
