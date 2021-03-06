// Copyright 2008 Google Inc. All Rights Reserved.
package main.java.skyplane.service;

import main.java.skyplane.PMF;

import javax.jdo.PersistenceManager;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Max Ross <maxr@google.com>
 */
public class AddFlight extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String orig = req.getParameter("orig");
    String dest = req.getParameter("dest");
    Flight f = new Flight(orig, dest);
    if (PersistenceStandard.get() == PersistenceStandard.JPA) {
      doPostJPA(f);
    } else {
      doPostJDO(f);
    }
    resp.sendRedirect("/");


/*    MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
            .iterations(1)
            .weightInit(WeightInit.XAVIER)
            .activation("relu")
            .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
            .learningRate(0.05)

            .backprop(true)
            .build();*/

    // ... other hyperparameters



  }

  private void doPostJDO(Flight f) {
    PersistenceManager pm = PMF.get().getPersistenceManager();
    try {
      pm.makePersistent(f);
    } finally {
      pm.close();
    }
  }

  private void doPostJPA(Flight f) {
    EntityManager em = EMF.get().createEntityManager();
    try {
      em.persist(f);
    } finally {
      em.close();
    }
  }
}
