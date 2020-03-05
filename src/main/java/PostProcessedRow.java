import java.util.ArrayList;
import java.util.HashMap;

public class PostProcessedRow implements Comparable<PostProcessedRow> {
    String header = "Line Number,Dist from rim,Li7,Na23,Mg24,Al27,Si29,P31,K39,Ca43,Ca44,Ti47,Mn55,Fe57,Cu65,Ga71,Sr88,Y89,Ba138,Ce140,Pb208";
    String fullRow;
    String recalculatedFullRow;
    int rowsSize = 21;
    Double distanceFromRim = 0.0;
    Double sumPpm = 0.0;
    String lineNumber;
    HashMap<String, String> ppmValueByElementName = new HashMap<>();
    HashMap<String, Double> percentValuesByOxideName = new HashMap<>();
    Boolean isNotValueRow = false;

    PostProcessedRow(String row) {
        this.fullRow = row;
        String[] rowValues = row.split(",");
        String firstRowValue = rowValues[0];
        if (firstRowValue == null || firstRowValue.equals("Line Number") || firstRowValue.equals("") ) {
            isNotValueRow = true;
        } else {
            String[] headerElements = header.split(",");
            for (int columnIndex = 0 ; columnIndex < rowsSize ; columnIndex++) {
                try {
                    String rowValue = rowValues[columnIndex];
                    if (columnIndex == 0) {
                        lineNumber = rowValue;
                    } else if (columnIndex == 1) {
                        distanceFromRim = Double.valueOf(rowValue);
                    } else {
                        sumPpm += Double.valueOf(rowValue);
                        ppmValueByElementName.put(headerElements[columnIndex], rowValue);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            ArrayList<String> recalculatedRow = new ArrayList<>();
            recalculatedRow.add(rowValues[0]);
            recalculatedRow.add(rowValues[1]);
            for (int columnIndex = 2 ; columnIndex < rowsSize ; columnIndex++) {
                Double value = Double.valueOf(rowValues[columnIndex]);
                Double normalizedValue = Math.round((value / sumPpm) * 100000000) / 100.0;
                recalculatedRow.add(String.valueOf(normalizedValue));
            }
            recalculatedFullRow = String.join( ",", recalculatedRow);
            setPercentValuesByOxideName();
        }
    }

    void setPercentValuesByOxideName() {
        Double percentSum = 0.0;
        for (String elementName : ppmValueByElementName.keySet()) {
            elementName = elementName.replaceAll("[0-9]", "");
            Double elementValue = Double.valueOf(ppmValueByElementName.get(elementName));
            String oxideName = "";
            if (elementName.contains("Si")) {
                oxideName = "SiO2";
            } else if (elementName.contains("Al")) {
                oxideName = "Al2O3";
            } else if (elementName.contains("Fe")) {
                oxideName = "FeO";
            } else if (elementName.contains("Ca")) {
                oxideName = "CaO";
            } else if (elementName.contains("Na")) {
                oxideName = "Na2O";
            } else if (elementName.contains("K")) {
                oxideName = "K2O";
            } else if (elementName.contains("Mg")) {
                oxideName = "MgO";
            } else if (elementName.contains("Ti")) {
                oxideName = "TiO2";
            } else if (elementName.contains("Mn")) {
                oxideName = "MnO";
            } else if (elementName.contains("Sr")) {
                oxideName = "SrO";
            } else if (elementName.contains("P")) {
                oxideName = "P2O5";
            }
            if (!oxideName.equals("")) {
                Oxide oxide = new Oxide(oxideName);
                Double percentValue = elementValue / oxide.ppmWeightRatio;
                percentValuesByOxideName.put(oxideName, percentValue);
                percentSum += percentValue;
            }
        }
        for (String oxideName : percentValuesByOxideName.keySet()) {
            Double oxidePercent = percentValuesByOxideName.get(oxideName);
            Double normalizedPercent = Math.round(10000.00 * (oxidePercent / percentSum)) / 100.00;
            percentValuesByOxideName.put(oxideName, normalizedPercent);
        }
        setAnorthite();
        updateHeaderAndRow();
    }

    void setAnorthite() {
        Double anorthite = percentValuesByOxideName.get("CaO") / (percentValuesByOxideName.get("CaO") + percentValuesByOxideName.get("Na2O") + percentValuesByOxideName.get("K2O"));
        percentValuesByOxideName.put("An", anorthite);
    }

    void updateHeaderAndRow() {
        for (String oxideName : percentValuesByOxideName.keySet()) {
            Double percentValue = percentValuesByOxideName.get(oxideName);
            header += ","  + oxideName;
            fullRow += "," + percentValue;
        }
    }

    @Override
    public int compareTo(PostProcessedRow comparedRow) {
        int distanceFromRim = (int) (this.distanceFromRim * 100);
        int comparedDistanceFromRim =  (int) (comparedRow.distanceFromRim * 100);
        return distanceFromRim - comparedDistanceFromRim;
    }
}
