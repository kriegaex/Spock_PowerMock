package de.scrum_master.testing

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.security.SecureRandom

class TempFileUtility {
  private static final SecureRandom random = new SecureRandom()

  static Path tempPath(baseDir = "") {
    generatePath("", "", toPath(baseDir))
  }

  static File tempFile(baseDir = "") {
    tempPath(baseDir).toFile()
  }

  static String tempName(baseDir = "") {
    tempFile(baseDir).absolutePath
  }

  static Path getPath(String first, String... more) {
    Paths.get(first, more)
  }

  static File getFile(String first, String... more) {
    getPath(first, more).toFile()
  }

  static String getName(String first, String... more) {
    getFile(first, more).absolutePath
  }

  private static Path toPath(def pathRepresentation) {
    switch (pathRepresentation) {
      case Path: return pathRepresentation
      case File: return pathRepresentation.toPath()
      default: return Path.of(pathRepresentation as String)
    }
  }

  /**
   * Shamelessly copied from private static JRE method
   * {@link java.nio.file.TempFileHelper#generatePath(java.lang.String, java.lang.String, java.nio.file.Path)
   * TempFileHelper.generatePath}
   */
  private static Path generatePath(String prefix, String suffix, Path dir) {
    long n = random.nextLong()
    String s = prefix + Long.toUnsignedString(n) + suffix
    Path name = dir.getFileSystem().getPath(s)
    // the generated name should be a simple file name
    if (name.getParent() != null)
      throw new IllegalArgumentException("Invalid prefix or suffix")
    return dir.resolve(name)
  }
}
