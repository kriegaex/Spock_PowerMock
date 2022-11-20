package de.scrum_master.stackoverflow.q71081881

import com.github.stefanbirkner.fakesftpserver.lambda.FakeSftpServer

class MyCloseableFakeSftpServer implements AutoCloseable {
  @Delegate
  private FakeSftpServer fakeSftpServer
  private Closeable closeServer

  MyCloseableFakeSftpServer() {
    fakeSftpServer = new FakeSftpServer(FakeSftpServer.createFileSystem())
    closeServer = fakeSftpServer.start(0)
  }

  @Override
  void close() throws Exception {
    fakeSftpServer.fileSystem.close()
    closeServer.close()
  }
}
