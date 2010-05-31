package com.goodworkalan.comfort.xml;

import java.util.Iterator;

import org.w3c.dom.NodeList;

public class NodeIterable<T> implements Iterable<T> {
    final Class<T> nodeClass;
    
    final NodeList nodeList;
    
    public NodeIterable(Class<T> nodeClass, NodeList nodeList) {
        this.nodeClass = nodeClass;
        this.nodeList = nodeList;
    }
    
    public Iterator<T> iterator() {
        return null;
    }
}