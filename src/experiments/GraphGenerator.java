package experiments;

import graph.*;
import java.util.*;

public class GraphGenerator
{
    public static Graph connected(int v, int e, int maxW, Random rnd)
    {
        Graph graph = new Graph(v);
        Set<Long> used = new HashSet<>();

        for (int i = 1; i < v; i++)
        {
            int j = rnd.nextInt(i);
            used.add(key(j, i, v));
            graph.addEdge(i, j, 1 + rnd.nextInt(maxW));
        }

        while (graph.edges.size() < e)
        {
            int a = rnd.nextInt(v);
            int b = rnd.nextInt(v);
            if (a == b) continue;

            if (used.add(key(Math.min(a, b), Math.max(a, b), v)))
            {
                graph.addEdge(a, b, 1 + rnd.nextInt(maxW));
            }
        }
        return graph;
    }

    public static Graph directed(int v, int e, int maxW, Random rnd)
    {
        Graph graph = new Graph(v);
        Set<Long> used = new HashSet<>();

        while (graph.edges.size() < e)
        {
            int a = rnd.nextInt(v);
            int b = rnd.nextInt(v);
            if (a == b) continue;

            if (used.add(key(a, b, v)))
            {
                graph.addEdge(a, b, 1 + rnd.nextInt(maxW));
            }
        }
        return graph;
    }

    public static Graph dag(int v, int e, int maxW, Random rnd)
    {
        Graph graph = new Graph(v);
        Set<Long> used = new HashSet<>();

        while (graph.edges.size() < e)
        {
            int a = rnd.nextInt(v);
            int b = rnd.nextInt(v);
            if (a == b) continue;

            int from = Math.min(a, b);
            int to = Math.max(a, b);
            if (used.add(key(from, to, v)))
            {
                graph.addEdge(from, to, 1 + rnd.nextInt(maxW));
            }
        }
        return graph;
    }

    public static Graph cyclic(int v, int e, int maxW, Random rnd)
    {
        Graph graph = new Graph(v);
        Set<Long> used = new HashSet<>();

        for (int i = 0; i < v; i++)
        {
            int next = (i + 1) % v;
            used.add(key(i, next, v));
            graph.addEdge(i, next, 1 + rnd.nextInt(maxW));
        }

        while (graph.edges.size() < e)
        {
            int a = rnd.nextInt(v);
            int b = rnd.nextInt(v);
            if (a == b) continue;

            if (used.add(key(a, b, v)))
            {
                graph.addEdge(a, b, 1 + rnd.nextInt(maxW));
            }
        }
        return graph;
    }

    private static long key(int a, int b, int v)
    {
        return (long) a * v + b;
    }
}
