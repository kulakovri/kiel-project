import java.util.*;

class Profile {
    private List<List<String>> csvData;
    private Integer backgroundMin;
    private Integer backgroundMax;
    ArrayList<Integer> nonAnalyteValueIndexes;
    Map<String, Double> backgroundAverages = new HashMap<>();

    Profile(List<List<String>> csvData) {
        this.csvData = csvData;
        setBackground();
        setBackgroundAverages();
        setNonAnalyteAreas();
    }

    private void setBackground() {
        this.backgroundMin = 0;
        this.backgroundMax = getBackgroundMax();
    }

    private Integer getBackgroundMax() {
        ArrayList<Double> values = getSignalValuesByName("Na23");
        Integer count = 0;
        Double comparedValue = values.get(0);
        for (Double value : values) {
            if (comparedValue * 3 < value) {
                count--;
                count--;
                break;
            }
            count++;
        }
        return count;
    }

    private void setBackgroundAverages() {
        for (String measurementName : getIsotopeHeader()) {
            Double backgroundAverage = calculateBackgroundAverage(measurementName);
            backgroundAverages.put(measurementName, backgroundAverage);
        }
    }

    private void setNonAnalyteAreas() {
        int nonNanalyteHaloSize = 20;
        ArrayList<Double> values = getSignalValuesByName("Na23");
        ArrayList<Integer> nonAnalyteValueIndexes = new ArrayList<>();
        int count = 0;
        for (Double value : values) {
            if (isBackground(count) || valueCloseToBackground(value)) {
                nonAnalyteValueIndexes.add(count);
                for (int i = -nonNanalyteHaloSize ; i < nonNanalyteHaloSize ; i ++) {
                    int nearestMeasure = count + i;
                    if (nearestMeasure > 0 && nearestMeasure < values.size()-1 && !nonAnalyteValueIndexes.contains(nearestMeasure)) {
                        nonAnalyteValueIndexes.add(nearestMeasure);
                    }
                }
            }
            count++;
        }
        this.nonAnalyteValueIndexes = nonAnalyteValueIndexes;
    }

    private boolean isBackground(int count) {
        return count < backgroundMin;
    }

    private boolean valueCloseToBackground(Double value){
        return backgroundAverages.get("Na23") * 3 > value;
    }

    private Double calculateBackgroundAverage(String measurementName) {
        ArrayList<Double> values = getSignalValuesByName(measurementName);
        Integer count = 0;
        Double sum = 0.0;
        for (Double value : values) {
            sum += value;
            count++;
            if (count > this.backgroundMax) {
                break;
            }
        }
        return sum / count;
    }

    List<String> getIsotopeHeader() {
        return csvData.get(0).subList(1, csvData.size()-1);
    }

    List<String> getMeasurementRow(int rowIndex) {
        return csvData.get(rowIndex);
    }

    List<String> getMeasurementsByName(String name) {
        List<String> measurementsByName = new ArrayList<>();
        int columnIndex = getColumnIndexByName(name);
        for (List<String> measurementRow : csvData) {
            measurementsByName.add(measurementRow.get(columnIndex));
        }
        return measurementsByName;
    }

    ArrayList<Double> getSignalValuesByName(String name) {
        ArrayList<Double> cpsArray = new ArrayList<>();
        for (String cpsString : getMeasurementsByName(name)) {
            try {
                cpsArray.add(Double.parseDouble(cpsString));
            } catch (Exception e) {

            }
        }
        return cpsArray;
    }

    private int getColumnIndexByName(String lookupName) {
        int columnIndex = 0;
        for (String nameInHeader : getIsotopeHeader()) {
            if (nameInHeader.equals(lookupName)) {
                break;
            } else {
                columnIndex++;
            }
        }
        return columnIndex;
    }

    Double getAnalyteAverageCps(String name) {
        ArrayList<Double> values = getSignalValuesByName(name);
        int count = 0;
        double sum = 0.0;
        for (int i = 0 ; i < values.size()-1 ; i++) {
            if (nonAnalyteValueIndexes.contains(i)) {
                continue;
            } else {
                sum += values.get(i);
                count++;
            }
        }
        return sum / count;
    }

    ArrayList<Double> getAnalyteValues(String name) {
        ArrayList<Double> values = getSignalValuesByName(name);
        ArrayList<Double> analyteValues = new ArrayList<>();
        for (int i = 0 ; i < values.size()-1 ; i++) {
            if (nonAnalyteValueIndexes.contains(i)) {
                continue;
            } else {
                analyteValues.add(values.get(i));
            }
        }
        return analyteValues;
    }

    HashMap<String,ArrayList<Double>> getPpmValuesByName(ArrayList<String> standardCsvAddresses) {
        HashMap<String,ArrayList<Double>> ppmValuesByElementNames = new HashMap<>();
        for (String name : getIsotopeHeader()) {
            ppmValuesByElementNames.put(name, calculatePpmValues(standardCsvAddresses, name));
        }
        return ppmValuesByElementNames;
    }

    ArrayList<Double> calculatePpmValues(ArrayList<String> standardCsvAddresses, String name) {
        ArrayList<Double> ppmValues = new ArrayList<>();
        Double standardsAverageCpsMinusBackground = getStandardsAverageCpsMinusBackground(standardCsvAddresses, name);
        Double ppmValue = Concentration.getSphPpmMap().get(name.replaceAll("[0-9]", ""));
        Double analyteBackgroundAverageCps = this.backgroundAverages.get(name);
        for (Double analyteValue : getAnalyteValues(name)) {
            Double calculatedValue = ppmValue * (analyteValue - analyteBackgroundAverageCps) / standardsAverageCpsMinusBackground;
            ppmValues.add(calculatedValue);
        }
        return ppmValues;
    }

    private Double getStandardsAverageCpsMinusBackground(ArrayList<String> standardCsvAddresses, String name) {
        Double standardAverageCps = 0.0;
        Double standardBackgroundCps = 0.0;
        for (String standardCsvAddress : standardCsvAddresses) {
            Profile standardProfile = new Profile(CSVLoader.loadCsv(standardCsvAddress));
            standardAverageCps += standardProfile.getAnalyteAverageCps(name);
            standardBackgroundCps += standardProfile.backgroundAverages.get(name);
        }
        standardAverageCps = standardAverageCps / standardCsvAddresses.size();
        standardBackgroundCps = standardBackgroundCps / standardCsvAddresses.size();
        return standardAverageCps - standardBackgroundCps;
    }

    ArrayList<Double> getPpmRatioValues(ArrayList<String> standardCsvAddresses, String firstElementName, String secondElementName) {
        ArrayList<Double> firstCpsValues = calculatePpmValues(standardCsvAddresses, firstElementName);
        ArrayList<Double> secondCpsValues = calculatePpmValues(standardCsvAddresses, secondElementName);
        return getPpmRatio(firstCpsValues, secondCpsValues);
    }

    ArrayList<Double> getPpmRatio(ArrayList<Double> firstSetOfValues, ArrayList<Double> secondSetOfValues) {
        ArrayList<Double> ratioValues = new ArrayList<>();
        for (int i = 0 ; i < firstSetOfValues.size() ; i++) {
            Double ratioValue = firstSetOfValues.get(i) / secondSetOfValues.get(i);
            ratioValues.add(ratioValue);
        }
        return ratioValues;
    }
}
