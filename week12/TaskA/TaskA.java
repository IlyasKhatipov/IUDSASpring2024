// Ilyas
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        Graph<Integer, Integer> graph = new Graph<>(N);

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int weight = scanner.nextInt();
                if (weight < 100000) {
                    graph.addEdge(i, j, weight);
                }
            }
        }

        LinkedList<Integer> dist = new LinkedList<>();
        LinkedList<Integer> pred = new LinkedList<>();
        int[] lastRelaxed = new int[1];
        if (graph.IlyasKhatipov_bellmanFord(dist, pred, lastRelaxed)) {
            System.out.println("YES");

            LinkedList<Integer> cycle = new LinkedList<>();
            LinkedList<Integer> visited = new LinkedList<>();
            for (int i = 0; i < N; i++) {
                visited.add(-1);
            }
            int x = lastRelaxed[0];

            while (visited.get(x) == -1) {
                visited.set(x, cycle.size());
                cycle.add(x);
                x = pred.get(x);
            }

            cycle.clear();

            int start = x;
            do {
                cycle.add(start);
                start = pred.get(start);
            } while (start != x);
            cycle.add(x);

            System.out.println(cycle.size() - 1);
            for (int i = cycle.size() - 1; i > 0; i--) {
                System.out.print((cycle.get(i) + 1) + " ");
            }
            System.out.println();
        } else {
            System.out.println("NO");
        }

        scanner.close();
    }

    static class Graph<V, E> {
        static class Vertex<V> {
            V value;
            LinkedList<Vertex<V>> adjacent;
            int inDegree = 0;
            int outDegree = 0;

            Vertex(V val) {
                value = val;
                adjacent = new LinkedList<>();
            }
        }

        static class Edge<V, E> {
            Vertex<V> from;
            Vertex<V> to;
            E weight;

            Edge(Vertex<V> f, Vertex<V> t, E w) {
                from = f;
                to = t;
                weight = w;
            }
        }

        LinkedList<Vertex<V>> vertices;
        LinkedList<Edge<V, E>> edges;

        Graph(int size) {
            vertices = new LinkedList<>();
            edges = new LinkedList<>();
            for (int i = 0; i < size; i++) {
                vertices.add((Vertex<V>) new Vertex<>(i));
            }
        }

        void addEdge(int from, int to, E weight) {
            Edge<V, E> e = new Edge<>(vertices.get(from), vertices.get(to), weight);
            edges.add(e);
            vertices.get(from).adjacent.add(vertices.get(to));
            vertices.get(to).adjacent.add(vertices.get(from));
            vertices.get(from).outDegree++;
            vertices.get(to).inDegree++;
        }

        boolean IlyasKhatipov_bellmanFord(LinkedList<Integer> dist, LinkedList<Integer> pred, int[] lastRelaxed) {
            int INF = 100000;
            dist.clear();
            pred.clear();
            for (int i = 0; i < vertices.size(); i++) {
                dist.add(INF);
                pred.add(-1);
            }
            dist.set(0, 0);
            lastRelaxed[0] = -1;

            for (int i = 0; i < vertices.size(); i++) {
                lastRelaxed[0] = -1;
                for (Edge<V, E> e : edges) {
                    if (dist.get((Integer) e.from.value) <= INF && dist.get((Integer) e.from.value) + (Integer) e.weight < dist.get((Integer) e.to.value)) {
                        dist.set((Integer) e.to.value, dist.get((Integer) e.from.value) + (Integer) e.weight);
                        pred.set((Integer) e.to.value, (Integer) e.from.value);
                        lastRelaxed[0] = (int) e.to.value;
                    }
                }
            }
            for (Edge<V, E> e : edges) {
                if (dist.get((Integer) e.from.value) < INF && dist.get((Integer) e.from.value) + (Integer) e.weight < dist.get((Integer) e.to.value)) {
                    return true;
                }
            }
            return false;
        }
    }
}
