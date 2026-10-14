package graph;

import java.util.*;

public class Graph
{

    public int V;
    public List<Edge> edges = new ArrayList<>();

    public Graph(int v)
    {
        this.V = v;
    }
    public void addEdge(int u,int v, int weight)
    {
        edges.add(new Edge(u,v,weight));
    }

    public List<List<int[]>> adjacency(boolean directed)
    {
        List<List<int[]>> adj = new ArrayList<>();

        for (int i = 0; i < V; i++)
        {
            adj.add(new ArrayList<>());
        }

        for (Edge e : edges)
        {
            adj.get(e.u).add(new int[]{e.v, e.weight});
            if (!directed) adj.get(e.v).add(new int[]{e.u, e.weight});
        }
        return adj;
    }
}
