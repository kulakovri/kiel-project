import java.io.File;

public class IO {
    private static String transactionDirPath = null;

    IO() {

    }

    String getDirPath(String folderName, int dirNumber) {
        if (transactionDirPath != null) {
            return transactionDirPath;
        }
        String dirPath = "./" + folderName + "/" + dirNumber + "/";
        if (new File(dirPath).exists()) {
            dirNumber++;
            return getDirPath(folderName, dirNumber);
        } else {
            new File(dirPath).mkdirs();
            transactionDirPath = dirPath;
            return dirPath;
        }
    }
}
