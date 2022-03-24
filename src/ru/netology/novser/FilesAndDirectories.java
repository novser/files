package ru.netology.novser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FilesAndDirectories {

    private static StringBuilder log = new StringBuilder();
    private static final String logDirectoryCreateSuccess = "%s - Папка %s была успешно создана в директории %s\n";
    private static final String logDirectoryCreateFail = "%s - Не удалось создать папку %s в директории %s\n";
    private static final String logFileCreateSuccess = "%s - Файл %s был успешно создан в директории %s\n";
    private static final String logFileCreateFail = "%s - Не удалось создать файл %s в директории %s. %s\n";
    private static final String logFileDeleteSuccess = "%s - Файл %s был успешно удален в директории %s\n";
    private static final String logFileDeleteFail = "%s - Не удалось удалить файл %s в директории %s\n";
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private static File tempFileOfLogs;

    public static File createFile(String fileName, File mainDir) {
        File file = new File(mainDir, fileName);
        try {
            if (file.createNewFile()) {
                log.append(String.format(logFileCreateSuccess, currentTime(), fileName, mainDir.getAbsolutePath()));
            } else {
                throw new Exception("Файл уже существует");
            }
        } catch (Exception exception) {
            log.append(String.format(logFileCreateFail, currentTime(), fileName, mainDir.getAbsolutePath(), exception.getMessage()));
        }

        if (fileName.equals("temp.txt")) {
            tempFileOfLogs = file;
        }
        return file;
    }

    public static File createDirectory(String directoryName, File mainDir) {
        File dir = directoryName.equals("Games") ? new File(directoryName) : new File(mainDir, directoryName);
        String stringLog = dir.mkdir() ? logDirectoryCreateSuccess : logDirectoryCreateFail;
        log.append(String.format(stringLog, currentTime(), directoryName, mainDir.getAbsolutePath()));
        return dir;
    }

    private static String currentTime() {
        return formatter.format(new Date());
    }

    public static void writeLog() {
        try (FileWriter writer = new FileWriter(tempFileOfLogs)) {
            writer.write(log.toString());
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void deleteFile(File file) {
        String stringLog = file.delete() ? logFileDeleteSuccess : logFileDeleteFail;
        log.append(String.format(stringLog, currentTime(), file.getName(), file.getParentFile().getAbsolutePath()));
    }
}

