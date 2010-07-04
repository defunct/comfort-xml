package com.goodworkalan.comfort.xml;

import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;

/**
 * A hash map backed implementation of <code>NamespaceContext</code>.
 *
 * @author Alan Gutierrez
 */
class HashMapNamespaceContext implements NamespaceContext {
    /** The map of namespace prefixes to namespace URIs. */
    private final Map<String, String> namespaces;

    /**
     * Create a <code>NamespaceContext</code> backed by the given hash map.
     * 
     * @param namespaces
     *            The hash map.
     */
    public HashMapNamespaceContext(Map<String, String> namespaces) {
        this.namespaces = namespaces;
    }

    /**
     * Get an iterator over the namespace URIs defined in this context.
     * 
     * @return An interator over the namespace URIs.
     */
    @SuppressWarnings("unchecked")
    public Iterator getPrefixes(String namespaceURI) {
        return namespaces.entrySet().iterator();
    }

    /**
     * Get the prefix for the given namespace URI.
     * 
     * @param namespaceURI
     *            The namespace URI.
     * @return The prefix for the namespace URI.
     */
    public String getPrefix(String namespaceURI) {
        for(Map.Entry<String, String> mapping : namespaces.entrySet()) {
            if (mapping.getValue().equals(namespaceURI)) {
                return mapping.getKey();
            }
        }
        return null;
    }

    /**
     * Get the namespace URI for the prefix.
     * 
     * @param prefix
     *            The prefix.
     * @return The namespace URI.
     */
    public String getNamespaceURI(String prefix) {
        return namespaces.get(prefix);
    }
}
