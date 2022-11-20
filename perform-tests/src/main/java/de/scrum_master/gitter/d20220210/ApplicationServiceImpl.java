package de.scrum_master.gitter.d20220210;

import java.util.Optional;

public class ApplicationServiceImpl implements ApplicationService {

  protected Application getDefaultApp() {
//    return new Application();
    throw new RuntimeException();
  }

  private final ApplicationsDAO applicationDao;

  public ApplicationServiceImpl(final ApplicationsDAO applicationDao) {
    this.applicationDao = applicationDao;
  }

  @Override
  public Application getByReceiverCompany(final ReceiverCompanyDTO receiverCompany) {
    final Application app = Optional.ofNullable(receiverCompany)
      .map(ReceiverCompanyDTO::getApplication)
//      .orElseGet(this::getDefaultApp);
      .orElse(getDefaultApp());
    return app;
  }
}
