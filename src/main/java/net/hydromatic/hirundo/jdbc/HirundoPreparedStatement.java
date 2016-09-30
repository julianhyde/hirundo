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
import org.olap4j.CellSetMetaData;
import org.olap4j.OlapException;
import org.olap4j.OlapParameterMetaData;
import org.olap4j.PreparedOlapStatement;
import org.olap4j.metadata.Cube;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

/** Implementation of {@link PreparedOlapStatement} for Hirundo. */
abstract class HirundoPreparedStatement extends HirundoStatement
    implements PreparedOlapStatement {
  final HirundoCellSetMetaData cellSetMetaData;

  /** Creates a HirundoPreparedStatement. */
  protected HirundoPreparedStatement(HirundoConnection connection,
      int queryTimeoutMillis, HirundoCellSetMetaData cellSetMetaData) {
    super(connection, queryTimeoutMillis);
    this.cellSetMetaData = cellSetMetaData;
  }

  public CellSet executeQuery() throws OlapException {
    throw new UnsupportedOperationException();
  }

  public OlapParameterMetaData getParameterMetaData() throws OlapException {
    throw new UnsupportedOperationException();
  }

  public CellSetMetaData getMetaData() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public Cube getCube() {
    throw new UnsupportedOperationException();
  }

  public boolean isSet(int parameterIndex) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void unset(int parameterIndex) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public int executeUpdate() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setNull(int parameterIndex, int sqlType) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setBoolean(int parameterIndex, boolean x) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setByte(int parameterIndex, byte x) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setShort(int parameterIndex, short x) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setInt(int parameterIndex, int x) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setLong(int parameterIndex, long x) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setFloat(int parameterIndex, float x) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setDouble(int parameterIndex, double x) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setBigDecimal(int parameterIndex, BigDecimal x)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setString(int parameterIndex, String x) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setBytes(int parameterIndex, byte[] x) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setDate(int parameterIndex, Date x) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setTime(int parameterIndex, Time x) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setTimestamp(int parameterIndex, Timestamp x)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setAsciiStream(int parameterIndex, InputStream x, int length)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setUnicodeStream(int parameterIndex, InputStream x, int length)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setBinaryStream(int parameterIndex, InputStream x, int length)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void clearParameters() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setObject(int parameterIndex, Object x, int targetSqlType)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setObject(int parameterIndex, Object x) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public boolean execute() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void addBatch() throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setCharacterStream(int parameterIndex, Reader reader, int length)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setRef(int parameterIndex, Ref x) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setBlob(int parameterIndex, Blob x) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setClob(int parameterIndex, Clob x) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setArray(int parameterIndex, Array x) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setDate(int parameterIndex, Date x, Calendar cal)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setTime(int parameterIndex, Time x, Calendar cal)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setNull(int parameterIndex, int sqlType, String typeName)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setURL(int parameterIndex, URL x) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setRowId(int parameterIndex, RowId x) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setNString(int parameterIndex, String value) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setNCharacterStream(int parameterIndex, Reader value, long length)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setNClob(int parameterIndex, NClob value) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setClob(int parameterIndex, Reader reader, long length)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setBlob(int parameterIndex, InputStream inputStream, long length)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setNClob(int parameterIndex, Reader reader, long length)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setSQLXML(int parameterIndex, SQLXML xmlObject)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setObject(int parameterIndex, Object x, int targetSqlType,
      int scaleOrLength) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setAsciiStream(int parameterIndex, InputStream x, long length)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setBinaryStream(int parameterIndex, InputStream x, long length)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setCharacterStream(int parameterIndex, Reader reader, long length)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setAsciiStream(int parameterIndex, InputStream x)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setBinaryStream(int parameterIndex, InputStream x)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setCharacterStream(int parameterIndex, Reader reader)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setNCharacterStream(int parameterIndex, Reader value)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setClob(int parameterIndex, Reader reader) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setBlob(int parameterIndex, InputStream inputStream)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void setNClob(int parameterIndex, Reader reader) throws SQLException {
    throw new UnsupportedOperationException();
  }
}

// End HirundoPreparedStatement.java
