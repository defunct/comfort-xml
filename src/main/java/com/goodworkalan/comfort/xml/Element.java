package com.goodworkalan.comfort.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import com.goodworkalan.danger.Danger;

/**
 * An element node in an XML document.
 *
 * @author Alan Gutierrez
 */
public class Element extends Node {
    /**
     * Create an element that is a member of the given Comfort XML
     * <code>document</code> that wraps the given W3C DOM <code>element</code>.
     * 
     * @param document
     *            The Comfort XML document.
     * @param element
     *            The W3C DOM element.
     */
    Element(Document document, org.w3c.dom.Element element) {
        super(document, element);
    }
    
    /**
     * Get the underlying node cast to a W3C DOM element.
     * 
     * @return The W3C DOM element.
     */
    public org.w3c.dom.Element getElement() {
        return (org.w3c.dom.Element) node;
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

    /**
     * Get the local name of the element.
     * 
     * @return The local name.
     */
    public String getLocalName() {
        if (getElement().getLocalName() == null) {
            return getElement().getTagName();
        }
        return getElement().getLocalName();
    }

    /**
     * Get the namespace URI of the element or null if none is defined.
     * 
     * @return The namespace URI or null.
     */
    public String getNamespaceURI() {
        return getElement().getNamespaceURI();
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
        return elements(document, getElement().getChildNodes());
    }

    /**
     * Return a list of the attributes attached to this element.
     * 
     * @return The list of attributes.
     */
    public List<Attribute> getAttributes() {
        List<Attribute> attributes = new ArrayList<Attribute>();
        NamedNodeMap namedNodeMap = getElement().getAttributes();
        for (int i = 0, stop = namedNodeMap.getLength(); i < stop; i++) {
            attributes.add((Attribute) Node.wrap(document, namedNodeMap.item(i)));
        }
        return attributes;
    }

    /**
     * Get the attribute with the given local name or null no such attribute
     * exists.
     * 
     * @param localName
     *            The attribute local name.
     * @return The attribute with the given local name or null.
     */
    public Attribute getAttribute(String localName) {
        return (Attribute) Node.wrap(document, getElement().getAttributeNodeNS(null, localName));
    }

    /**
     * Set the attribute with the given local name to the given value creating
     * the attribute if necessary.
     * 
     * @param localName
     *            The attribute local name.
     * @param value
     *            The attribute value.
     */
    public void setAttribute(String localName, String value) {
        setAttribute(localName, null, value);
    }

    /**
     * Set the attribute with the given namespace URI and local name to the
     * given value creating the attribute if necessary.
     * 
     * @param localName
     *            The attribute local name.
     * @param namespaceURI
     *            The namespace URI.
     * @param value
     *            The attribute value.
     */
    public void setAttribute(String localName, String namespaceURI, String value) {
        Attr attr;
        if (namespaceURI == null) {
            attr = getElement().getAttributeNode(localName);
        } else {
            attr = getElement().getAttributeNodeNS(namespaceURI, localName);
        }
        Attribute attribute;
        if (attr == null) {
            attribute = document.createAttribute(localName, namespaceURI);
            getElement().setAttributeNodeNS(attribute.getAttr());
        } else {
            attribute = (Attribute) Node.wrap(document, attr);
        }
        attribute.setTextContent(value);
    }

    /**
     * Get the given XPath expression as a string, catenating the text values of
     * all the nodes returned by the XPath expression into a single string.
     * 
     * @param expression
     *            The XPath expression.
     * @return The string value of the XPath expression.
     */
    public String getText(String expression) {
        XPathExpression expr = document.compile(expression);
        NodeList nodeList;
        try {
            nodeList = (NodeList) expr.evaluate(getElement(), XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            throw new Danger(e, Document.class, "xpath.evaluate", expression);
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
