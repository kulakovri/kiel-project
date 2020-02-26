import java.util.HashMap;
import java.util.List;

public class PostProcessedRow implements Comparable<PostProcessedRow> {
    String header = "Line Number,Dist from rim,Li7,Na23,Mg24,Al27,Si29,P31,K39,Ca43,Ca44,Ti47,Mn55,Fe57,Cu65,Ga71,Sr88,Y89,Ba138,Ce140,Pb208";
    int rowsSize = 21;
    Double distanceFromRim;
    String lineNumber;
    HashMap<String, String> ppmValueByElementName = new HashMap<>();
    Boolean isNotValueRow = false;

    PostProcessedRow(List<String> rowValues) {
        String firstRowValue = rowValues.get(0);
        if (firstRowValue == null || firstRowValue.equals("Line Number")) {
            isNotValueRow = true;
        } else {
            String[] headerElements = header.split(",");
            for (int columnIndex = 0 ; columnIndex < rowsSize ; columnIndex++) {
                String rowValue = rowValues.get(columnIndex);
                if (columnIndex == 0) {
                    lineNumber = rowValue;
                } else if (columnIndex == 1) {
                    distanceFromRim = Double.valueOf(rowValue);
                } else {
                    ppmValueByElementName.put(headerElements[columnIndex], rowValue);
                }
            }
        }
    }

    @Override
    public int compareTo(PostProcessedRow comparedRow) {
        int distanceFromRim = (int) (this.distanceFromRim * 100);
        int comparedDistanceFromRim =  (int) (comparedRow.distanceFromRim * 100);
        return distanceFromRim - comparedDistanceFromRim;
    }

    String getString() {
       return lineNumber + "," + distanceFromRim+ "," + String.join(",", ppmValueByElementName.values());
    }
}
