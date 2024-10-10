// Ilyas
import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[][] adjacencyMatrix = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                adjacencyMatrix[i][j] = scanner.nextInt();
            }
        }

        Graph<Integer, Void> graph = new Graph<>();
        for (int i = 0; i < n; i++) {
            graph.addVertex(i);
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (adjacencyMatrix[i][j] == 1) {
                    graph.addEdge(graph.verticesAt(i), graph.verticesAt(j), null);
                }
            }
        }

        if (graph.isConnected()) {
            System.out.println("YES");
        } else {
            System.out.println("NO");
        }
    }

    static class DoublyLinkedList<V> implements Iterable<DoublyLinkedList.ListNode<V>> {
        static class ListNode<V> {
            V data;
            ListNode<V> prev;
            ListNode<V> next;

            public ListNode(V data) {
                this.data = data;
                this.prev = null;
                this.next = null;
            }
        }

        private ListNode<V> head;
        private ListNode<V> tail;

        public DoublyLinkedList() {
            this.head = null;
            this.tail = null;
        }

        public void add(V data) {
            ListNode<V> newNode = new ListNode<>(data);
            if (head == null) {
                head = newNode;
                tail = newNode;
            } else {
                tail.next = newNode;
                newNode.prev = tail;
                tail = newNode;
            }
        }

        @Override
        public Iterator<ListNode<V>> iterator() {
            return new Iterator<>() {
                private ListNode<V> current = head;

                @Override
                public boolean hasNext() {
                    return current != null;
                }

                @Override
                public ListNode<V> next() {
                    ListNode<V> temp = current;
                    current = current.next;
                    return temp;
                }
            };
        }
    }

    static class Graph<V, E> {
        class Vertex {
            V value;
            DoublyLinkedList.ListNode<Vertex> listNode;
            DoublyLinkedList<Edge> adjacentEdges;
            int in;
            int out;
            boolean visited;

            public Vertex(V value) {
                this.value = value;
                this.adjacentEdges = new DoublyLinkedList<>();
                this.visited = false;
            }
        }

        class Edge {
            Vertex from, to;
            E label;

            public Edge(Vertex from, Vertex to, E label) {
                this.from = from;
                this.to = to;
                this.label = label;
            }
        }

        DoublyLinkedList<Vertex> vertices;
        DoublyLinkedList<Edge> edges;

        public Graph() {
            this.vertices = new DoublyLinkedList<>();
            this.edges = new DoublyLinkedList<>();
        }

        void addVertex(V value) {
            Vertex v = new Vertex(value);
            v.listNode = new DoublyLinkedList.ListNode<>(v);
            this.vertices.add(v);
        }

        void addEdge(Vertex from, Vertex to, E label) {
            Edge edge = new Edge(from, to, label);
            this.edges.add(edge);
            from.adjacentEdges.add(edge);
            from.out++;
            to.in++;
        }

        Vertex verticesAt(int index) {
            int i = 0;
            for (DoublyLinkedList.ListNode<Vertex> node : vertices) {
                if (i == index) {
                    return node.data;
                }
                i++;
            }
            return null;
        }

        boolean isConnected() {
            Vertex startVertex = verticesAt(0);
            IlyasKhatipov_DFS(startVertex);
            for (DoublyLinkedList.ListNode<Vertex> node : vertices) {
                if (!node.data.visited) {
                    return false;
                }
            }
            return true;
        }

        boolean IlyasKhatipov_DFS(Vertex current) {
            current.visited = true;
            for (DoublyLinkedList.ListNode<Edge> edge : current.adjacentEdges) {
                if (!edge.data.to.visited && !IlyasKhatipov_DFS(edge.data.to)) {
                    return false;
                }
            }
            return true;
        }
    }
}
