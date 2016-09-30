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

import com.google.common.collect.ImmutableList;

import org.olap4j.OlapException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/** Implementation of {@link Factory}. */
class FactoryImpl implements Factory {
  public Connection newConnection(HirundoDriver driver, String url,
      Properties info) throws SQLException {
    return new HirundoConnection(driver);
  }

  public EmptyResultSet newEmptyResultSet(HirundoConnection connection) {
    final HirundoStatement statement = newStatement(connection);
    return new EmptyResultSet(statement, ImmutableList.of("dummy"),
        ImmutableList.<List<Object>>of());
  }

  public ResultSet newFixedResultSet(HirundoConnection connection,
      List<String> headerList, List<List<Object>> rowList) {
    throw new UnsupportedOperationException();
  }

  public HirundoCellSet newCellSet(HirundoStatement statement) {
    throw new UnsupportedOperationException();
  }

  public HirundoStatement newStatement(HirundoConnection connection) {
    return new HirundoStatement(connection, 0);
  }

  public HirundoPreparedStatement newPreparedStatement(String mdx,
      HirundoConnection connection) throws OlapException {
    throw new UnsupportedOperationException();
  }

  public HirundoDatabaseMetaData newDatabaseMetaData(
      HirundoConnection connection) {
    throw new UnsupportedOperationException();
  }

  public SQLException createException(String message) {
    throw new UnsupportedOperationException();
  }
}

// End FactoryImpl.java
