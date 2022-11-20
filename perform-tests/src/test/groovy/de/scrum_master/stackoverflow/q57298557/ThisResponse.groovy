package de.scrum_master.stackoverflow.q57298557

import org.spockframework.mock.IDefaultResponse
import org.spockframework.mock.IMockInvocation

class ThisResponse implements IDefaultResponse {
  public static final ThisResponse INSTANCE = new ThisResponse()

  private ThisResponse() {}

  @Override
  Object respond(IMockInvocation invocation) {
    invocation.mockObject.instance
  }
}
