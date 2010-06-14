## Developer Reads XML Document

Common use case is the reading of an XML document from file or form standard
input. This will require create a document builder factory, then a document
builder, then a document and there are settings to flip along the way. The
document will ignore namespaces unless you mark it as namespace aware.

We'll default to namespace aware documents, with no option to make it otherwise.
