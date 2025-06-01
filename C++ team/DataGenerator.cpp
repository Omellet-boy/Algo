#include <iostream>
#include <fstream>
#include <unordered_set>
#include <random>
#include <string>
#include <ctime>

const int DATASET_SIZE = 1000; // 10 million or more, increase for longer sorting
const int MAX_INT = 1000000000;

std::string generateRandomString(int length) {
    static const char charset[] =
        "abcdefghijklmnopqrstuvwxyz";
    static std::mt19937 rng(std::random_device{}());
    std::uniform_int_distribution<> dist(0, sizeof(charset) - 2);

    std::string result;
    for (int i = 0; i < length; ++i) {
        result += charset[dist(rng)];
    }
    return result;
}

int main() {
    std::ofstream outFile("generated_dataset_1000.csv");
    if (!outFile.is_open()) {
        std::cerr << "Failed to open file for writing.\n";
        return 1;
    }

    std::unordered_set<int> usedIntegers;
    std::mt19937 rng(std::random_device{}());
    std::uniform_int_distribution<int> intDist(1, MAX_INT);
    std::uniform_int_distribution<int> strLenDist(5, 6); // string lengths 5 or 6

    int count = 0;
    while (count < DATASET_SIZE) {
        int number = intDist(rng);
        if (usedIntegers.find(number) != usedIntegers.end()) continue;

        usedIntegers.insert(number);
        std::string randStr = generateRandomString(strLenDist(rng));
        outFile << number << "," << randStr << "\n";
        ++count;

        // Optional progress display
        if (count % 100000 == 0) {
            std::cout << "Generated: " << count << " entries.\n";
        }
    }

    outFile.close();
    std::cout << "Dataset generation completed: " << DATASET_SIZE << " rows.\n";
    return 0;
}
