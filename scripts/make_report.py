import csv
import os

import matplotlib

matplotlib.use("Agg")
import matplotlib.pyplot as plt

ROOT = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..")
TABLES = os.path.join(ROOT, "results", "tables")
PLOTS = os.path.join(ROOT, "results", "plots")


def read(name):
    with open(os.path.join(TABLES, name)) as f:
        rows = list(csv.DictReader(f))
    return {key: [float(r[key]) for r in rows] for key in rows[0]}


mst = read("mst.csv")
con = read("connectivity.csv")
dij = read("dijkstra.csv")

series = [
    ("Kruskal", mst["kruskal_ms"]),
    ("Prim", mst["prim_ms"]),
    ("SCC", con["scc_ms"]),
    ("Topological sort (DAG)", con["topo_dag_ms"]),
    ("Dijkstra", dij["time_ms"]),
]

os.makedirs(PLOTS, exist_ok=True)

for key, label, name in (("V", "number of vertices", "time_vs_vertices.png"),
                         ("E", "number of edges", "time_vs_edges.png")):
    plt.figure(figsize=(6, 4))
    for title, times in series:
        plt.plot(mst[key], times, marker="o", label=title)
    plt.xscale("log")
    plt.yscale("log")
    plt.xlabel(label)
    plt.ylabel("average time (ms)")
    plt.title("Execution time vs " + label)
    plt.grid(True)
    plt.legend()
    plt.tight_layout()
    plt.savefig(os.path.join(PLOTS, name), dpi=120)
    plt.close()
    print("saved", name)

plt.figure(figsize=(6, 4))
plt.plot(mst["E"], mst["kruskal_comparisons"], marker="o", label="Kruskal sort comparisons")
plt.plot(mst["E"], mst["kruskal_finds"], marker="o", label="Kruskal DSU find calls")
plt.plot(mst["E"], mst["prim_pushes"], marker="o", label="Prim heap pushes")
plt.xscale("log")
plt.yscale("log")
plt.xlabel("number of edges")
plt.ylabel("operations")
plt.title("Kruskal vs Prim: operation counts")
plt.grid(True)
plt.legend()
plt.tight_layout()
plt.savefig(os.path.join(PLOTS, "mst_operations.png"), dpi=120)
plt.close()
print("saved mst_operations.png")
