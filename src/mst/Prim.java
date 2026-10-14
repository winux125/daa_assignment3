package mst;

import graph.*;
import java.util.*;

public class Prim
{
    public static long pushes = 0;

    public static int mst(Graph graph)
    {
        pushes = 0;

        List<List<int[]>> adj = graph.adjacency(false);
        boolean[] visited = new boolean[graph.V];

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
        pq.add(new int[]{0, 0});
        pushes++;

        int total = 0;
        int count = 0;

        while (!pq.isEmpty())
        {
            int[] cur = pq.poll();
            int u = cur[0];

            if (visited[u]) continue;

            visited[u] = true;
            total += cur[1];
            count++;

            for (int[] next : adj.get(u))
            {
                if (!visited[next[0]])
                {
                    pq.add(next);
                    pushes++;
                }
            }
        }

        if (count != graph.V)
        {
            System.out.println("Graph is disconnected!");
            return -1;
        }
        return total;
    }
}
