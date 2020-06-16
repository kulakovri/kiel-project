import java.util.ArrayList;
import java.util.TreeSet;

public class GFG {

    // Pair class for the value and its index
    static class Pair implements Comparable<Pair> {
        private Double value;
        private int index;

        // Constructor
        Pair(Double v, int p) {
            value = v;
            index = p;
        }

        // This method will be used by the treeset to
        // search a value by index and setting the tree
        // nodes (left or right)
        @Override
        public int compareTo(Pair o) {

            // Two nodes are equal only when
            // their indices are same
            if (index == o.index) {
                return 0;
            } else if (value == o.value) {
                return Integer.compare(index, o.index);
            } else {
                return Double.compare(value, o.value);
            }
        }

        // Function to return the value
        // of the current object
        public Double value() {
            return value;
        }

        // Update the value and the position
        // for the same object to save space
        public void renew(Double v, int p) {
            value = v;
            index = p;
        }

        @Override
        public String toString() {
            return String.format("(%d, %d)", value, index);
        }
    }

    // Function to find the median
    // of every window of size k
    static ArrayList<Double> findMedian(Double arr[], int k) {
        ArrayList<Double> medianValues = new ArrayList<>();
        TreeSet<Pair> minSet = new TreeSet<>();
        TreeSet<Pair> maxSet = new TreeSet<>();
        // To hold the pairs, we will keep renewing
        // these instead of creating the new pairs
        Pair[] windowPairs = new Pair[k];
        for (int i = 0; i < k; i++) {
            windowPairs[i] = new Pair(arr[i], i);
        }
        // Add k/2 items to maxSet
        for (int i = 0; i < k / 2; i++) {
            maxSet.add(windowPairs[i]);
        }
        for (int i = k / 2; i < k; i++) {
            // Below logic is to maintain the
            // maxSet and the minSet criteria
            if (arr[i] < maxSet.first().value()) {
                minSet.add(windowPairs[i]);
            } else {
                minSet.add(maxSet.pollFirst());
                maxSet.add(windowPairs[i]);
            }
        }
        medianValues.add(getMedian(minSet, maxSet, k));
        for (int i = k; i < arr.length; i++) {
            // Get the pair at the start of the window, this
            // will reset to 0 at every k, 2k, 3k, ...
            Pair temp = windowPairs[i % k];
            if (temp.value() <= minSet.last().value()) {
                // Remove the starting pair of the window
                minSet.remove(temp);
                // Renew window start to new window end
                temp.renew(arr[i], i);
                // Below logic is to maintain the
                // maxSet and the minSet criteria
                if (temp.value() < maxSet.first().value()) {
                    minSet.add(temp);
                } else {
                    minSet.add(maxSet.pollFirst());
                    maxSet.add(temp);
                }
            } else {
                maxSet.remove(temp);
                temp.renew(arr[i], i);
                // Below logic is to maintain the
                // maxSet and the minSet criteria
                if (temp.value() > minSet.last().value()) {
                    maxSet.add(temp);
                } else {
                    maxSet.add(minSet.pollLast());
                    minSet.add(temp);
                }
            }
            medianValues.add(getMedian(minSet, maxSet, k));
        }
        return medianValues;
    }

    // Function to print the median for the current window
    static void printMedian(TreeSet<Pair> minSet,
                            TreeSet<Pair> maxSet, int window) {

        // If the window size is even then the
        // median will be the average of the
        // two middle elements
        if (window % 2 == 0) {
            System.out.print((minSet.last().value()
                    + maxSet.first().value())
                    / 2.0);
            System.out.print("\n");
        }

        // Else it will be the middle element
        else {
            System.out.print(minSet.size() > maxSet.size()
                    ? minSet.last().value()
                    : maxSet.first().value());
            System.out.print("\n");
        }
    }

    static Double getMedian(TreeSet<Pair> minSet, TreeSet<Pair> maxSet, int window) {
        if (window % 2 == 0) {
            return (minSet.last().value() + maxSet.first().value()) / 2.0;
        } else {
            return minSet.size() > maxSet.size() ? minSet.last().value() : maxSet.first().value();
        }
    }

    // Driver code
    public static void main(String[] args) {
        Double[] arr = new Double[]{0.0, 9.0, 1.0, 8.0, 2.0, 7.0, 3.0, 6.0, 4.0, 5.0};
        int k = 3;

        findMedian(arr, k);
    }
}