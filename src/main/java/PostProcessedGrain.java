import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

class PostProcessedGrain {
    String[] csvData;
    ArrayList<PostProcessedRow> postProcessedRows = new ArrayList<>();
    String dirpath = null;

    PostProcessedGrain(String csvFileAddress) {
        System.out.println(csvFileAddress);
        csvData = readCsv(csvFileAddress);
        for (String row : csvData) {
            PostProcessedRow postProcessedRow = new PostProcessedRow(row);
            postProcessedRows.add(postProcessedRow);
        }
        Collections.sort(postProcessedRows);
    }

    String[] readCsv(String csvFileAddress) {
        try {
            String csvContent = new String(Files.readAllBytes(Paths.get(csvFileAddress)));
            return csvContent.split("\n");
        } catch (IOException e){
            return null;
        }
    }

    void buildCsv(String fileName) {
        String csvString = "Line Number,Dist from rim,Li7,Na23,Mg24,Al27,Si29,P31,K39,Ca43,Ca44,Ti47,Mn55,Fe57,Cu65,Ga71,Sr88,Y89,Ba138,Ce140,Pb208,SiO2,Al2O3,FeO,CaO,Na2O,K2O,MgO,SrO,P2O5,MnO,TiO2,An";
        for (PostProcessedRow postProcessedRow : postProcessedRows) {
            if (postProcessedRow.isNotValueRow) {
                continue;
            }
            csvString += "\n" + postProcessedRow.fullRow;
        }
        try {
            FileWriter fileWriter = new FileWriter(getDirPath() + fileName + ".csv", false);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(csvString);
            printWriter.close();
        } catch (IOException e){

        }
    }

    private String getDirPath() {
        if (dirpath == null) {
            dirpath = new IO().getDirPath("csv-out", 0);
        }
        return dirpath;
    }

    void printRimValues() {
        String elementName = "Na23";
        Double sum = 0.0;
        int count = 0;
        int distanceFromRim = 10;
        for (PostProcessedRow processedRow : postProcessedRows) {
            if (processedRow.distanceFromRim > distanceFromRim) {
                continue;
            }
            String ppmValue = processedRow.ppmValueByElementName.get(elementName);
            if (ppmValue == null) {
                continue;
            }
            sum += Double.valueOf(ppmValue);
            count++;
        }
        Double average = sum / count;
        System.out.println( "For " + elementName + " average rim (in " + distanceFromRim + "mkm) value is " + average + " ppm for " + count + " measures");
    }
}
