/*
 * Copyright (c) 2011-2014 The original author or authors
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *     The Eclipse Public License is available at
 *     http://www.eclipse.org/legal/epl-v10.html
 *
 *     The Apache License v2.0 is available at
 *     http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */

package io.vertx.ext.jdbc.impl.actions;

import io.vertx.core.Context;
import io.vertx.core.Vertx;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class JDBCExecute extends AbstractJDBCAction<Void> {

  private final String sql;

  public JDBCExecute(Vertx vertx, Connection connection, Context context, String sql) {
    super(vertx, connection, context);
    this.sql = sql;
  }

  @Override
  protected Void execute(Connection conn) throws SQLException {
    try (Statement stmt = conn.createStatement()) {
      boolean isResultSet = stmt.execute(sql);
      // If the execute statement happens to return a result set, we should close it in case
      // the connection pool doesn't.
      if (isResultSet) {
        while (stmt.getMoreResults()) {
          try (ResultSet rs = stmt.getResultSet()) {
            // TODO: is this correct? just ignore?
          };
        }
      }
      return null;
    }
  }

  @Override
  protected String name() {
    return "execute";
  }
}
