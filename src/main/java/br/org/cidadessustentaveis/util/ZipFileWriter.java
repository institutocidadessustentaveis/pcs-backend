package br.org.cidadessustentaveis.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFileWriter {

    public static String zip(String... filePaths) throws IOException {
        if(filePaths == null) throw new IllegalArgumentException("Path list cannot be null");

        File firstFile = new File(filePaths[0]);
        String zipFileName = firstFile.getName().concat(".zip");

        FileOutputStream fos = new FileOutputStream(zipFileName);
        ZipOutputStream zos = new ZipOutputStream(fos);

        for (String aFile : filePaths) {
            zos.putNextEntry(new ZipEntry(new File(aFile).getName()));

            byte[] bytes = Files.readAllBytes(Paths.get(aFile));
            zos.write(bytes, 0, bytes.length);
            zos.closeEntry();
        }

        zos.close();

        return zipFileName;
    }

    public static String zip(List<String> filePaths) throws IOException {
        if(filePaths == null) throw new IllegalArgumentException("Path list cannot be null");
        if(filePaths.isEmpty()) throw new IllegalArgumentException("Path list cannot be empty");

        String[] paths = new String[filePaths.size()];
        filePaths.toArray(paths);
        return ZipFileWriter.zip(paths);
    }

}
