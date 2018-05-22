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

    fun bfsTraversal(
            start: String,
            stop: String = "",
            queue: List<String> = listOf(start),
            visited: List<String> = listOf()
    ): List<String>{
        require(this.verticies.contains(start)) {"Starting city is not in this graph"}
        if (stop != "") require(this.verticies.contains(stop)) {"Ending city is not in this graph"}
        val newQueue = queue.toMutableList()
        val newVisited = visited.toMutableList()

        return when {
            visited.contains(stop) -> visited
            else -> {
                graph[queue[0]]!!.map { neighbor ->
                    if (!visited.contains(neighbor))
                        newQueue.add(neighbor)
                }
                if (!visited.contains(queue[0])) newVisited.add(queue[0])
                newQueue.removeAt(0)

                if (newQueue.isEmpty())
                    return newVisited
                bfsTraversal(newQueue[0], stop, newQueue, newVisited)
            }
        }
    }

    fun dfsTraversal(
            start: String,
            stop: String = "",
            stack: List<String> = listOf(start),
            visited: List<String> = listOf(),
            paths: List<List<String>> = listOf()
    ): List<String>{
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
                if (currentCity === stop) newPaths.add(stack.reversed())
                when {
                    nextCityArray.isEmpty() -> {
                        newStack.removeAt(0)
                        dfsTraversal(newStack[0], stop, newStack.toList(), newVisited.toList(), newPaths.toList())
                    }
                    stop != "" && nextCityArray.contains(stop) -> {
                        newStack.add(0, stop)
                        dfsTraversal(newStack[0], stop, newStack.toList(), newVisited.toList(), newPaths.toList())
                    }
                    else -> {
                        newStack.add(0, nextCityArray.first())
                        dfsTraversal(newStack[0], stop, newStack.toList(), newVisited.toList(), newPaths.toList())
                    }
                }
            }
        }
    }
}
