#include <iostream>

using namespace std;

struct Player {
    string name;
    int score;
};

int partition(Player players[], int low, int high) {
    int pivot = players[high].score;
    int i = low - 1;

    for (int j = low; j < high; ++j) {
        if (players[j].score >= pivot) {
            ++i;
            swap(players[i], players[j]);
        }
    }

    swap(players[i + 1], players[high]);
    return i + 1;
}

void quickSort(Player players[], int low, int high) {
    if (low < high) {
        int pivotIndex = partition(players, low, high);
        quickSort(players, low, pivotIndex - 1);
        quickSort(players, pivotIndex + 1, high);
    }
}

void buildLeadershipTable(Player players[], int n, int k) {
    quickSort(players, 0, n - 1);
    for (int i = 0; i < min(n, k); ++i) {
        cout << players[i].name << " " << players[i].score << endl;
    }
}

int main() {
    int n, k;
    cin >> n >> k;
    Player players[n];
    for (int i = 0; i < n; ++i) {
        cin >> players[i].name >> players[i].score;
    }
    buildLeadershipTable(players, n, k);
    return 0;
}
