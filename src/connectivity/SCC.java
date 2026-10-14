package connectivity;

import graph.*;
import java.util.*;

public class SCC
{
    public static long edgeVisits = 0;

    public static List<List<Integer>> components(Graph graph)
    {
        edgeVisits = 0;
        int n = graph.V;

        List<List<int[]>> adj = graph.adjacency(true);
        List<List<int[]>> radj = new ArrayList<>();
        for (int i = 0; i < n; i++)
        {
            radj.add(new ArrayList<>());
        }
        for (Edge e : graph.edges)
        {
            radj.get(e.v).add(new int[]{e.u, e.weight});
        }

        boolean[] visited = new boolean[n];
        Deque<Integer> order = new ArrayDeque<>();
        for (int i = 0; i < n; i++)
        {
            if (!visited[i]) dfs1(i, adj, visited, order);
        }

        Arrays.fill(visited, false);
        List<List<Integer>> result = new ArrayList<>();
        while (!order.isEmpty())
        {
            int s = order.pop();
            if (visited[s]) continue;

            List<Integer> component = new ArrayList<>();
            dfs2(s, radj, visited, component);
            result.add(component);
        }
        return result;
    }

    private static void dfs1(int u, List<List<int[]>> adj, boolean[] visited, Deque<Integer> order)
    {
        visited[u] = true;
        for (int[] e : adj.get(u))
        {
            edgeVisits++;
            if (!visited[e[0]]) dfs1(e[0], adj, visited, order);
        }
        order.push(u);
    }

    private static void dfs2(int u, List<List<int[]>> radj, boolean[] visited, List<Integer> component)
    {
        visited[u] = true;
        component.add(u);
        for (int[] e : radj.get(u))
        {
            edgeVisits++;
            if (!visited[e[0]]) dfs2(e[0], radj, visited, component);
        }
    }
}
