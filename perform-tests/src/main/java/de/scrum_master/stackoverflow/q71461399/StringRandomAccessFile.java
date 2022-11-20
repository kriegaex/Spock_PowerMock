package de.scrum_master.stackoverflow.q71461399;

import java.io.File;
import java.io.FileNotFoundException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class StringRandomAccessFile extends FixedElementSizeRandomAccessFile<String> {
  public StringRandomAccessFile(String name, String mode, int elementSize)
    throws FileNotFoundException, IllegalElementSizeException
  {
    super(name, mode, checkElementSize(elementSize));
  }

  public StringRandomAccessFile(String name, String mode, int elementSize, int bufferSize)
    throws FileNotFoundException, IllegalElementSizeException, IllegalBufferSizeException
  {
    super(name, mode, checkElementSize(elementSize), bufferSize);
  }

  public StringRandomAccessFile(File file, String mode, int elementSize)
    throws FileNotFoundException, IllegalElementSizeException
  {
    super(file, mode, checkElementSize(elementSize));
  }

  public StringRandomAccessFile(File file, String mode, int elementSize, int bufferSize)
    throws FileNotFoundException, IllegalElementSizeException, IllegalBufferSizeException
  {
    super(file, mode, checkElementSize(elementSize), bufferSize);
  }

  private static int checkElementSize(int elementSize) {
    if (elementSize < 5)
      throw new IllegalElementSizeException(String.format(
        "element size must be 4 bytes (integer encoding the string length) + length in bytes of UTF-8 string," +
          "i.e. even for the simplest 1-byte UTF-8 string consisting of a US-ASCII character we need " +
          "no less than 5 bytes",
        elementSize
      ), 5, elementSize);
    return elementSize;
  }

  @Override
  public byte[] toBytes(String element) {
    final byte[] stringBytes = element.getBytes(UTF_8);
    final int length = stringBytes.length;
    if (length + 4 > elementSize)
      throw new IllegalElementSizeException(String.format(
        "string is too long for element size %d, it would need size %d",
        elementSize, length + 4
      ), elementSize, length + 4);
    final byte[] buffer = new byte[elementSize];
    buffer[0] = (byte) ((length >>> 24) & 0xFF);
    buffer[1] = (byte) ((length >>> 16) & 0xFF);
    buffer[2] = (byte) ((length >>> 8) & 0xFF);
    buffer[3] = (byte) (length & 0xFF);
    System.arraycopy(stringBytes, 0, buffer, 4, length);
    return buffer;
  }

  @Override
  public String fromBytes(byte[] bytes) {
    final int length = (bytes[0] << 24) + (bytes[1] << 16) + (bytes[2] << 8) + bytes[3];
    return new String(bytes, 4, length, UTF_8);
  }
}
