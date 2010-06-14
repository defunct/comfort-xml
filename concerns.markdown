## Thread-Safety

W3C DOM is not thread-safe and the default implementation does not address
thread-safety, however W3C DOM is the default DOM for Java. People use W3C DOM
in multi-threaded applications based on assumptions made on the thread-safety of
the W3C DOM. I've written about this extensively in the project documentation
for Stencil. 
