import java.util.*;

class Profile {
    List<List<String>> csvData;
    Integer backgroundMin;
    Integer backgroundMax;
    boolean isFromRimToCore;
    boolean isContinuation;
    Double closestDistanceFromRim;
    Double endDistanceFromRim;
    Double profileLength;
    ArrayList<Integer> nonAnalyteValueIndexes;
    Map<String, Double> backgroundAverages = new HashMap<>();
    HashMap<String, ArrayList<Double>> analyteCpsValuesMinusBackgroundByElement = new HashMap<>();
    Integer analyteSize = 0;
    ArrayList<Double> analyteCpsValuesMinusBackgroundSums = new ArrayList<>();
    HashMap<String, ArrayList<Double>> analyteCpsValuesMinusBackgroundInPercents = new HashMap<>();
    String csvFileName;

    Profile(String csvFileName) {
        this.csvFileName = csvFileName;
        this.csvData = CSVLoader.loadCsv("csv/" + csvFileName);

        setBackground();
        setBackgroundAverages();
        setNonAnalyteAreas();
        setAnalyteValuesMinusBackground();
        setAnalyteValuesMinusBackgroundSums();
        setAnalyteCPSPercents();
        isFromRimToCore = isFromRimToCoreProfile(csvFileName);
        isContinuation = isContinuationProfile(csvFileName);
        profileLength = Store.getProfileLength(csvFileName);
    }

    private void setAnalyteValuesMinusBackground() {
        for (String elementName : getIsotopeHeader()) {
            if (elementName.equals("Time [Sec]")) {
                continue;
            }
            Double analyteBackgroundAverageCps = this.backgroundAverages.get(elementName);
            for (Double value : getAnalyteValues(elementName)) {
                Double valueMinusBackground = value - analyteBackgroundAverageCps;
                analyteCpsValuesMinusBackgroundByElement.computeIfAbsent(elementName, k -> new ArrayList<>()).add(valueMinusBackground);
            }
            if (analyteSize == 0) {
                try {
                    analyteSize = analyteCpsValuesMinusBackgroundByElement.get(elementName).size();
                } catch (Exception e) {

                }
            }
        }
    }

    private void setAnalyteValuesMinusBackgroundSums() {
        for (int i = 0 ; i < analyteSize ; i++) {
            Double sum = 0.0;
            for (String elementName : getIsotopeHeader()) {
                if (elementName.equals("Time [Sec]")) {
                    continue;
                }
                sum += analyteCpsValuesMinusBackgroundByElement.get(elementName).get(i);
            }
            analyteCpsValuesMinusBackgroundSums.add(sum);
        }
    }

    private void setAnalyteCPSPercents() {
        for (int i = 0 ; i < analyteCpsValuesMinusBackgroundSums.size() ; i++) {
            Double sum = analyteCpsValuesMinusBackgroundSums.get(i);
            for (String elementName : getIsotopeHeader()) {
                if (elementName.equals("Time [Sec]")) {
                    continue;
                }
                Double value = analyteCpsValuesMinusBackgroundByElement.get(elementName).get(i);
                Double percent = (100 / sum) * value;
                analyteCpsValuesMinusBackgroundInPercents.computeIfAbsent(elementName, k-> new ArrayList<>()).add(percent);
            }
        }
    }

    private void setBackground() {
        this.backgroundMin = 0;
        this.backgroundMax = getBackgroundMax();
    }

    private Integer getBackgroundMax() {
        ArrayList<Double> values = getColumnValuesByName("Na23");
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
        int nonNanalyteHaloSize = 15;
        ArrayList<Double> values = getColumnValuesByName("Na23");
        ArrayList<Integer> nonAnalyteValueIndexes = new ArrayList<>();
        int count = 0;
        for (Double value : values) {
            if (isBackground(count) || valueCloseToBackground(value)) {
                nonAnalyteValueIndexes.add(count);
                for (int i = -nonNanalyteHaloSize; i < nonNanalyteHaloSize; i++) {
                    int nearestMeasure = count + i;
                    if (nearestMeasure > 0 && nearestMeasure < values.size() - 1 && !nonAnalyteValueIndexes.contains(nearestMeasure)) {
                        nonAnalyteValueIndexes.add(nearestMeasure);
                    }
                }
            }
            count++;
        }
        this.nonAnalyteValueIndexes = nonAnalyteValueIndexes;
    }

    private boolean isFromRimToCoreProfile(String csvFileName) {
        for (String rimToCoreLine : Store.getRimToCoreLines()) {
            if (csvFileName.contains(rimToCoreLine + ".csv") || csvFileName.contains(rimToCoreLine + "a.csv")) {
                return true;
            }
        }
        return false;
    }

    private boolean isContinuationProfile(String csvFileName) {
        return csvFileName.contains("a.csv");
    }

    private boolean isBackground(int count) {
        return count < backgroundMin;
    }

    private boolean valueCloseToBackground(Double value) {
        return backgroundAverages.get("Na23") * 3 > value;
    }

    private Double calculateBackgroundAverage(String measurementName) {
        ArrayList<Double> values = getColumnValuesByName(measurementName);
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
        return csvData.get(0);
    }

    List<String> getMeasurementRow(int rowIndex) {
        return csvData.get(rowIndex);
    }

    private void addColumn(String name, ArrayList<Double> columnValues) {
        boolean pastTitleRow = false;
        int count = 0;
        for (List<String> measurementRow : csvData) {
            if (pastTitleRow) {
                measurementRow.add(columnValues.get(count).toString());
            } else if (isHeaderRow(measurementRow)) {
                measurementRow.add(name);
            }
            count++;
        }
    }

    private boolean isHeaderRow(List<String> measurementRow) {
        return measurementRow.contains("Al27");
    }

    List<String> getColumnByName(String name) {
        List<String> measurementsByName = new ArrayList<>();
        int columnIndex = getColumnIndexByName(name);
        for (List<String> measurementRow : csvData) {
            measurementsByName.add(measurementRow.get(columnIndex));
        }
        return measurementsByName;
    }

    ArrayList<Double> getColumnValuesByName(String name) {
        ArrayList<Double> cpsArray = new ArrayList<>();
        for (String cpsString : getColumnByName(name)) {
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
        ArrayList<Double> values = getColumnValuesByName(name);
        int count = 0;
        double sum = 0.0;
        for (int i = 0; i < values.size() - 1; i++) {
            if (nonAnalyteValueIndexes.contains(i)) {
                continue;
            } else {
                sum += values.get(i);
                count++;
            }
        }
        return sum / count;
    }

    Double getAnalyteAverageCpsMinusBackgroundInPercents(String name) {
        ArrayList<Double> values = analyteCpsValuesMinusBackgroundInPercents.get(name);
        int count = 0;
        double sum = 0.0;
        for (int i = 0; i < values.size() - 1; i++) {
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
        ArrayList<Double> values = getColumnValuesByName(name);
        ArrayList<Double> analyteValues = new ArrayList<>();
        for (int i = 0; i < values.size() - 1; i++) {
            if (nonAnalyteValueIndexes.contains(i)) {
                continue;
            } else {
                analyteValues.add(values.get(i));
            }
        }
        return analyteValues;
    }

    HashMap<String, ArrayList<Double>> getCalculatedValuesByName(ArrayList<String> standardCsvAddresses, Double closestDistanceFromRim) {
        this.closestDistanceFromRim = closestDistanceFromRim;
        if (standardCsvAddresses == null) {
            standardCsvAddresses = getStandards();
        }
        HashMap<String, ArrayList<Double>> columnsByNames = new HashMap<>();
        Integer mineralProfileSize = 0;
        for (String name : getIsotopeHeader()) {
            ArrayList<Double> ppmValuesForElement = calculatePpmValues(standardCsvAddresses, name);
            columnsByNames.put(name, ppmValuesForElement);
            if (mineralProfileSize == 0) {
                mineralProfileSize = ppmValuesForElement.size();
            }
        }
        columnsByNames.put("Dist from rim", getDistancesFromRim(mineralProfileSize));
        if (isFromRimToCore) {
            return columnsByNames;
        } else {
            return reverseArraysInMap(columnsByNames);
        }
    }

    private ArrayList<String> getStandards() {
        ArrayList<String> standardCsvAddresses = new ArrayList<>();
        standardCsvAddresses.add(getFirstStandardForAnalyte());
        standardCsvAddresses.add(getSecondStandardForAnalyte());
        return standardCsvAddresses;
    }

    private ArrayList<Double> getDistancesFromRim(Integer mineralProfileSize) {
        ArrayList<Double> distancesFromRimToCore = new ArrayList<>();
        Double speedOfLaserMovementPerOneMeasure = getLaserMovementSpeed(mineralProfileSize);
        System.out.println(csvFileName + " distance from rim: " + closestDistanceFromRim + " isFromRimToCore: " + isFromRimToCore + " isContinuation" + isContinuation);
        for (Integer i = 0 ; i < mineralProfileSize ; i++) {
            if (isFromRimToCore) {
                distancesFromRimToCore.add(i*speedOfLaserMovementPerOneMeasure + closestDistanceFromRim);
            } else {
                distancesFromRimToCore.add((mineralProfileSize-i-1)*speedOfLaserMovementPerOneMeasure + closestDistanceFromRim);
            }
        }
        return distancesFromRimToCore;
    }

    private Double getLaserMovementSpeed(Integer mineralProfileSize) {
        return profileLength / mineralProfileSize;
    }

    private HashMap<String, ArrayList<Double>> reverseArraysInMap(HashMap<String, ArrayList<Double>> columnsByNames) {
        for (String key : columnsByNames.keySet()) {
            ArrayList<Double> column = columnsByNames.get(key);
            Collections.reverse(column);
            columnsByNames.put(key, column);
        }
        return columnsByNames;
    }

    private String getFirstStandardForAnalyte() {
        String sphCsvName = "";
        for (String comparedCsvFileName : CSVLoader.getListOfCsvFiles()) {
            if (isSPHProfile(comparedCsvFileName) && isReliableSPH(comparedCsvFileName)) {
                sphCsvName = comparedCsvFileName;
            }
            if (comparedCsvFileName.equals(csvFileName)) {
                break;
            }
        }
        return sphCsvName;
    }

    private String getSecondStandardForAnalyte() {
        String sphCsvName = "";
        boolean analytePassed = false;
        for (String comparedCsvFileName : CSVLoader.getListOfCsvFiles()) {
            if (comparedCsvFileName.equals(csvFileName)) {
                analytePassed = true;
            }
            if (isSPHProfile(comparedCsvFileName) && isReliableSPH(comparedCsvFileName) && analytePassed) {
                sphCsvName = comparedCsvFileName;
                break;
            }
        }
        return sphCsvName;
    }

    private boolean isSPHProfile(String csvFileName) {
        return csvFileName.contains("SPH");
    }

    private boolean isReliableSPH(String csvFileName) {
        return !Store.getUnreliableSPHes().contains(csvFileName);
    }

    ArrayList<Double> calculatePpmValues(ArrayList<String> standardCsvAddresses, String name) {
        ArrayList<Double> ppmValues = new ArrayList<>();
        Double standardsAverageCpsMinusBackground = getStandardsAverageCpsMinusBackground(standardCsvAddresses, name);
        Double ppmValue = Store.getSphPpmMap().get(name.replaceAll("[0-9]", ""));
        ArrayList<Double> analyteValuesMinusBackgroundInPercents = analyteCpsValuesMinusBackgroundInPercents.get(name);
        for (Double analyteValue : analyteValuesMinusBackgroundInPercents) {
            Double calculatedValue = ppmValue * (analyteValue) / standardsAverageCpsMinusBackground;
            ppmValues.add(calculatedValue);
        }
        return ppmValues;
    }

    private Double getStandardsAverageCpsMinusBackground(ArrayList<String> standardCsvAddresses, String name) {
        Double standardAverageCps = 0.0;
        for (String standardCsvAddress : standardCsvAddresses) {
            Profile standardProfile = new Profile(standardCsvAddress);
            standardAverageCps += standardProfile.getAnalyteAverageCpsMinusBackgroundInPercents(name);
        }
        return standardAverageCps / standardCsvAddresses.size();
    }

    ArrayList<Double> getPpmRatioValues(ArrayList<String> standardCsvAddresses, String firstElementName, String secondElementName) {
        ArrayList<Double> firstCpsValues = calculatePpmValues(standardCsvAddresses, firstElementName);
        ArrayList<Double> secondCpsValues = calculatePpmValues(standardCsvAddresses, secondElementName);
        return getPpmRatio(firstCpsValues, secondCpsValues);
    }

    ArrayList<Double> getPpmRatio(ArrayList<Double> firstSetOfValues, ArrayList<Double> secondSetOfValues) {
        ArrayList<Double> ratioValues = new ArrayList<>();
        for (int i = 0; i < firstSetOfValues.size(); i++) {
            Double ratioValue = firstSetOfValues.get(i) / secondSetOfValues.get(i);
            ratioValues.add(ratioValue);
        }
        return ratioValues;
    }
}
