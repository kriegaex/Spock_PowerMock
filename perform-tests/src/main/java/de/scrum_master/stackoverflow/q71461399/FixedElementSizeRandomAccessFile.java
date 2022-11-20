package de.scrum_master.stackoverflow.q71461399;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.arraycopy;

public abstract class FixedElementSizeRandomAccessFile<T> extends RandomAccessFile implements AutoCloseable {
  protected final int elementSize;
  protected final int bufferSize;
  private final int halfBufferSize;
  private final byte[] buffer;

  public FixedElementSizeRandomAccessFile(String name, String mode, int elementSize)
    throws FileNotFoundException, IllegalElementSizeException
  {
    this(name, mode, elementSize, getDefaultBufferSize(elementSize));
  }

  public FixedElementSizeRandomAccessFile(String name, String mode, int elementSize, int bufferSize)
    throws FileNotFoundException, IllegalElementSizeException, IllegalBufferSizeException
  {
    this(name != null ? new File(name) : null, mode, elementSize, bufferSize);
  }

  public FixedElementSizeRandomAccessFile(File file, String mode, int elementSize)
    throws FileNotFoundException, IllegalElementSizeException
  {
    this(file, mode, elementSize, getDefaultBufferSize(elementSize));
  }

  public FixedElementSizeRandomAccessFile(File file, String mode, int elementSize, int bufferSize)
    throws FileNotFoundException, IllegalElementSizeException, IllegalBufferSizeException
  {
    super(file, mode);
    if (elementSize < 1)
      throw new IllegalElementSizeException(String.format("element size %d must be > 0", elementSize), 1, elementSize);
    if (bufferSize < 1)
      throw new IllegalBufferSizeException(String.format("buffer size %d must be > 0", bufferSize));
    if (bufferSize % (2 * elementSize) != 0)
      throw new IllegalBufferSizeException(String.format(
        "buffer size %d must be a multiple of %d (2 times element size %d)",
        bufferSize, 2 * elementSize, elementSize
      ));
    this.elementSize = elementSize;
    this.bufferSize = bufferSize;
    halfBufferSize = bufferSize / 2;
    buffer = new byte[bufferSize];
  }

  public abstract byte[] toBytes(T element);

  public abstract T fromBytes(byte[] bytes);

  public static int getDefaultBufferSize(int elementSize) throws IllegalElementSizeException {
    if (elementSize < 1)
      throw new IllegalElementSizeException(String.format(
        "element size %d must be > 0", elementSize
      ), 1, elementSize);
    return 1024 * 1024 / (2 * elementSize) * (2 * elementSize);
  }

  public synchronized void writeElements(List<T> elements) throws IOException, IllegalElementSizeException {
    for (T element : elements)
      writeElement(element);
  }

  public synchronized void writeElement(T element) throws IOException, IllegalElementSizeException {
    byte[] bytes = toBytes(element);
    if (bytes.length != elementSize)
      throw new IllegalElementSizeException(String.format(
        "element size %d must be %d", bytes.length, elementSize
      ), elementSize, bytes.length);
    write(bytes);
  }

  public synchronized List<T> readElements() throws IOException, IllegalElementSizeException {
    return readElements(Integer.MAX_VALUE);
  }

  public synchronized List<T> readElements(int maxNumElements) throws IOException, IllegalElementSizeException {
    final List<T> elements = new ArrayList<>();
    int elementsRead = 0;
    while (elementsRead < maxNumElements) {
      try {
        elements.add(readElement());
      }
      catch (EOFException e) {
        break;
      }
    }
    return elements;
  }

  /**
   * @return
   * @throws EOFException
   * @throws IOException
   */
  public synchronized T readElement() throws IOException {
    final byte[] bytes = new byte[elementSize];
    int bytesRead = read(bytes);
    if (bytesRead == -1)
      throw new EOFException("end of file, cannot read element");
    if (bytesRead != elementSize)
      throw new IllegalElementSizeException(String.format(
        "element size must be %d, could only read %d", elementSize, bytesRead
      ), elementSize, bytesRead);
    return fromBytes(bytes);
  }

  public synchronized void reverseElements() throws IOException, IllegalFileSizeException {
    final long fileSize = length();
    if (fileSize == 0)
      return;
    if (fileSize % elementSize != 0)
      throw new IllegalFileSizeException(String.format(
        "illegal file size %d, must be a multiple of %d (element size)",
        fileSize, elementSize
      ));
    seek(0);
    long seekLeft = 0;
    long seekRight = fileSize - halfBufferSize;
    while (seekRight - seekLeft >= halfBufferSize) {
      // Read left/right file ranges into corresponding buffer halves
      readToBuffer(seekLeft, 0, halfBufferSize);
      readToBuffer(seekRight, halfBufferSize, halfBufferSize);
      // Reverse buffer
      reverseBuffer(bufferSize);
      // Write buffer halves into corresponding left/right file ranges
      writeFromBuffer(seekLeft, 0, halfBufferSize);
      writeFromBuffer(seekRight, halfBufferSize, halfBufferSize);
      // Move file pointers inwards, closer to the centre of the file
      seekLeft += halfBufferSize;
      seekRight -= halfBufferSize;
    }
    // Remaining bytes from the centre of the file fit into buffer -> read, reverse and write back
    final int numBytesRemaining = (int) (seekRight + halfBufferSize - seekLeft);
    readToBuffer(seekLeft, 0, numBytesRemaining);
    reverseBuffer(numBytesRemaining);
    writeFromBuffer(seekLeft, 0, numBytesRemaining);
  }

  private void readToBuffer(long seekPosition, int offset, int numBytes) throws IOException {
    seek(seekPosition);
    read(buffer, offset, numBytes);
  }

  private void reverseBuffer(int numBytes) {
    byte[] tempElement = new byte[elementSize];
    int leftOffset = 0;
    int rightOffset = numBytes - elementSize;
    while (leftOffset < rightOffset) {
      // Swap elements: temp = left, left = right, right = temp
      arraycopy(buffer, leftOffset, tempElement, 0, elementSize);
      arraycopy(buffer, rightOffset, buffer, leftOffset, elementSize);
      arraycopy(tempElement, 0, buffer, rightOffset, elementSize);
      // Move offsets closer to the middle
      leftOffset += elementSize;
      rightOffset -= elementSize;
    }
  }

  private void writeFromBuffer(long seekPosition, int offset, int numBytes) throws IOException {
    seek(seekPosition);
    write(buffer, offset, numBytes);
  }

  public static class IllegalElementSizeException extends RuntimeException {
    public final int requiredSize;
    public final int actualSize;

    public IllegalElementSizeException(String message, int requiredSize, int actualSize) {
      super(message);
      this.requiredSize = requiredSize;
      this.actualSize = actualSize;
    }
  }

  public static class IllegalBufferSizeException extends RuntimeException {
    public IllegalBufferSizeException(String message) {
      super(message);
    }
  }

  public static class IllegalFileSizeException extends RuntimeException {
    public IllegalFileSizeException(String message) {
      super(message);
    }
  }

}
