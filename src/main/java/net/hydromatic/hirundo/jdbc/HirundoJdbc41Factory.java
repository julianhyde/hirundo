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

import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.avatica.AvaticaConnection;
import org.apache.calcite.avatica.AvaticaFactory;
import org.apache.calcite.avatica.AvaticaPreparedStatement;
import org.apache.calcite.avatica.AvaticaStatement;
import org.apache.calcite.avatica.Meta;
import org.apache.calcite.avatica.QueryState;
import org.apache.calcite.avatica.UnregisteredDriver;
import org.apache.calcite.jdbc.CalciteFactory;
import org.apache.calcite.jdbc.CalcitePrepare;
import org.apache.calcite.jdbc.CalciteResultSet;
import org.apache.calcite.jdbc.CalciteSchema;
import org.apache.calcite.jdbc.Driver;

import org.olap4j.CellSet;
import org.olap4j.CellSetListener;
import org.olap4j.OlapException;
import org.olap4j.mdx.SelectNode;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;
import java.util.TimeZone;

/**
 * Implementation of {@link org.apache.calcite.avatica.AvaticaFactory}
 * for Calcite and JDBC 4.1 (corresponds to JDK 1.7).
 */
@SuppressWarnings("UnusedDeclaration")
public class HirundoJdbc41Factory extends CalciteFactory {
  /** Creates a factory for JDBC version 4.1. */
  public HirundoJdbc41Factory() {
    this(4, 1);
  }

  /** Creates a JDBC factory with given major/minor version number. */
  protected HirundoJdbc41Factory(int major, int minor) {
    super(major, minor);
  }

  public HirundoJdbc41Connection newConnection(UnregisteredDriver driver,
      AvaticaFactory factory, String url, Properties info,
      CalciteSchema rootSchema, JavaTypeFactory typeFactory) {
    return new HirundoJdbc41Connection((Driver) driver, factory, url, info,
        rootSchema, typeFactory);
  }

  public HirundoJdbc41DatabaseMetaData newDatabaseMetaData(
      AvaticaConnection connection) {
    return new HirundoJdbc41DatabaseMetaData((HirundoConnection) connection);
  }

  public HirundoJdbc41Statement newStatement(AvaticaConnection connection,
      Meta.StatementHandle h, int resultSetType, int resultSetConcurrency,
      int resultSetHoldability) {
    return new HirundoJdbc41Statement((HirundoConnection) connection, h,
        resultSetType, resultSetConcurrency, resultSetHoldability);
  }

  public AvaticaPreparedStatement newPreparedStatement(
      AvaticaConnection connection, Meta.StatementHandle h,
      Meta.Signature signature, int resultSetType, int resultSetConcurrency,
      int resultSetHoldability) throws SQLException {
    return new HirundoJdbc41PreparedStatement(
        (HirundoConnection) connection, h,
        (CalcitePrepare.CalciteSignature) signature, resultSetType,
        resultSetConcurrency, resultSetHoldability);
  }

  public CalciteResultSet newResultSet(AvaticaStatement statement,
      QueryState state, Meta.Signature signature, TimeZone timeZone,
      Meta.Frame firstFrame) {
    final ResultSetMetaData metaData =
        newResultSetMetaData(statement, signature);
    final CalcitePrepare.CalciteSignature calciteSignature =
        (CalcitePrepare.CalciteSignature) signature;
    if (true) {
      return new HirundoResultSet(statement, calciteSignature, metaData,
          timeZone, firstFrame);
    } else {
      return new HirundoCellSet(statement, calciteSignature, metaData, timeZone,
          firstFrame);
    }
  }

  public HirundoCellSetMetaData newResultSetMetaData(AvaticaStatement statement,
      Meta.Signature signature) {
    return new HirundoCellSetMetaData(statement, null, signature);
  }

  /** Implementation of connection for JDBC 4.1. */
  private static class HirundoJdbc41Connection extends HirundoConnection {
    HirundoJdbc41Connection(Driver driver, AvaticaFactory factory, String url,
        Properties info, CalciteSchema rootSchema,
        JavaTypeFactory typeFactory) {
      super((HirundoDriver) driver, factory, url, info, rootSchema,
          typeFactory);
    }
  }

  /** Implementation of statement for JDBC 4.1. */
  private static class HirundoJdbc41Statement extends HirundoStatement {
    HirundoJdbc41Statement(HirundoConnection connection,
        Meta.StatementHandle h, int resultSetType, int resultSetConcurrency,
        int resultSetHoldability) {
      super(connection, h, resultSetType, resultSetConcurrency,
          resultSetHoldability);
    }
  }

  /** Implementation of prepared statement for JDBC 4.1. */
  static class HirundoJdbc41PreparedStatement
      extends HirundoPreparedStatement {
    HirundoJdbc41PreparedStatement(HirundoConnection connection,
        Meta.StatementHandle h, CalcitePrepare.CalciteSignature signature,
        int resultSetType, int resultSetConcurrency, int resultSetHoldability)
        throws SQLException {
      super(connection, h, signature, resultSetType, resultSetConcurrency,
          resultSetHoldability);
    }

    public CellSet executeOlapQuery(String mdx) throws OlapException {
      throw new UnsupportedOperationException();
    }

    public CellSet executeOlapQuery(SelectNode selectNode)
        throws OlapException {
      throw new UnsupportedOperationException();
    }

    public void addListener(CellSetListener.Granularity granularity,
        CellSetListener listener) throws OlapException {
      throw new UnsupportedOperationException();
    }

    public long executeLargeUpdate(String sql, int autoGeneratedKeys)
        throws SQLException {
      throw new UnsupportedOperationException();
    }

    public long executeLargeUpdate(String sql, int[] columnIndexes)
        throws SQLException {
      throw new UnsupportedOperationException();
    }

    public long executeLargeUpdate(String sql, String[] columnNames)
        throws SQLException {
      throw new UnsupportedOperationException();
    }
  }

  /** Implementation of database metadata for JDBC 4.1. */
  private static class HirundoJdbc41DatabaseMetaData
      extends HirundoDatabaseMetaData {
    HirundoJdbc41DatabaseMetaData(HirundoConnection connection) {
      super(connection);
    }
  }

  /** A result set for relational results (as opposed to
   * {@link HirundoCellSet} for multi-dimensional results). */
  private static class HirundoResultSet extends CalciteResultSet {
    protected HirundoResultSet(AvaticaStatement statement,
        CalcitePrepare.CalciteSignature calciteSignature,
        ResultSetMetaData resultSetMetaData, TimeZone timeZone,
        Meta.Frame firstFrame) {
      super(statement, calciteSignature, resultSetMetaData, timeZone,
          firstFrame);
    }
  }
}

// End HirundoJdbc41Factory.java
