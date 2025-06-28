import java.io.*;
import java.util.*;

public class QuickSortStep {
    
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

    private static void writeStep(List<Record> data, BufferedWriter writer) throws IOException {
        for (Record item : data) {
            writer.write(item.number + "," + item.word + " ");
        }
        writer.newLine();
    }

    private static int partition(List<Record> arr, int low, int high, BufferedWriter writer) throws IOException {
        Record pivot = arr.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr.get(j).number <= pivot.number) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);

        writeStep(arr.subList(low, high + 1), writer);
        return i + 1;
    }

    private static void swap(List<Record> arr, int a, int b) {
        Record temp = arr.get(a);
        arr.set(a, arr.get(b));
        arr.set(b, temp);
    }

    private static void quickSort(List<Record> arr, int low, int high, BufferedWriter writer) throws IOException {
        if (low < high) {
            int pi = partition(arr, low, high, writer);
            quickSort(arr, low, pi - 1, writer);
            quickSort(arr, pi + 1, high, writer);
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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter input filename (e.g., dataset_sample_1000.csv): ");
        String inputFile = scanner.next();
        System.out.print("Enter start row (1-based): ");
        int startRow = scanner.nextInt();
        System.out.print("Enter end row (1-based): ");
        int endRow = scanner.nextInt();

        List<Record> data;
        try {
            data = readCSV(inputFile);
            // Adjust for 1-based to 0-based index
            List<Record> subList = data.subList(startRow-1, endRow);
            
            String outFileName = "quick_sort_step_" + startRow + "_" + endRow + ".txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(outFileName));
            
            writer.write("Initial data:\n");
            writeStep(subList, writer);

            quickSort(subList, 0, subList.size()-1, writer);

            writer.write("Final sorted data:\n");
            writeStep(subList, writer);
            
            writer.close();
            System.out.println("Quick sort steps saved to " + outFileName);
        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
        }
    }
}