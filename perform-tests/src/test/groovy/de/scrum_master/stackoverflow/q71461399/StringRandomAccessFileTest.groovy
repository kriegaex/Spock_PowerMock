package de.scrum_master.stackoverflow.q71461399

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.TempDir
import spock.lang.Unroll

import java.nio.file.Path

import static de.scrum_master.stackoverflow.q71461399.FixedElementSizeRandomAccessFile.IllegalElementSizeException
import static de.scrum_master.stackoverflow.q71461399.FixedElementSizeRandomAccessFile.getDefaultBufferSize
import static de.scrum_master.testing.TempFileUtility.tempFile
import static de.scrum_master.testing.TempFileUtility.tempName

class StringRandomAccessFileTest extends Specification {
  def static final ELEMENT_SIZE = 50
  def static final DEFAULT_BUFFER_SIZE = getDefaultBufferSize(ELEMENT_SIZE)

  @TempDir
  @Shared
  Path tempDir

  def fileName = tempName(tempDir)

  @Unroll("reverse #numElements elements, buffer size #bufSize")
  def "reverse element order in file completely"(int numElements, int bufSize) {
    given: "an empty random access file"
    def randomAccessFile = new StringRandomAccessFile(fileName, "rw", ELEMENT_SIZE, bufSize)
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

    elements = numElements == 0 ? [] : (1..numElements).collect { "--- $it ---" as String }
  }

  @Unroll("#type path, buffer size #bufferSize")
  def "all constructors for the same file name yield identical results"() {
    given: "an empty random access file"
    def randomAccessFile = new StringRandomAccessFile(*arguments)
    randomAccessFile.length = 0

    and: "an elements list"
    def elements = ["one", "two", "three", "four", "five"]

    when: "writing elements and reversing their order"

    randomAccessFile.writeElements(elements)
    randomAccessFile.reverseElements()
    randomAccessFile.seek 0

    then: "elements are stored in reversed order"
    randomAccessFile.readElements() == elements.reverse(true)

    cleanup:
    randomAccessFile.close()

    where: "we have multiple constructor calls which result in equivalent configurations"
    type     | file              | bufferSize
    "String" | tempName(tempDir) | "default"
    "String" | tempName(tempDir) | DEFAULT_BUFFER_SIZE
    "File"   | tempFile(tempDir) | "default"
    "File"   | tempFile(tempDir) | DEFAULT_BUFFER_SIZE

    arguments = [file, "rw", ELEMENT_SIZE, bufferSize] - "default"
  }

  def "string is too long to fit into element buffer"() {
    given:
    def longText = "too long to fit into element buffer"
    def tooSmallElementSize = 10

    when: "trying to write a long text into a small element buffer"
    def randomAccessFile1 = new StringRandomAccessFile(tempName(tempDir), "rw", tooSmallElementSize)
    randomAccessFile1.writeElement(longText)

    then: "an IllegalElementSizeException is thrown"
    def e = thrown IllegalElementSizeException
    def bigEnoughElementSize = e.actualSize
    e.message =~ /string is too long for element size $tooSmallElementSize, it would need size $bigEnoughElementSize/

    when: "writing the same text into a big enough byte buffer"
    def randomAccessFile2 = new StringRandomAccessFile(tempName(tempDir), "rw", bigEnoughElementSize)
    randomAccessFile2.writeElement(longText)

    then: "it works as expected"
    noExceptionThrown()

    cleanup:
    randomAccessFile1.close()
    randomAccessFile2.close()
  }

  def "minimum element size is 5"() {
    when:
    new StringRandomAccessFile(tempName(tempDir), "rw", 4)

    then:
    def e = thrown IllegalElementSizeException
    e.message =~ /element size must be 4 bytes .* \+ length in bytes of UTF-8 string.*/
  }
}
