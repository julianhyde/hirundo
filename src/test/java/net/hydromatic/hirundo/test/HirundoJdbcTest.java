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

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

import org.olap4j.CellSet;
import org.olap4j.OlapConnection;
import org.olap4j.OlapDatabaseMetaData;
import org.olap4j.OlapStatement;
import org.olap4j.layout.TraditionalCellSetFormatter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/** Unit test for Hirundo JDBC driver. */
public class HirundoJdbcTest {
  @Test public void testConnect() throws SQLException {
    try (Connection c = DriverManager.getConnection("jdbc:hirundo:");
         OlapConnection olapConnection = c.unwrap(OlapConnection.class)) {
      final OlapDatabaseMetaData metaData = olapConnection.getMetaData();
      final ResultSet tables =
          metaData.getTables(c.getCatalog(), c.getSchema(), null, null);
      assertThat(tables.next(), is(false));
      assertThat(olapConnection.isClosed(), is(false));
      c.close();
      assertThat(olapConnection.isClosed(), is(true));
    }
  }

  @Test public void testParse() throws SQLException {
    try (Connection c = DriverManager.getConnection("jdbc:hirundo:");
         final OlapConnection olapConnection = c.unwrap(OlapConnection.class)) {
      final OlapStatement statement = olapConnection.createStatement();
      final CellSet cellSet = statement.executeOlapQuery("select from [Sales]");
      assertThat(cellSet, returns("xx"));
    }
  }

  private Matcher<CellSet> returns(final String s) {
    return new BaseMatcher<CellSet>() {
      public boolean matches(Object o) {
        if (o instanceof CellSet) {
          final StringWriter sw = new StringWriter();
          final PrintWriter pw = new PrintWriter(sw);
          new TraditionalCellSetFormatter().format((CellSet) o, pw);
          pw.close();
          return sw.toString().equals(s);
        }
        return false;
      }

      public void describeTo(Description description) {
        description.appendText(s);
      }
    };
  }
}

// End HirundoJdbcTest.java
