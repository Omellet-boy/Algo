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

public class QuickSortStep {
    
    private static void printStep(ArrayList<Data> data, PrintWriter out) {
        for (Data item : data) {
            out.print(item.number + "," + item.text + " ");
        }
        out.println();
    }

    private static int partition(ArrayList<Data> arr, int low, int high, PrintWriter out) {
        int pivot = arr.get(high).number;
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr.get(j).number <= pivot) {
                i++;
                Data temp = arr.get(i);
                arr.set(i, arr.get(j));
                arr.set(j, temp);
            }
        }
        Data temp = arr.get(i + 1);
        arr.set(i + 1, arr.get(high));
        arr.set(high, temp);

        printStep(arr, out); // Log step after partitioning
        return i + 1;
    }

    private static void quickSort(ArrayList<Data> arr, int low, int high, PrintWriter out) {
        if (low < high) {
            int pi = partition(arr, low, high, out);
            quickSort(arr, low, pi - 1, out);
            quickSort(arr, pi + 1, high, out);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter start row (1-based): ");
        int startRow = scanner.nextInt();
        System.out.print("Enter end row (1-based): ");
        int endRow = scanner.nextInt();

        ArrayList<Data> data = new ArrayList<>();
        try (BufferedReader inFile = new BufferedReader(new FileReader("generated_dataset.csv"))) {
            String line;
            int row = 0;
            
            while ((line = inFile.readLine()) != null) {
                row++;
                if (row < startRow) continue;
                if (row > endRow) break;

                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    data.add(new Data(Integer.parseInt(parts[0]), parts[1]));
                }
            }
        } catch (IOException e) {
            System.err.println("Cannot open input file: " + e.getMessage());
            return;
        }

        String outFileName = "quick_sort_step_" + startRow + "_" + endRow + ".txt";
        try (PrintWriter outFile = new PrintWriter(new FileWriter(outFileName))) {
            outFile.println("Initial data:");
            printStep(data, outFile);

            quickSort(data, 0, data.size() - 1, outFile);

            outFile.println("Final sorted data:");
            printStep(data, outFile);
            
            System.out.println("Quick sort steps saved to " + outFileName);
        } catch (IOException e) {
            System.err.println("Cannot open output file: " + e.getMessage());
        }
    }
}