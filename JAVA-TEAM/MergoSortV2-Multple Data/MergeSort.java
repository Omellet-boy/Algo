import java.io.*;
import java.util.*;

public class MergeSort {

    static class Record {
        int number;
        String word;

        Record(int number, String word) {
            this.number = number;
            this.word = word;
        }

        @Override
        public String toString() {
            return number + "," + word;
        }
    }

    // Dataset Generator
    public static void generateDataset(int n, String filename) throws IOException {
        Random rand = new Random();
        Set<Integer> uniqueNumbers = new HashSet<>();
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

        while (uniqueNumbers.size() < n) {
            int num = rand.nextInt(1_000_000_000);
            if (uniqueNumbers.add(num)) {
                String word = randomWord(rand, 5 + rand.nextInt(3));
                writer.write(num + "," + word);
                writer.newLine();
            }
        }
        writer.close();
    }

    private static String randomWord(Random rand, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + rand.nextInt(26)));
        }
        return sb.toString();
    }

    // Merge Sort with step recording
    public static void mergeSortWithSteps(List<Record> records, int start, int end, String stepFile) throws IOException {
        List<Record> subList = new ArrayList<>(records.subList(start, end + 1));
        BufferedWriter writer = new BufferedWriter(new FileWriter(stepFile));
        mergeSortStep(subList, 0, subList.size() - 1, writer);
        writer.close();
    }

    private static void mergeSortStep(List<Record> arr, int left, int right, BufferedWriter writer) throws IOException {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSortStep(arr, left, mid, writer);
            mergeSortStep(arr, mid + 1, right, writer);
            mergeStep(arr, left, mid, right);
            writer.write(arr.toString());
            writer.newLine();
        }
    }

    private static void mergeStep(List<Record> arr, int left, int mid, int right) {
        List<Record> leftList = new ArrayList<>(arr.subList(left, mid + 1));
        List<Record> rightList = new ArrayList<>(arr.subList(mid + 1, right + 1));

        int i = 0, j = 0, k = left;
        while (i < leftList.size() && j < rightList.size()) {
            if (leftList.get(i).number <= rightList.get(j).number) {
                arr.set(k++, leftList.get(i++));
            } else {
                arr.set(k++, rightList.get(j++));
            }
        }
        while (i < leftList.size()) arr.set(k++, leftList.get(i++));
        while (j < rightList.size()) arr.set(k++, rightList.get(j++));
    }

    // Merge Sort (final version for benchmarking)
    public static void mergeSort(List<Record> arr) {
        mergeSortHelper(arr, 0, arr.size() - 1);
    }

    private static void mergeSortHelper(List<Record> arr, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSortHelper(arr, left, mid);
            mergeSortHelper(arr, mid + 1, right);
            mergeStep(arr, left, mid, right);
        }
    }

    public static List<Record> readCSV(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        List<Record> records = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            records.add(new Record(Integer.parseInt(parts[0]), parts[1]));
        }
        reader.close();
        return records;
    }

    public static void writeCSV(String filename, List<Record> records) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for (Record r : records) {
            writer.write(r.toString());
            writer.newLine();
        }
        writer.close();
    }

    /*public static void main(String[] args) throws IOException {
        // Example usage for demo/testing

        // Step 1: Generate dataset
        generateDataset(1000000, "dataset_1000000.csv");

        // Step 2: Read dataset
        List<Record> data = readCSV("dataset_1000000.csv");

        // Step 3: Merge sort and measure time
        long start = System.nanoTime();
        mergeSort(data);
        long end = System.nanoTime();

        // Step 4: Write sorted data
        writeCSV("merge_sort_1000000.csv", data);

        // Step 5: Print running time
        System.out.println("Running time (ns): " + (end - start));

        // Step 6: Run merge sort step demo
        List<Record> stepData = readCSV("dataset_sample_1000.csv");
        mergeSortWithSteps(stepData, 0, 6, "merge_sort_step_1_7.txt");
    } */
    public static void main(String[] args) throws IOException {
    int[] sizes = {
        1000, 2000, 5000,
        10000, 20000, 50000,
        100000, 200000, 500000, 1000000
    };

    for (int n : sizes) {
        String inputFile = "dataset_" + n + ".csv";
        String outputFile = "merge_sort_" + n + ".csv";

        // Step 1: Generate dataset
        generateDataset(n, inputFile);

        // Step 2: Read dataset
        List<Record> data = readCSV(inputFile);

        // Step 3: Merge sort and measure time
        long start = System.nanoTime();
        mergeSort(data);
        long end = System.nanoTime();

        // Step 4: Write sorted data
        writeCSV(outputFile, data);

        // Step 5: Print running time
        long timeNs = end - start;
        double timeMs = timeNs / 1_000_000.0;
        System.out.printf("Dataset Size: %d -> Time: %.3f ms\n", n, timeMs);
    }

    // Optional Step 6: Demo merge sort steps on sample dataset
    List<Record> stepData = readCSV("dataset_sample_1000.csv");
    mergeSortWithSteps(stepData, 0, 6, "merge_sort_step_1_7.txt");
}

} 

