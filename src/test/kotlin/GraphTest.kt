import org.junit.Rule
import org.junit.Test
import java.lang.IllegalArgumentException
import kotlin.test.assertEquals
import org.junit.rules.ExpectedException

class GraphTest {
    @Rule @JvmField
    var expectedException: ExpectedException = ExpectedException.none()
    private val verts = listOf("Seattle", "Denver", "Chicago", "San Diego", "Detroit", "Minneapolis", "Boston", "Atlanta")
    private val edges = listOf(
        Pair("Chicago", "Denver"),
        Pair("Seattle", "Denver"),
        Pair("Chicago", "Denver"),
        Pair("Chicago", "Detroit"),
        Pair("Chicago", "Atlanta"),
        Pair("Detroit", "Minneapolis"),
        Pair("Detroit", "Boston"),
        Pair("Boston", "Atlanta")
    )
    private var graph = CityGraph(verts, edges)

    /*
    Depth First Traversal
     */

    @Test
    fun bfsTraversalStartIllegalArgumentsException() {
        expectedException.expect(IllegalArgumentException::class.java)
        expectedException.expectMessage("Starting city is not in this graph")
        graph.bfsTraversal("Louisville")
    }

    @Test
    fun bfsTraversalStopIllegalArgumentsException() {
        expectedException.expect(IllegalArgumentException::class.java)
        expectedException.expectMessage("Ending city is not in this graph")
        graph.bfsTraversal("Chicago","Louisville")
    }

    @Test
    fun bfsTraversalChicagoToDenver() {
        val bfsTraversalResult = graph.bfsTraversal("Chicago", "Denver")
        assert(bfsTraversalResult.first() === "Chicago")
        assert(bfsTraversalResult.last() === "Denver")
        assert(bfsTraversalResult.size > 1)
    }

    @Test
    fun bfsTraversalChicagoToSeattle() {
        val bfsTraversalResult = graph.bfsTraversal("Chicago", "Seattle")
        assert(bfsTraversalResult.first() === "Chicago")
        assert(bfsTraversalResult.last() === "Seattle")
        assert(bfsTraversalResult.size > 4)
    }

    @Test
    fun bfsTraversalChicagoToSanDiego() {
        assert(graph.bfsTraversal("Chicago", "San Diego").containsAll(verts.filter { city -> city != "San Diego" }))
    }

    @Test
    fun bfsTraversal() {
        assert(graph.bfsTraversal("Chicago").containsAll(verts.filter { city -> city != "San Diego" }))
    }

    /*
    Depth First Traversal
     */

    @Test
    fun dfsTraversalStartIllegalArgumentsException() {
        expectedException.expect(IllegalArgumentException::class.java)
        expectedException.expectMessage("Starting city is not in this graph")
        graph.dfsTraversal("Louisville")
    }

    @Test
    fun dfsTraversalStopIllegalArgumentsException() {
        expectedException.expect(IllegalArgumentException::class.java)
        expectedException.expectMessage("Ending city is not in this graph")
        graph.dfsTraversal("Chicago","Louisville")
    }

    @Test
    fun dfsTraversalChicagoToMinneapolis() {
        val dfsTraversalResult = graph.dfsTraversal("Chicago", "Minneapolis")
        assertEquals("Chicago", dfsTraversalResult.first())
        assertEquals("Minneapolis", dfsTraversalResult.last())
        assertEquals(3, dfsTraversalResult.size)
    }

    @Test
    fun dfsTraversalSeattleToMinneapolis() {
        val dfsTraversalResult = graph.dfsTraversal("Seattle", "Minneapolis")
        assertEquals("Seattle", dfsTraversalResult.first())
        assertEquals("Minneapolis", dfsTraversalResult.last())
        assertEquals(5, dfsTraversalResult.size)
    }

    @Test
    fun dfsTraversalDenverToAtlanta() {
        val dfsTraversalResult = graph.dfsTraversal("Denver", "Atlanta")
        assertEquals("Denver", dfsTraversalResult.first())
        assertEquals("Atlanta", dfsTraversalResult.last())
        assertEquals(3, dfsTraversalResult.size)
    }

    @Test
    fun dfsTraversal() {
        assert(graph.dfsTraversal("Chicago").containsAll(verts.filter { city -> city != "San Diego" }))
    }
    
}