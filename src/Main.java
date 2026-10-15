import connectivity.SCC;
import connectivity.TopologicalSort;
import graph.Graph;
import mst.Kruskal;
import mst.Prim;
import shortestpath.Dijkstra;
import java.util.Arrays;

public class Main
{
    public static void main(String[] args)
    {
        Graph roads = new Graph(5);
        roads.addEdge(0, 1, 4);
        roads.addEdge(0, 2, 1);
        roads.addEdge(1, 2, 2);
        roads.addEdge(1, 3, 5);
        roads.addEdge(2, 3, 8);
        roads.addEdge(3, 4, 3);

        System.out.println("Kruskal MST weight: " + Kruskal.mst(roads));
        System.out.println("Prim MST weight: " + Prim.mst(roads));

        Graph services = new Graph(6);
        services.addEdge(0, 1, 4);
        services.addEdge(1, 2, 1);
        services.addEdge(2, 0, 2);
        services.addEdge(2, 3, 7);
        services.addEdge(3, 4, 1);
        services.addEdge(4, 3, 1);

        System.out.println("SCC: " + SCC.components(services));
        System.out.println("Topological order of services: " + Arrays.toString(TopologicalSort.order(services)));

        Graph dag = new Graph(5);
        dag.addEdge(0, 1, 1);
        dag.addEdge(0, 2, 1);
        dag.addEdge(1, 3, 1);
        dag.addEdge(2, 3, 1);
        dag.addEdge(3, 4, 1);
        System.out.println("Topological order of dag: " + Arrays.toString(TopologicalSort.order(dag)));

        int[] parent = new int[services.V];
        int[] dist = Dijkstra.run(services, 0, parent);
        for (int v = 0; v < services.V; v++)
        {
            if (dist[v] == Dijkstra.INF)
            {
                System.out.println("0 -> " + v + ": unreachable");
            }
            else
            {
                System.out.println("0 -> " + v + ": distance " + dist[v] + ", path " + Dijkstra.path(dist, parent, v));
            }
        }
    }
}
