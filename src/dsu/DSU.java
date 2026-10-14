package dsu;

public class DSU
{
    private int[] parent;
    private int[] size;

    public long finds = 0;
    public long unions = 0;

    public DSU(int n)
    {
        parent = new int[n];
        size = new int[n];

        for (int i = 0; i < n; i++)
        {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public int find(int x)
    {
        finds++;
        if (parent[x] != x)
        {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    public boolean union(int a, int b)
    {
        unions++;
        int rootA = find(a);
        int rootB = find(b);

        if (rootA == rootB) return false;

        if (size[rootA] < size[rootB])
        {
            int t = rootA;
            rootA = rootB;
            rootB = t;
        }

        parent[rootB] = rootA;
        size[rootA] += size[rootB];
        return true;
    }
}
