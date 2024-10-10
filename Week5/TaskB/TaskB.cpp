// Ilyas
#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

int main() {
    int n, W;
    cin >> n >> W;

    vector<int> weights(n);
    for (int i = 0; i < n; ++i) {
        cin >> weights[i];
    }

    vector<int> costs(n);
    for (int i = 0; i < n; ++i) {
        cin >> costs[i];
    }

    vector<vector<int>> MaxCosts(n + 1, vector<int>(W + 1, 0));

    int maxCost = 0;
    for (int i = 1; i <= n; ++i) {
        int weight_i = weights[i - 1];
        int cost_i = costs[i - 1];
        for (int j = 1; j <= W; ++j) {
            if (weight_i <= j) {
                MaxCosts[i][j] = max(MaxCosts[i - 1][j], MaxCosts[i - 1][j - weight_i] + cost_i);
            } else {
                MaxCosts[i][j] = MaxCosts[i - 1][j];
            }
        }
    }
    maxCost = MaxCosts[n][W];

    vector<int> selectedItems;
    int remainingWeight = W;
    for (int i = n; i > 0 && maxCost > 0; --i) {
        if (maxCost != MaxCosts[i - 1][remainingWeight]) {
            selectedItems.push_back(i - 1);
            maxCost -= costs[i - 1];
            remainingWeight -= weights[i - 1];
        }
    }

    reverse(selectedItems.begin(), selectedItems.end());

    cout << selectedItems.size() << endl;
    for (int i = 0; i < selectedItems.size(); ++i) {
        cout << selectedItems[i] + 1;
        if (i < selectedItems.size() - 1) {
            cout << " ";
        }
    }
    cout << endl;

    return 0;
}
