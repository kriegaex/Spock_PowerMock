package de.scrum_master.stackoverflow.q68060743

import spock.lang.Specification

class UnpredictableStubInitialisationTest extends Specification {
  def "my failing test"() {
    given:
    // Do not name this variable like the inline-stubbed method 'upload'
    var uploadMock = Mock(Upload){
      waitForCompletion() >> { throw new InterruptedException() }
    }

    var transferManager = Mock(TransferManager) {
      upload(_,_,_) >> uploadMock
    }

    when:
    var up = transferManager.upload(null, null, null)
    up.waitForCompletion()

    then:
    thrown(InterruptedException)
  }

  def "my working test"() {
    given:
    var upload = Mock(Upload) {
      waitForCompletion() >> { throw new InterruptedException() }
    }
    var transferManager = Mock(TransferManager)
    transferManager.upload(_,_,_) >> upload

    when:
    var up = transferManager.upload(null, null, null)
    up.waitForCompletion()

    then:
    thrown(InterruptedException)
  }

  static class Upload {
    String id
    void waitForCompletion() {
      println "Waiting for completion"
    }
  }

  static class TransferManager {
    Upload upload(String a, String b, String c) {
      println "Uploading"
      new Upload(id: a)
    }
  }
}
