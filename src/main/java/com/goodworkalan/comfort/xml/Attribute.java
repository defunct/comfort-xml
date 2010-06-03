package com.goodworkalan.comfort.xml;

/**
 * Java comfort wrapper class around a W3C DOM <code>Attr</code>.
 * 
 * @author Alan Gutierrez
 */
public class Attribute extends Node {
    /**
     * Create an attribute with the given Comfort XML owner
     * <code>document</code> and the given W3C DOM <code>attribute</code>.
     * 
     * @param document The Comfort XML owner document.
     * @param attribute The W3C DOM attribute.
     */
    public Attribute(Document document, org.w3c.dom.Attr attribute) {
        super(document, attribute);
    }

    /**
     * Cast the underlying node to a W3C DOM <code>Attr</code>.
     */
    public org.w3c.dom.Attr getAttr() {
        return (org.w3c.dom.Attr) node;
    }

    /**
     * Get the local name of the attribute.
     * 
     * @return The local name.
     */
    public String getLocalName() {
        String localName = getAttr().getLocalName();
        if (localName == null) {
            localName = getAttr().getNodeName();
        }
        return localName;
    }

    /**
     * Get the name space URI of the attribute or null if the attribute name is
     * unqualified.
     * 
     * @return The name space URI.
     */
    public String getNamespaceURI() {
        String namespaceURI = getAttr().getNamespaceURI();
        if ("".equals(namespaceURI)) {
            namespaceURI = null;
        }
        return namespaceURI;
    }
}
