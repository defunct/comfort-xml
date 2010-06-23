package com.goodworkalan.comfort.xml;

/**
 * A text node in a Comfort XML document.
 *
 * @author Alan Gutierrez
 */
public class TextNode extends Node {
    /**
     * Construct a text node in owned by the given Comfort XML document that
     * wraps the given W3C DOM text node.
     * 
     * @param document
     *            The owner document.
     * @param text
     *            The W3C DOM text node.
     */
    TextNode(Document document, org.w3c.dom.Text text) {
        super(document, text);
    }
}
