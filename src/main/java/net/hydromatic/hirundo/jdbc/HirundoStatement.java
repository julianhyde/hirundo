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

import net.hydromatic.hirundo.prepare.ValidatedQuery;

import org.apache.calcite.avatica.Meta;
import org.apache.calcite.jdbc.CalciteOlapStatement;

import org.olap4j.CellSet;
import org.olap4j.CellSetListener;
import org.olap4j.OlapException;
import org.olap4j.OlapStatement;
import org.olap4j.mdx.SelectNode;

import java.sql.ResultSet;
import java.sql.SQLException;

/** Implementation of {@link OlapStatement} for Hirundo. */
class HirundoStatement extends CalciteOlapStatement implements OlapStatement {
  HirundoStatement(HirundoConnection connection, Meta.StatementHandle h,
      int resultSetType, int resultSetConcurrency, int resultSetHoldability) {
    super(connection, h, resultSetType, resultSetConcurrency,
        resultSetHoldability);
  }

  public ValidatedQuery getQuery() {
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

}

// End HirundoStatement.java
