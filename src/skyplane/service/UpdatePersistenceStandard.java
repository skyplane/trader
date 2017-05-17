// Copyright 2008 Google Inc. All Rights Reserved.
package skyplane.service;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Max Ross <maxr@google.com>
 */
public class UpdatePersistenceStandard extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    PersistenceStandard ps = PersistenceStandard.valueOf(req.getParameter("persistenceStandard"));
    PersistenceStandard.set(ps);
    resp.sendRedirect("/");
  }
}
