import java.util.ArrayList;
import java.util.HashMap;

public class PostProcessedRow implements Comparable<PostProcessedRow> {
    String header = "Line Number,Dist from rim,Li7,Na23,Mg24,Al27,Si29,P31,K39,Ca43,Ca44,Ti47,Mn55,Fe57,Cu65,Ga71,Sr88,Y89,Ba138,Ce140,Pb208";
    String fullRow;
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
            setPercentValuesByOxideName();
        }
    }

    void setPercentValuesByOxideName() {
        Double percentSum = 0.0;
        for (String elementName : ppmValueByElementName.keySet()) {
            Double elementValue = Double.valueOf(ppmValueByElementName.get(elementName));
            String formattedElementName = elementName.replaceAll("[0-9]", "");
            String oxideName = "";
            if (formattedElementName.equalsIgnoreCase("Si")) {
                oxideName = "SiO2";
            } else if (formattedElementName.equalsIgnoreCase("Al")) {
                oxideName = "Al2O3";
            } else if (formattedElementName.equalsIgnoreCase("Fe")) {
                oxideName = "FeO";
            } else if (formattedElementName.equalsIgnoreCase("Ca")) {
                oxideName = "CaO";
            } else if (formattedElementName.equalsIgnoreCase("Na")) {
                oxideName = "Na2O";
            } else if (formattedElementName.equalsIgnoreCase("K")) {
                oxideName = "K2O";
            } else if (formattedElementName.equalsIgnoreCase("Mg")) {
                oxideName = "MgO";
            } else if (formattedElementName.equalsIgnoreCase("Ti")) {
                oxideName = "TiO2";
            } else if (formattedElementName.equalsIgnoreCase("Mn")) {
                oxideName = "MnO";
            } else if (formattedElementName.equalsIgnoreCase("Sr")) {
                oxideName = "SrO";
            } else if (formattedElementName.equalsIgnoreCase("P")) {
                oxideName = "P2O5";
            }
            if (!oxideName.equals("")) {
                Oxide oxide = new Oxide(oxideName);
                Double percentValue = elementValue / oxide.ppmWeightRatio;
                percentValuesByOxideName.put(oxideName, percentValue);
                if (!elementName.equals("Ca43")) {
                    percentSum += percentValue;
                }
            }
        }
        for (String oxideName : percentValuesByOxideName.keySet()) {
            Double oxidePercent = percentValuesByOxideName.get(oxideName);
            Double normalizedPercent = Math.round(100000.00 * (oxidePercent / percentSum)) / 1000.00;
            percentValuesByOxideName.put(oxideName, normalizedPercent);
        }
        setAnorthite();
        updateHeaderAndRow();
    }

    void setAnorthite() {
        Double anorthite = percentValuesByOxideName.get("CaO") / (percentValuesByOxideName.get("CaO") + percentValuesByOxideName.get("Na2O") + percentValuesByOxideName.get("K2O"));
        anorthite = Math.round(anorthite * 10000.00) / 100.00;
        percentValuesByOxideName.put("An", anorthite);
    }

    void updateHeaderAndRow() {
        for (String oxideName : new String[]{"SiO2","Al2O3","FeO","CaO","Na2O","K2O","MgO","SrO","P2O5","MnO","TiO2","An"}) {
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
