import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class CSVLoader {
    private static final String[] listOfMetals = new String[]{
            "Al", "Ba", "Ca", "Ce", "Co", "Cr", "Cu", "Dy", "Eu", "Fe", "Ga", "Gd", "K", "La", "Li", "Mg", "Mn", "Na",
            "Nd", "Pb", "Pr", "P", "Rb", "Sc", "Si", "Sm", "Sr", "Ti", "V", "Y", "Zn", "Ru", "Nb", "Ta", "Tb", "Te", "Th",
            "Tm", "U", "W", "Zr", "Mo", "Ho", "Hf", "Bi", "Ag", "Lu", "Yb", "Er", "Rh", "Ni", "Re", "Sb", "Sn", "Pd",
            "Ir", "Pt"};

    private static final String unparsedMolarWeights =
            "'O': 15.9994, 'Na': 22.99, 'Mg': 24.305, 'Al': 26.9815, " +
                    "'Si': 28.086, 'P': 30.9738, 'K': 39.098, 'Ca': 40.078," +
                    " 'Ti': 47.867, 'V': 50.9415, 'Cr': 51.996, 'Mn': 54.938, " +
                    "'Fe': 55.845, 'Ni': 58.693, 'Sr': 103.62";

    static List<List<String>> loadCsv(String csvAddress) {
        return loadProfileData(csvAddress);
    }

    private static List<List<String>> loadProfileData(String csvAddress) {
        CSVReader csvReader = readCsvFile(csvAddress);
        List<String[]> profileRows = transformCsv(csvReader);
        return convertToListsOfListofString(profileRows);
    }

    private static CSVReader readCsvFile(String csvAddress) {
        try {
            return new CSVReader(new FileReader(csvAddress));
        } catch (IOException e) {
            return null;
        }
    }

    private static List<String[]> transformCsv(CSVReader csvReader) {
        try {
            try {
                return csvReader.readAll();
            } catch (IOException e) {
                return null;
            }
        } catch (CsvException e) {
            return null;
        }
    }

    private static List<List<String>> convertToListsOfListofString(List<String[]> profileRows) {
        int startingColumnIndex = getStaringColumnIndex(profileRows);
        List<List<String>> convertedEntry = new ArrayList<>();
        for (String[] row : profileRows) {
            List<String> convertedRow = new ArrayList<>();
            int iteration = 0;
            for (String column : row) {
                if (iteration >= startingColumnIndex) {
                    convertedRow.add(column);
                }
                iteration++;
            }
            if (convertedRow.size() > 5) convertedEntry.add(convertedRow);
        }
        return convertedEntry;
    }

    private static int getStaringColumnIndex(List<String[]> profileRows) {
        int startingColumnIndex = 0;
        for (String[] row : profileRows) {
            if (isElementNameHeaderRow(row)) {
                for (String column : row) {
                    if (ArrayUtils.contains(listOfMetals, column.replaceAll("[0-9]", ""))) {
                        break;
                    }
                    startingColumnIndex++;
                }
            }
        }
        return startingColumnIndex;
    }

    private static boolean isElementNameHeaderRow(String[] row) {
        return ArrayUtils.contains(row, "Al27");
    }

    static ArrayList<String> getListOfCsvFiles() {
        File folder = new File("csv/");
        File[] listOfFiles = folder.listFiles();

        ArrayList<String> fileNames = new ArrayList<>();
        for (File file : listOfFiles) {
            fileNames.add(file.getName());
        }
        Collections.sort(fileNames);
        return fileNames;
    }
}
