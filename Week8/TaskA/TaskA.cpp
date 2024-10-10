// Ilyas
#include <iostream>
#include <vector>

using namespace std;

struct Point {
    int x, y;
};

struct Segment {
    Point p1, p2;
};

struct Node {
    Segment data;
    Node* left;
    Node* right;
    int height;
};

Node* newNode(Segment data) {
    Node* node = new Node();
    node->data = data;
    node->left = node->right = nullptr;
    node->height = 1;
    return node;
}

int max(int a, int b) {
    return (a > b) ? a : b;
}

int height(Node* node) {
    if (node == nullptr)
        return 0;
    return node->height;
}

int getBalance(Node* node) {
    if (node == nullptr)
        return 0;
    return height(node->left) - height(node->right);
}

Node* rightRotate(Node* y) {
    Node* x = y->left;
    Node* T2 = x->right;
    x->right = y;
    y->left = T2;
    y->height = max(height(y->left), height(y->right)) + 1;
    x->height = max(height(x->left), height(x->right)) + 1;
    return x;
}

Node* leftRotate(Node* x) {
    Node* y = x->right;
    Node* T2 = y->left;
    y->left = x;
    x->right = T2;
    x->height = max(height(x->left), height(x->right)) + 1;
    y->height = max(height(y->left), height(y->right)) + 1;
    return y;
}

Node* insert(Node* node, Segment data) {
    if (node == nullptr)
        return newNode(data);
    if (min(data.p1.x, data.p2.x) < min(node->data.p1.x, node->data.p2.x))
        node->left = insert(node->left, data);
    else
        node->right = insert(node->right, data);
    node->height = 1 + max(height(node->left), height(node->right));
    int balance = getBalance(node);
    if (balance > 1 && min(data.p1.x, data.p2.x) < min(node->left->data.p1.x, node->left->data.p2.x))
        return rightRotate(node);
    if (balance < -1 && min(data.p1.x, data.p2.x) > min(node->right->data.p1.x, node->right->data.p2.x))
        return leftRotate(node);
    if (balance > 1 && min(data.p1.x, data.p2.x) > min(node->left->data.p1.x, node->left->data.p2.x)) {
        node->left = leftRotate(node->left);
        return rightRotate(node);
    }
    if (balance < -1 && min(data.p1.x, data.p2.x) < min(node->right->data.p1.x, node->right->data.p2.x)) {
        node->right = rightRotate(node->right);
        return leftRotate(node);
    }
    return node;
}

bool doIntersect(Segment s1, Segment s2) {
    int o1 = (s1.p2.y - s1.p1.y) * (s2.p1.x - s1.p1.x) - (s1.p2.x - s1.p1.x) * (s2.p1.y - s1.p1.y);
    int o2 = (s1.p2.y - s1.p1.y) * (s2.p2.x - s1.p1.x) - (s1.p2.x - s1.p1.x) * (s2.p2.y - s1.p1.y);
    int o3 = (s2.p2.y - s2.p1.y) * (s1.p1.x - s2.p1.x) - (s2.p2.x - s2.p1.x) * (s1.p1.y - s2.p1.y);
    int o4 = (s2.p2.y - s2.p1.y) * (s1.p2.x - s2.p1.x) - (s2.p2.x - s2.p1.x) * (s1.p2.y - s2.p1.y);
    if ((o1 > 0 && o2 < 0) || (o1 < 0 && o2 > 0)) {
        if ((o3 > 0 && o4 < 0) || (o3 < 0 && o4 > 0))
            return true;
    }
    if (o1 == 0 && s2.p1.x <= max(s1.p1.x, s1.p2.x) && s2.p1.x >= min(s1.p1.x, s1.p2.x) && s2.p1.y <= max(s1.p1.y, s1.p2.y) && s2.p1.y >= min(s1.p1.y, s1.p2.y))
        return true;
    if (o2 == 0 && s2.p2.x <= max(s1.p1.x, s1.p2.x) && s2.p2.x >= min(s1.p1.x, s1.p2.x) && s2.p2.y <= max(s1.p1.y, s1.p2.y) && s2.p2.y >= min(s1.p1.y, s1.p2.y))
        return true;
    if (o3 == 0 && s1.p1.x <= max(s2.p1.x, s2.p2.x) && s1.p1.x >= min(s2.p1.x, s2.p2.x) && s1.p1.y <= max(s2.p1.y, s2.p2.y) && s1.p1.y >= min(s2.p1.y, s2.p2.y))
        return true;
    if (o4 == 0 && s1.p2.x <= max(s2.p1.x, s2.p2.x) && s1.p2.x >= min(s2.p1.x, s2.p2.x) && s1.p2.y <= max(s2.p1.y, s2.p2.y) && s1.p2.y >= min(s2.p1.y, s2.p2.y))
        return true;
    return false;
}

bool detectIntersections(Node* root, Segment segments[], int n) {
    for (int i = 0; i < n - 1; ++i) {
        for (int j = i + 1; j < n; ++j) {
            if (doIntersect(segments[i], segments[j])) {
                cout << "INTERSECTION" << endl;
                cout << segments[i].p1.x << " " << segments[i].p1.y << " "
                     << segments[i].p2.x << " " << segments[i].p2.y << endl;
                cout << segments[j].p1.x << " " << segments[j].p1.y << " "
                     << segments[j].p2.x << " " << segments[j].p2.y << endl;
                return true;
            }
        }
    }
    return false;
}



int partition(Segment arr[], int low, int high) {
    Segment pivot = arr[high];
    int i = low - 1;
    for (int j = low; j <= high - 1; j++) {
        if (min(arr[j].p1.x, arr[j].p2.x) < min(pivot.p1.x, pivot.p2.x)) {
            i++;
            swap(arr[i], arr[j]);
        }
    }
    swap(arr[i + 1], arr[high]);
    return i + 1;
}
void quickSort(Segment arr[], int low, int high) {
    if (low < high) {
        int pi = partition(arr, low, high);
        quickSort(arr, low, pi - 1);
        quickSort(arr, pi + 1, high);
    }
}
int main() {
    int N;
    cin >> N;
    Segment segments[N];
    for (int i = 0; i < N; ++i) {
        cin >> segments[i].p1.x >> segments[i].p1.y >> segments[i].p2.x >> segments[i].p2.y;
    }

    quickSort(segments, 0, N - 1);

    Node* root = nullptr;
    for (int i = 0; i < N; ++i) {
        root = insert(root, segments[i]);
    }

    bool intersections = detectIntersections(root, segments, N);
    if (!intersections)
        cout << "NO INTERSECTIONS" << endl;
    return 0;
}
