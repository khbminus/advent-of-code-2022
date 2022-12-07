import java.io.File

class Day7 : Day {


    private val root = Node.Directory(null, "/")
    private var currentDirectory = root

    init {
        readInput()
    }

    override fun part1(): Any {
        calculateSizes(root)
        return sizes.filter { it <= THRESHOLD }.sum()
    }

    override fun part2(): Any {
        val currentSize = sizes.max()
        return sizes.filter { it >= NEED_UNUSED + currentSize - MAX_SIZE }.min()
    }

    private fun readInput() = File(INPUT_PATH).readLines().forEach {
        val args = it.split(" ")
        when {
            args[0] == "dir" -> currentDirectory.addDirectory(args[1])
            args[0] != "$" -> currentDirectory.addFile(args[1], args[0].toInt())
            args[1] == "ls" -> {}
            args[1] == "cd" && args[2] == "/" -> currentDirectory = root
            args[1] == "cd" && args[2] == ".." -> {
                require(currentDirectory.parent is Node.Directory)
                currentDirectory = (currentDirectory.parent ?: error("can't find parent")) as Node.Directory
            }

            args[1] == "cd" -> currentDirectory =
                currentDirectory.getChildren(args[2]) as? Node.Directory ?: error("children is file")

            else -> error("invalid command")
        }
    }

    private var sizes = mutableListOf<Int>()

    private fun printTree(indent: String, node: Node): Unit = when (node) {
        is Node.File -> println("$indent- ${node.name} (file, size=${node.size})")
        is Node.Directory -> {
            println("$indent- ${node.name} (dir)")
            node.children.forEach { printTree("$indent  ", it) }
        }
    }

    private fun calculateSizes(node: Node): Int = when (node) {
        is Node.File -> node.size
        is Node.Directory -> {
            node.size = node.children.sumOf { calculateSizes(it) }
            sizes.add(node.size)
            node.size
        }
    }

    companion object {
                private const val INPUT_PATH = "day7.in"
//        private val INPUT_PATH = "day7.in.exmp"
        private const val THRESHOLD = 100000
        private const val MAX_SIZE = 70000000
        private const val NEED_UNUSED = 30000000
    }

    private sealed class Node(val parent: Node?, val name: String, var size: Int) {
        class Directory(parent: Node?, name: String) : Node(parent, name, 0) {
            val children = mutableListOf<Node>()
            fun addDirectory(name: String) {
                if (children.all { it.name != name }) {
                    children.add(Directory(this, name))
                }
            }

            fun addFile(name: String, size: Int) {
                if (children.all { it.name != name }) {
                    children.add(File(this, name, size))
                }
            }

            fun getChildren(name: String): Node = children.find { it.name == name } ?: error("not found")
        }

        class File(parent: Node?, name: String, size: Int) : Node(parent, name, size)
    }
}