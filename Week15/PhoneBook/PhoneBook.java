// Ilyas
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine());
        CustomHashMap<String, String> phoneBook = new CustomHashMap<>(10000);
        ArrayList<String> results = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] tokens = line.split(" ", 2);
            String command = tokens[0];
            String details = tokens[1];
            switch (command) {
                case "ADD":
                    String[] parts = details.split(",", 2);
                    String name = parts[0];
                    String phone = parts[1];
                    phoneBook.put(name, phone);
                    break;
                case "DELETE":
                    String[] info = details.split(",", 2);
                    if (info.length > 1) {
                        String id = info[0];
                        String num = info[1];
                        phoneBook.removeValue(id, num);
                    } else {
                        phoneBook.removeEntry(details);
                    }
                    break;
                case "FIND":
                    ArrayList<String> phones = phoneBook.get(details);
                    if (phones.isEmpty()) {
                        results.add("No contact info found for " + details);
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Found ").append(phones.size()).append(" phone numbers for ").append(details).append(": ");
                        for (String number : phones) {
                            sb.append(number).append(" ");
                        }
                        results.add(sb.toString());
                    }
                    break;
            }
        }

        for (String result : results) {
            System.out.println(result);
        }
        scanner.close();
    }

    interface Map<K, V> {
        int size();
        void put(K key, V value);
        ArrayList<V> get(K key);
        void removeEntry(K key);
        void removeValue(K key, V value);
    }
    static class CustomHashMap<K, V> implements Map<K, V> {
        static class Entry<K, V> {
            public K key;
            public V value;
            public Entry(K key, V value) {
                this.key = key;
                this.value = value;
            }
        }
        private final ArrayList<ArrayList<Entry<K, ArrayList<V>>>> content;
        private final int capacity;
        private int elemsAmount;

        public CustomHashMap(int num) {
            capacity = num;
            elemsAmount = 0;
            content = new ArrayList<>(capacity);
            for (int i = 0; i < capacity; i++) {
                content.add(new ArrayList<>());
            }
        }

        @Override
        public int size() {
            return elemsAmount;
        }

        @Override
        public void put(K key, V value) {
            int index = Math.abs(key.hashCode() % capacity);
            ArrayList<Entry<K, ArrayList<V>>> container = content.get(index);
            for (Entry<K, ArrayList<V>> kv : container) {
                if (kv.key.equals(key)) {
                    if (!kv.value.contains(value)) {
                        kv.value.add(value);
                    }
                    return;
                }
            }
            ArrayList<V> newValueList = new ArrayList<>();
            newValueList.add(value);
            container.add(new Entry<>(key, newValueList));
            elemsAmount++;
        }

        @Override
        public ArrayList<V> get(K key) {
            int index = Math.abs(key.hashCode() % capacity);
            for (Entry<K, ArrayList<V>> kv : content.get(index)) {
                if (kv.key.equals(key)) {
                    return kv.value;
                }
            }
            return new ArrayList<>();
        }

        @Override
        public void removeEntry(K key) {
            int index = Math.abs(key.hashCode() % capacity);
            ArrayList<Entry<K, ArrayList<V>>> container = content.get(index);
            for (int i = 0; i < container.size(); i++) {
                if (container.get(i).key.equals(key)) {
                    container.remove(i);
                    elemsAmount--;
                }
            }
        }

        @Override
        public void removeValue(K key, V value) {
            int index = Math.abs(key.hashCode() % capacity);
            ArrayList<Entry<K, ArrayList<V>>> container = content.get(index);
            for (Entry<K, ArrayList<V>> kv : container) {
                if (kv.key.equals(key)) {
                    ArrayList<V> values = kv.value;
                    if (values.remove(value) && values.isEmpty()) {
                        container.remove(kv);
                        elemsAmount--;
                        return;
                    }
                    break;
                }
            }
        }
    }
}
