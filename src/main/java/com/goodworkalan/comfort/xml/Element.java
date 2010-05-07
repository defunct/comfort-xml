package com.goodworkalan.comfort.xml;

import static com.goodworkalan.comfort.xml.ComfortXMLException.XPATH_EVALUATE;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Create a list of the child elements for the given W3C DOM node list whose
     * parent document is the given document.
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
    static final List<Element> elements(Document document, NodeList nodeList) {
        List<Element> elements = new ArrayList<Element>();
        for (int i = 0, stop = nodeList.getLength(); i < stop; i++) {
            org.w3c.dom.Node node = nodeList.item(i);
            if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                elements.add(new Element(document, (org.w3c.dom.Element) node));
            }
        }
        return elements;
    }
    
    public void remove() {
    }

    /**
     * Get the local name of the element.
     * 
     * @return The local name.
     */
    public String getLocalName() {
        if (element.getLocalName() == null) {
            return element.getTagName();
        }
        return element.getLocalName();
    }
    
    public String getNamespace() {
        return element.getNamespaceURI();
    }

    public String getText() {
        return element.getTextContent();
    }

    /**
     * Create a list of the child elements of this element. Nodes that are not
     * elements are ignored.
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
    public List<Element> elements() {
        return elements(document, element.getChildNodes());
    }

    public String getText(String expression) {
        XPathExpression expr = document.compile(expression);
        NodeList nodeList;
        try {
            nodeList = (NodeList) expr.evaluate(element, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            throw new ComfortXMLException(XPATH_EVALUATE, e, expression);
        }
        int stop = nodeList.getLength();
        if (stop == 0) {
            return null;
        }
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < stop; i++) {
            org.w3c.dom.Node node = nodeList.item(i);
            string.append(node.getTextContent());
        }
        return string.toString();
    }
}
