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

import org.olap4j.OlapException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * Instantiates classes to implement the olap4j API against Hirundo.
 */
interface Factory {
  /**
   * Creates a connection.
   *
   * @param driver Driver
   * @param url URL of server
   * @param info Properties defining the connection
   * @return Connection
   * @throws SQLException on error
   */
  Connection newConnection(HirundoDriver driver, String url,
      Properties info) throws SQLException;

  /**
   * Creates an empty result set.
   *
   * @param connection Connection
   * @return Result set
   */
  EmptyResultSet newEmptyResultSet(HirundoConnection connection);

  /**
   * Creates a result set with a fixed set of rows.
   *
   * @param connection Connection
   * @param headerList Column headers
   * @param rowList Row values
   * @return Result set
   */
  ResultSet newFixedResultSet(HirundoConnection connection,
      List<String> headerList, List<List<Object>> rowList);

  /**
   * Creates a cell set.
   *
   * @param statement Statement
   * @return Cell set
   */
  HirundoCellSet newCellSet(HirundoStatement statement);

  /**
   * Creates a statement.
   *
   * @param connection Connection
   * @return Statement
   */
  HirundoStatement newStatement(HirundoConnection connection);

  /**
   * Creates a prepared statement.
   *
   * @param mdx MDX query text
   * @param connection Connection
   * @return Prepared statement
   * @throws org.olap4j.OlapException on database error
   */
  HirundoPreparedStatement newPreparedStatement(String mdx,
      HirundoConnection connection) throws OlapException;

  /**
   * Creates a metadata object.
   *
   * @param connection Connection
   * @return Metadata object
   */
  HirundoDatabaseMetaData newDatabaseMetaData(HirundoConnection connection);

  SQLException createException(String message);
}

// End Factory.java
