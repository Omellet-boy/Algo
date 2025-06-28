import java.io.*;
import java.util.*;

public class MergeSortSteps {

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

    private static void merge(List<Record> arr, int left, int mid, int right, BufferedWriter writer) throws IOException {
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
        while (i < leftList.size()) {
            arr.set(k++, leftList.get(i++));
        }
        while (j < rightList.size()) {
            arr.set(k++, rightList.get(j++));
        }

        // log the merged segment
        writeStep(arr.subList(left, right + 1), writer);
    }

    private static void mergeSort(List<Record> arr, int left, int right, BufferedWriter writer) throws IOException {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(arr, left, mid, writer);
            mergeSort(arr, mid + 1, right, writer);
            merge(arr, left, mid, right, writer);
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

        try {
            List<Record> data = readCSV(inputFile);
            List<Record> subList = data.subList(startRow - 1, endRow);

            String outFileName = "merge_sort_step_" + startRow + "_" + endRow + ".txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(outFileName));

            writer.write("Initial data:\n");
            writeStep(subList, writer);

            mergeSort(subList, 0, subList.size() - 1, writer);

            writer.write("Final sorted data:\n");
            writeStep(subList, writer);

            writer.close();

            System.out.println("Merge sort steps saved to " + outFileName);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
