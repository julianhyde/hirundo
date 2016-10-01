/*
// Licensed to Julian Hyde under one or more contributor license
// agreements. See the NOTICE file distributed with this work for
// additional information regarding copyright ownership. Julian Hyde
// licenses this file to you under the Apache License, Version 2.0
// (the "License"); you may not use this file except in compliance with
// the License. You may obtain a copy of the License at:
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
*/
package net.hydromatic.hirundo.jdbc;

import net.hydromatic.hirundo.prepare.Result;

import com.google.common.base.Throwables;

import org.apache.calcite.avatica.BuiltInConnectionProperty;
import org.apache.calcite.avatica.ConnectionProperty;
import org.apache.calcite.avatica.DriverVersion;
import org.apache.calcite.config.CalciteConnectionProperty;
import org.apache.calcite.jdbc.CalciteFactory;
import org.apache.calcite.jdbc.CalciteSchema;
import org.apache.calcite.jdbc.Driver;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/** Olap4j (JDBC) driver for Hirundo. */
public class HirundoDriver extends Driver {
  private static final String CONNECT_STRING_PREFIX = "jdbc:hirundo:";

  static {
    new HirundoDriver().register();
  }

  public final Executor executor;

  /** Creates a {@code HirundoDriver}. */
  public HirundoDriver() {
    this.executor = createExecutor();
  }

  private static Executor createExecutor() {
    final String className = getExecutorClassName();
    try {
      final Class<?> clazz = Class.forName(className);
      return (Executor) clazz.newInstance();
    } catch (ClassNotFoundException | IllegalAccessException
        | InstantiationException e) {
      throw Throwables.propagate(e);
    }
  }

  @Override protected String getFactoryClassName(JdbcVersion jdbcVersion) {
    return "net.hydromatic.hirundo.jdbc.HirundoJdbc41Factory";
  }

  private static String getExecutorClassName() {
    return "net.hydromatic.hirundo.prepare.ExecutorImpl";
  }

  static boolean acceptsUrl(String url) {
    return url.startsWith(CONNECT_STRING_PREFIX);
  }

  public Connection connect(String url, Properties info) throws SQLException {
    if (!acceptsUrl(url)) {
      return null;
    }
    final CalciteSchema rootSchema =
        CalciteSchema.createRootSchema(true, false);
    return ((CalciteFactory) factory).newConnection(this, factory, url, info,
        rootSchema, null);
  }

  public boolean acceptsURL(String url) throws SQLException {
    return HirundoConnection.acceptsUrl(url);
  }

  @Override protected Collection<ConnectionProperty> getConnectionProperties() {
    final List<ConnectionProperty> list = new ArrayList<>();
    Collections.addAll(list, BuiltInConnectionProperty.values());
    Collections.addAll(list, CalciteConnectionProperty.values());
    Collections.addAll(list, HirundoConnectionProperty.values());
    return list;
  }

  protected DriverVersion createDriverVersion() {
    return DriverVersion.load(Driver.class,
        "net-hydromatic-hirundo-jdbc.properties",
        "Hirundo olap4j Driver",
        "unknown version",
        "Hirundo",
        "unknown version");
  }

  /** Encapsulates the capabilities of the engine.
   *
   * TODO: move and rename */
  public interface Executor {
    Result execute(HirundoCellSet cellSet);
  }
}

// End HirundoDriver.java
