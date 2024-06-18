package org.example.registrationpage.services;

import org.example.registrationpage.dtos.LotRegisterDto;
import org.example.registrationpage.entities.LotEntity;
import org.example.registrationpage.repositories.LotRepository;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class XmlLotRepository implements LotRepository {
    @Override
    public LotEntity getLotById(Long id) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("src/main/resources/lot.xml"));
            document.getDocumentElement().normalize();
            Element root = document.getDocumentElement();
            NodeList userList = root.getElementsByTagName("lot");
            for (int i = 0; i < userList.getLength(); i++) {
                Element userElement = (Element) userList.item(i);
                String idStr = userElement.getElementsByTagName("id").item(0).getTextContent();
                if (idStr != null && !idStr.isEmpty()) {
                    Long userId = Long.parseLong(idStr);
                    if (userId.equals(id)) {
                        String name = userElement.getElementsByTagName("name").item(0).getTextContent();
                        String description = userElement.getElementsByTagName("description").item(0).getTextContent();
                        Integer quantity = Integer.parseInt(userElement.getElementsByTagName("quantity").item(0).getTextContent());
                        Integer price = Integer.parseInt(userElement.getElementsByTagName("price").item(0).getTextContent());
                        return new LotEntity(id, name, description, quantity, price);
                    }
                }
            }
            return null;

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void saveLot(LotRegisterDto lot) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document;
            File xmlFile = new File("src/main/resources/lot.xml");
            if (xmlFile.exists()) {
                document = builder.parse(xmlFile);
            } else {
                document = builder.newDocument();
                Element rootElement = document.createElement("lots");
                document.appendChild(rootElement);
            }
            Document documen = builder.parse(new File("src/main/resources/lot.xml"));
            documen.getDocumentElement().normalize();
            Element root = documen.getDocumentElement();
            NodeList userList = root.getElementsByTagName("lot");
            int maxId = Integer.MIN_VALUE;
            for (int i = userList.getLength() - 1; i >= 0; i--) {
                Element userElement = (Element) userList.item(i);
                long id = Long.parseLong(userElement.getElementsByTagName("id").item(0).getTextContent());
                int currentId = Math.toIntExact(id);
                if (currentId > maxId) {
                    maxId = currentId;
                }
            }
            Element lotElement = document.createElement("lot");

            Element idElement = document.createElement("id");
            idElement.appendChild(document.createTextNode(String.valueOf(maxId + 1)));
            lotElement.appendChild(idElement);

            Element usernameElement = document.createElement("name");
            usernameElement.appendChild(document.createTextNode(lot.getName()));
            lotElement.appendChild(usernameElement);

            Element emailElement = document.createElement("description");
            emailElement.appendChild(document.createTextNode(lot.getDescription()));
            lotElement.appendChild(emailElement);

            Element ageElement = document.createElement("quantity");
            ageElement.appendChild(document.createTextNode(String.valueOf(lot.getQuantity())));
            lotElement.appendChild(ageElement);

            Element price = document.createElement("price");
            price.appendChild(document.createTextNode(String.valueOf(lot.getPrice())));
            lotElement.appendChild(price);

            document.getDocumentElement().appendChild(lotElement);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);

        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteLot(Long id) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("src/main/resources/lot.xml"));
            document.getDocumentElement().normalize();
            Element root = document.getDocumentElement();
            NodeList userList = root.getElementsByTagName("lot");
            for (int i = 0; i < userList.getLength(); i++) {
                Element userElement = (Element) userList.item(i);
                String idStr = userElement.getElementsByTagName("id").item(0).getTextContent();
                if (idStr != null && !idStr.isEmpty()) {
                    Long userId = Long.parseLong(idStr);
                    if (userId.equals(id)) {
                        root.removeChild(userElement);
                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                        Transformer transformer = transformerFactory.newTransformer();
                        DOMSource source = new DOMSource(document);
                        StreamResult result = new StreamResult(new File("src/main/resources/lot.xml"));
                        transformer.transform(source, result);
                        return;
                    }
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<LotEntity> getAllLots() {
        List<LotEntity> users = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("src/main/resources/lot.xml"));
            document.getDocumentElement().normalize();
            Element root = document.getDocumentElement();
            NodeList userList = root.getElementsByTagName("lot");
            for (int i = 0; i < userList.getLength(); i++) {
                Element lotElement = (Element) userList.item(i);
                String name = lotElement.getElementsByTagName("name").item(0).getTextContent();
                String description = lotElement.getElementsByTagName("description").item(0).getTextContent();
                Integer quantity = Integer.parseInt(lotElement.getElementsByTagName("quantity").item(0).getTextContent());
                Integer price = Integer.parseInt(lotElement.getElementsByTagName("price").item(0).getTextContent());
                Long id = Long.valueOf(lotElement.getElementsByTagName("id").item(0).getTextContent());
                LotEntity lot = new LotEntity(id, name, description, quantity, price);
                users.add(lot);
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public void updateLotById(LotRegisterDto updatedLot) {
        System.out.println(updatedLot);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("src/main/resources/lot.xml"));
            document.getDocumentElement().normalize();
            Element root = document.getDocumentElement();
            NodeList userList = root.getElementsByTagName("lot");
            for (int i = 0; i < userList.getLength(); i++) {
                Element userElement = (Element) userList.item(i);
                String idStr = userElement.getElementsByTagName("id").item(0).getTextContent();
                if (idStr != null && !idStr.isEmpty()) {
                    Long userId = Long.parseLong(idStr);
                    if (userId.equals(updatedLot.getId())) {
                        LotRegisterDto newLot = new LotRegisterDto(updatedLot.getId(), updatedLot.getName(),
                                updatedLot.getDescription(), updatedLot.getQuantity(), updatedLot.getPrice());
                        deleteLot(updatedLot.getId());
                        saveLot(newLot);
                    }
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }
}
