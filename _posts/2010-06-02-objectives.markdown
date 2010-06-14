---
layout: default
title: Objectives
---

Comfort XML seeks to paper over the dysfunciton of the W3C DOM with assumptions
and priorities to create an XML DOM API for Java that feels like Java but has a
trivial impact on the footprint of your application.

Some of Comfort XML will be the boilerplate you'd have to add to your program
anyway, the maze of try catch blocks needed to read a document, the sundry
requisitions to factories and with the approriate settings and functional XSLT
pipeline pretty print a document, or the muddle of code to execute an XPath
statement.

That last bit is what makes Comfort XML worthwhile. Comfort XML exposes the
underlying XPath engine in a way that says that it is okay to use it, returing
the results in the form of lists or a nullable single return value, so you can
navigate the results or simply pluck out the elements of interest.

Unlike W3C DOM, Comfort XML exposes a consistant interface regardless of the
whether the parser that created the document was namespace aware.

Comfort XML finds value in namespaces and takes them seriously.

Qualified names are modeled using string lists, where an unqualified name is a
single element string list and qualified name is two element string list, with
the second element the namspace URI.

Namespace prefixes are never specified in elements and only assigned by
referencing the delcared namespaces in the XML document. Node surgery sorts this
out in the underlying W3C DOM and it is the one place were some expense is added
to the W3C DOM. Ideally, the prefix would always be a lookup, but the underlying
DOM requires an assignment.

We provide a comfort layer over W3C DOM whose primary goal is programmer
efficency and whose secondary goal is size. Performance is a non-goal for this
library, however that does not mean that we'll embrace brute force, simply that
we're not going to worry about micro-optimzation.

This library will be ideal for the Java programming that wants to edit an XML
document. Any reasonably sized document, like the sorts that you would expect to
open in a web browser. Use cases that strain the limits of memory using W3C DOM
are not interesting to Comfort XML. You are doing uncomfortable things, so
you'll need to take a deliberate approach.

The wrapped objects are wrap as you go, except where lists are returned, in
which case all the objects in the list are wrapped.

No support for live node lists. Comfort XML will slurp them into array lists of
wrapped nodes and present that to the user. There is little faith here in the
efficiency of W3C DOM, so going to great lengths to preserve the features that
are unique to it, like live node lists. No investigations will be done to
compare the efficency of a proxy to a live node list versus a copy. We'll assume
that a live node list is fraught with overheads, that a copy is a copy, and the
nodes have to be wrapped anyway.

Attributes are accessible through the element interface and addressable by name 
or by qualified name. They can be converted into `LinkedHashMap` for iteration
where the keys are qualified names in the form of string lists.
