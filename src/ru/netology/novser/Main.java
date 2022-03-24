package ru.netology.novser;

import java.io.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
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

        File zipFile = zipFiles(saveDir);
        FilesAndDirectories.writeLog();

        //Задача 3
        openZip(zipFile, saveDir);
        GameProgress save = openProgress(saveDatList.get(1));
        System.out.println(save);
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

    private static File zipFiles(File saveDirectory) {
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
        return zipFile;
    }

    private static File installGame() {
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

    private static void openZip(File zipFile, File saveDir) {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zin.getNextEntry()) != null) {
                File saveFile = FilesAndDirectories.createFile(entry.getName(), saveDir);
                FileOutputStream fout = new FileOutputStream(saveFile);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
                saveDatList.add(saveFile);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        FilesAndDirectories.deleteFile(zipFile);
    }

    private static GameProgress openProgress(File saveDat) {
        GameProgress save = null;
        try (FileInputStream fis = new FileInputStream(saveDat);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            save = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return save;
    }
}
