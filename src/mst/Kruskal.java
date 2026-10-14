package mst;

import dsu.DSU;
import graph.*;
import java.util.*;

public class Kruskal
{
    public static long comparisons = 0;
    public static long finds = 0;
    public static long unions = 0;

    public static int mst(Graph graph)
    {
        comparisons = 0;

        List<Edge> edges = new ArrayList<>(graph.edges);
        edges.sort((a, b) -> {
            comparisons++;
            return Integer.compare(a.weight, b.weight);
        });

        DSU dsu = new DSU(graph.V);
        int total = 0;
        int used = 0;

        for (Edge e : edges)
        {
            if (dsu.union(e.u, e.v))
            {
                total += e.weight;
                used++;
            }
        }

        finds = dsu.finds;
        unions = dsu.unions;

        if (used != graph.V - 1)
        {
            System.out.println("Graph is disconnected!");
            return -1;
        }
        return total;
    }
}
