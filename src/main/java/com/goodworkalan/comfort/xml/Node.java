package com.goodworkalan.comfort.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.w3c.dom.Node.*;

import org.w3c.dom.NodeList;

public class Node {
    /** The owner Comfort XML document. */
    protected final Document document;
    
    /** The underlying W3C DOM node. */
    protected final org.w3c.dom.Node node;

    /**
     * Create a node with the given owner <code>document</code> and the given
     * underlying W3C DOM <code>node</code>.
     * 
     * @param document
     *            The owner Comfort XML document.
     * @param node
     *            The underlying W3C DOM node.
     */
    public Node(Document document, org.w3c.dom.Node node) {
        if (document == null && !(this instanceof Document)) {
            throw new NullPointerException();
        }
        this.document = document == null ? (Document) this : document;
        this.node = node;
    }
    
    /**
     * Get the owner Comfort XML document.
     * 
     * @return The owner Comfort XML document.
     */
    public Document getOwnerDocument() {
        return document;
    }
    
    /**
     * Remove the node from the document.
     */
    public void remove() {
        node.getParentNode().removeChild(node);
    }

    /**
     * Return the next sibling wrapped in a Comfort XML node.
     * 
     * @return The next sibling.
     */
    public Node getNextSibling() {
        return wrap(document, node.getNextSibling());
    }

    /**
     * Return the next sibling that is an element or null if none exists.
     * 
     * @return The next element.
     */
    public Element getNextElement() {
        org.w3c.dom.Node next = node.getNextSibling();
        while (next != null && next.getNodeType() != org.w3c.dom.Node.ELEMENT_NODE) {
            next = next.getNextSibling();
        }
        return (Element) wrap(document, next);
    }

    /**
     * Return the previous sibling wrapped in a Comfort XML node.
     * 
     * @return The previous sibling.
     */
    public Node getPreviousSibling() {
        return wrap(document, node.getPreviousSibling());
    }

    /**
     * Return the first previous sibling that is an element or null if none
     * exists.
     * 
     * @return The previous sibling.
     */
    public Element getPreviousElement() {
        org.w3c.dom.Node previous = node.getNextSibling();
        while (previous != null && previous.getNodeType() != org.w3c.dom.Node.ELEMENT_NODE) {
            previous = previous.getPreviousSibling();
        }
        return (Element) wrap(document, previous);
    }

    /**
     * Get the first child node wrapped in a Comfort XML node or null if there
     * are no children.
     * 
     * @return The first child.
     */
    public Node getFirstChild() {
        return wrap(document, node.getFirstChild());
    }

    /**
     * Get the first child node that is an element wrapped in a Comfort XML node
     * or null if there are no element children.
     * 
     * @return The first element.
     */
    public Element getFirstElement() {
        org.w3c.dom.Node next = node.getFirstChild();
        while (next != null && next.getNodeType() != org.w3c.dom.Node.ELEMENT_NODE) {
            next = next.getNextSibling();
        }
        return (Element) wrap(document, next);
    }
    
    /**
     * Get the last child node wrapped in a Comfort XML node or null if there
     * are no children.
     * 
     * @return The last child.
     */
    public Node getLastChild() {
        return wrap(document, node.getLastChild());
    }

    /**
     * Get the last child node that is an element wrapped in a Comfort XML node
     * or null if there are no element children.
     * 
     * @return The last element.
     */
    public Element getLastElement() {
        org.w3c.dom.Node previous = node.getLastChild();
        while (previous != null && previous.getNodeType() != org.w3c.dom.Node.ELEMENT_NODE) {
            previous = previous.getPreviousSibling();
        }
        return (Element) wrap(document, previous);
    }
    
    public Node removeChild(Node child) {
        return wrap(document, node.removeChild(child.node));
    }

    /**
     * Get the text content of the node.
     * 
     * @return The text content.
     */
    public String getTextContent() { 
        return node.getTextContent();
    }

    /**
     * Set the text content to the given <code>text</code>. This method will
     * remove all child nodes and add a single text node containing the given
     * string.
     * 
     * @param text
     *            The text content.
     */
    public void setTextContent(String text) {
        while (node.hasChildNodes()) {
            node.removeChild(node.getFirstChild());
        }
        node.appendChild(document.getDocument().createTextNode(text));
    }

    private final static String XMLNS = "xmlns";
    
    private final static String XMLNS_URI = "http://www.w3.org/2000/xmlns/";
    
    /**
     * Create a map of the name spaces defined for the given W3C DOM element.
     * 
     * @param element The W3C DOM element.
     * @param namespaces The map of prefixes.
     * @return
     */
    private static Map<String, String> namespaces(Element element, Map<String, String> namespaces) {
        for (;;) {
            for (Attribute attribute : element.getAttributes()) {
                String prefix = null;
                if (attribute.getNamespaceURI() == null && XMLNS.equals(attribute.getLocalName())) {
                    prefix = "";
                } else if (XMLNS_URI.equals(attribute.getNamespaceURI())) {
                    prefix = attribute.getLocalName();
                }
                if (prefix != null) {
                    String namespaceURI = attribute.getTextContent();
                    if (!namespaceURI.equals("") && !namespaces.containsKey(prefix)) {
                        namespaces.put(prefix, namespaceURI);
                    }
                }
            }
            Node parent = element.getParentNode();
            if (parent == null || parent.getNode().getNodeType() != element.getNode().getNodeType()) {
                return namespaces;
            }
            element = (Element) parent;
        }
    }
    
    private static Map<String, String> invert(Map<String, String> map) {
        Map<String, String> inverted = new HashMap<String, String>();
        for (Map.Entry<String, String> mapping : map.entrySet()) {
            inverted.put(mapping.getValue(), mapping.getKey());
        }
        return inverted;
    }
    
    private static void prefix(Map<String, String> namespaces, Map<String, String> prefixes, Element element) {
        boolean dirty = false;
        for (Attribute attribute : element.getAttributes()) {
            String prefix = null;
            if (attribute.getNamespaceURI() == null && XMLNS.equals(attribute.getLocalName())) {
                prefix = "";
            } else if (XMLNS_URI.equals(attribute.getNamespaceURI())) {
                prefix = attribute.getLocalName();
            }
            if (prefix != null) {
                dirty = true;
                String namespaceURI = attribute.getTextContent();
                if (namespaceURI.equals("")) {
                    namespaces.remove(prefix);
                } else {
                    namespaces.put(prefix, namespaceURI);
                }
            }
        }
        if (dirty) {
            prefixes = invert(namespaces);
        }
        for (Attribute attribute : element.getAttributes()) {
            String namespaceURI = attribute.getNamespaceURI();
            if (namespaceURI != null && !XMLNS_URI.equals(namespaceURI)) {
                attribute.getAttr().setPrefix(prefixes.get(namespaceURI));
            }
        }
        String namespaceURI = element.getNamespaceURI();
        if (namespaceURI != null) {
            element.getElement().setPrefix(prefixes.get(namespaceURI));
        }
    }
    
    private static Node prefix(Node node) {
        if (node.getNode().getNodeType() == ELEMENT_NODE) {
            Map<String, String> namespaces = namespaces((Element) node, new HashMap<String, String>());
            prefix(namespaces, invert(namespaces), (Element) node);
        }
        return node;
    }
    
    public Node appendChild(Node child) {
        return prefix(wrap(document, node.appendChild(child.node)));
    }
    
    public Node insertBefore(Node newChild, Node reference) {
        return prefix(wrap(document, node.insertBefore(newChild.node, reference.node)));
    }

    
    public org.w3c.dom.Node getNode() {
        return node;
    }

    /**
     * Return the parent node or null.
     * <p>
     * XXX Does not seem to be a need for a parent element, a) going backwards
     * is usually to navigate to the next element from the backwards step, b) if
     * you know that the parent is an element and not a document, then just
     * cast.
     * 
     * @return The parent node.
     */
    public Node getParentNode() {
        return wrap(document, node.getParentNode());
    }

    /**
     * Invoked in fall-through branches of the switch statement to get a green
     * line from Cobertura.
     */
    private static void forTheSakeOfCoberturaCoverage() {
    }

    public static Node wrap(Document document, org.w3c.dom.Node node) {
        if (node == null) {
            return null;
        }
        return wrap(document, node, node.getNodeType());
    }

    public static Node wrap(Document document, org.w3c.dom.Node node, short nodeType) {
        switch (node.getNodeType()) {
        case ELEMENT_NODE:
            return new Element(document, (org.w3c.dom.Element) node);
        case CDATA_SECTION_NODE:
            forTheSakeOfCoberturaCoverage();
        case TEXT_NODE:
            return new TextNode(document, (org.w3c.dom.Text) node);
        case ATTRIBUTE_NODE:
            return new Attribute(document, (org.w3c.dom.Attr) node);
        case DOCUMENT_NODE:
            if (document.getNode().isSameNode(node)) {
                return document;
            }
            return new Document((org.w3c.dom.Document) node);
        }
        throw new IllegalArgumentException();
    }
    
    public static List<Node> nodes(Document document, NodeList nodeList) {
        List<Node> list = new ArrayList<Node>();
        for (int i = 0, stop = nodeList.getLength(); i < stop; i++) {
            list.add(wrap(document, nodeList.item(i)));
        }
        return list;
    }
}
