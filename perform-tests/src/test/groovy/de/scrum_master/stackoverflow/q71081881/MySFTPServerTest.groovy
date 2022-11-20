package de.scrum_master.stackoverflow.q71081881

import com.github.stefanbirkner.fakesftpserver.lambda.CloseableFakeSftpServer
import spock.lang.AutoCleanup
import spock.lang.Specification
import spock.lang.Unroll

import static java.nio.charset.StandardCharsets.UTF_8

class MySFTPServerTest extends Specification {
  @AutoCleanup
  def server = new CloseableFakeSftpServer()

  @Unroll("user #user downloads file #file")
  def "users can download text files"() {
    given: "a preconfigured fake SFTP server"
    server.addUser(user, password)
    server.putFile(file, content, UTF_8)

    and: "an SFTP client under test"
    def client = new SFTPFileDownloader("localhost", server.port, user, password)

    expect:
    client.getFileContent(file) == content

    where:
    user    | password | file                   | content
    "me"    | "xoxox"  | "/a/b/c/one.txt"       | "First line\nSecondLine\n"
    "you"   | "mypass" | "/etc/two.xml"         | "<root><foo>abc</foo></root>"
    "admin" | "secret" | "/home/admin/three.sh" | "#!/usr/bin/bash\n\nls -al\n"
  }
}
