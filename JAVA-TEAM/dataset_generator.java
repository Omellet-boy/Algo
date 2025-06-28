import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class dataset_generator {
    static class Record {
        int number;
        String word;

        Record(int number, String word) {
            this.number = number;
            this.word = word;
        }
    }

    public static List<Record> generateTestData(int n) {
        Random rand = new Random();
        Set<Integer> usedNumbers = new HashSet<>(); // Ensure number uniqueness
        List<Record> records = new ArrayList<>();
        
        while (records.size() < n) {
            int num = 1_000_000_000 + rand.nextInt(1_147_483_647);
            if (!usedNumbers.contains(num)) {
                usedNumbers.add(num);
                String word = randomWord(rand, 5 + rand.nextInt(3));
                records.add(new Record(num, word));
            }
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

    public static void writeCSV(String filename, List<Record> records) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Record r : records) {
                writer.write(r.number + "," + r.word);
                writer.newLine();
            }
        }
    }

    public static void main(String[] args) {
        int[] sizes = {
            100_000, 200_000, 500_000,
            1_000_000, 2_000_000, 5_000_000,
            10_000_000, 20_000_000, 50_000_000, 100_000_000
        };

        for (int n : sizes) {
            System.out.print("Generating dataset_" + n + ".csv... ");
            List<Record> data = generateTestData(n);
            try {
                writeCSV("dataset_" + n + ".csv", data);
                System.out.println("Done");
            } catch (IOException e) {
                System.out.println("Failed: " + e.getMessage());
            }
        }
    }
}