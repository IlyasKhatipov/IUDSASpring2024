//Ilyas
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine());

        HashMap<Integer, String> hashMap = new HashMap<>(n);

        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int key = Integer.parseInt(parts[0]);
            String value = parts[1] + " " + parts[2];
            hashMap.put(key, value);
        }

        ArrayList<Integer> data = hashMap.getKeySet();

        int k = n / 2;
        int medianScore = median_of_medians(data, k);

        System.out.println(hashMap.get(medianScore));
    }
    static int median_of_medians(ArrayList<Integer> arr, int k) {
        if (arr.size() <= 5) {
            return insertionSortAndGetMedian(arr);
        }

        ArrayList<Integer> medians = new ArrayList<>();
        for (int i = 0; i < arr.size(); i += 5) {
            ArrayList<Integer> chunk = new ArrayList<>(arr.subList(i, Math.min(i + 5, arr.size())));
            medians.add(insertionSortAndGetMedian(chunk));
        }

        int pivot = median_of_medians(medians, medians.size() / 2);

        ArrayList<Integer> lesser = new ArrayList<>();
        ArrayList<Integer> equal = new ArrayList<>();
        ArrayList<Integer> greater = new ArrayList<>();

        for (int x : arr) {
            if (x < pivot) {
                lesser.add(x);
            } else if (x == pivot) {
                equal.add(x);
            } else {
                greater.add(x);
            }
        }

        if (lesser.size() <= k && (lesser.size() + equal.size()) > k) {
            return pivot;
        } else if (lesser.size() < k) {
            return median_of_medians(greater, k - lesser.size() - equal.size());
        } else {
            return median_of_medians(lesser, k);
        }
    }

    static int insertionSortAndGetMedian(ArrayList<Integer> arr) {
        for (int i = 1; i < arr.size(); ++i) {
            int key = arr.get(i);
            int j = i - 1;
            while (j >= 0 && arr.get(j) > key) {
                arr.set(j + 1, arr.get(j));
                j--;
            }
            arr.set(j + 1, key);
        }
        return arr.get(arr.size() / 2);
    }

    static class KeyValue<K, V> {
        K key;
        V value;

        KeyValue(K k, V v) {
            key = k;
            value = v;
        }
    }

    interface Map<K, V> {
        int getSize();
        void put(K key, V value);
        V get(K key);
        ArrayList<KeyValue<K, V>> elemSet();
        ArrayList<K> getKeySet();
    }

    static class HashMap<K, V> implements Map<K, V> {
        private List<LinkedList<KeyValue<K, V>>> hashTable;
        private int capacity;
        private int mapSize;

        HashMap(int cap) {
            capacity = cap;
            mapSize = 0;
            hashTable = new ArrayList<>(capacity);
            for (int i = 0; i < capacity; i++) {
                hashTable.add(new LinkedList<>());
            }
        }

        public int getSize() {
            return mapSize;
        }

        public void put(K key, V value) {
            int index = key.hashCode() % capacity;
            LinkedList<KeyValue<K, V>> bucket = hashTable.get(index);
            for (KeyValue<K, V> kv : bucket) {
                if (kv.key.equals(key)) {
                    kv.value = value;
                    return;
                }
            }
            bucket.add(new KeyValue<>(key, value));
            mapSize++;
        }

        public V get(K key) {
            int index = key.hashCode() % capacity;
            LinkedList<KeyValue<K, V>> bucket = hashTable.get(index);
            for (KeyValue<K, V> kv : bucket) {
                if (kv.key.equals(key)) {
                    return kv.value;
                }
            }
            return null;
        }

        public ArrayList<KeyValue<K, V>> elemSet() {
            ArrayList<KeyValue<K, V>> elems = new ArrayList<>();
            for (LinkedList<KeyValue<K, V>> bucket : hashTable) {
                elems.addAll(bucket);
            }
            return elems;
        }

        public ArrayList<K> getKeySet() {
            ArrayList<K> keys = new ArrayList<>();
            for (LinkedList<KeyValue<K, V>> bucket : hashTable) {
                for (KeyValue<K, V> kv : bucket) {
                    keys.add(kv.key);
                }
            }
            return keys;
        }
    }

}
