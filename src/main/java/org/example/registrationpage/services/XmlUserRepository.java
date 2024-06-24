package org.example.registrationpage.services;

import org.example.registrationpage.dtos.UserRegisterDto;
import org.example.registrationpage.entities.UserEntity;
import org.example.registrationpage.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import java.util.Objects;
import java.util.Optional;

@Service
public class XmlUserRepository implements UserRepository {

    @Override
    public UserEntity getUserById(Long id) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("src/main/resources/users.xml"));
            document.getDocumentElement().normalize();
            Element root = document.getDocumentElement();
            NodeList userList = root.getElementsByTagName("user");
            for (int i = 0; i < userList.getLength(); i++) {
                Element userElement = (Element) userList.item(i);
                String idStr = userElement.getElementsByTagName("id").item(0).getTextContent();
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
            return null;

        } catch (ParserConfigurationException | IOException | SAXException e) {
            return null;
        }
    }

    @Override
    public void saveUser(UserRegisterDto user) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document;
            File xmlFile = new File("src/main/resources/users.xml");
            if (xmlFile.exists()) {
                document = builder.parse(xmlFile);
            } else {
                document = builder.newDocument();
                Element rootElement = document.createElement("users");
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

            document.getDocumentElement().appendChild(userElement);

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
    public void deleteUser(Long id) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("src/main/resources/users.xml"));
            document.getDocumentElement().normalize();
            Element root = document.getDocumentElement();
            NodeList userList = root.getElementsByTagName("user");
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
                        StreamResult result = new StreamResult(new File("src/main/resources/users.xml"));  // Replace with your XML file path
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
    public List<UserEntity> getAllUsers() {
        List<UserEntity> users = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("src/main/resources/users.xml"));  // Replace with your XML file path
            document.getDocumentElement().normalize();
            Element root = document.getDocumentElement();
            NodeList userList = root.getElementsByTagName("user");
            for (int i = 0; i < userList.getLength(); i++) {
                Element userElement = (Element) userList.item(i);
                String username = userElement.getElementsByTagName("username").item(0).getTextContent();
                String email = userElement.getElementsByTagName("email").item(0).getTextContent();
                String password = userElement.getElementsByTagName("password").item(0).getTextContent();
                String role = userElement.getElementsByTagName("role").item(0).getTextContent();
                Integer age = Integer.valueOf(userElement.getElementsByTagName("age").item(0).getTextContent());
                Long id = Long.valueOf(userElement.getElementsByTagName("id").item(0).getTextContent());
                UserEntity user = new UserEntity(id, username, age, password, email, role);
                users.add(user);
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public Optional<UserEntity> findByName(String username) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("src/main/resources/users.xml"));
            document.getDocumentElement().normalize();
            Element root = document.getDocumentElement();
            NodeList userList = root.getElementsByTagName("user");
            for (int i = 0; i < userList.getLength(); i++) {
                Element userElement = (Element) userList.item(i);
                String nameId = userElement.getElementsByTagName("username").item(0).getTextContent();
                if (nameId.equals(username)) {
                    String name = userElement.getElementsByTagName("username").item(0).getTextContent();
                    String email = userElement.getElementsByTagName("email").item(0).getTextContent();
                    String password = userElement.getElementsByTagName("password").item(0).getTextContent();
                    String role = userElement.getElementsByTagName("role").item(0).getTextContent();
                    Integer age = Integer.valueOf(userElement.getElementsByTagName("age").item(0).getTextContent());
                    Long id = (long) Integer.parseInt(userElement.getElementsByTagName("id").item(0).getTextContent());

                    return Optional.of(new UserEntity(id, name, age, password, email, role));
                }
            }
            return Optional.empty();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public void updateUserById(UserRegisterDto updatedUser) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("src/main/resources/users.xml"));
            document.getDocumentElement().normalize();
            Element root = document.getDocumentElement();
            NodeList userList = root.getElementsByTagName("user");
            for (int i = 0; i < userList.getLength(); i++) {
                Element userElement = (Element) userList.item(i);
                String idStr = userElement.getElementsByTagName("id").item(0).getTextContent();
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
            e.printStackTrace();
        }
    }

    @Override
    public Long getCurrentUserId() {
        String username = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();

            }
        }
        Long userId = null;
        List<UserEntity> users = getAllUsers();
        for (UserEntity user : users) {
            if (Objects.equals(user.getName(), username)) {
                userId = user.getId();
            }
        }
        return userId;
    }
}
