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

import org.olap4j.CellSet;
import org.olap4j.CellSetListener;
import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.OlapStatement;
import org.olap4j.mdx.SelectNode;
import org.olap4j.query.Query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;

/** Implementation of {@link OlapStatement} for Hirundo. */
class HirundoStatement implements OlapStatement {
  private int queryTimeoutMillis;
  final HirundoConnection connection;

  /** Support for {@link #closeOnCompletion()} method. */
  boolean closeOnCompletion;

  protected HirundoStatement(HirundoConnection connection,
      int queryTimeoutMillis) {
    this.queryTimeoutMillis = queryTimeoutMillis;
    this.connection = connection;
  }

  public int getQueryTimeoutMillis() {
    return queryTimeoutMillis;
  }

  public Query getQuery() {
    return null;
  }

  /**
   * Called by each child result set (most likely a cell set) when it is
   * closed.
   *
   * @param resultSet Result set or cell set
   */
  void onResultSetClose(ResultSet resultSet) throws SQLException {
    if (closeOnCompletion) {
      close();
    }
  }

  public OlapConnection getConnection() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public CellSet executeOlapQuery(String mdx) throws OlapException {
    throw new UnsupportedOperationException();
  }

  public CellSet executeOlapQuery(SelectNode selectNode) throws OlapException {
    throw new UnsupportedOperationException();
  }

  public void addListener(CellSetListener.Granularity granularity,
      CellSetListener listener) throws OlapException {
    throw new UnsupportedOperationException();
  }

  public ResultSet executeQuery(String sql) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public int executeUpdate(String sql) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void close() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public int getMaxFieldSize() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setMaxFieldSize(int max) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public int getMaxRows() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setMaxRows(int max) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setEscapeProcessing(boolean enable) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public int getQueryTimeout() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setQueryTimeout(int seconds) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void cancel() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public SQLWarning getWarnings() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void clearWarnings() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setCursorName(String name) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public boolean execute(String sql) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public ResultSet getResultSet() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public int getUpdateCount() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public boolean getMoreResults() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setFetchDirection(int direction) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public int getFetchDirection() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setFetchSize(int rows) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public int getFetchSize() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public int getResultSetConcurrency() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public int getResultSetType() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void addBatch(String sql) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void clearBatch() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public int[] executeBatch() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public boolean getMoreResults(int current) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public ResultSet getGeneratedKeys() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public int executeUpdate(String sql, int autoGeneratedKeys)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public int executeUpdate(String sql, int[] columnIndexes)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public int executeUpdate(String sql, String[] columnNames)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public boolean execute(String sql, int autoGeneratedKeys)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public boolean execute(String sql, int[] columnIndexes) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public boolean execute(String sql, String[] columnNames) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public int getResultSetHoldability() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public boolean isClosed() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setPoolable(boolean poolable) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public boolean isPoolable() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void closeOnCompletion() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public boolean isCloseOnCompletion() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public <T> T unwrap(Class<T> iface) throws SQLException {
    if (iface.isInstance(this)) {
      return iface.cast(this);
    }
    throw connection.factory.createException("cannot cast");
  }

  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return iface.isInstance(this);
  }
}

// End HirundoStatement.java
