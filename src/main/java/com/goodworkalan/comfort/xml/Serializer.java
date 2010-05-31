package com.goodworkalan.comfort.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;

/**
 * Reads and writes XML documents to and from I/O streams.
 *
 * @author Alan Gutierrez
 */
public class Serializer {
    /** Whether or not to enable namespaces in the XML parser. */
    private boolean namespaceAware = true;

    /**
     * Create a serializer.
     */
    public Serializer() {
    }
    
    public void setNamespaceAware(boolean namespaceAware) {
        this.namespaceAware = namespaceAware;
    }

    public Document load(InputStream in, String uri) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(namespaceAware);
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // Yeah, well, we never change the default configuration, so WTF?
            throw new RuntimeException(e);
        }
        org.w3c.dom.Document doc;
        try {
            doc = db.parse(in, uri);
        } catch (SAXException e) {
            throw new ComfortXMLException(0, e, in, e.getMessage());
        } catch (IOException e) {
            throw new ComfortXMLException(0, e, in, e.getMessage());
        }
        return new Document(doc);
    }
    
    public Document load(File file) {
        try {
            return load(new FileInputStream(file), null);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void write(Document document, File file) {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();

            factory.setAttribute("indent-number", new Integer(2));

            Transformer xformer = factory.newTransformer();

            xformer.setOutputProperty(OutputKeys.INDENT, "yes");
            xformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
     
            Source source = new DOMSource(document.document);
            Result result = new StreamResult(new FileWriter(file));
            xformer.transform(source, result);
        } catch (TransformerException e) {
            throw new ComfortXMLException(0, e, file, e.getMessageAndLocation());
        } catch (IOException e) {
            throw new ComfortXMLException(0, e, file, e.getMessage());
        }
    }
}
