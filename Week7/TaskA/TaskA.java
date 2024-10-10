// Ilyas
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        AVLTree<Integer> tree = new AVLTree<>();
        for (int i = 0; i < n; i++) {
            int key = scanner.nextInt();
            tree.root = tree.paste(tree.root, key);
        }
        HashMap<Integer, Integer> keys = new HashMap<>();
        tree.printNodes(tree.root, keys);
        ArrayList<ArrayList<Integer>> array = new ArrayList<>();
        tree.printNodes(tree.root, array);
        for (int i = 0; i < n; i++) {
            array.get(i).set(1, keys.getOrDefault(array.get(i).get(1), -1));
            array.get(i).set(2, keys.getOrDefault(array.get(i).get(2), -1));
        }
        System.out.println(n);
        for (ArrayList<Integer> L : array) {
            StringBuilder st = new StringBuilder();
            for (Integer num : L) {
                String sym = num != null ? num.toString() : "-1";
                st.append(sym).append(" ");
            }
            System.out.println(st);
        }
        System.out.println(1);
    }

    static class AVLNode<K extends Comparable<K>> {
        K key;
        int h;
        static int ind = 1;
        int id;
        AVLNode<K> less, more;

        AVLNode(K key) {
            this.key = key;
            this.h = 1;
            this.id = ind++;
        }
    }

    static class AVLTree<K extends Comparable<K>> {
        AVLNode<K> root;

        int h(AVLNode<K> node) {
            if (node == null)
                return 0;
            return node.h;
        }

        int balanceFactor(AVLNode<K> node) {
            if (node == null)
                return 0;
            return h(node.less) - h(node.more);
        }

        AVLNode<K> rotateRight(AVLNode<K> initial) {
            AVLNode<K> less = initial.less;
            AVLNode<K> lm = less.more;
            less.more = initial;
            initial.less = lm;
            initial.h = Math.max(h(initial.less), h(initial.more)) + 1;
            less.h = Math.max(h(less.less), h(less.more)) + 1;
            return less;
        }

        AVLNode<K> rotateLeft(AVLNode<K> initial) {
            AVLNode<K> more = initial.more;
            AVLNode<K> ml = more.less;
            more.less = initial;
            initial.more = ml;
            initial.h = Math.max(h(initial.less), h(initial.more)) + 1;
            more.h = Math.max(h(more.less), h(more.more)) + 1;
            return more;
        }

        AVLNode<K> paste(AVLNode<K> node, K key) {
            if (node == null) {
                return new AVLNode<>(key);
            }
            if (key.compareTo(node.key) < 0) {
                node.less = paste(node.less, key);
            } else if (key.compareTo(node.key) > 0) {
                node.more = paste(node.more, key);
            } else {
                return node;
            }
            node.h = 1 + Math.max(h(node.less), h(node.more));
            int balance = balanceFactor(node);
            if (balance > 1) {
                if (key.compareTo(node.less.key) < 0) {
                    return rotateRight(node);
                } else if (key.compareTo(node.less.key) > 0) {
                    node.less = rotateLeft(node.less);
                    return rotateRight(node);
                }
            }
            if (balance < -1) {
                if (key.compareTo(node.more.key) > 0) {
                    return rotateLeft(node);
                } else if (key.compareTo(node.more.key) < 0) {
                    node.more = rotateRight(node.more);
                    return rotateLeft(node);
                }
            }
            return node;
        }

        void printNodes(AVLNode<K> node, HashMap<K, Integer> keys) {
            if (node != null) {
                keys.put(node.key, keys.size() + 1);
                printNodes(node.less, keys);
                printNodes(node.more, keys);
            }
        }

        void printNodes(AVLNode<K> node, ArrayList<ArrayList<K>> array) {
            if (node != null) {
                ArrayList<K> list = new ArrayList<>();
                list.add(node.key);
                list.add(node.less != null ? node.less.key : null);
                list.add(node.more != null ? node.more.key : null);
                array.add(list);
                printNodes(node.less, array);
                printNodes(node.more, array);
            }
        }
    }

    static class HashMap<K, V> {
        private static final int DEFAULT_CAPACITY = 16;
        private static final double DEFAULT_LOAD_FACTOR = 0.75;
        private Entry<K, V>[] table;
        private int size;
        private int threshold;
        private final double loadFactor;

        HashMap() {
            this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
        }

        HashMap(int initialCapacity) {
            this(initialCapacity, DEFAULT_LOAD_FACTOR);
        }

        HashMap(int initialCapacity, double loadFactor) {
            if (initialCapacity < 0)
                throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
            if (loadFactor <= 0 || Double.isNaN(loadFactor))
                throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
            this.loadFactor = loadFactor;
            this.threshold = (int) (initialCapacity * loadFactor);
            this.table = new Entry[initialCapacity];
        }

        void put(K key, V value) {
            if (key == null)
                return;

            int hash = hash(key.hashCode());
            int index = indexFor(hash, table.length);

            for (Entry<K, V> e = table[index]; e != null; e = e.next) {
                if (e.hash == hash && (e.key == key || key.equals(e.key))) {
                    e.value = value;
                    return;
                }
            }

            addEntry(hash, key, value, index);
        }

        V getOrDefault(K key, V defaultValue) {
            if (key == null)
                return defaultValue;

            int hash = hash(key.hashCode());
            int index = indexFor(hash, table.length);

            for (Entry<K, V> e = table[index]; e != null; e = e.next) {
                if (e.hash == hash && (e.key == key || key.equals(e.key))) {
                    return e.value;
                }
            }

            return defaultValue;
        }

        private void addEntry(int hash, K key, V value, int bucketIndex) {
            Entry<K, V> e = table[bucketIndex];
            table[bucketIndex] = new Entry<>(hash, key, value, e);
            if (size++ >= threshold)
                resize(2 * table.length);
        }

        private void resize(int newCapacity) {
            Entry<K, V>[] oldTable = table;
            int oldCapacity = oldTable.length;
            if (oldCapacity == 1 << 30) {
                threshold = Integer.MAX_VALUE;
                return;
            }

            Entry<K, V>[] newTable = new Entry[newCapacity];
            transfer(newTable);
            table = newTable;
            threshold = (int) (newCapacity * loadFactor);
        }

        private void transfer(Entry<K, V>[] newTable) {
            Entry<K, V>[] src = table;
            int newCapacity = newTable.length;
            for (Entry<K, V> e : src) {
                while (e != null) {
                    Entry<K, V> next = e.next;
                    int index = indexFor(e.hash, newCapacity);
                    e.next = newTable[index];
                    newTable[index] = e;
                    e = next;
                }
            }
        }

        private int indexFor(int h, int length) {
            return h & (length - 1);
        }

        private int hash(int h) {
            h ^= (h >>> 20) ^ (h >>> 12);
            return h ^ (h >>> 7) ^ (h >>> 4);
        }

        private int size(){return this.size;}

        static class Entry<K, V> {
            final K key;
            V value;
            final int hash;
            Entry<K, V> next;

            Entry(int hash, K key, V value, Entry<K, V> next) {
                this.value = value;
                this.hash = hash;
                this.key = key;
                this.next = next;
            }
        }
    }
}
