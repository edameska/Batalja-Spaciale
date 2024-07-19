import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.*;

public class Parser {
    public static void main(String[] args) {
        String zipFilePath1 = "/home/edameska/Desktop/2024_02_05_games.zip";
        String zipFilePath2 = "/home/edameska/Desktop/2024_02_17_games.zip";
        String destDirectory = "/home/edameska/Desktop/AI project/Batalja-Spaciale";

        //random bot IDs
         Set<String> randomBotIDs = new HashSet<>(Arrays.asList(
            "04b6ddfa-58b8-4477-8a3a-8e5c1981326d",
            "f172d26a-76f2-4718-88e2-9a9ea035ecd9",
            "1d5b085b-044a-4c65-9c72-fc4bb60d76c4",
            "720e5c79-1f5b-4198-8d91-5403b07ea838",
            "cfe82210-7b49-4d98-9912-c6f9f62b0a3e",
            "95a5b542-8fc4-490b-a1cc-62eff07b954b",
            "6980f103-e102-4a58-bde0-d59669f28be6",
            "23d7836e-aced-4354-9cec-dfd056541dee"
        ));


        try {
            unzipAndParseLogs(zipFilePath1, destDirectory, randomBotIDs);
            unzipAndParseLogs(zipFilePath2, destDirectory, randomBotIDs);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     public static void unzipAndParseLogs(String zipFilePath, String destDirectory, Set<String> randomBotIDs) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        byte[] buffer = new byte[1024];
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                String fileName = zipEntry.getName();
                if (fileName.endsWith(".txt")) {
                    File newFile = new File(destDirectory + File.separator + fileName);
                    new File(newFile.getParent()).mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                    parseLogFile(newFile, randomBotIDs);
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        }
    }

    public static void parseLogFile(File file, Set<String> randomBotIDs) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean gameCompleted = false;
            List<String> botIds = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                if (line.startsWith("C ")) {
                    String[] parts = line.split(" ");
                    if (parts.length >= 4) {
                        String botId = parts[2].split("/")[5];
                        botIds.add(botId);
                    }
                }
                if (line.contains("summary")) {
                    gameCompleted = true;
                }
            }

            if (gameCompleted) {
                for (String botId : botIds) {
                    if (!randomBotIDs.contains(botId)) {
                        System.out.println("Game ID: " + file.getName() + " - Bot ID: " + botId);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
