package connectivity;

import graph.*;
import java.util.*;

public class TopologicalSort
{

    public static long edgeVisits = 0;

    public static int[] order(Graph graph)
    {
        edgeVisits = 0;

        int n = graph.V;
        List<List<int[]>> adj = graph.adjacency(true);

        int[] indegree = new int[n];
        for (Edge e : graph.edges)
        {
            indegree[e.v]++;
        }

        int[] queue = new int[n];
        int head = 0;
        int tail = 0;

        for (int i = 0; i < n; i++)
        {
            if (indegree[i] == 0) queue[tail++] = i;
        }

        while (head < tail)
        {
            int u = queue[head++];

            for (int[] edge : adj.get(u))
            {
                edgeVisits++;

                if (--indegree[edge[0]] == 0)
                {
                    queue[tail++] = edge[0];
                }
            }
        }

        if (tail != n)
        {
            return null;
        }

        return queue;
    }
}
