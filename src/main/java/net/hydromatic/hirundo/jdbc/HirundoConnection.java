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

import org.olap4j.OlapConnection;
import org.olap4j.OlapDatabaseMetaData;
import org.olap4j.OlapException;
import org.olap4j.OlapStatement;
import org.olap4j.PreparedOlapStatement;
import org.olap4j.Scenario;
import org.olap4j.mdx.parser.MdxParserFactory;
import org.olap4j.metadata.Catalog;
import org.olap4j.metadata.Database;
import org.olap4j.metadata.NamedList;
import org.olap4j.metadata.Schema;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/** Implementation of {@link OlapConnection} for Hirundo. */
class HirundoConnection implements OlapConnection {
  private static final String CONNECT_STRING_PREFIX = "jdbc:hirundo:";

  final Factory factory;
  final HirundoDriver.Executor executor;
  boolean closed;

  static boolean acceptsUrl(String url) {
    return url.startsWith(CONNECT_STRING_PREFIX);
  }

  /** Creates a HirundoConnection. */
  public HirundoConnection(HirundoDriver driver) {
    this.factory = driver.factory;
    this.executor = driver.executor;
    this.closed = false;
  }

  public OlapDatabaseMetaData getMetaData() throws OlapException {
    return new HirundoDatabaseMetaData(this);
  }

  public PreparedOlapStatement prepareOlapStatement(String mdx)
      throws OlapException {
    throw new UnsupportedOperationException();
  }

  public MdxParserFactory getParserFactory() {
    throw new UnsupportedOperationException();
  }

  public OlapStatement createStatement() throws OlapException {
    throw new UnsupportedOperationException();
  }

  public String getDatabase() throws OlapException {
    throw new UnsupportedOperationException();
  }

  public void setDatabase(String databaseName) throws OlapException {
    throw new UnsupportedOperationException();
  }

  public Database getOlapDatabase() throws OlapException {
    throw new UnsupportedOperationException();
  }

  public NamedList<Database> getOlapDatabases() throws OlapException {
    throw new UnsupportedOperationException();
  }

  public String getCatalog() throws OlapException {
    throw new UnsupportedOperationException();
  }

  public void setCatalog(String catalogName) throws OlapException {
    throw new UnsupportedOperationException();
  }

  public Catalog getOlapCatalog() throws OlapException {
    throw new UnsupportedOperationException();
  }

  public NamedList<Catalog> getOlapCatalogs() throws OlapException {
    throw new UnsupportedOperationException();
  }

  public String getSchema() throws OlapException {
    throw new UnsupportedOperationException();
  }

  public void setSchema(String schemaName) throws OlapException {
    throw new UnsupportedOperationException();
  }

  public Schema getOlapSchema() throws OlapException {
    throw new UnsupportedOperationException();
  }

  public NamedList<Schema> getOlapSchemas() throws OlapException {
    throw new UnsupportedOperationException();
  }

  public void setLocale(Locale locale) {
    throw new UnsupportedOperationException();
  }

  public Locale getLocale() {
    throw new UnsupportedOperationException();
  }

  public void setRoleName(String roleName) throws OlapException {
    throw new UnsupportedOperationException();
  }

  public String getRoleName() {
    throw new UnsupportedOperationException();
  }

  public List<String> getAvailableRoleNames() throws OlapException {
    throw new UnsupportedOperationException();
  }

  public Scenario createScenario() throws OlapException {
    throw new UnsupportedOperationException();
  }

  public void setScenario(Scenario scenario) throws OlapException {
    throw new UnsupportedOperationException();
  }

  public Scenario getScenario() throws OlapException {
    throw new UnsupportedOperationException();
  }

  public PreparedStatement prepareStatement(String sql) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public CallableStatement prepareCall(String sql) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public String nativeSQL(String sql) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setAutoCommit(boolean autoCommit) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public boolean getAutoCommit() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void commit() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void rollback() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void close() throws SQLException {
    closed = true;
  }

  public boolean isClosed() throws SQLException {
    return closed;
  }

  public void setReadOnly(boolean readOnly) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public boolean isReadOnly() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setTransactionIsolation(int level) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public int getTransactionIsolation() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public SQLWarning getWarnings() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void clearWarnings() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public PreparedStatement prepareStatement(String sql, int resultSetType,
        int resultSetConcurrency) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public CallableStatement prepareCall(String sql, int resultSetType,
      int resultSetConcurrency) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public Map<String, Class<?>> getTypeMap() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setHoldability(int holdability) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public int getHoldability() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public Savepoint setSavepoint() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public Savepoint setSavepoint(String name) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void rollback(Savepoint savepoint) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void releaseSavepoint(Savepoint savepoint) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public Statement createStatement(int resultSetType, int resultSetConcurrency)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public Statement createStatement(int resultSetType, int resultSetConcurrency,
      int resultSetHoldability) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public PreparedStatement prepareStatement(String sql, int resultSetType,
      int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    throw new UnsupportedOperationException();
  }


  public CallableStatement prepareCall(String sql, int resultSetType,
      int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public PreparedStatement prepareStatement(String sql, String[] columnNames)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public Clob createClob() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public Blob createBlob() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public NClob createNClob() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public SQLXML createSQLXML() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public boolean isValid(int timeout) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setClientInfo(String name, String value)
      throws SQLClientInfoException {
    throw new UnsupportedOperationException();
  }

  public void setClientInfo(Properties properties)
      throws SQLClientInfoException {
    throw new UnsupportedOperationException();
  }

  public String getClientInfo(String name) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public Properties getClientInfo() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public Array createArrayOf(String typeName, Object[] elements)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public Struct createStruct(String typeName, Object[] attributes)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void abort(Executor executor) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setNetworkTimeout(Executor executor, int milliseconds)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public int getNetworkTimeout() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public <T> T unwrap(Class<T> iface) throws SQLException {
    if (iface.isInstance(this)) {
      return iface.cast(this);
    }
    throw factory.createException("cannot cast");
  }

  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return iface.isInstance(this);
  }
}

// End HirundoConnection.java
