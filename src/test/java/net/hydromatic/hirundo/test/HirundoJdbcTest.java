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
package net.hydromatic.hirundo.test;

import org.junit.Test;

import org.olap4j.OlapConnection;
import org.olap4j.OlapDatabaseMetaData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/** Unit test for Hirundo JDBC driver. */
public class HirundoJdbcTest {
  @Test public void testConnect() throws SQLException {
    Connection c = DriverManager.getConnection("jdbc:hirundo:");
    final OlapConnection olapConnection = c.unwrap(OlapConnection.class);
    final OlapDatabaseMetaData metaData = olapConnection.getMetaData();
    final ResultSet tables = metaData.getTables(null, null, null, null);
    assertThat(tables.next(), is(false));
    assertThat(olapConnection.isClosed(), is(false));
    c.close();
    assertThat(olapConnection.isClosed(), is(true));
  }
}

// End HirundoJdbcTest.java
