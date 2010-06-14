## Thread-Safety

W3C DOM is not thread-safe and the default implementation does not address
thread-safety, however W3C DOM is the default DOM for Java. People use W3C DOM
in multi-threaded applications based on assumptions made on the thread-safety of
the W3C DOM. I've written about this extensively in the project documentation
for Stencil. 

## Easy XML Munging

Many times, we're not even very interested in the documents themselves, but
rather the contents of them, so if you're reading a configuration file, you
might simply want to run it as if it were a single command, read this XPath from
this XML file. You'll forgo creating a `Document` instance yourself. Like the
`slurp` method in Comfort I/O, you're not interested in the file, but in the
lines therein.

This occurs with reading POM files in Cups Remix. It could be so much more terse
to say, here is a root directory, now let me extract content form XML by
specifying a relative file path, and an XPath, or better still, a file glob an
an XPath.

The parsed documents are cached in a hash map, or better still a weak hash map,
and the results are iterable or single values.

In which case, this might be an extension to Comfort XML, for when you really
just want to get the information out of an XML corpus, like a Maven repository
full of POMs, you simply want 
