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

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import javax.sql.rowset.RowSetMetaDataImpl;

/**
 * Implementation of {@link ResultSet} which returns 0 rows.
 *
 * <p>This class is used to implement {@link java.sql.DatabaseMetaData}
 * methods for querying object types where those object types never have
 * any instances for this particular driver.
 *
 * <p>This class has sub-classes which implement JDBC 3.0 and JDBC 4.0 APIs;
 * it is instantiated using {@link Factory#newEmptyResultSet}.
 */
class EmptyResultSet extends AbstractResultSet {
  private final ImmutableList<String> headerList;
  private final ImmutableList<List<Object>> rowList;
  private int rowOrdinal = -1;
  private final RowSetMetaDataImpl metaData = new RowSetMetaDataImpl();

  EmptyResultSet(HirundoStatement statement,
      List<String> headerList,
      List<List<Object>> rowList) {
    super(statement);
    this.headerList = ImmutableList.copyOf(headerList);
    this.rowList = ImmutableList.copyOf(rowList);
    try {
      metaData.setColumnCount(headerList.size());
      for (int i = 0; i < headerList.size(); i++) {
        metaData.setColumnName(i + 1, headerList.get(i));
        deduceType(rowList, i);
      }
    } catch (SQLException e) {
      throw Throwables.propagate(e);
    }
  }

  protected void deduceType(List<List<Object>> rowList, int column)
      throws SQLException {
    int nullability = ResultSetMetaData.columnNoNulls;
    int type = Types.OTHER;
    int maxLen = 0;
    for (List<Object> objects : rowList) {
      final Object o = objects.get(column);
      if (o == null) {
        nullability = ResultSetMetaData.columnNullable;
      } else {
        if (type == Types.OTHER) {
          type = deduceColumnType(o);
        }
        if (o instanceof String) {
          maxLen = Math.max(maxLen, ((String) o).length());
        }
      }
    }
    metaData.setNullable(column + 1, nullability);
    metaData.setColumnType(column + 1, type);
    if (maxLen > 0) {
      metaData.setPrecision(column + 1, maxLen);
    }
  }

  private int deduceColumnType(Object o) {
    if (o instanceof String) {
      return Types.VARCHAR;
    } else if (o instanceof Integer) {
      return Types.INTEGER;
    } else if (o instanceof Long) {
      return Types.BIGINT;
    } else if (o instanceof Double) {
      return Types.DOUBLE;
    } else if (o instanceof Float) {
      return Types.FLOAT;
    } else if (o instanceof Boolean) {
      return Types.BOOLEAN;
    } else {
      return Types.VARCHAR;
    }
  }

  // helper methods

  /**
   * Returns the value of a given column
   * @param columnOrdinal 0-based ordinal
   * @return Value
   */
  private Object getColumn(int columnOrdinal) {
    return rowList.get(rowOrdinal).get(columnOrdinal);
  }

  private Object getColumn(String columnLabel) throws SQLException {
    int column = headerList.indexOf(columnLabel);
    if (column < 0) {
      throw new SQLException("Column not found: " + columnLabel);
    }
    return rowList.get(rowOrdinal).get(column);
  }

  // implement ResultSet

  public boolean next() throws SQLException {
    // note that if rowOrdinal == rowList.size - 1, we move but then return
    // false
    if (rowOrdinal < rowList.size()) {
      ++rowOrdinal;
    }
    return rowOrdinal < rowList.size();
  }

  public void close() throws SQLException {
  }

  public String getString(int columnIndex) throws SQLException {
    final Object result = getColumn(columnIndex - 1);
    return result == null ? null : String.valueOf(result);
  }

  public boolean getBoolean(int columnIndex) throws SQLException {
    Object o = getColumn(columnIndex - 1);
    if (o instanceof Boolean) {
      return (Boolean) o;
    } else if (o instanceof String) {
      return Boolean.valueOf((String) o);
    } else {
      return !o.equals(0);
    }
  }

  public byte getByte(int columnIndex) throws SQLException {
    Object o = getColumn(columnIndex - 1);
    return ((Number) o).byteValue();
  }

  public short getShort(int columnIndex) throws SQLException {
    Object o = getColumn(columnIndex - 1);
    return ((Number) o).shortValue();
  }

  public int getInt(int columnIndex) throws SQLException {
    Object o = getColumn(columnIndex - 1);
    return ((Number) o).intValue();
  }

  public long getLong(int columnIndex) throws SQLException {
    Object o = getColumn(columnIndex - 1);
    return ((Number) o).longValue();
  }

  public float getFloat(int columnIndex) throws SQLException {
    Object o = getColumn(columnIndex - 1);
    return ((Number) o).floatValue();
  }

  public double getDouble(int columnIndex) throws SQLException {
    Object o = getColumn(columnIndex - 1);
    return ((Number) o).doubleValue();
  }

  public BigDecimal getBigDecimal(int columnIndex, int scale)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public byte[] getBytes(int columnIndex) throws SQLException {
    Object o = getColumn(columnIndex - 1);
    return (byte[]) o;
  }

  public Date getDate(int columnIndex) throws SQLException {
    Object o = getColumn(columnIndex - 1);
    return (Date) o;
  }

  public Time getTime(int columnIndex) throws SQLException {
    Object o = getColumn(columnIndex - 1);
    return (Time) o;
  }

  public Timestamp getTimestamp(int columnIndex) throws SQLException {
    Object o = getColumn(columnIndex - 1);
    return (Timestamp) o;
  }

  public String getString(String columnLabel) throws SQLException {
    final Object result = getColumn(columnLabel);
    return result == null ? null : String.valueOf(result);
  }

  public boolean getBoolean(String columnLabel) throws SQLException {
    Object o = getColumn(columnLabel);
    if (o instanceof Boolean) {
      return (Boolean) o;
    } else if (o instanceof String) {
      return Boolean.valueOf((String) o);
    } else {
      return !o.equals(0);
    }
  }

  public byte getByte(String columnLabel) throws SQLException {
    Object o = getColumn(columnLabel);
    return ((Number) o).byteValue();
  }

  public short getShort(String columnLabel) throws SQLException {
    Object o = getColumn(columnLabel);
    return ((Number) o).shortValue();
  }

  public int getInt(String columnLabel) throws SQLException {
    Object o = getColumn(columnLabel);
    return ((Number) o).intValue();
  }

  public long getLong(String columnLabel) throws SQLException {
    Object o = getColumn(columnLabel);
    return ((Number) o).longValue();
  }

  public float getFloat(String columnLabel) throws SQLException {
    Object o = getColumn(columnLabel);
    return ((Number) o).floatValue();
  }

  public double getDouble(String columnLabel) throws SQLException {
    Object o = getColumn(columnLabel);
    return ((Number) o).doubleValue();
  }

  public BigDecimal getBigDecimal(String columnLabel, int scale)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  public byte[] getBytes(String columnLabel) throws SQLException {
    Object o = getColumn(columnLabel);
    return (byte[]) o;
  }

  public Date getDate(String columnLabel) throws SQLException {
    Object o = getColumn(columnLabel);
    return (Date) o;
  }

  public Time getTime(String columnLabel) throws SQLException {
    Object o = getColumn(columnLabel);
    return (Time) o;
  }

  public Timestamp getTimestamp(String columnLabel) throws SQLException {
    Object o = getColumn(columnLabel);
    return (Timestamp) o;
  }

  public ResultSetMetaData getMetaData() throws SQLException {
    return metaData;
  }

  public boolean isBeforeFirst() throws SQLException {
    return rowOrdinal < 0;
  }

  public boolean isAfterLast() throws SQLException {
    return rowOrdinal >= rowList.size();
  }

  public boolean isFirst() throws SQLException {
    return rowOrdinal == 0;
  }

  public boolean isLast() throws SQLException {
    return rowOrdinal == rowList.size() - 1;
  }

  public void beforeFirst() throws SQLException {
    rowOrdinal = -1;
  }

  public void afterLast() throws SQLException {
    rowOrdinal = rowList.size();
  }

  public boolean first() throws SQLException {
    if (rowList.size() == 0) {
      return false;
    } else {
      rowOrdinal = 0;
      return true;
    }
  }

  public boolean last() throws SQLException {
    if (rowList.size() == 0) {
      return false;
    } else {
      rowOrdinal = rowList.size() - 1;
      return true;
    }
  }

  public int getRow() throws SQLException {
    return rowOrdinal + 1; // 1-based
  }

  public boolean absolute(int row) throws SQLException {
    int newRowOrdinal = row - 1; // convert to 0-based
    if (newRowOrdinal >= 0 && newRowOrdinal < rowList.size()) {
      rowOrdinal = newRowOrdinal;
      return true;
    } else {
      return false;
    }
  }

  public boolean relative(int rows) throws SQLException {
    int newRowOrdinal = rowOrdinal + (rows - 1);
    if (newRowOrdinal >= 0 && newRowOrdinal < rowList.size()) {
      rowOrdinal = newRowOrdinal;
      return true;
    } else {
      return false;
    }
  }

  public boolean previous() throws SQLException {
    // converse of next(); note that if rowOrdinal == 0, we decrement
    // but return false
    if (rowOrdinal >= 0) {
      --rowOrdinal;
    }
    return rowOrdinal >= 0;
  }

}

// End EmptyResultSet.java
