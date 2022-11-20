package de.scrum_master.stackoverflow.q71461399;

import java.io.File;
import java.io.FileNotFoundException;

public class IntegerRandomAccessFile extends FixedElementSizeRandomAccessFile<Integer> {
  public IntegerRandomAccessFile(String name, String mode)
    throws FileNotFoundException
  {
    super(name, mode, 4);
  }

  public IntegerRandomAccessFile(String name, String mode, int bufferSize)
    throws FileNotFoundException, IllegalBufferSizeException
  {
    super(name, mode, 4, bufferSize);
  }

  public IntegerRandomAccessFile(File file, String mode)
    throws FileNotFoundException
  {
    super(file, mode, 4);
  }

  public IntegerRandomAccessFile(File file, String mode, int bufferSize)
    throws FileNotFoundException, IllegalBufferSizeException
  {
    super(file, mode, 4, bufferSize);
  }

  @Override
  public byte[] toBytes(Integer element) {
    return new byte[] {
      (byte) ((element >>> 24) & 0xFF),
      (byte) ((element >>> 16) & 0xFF),
      (byte) ((element >>> 8) & 0xFF),
      (byte) (element & 0xFF)
    };
  }

  @Override
  public Integer fromBytes(byte[] bytes) {
    return (bytes[0] << 24) + (bytes[1] << 16) + (bytes[2] << 8) + bytes[3];
  }
}
