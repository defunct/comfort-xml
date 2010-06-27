package com.goodworkalan.comfort.xml;

import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;

// TODO Document.
class HashMapNamespaceContext implements NamespaceContext {
    /** The map of namespace prefixes to namespace URIs. */
    private final Map<String, String> namespaces;

    // TODO Document.
    public HashMapNamespaceContext(Map<String, String> namespaces) {
        this.namespaces = namespaces;
    }
    
    // TODO Document.
    @SuppressWarnings("unchecked")
    public Iterator getPrefixes(String namespaceURI) {
        return namespaces.entrySet().iterator();
    }
    
    // TODO Document.
    public String getPrefix(String namespaceURI) {
        for(Map.Entry<String, String> mapping : namespaces.entrySet()) {
            if (mapping.getValue().equals(namespaceURI)) {
                return mapping.getKey();
            }
        }
        return null;
    }
    
    // TODO Document.
    public String getNamespaceURI(String prefix) {
        return namespaces.get(prefix);
    }
}
