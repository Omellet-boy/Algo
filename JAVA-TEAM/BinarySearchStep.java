import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class Data {
    int number;
    String text;

    public Data(int number, String text) {
        this.number = number;
        this.text = text;
    }
}

public class BinarySearchStep {
    
    private static int binarySearch(ArrayList<Data> data, int target, PrintWriter logFile) {
        int low = 0;
        int high = data.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            logFile.println("Compare with value " + data.get(mid).number + 
                          " at row " + (mid + 1));

            if (data.get(mid).number == target) {
                return mid;
            } else if (data.get(mid).number < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1; // Not found
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter dataset filename (e.g., merge_sort_n.csv): ");
        String filename = scanner.next();
        System.out.print("Enter target integer to search: ");
        int target = scanner.nextInt();

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
            System.err.println("Error opening file: " + e.getMessage());
            return;
        }

        String outFilename = "binary_search_step_" + target + ".txt";
        try (PrintWriter outFile = new PrintWriter(new FileWriter(outFilename))) {
            int result = binarySearch(data, target, outFile);

            if (result != -1) {
                outFile.println("Target " + target + " found at row " + (result + 1) + ".");
            } else {
                outFile.println("Target " + target + " not found.");
            }
            
            System.out.println("Search steps written to " + outFilename);
        } catch (IOException e) {
            System.err.println("Cannot create output file: " + e.getMessage());
        }
    }
}