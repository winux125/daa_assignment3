package tests;

import connectivity.*;
import dsu.DSU;
import experiments.GraphGenerator;
import graph.*;
import mst.*;
import shortestpath.Dijkstra;
import java.util.*;

public class AllTests
{
    static int passed = 0;
    static int failed = 0;

    static void check(String name, boolean ok)
    {
        if (ok)
        {
            passed++;
            System.out.println("PASS  " + name);
        }
        else
        {
            failed++;
            System.out.println("FAIL  " + name);
        }
    }

    public static void main(String[] args)
    {
        testDSU();
        testMST();
        testTopologicalSort();
        testSCC();
        testDijkstra();
        testGeneratedGraphs();

        System.out.println();
        System.out.println(passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }

    static void testDSU()
    {
        DSU dsu = new DSU(5);
        check("DSU: separate sets at the start", dsu.find(0) != dsu.find(1));
        check("DSU: union of different sets", dsu.union(0, 1));
        check("DSU: union of the same set", !dsu.union(1, 0));
        dsu.union(2, 3);
        dsu.union(1, 3);
        check("DSU: transitive union", dsu.find(0) == dsu.find(2));
        check("DSU: untouched element", dsu.find(4) != dsu.find(0));
    }

    static void testMST()
    {
        Graph g = new Graph(4);
        g.addEdge(0, 1, 10);
        g.addEdge(0, 2, 1);
        g.addEdge(1, 3, 2);
        g.addEdge(2, 3, 3);
        g.addEdge(0, 3, 4);
        check("MST small graph: Kruskal", Kruskal.mst(g) == 6);
        check("MST small graph: Prim", Prim.mst(g) == 6);

        Graph triangle = new Graph(3);
        triangle.addEdge(0, 1, 5);
        triangle.addEdge(1, 2, 5);
        triangle.addEdge(2, 0, 5);
        check("MST many valid trees: Kruskal", Kruskal.mst(triangle) == 10);
        check("MST many valid trees: Prim", Prim.mst(triangle) == 10);

        Graph dup = new Graph(5);
        dup.addEdge(0, 1, 2);
        dup.addEdge(1, 2, 2);
        dup.addEdge(2, 3, 2);
        dup.addEdge(3, 4, 2);
        dup.addEdge(0, 4, 2);
        dup.addEdge(0, 2, 3);
        check("MST duplicate weights: Kruskal", Kruskal.mst(dup) == 8);
        check("MST duplicate weights: Prim", Prim.mst(dup) == 8);

        Graph disc = new Graph(4);
        disc.addEdge(0, 1, 1);
        disc.addEdge(2, 3, 1);
        check("MST disconnected graph: Kruskal", Kruskal.mst(disc) == -1);
        check("MST disconnected graph: Prim", Prim.mst(disc) == -1);

        Graph one = new Graph(1);
        check("MST single vertex: Kruskal", Kruskal.mst(one) == 0);
        check("MST single vertex: Prim", Prim.mst(one) == 0);
    }

    static boolean validOrder(Graph g, int[] order)
    {
        if (order == null || order.length != g.V) return false;

        int[] pos = new int[g.V];
        Arrays.fill(pos, -1);
        for (int i = 0; i < order.length; i++)
        {
            if (pos[order[i]] != -1) return false;
            pos[order[i]] = i;
        }
        for (Edge e : g.edges)
        {
            if (pos[e.u] >= pos[e.v]) return false;
        }
        return true;
    }

    static void testTopologicalSort()
    {
        Graph dag = new Graph(6);
        dag.addEdge(5, 2, 1);
        dag.addEdge(5, 0, 1);
        dag.addEdge(4, 0, 1);
        dag.addEdge(4, 1, 1);
        dag.addEdge(2, 3, 1);
        dag.addEdge(3, 1, 1);
        check("Topo DAG: valid order", validOrder(dag, TopologicalSort.order(dag)));

        Graph cyclic = new Graph(3);
        cyclic.addEdge(0, 1, 1);
        cyclic.addEdge(1, 2, 1);
        cyclic.addEdge(2, 0, 1);
        check("Topo cycle: detected", TopologicalSort.order(cyclic) == null);

        Graph partial = new Graph(5);
        partial.addEdge(0, 1, 1);
        partial.addEdge(1, 2, 1);
        partial.addEdge(2, 1, 1);
        partial.addEdge(3, 4, 1);
        check("Topo cycle in one part of the graph: detected", TopologicalSort.order(partial) == null);

        Graph disc = new Graph(6);
        disc.addEdge(0, 1, 1);
        disc.addEdge(2, 3, 1);
        check("Topo disconnected DAG: valid order", validOrder(disc, TopologicalSort.order(disc)));

        Graph one = new Graph(1);
        check("Topo single vertex: valid order", validOrder(one, TopologicalSort.order(one)));
    }

    static boolean partitions(Graph g, List<List<Integer>> comps)
    {
        int[] seen = new int[g.V];
        for (List<Integer> c : comps)
        {
            for (int v : c)
            {
                seen[v]++;
            }
        }
        for (int i = 0; i < g.V; i++)
        {
            if (seen[i] != 1) return false;
        }
        return true;
    }

    static void testSCC()
    {
        Graph g = new Graph(8);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1);
        g.addEdge(2, 3, 1);
        g.addEdge(3, 4, 1);
        g.addEdge(4, 5, 1);
        g.addEdge(5, 3, 1);
        g.addEdge(6, 5, 1);
        g.addEdge(6, 7, 1);
        List<List<Integer>> comps = SCC.components(g);
        check("SCC small graph: 4 components", comps.size() == 4);
        check("SCC small graph: every vertex in exactly one component", partitions(g, comps));

        Graph dag = new Graph(4);
        dag.addEdge(0, 1, 1);
        dag.addEdge(1, 2, 1);
        dag.addEdge(0, 3, 1);
        check("SCC DAG: every vertex alone", SCC.components(dag).size() == 4);

        Graph ring = new Graph(5);
        for (int i = 0; i < 5; i++)
        {
            ring.addEdge(i, (i + 1) % 5, 1);
        }
        check("SCC ring: one component", SCC.components(ring).size() == 1);

        Graph one = new Graph(1);
        check("SCC single vertex: one component", SCC.components(one).size() == 1);
    }

    static void testDijkstra()
    {
        Graph g = new Graph(5);
        g.addEdge(0, 1, 4);
        g.addEdge(0, 2, 1);
        g.addEdge(2, 1, 2);
        g.addEdge(1, 3, 1);
        g.addEdge(2, 3, 5);

        int[] parent = new int[5];
        int[] d = Dijkstra.run(g, 0, parent);
        check("Dijkstra small graph: distances", d[0] == 0 && d[1] == 3 && d[2] == 1 && d[3] == 4);
        check("Dijkstra unreachable vertex", d[4] == Dijkstra.INF);
        check("Dijkstra path", Dijkstra.path(d, parent, 3).equals(Arrays.asList(0, 2, 1, 3)));
        check("Dijkstra path to unreachable vertex is empty", Dijkstra.path(d, parent, 4).isEmpty());

        Graph ties = new Graph(4);
        ties.addEdge(0, 1, 2);
        ties.addEdge(0, 2, 2);
        ties.addEdge(1, 3, 2);
        ties.addEdge(2, 3, 2);
        check("Dijkstra equal weights", Dijkstra.run(ties, 0, new int[4])[3] == 4);

        Graph one = new Graph(1);
        check("Dijkstra single vertex", Dijkstra.run(one, 0, new int[1])[0] == 0);

        Graph neg = new Graph(2);
        neg.addEdge(0, 1, -1);
        boolean thrown = false;
        try
        {
            Dijkstra.run(neg, 0, new int[2]);
        }
        catch (IllegalArgumentException ex)
        {
            thrown = true;
        }
        check("Dijkstra negative weight is rejected", thrown);
    }

    static void testGeneratedGraphs()
    {
        int[] vertices = {10, 100, 1000};
        int[] edges = {30, 500, 5000};

        for (int i = 0; i < vertices.length; i++)
        {
            int v = vertices[i];
            int e = edges[i];
            String name = "V=" + v + " E=" + e;

            Graph connected = GraphGenerator.connected(v, e, 100, new Random(2024));
            Graph dag = GraphGenerator.dag(v, e, 100, new Random(2024));
            Graph cyclic = GraphGenerator.cyclic(v, e, 100, new Random(2024));
            Graph digraph = GraphGenerator.directed(v, e, 100, new Random(2024));

            check(name + ": Kruskal weight equals Prim weight", Kruskal.mst(connected) == Prim.mst(connected)
                && Kruskal.mst(connected) > 0);
            check(name + ": topological order of the DAG is valid", validOrder(dag, TopologicalSort.order(dag)));
            check(name + ": cycle is detected", TopologicalSort.order(cyclic) == null);
            check(name + ": SCC partition", partitions(digraph, SCC.components(digraph)));
        }

        Random rnd = new Random(42);
        int same = 0;
        int valid = 0;
        int rounds = 100;
        for (int t = 0; t < rounds; t++)
        {
            int v = 2 + rnd.nextInt(20);
            int e = Math.min(v * (v - 1) / 2, v - 1 + rnd.nextInt(v));

            Graph g = GraphGenerator.connected(v, e, 5, rnd);
            if (Kruskal.mst(g) == Prim.mst(g)) same++;

            Graph dag = GraphGenerator.dag(v, e, 5, rnd);
            if (validOrder(dag, TopologicalSort.order(dag))) valid++;
        }
        check("Random graphs: Kruskal = Prim (" + rounds + " graphs)", same == rounds);
        check("Random graphs: valid topological order (" + rounds + " DAGs)", valid == rounds);
    }
}
