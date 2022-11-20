package de.scrum_master.stackoverflow.q71461399

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.TempDir
import spock.lang.Unroll

import java.nio.file.Path

import static de.scrum_master.testing.TempFileUtility.tempFile
import static de.scrum_master.testing.TempFileUtility.tempName
import static de.scrum_master.stackoverflow.q71461399.FixedElementSizeRandomAccessFile.IllegalElementSizeException
import static de.scrum_master.stackoverflow.q71461399.FixedElementSizeRandomAccessFile.IllegalFileSizeException
import static de.scrum_master.stackoverflow.q71461399.FixedElementSizeRandomAccessFile.IllegalBufferSizeException

class ByteArrayRandomAccessFileTest extends Specification {
  def static final ELEMENT_SIZE = 4

  @TempDir
  @Shared
  Path tempDir

  def fileName = tempName(tempDir)

  def "conversion functions return original data"() {
    given:
    def randomAccessFile = new ByteArrayRandomAccessFile(tempName(tempDir), "rw", 3)
    byte[] bytes = [1, 2, 3]

    expect:
    randomAccessFile.toBytes(bytes) == bytes
    randomAccessFile.fromBytes(bytes) == bytes

    cleanup:
    randomAccessFile.close()
  }

  def "cannot write elements of wrong byte size"() {
    given:
    def randomAccessFile = new ByteArrayRandomAccessFile(tempName(tempDir), "rw", 3)

    when:
    randomAccessFile.writeElement([1, 2] as byte[])

    then:
    thrown IllegalElementSizeException

    cleanup:
    randomAccessFile.close()
  }

  def "cannot read elements of wrong byte size"() {
    given:
    def randomAccessFile = new ByteArrayRandomAccessFile(tempName(tempDir), "rw", 3)
    randomAccessFile.length = 2

    when:
    randomAccessFile.readElement()

    then:
    thrown IllegalElementSizeException

    cleanup:
    randomAccessFile.close()
  }

  @Unroll("#type path, element size #elementSize, buffer size #bufferSize")
  def "element size must be positive"() {
    when: "trying to create a random access file with illegal element size"
    new ByteArrayRandomAccessFile(*arguments)

    then: "an IllegalElementSizeException is thrown"
    thrown IllegalElementSizeException

    where:
    type     | file              | elementSize | bufferSize
    "String" | tempName(tempDir) | 0           | "default"
    "String" | tempName(tempDir) | -1          | 1024
    "File"   | tempFile(tempDir) | -2          | "default"
    "File"   | tempFile(tempDir) | -99         | 1024

    arguments = [file, "rw", elementSize, bufferSize] - "default"
  }

  @Unroll("buffer size #bufSize is illegal")
  def "buffer size must be a positive multiple of 2 times element size"(int bufSize) {
    when: "trying to create a random access file with illegal buffer size"
    new ByteArrayRandomAccessFile(fileName, "rw", ELEMENT_SIZE, bufSize)

    then: "an IllegalBufferSizeException is thrown"
    thrown IllegalBufferSizeException

    where: "we use all but the permitted buffer sizes"
    bufSize << (0..4 * ELEMENT_SIZE).toArray() - [2 * ELEMENT_SIZE, 4 * ELEMENT_SIZE]
  }

  def "cannot write into a closed file"() {
    given: "a closed random access file"
    def randomAccessFile = new ByteArrayRandomAccessFile(fileName, "rw", ELEMENT_SIZE)
    randomAccessFile.close()

    when: "trying to write elements"
    randomAccessFile.writeElements([(1..ELEMENT_SIZE).toArray() as byte[]])

    then: "an IOException is thrown"
    thrown IOException
  }

  def "cannot write into a read-only file"() {
    given: "a read-only random access file"
    new File(fileName).createNewFile()
    def randomAccessFile = new ByteArrayRandomAccessFile(fileName, "r", ELEMENT_SIZE)

    when: "trying to write a stream of integers"
    randomAccessFile.writeElements([(1..ELEMENT_SIZE).toArray() as byte[]])

    then: "an IOException is thrown"
    thrown IOException

    cleanup:
    randomAccessFile.close()
  }

  def "file size is no multiple of element size"() {
    given: "a random access file with implausible file size"
    def randomAccessFile = new ByteArrayRandomAccessFile(fileName, "rw", ELEMENT_SIZE)
    randomAccessFile.length = 3

    when: "trying to get the complete entry set in an array"
    randomAccessFile.reverseElements()

    then: "an IllegalFileSizeException with a helpful error message is thrown"
    thrown IllegalFileSizeException

    cleanup:
    randomAccessFile.close()
  }
}
