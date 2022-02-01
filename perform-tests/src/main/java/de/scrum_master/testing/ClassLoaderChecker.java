package de.scrum_master.testing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassLoaderChecker {
  private static ClassLoader contextClassLoader;
  private static Method findLoadedClassMethod;

  static {
    contextClassLoader = Thread.currentThread().getContextClassLoader();
    try {
      findLoadedClassMethod = ClassLoader.class.getDeclaredMethod("findLoadedClass", String.class);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
    findLoadedClassMethod.setAccessible(true);
  }

  public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
    isClassLoaded("java.lang.String");
    isClassLoaded("java.net.URL");
    isClassLoaded("java.net.URI");
    isClassLoaded("java.util.Properties");
    isClassLoaded("java.io.InputStream");
    isClassLoaded("java.io.FileInputStream");
  }

  private static boolean isClassLoaded(String className) {
    boolean isLoaded;
    try {
      isLoaded = findLoadedClassMethod.invoke(contextClassLoader, className) != null;
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
    System.out.println(className + " -> " + isLoaded);
    return isLoaded;
  }
}
