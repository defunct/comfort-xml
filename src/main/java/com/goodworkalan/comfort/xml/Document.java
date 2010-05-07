package com.goodworkalan.comfort.xml;

import static com.goodworkalan.comfort.xml.ComfortXMLException.XPATH_COMPILE;
import static com.goodworkalan.comfort.xml.ComfortXMLException.XPATH_EVALUATE;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.NodeList;

public class Document {
    final org.w3c.dom.Document document;
    
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
    private final Map<String, XPathExpression> cached = new HashMap<String, XPathExpression>();
    
    public XPathExpression compile(String expression) {
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
            nodeList = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            throw new ComfortXMLException(XPATH_EVALUATE, e, expression);
        }
        return Element.elements(this, nodeList);
    }
}
