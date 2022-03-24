package ru.netology.novser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    private static ArrayList<File> saveDatList = new ArrayList<>();

    public static void main(String[] args) {
        //Задача 1
        File saveDir = installGame();

        //Задача 2
        GameProgress save1 = new GameProgress(100, 2, 10, 5.5);
        GameProgress save2 = new GameProgress(82, 5, 30, 60.4);
        GameProgress save3 = new GameProgress(74, 3, 66, 222.1);

        saveGame(saveDir, save1, 1);
        saveGame(saveDir, save2, 2);
        saveGame(saveDir, save3, 3);

        zipFiles(saveDir);
        FilesAndDirectories.writeLog();
    }

    private static void saveGame(File saveDirectory, GameProgress save, int saveNumber) {
        File saveFile = FilesAndDirectories.createFile("save" + saveNumber + ".dat", saveDirectory);
        saveDatList.add(saveFile);
        try (FileOutputStream fos = new FileOutputStream(saveFile);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(save);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void zipFiles(File saveDirectory) {
        File zipFile = FilesAndDirectories.createFile("zip.zip", saveDirectory);
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipFile))) {
            for (File saveFile : saveDatList) {
                try (FileInputStream fis = new FileInputStream(saveFile)) {
                    ZipEntry entry = new ZipEntry(saveFile.getName());
                    zout.putNextEntry(entry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                    zout.closeEntry();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
                FilesAndDirectories.deleteFile(saveFile);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        saveDatList.clear();
    }

    public static File installGame() {
        File gamesDir = FilesAndDirectories.createDirectory("Games", new File(""));
        File srcDir = FilesAndDirectories.createDirectory("src", gamesDir);
        File resDir = FilesAndDirectories.createDirectory("res", gamesDir);
        File saveDir = FilesAndDirectories.createDirectory("savegames", gamesDir);
        File tempDir = FilesAndDirectories.createDirectory("temp", gamesDir);

        File mainSrcDir = FilesAndDirectories.createDirectory("main", srcDir);
        FilesAndDirectories.createDirectory("test", srcDir);
        FilesAndDirectories.createFile("Main.java", mainSrcDir);
        FilesAndDirectories.createFile("Utils.java", mainSrcDir);

        FilesAndDirectories.createDirectory("drawables", resDir);
        FilesAndDirectories.createDirectory("vectors", resDir);
        FilesAndDirectories.createDirectory("icons", resDir);

        FilesAndDirectories.createFile("temp.txt", tempDir);
        FilesAndDirectories.writeLog();

        return saveDir;
    }
}
