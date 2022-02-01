package de.scrum_master.testing

//import de.scrum_master.agent.aspect.ByteBuddyAspectAgent
//import de.scrum_master.agent.remove_final.RemoveFinalAgent
import groovy.transform.Memoized
import spock.mock.DetachedMockFactory

import static mockit.internal.startup.Startup.verifyInitialization

class JavaAgentDetector {
  /**
   * Detects an active JMockit agent
   *
   * @return true if JMockit agent has been initialised successfully
   * (needs to be on the Java command line via '-javaagent:');<br>
   * false otherwise
   */
  @Memoized
  static boolean isJMockitActive() {
    try {
      verifyInitialization()
      return true
    } catch (IllegalStateException exception) {
      return false
    }
  }

  /**
   * Detects if Alexander Kriegisch's Remove Final Agent (RFA) is active
   *
   * @return true if RFA has been initialised successfully (needs to be
   * on the Java command line via '-javaagent:' or attached dynamically);<br>
   * false otherwise
   */
  @Memoized
  static boolean isRemoveFinalAgentActive() {
    // TODO/FIXME: fix
    true
//    RemoveFinalAgent.active || ByteBuddyAspectAgent.removeFinalActive
  }

  /**
   * Detects if Alexander Kriegisch's ByteBuddy Agent (BBA) is active
   *
   * @return true if BBA has been initialised successfully (needs to be
   * on the Java command line via '-javaagent:' or attached dynamically);<br>
   * false otherwise
   */
  @Memoized
  static boolean isByteBuddyAspectAgentActive() {
    // TODO/FIXME: fix
    true
//    ByteBuddyAspectAgent.active
  }

  @Memoized
  static boolean canMock(Class clazz) {
    try {
      new DetachedMockFactory().Mock(clazz)
      true
    }
    catch (e) {
      false
    }
  }

}
