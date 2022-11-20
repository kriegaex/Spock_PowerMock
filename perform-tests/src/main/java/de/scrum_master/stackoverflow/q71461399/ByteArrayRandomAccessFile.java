package de.scrum_master.stackoverflow.q71461399;

import java.io.File;
import java.io.FileNotFoundException;

public class ByteArrayRandomAccessFile extends FixedElementSizeRandomAccessFile<byte[]> {
  public ByteArrayRandomAccessFile(String name, String mode, int elementSize)
    throws FileNotFoundException, IllegalElementSizeException
  {
    super(name, mode, elementSize);
  }

  public ByteArrayRandomAccessFile(String name, String mode, int elementSize, int bufferSize)
    throws FileNotFoundException, IllegalElementSizeException, IllegalBufferSizeException
  {
    super(name, mode, elementSize, bufferSize);
  }

  public ByteArrayRandomAccessFile(File file, String mode, int elementSize)
    throws FileNotFoundException, IllegalElementSizeException
  {
    super(file, mode, elementSize);
  }

  public ByteArrayRandomAccessFile(File file, String mode, int elementSize, int bufferSize)
    throws FileNotFoundException, IllegalElementSizeException, IllegalBufferSizeException
  {
    super(file, mode, elementSize, bufferSize);
  }

  @Override
  public byte[] toBytes(byte[] element) {
    return element;
  }

  @Override
  public byte[] fromBytes(byte[] bytes) {
    return bytes;
  }
}
