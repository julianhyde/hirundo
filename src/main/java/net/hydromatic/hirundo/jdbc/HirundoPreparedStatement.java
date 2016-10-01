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

import org.apache.calcite.avatica.Meta;
import org.apache.calcite.jdbc.CalcitePreparedOlapStatement;

import org.olap4j.PreparedOlapStatement;
import org.olap4j.metadata.Cube;

import java.sql.SQLException;

/** Implementation of {@link PreparedOlapStatement} for Hirundo. */
abstract class HirundoPreparedStatement extends CalcitePreparedOlapStatement {
  /** Creates a HirundoPreparedStatement. */
  protected HirundoPreparedStatement(HirundoConnection connection,
      Meta.StatementHandle h, Meta.Signature signature, int resultSetType,
      int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    super(connection, h, signature, resultSetType,
        resultSetConcurrency, resultSetHoldability);
  }

  public Cube getCube() {
    return getMetaData().getCube();
  }

  public boolean isSet(int parameterIndex) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public void unset(int parameterIndex) throws SQLException {
    throw new UnsupportedOperationException();
  }
}

// End HirundoPreparedStatement.java
