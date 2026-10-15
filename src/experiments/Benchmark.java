package experiments;

import connectivity.*;
import graph.*;
import mst.*;
import shortestpath.Dijkstra;
import java.io.*;
import java.util.*;

public class Benchmark
{
    static final int WARMUP = 20;
    static final int RUNS = 5;
    static final int MAX_WEIGHT = 100;
    static final long SEED = 2024;

    static final int[] VERTICES = {10, 100, 1000};
    static final int[] EDGES = {30, 500, 5000};

    static double time(Runnable task)
    {
        for (int i = 0; i < WARMUP; i++)
        {
            task.run();
        }

        double total = 0;
        for (int i = 0; i < RUNS; i++)
        {
            long start = System.nanoTime();
            task.run();
            total += (System.nanoTime() - start) / 1e6;
        }
        return total / RUNS;
    }

    public static void main(String[] args) throws IOException
    {
        String dir = args.length > 0 ? args[0] : "results/tables";
        new File(dir).mkdirs();

        PrintWriter mst = new PrintWriter(dir + "/mst.csv");
        PrintWriter con = new PrintWriter(dir + "/connectivity.csv");
        PrintWriter dij = new PrintWriter(dir + "/dijkstra.csv");

        mst.println("V,E,kruskal_ms,prim_ms,kruskal_comparisons,kruskal_finds,kruskal_unions,prim_pushes");
        con.println("V,E,scc_ms,scc_edge_visits,scc_count,topo_dag_ms,topo_dag_edge_visits,topo_cyclic_ms,cycle_detected");
        dij.println("V,E,time_ms,relaxations,heap_pushes,reached");

        for (int i = 0; i < VERTICES.length; i++)
        {
            int v = VERTICES[i];
            int e = EDGES[i];

            Graph undirected = GraphGenerator.connected(v, e, MAX_WEIGHT, new Random(SEED));
            double kruskalMs = time(() -> Kruskal.mst(undirected));
            double primMs = time(() -> Prim.mst(undirected));
            if (Kruskal.mst(undirected) != Prim.mst(undirected))
            {
                throw new IllegalStateException("Kruskal and Prim give different weights");
            }
            mst.println(v + "," + e + "," + kruskalMs + "," + primMs + "," + Kruskal.comparisons + ","
                + Kruskal.finds + "," + Kruskal.unions + "," + Prim.pushes);

            Graph digraph = GraphGenerator.directed(v, e, MAX_WEIGHT, new Random(SEED));
            Graph dag = GraphGenerator.dag(v, e, MAX_WEIGHT, new Random(SEED));
            Graph cyclic = GraphGenerator.cyclic(v, e, MAX_WEIGHT, new Random(SEED));

            double sccMs = time(() -> SCC.components(digraph));
            int sccCount = SCC.components(digraph).size();
            long sccVisits = SCC.edgeVisits;

            double dagMs = time(() -> TopologicalSort.order(dag));
            TopologicalSort.order(dag);
            long dagVisits = TopologicalSort.edgeVisits;

            double cyclicMs = time(() -> TopologicalSort.order(cyclic));
            int detected = TopologicalSort.order(cyclic) == null ? 1 : 0;

            con.println(v + "," + e + "," + sccMs + "," + sccVisits + "," + sccCount + "," + dagMs + ","
                + dagVisits + "," + cyclicMs + "," + detected);

            int[] parent = new int[v];
            double dijMs = time(() -> Dijkstra.run(digraph, 0, parent));
            int[] dist = Dijkstra.run(digraph, 0, parent);
            int reached = 0;
            for (int d : dist)
            {
                if (d != Dijkstra.INF) reached++;
            }
            dij.println(v + "," + e + "," + dijMs + "," + Dijkstra.relaxations + "," + Dijkstra.heapPushes + "," + reached);

            System.out.println("done V=" + v + " E=" + e);
        }

        mst.close();
        con.close();
        dij.close();
    }
}
