package com.goodworkalan.comfort.xml;

import static com.goodworkalan.comfort.xml.ComfortXMLException.XPATH_COMPILE;
import static com.goodworkalan.comfort.xml.ComfortXMLException.XPATH_EVALUATE;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;
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
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Document {
    private final org.w3c.dom.Document document;
    
    final Map<String, String> namespaces = new HashMap<String, String>();

    final XPath xpath = XPathFactory.newInstance().newXPath();
    
    public Document(org.w3c.dom.Document document) {
        this.document = document;
        this.xpath.setNamespaceContext(new NamespaceContext() {
            @SuppressWarnings("unchecked")
            public Iterator getPrefixes(String namespaceURI) {
                return namespaces.entrySet().iterator();
            }
            
            public String getPrefix(String namespaceURI) {
                for(Map.Entry<String, String> mapping : namespaces.entrySet()) {
                    if (mapping.getValue().equals(namespaceURI)) {
                        return mapping.getKey();
                    }
                }
                return null;
            }
            
            public String getNamespaceURI(String prefix) {
                return namespaces.get(prefix);
            }
        });
    }
    
    public void setNamespace(String name, String url) {
        namespaces.put(name, url);
    }

    /**
     * Evaluate an XPath expression and return a list of elements that match the
     * XPath expression. If the XPath expression matches nodes that are not
     * elements, those nodes are discarded and are not present in the resulting
     * element list.
     * <p>
     * Note that the resulting list is not a live list like in W3C DOM and is
     * not connected to the underlying document. Unlike a W3C DOM
     * {@link NodeList}, changes to document are not reflected in the list.
     * Likewise, removing an element from the list does not remove it from the
     * document. You can use the {@link Element#remove() remove} method of
     * <code>Element</code> to remove the elements from the document.
     * 
     * @param expression
     *            The XPath expression.
     * @return The list of elements matched by the XPath expression.
     */
    public List<Element> elements(String expression) {
        List<Element> elements = new ArrayList<Element>();
        XPathExpression expr;
        try {
            expr = xpath.compile(expression);
        } catch (XPathExpressionException e) {
            throw new ComfortXMLException(XPATH_COMPILE, e, expression);
        }
        NodeList nodeList;
        try {
            nodeList = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            throw new ComfortXMLException(XPATH_EVALUATE, e, expression);
        }
        for (int i = 0; i < nodeList.getLength(); i++) {
            org.w3c.dom.Node node = nodeList.item(i);
            if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                elements.add(new Element(this, (org.w3c.dom.Element) node));
            }
        }
        return elements;
    }
    
    public final static Document load(InputStream in) {
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
            doc = db.parse(in);
        } catch (SAXException e) {
            throw new ComfortXMLException(0, e, in, e.getMessage());
        } catch (IOException e) {
            throw new ComfortXMLException(0, e, in, e.getMessage());
        }
        return new Document(doc);
    }
    
    public void write(File file) {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();

            factory.setAttribute("indent-number", new Integer(2));

            Transformer xformer = factory.newTransformer();

            xformer.setOutputProperty(OutputKeys.INDENT, "yes");
            xformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
     
            Source source = new DOMSource(document);
            Result result = new StreamResult(new FileWriter(file));
            xformer.transform(source, result);
        } catch (TransformerException e) {
            throw new ComfortXMLException(0, e, file, e.getMessageAndLocation());
        } catch (IOException e) {
            throw new ComfortXMLException(0, e, file, e.getMessage());
        }
    }
}
