import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

class ProfileAnalyzer {
    Profile profile;
    HashMap<String,ArrayList<Double>> ppmValuesByName;
    HashMap<String,ArrayList<Double>> ppmAlRatiosByName;
    HashMap<Integer, HashMap<Integer, Double>> peakRatioValuesByName;

    ProfileAnalyzer(Profile profile, ArrayList<String> standardCsvAddresses) {
        this.profile = profile;
        this.ppmValuesByName = profile.getPpmValuesByName(standardCsvAddresses);
        this.ppmAlRatiosByName = getPpmAlValuesByName();
    }

    private HashMap<String,ArrayList<Double>> getPpmAlValuesByName() {
        HashMap<String,ArrayList<Double>> ppmAlRatiosByName = new HashMap<>();
        ArrayList<Double> alPpmValues = ppmValuesByName.get("Al27");
        for (String name : ppmValuesByName.keySet()) {
            ArrayList<Double> analyzedPpmValues = ppmValuesByName.get(name);
            ppmAlRatiosByName.put(name, profile.getPpmRatio(analyzedPpmValues, alPpmValues));
        }
        return ppmAlRatiosByName;
    }

    HashMap<Integer, Double> getAngleValues(String name) {
        ArrayList<Double> sequenceValues = ppmAlRatiosByName.get(name);
        ArrayList<Integer> angleIndexes = initAngleIndexes(sequenceValues);



        ArrayList<ArrayList<Double>> partedSequences = partSequenceValues(angleIndexes, sequenceValues);
        angleIndexes = addMinAndMaxValues(angleIndexes, sequenceValues);
        partedSequences = partSequenceValues(angleIndexes, sequenceValues);


        int sequenceIndex = 0;
        for (ArrayList<Double> partedSequence : partedSequences) {
            ArrayList<Double> perfectSequenceLine = getPerfectSequenceLine(partedSequence);
            ArrayList<Double> differenceSequence = getDifferenceSequence(perfectSequenceLine, partedSequence);

            angleIndexes = addMinAndMaxValues(angleIndexes, differenceSequence);

            sequenceIndex++;
        }

        return new HashMap<>();
    }

    private ArrayList<Double> getDifferenceSequence(ArrayList<Double> minuendSequence, ArrayList<Double> subtrahendSequence) {
        ArrayList<Double> differenceSequence = new ArrayList<>();
        for (Integer i = 0 ; i < minuendSequence.size() ; i++) {
            Double differenceForStep = minuendSequence.get(i) - subtrahendSequence.get(i);
            differenceSequence.add(differenceForStep);
        }
        return differenceSequence;
    }

    private ArrayList<ArrayList<Double>> partSequenceValues(ArrayList<Integer> angleIndexes, ArrayList<Double> sequenceValues) {
        ArrayList<ArrayList<Double>> partedSequences = new ArrayList<>();
        Integer previousKey = null;
        Collections.sort(angleIndexes);
        for (Integer i : angleIndexes) {
            if (previousKey != null) {
                System.out.println("Adding Double range " + previousKey + " and " + i);
                partedSequences.add(new ArrayList<>(sequenceValues.subList(previousKey, i+1)));
            }
            previousKey = i;
        }
        return partedSequences;
    }

    private ArrayList<Integer> initAngleIndexes(ArrayList<Double> sequenceValues) {
        ArrayList<Integer> angleIndexes = new ArrayList<>();
        angleIndexes.add(0);
        angleIndexes.add(sequenceValues.size()-1);
        return angleIndexes;
    }

    private ArrayList<Integer> addMinAndMaxValues(ArrayList<Integer> angleIndexes, ArrayList<Double> sequenceValues) {
        Double maxValue = Collections.max(sequenceValues);
        Double minValue = Collections.min(sequenceValues);
        Double stDev = findStandardDev(sequenceValues);
        Double average = calculateAverage(sequenceValues);
        Double maxOutOfStDev = average + stDev;
        Double minOutOfStDev = average - stDev;
        System.out.println("Maximum out of Standard deviation  " + maxOutOfStDev);
        System.out.println("Minimum out of Standard deviation  " + minOutOfStDev);
        for (Integer i = 0 ; i < sequenceValues.size()-1 ; i++) {
            Double value = sequenceValues.get(i);
            if (value == maxValue && value > maxOutOfStDev) {
                System.out.println("max value: " + value + " || index: " + i);
                angleIndexes.add(i);
            }
            if (value == minValue && value < minOutOfStDev) {
                System.out.println("min value: " + value + " || index: " + i);
                angleIndexes.add(i);
            }
        }
        return angleIndexes;
    }

    private ArrayList<Double> getPerfectSequenceLine(ArrayList<Double> partedSequence) {
        ArrayList<Double> perfectSequenceLine = new ArrayList<>();
        Double firstValue = partedSequence.get(0);
        Double perStepValue = getPerStepValue(partedSequence);
        for (Integer i = 0 ; i < partedSequence.size() ; i++) {
            Double stepValue = firstValue + (i * perStepValue);
            //System.out.println("For the step " + i + " value is " + stepValue);
            perfectSequenceLine.add(stepValue);
        }
        return perfectSequenceLine;
    }

    private Double getPerStepValue(ArrayList<Double> partedSequence) {
        Integer partedSequenceSize = partedSequence.size();
        Double firstValue = partedSequence.get(0);
        Double lastValue = partedSequence.get(partedSequenceSize-1);
        return  (lastValue - firstValue) / partedSequenceSize;
    }

    private Double findStandardDev(ArrayList<Double> table) {
        Double mean =  calculateAverage(table);
        System.out.println("mean value: " + mean);
        Double temp = 0.0;
        for (Integer i = 0; i < table.size(); i++) {
            Double val = table.get(i);
            Double squrDiffToMean = Math.pow(val - mean, 2);
            temp += squrDiffToMean;
        }
        Double meanOfDiffs = temp / table.size();
        return Math.sqrt(meanOfDiffs);
    }

    private Double calculateAverage(ArrayList <Double> marks) {
        Double sum = 0.0;
        if(!marks.isEmpty()) {
            for (Double mark : marks) {
                sum += mark;
            }
            return sum.doubleValue() / marks.size();
        }
        return sum;
    }
}
