package com.goodworkalan.comfort.xml;

import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;

class HashMapNamespaceContext implements NamespaceContext {
    /** The map of namespace prefixes to namespace URIs. */
    private final Map<String, String> namespaces;

    public HashMapNamespaceContext(Map<String, String> namespaces) {
        this.namespaces = namespaces;
    }
    
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
}
