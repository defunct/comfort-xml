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

import com.goodworkalan.danger.Danger;

/**
 * Reads and writes XML documents to and from I/O streams.
 *
 * @author Alan Gutierrez
 */
public class Serializer {
    /**
     * Create a serializer.
     */
    public Serializer() {
    }

    /**
     * Load a Comfort XML document from the given input stream using the given
     * URI as the system identifier.
     * 
     * @param in
     *            The input stream.
     * @param uri
     *            The system identifier.
     * @return A Comfort XML document.
     * @exception ComfortXMLException
     *                For any parsing errors.
     */
    public Document load(InputStream in, String uri) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
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
            throw new Danger(e, Serializer.class, "load.sax.exception", in, e.getMessage());
        } catch (IOException e) {
            throw new Danger(e, Serializer.class, "load.io.exception", in, e.getMessage());
        }
        return new Document(doc);
    }
    
    /**
     * Load a Comfort XML document from the given file.
     * 
     * @param file
     *            The file to load.
     * @return A Comfort XML document.
     * @exception ComfortXMLException
     *                For any parsing errors.
     */
    public Document load(File file) {
        try {
            return load(new FileInputStream(file), file.toURI().toString());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Write the given Comfort XML document to the given file.
     * 
     * @param document
     *            The Comfort XML document.
     * @param file
     *            The file.
     */
    public void write(Document document, File file) {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();

            factory.setAttribute("indent-number", new Integer(2));

            Transformer xformer = factory.newTransformer();

            xformer.setOutputProperty(OutputKeys.INDENT, "yes");
            xformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
     
            Source source = new DOMSource(document.getDocument());
            Result result = new StreamResult(new FileWriter(file));
            xformer.transform(source, result);
        } catch (TransformerException e) {
            throw new Danger(e, Serializer.class, "write.transformer.exception", file, e.getMessageAndLocation());
        } catch (IOException e) {
            throw new Danger(e, Serializer.class, "write.io.exception", file, e.getMessage());
        }
    }
}
