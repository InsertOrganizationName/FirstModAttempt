package gmod.utils;

import anvil.particle.BaseParticle;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassReflectionMagic {

    public static <T> Collection<T> getSubclasses(Class<T> clazz, String pkgname) {
        Collection<T> subclasses = new ArrayList<>();

        try {
            ArrayList<Class<?>> classesForPackage = new ArrayList<>(getClassesForPackage(clazz, pkgname));

            for (Class<?> particle : classesForPackage) {
                try {
                    //noinspection unchecked
                    subclasses.add((T)particle.newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return subclasses;
    }

    private static ArrayList<Class<?>> getClassesForPackage(Class inheritCheckClass, String pkgname) {
        ArrayList<Class<?>> classes = new ArrayList<>();
        // Get a File object for the package
        File directory;
        String fullPath;
        String relPath = pkgname.replace('.', '/');
        System.out.println("ClassDiscovery: Package: " + pkgname + " becomes Path:" + relPath);
        URL resource = ClassLoader.getSystemClassLoader().getResource(relPath);
        System.out.println("ClassDiscovery: Resource = " + resource);
        if (resource == null) {
            throw new RuntimeException("No resource for " + relPath);
        }
        fullPath = resource.getFile();
        System.out.println("ClassDiscovery: FullPath = " + resource);

        try {
            directory = new File(resource.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(pkgname + " (" + resource + ") does not appear to be a valid URL / URI.  Strange, since we got it from the system...", e);
        } catch (IllegalArgumentException e) {
            directory = null;
        }
        System.out.println("ClassDiscovery: Directory = " + directory);

        if (directory != null && directory.exists()) {
            // Get the list of the files contained in the package
            String[] files = directory.list();
            assert files != null;
            for (String file : files) {
                // we are only interested in .class files
                if (file.endsWith(".class")) {
                    // removes the .class extension
                    String className = pkgname + '.' + file.substring(0, file.length() - 6);
                    getClass(className, inheritCheckClass).ifPresent(classes::add);
                }
            }
        }
        else {
            try {
                String jarPath = fullPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
                JarFile jarFile = new JarFile(jarPath);
                Enumeration<JarEntry> entries = jarFile.entries();
                while(entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String entryName = entry.getName();
                    if(entryName.startsWith(relPath) && entryName.length() > (relPath.length() + "/".length())) {
                        System.out.println("ClassDiscovery: JarEntry: " + entryName);
                        String className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
                        getClass(className, inheritCheckClass).ifPresent(classes::add);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(pkgname + " (" + directory + ") does not appear to be a valid package", e);
            }
        }
        return classes;
    }

    private static Optional<Class> getClass(String className, Class<?> inheritCheckClass) {
        System.out.println("ClassDiscovery: className = " + className);
        try {
            Class<?> possibleClass = Class.forName(className);
            if (inheritCheckClass.isAssignableFrom(possibleClass)
                    && possibleClass != BaseParticle.class) {
                return Optional.of(possibleClass);
            }
            return Optional.empty();
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException("ClassNotFoundException loading " + className);
        }
    }
}
