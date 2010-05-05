package com.goodworkalan.comfort.xml;

import static com.goodworkalan.comfort.xml.ComfortXMLException.XPATH_COMPILE;
import static com.goodworkalan.comfort.xml.ComfortXMLException.XPATH_EVALUATE;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.NodeList;

public class Element {
    private org.w3c.dom.Element element;
    
    private final Document document;
    
    Element(Document document, org.w3c.dom.Element element) {
        this.document = document;
        this.element = element;
    }
    
    public String getLocalName() {
        return element.getLocalName();
    }
    
    public String getNamespace() {
        return element.getNamespaceURI();
    }

    public String getText(String expression) {
        XPathExpression expr;
        try {
            expr = document.xpath.compile(expression);
        } catch (XPathExpressionException e) {
            throw new ComfortXMLException(XPATH_COMPILE, e, expression);
        }
        NodeList nodeList;
        try {
            nodeList = (NodeList) expr.evaluate(element, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            throw new ComfortXMLException(XPATH_EVALUATE, e, expression);
        }
        StringBuilder string = new StringBuilder();
        for (int i = 0, stop = nodeList.getLength(); i < stop; i++) {
            org.w3c.dom.Node node = nodeList.item(i);
            string.append(node.getTextContent());
        }
        return string.toString();
    }
}
