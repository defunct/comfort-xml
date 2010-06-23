package com.goodworkalan.comfort.xml;

import static com.goodworkalan.comfort.xml.ComfortXMLException.XPATH_COMPILE;
import static com.goodworkalan.comfort.xml.ComfortXMLException.XPATH_EVALUATE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.NodeList;

/**
 * An XML document.
 * 
 * @author Alan Gutierrez
 */
public class Document extends Node {
    /** The cache of compiled XPath expressions. */
    private final Map<String, XPathExpression> cached = new HashMap<String, XPathExpression>();

    /** The map of namespace prefixes to namespace URIs. */
    private final Map<String, String> namespaces = new HashMap<String, String>();

    /** The XPath expression compiler. */
    final XPath xpath = XPathFactory.newInstance().newXPath();

    /**
     * Create a document that is a wrapper around the given W3C DOM document.
     * 
     * @param document
     *            The document to wrap.
     */
    public Document(org.w3c.dom.Document document) {
        super(null, document);
        this.xpath.setNamespaceContext(new HashMapNamespaceContext(namespaces));
    }
    
    public org.w3c.dom.Document getDocument() {
        return (org.w3c.dom.Document) node;
    }

    /**
     * Set the namespace prefix for the given url to the given prefix.
     * 
     * @param prefix
     *            The namespace prefix.
     * @param url
     *            The namespace url.
     */
    public void setNamespacePrefix(String prefix, String url) {
        namespaces.put(prefix, url);
    }

    /**
     * Compile the given XPath expression. If the expression already exists in
     * the cache, the cached expression is returned.
     * 
     * @param expression
     *            The XPath expression.
     * @return A compiled XPath expression.
     */
    XPathExpression compile(String expression) {
        XPathExpression expr = cached.get(expression);
        if (expr != null) {
            return expr;
        }
        try {
            expr = xpath.compile(expression);
        } catch (XPathExpressionException e) {
            throw new ComfortXMLException(XPATH_COMPILE, e, expression);
        }
        cached.put(expression, expr);
        return expr;
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
        XPathExpression expr = compile(expression);
        NodeList nodeList;
        try {
            nodeList = (NodeList) expr.evaluate(getDocument(), XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            throw new ComfortXMLException(XPATH_EVALUATE, e, expression);
        }
        return Element.elements(document, nodeList);
    }

    /**
     * Evaluate an XPath expression and return a list of nodes that match the
     * XPath expression.
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
    public List<Node> nodes(String expression) {
        XPathExpression expr = compile(expression);
        NodeList nodeList;
        try {
            nodeList = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            throw new ComfortXMLException(XPATH_EVALUATE, e, expression);
        }
        return Node.nodes(this, nodeList);
    }

    /**
     * Create an element that belongs to the W3C DOM document wrapped by this
     * Comfort XML document that has the given local name and namespace URI.
     * 
     * @param localName
     *            The local name.
     * @param namespaceURI
     *            The namespace URI.
     * @return A new element.
     */
    public Element createElement(String localName, String namespaceURI) {
        return (Element) wrap(document, getDocument().createElementNS(namespaceURI, localName));
    }

    /**
     * Create an attribute that belongs to the W3C DOM document wrapped by this
     * Comfort XML document that has the given namespace URI and local name.
     * 
     * @param localName
     *            The local name.
     * @param namespaceURI
     *            The namespace URI.
     * @return A new attribute.
     */
    public Attribute createAttribute(String localName, String namespaceURI) {
        return (Attribute) wrap(document, getDocument().createAttributeNS(namespaceURI, localName));
    }
}
