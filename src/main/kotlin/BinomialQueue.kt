package juko

class BinomialQueue<T : Comparable<T>>() : MutableCollection<T> {
    private var head: BinomialNode<T>? = null
    private var _size: Int = 0
    override val size: Int
        get() = _size

    private class BinomialNode<T : Comparable<T>>(
        val key: T,
        var degree: Int = 0,
        var parent: BinomialNode<T>? = null,
        var child: BinomialNode<T>? = null,
        var sibling: BinomialNode<T>? = null,
    )

    inner class Iterator : MutableIterator<T> {
        private val stack = ArrayDeque<BinomialNode<T>>()
        private var lastReturned: BinomialNode<T>? = null

        init {
            head?.let { stack.addLast(it) } // начинаем с головы списка корней
        }

        override fun hasNext(): Boolean = stack.isNotEmpty()

        override fun next(): T {
            if (!hasNext()) throw NoSuchElementException()

            val node = stack.removeLast()
            lastReturned = node

            // добавляем siblings и потомков в стек
            // сначала sibling, потом child, чтобы child обрабатывался первым
            node.sibling?.let { stack.addLast(it) }
            node.child?.let { stack.addLast(it) }

            return node.key
        }

        override fun remove() {
            // реализация remove для биномиальной кучи нетривиальна
            // и ломает структуру дерева, поэтому лучше не поддерживать
            throw UnsupportedOperationException("Remove not supported in BinomialQueue iterator")
        }
    }

    // todo: мб переписать под хвостовую рекурсию?
    fun min(): T? {
        if (head == null) return null
        var min = head!!.key
        var cur = head!!.sibling
        while (cur != null) {
            if (cur.key < min) min = cur.key
            cur = cur.sibling
        }
        return min
    }

    fun extractMin(): T? {
        if (head == null) return null

        var prevMin: BinomialNode<T>? = null
        var minNode = head
        var prev: BinomialNode<T>? = null
        var curr = head

        while (curr != null) {
            if (curr.key < minNode!!.key) {
                minNode = curr
                prevMin = prev
            }
            prev = curr
            curr = curr.sibling
        }
        if (prevMin != null) prevMin.sibling = minNode!!.sibling else head = minNode!!.sibling

        var child = minNode.child
        val newHeap = BinomialQueue<T>()
        var prevChild: BinomialNode<T>? = null
        var nextChild: BinomialNode<T>?
        while (child != null) {
            nextChild = child.sibling
            child.sibling = prevChild
            child.parent = null
            prevChild = child
            child = nextChild
        }
        newHeap.head = prevChild

        this.union(newHeap)
        _size--
        return minNode.key
    }

    fun union(other: BinomialQueue<T>) {
        if (other.head == null) return
        if (this.head == null) {
            this.head = other.head
            return
        }
        head = mergeRootLists(other.head)
        _size += other._size

        var prev: BinomialNode<T>? = null
        var curr = head!!
        var next = curr.sibling

        while (next != null) {
            if (curr.degree != next.degree ||
                (next.sibling != null && next.sibling!!.degree == curr.degree)
            ) {
                prev = curr
                curr = next
            } else {
                if (curr.key <= next.key) {
                    curr.sibling = next.sibling
                    linkTrees(next, curr)
                } else {
                    if (prev != null) prev.sibling = next else head = next
                    linkTrees(curr, next)
                    curr = next
                }
            }
            next = curr.sibling
        }
    }

    private fun mergeRootLists(h2: BinomialNode<T>?): BinomialNode<T>? {
        val h1 = this.head ?: return h2
        if (h2 == null) return h1

        var p1: BinomialNode<T>? = h1
        var p2: BinomialNode<T>? = h2

        val head: BinomialNode<T>
        if (p1!!.degree <= p2!!.degree) {
            head = p1
            p1 = p1.sibling
        } else {
            head = p2
            p2 = p2.sibling
        }
        var tail = head

        while (p1 != null && p2 != null) {
            if (p1.degree <= p2.degree) {
                tail.sibling = p1
                tail = p1
                p1 = p1.sibling
            } else {
                tail.sibling = p2
                tail = p2
                p2 = p2.sibling
            }
        }

        tail.sibling = p1 ?: p2

        return head
    }

    private fun linkTrees(
        y: BinomialNode<T>,
        z: BinomialNode<T>,
    ) {
        y.parent = z
        y.sibling = z.child
        z.child = y
        z.degree++
    }

    override fun iterator(): MutableIterator<T> = Iterator()

    override fun add(element: T): Boolean {
        val node = BinomialNode(element)
        val newHeap = BinomialQueue<T>()
        newHeap.head = node
        newHeap._size = 1
        this.union(newHeap)
        _size++
        return true
    }

    override fun remove(element: T): Boolean {
        TODO("Not yet implemented")
    }

    override fun addAll(elements: Collection<T>): Boolean {
        try {
            for (element in elements) add(element)
        } catch (_: Exception) {
            return false
        }
        return true
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        TODO("Not yet implemented")
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        TODO("Not yet implemented")
    }

    override fun clear() {
        TODO()
    }

    override fun isEmpty() = head == null

    override fun contains(element: T): Boolean {
        TODO("Not yet implemented")
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        TODO("Not yet implemented")
    }

}