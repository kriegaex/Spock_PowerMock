package de.scrum_master.stackoverflow.q71081881;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.xfer.InMemoryDestFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

//@Grab(group='com.hierynomus', module='sshj', version='0.32.0')
public class SFTPFileDownloader {
  private String host;
  private int port;
  private String user;
  private String password;

  public SFTPFileDownloader(String host, int port, String user, String password) {
    this.host = host;
    this.port = port;
    this.user = user;
    this.password = password;
  }

  protected static class ByteArrayInMemoryDestFile extends InMemoryDestFile {
    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @Override
    public ByteArrayOutputStream getOutputStream() {
      return outputStream;
    }
  }

  public String getFileContent(String path) throws IOException {
    try (
      SSHClient sshClient = setupSshj();
      SFTPClient sftpClient = sshClient.newSFTPClient()
    )
    {
      ByteArrayInMemoryDestFile inMemoryDestFile = new ByteArrayInMemoryDestFile();
      sftpClient.get(path, inMemoryDestFile);
      return inMemoryDestFile.getOutputStream().toString("UTF-8");
    }
  }

  private SSHClient setupSshj() throws IOException {
    SSHClient client = new SSHClient();
    client.addHostKeyVerifier(new PromiscuousVerifier());
    client.connect(host, port);
    client.authPassword(user, password);
    return client;
  }
}
