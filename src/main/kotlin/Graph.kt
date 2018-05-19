class CityGraph(verticies: List<String>, edges: List<Pair<String, String>>) {
    val graph = hashMapOf<String,List<String>>()
    var verticies: List<String>
    var edges: List<Pair<String, String>>

    init {
        verticies.map { vert -> graph[vert] = adjacencyListBuilder(vert,edges) }
        this.verticies = verticies
        this.edges = edges
    }

    private fun adjacencyListBuilder(vert: String, edges: List<Pair<String, String>>): List<String> {
        val cityEdges = mutableListOf<String>()
        edges.map {
            edge ->
            if (edge.toList().contains(vert)){
                val newEdge = edge.toList().toMutableList()
                newEdge.remove(vert)
                cityEdges.add(newEdge.first())
            }
        }
        return cityEdges.toList()
    }

    /*
    Add a node/vertex from the graph to a queue of nodes to be “visited”.
    Visit the topmost node in the queue, and mark it as such.
    If that node has any neighbors, check to see if they have been “visited” or not.
    Add any neighboring nodes that still need to be “visited” to the queue.
    Remove the node we’ve visited from the queue.
     */

    fun bfsTraversal(
            start: String,
            stop: String = "",
            queue: MutableList<String> = mutableListOf(start),
            visited: MutableList<String> = mutableListOf()
    ): MutableList<String>{
        require(this.verticies.contains(start)) {"Starting city is not in this graph"}
        if (stop != "") require(this.verticies.contains(stop)) {"Ending city is not in this graph"}

        return when {
            visited.contains(stop) -> visited
            else -> {
                graph[queue[0]]!!.map { neighbor ->
                    if (!visited.contains(neighbor))
                        queue.add(neighbor)
                }
                visited.add(queue[0])
                queue.removeAt(0)
                if (queue.isEmpty())
                    return visited
                bfsTraversal(queue[0], stop, queue, visited)
            }
        }
    }

    /*
    set up a pointer ref to the node we're searching through
	- i.e. "Chicago" as a starting node

    set "Chicago's" parent pointer to NULL and mark the node as 'visited'

    the moment we check "Chicago's" neighbors, we can push it to the top of a 'stack data structure'

    recursion....
    check the first neighbor (arbitrary order), set it's parent pointer to "Chicago" and push onto the top of
    the stack

    once we've gone all the way down the path until we can't continue, pop off the last node from the stack
    and check hte previous node (now at the "top") if there are other paths we can check. If there are, pop
    that on top and continue; if not, go back another step.
     */

    fun dfsTraversal(
            start: String,
            stop: String = "",
            stack: List<String> = mutableListOf(start),
            visited: List<String> = mutableListOf(),
            paths: List<List<String>> = mutableListOf()
    ): List<String>{

        print(graph)

        require(this.verticies.contains(start)) {"Starting city is not in this graph"}
        if (stop != "") require(this.verticies.contains(stop)) {"Ending city is not in this graph"}

        val potentialCities = this.verticies.filter { city -> !graph[city]!!.isEmpty() }

        return when {
            visited.containsAll(potentialCities) && stop != "" -> paths.minBy { path -> path.size }.orEmpty()
            visited.containsAll(potentialCities) -> visited

            else -> {
                val currentCity = stack[0]
                val nextCityArray = graph[stack[0]]!!.filter { city -> !visited.contains(city) }
                val newPaths = paths.toMutableList()
                val newVisited = visited.toMutableList()
                val newStack = stack.toMutableList()
                if (!visited.contains(currentCity)) newVisited.add(currentCity)

                if (currentCity === stop) {
                     newPaths.add(stack.reversed())
                }

                if (nextCityArray.isEmpty()) {
                    newStack.removeAt(0)
                    dfsTraversal(newStack[0], stop, newStack.toList(), newVisited.toList(), newPaths.toList())
                }
                else {
                    newStack.add(0, nextCityArray.first())
                    dfsTraversal(newStack[0], stop, newStack.toList(), newVisited.toList(), newPaths.toList())
                }
            }
        }
    }
}
