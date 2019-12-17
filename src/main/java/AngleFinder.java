import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

class AngleFinder {
    private ArrayList<Double> sequenceValues;
    private TreeMap<Integer, Double> indexedSequenceValues = new TreeMap<>();
    private ArrayList<Integer> angleIndexes = new ArrayList<>();

    AngleFinder(ArrayList<Double> sequenceValues) {
        this.sequenceValues = sequenceValues;
        addSequenceValuesToTreeMap();
        addFirstAndLastAngleValues();
    }

    private void addSequenceValuesToTreeMap() {
        for (int i = 0 ; i < sequenceValues.size()-1 ; i++) {
            this.indexedSequenceValues.put(i, sequenceValues.get(i));
        }
    }

    private void addFirstAndLastAngleValues() {
        angleIndexes.add(0);
        angleIndexes.add(sequenceValues.size()-1);
    }

    void addExtremes() {
        ArrayList<TreeMap<Integer, Double>> partedSequences = partSequenceValues();
        ArrayList<TreeMap<Integer, Double>> perfectSequenceLines = createPerfectSequenceLines(partedSequences);
        ArrayList<TreeMap<Integer, Double>> differenceSequences = getDifferenceSequences(partedSequences, perfectSequenceLines);
        addAngleIndexes(differenceSequences);
    }

    private ArrayList<TreeMap<Integer, Double>> partSequenceValues() {
        ArrayList<TreeMap<Integer, Double>> partedSequences = new ArrayList<>();
        Collections.sort(angleIndexes);
        Integer previousIndex = null;
        for (Integer currentIndex : angleIndexes) {
            if (previousIndex != null) {
                partedSequences.add(createPartedSequenceMap(previousIndex, currentIndex));
            }
            previousIndex = currentIndex;
        }
        return partedSequences;
    }

    private TreeMap<Integer, Double> createPartedSequenceMap(Integer startIndex, Integer endIndex) {
        TreeMap<Integer, Double> partedSequenceMap = new TreeMap<>();
        for (Integer index : indexedSequenceValues.keySet()) {
            if (index >= startIndex && index <= endIndex) {
                partedSequenceMap.put(index, indexedSequenceValues.get(index));
            }
        }
        return partedSequenceMap;
    }

    private ArrayList<TreeMap<Integer, Double>> createPerfectSequenceLines(ArrayList<TreeMap<Integer, Double>> partedSequences) {
        ArrayList<TreeMap<Integer, Double>> perfectSequenceLines = new ArrayList<>();
        for (TreeMap<Integer, Double> partedSequence : partedSequences) {
            perfectSequenceLines.add(createPefectSequenceLine(partedSequence));
        }
        return perfectSequenceLines;
    }

    private TreeMap<Integer, Double> createPefectSequenceLine(TreeMap<Integer, Double> partedSequence) {
        TreeMap<Integer, Double> perfectSequenceLine = new TreeMap<>();
        Integer firstKey = partedSequence.firstKey();
        Double firstValue = partedSequence.get(firstKey);
        Double perStepValue = getPerStepValue(partedSequence);
        for (Integer i = 0; i < partedSequence.size(); i++) {
            Double stepValue = firstValue + (i * perStepValue);
            Integer key = firstKey + i;
            perfectSequenceLine.put(key, stepValue);
        }
        return perfectSequenceLine;
    }

    private Double getPerStepValue(TreeMap<Integer, Double> partedSequence) {
        Integer partedSequenceSize = partedSequence.size();
        Double firstValue = partedSequence.get(partedSequence.firstKey());
        Double lastValue = partedSequence.get(partedSequence.lastKey());
        return (lastValue - firstValue) / partedSequenceSize;
    }

    private ArrayList<TreeMap<Integer, Double>> getDifferenceSequences(ArrayList<TreeMap<Integer, Double>> partedSequences, ArrayList<TreeMap<Integer, Double>> perfectSequenceLines) {
        ArrayList<TreeMap<Integer, Double>> differenceSequences = new ArrayList<>();
        for (Integer i = 0; i < partedSequences.size(); i++) {
            TreeMap<Integer, Double> partedSequence = partedSequences.get(i);
            TreeMap<Integer, Double> perfectSequenceLine = perfectSequenceLines.get(i);
            differenceSequences.add(getDifferenceSequence(partedSequence, perfectSequenceLine));
        }
        return differenceSequences;
    }

    private TreeMap<Integer, Double> getDifferenceSequence(TreeMap<Integer, Double> partedSequence, TreeMap<Integer, Double> perfectSequenceLine) {
        TreeMap<Integer, Double> differenceSequence = new TreeMap<>();
        for (Integer i : partedSequence.keySet()) {
            Double valueFromActualSequence = partedSequence.get(i);
            Double valueFromPerfectSequence = perfectSequenceLine.get(i);
            Double difference = valueFromActualSequence - valueFromPerfectSequence;
            differenceSequence.put(i, difference);
        }
        return differenceSequence;
    }

    private void addAngleIndexes(ArrayList<TreeMap<Integer, Double>> differenceSequences) {
        for (TreeMap<Integer, Double> differenceSequence : differenceSequences) {
            ArrayList<Double> differenceSequenceArray = (ArrayList<Double>) differenceSequence.values();
            Double maxValue = Collections.max(differenceSequenceArray);
            Double minValue = Collections.min(differenceSequenceArray);
            Double stDev = findStandardDev(differenceSequenceArray);
            for (Integer i : differenceSequence.keySet()) {
                Double value = differenceSequence.get(i);
                if (value == maxValue && value > stDev) {
                    angleIndexes.add(i);
                }
                if (value == minValue && value < -stDev) {
                    angleIndexes.add(i);
                }
            }
        }
    }

    private Double findStandardDev(ArrayList<Double> table) {
        Double mean = calculateAverage(table);
        Double temp = 0.0;
        for (Integer i = 0; i < table.size(); i++) {
            Double val = table.get(i);
            Double squrDiffToMean = Math.pow(val - mean, 2);
            temp += squrDiffToMean;
        }
        Double meanOfDiffs = temp / table.size();
        return Math.sqrt(meanOfDiffs);
    }

    private Double calculateAverage(ArrayList<Double> marks) {
        Double sum = 0.0;
        if (!marks.isEmpty()) {
            for (Double mark : marks) {
                sum += mark;
            }
            return sum.doubleValue() / marks.size();
        }
        return sum;
    }
}
