// Ilyas
import java.util.Scanner;

public class Main {
    static class Point {
        int x, y;
    }

    static class Segment {
        Point start, end;
    }

    static class Node {
        Segment data;
        Node left, right;
        int height;

        Node(Segment data) {
            this.data = data;
            this.left = null;
            this.right = null;
            this.height = 1;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        Segment[] segments = new Segment[N];
        for (int i = 0; i < N; ++i) {
            segments[i] = new Segment();
            segments[i].start = new Point();
            segments[i].end = new Point();
            segments[i].start.x = scanner.nextInt();
            segments[i].start.y = scanner.nextInt();
            segments[i].end.x = scanner.nextInt();
            segments[i].end.y = scanner.nextInt();
        }

        sort(segments, 0, N - 1);

        Node root = null;
        for (int i = 0; i < N; ++i) {
            root = insert(root, segments[i]);
        }

        boolean intersections = detectIntersections(root, segments);
        if (!intersections)
            System.out.println("NO INTERSECTIONS");
    }

    static Node newNode(Segment data) {
        return new Node(data);
    }

    static int max(int a, int b) {
        return Math.max(a, b);
    }

    static int height(Node node) {
        return (node == null) ? 0 : node.height;
    }

    static int getBalance(Node node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    static Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;
        x.right = y;
        y.left = T2;
        y.height = max(height(y.left), height(y.right)) + 1;
        x.height = max(height(x.left), height(x.right)) + 1;
        return x;
    }

    static Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;
        y.left = x;
        x.right = T2;
        x.height = max(height(x.left), height(x.right)) + 1;
        y.height = max(height(y.left), height(y.right)) + 1;
        return y;
    }

    static Node insert(Node node, Segment data) {
        if (node == null)
            return newNode(data);

        if (Math.min(data.start.x, data.end.x) < Math.min(node.data.start.x, node.data.end.x))
            node.left = insert(node.left, data);
        else
            node.right = insert(node.right, data);

        node.height = 1 + max(height(node.left), height(node.right));
        int balance = getBalance(node);

        if (balance > 1 && Math.min(data.start.x, data.end.x) < Math.min(node.left.data.start.x, node.left.data.end.x))
            return rightRotate(node);
        if (balance < -1 && Math.min(data.start.x, data.end.x) > Math.min(node.right.data.start.x, node.right.data.end.x))
            return leftRotate(node);
        if (balance > 1 && Math.min(data.start.x, data.end.x) > Math.min(node.left.data.start.x, node.left.data.end.x)) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        if (balance < -1 && Math.min(data.start.x, data.end.x) < Math.min(node.right.data.start.x, node.right.data.end.x)) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    static boolean doIntersect(Segment s1, Segment s2) {
        int o1 = orientation(s1.start, s1.end, s2.start);
        int o2 = orientation(s1.start, s1.end, s2.end);
        int o3 = orientation(s2.start, s2.end, s1.start);
        int o4 = orientation(s2.start, s2.end, s1.end);

        if (o1 != o2 && o3 != o4)
            return true;

        if (o1 == 0 && onSegment(s1.start, s1.end, s2.start))
            return true;

        if (o2 == 0 && onSegment(s1.start, s1.end, s2.end))
            return true;

        if (o3 == 0 && onSegment(s2.start, s2.end, s1.start))
            return true;

        if (o4 == 0 && onSegment(s2.start, s2.end, s1.end))
            return true;

        return false;
    }

    static boolean onSegment(Point p, Point q, Point r) {
        return q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) &&
               q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y);
    }

    static int orientation(Point p, Point q, Point r) {
        int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
        if (val == 0) return 0;
        return (val > 0) ? 1 : 2;
    }

    static boolean detectIntersections(Node root, Segment[] segments) {
        int n = segments.length;
        for (int i = 0; i < n - 1; ++i) {
            for (int j = i + 1; j < n; ++j) {
                if (doIntersect(segments[i], segments[j])) {
                    System.out.println("INTERSECTION");
                    System.out.println(segments[i].start.x + " " + segments[i].start.y + " "
                            + segments[i].end.x + " " + segments[i].end.y);
                    System.out.println(segments[j].start.x + " " + segments[j].start.y + " "
                            + segments[j].end.x + " " + segments[j].end.y);
                    return true;
                }
            }
        }
        return false;
    }

    static void sort(Segment[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            sort(arr, low, pi - 1);
            sort(arr, pi + 1, high);
        }
    }

    static int partition(Segment[] arr, int low, int high) {
        Segment pivot = arr[high];
        int i = low - 1;
        for (int j = low; j <= high - 1; j++) {
            if (Math.min(arr[j].start.x, arr[j].end.x) < Math.min(pivot.start.x, pivot.end.x)) {
                i++;
                Segment temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        Segment temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        return i + 1;
    }
}
