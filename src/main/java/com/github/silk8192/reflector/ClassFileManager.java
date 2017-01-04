package com.github.silk8192.reflector;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Handles the loading of classes from either a local .jar file or the root directory of many classes.
 *
 * @author silk8192
 */
public class ClassFileManager {

    private HashMap<String, Class<?>> classes = new HashMap<>();

    public ClassFileManager(File jarFile) {
        try {
            URLClassLoader loader = new URLClassLoader(new URL[]{jarFile.toPath().toUri().toURL()});
            Enumeration entries = new ZipFile(jarFile).entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (FilenameUtils.getExtension(entry.getName()).equals("class") && !entry.getName().contains("$")) {
                    String className = entry.getName().replace("\\", ".").replace(".class", "").replace("/", ".");
                    this.classes.put(className, loader.loadClass(className));
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ClassFileManager(Path classPath) {
        try {
            Predicate<Path> fileFilter = f -> Files.isRegularFile(f.toAbsolutePath()) && FilenameUtils.getExtension(f.toString()).equals("class");
            Files.walk(classPath).filter(fileFilter)
                    .forEach(classFile -> {
                        String className = FilenameUtils.removeExtension(classPath.relativize(classFile).toString());
                        //replace forward slashes in path to "."
                        className = className.replace("\\", ".");
                        try {
                            ClassLoader cr = new URLClassLoader(new URL[]{classFile.toUri().toURL()});
                            Class<?> classFileObject = cr.loadClass(className);
                            classes.put(className, classFileObject);
                        } catch (MalformedURLException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Class<?> getClass(String canonicalClassName) {
        return this.classes.get(canonicalClassName);
    }
}