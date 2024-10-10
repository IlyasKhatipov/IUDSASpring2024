// Ilyas

import java.util.*;

public class Main {

    interface Map<K , V> {
        int size();
        boolean isEmpty();
        void put(K key, V value);
        void remove(K key);
        V get(K key);
        V getOrDefault(K key, V defaultValue);
    }

    static class Entry<K extends Comparable<K>, V extends Comparable<V>> {
        K key;
        V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        void setValue(V value) {
            this.value = value;
        }
    }

    static class HashMap<K extends Comparable<K>, V extends Comparable<V>> implements Map<K, V> {
        int size;
        int capacity;
        List<Entry<K, V>>[] hashTable;

        public HashMap(int capacity) {
            this.size = 0;
            this.capacity = capacity;
            this.hashTable = new List[capacity];
            for (int i = 0; i < capacity; i++) {
                hashTable[i] = new LinkedList<>();
            }
        }

        private Entry<K, V> getEntry(K key) {
            int hash = Math.abs(key.hashCode()) % capacity;
            for (Entry<K, V> entry : hashTable[hash]) {
                if (entry.key.equals(key)) {
                    return entry;
                }
            }
            return null;
        }

        @Override
        public int size() {
            return this.size;
        }

        @Override
        public boolean isEmpty() {
            return this.size <= 0;
        }

        @Override
        public void put(K key, V value) {
            int hash = Math.abs(key.hashCode()) % capacity;
            Entry<K, V> e = getEntry(key);
            if (e != null) {
                e.setValue(value);
            } else {
                hashTable[hash].add(new Entry<>(key, value));
                this.size++;
            }
        }

        @Override
        public void remove(K key) {
            int hash = Math.abs(key.hashCode()) % capacity;
            for (Entry<K, V> entry : hashTable[hash]) {
                if (entry.key.equals(key)) {
                    hashTable[hash].remove(entry);
                    this.size--;
                    return;
                }
            }
        }

        @Override
        public V get(K key) {
            int hash = Math.abs(key.hashCode()) % capacity;
            for (Entry<K, V> entry : hashTable[hash]) {
                if (entry.key.equals(key)) {
                    return entry.value;
                }
            }
            return null;
        }

        @Override
        public V getOrDefault(K key, V defaultValue) {
            V value = get(key);
            return (value != null) ? value : defaultValue;
        }

        public List<K> keys() {
            List<K> keysList = new LinkedList<>();
            for (List<Entry<K, V>> bucket : hashTable) {
                for (Entry<K, V> entry : bucket) {
                    keysList.add(entry.key);
                }
            }
            return keysList;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        scanner.nextLine();
        Map<String, Integer> wordMap = new HashMap<>(n);
        for (int i = 0; i < n; i++) {
            String word = scanner.next();
            wordMap.put(word, wordMap.getOrDefault(word, 0) + 1);
        }

        int m = scanner.nextInt();
        scanner.nextLine();
        List<String> uniqueWords = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            String word = scanner.next();
            if (wordMap.get(word) == null) {
                uniqueWords.add(word);
                wordMap.put(word, 0);
            }
        }

        System.out.println(uniqueWords.size());
        for (String word : uniqueWords) {
            System.out.println(word);
        }
    }
}
