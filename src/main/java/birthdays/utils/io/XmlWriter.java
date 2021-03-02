package birthdays.utils.io;


import birthdays.model.BDayUnit;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;

public class XmlWriter {

    private File file;

    public XmlWriter(String filePath) {
        file = new File(filePath);
    }

    public void saveBdays(List<BDayUnit> dataBase) throws Exception, NullPointerException{
        Document doc = getDoc();
        Element rootElement = doc.createElementNS("","directory");
        doc.appendChild(rootElement);

        for(BDayUnit unit: dataBase){
            if(unit.getPhoneNumber() == null){
                unit.setPhoneNumber("");
            }
            rootElement.appendChild((getLanguage(doc,unit)));
        }

        DOMSource source = new DOMSource(doc);
        StreamResult file = new StreamResult(this.file);
        Transformer transformer = getTransformer();
        transformer.transform(source, file);
    }

    private static Node getLanguage(Document doc, BDayUnit unit) {
        Element language = doc.createElement("directory");
//        language.setAttribute("id", String.valueOf(unit.getId()));
        language.appendChild(getLanguageElements(doc, language, "id", String.valueOf(unit.getId())));
        language.appendChild(getLanguageElements(doc, language, "first_name", unit.getFirstName()));
        language.appendChild(getLanguageElements(doc, language, "last_name", unit.getLastName()));
        language.appendChild(getLanguageElements(doc, language, "birthday", unit.getDate()));
        language.appendChild(getLanguageElements(doc, language, "phone_num", unit.getPhoneNumber()));
        return language;
    }

    private Document getDoc() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.newDocument();
    }

    private static Node getLanguageElements(Document doc, Element element, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }

    private Transformer getTransformer() throws TransformerConfigurationException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        return transformer;
    }
}

