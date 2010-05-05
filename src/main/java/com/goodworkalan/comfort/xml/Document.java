package com.goodworkalan.comfort.xml;

import static com.goodworkalan.comfort.xml.ComfortXMLException.XPATH_COMPILE;
import static com.goodworkalan.comfort.xml.ComfortXMLException.XPATH_EVALUATE;

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
}
