//Ilyas
import java.util.*;

public class Main {
    public static int count(String s, String[] arr) {
        int counter = 0;
        for (int i = 0; i < arr.length; i++) {
            if (Objects.equals(arr[i], s)) {
                counter++;
            }
        }
        return counter;
    }

    static interface Map<K extends Comparable<K>, V extends Comparable<V>> {
        int size();

        boolean isEmpty();

        void put(K key, V value);

        void remove(K key);

        V get(K key);
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

        public List<K> keys() {
            List<K> keysList = new LinkedList<>();
            for (List<Entry<K, V>> bucket : hashTable) {
                for (Entry<K, V> entry : bucket) {
                    keysList.add(entry.key);
                }
            }
            return keysList;
        }

        private void clear() {
            for (List<Entry<K, V>> bucket : hashTable) {
                bucket.clear();
            }
            size = 0;
        }

        public List<Entry<K, V>> elemSet() {
            List<Entry<K, V>> elemList = new ArrayList<>();
            for (List<Entry<K, V>> bucket : hashTable) {
                elemList.addAll(bucket);
            }
            return elemList;
        }

        public void sortByValues() {
            List<Entry<K, V>> elems = elemSet();
            int num = elems.size();

            for (int i = 0; i < num; i++) {
                Entry<K, V> elem = elems.get(i);
                int j = i - 1;
                while (j >= 0 && (elems.get(j).value.compareTo(elem.value) < 0 || (elems.get(j).value.compareTo(elem.value) == 0 && elems.get(j).key.compareTo(elem.key) > 0))) {
                    elems.set(j + 1, elems.get(j));
                    j--;
                }
                elems.set(j + 1, elem);
            }
            for (int i = 0;i < num;i++){
                System.out.println(elems.get(i).key + " " + elems.get(i).value);
            }
        }
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int n = s.nextInt();
        s.nextLine();
        String text = s.nextLine();
        String[] words = text.split(" ");
        HashMap<String, Integer> hashMap = new HashMap<>(n);
        for (int i = 0; i < n; i++) {
            hashMap.put(words[i], count(words[i], words));
        }
        hashMap.sortByValues();
    }
}
