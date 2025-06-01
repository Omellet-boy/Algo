#include <iostream>
#include <fstream>
#include <vector>
#include <sstream>
#include <string>

struct Data {
    int number;
    std::string text;
};

int binarySearch(const std::vector<Data>& data, int target, std::ofstream& logFile) {
    int low = 0;
    int high = data.size() - 1;

    while (low <= high) {
        int mid = low + (high - low) / 2;
        logFile << "Compare with value " << data[mid].number << " at row " << mid + 1 << "\n";

        if (data[mid].number == target) {
            return mid;
        } else if (data[mid].number < target) {
            low = mid + 1;
        } else {
            high = mid - 1;
        }
    }
    return -1; // Not found
}

int main() {
    std::string filename;
    int target;

    std::cout << "Enter dataset filename (e.g., merge_sort_n.csv): ";
    std::cin >> filename;
    std::cout << "Enter target integer to search: ";
    std::cin >> target;

    std::ifstream inFile(filename);
    if (!inFile) {
        std::cerr << "Error opening file: " << filename << "\n";
        return 1;
    }

    std::vector<Data> data;
    std::string line;

    while (std::getline(inFile, line)) {
        std::stringstream ss(line);
        std::string numStr, txt;
        if (std::getline(ss, numStr, ',') && std::getline(ss, txt)) {
            data.push_back({std::stoi(numStr), txt});
        }
    }
    inFile.close();

    std::string outFilename = "binary_search_step_" + std::to_string(target) + ".txt";
    std::ofstream outFile(outFilename);
    if (!outFile) {
        std::cerr << "Cannot create output file.\n";
        return 1;
    }

    int result = binarySearch(data, target, outFile);

    if (result != -1)
        outFile << "Target " << target << " found at row " << result + 1 << ".\n";
    else
        outFile << "Target " << target << " not found.\n";

    outFile.close();
    std::cout << "Search steps written to " << outFilename << "\n";

    return 0;
}
