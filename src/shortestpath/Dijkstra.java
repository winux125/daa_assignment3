package shortestpath;

import graph.*;
import java.util.*;

public class Dijkstra
{
    public static final int INF = Integer.MAX_VALUE;

    public static long relaxations = 0;
    public static long heapPushes = 0;

    public static int[] run(Graph graph, int source, int[] parent)
    {
        relaxations = 0;
        heapPushes = 0;

        for (Edge e : graph.edges)
        {
            if (e.weight < 0) throw new IllegalArgumentException("negative weight");
        }

        List<List<int[]>> adj = graph.adjacency(true);
        int[] dist = new int[graph.V];
        Arrays.fill(dist, INF);
        Arrays.fill(parent, -1);
        dist[source] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
        pq.add(new int[]{source, 0});
        heapPushes++;

        while (!pq.isEmpty())
        {
            int[] cur = pq.poll();
            int u = cur[0];

            if (cur[1] > dist[u]) continue;

            for (int[] e : adj.get(u))
            {
                int newDist = dist[u] + e[1];
                if (newDist < dist[e[0]])
                {
                    dist[e[0]] = newDist;
                    parent[e[0]] = u;
                    pq.add(new int[]{e[0], newDist});
                    relaxations++;
                    heapPushes++;
                }
            }
        }
        return dist;
    }

    public static List<Integer> path(int[] dist, int[] parent, int target)
    {
        List<Integer> path = new ArrayList<>();
        if (dist[target] == INF) return path;

        for (int v = target; v != -1; v = parent[v])
        {
            path.add(v);
        }
        Collections.reverse(path);
        return path;
    }
}
