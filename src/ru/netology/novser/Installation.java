package ru.netology.novser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Installation {

    private static StringBuilder log = new StringBuilder();
    private static final String logDirectorySuccess = "%s - Папка %s была успешно создана в директории %s\n";
    private static final String logDirectoryFail = "%s - Не удалось создать папку %s в директории %s\n";
    private static final String logFileSuccess = "%s - Файл %s был успешно создан в директории %s\n";
    private static final String logFailFail = "%s - Не удалось создать файл %s в директории %s. %s\n";
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public static void installGame() {
        File gamesDir = createDirectory("Games", new File(""));
        File srcDir = createDirectory("src", gamesDir);
        File resDir = createDirectory("res", gamesDir);
        createDirectory("savegames", gamesDir);
        File tempDir = createDirectory("temp", gamesDir);

        File mainSrcDir = createDirectory("main", srcDir);
        createDirectory("test", srcDir);
        createFile("Main.java", mainSrcDir);
        createFile("Utils.java", mainSrcDir);

        createDirectory("drawables", resDir);
        createDirectory("vectors", resDir);
        createDirectory("icons", resDir);

        File tempFile = createFile("temp.txt", tempDir);
        writeLog(tempFile);
    }

    private static File createFile(String fileName, File mainDir) {
        File file = new File(mainDir, fileName);
        try {
            if (file.createNewFile()) {
                log.append(String.format(logFileSuccess, currentTime(), fileName, mainDir.getAbsolutePath()));
            } else {
                throw new Exception("Файл уже существует");
            }
        } catch (Exception exception) {
            log.append(String.format(logFailFail, currentTime(), fileName, mainDir.getAbsolutePath(), exception.getMessage()));
        }

        return file;
    }

    private static File createDirectory(String directoryName, File mainDir) {
        File dir = directoryName.equals("Games") ? new File(directoryName) : new File(mainDir, directoryName);
        String stringLog = dir.mkdir() ? logDirectorySuccess : logDirectoryFail;
        log.append(String.format(stringLog, currentTime(), directoryName, mainDir.getAbsolutePath()));
        return dir;
    }

    private static String currentTime() {
        return formatter.format(new Date());
    }

    private static void writeLog(File tempFile) {
        try (FileWriter writer = new FileWriter(tempFile)) {// запись всей строки
            writer.write(log.toString());// запись по символам
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
