package de.scrum_master.stackoverflow.q71461399

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.TempDir
import spock.lang.Unroll

import java.nio.file.Path

import static de.scrum_master.stackoverflow.q71461399.FixedElementSizeRandomAccessFile.getDefaultBufferSize
import static de.scrum_master.testing.TempFileUtility.tempFile
import static de.scrum_master.testing.TempFileUtility.tempName

class IntegerRandomAccessFileTest extends Specification {
  def static final ELEMENT_SIZE = Integer.BYTES
  def static final DEFAULT_BUFFER_SIZE = getDefaultBufferSize(ELEMENT_SIZE)

  @TempDir
  @Shared
  Path tempDir

  def fileName = tempName(tempDir)

  @Unroll("reverse #numElements elements, buffer size #bufSize")
  def "reverse element order in file completely"(int numElements, int bufSize) {
    given: "an empty random access file"
    def randomAccessFile = new IntegerRandomAccessFile(fileName, "rw", bufSize)
    randomAccessFile.length = 0

    when: "writing some integers"
    randomAccessFile.writeElements(elements)

    then: "file has expected size"
    randomAccessFile.length() == numElements * ELEMENT_SIZE

    when: "reading from beginning of file"
    randomAccessFile.seek 0

    then: "elements are stored in original order"
    randomAccessFile.readElements() == elements

    when: "reversing the order of elements"
    randomAccessFile.reverseElements()

    then: "file size is unchanged"
    randomAccessFile.length() == numElements * ELEMENT_SIZE

    when: "reading from beginning of file"
    randomAccessFile.seek 0

    then: "elements are stored in reversed order"
    randomAccessFile.readElements() == elements.reverse(true)

    cleanup:
    randomAccessFile.close()

    where: "we have multiple combinations of numbers of elements and buffer sizes"
    [numElements, bufSize] << [
      [0..3, 103].flatten(),
      [(2 * ELEMENT_SIZE..10 * ELEMENT_SIZE).by(2 * ELEMENT_SIZE), DEFAULT_BUFFER_SIZE].flatten()
    ].combinations()

    elements = numElements == 0 ? [] : (1..numElements).toList()
  }

  @Unroll("#type path, buffer size #bufferSize")
  def "all constructors for the same file name yield identical results"() {
    given: "an empty random access file"
    def randomAccessFile = new IntegerRandomAccessFile(*arguments)
    randomAccessFile.length = 0

    when: "writing elements and reversing their order"
    randomAccessFile.writeElements(1..5)
    randomAccessFile.reverseElements()
    randomAccessFile.seek 0

    then: "elements are stored in reversed order"
    randomAccessFile.readElements() == [5, 4, 3, 2, 1]

    cleanup:
    randomAccessFile.close()

    where: "we have multiple constructor calls which result in equivalent configurations"
    type     | file              | bufferSize
    "String" | tempName(tempDir) | "default"
    "String" | tempName(tempDir) | 1024
    "File"   | tempFile(tempDir) | "default"
    "File"   | tempFile(tempDir) | 1024

    arguments = [file, "rw", bufferSize] - "default"
  }
}
