package com.goodworkalan.comfort.xml;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class DocumentTest {
    @Test
    public void test() {
        Document document = Document.load(getClass().getResourceAsStream("example.xml"));
        document.setNamespace("doc", "http://goodworkalan.com/document");
        for (Element element : document.elements("/doc:document/doc:employee")) {
            System.out.println(element.getText("doc:first-name"));
        }
    }
    
    @Test
    public void pomParent() {
        Document document = Document.load(getClass().getResourceAsStream("plexus-utils-1.0.2.pom"));
//        for (Element element : document.elements("/*[local-name() = 'project']/*[local-name() = 'parent' and *[local-name() = 'artifactId']]")) {     
        for (Element element : document.elements("/*[local-name() = 'project']/*[local-name() = 'parent' and *[local-name() = 'artifactId']]")) {
           assertEquals(element.getText("artifactId"), "plexus-root");
           assertEquals(element.getText("groupId"), "plexus");
           assertEquals(element.getText("version"), "1.0.3");
        }
        document = Document.load(getClass().getResourceAsStream("slf4j-api-1.5.2.pom"));
        for (Element element : document.elements("/*[local-name() = 'project']/*[local-name() = 'parent' and *[local-name() = 'artifactId']]")) {
            assertEquals(element.getLocalName(), "parent");
            assertEquals(element.getNamespace(), "http://maven.apache.org/POM/4.0.0");
            assertEquals(element.getText("*[local-name() = 'artifactId']"), "slf4j-parent");
            assertEquals(element.getText("*[local-name() = 'groupId']"), "org.slf4j");
            assertEquals(element.getText("*[local-name() = 'version']"), "1.5.2");
         }
    }
}
