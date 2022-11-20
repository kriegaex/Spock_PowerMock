package de.scrum_master.gitter.d20220210

import spock.lang.Specification

class ApplicationServiceSpec extends Specification {
  def 'getByReceiverCompany ReceiverCompanyDTO -> application is not null'() {
    given:
    def application = new GenericApp()
    def receiverCompanyDTO = new ReceiverCompanyDTO(application: null)
    ApplicationService applicationService = Spy(ApplicationServiceImpl, constructorArgs: [Mock(ApplicationsDAO)]) {
      getDefaultApp() >> application
    }

    when:
    def result = applicationService.getByReceiverCompany(receiverCompanyDTO)

    then:
    println result
    result != null
  }
}
