package birthdays.utils.io;

import birthdays.model.BDayUnit;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;

public class XmlReader {

    private final String filePath;

    public XmlReader(String filePath){
        this.filePath = filePath;
    }

    public ArrayList<BDayUnit> readBdays() throws Exception{
        NodeList nodeListTemp = getNodeList(filePath);
        ArrayList<BDayUnit> BdayUnitsList = new ArrayList<BDayUnit>();
        for (int i = 1; i < nodeListTemp.getLength(); i++) {
            BdayUnitsList.add(getDirectoryBdaysUnitData(nodeListTemp.item(i)));
        }
        Collections.reverse(BdayUnitsList);
        return BdayUnitsList;
    }

    private static BDayUnit getDirectoryBdaysUnitData(Node node) {
        BDayUnit output = new BDayUnit();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
//            try {
//                BDayUnit output2 = new BDayUnit(
//                        Integer.parseInt(getTagValue("id", element)),
//                        getTagValue("first_name", element),
//                        getTagValue("last_name", element),
//                        new Date(Long.parseLong(getTagValue("birthday", element))),
//                        getTagValue("phone_num", element)
//                );
//                System.out.println(output2.toString());
//                output = output2;
//            }catch (NullPointerException e){
//                e.printStackTrace();
//            }
//            try{
//                output.setId(Integer.parseInt(getTagValue("id", element)));
//            }catch (NullPointerException e){
//                System.out.println("id");
//            }

            try{
                output.setFirstName(getTagValue("first_name", element));
            }catch (NullPointerException e){
                System.out.println("id2");
            }

            try{
                output.setLastName(getTagValue("last_name", element));
            }catch (NullPointerException e){
                System.out.println("id3");
            }

            try{
                output.setDate(getTagValue("birthday", element));
            }catch (NullPointerException e){
                System.out.println("id4");
            }

            try{
                output.setPhoneNumber(getTagValue("phone_num", element));
            }catch (NullPointerException e){
                System.out.println("id5");
            }

        }
        return output;
    }

    private NodeList getNodeList(String filepath) throws Exception{

        File xmlFile = new File(filepath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlFile);
        document.getDocumentElement().normalize();

        NodeList nodeListTemp = document.getElementsByTagName("directory");

        return nodeListTemp;
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }
}
