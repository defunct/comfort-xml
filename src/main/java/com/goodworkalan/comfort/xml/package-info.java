/**
 * This is a comfort layer over the W3C DOM and sundry XML APIs that ship with
 * Java. It is an alternative to including a library like DOM4J or XOM when all
 * you want to do read that one XML file and write back to disk. Comfort XML
 * strives to be about 30K of code that you'll have to write anyway, and 20K of
 * sugar.
 * <p>
 * Comfort XML has the long, unwieldy code block that parses the file, creates
 * the W3C DOM document. Comfort XML has the long, unwieldy code block that
 * writes the file to disk pretty printed. No need to hit Google for the
 * incantations, they are in there.
 * <p>
 * Comfort XML is going to prize size over performance. It great when you are
 * going to slurp and pour XML documents like XHTML pages or configuration
 * files. If you need to process huge XML files, then you're going to want to
 * use a different library, but these are the age old choices of document models
 * versus streaming APIs. Comfort XML aims to be only slightly worse in
 * performance than W3C DOM, while being much, much smaller than replacing W3C
 * DOM with surrogate.
 * <p>
 * Comfort XML treats and XML document as a tree of nodes, not a living
 * interface to a web browser. The node lists are translated into Java
 * Collections API, quite literally copied. Node list iterators provide list
 * iterator semantics for <code>NoteList</code> with a working
 * <code>remove</code>, but you're going to have to be careful.
 * <p>
 * Comfort XML is not thread-safe.
 */
package com.goodworkalan.comfort.xml;