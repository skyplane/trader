// Copyright 2008 Google Inc. All Rights Reserved.
package skyplane.service;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author Max Ross <maxr@google.com>
 */
public final class EMF {

  private static final EntityManagerFactory INSTANCE = Persistence.createEntityManagerFactory("helloorm");

  public static EntityManagerFactory get() {
    return INSTANCE;
  }

  private EMF() {}
}
