import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class PostProcessedGrain {
    List<List<String>> csvData;
    ArrayList<PostProcessedRow> postProcessedRows = new ArrayList<>();
    String dirpath = null;

    PostProcessedGrain(String csvFileAddress) {
        csvData = CSVLoader.loadCsv(csvFileAddress);
        for (List<String> row : csvData) {
            PostProcessedRow postProcessedRow = new PostProcessedRow(row);
            postProcessedRows.add(postProcessedRow);
        }
        Collections.sort(postProcessedRows);
    }

    void buildCsv(String fileName) {
        String csvString = "Line Number,Dist from rim,Li7,Na23,Mg24,Al27,Si29,P31,K39,Ca43,Ca44,Ti47,Mn55,Fe57,Cu65,Ga71,Sr88,Y89,Ba138,Ce140,Pb208";
        for (PostProcessedRow postProcessedRow : postProcessedRows) {
            csvString.concat("\n" + postProcessedRow.getString());
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

}
