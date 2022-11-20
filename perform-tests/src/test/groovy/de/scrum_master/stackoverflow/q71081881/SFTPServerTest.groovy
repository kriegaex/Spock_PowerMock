package de.scrum_master.stackoverflow.q71081881

import com.github.stefanbirkner.fakesftpserver.lambda.CloseableFakeSftpServer
import spock.lang.AutoCleanup
import spock.lang.Specification

import static com.github.stefanbirkner.fakesftpserver.lambda.FakeSftpServer.withSftpServer
import static java.nio.charset.StandardCharsets.UTF_8

//@Grab(group='com.github.stefanbirkner', module='fake-sftp-server-lambda', version='2.0.0')
class SFTPServerTest extends Specification {
  @AutoCleanup
  def server = new CloseableFakeSftpServer()
  def user = "someone"
  def password = "secret"

  def "use original server class"() {
    given:
    def fileContent = null

    when:
    withSftpServer { server ->
      server.addUser(user, password)
      server.putFile("/directory/file.txt", "content of file", UTF_8)
      // Interact with the subject under test
      def client = new SFTPFileDownloader("localhost", server.port, user, password)
      fileContent = client.getFileContent("/directory/file.txt")
    }

    then:
    fileContent == "content of file"
  }

  def "use custom server class"() {
    given: "a preconfigured fake SFTP server"
    server.addUser(user, password)
    server.putFile("/directory/file.txt", "content of file", UTF_8)

    and: "an SFTP client under test"
    def client = new SFTPFileDownloader("localhost", server.port, user, password)

    expect:
    client.getFileContent("/directory/file.txt") == "content of file"
  }

}
