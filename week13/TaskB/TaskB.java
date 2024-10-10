// Ilyas
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        int M = scanner.nextInt();
        AdjacencyListGraph graph = new AdjacencyListGraph(N);

        for (int i = 0; i < M; i++) {
            int from = scanner.nextInt() - 1;
            int to = scanner.nextInt() - 1;
            int length = scanner.nextInt();
            int bandwidth = scanner.nextInt();
            graph.addEdge(from, to, length, bandwidth);
        }

        int startVertex = scanner.nextInt() - 1;
        int endVertex = scanner.nextInt() - 1;
        int minBandwidth = scanner.nextInt();
        scanner.close();
        ArrayList<Integer> path = graph.IlyasKhatipov_shortestPathWithMinBandwidth_with_Dijkstras_algorithm(startVertex, endVertex, minBandwidth);
        if (path.isEmpty()) {
            System.out.println("IMPOSSIBLE");
        } else {
            int totalLength = graph.getTotalLength(path);
            int totalBandwidth = graph.getMinBandwidth(path);
            System.out.println(path.size() + " " + totalLength + " " + totalBandwidth);
            for (int vertex : path) {
                System.out.print((vertex + 1) + " ");
            }
            System.out.println();
        }
    }

    static class AdjacencyListGraph {
        private ArrayList<ArrayList<Edge>> adjacencyList;
        private int size;

        public AdjacencyListGraph(int size) {
            this.size = size;
            adjacencyList = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                adjacencyList.add(new ArrayList<>());
            }
        }

        public void addEdge(int from, int to, int length, int bandwidth) {
            adjacencyList.get(from).add(new Edge(to, length, bandwidth));
            adjacencyList.get(to).add(new Edge(from, length, bandwidth));
        }

        public ArrayList<Integer> IlyasKhatipov_shortestPathWithMinBandwidth_with_Dijkstras_algorithm(int start, int end, int minBandwidth) {
            int[] dist = new int[size];
            boolean[] visited = new boolean[size];
            int[] prev = new int[size];

            for (int i = 0; i < size; i++) {
                dist[i] = Integer.MAX_VALUE;
                prev[i] = -1;
            }
            dist[start] = 0;

            for (int i = 0; i < size; i++) {
                int minDist = Integer.MAX_VALUE;
                int minVertex = -1;
                for (int v = 0; v < size; v++) {
                    if (!visited[v] && dist[v] < minDist) {
                        minDist = dist[v];
                        minVertex = v;
                    }
                }

                if (minVertex == -1 || minDist == Integer.MAX_VALUE) {
                    break;
                }

                visited[minVertex] = true;

                for (Edge edge : adjacencyList.get(minVertex)) {
                    int v = edge.to;
                    int length = edge.length;
                    int bandwidth = edge.bandwidth;
                    if (!visited[v] && bandwidth >= minBandwidth && dist[minVertex] + length < dist[v]) {
                        dist[v] = dist[minVertex] + length;
                        prev[v] = minVertex;
                    }
                }
            }

            ArrayList<Integer> path = new ArrayList<>();
            for (int at = end; at != -1; at = prev[at]) {
                path.add(at);
            }
            reverseArrayList(path);

            return path.size() > 1 && dist[end] != Integer.MAX_VALUE ? path : new ArrayList<>();
        }

        public void reverseArrayList(ArrayList<Integer> list) {
            int start = 0;
            int end = list.size() - 1;
            while (start < end) {
                int temp = list.get(start);
                list.set(start, list.get(end));
                list.set(end, temp);
                start++;
                end--;
            }
        }

        public int getTotalLength(ArrayList<Integer> path) {
            int totalLength = 0;
            for (int i = 0; i < path.size() - 1; i++) {
                int u = path.get(i);
                int v = path.get(i + 1);
                for (Edge edge : adjacencyList.get(u)) {
                    if (edge.to == v) {
                        totalLength += edge.length;
                        break;
                    }
                }
            }
            return totalLength;
        }

        public int getMinBandwidth(ArrayList<Integer> path) {
            int minBandwidth = Integer.MAX_VALUE;
            for (int i = 0; i < path.size() - 1; i++) {
                int u = path.get(i);
                int v = path.get(i + 1);
                for (Edge edge : adjacencyList.get(u)) {
                    if (edge.to == v) {
                        minBandwidth = Math.min(minBandwidth, edge.bandwidth);
                        break;
                    }
                }
            }
            return minBandwidth;
        }

        static class Edge {
            int to;
            int length;
            int bandwidth;

            Edge(int to, int length, int bandwidth) {
                this.to = to;
                this.length = length;
                this.bandwidth = bandwidth;
            }
        }
    }
}
