import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class Data {
    int number;
    String text;
    
    Data(int number, String text) {
        this.number = number;
        this.text = text;
    }
}

public class binary_search {
    
    static int binarySearch(ArrayList<Data> data, int target) {
        int low = 0, high = data.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (data.get(mid).number == target) {
                return mid;
            } else if (data.get(mid).number < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter dataset filename (e.g., merge_sort_n.csv): ");
        String filename = scanner.next();

        ArrayList<Data> data = new ArrayList<>();
        try (BufferedReader inFile = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = inFile.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    data.add(new Data(Integer.parseInt(parts[0]), parts[1]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error opening file.");
            return;
        }

        int n = data.size();
        
        // Best case
        int midIndex = n / 2;
        int bestCaseTarget = data.get(midIndex).number;
        long start = System.nanoTime();
        binarySearch(data, bestCaseTarget);
        long end = System.nanoTime();
        double bestTime = (end - start) / 1000.0;

        // Average case
        Random rng = new Random();
        double totalAvgTime = 0;
        int trials = n / 2;
        for (int i = 0; i < trials; ++i) {
            int target = data.get(rng.nextInt(n)).number;
            long startAvg = System.nanoTime();
            binarySearch(data, target);
            long endAvg = System.nanoTime();
            totalAvgTime += (endAvg - startAvg) / 1000.0;
        }
        double averageTime = totalAvgTime / trials;

        // Worst case
        int worstTarget = Integer.MAX_VALUE;
        start = System.nanoTime();
        binarySearch(data, worstTarget);
        end = System.nanoTime();
        double worstTime = (end - start) / 1000.0;

        // Output to terminal instead of file
        System.out.println("Binary Search Performance (average of microseconds):");
        System.out.println("Best Case: " + bestTime + " µs");
        System.out.println("Average Case (" + trials + " searches): " + averageTime + " µs");
        System.out.println("Worst Case: " + worstTime + " µs");
    }
}
