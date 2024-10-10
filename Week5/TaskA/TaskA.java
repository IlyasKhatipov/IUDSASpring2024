// Ilyas
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int k = scanner.nextInt();
        scanner.nextLine();
        String[] words = scanner.nextLine().split(" ");
        String text = scanner.nextLine();

        List<String> dictionary = Arrays.asList(words);
        List<String> result = restoreSpaces(dictionary, text);

        for (String word : result) {
            System.out.print(word + " ");
        }
    }

    public static List<String> restoreSpaces(List<String> dictionary, String text) {
        int n = text.length();
        int[] dp = new int[n + 1];
        Arrays.fill(dp, -1);

        dp[n] = 0; 

        return restoreSpacesHelper(dictionary, text, 0, dp);
    }

    private static List<String> restoreSpacesHelper(List<String> dictionary, String text, int start, int[] dp) {
        if (start == text.length()) {
            return new ArrayList<>();
        }

        if (dp[start] != -1) {
            return null;
        }

        List<String> result = null;
        for (String word : dictionary) {
            if (text.startsWith(word, start)) {
                List<String> suffixList = restoreSpacesHelper(dictionary, text, start + word.length(), dp);
                if (suffixList != null) {
                    suffixList.add(0, word);
                    if (result == null || suffixList.size() < result.size()) {
                        result = new ArrayList<>(suffixList);
                    }
                }
            }
        }

        dp[start] = (result == null) ? Integer.MAX_VALUE : result.size();
        return result;
    }
}
