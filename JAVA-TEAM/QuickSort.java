import java.util.*;

public class QuickSort {

    static class Record {
        int number;
        String word;

        Record(int number, String word) {
            this.number = number;
            this.word = word;
        }
    }

    public static void quickSort(List<Record> arr) {
        quickSortHelper(arr, 0, arr.size() - 1);
    }

    private static void quickSortHelper(List<Record> arr, int start, int end) {
        if (start < end) {
            int pivotIndex = partition(arr, start, end);
            quickSortHelper(arr, start, pivotIndex - 1);
            quickSortHelper(arr, pivotIndex + 1, end);
        }
    }

    private static int partition(List<Record> arr, int start, int end) {
        Record pivot = arr.get(end);
        int i = start - 1;

        for (int j = start; j < end; j++) {
            if (arr.get(j).number <= pivot.number) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, end);
        return i + 1;
    }

    private static void swap(List<Record> arr, int a, int b) {
        Record temp = arr.get(a);
        arr.set(a, arr.get(b));
        arr.set(b, temp);
    }

    private static List<Record> generateTestData(int n) {
        Random rand = new Random();
        List<Record> records = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int num = 1_000_000_000 + rand.nextInt(1_147_483_647);
            String word = randomWord(rand, 5 + rand.nextInt(3));
            records.add(new Record(num, word));
        }
        return records;
    }

    private static String randomWord(Random rand, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + rand.nextInt(26)));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        int[] sizes = {
            100_000,        // 100 thousand
            200_000, 
            500_000, 
            1_000_000,      // 1 million
            2_000_000, 
            5_000_000, 
            10_000_000,     // 10 million
            20_000_000, 
            50_000_000, 
            100_000_000     // 100 million
        };

        for (int n : sizes) {
            // Generate test data in memory
            List<Record> data = generateTestData(n);

            // Quick sort and measure time
            long start = System.nanoTime();
            quickSort(data);
            long end = System.nanoTime();

            // Print running time in milliseconds
            double timeMs = (end - start) / 1_000_000.0;
            System.out.printf("QuickSort - Dataset Size: %d -> Time: %.3f ms\n", n, timeMs);
        }
    }
}