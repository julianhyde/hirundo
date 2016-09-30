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

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

/** Olap4j (JDBC) driver for Hirundo. */
public class HirundoDriver implements Driver {
  static {
    try {
      register();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  protected final Factory factory;
  public final Executor executor;

  /** Creates a {@code HirundoDriver}. */
  public HirundoDriver() {
    this.factory = createFactory();
    this.executor = createExecutor();
  }

  private static Factory createFactory() {
    final String className = getFactoryClassName();
    try {
      final Class<?> clazz = Class.forName(className);
      return (Factory) clazz.newInstance();
    } catch (ClassNotFoundException | IllegalAccessException
        | InstantiationException e) {
      throw Throwables.propagate(e);
    }
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

  private static String getFactoryClassName() {
    return "net.hydromatic.hirundo.jdbc.FactoryImpl";
  }

  private static String getExecutorClassName() {
    return "net.hydromatic.hirundo.prepare.ExecutorImpl";
  }

  /**
   * Registers an instance of HirundoOlap4jDriver.
   *
   * <p>Called implicitly on class load, and implements the traditional
   * 'Class.forName' way of registering JDBC drivers.
   *
   * @throws SQLException on error
   */
  private static void register() throws SQLException {
    DriverManager.registerDriver(new HirundoDriver());
  }

  public Connection connect(String url, Properties info) throws SQLException {
    if (!HirundoConnection.acceptsUrl(url)) {
      return null;
    }
    return factory.newConnection(this, url, info);
  }

  public boolean acceptsURL(String url) throws SQLException {
    return HirundoConnection.acceptsUrl(url);
  }

  public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
      throws SQLException {
    List<DriverPropertyInfo> list = new ArrayList<>();

    // First, add the contents of info
    for (Map.Entry<Object, Object> entry : info.entrySet()) {
      list.add(
          new DriverPropertyInfo(
              (String) entry.getKey(),
              (String) entry.getValue()));
    }
    // Next, add property defns not mentioned in info
    for (ConnectionProperty p : ConnectionProperty.values()) {
      if (info.containsKey(p.name())) {
        continue;
      }
      list.add(new DriverPropertyInfo(p.name(), null));
    }
    return list.toArray(new DriverPropertyInfo[list.size()]);
  }

  public Logger getParentLogger() {
    return Logger.getLogger("");
  }

  /**
   * Returns the driver name. Not in the JDBC API.
   *
   * @return Driver name
   */
  String getName() {
    return HirundoDriverVersion.NAME;
  }

  /**
   * Returns the driver version. Not in the JDBC API.
   *
   * @return Driver version
   */
  String getVersion() {
    return HirundoDriverVersion.VERSION;
  }

  public int getMajorVersion() {
    return HirundoDriverVersion.MAJOR_VERSION;
  }

  public int getMinorVersion() {
    return HirundoDriverVersion.MINOR_VERSION;
  }

  public boolean jdbcCompliant() {
    return false;
  }

  /** Encapsulates the capabilities of the engine.
   *
   * TODO: move and rename */
  public interface Executor {
    Result execute(HirundoCellSet cellSet);
  }
}

// End HirundoDriver.java
