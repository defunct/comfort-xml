---
layout: default
title: Objectives
---

One can imagine a mapping from `Iterator` to `NodeList`, but the underlying
`NodeList` hides an enormous Rube Goldberg machine, a list over elements that
will update if the tree updates. That exists, without any way of detecting the
change, so we are unable to throw a `ConcurrentModificationException` if the
elements change. We open ourselves up to a world of what ifs, and for what?

We are not going to expose the live node list nature through `Iterator`, we are
not going to implement `remove` by virtue of removing a node of the list from
its parent. Although it appears as though that would be an obvious
implementation of `remove` using `NodeList` and it would work perfectly for
lists of child nodes, it would produce strange behavior for
`getElementsByTagName`. If the node removed from a list returned by
`getElementsByTagName` contained children that shared the tag name, calling
`remove` would remove more than one element from iteration.

`Iterator` over `NodeList` would work for some cases, but have unmappable
behavior for others. For those cases where it would work, what are the
advantages? Comfort XML is meant to work only with in memory documents. If the
document can fit in memory, a list of nodes in the document can also fit in
memory. Is there some huge savings in having `NodeList` live? Probably not. It
is such a Rube Goldberg machine, there is probably a lot of resource given to
making the live list magic happen.

An array copy of the list is simple. We don't have to worry about live lists.
The ablility to `remove` is still there, of course, using node surgery, which
would not have side effects that feedback onto the list we're iterating.

There could be an even more clever mapping of `NodeList` to `ListIterator`. It
would seem to some to be such a nice talking point about the library, to iterate
a live list of child nodes, forward and backwards, and to add and remove them
form their parent, all through a Java utility API. It would make for a powerful
2 minute intro, but the caveats shouted from every other rooftop in the
documentation would erode confidence and give this little library the taint of
the too clever.

Indeed, the only place were the side effects could be contained, where we could
be certain that removing a particular node would not remove the root of a tree
containing other nodes also in the list, would be a list of children. If it is
too memory expensive to create a copy of those children, then one could as
easily iterate over the list using the next sibling iterators. You won't be able
to use the iterator to mutate in a foreach style for loop anyway, you'll have to
create an instance of `ListIterator`, so your while loop will be as verbose as a
loop that used a current node as an iterator.
