import java.io.*;
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

    private static List<Record> readDataset(String filename) throws IOException {
        List<Record> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2);
                data.add(new Record(Integer.parseInt(parts[0]), parts[1]));
            }
        }
        return data;
    }

    private static void writeSortedFile(String filename, List<Record> data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Record r : data) {
                writer.write(r.number + "," + r.word);
                writer.newLine();
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter dataset filename to sort (e.g., dataset_100000.csv): ");
        String inputFilename = scanner.nextLine();
        
        // Generate output filename based on input
        String outputFilename = "sorted_" + inputFilename;

        try {
            List<Record> data = readDataset(inputFilename);
            
            long start = System.nanoTime();
            quickSort(data);
            long end = System.nanoTime();
            
            writeSortedFile(outputFilename, data);
            
            System.out.printf("Sorted %d records in %.3f ms\n", 
                           data.size(), (end - start)/1_000_000.0);
            System.out.println("Sorted output saved to: " + outputFilename);
            
        } catch (IOException e) {
            System.err.println("Error processing file: " + e.getMessage());
        }
    }
}
