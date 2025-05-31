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

  // QuickSort with step recording
  public static void quickSortWithSteps(List<Record> records, int start, int end, String stepFile) throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter(stepFile));
    quickSortStep(records, start, end, writer);
    writer.close();
  }

  private static void quickSortStep(List<Record> arr, int start, int end, BufferedWriter writer) throws IOException {
    if (start < end) {
      int pivotIndex = partition(arr, start, end, writer);
      quickSortStep(arr, start, pivotIndex - 1, writer);
      quickSortStep(arr, pivotIndex + 1, end, writer);
    }
  }

  private static int partition(List<Record> arr, int start, int end, BufferedWriter writer) throws IOException {
    Record pivot = arr.get(end);  // Last element as pivot
    int i = start - 1;

    for (int j = start; j < end; j++) {
      if (arr.get(j).number <= pivot.number) {
        i++;
        swap(arr, i, j);
      }
    }
    swap(arr, i + 1, end);

    // Log current state after partitioning
    writer.write(arr.subList(start, end + 1).toString());
    writer.newLine();

    return i + 1;
  }

  private static void swap(List<Record> arr, int a, int b) {
    Record temp = arr.get(a);
    arr.set(a, arr.get(b));
    arr.set(b, temp);
  }

  //benchmarking
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

  // File I/O 
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

  public static void main(String[] args) throws IOException {
    // === 1. Sort the 1000-record sample and measure time ===
    List<Record> sampleData = readCSV("dataset_sample_1000.csv");
    
    long startSample = System.nanoTime();  // Start timer for 1K dataset
    quickSort(sampleData);
    long endSample = System.nanoTime();    // End timer
    
    writeCSV("quick_sort_1000.csv", sampleData);
    System.out.println("QuickSort (1K records) time (ns): " + (endSample - startSample));

    // === 2.Benchmark with 1M records ===
    generateDataset(1000000, "dataset_1000000.csv");
    List<Record> largeData = readCSV("dataset_1000000.csv");
    
    long startLarge = System.nanoTime();
    quickSort(largeData);
    long endLarge = System.nanoTime();
    
    writeCSV("quick_sort_1000000.csv", largeData);
    System.out.println("QuickSort (1M records) time (ns): " + (endLarge - startLarge));

    // === 3. Log steps for a small subset ===
    quickSortWithSteps(sampleData.subList(0, 7), 0, 6, "quick_sort_step_1_7.txt");
  }
}