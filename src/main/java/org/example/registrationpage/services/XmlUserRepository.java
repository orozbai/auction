package org.example.registrationpage.services;

import org.example.registrationpage.dtos.UserRegisterDto;
import org.example.registrationpage.entities.UserEntity;
import org.example.registrationpage.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import java.util.Optional;

@Service
public class XmlUserRepository implements UserRepository {

    @Override
    public UserEntity getUserById(Long id) {
        try {
            // Step 1: Create a DocumentBuilderFactory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // Step 2: Create a DocumentBuilder
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Step 3: Parse the XML file
            Document document = builder.parse(new File("src/main/resources/users.xml"));  // Replace with your XML file path
            // Step 4: Normalize the document (optional but recommended)
            document.getDocumentElement().normalize();
            // Step 5: Get the root element
            Element root = document.getDocumentElement();
            // Step 6: Find user elements
            NodeList userList = root.getElementsByTagName("user");
            // Step 7: Iterate over user elements to find the one with matching id
            for (int i = 0; i < userList.getLength(); i++) {
                Element userElement = (Element) userList.item(i);
                String idStr = userElement.getElementsByTagName("id").item(0).getTextContent();
                // Check if id element is not empty
                if (idStr != null && !idStr.isEmpty()) {
                    Long userId = Long.parseLong(idStr);
                    if (userId.equals(id)) {
                        String username = userElement.getElementsByTagName("username").item(0).getTextContent();
                        String email = userElement.getElementsByTagName("email").item(0).getTextContent();
                        String password = userElement.getElementsByTagName("password").item(0).getTextContent();
                        String role = userElement.getElementsByTagName("role").item(0).getTextContent();
                        Integer age = Integer.valueOf(userElement.getElementsByTagName("age").item(0).getTextContent());
                        return new UserEntity(id, username, age, password, email, role);
                    }
                }
            }
            // If no user with the given id is found
            return null;

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace(); // Handle exception as needed
            return null;
        }
    }

    @Override
    public void saveUser(UserRegisterDto user) {
        try {
            // Step 1: Create a DocumentBuilderFactory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // Step 2: Create a DocumentBuilder
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Step 3: Parse the existing XML file or create a new document if it doesn't exist
            Document document;
            File xmlFile = new File("src/main/resources/users.xml");  // Replace with your XML file path
            if (xmlFile.exists()) {
                document = builder.parse(xmlFile);
            } else {
                document = builder.newDocument();
                Element rootElement = document.createElement("users"); // Root element for users if new file
                document.appendChild(rootElement);
            }
            Document documen = builder.parse(new File("src/main/resources/users.xml"));
            documen.getDocumentElement().normalize();
            Element root = documen.getDocumentElement();
            NodeList userList = root.getElementsByTagName("user");
            int maxId = Integer.MIN_VALUE;
            for (int i = userList.getLength() - 1; i >= 0; i--) {
                Element userElement = (Element) userList.item(i);
                long id = Long.parseLong(userElement.getElementsByTagName("id").item(0).getTextContent());
                int currentId = Math.toIntExact(id);
                if (currentId > maxId) {
                    maxId = currentId;
                }
            }
            // Step 4: Create user element and add attributes and child elements
            Element userElement = document.createElement("user");

            Element idElement = document.createElement("id");
            idElement.appendChild(document.createTextNode(String.valueOf(maxId + 1)));
            userElement.appendChild(idElement);

            Element usernameElement = document.createElement("username");
            usernameElement.appendChild(document.createTextNode(user.getName()));
            userElement.appendChild(usernameElement);

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodedPassword = encoder.encode(user.getPassword());
            Element passwordElement = document.createElement("password");
            passwordElement.appendChild(document.createTextNode(encodedPassword));
            userElement.appendChild(passwordElement);

            Element emailElement = document.createElement("email");
            emailElement.appendChild(document.createTextNode(user.getEmail()));
            userElement.appendChild(emailElement);

            Element ageElement = document.createElement("age");
            ageElement.appendChild(document.createTextNode(String.valueOf(user.getAge())));
            userElement.appendChild(ageElement);

            Element roleElement = document.createElement("role");
            roleElement.appendChild(document.createTextNode("ROLE_ADMIN"));
            userElement.appendChild(roleElement);

            // Step 5: Append user element to the root element
            document.getDocumentElement().appendChild(userElement);

            // Step 6: Write the updated document back to the XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);

        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
            e.printStackTrace(); // Handle exception as needed
        }
    }

    @Override
    public void deleteUser(Long id) {
        try {
            // Step 1: Create a DocumentBuilderFactory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // Step 2: Create a DocumentBuilder
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Step 3: Parse the XML file
            Document document = builder.parse(new File("src/main/resources/users.xml"));  // Replace with your XML file path
            // Step 4: Normalize the document (optional but recommended)
            document.getDocumentElement().normalize();
            // Step 5: Get the root element
            Element root = document.getDocumentElement();
            // Step 6: Find user elements
            NodeList userList = root.getElementsByTagName("user");
            // Step 7: Iterate over user elements to find the one with matching id
            for (int i = 0; i < userList.getLength(); i++) {
                Element userElement = (Element) userList.item(i);
                String idStr = userElement.getElementsByTagName("id").item(0).getTextContent();

                // Check if id element is not empty
                if (idStr != null && !idStr.isEmpty()) {
                    Long userId = Long.parseLong(idStr);

                    if (userId.equals(id)) {
                        // Remove the user element from the parent node
                        root.removeChild(userElement);

                        // Save the changes to the XML file
                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                        Transformer transformer = transformerFactory.newTransformer();
                        DOMSource source = new DOMSource(document);
                        StreamResult result = new StreamResult(new File("src/main/resources/users.xml"));  // Replace with your XML file path
                        transformer.transform(source, result);

                        return;  // Exit after deleting the user
                    }
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
            e.printStackTrace(); // Handle exception as needed
        }
    }

    @Override
    public List<UserEntity> getAllUsers() {
        List<UserEntity> users = new ArrayList<>();
        try {
            // Step 1: Create a DocumentBuilderFactory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // Step 2: Create a DocumentBuilder
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Step 3: Parse the XML file
            Document document = builder.parse(new File("src/main/resources/users.xml"));  // Replace with your XML file path
            // Step 4: Normalize the document (optional but recommended)
            document.getDocumentElement().normalize();
            // Step 5: Get the root element
            Element root = document.getDocumentElement();
            // Step 6: Find user elements
            NodeList userList = root.getElementsByTagName("user");
            // Step 7: Iterate over user elements to extract data
            for (int i = 0; i < userList.getLength(); i++) {
                Element userElement = (Element) userList.item(i);
                // Extract user data
                String username = userElement.getElementsByTagName("username").item(0).getTextContent();
                String email = userElement.getElementsByTagName("email").item(0).getTextContent();
                String password = userElement.getElementsByTagName("password").item(0).getTextContent();
                String role = userElement.getElementsByTagName("role").item(0).getTextContent();
                Integer age = Integer.valueOf(userElement.getElementsByTagName("age").item(0).getTextContent());
                Long id = Long.valueOf(userElement.getElementsByTagName("id").item(0).getTextContent());
                // Assuming UserEntity constructor takes username and email as parameters
                UserEntity user = new UserEntity(id, username, age, password, email, role);
                users.add(user);
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace(); // Handle exception as needed
        }

        return users;
    }

    @Override
    public Optional<UserEntity> findByName(String username) {
        try {
            // Step 1: Create a DocumentBuilderFactory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // Step 2: Create a DocumentBuilder
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Step 3: Parse the XML file
            Document document = builder.parse(new File("src/main/resources/users.xml"));  // Replace with your XML file path
            // Step 4: Normalize the document (optional but recommended)
            document.getDocumentElement().normalize();
            // Step 5: Get the root element
            Element root = document.getDocumentElement();
            // Step 6: Find user elements
            NodeList userList = root.getElementsByTagName("user");
            // Step 7: Iterate over user elements to find the one with matching id
            for (int i = 0; i < userList.getLength(); i++) {
                Element userElement = (Element) userList.item(i);
                String nameId = userElement.getElementsByTagName("username").item(0).getTextContent();
                if (nameId.equals(username)) {
                    // Found the user with the given id, extract data
                    String name = userElement.getElementsByTagName("username").item(0).getTextContent();
                    String email = userElement.getElementsByTagName("email").item(0).getTextContent();
                    String password = userElement.getElementsByTagName("password").item(0).getTextContent();
                    String role = userElement.getElementsByTagName("role").item(0).getTextContent();
                    Integer age = Integer.valueOf(userElement.getElementsByTagName("age").item(0).getTextContent());
                    Long id = (long) Integer.parseInt(userElement.getElementsByTagName("id").item(0).getTextContent());

                    // Assuming UserEntity constructor takes username and email as parameters
                    return Optional.of(new UserEntity(id, name, age, password, email, role));
                }
            }
            // If no user with the given id is found
            return Optional.empty();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace(); // Handle exception as needed
            return Optional.empty();
        }
    }

    @Override
    public void updateUserById(UserRegisterDto updatedUser) {
        try {
            // Step 1: Create a DocumentBuilderFactory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // Step 2: Create a DocumentBuilder
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Step 3: Parse the XML file
            Document document = builder.parse(new File("src/main/resources/users.xml"));  // Replace with your XML file path
            // Step 4: Normalize the document (optional but recommended)
            document.getDocumentElement().normalize();
            // Step 5: Get the root element
            Element root = document.getDocumentElement();
            // Step 6: Find user elements
            NodeList userList = root.getElementsByTagName("user");
            // Step 7: Iterate over user elements to find the one with matching id
            for (int i = 0; i < userList.getLength(); i++) {
                Element userElement = (Element) userList.item(i);
                String idStr = userElement.getElementsByTagName("id").item(0).getTextContent();
                // Check if id element is not empty
                if (idStr != null && !idStr.isEmpty()) {
                    Long userId = Long.parseLong(idStr);
                    if (userId.equals(updatedUser.getId())) {
                        String password = userElement.getElementsByTagName("password").item(0).getTextContent();
                        UserRegisterDto newUser = new UserRegisterDto(updatedUser.getName(), updatedUser.getAge(),
                                password, updatedUser.getEmail(), updatedUser.getId());
                        deleteUser(updatedUser.getId());
                        saveUser(newUser);
                    }
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace(); // Handle exception as needed
        }
    }
}
