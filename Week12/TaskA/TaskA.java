import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] str_cfg = scanner.nextLine().split(" ");
        int V = Integer.parseInt(str_cfg[0]);
        int E = Integer.parseInt(str_cfg[1]);

        Graph<Integer, Integer> graph = new Graph<>();

        for (int i = 1; i <= V; i++) {
            graph.addVertex(i);
        }

        for (int i = 0; i < E; i++) {
            String[] str_edge_cfg = scanner.nextLine().split(" ");
            int from = Integer.parseInt(str_edge_cfg[0]);
            int to = Integer.parseInt(str_edge_cfg[1]);
            int weight = Integer.parseInt(str_edge_cfg[2]);
            graph.addEdge(graph.verticesAt(from - 1), graph.verticesAt(to - 1), weight);
        }

        List<List<Graph<Integer, Integer>.Edge>> forest = minimumSpanningForest(graph);
        System.out.println(forest.size());
        for (List<Graph<Integer, Integer>.Edge> tree : forest) {
            System.out.println(tree.size()+1 + " " + tree.get(0).from.value);
            int cnt = 0;
            Collections.reverse(tree);
            for (Graph<Integer, Integer>.Edge edge : tree) {
                System.out.println(edge.from.value + " " + edge.to.value + " " + edge.label);
                cnt++;
            }
        }
    }
    public static <V, E extends Comparable<E>> List<List<Graph<V, E>.Edge>> minimumSpanningForest(Graph<V, E> graph) {
        List<List<Graph<V, E>.Edge>> forest = new ArrayList<>();
        for (DoublyLinkedList.ListNode<Graph<V, E>.Vertex> vertexNode : graph.vertices) {
            Graph<V, E>.Vertex vertex = vertexNode.data;
            if (!vertex.visited) {
                List<Graph<V, E>.Edge> mstEdges = new ArrayList<>();
                graph.IlyasKhatipov_DFS(vertex, mstEdges);
                forest.add(mstEdges);
            }
        }
        resetVisited(graph); // Reset visited status after each traversal
        return forest;
    }

    private static <V, E extends Comparable<E>> void resetVisited(Graph<V, E> graph) {
        for (DoublyLinkedList.ListNode<Graph<V, E>.Vertex> vertexNode : graph.vertices) {
            vertexNode.data.visited = false;
        }
    }

    static class DoublyLinkedList<T> implements Iterable<DoublyLinkedList.ListNode<T>> {
        static class ListNode<T> {
            ListNode<T> prev, next;
            T data;

            ListNode(T data) {
                this.data = data;
            }
        }

        ListNode<T> head, tail;

        public void add(T data) {
            ListNode<T> newNode = new ListNode<>(data);
            if (head == null) {
                head = tail = newNode;
            } else {
                tail.next = newNode;
                newNode.prev = tail;
                tail = newNode;
            }
        }

        @Override
        public Iterator<ListNode<T>> iterator() {
            return new Iterator<>() {
                ListNode<T> current = head;

                @Override
                public boolean hasNext() {
                    return current != null;
                }

                @Override
                public ListNode<T> next() {
                    ListNode<T> temp = current;
                    current = current.next;
                    return temp;
                }
            };
        }
    }

    static class DisjointSet<T> {
        private Map<T, Node<T>> map = new HashMap<>();

        static class Node<T> {
            T data;
            Node<T> parent;
            int rank;

            Node(T data) {
                this.data = data;
                this.parent = this;
                this.rank = 0;
            }
        }

        public void makeSet(T data) {
            if (!map.containsKey(data)) {
                map.put(data, new Node<>(data));
            }
        }

        public void union(T data1, T data2) {
            Node<T> parent1 = findSet(data1);
            Node<T> parent2 = findSet(data2);

            if (parent1 != parent2) {
                if (parent1.rank >= parent2.rank) {
                    parent1.rank = (parent1.rank == parent2.rank) ? parent1.rank + 1 : parent1.rank;
                    parent2.parent = parent1;
                } else {
                    parent1.parent = parent2;
                }
            }
        }

        public Node<T> findSet(T data) {
            Node<T> node = map.get(data);
            if (node != null && node != node.parent) {
                node.parent = findSet(node.parent.data);
            }
            return node.parent;
        }

    }

    static class Graph<V, E extends Comparable<E>> {
        class Vertex {
            int value;
            DoublyLinkedList.ListNode<Vertex> listNode;
            DoublyLinkedList<Edge> adjacentEdges;
            int in;
            int out;
            boolean visited;

            public Vertex(int value) {
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

        void addVertex(int value) {
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

        boolean IlyasKhatipov_DFS(Vertex current, List<Edge> mstEdges) {
            current.visited = true;
            List<Edge> sortedEdges = new ArrayList<>();
            for (DoublyLinkedList.ListNode<Edge> edge:current.adjacentEdges){sortedEdges.add(edge.data);}
            sortedEdges.sort(Comparator.comparing(edge -> edge.label));
            for (Edge edge : sortedEdges) {
                if (!edge.to.visited) {
                    mstEdges.add(edge);
                    if (!IlyasKhatipov_DFS(edge.to, mstEdges)) {
                        return false;
                    }
                }
            }
            return true;
        }



        List<Edge> IlyasKhatipov_mst() {
            List<Edge> mstEdges = new ArrayList<>();

            ArrayList<Edge> sortedEdges = new ArrayList<>();
            for (DoublyLinkedList.ListNode<Edge> node : edges) {
                sortedEdges.add(node.data);
            }

            Collections.sort(sortedEdges, Comparator.comparing(edge -> edge.label));

            DisjointSet<Vertex> disjointSet = new DisjointSet<>();
            for (DoublyLinkedList.ListNode<Vertex> vertex : vertices) {
                disjointSet.makeSet(vertex.data);
            }

            for (Edge edge : sortedEdges) {
                Vertex fromVertex = edge.from;
                Vertex toVertex = edge.to;

                DisjointSet.Node<Vertex> fromParent = disjointSet.findSet(fromVertex).parent;
                DisjointSet.Node<Vertex> toParent = disjointSet.findSet(toVertex).parent;


                if (fromParent != toParent) {
                    mstEdges.add(edge);
                    disjointSet.union(fromParent.data, toParent.data);
                }
            }
            return mstEdges;
        }
    }
}
