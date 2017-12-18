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

import net.hydromatic.hirundo.prepare.Preparing;
import net.hydromatic.hirundo.prepare.ValidatedQuery;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.avatica.AvaticaFactory;
import org.apache.calcite.avatica.Meta;
import org.apache.calcite.avatica.NoSuchStatementException;
import org.apache.calcite.avatica.QueryState;
import org.apache.calcite.jdbc.CalciteOlapConnection;
import org.apache.calcite.jdbc.CalciteSchema;

import org.olap4j.OlapConnection;
import org.olap4j.OlapDatabaseMetaData;
import org.olap4j.OlapException;
import org.olap4j.PreparedOlapStatement;
import org.olap4j.Scenario;
import org.olap4j.impl.NamedListImpl;
import org.olap4j.impl.Olap4jUtil;
import org.olap4j.mdx.SelectNode;
import org.olap4j.mdx.parser.MdxParser;
import org.olap4j.mdx.parser.MdxParserFactory;
import org.olap4j.mdx.parser.MdxValidator;
import org.olap4j.mdx.parser.impl.DefaultMdxParserImpl;
import org.olap4j.metadata.Catalog;
import org.olap4j.metadata.Database;
import org.olap4j.metadata.NamedList;
import org.olap4j.metadata.Schema;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/** Implementation of {@link OlapConnection} for Hirundo. */
abstract class HirundoConnection extends CalciteOlapConnection {
  private static final String CONNECT_STRING_PREFIX = "jdbc:hirundo:";

  final HirundoDriver.Executor executor;
  private final NamedList<HirundoDatabase> databases;
  private final Schema currentSchema;
  private final AtomicInteger handleGenerator = new AtomicInteger();
  final OlapHelper olapHelper = OlapHelper.INSTANCE;

  static boolean acceptsUrl(String url) {
    return url.startsWith(CONNECT_STRING_PREFIX);
  }

  /** Creates a HirundoConnection. */
  HirundoConnection(HirundoDriver driver, AvaticaFactory factory, String url,
      Properties info, CalciteSchema rootSchema, JavaTypeFactory typeFactory) {
    super(driver, factory, url, info, rootSchema, typeFactory);
    Preconditions.checkArgument(factory instanceof HirundoJdbc41Factory);
    this.executor = driver.executor;
    final NamedList<HirundoDatabase> databases = new NamedListImpl<>();
    final NamedList<HirundoCatalog> catalogs = new NamedListImpl<>();
    final HirundoDatabase database =
        new HirundoDatabase(this, "DATABASE", catalogs);
    databases.add(database);
    final NamedList<HirundoSchema> schemas = new NamedListImpl<>();
    final NamedList<HirundoDimension> sharedDimensions = new NamedListImpl<>();
    final HirundoCatalog catalog =
        new HirundoCatalog(database, "CATALOG", schemas, sharedDimensions,
            new HirundoDatabaseMetaData(this));
    catalogs.add(catalog);
    final NamedList<HirundoCube> cubes = new NamedListImpl<>();
    final ImmutableList<Locale> supportedLocales = ImmutableList.of();
    final HirundoSchema schema =
        new HirundoSchema(catalog, "SCHEMA", cubes, sharedDimensions,
            supportedLocales);
    this.databases = databases;
    this.currentSchema = schema;
  }

  public OlapDatabaseMetaData getMetaData() throws OlapException {
    return new HirundoDatabaseMetaData(this);
  }

  public PreparedOlapStatement prepareOlapStatement(String mdx)
      throws OlapException {
    final Preparing preparing =
        new Preparing(this, handleGenerator.getAndIncrement());
    final ValidatedQuery q = preparing.withMdx(mdx).validate();
    try {
      return getFactory()
          .newPreparedStatement(this,
              new Meta.StatementHandle(id, q.h, q.signature), q.signature,
              q.resultSetType, q.resultSetConcurrency, q.resultSetHoldability);
    } catch (SQLException e) {
      throw olapHelper.toOlap(e);
    }
  }

  PreparedOlapStatement prepareOlapStatement(SelectNode mdx)
      throws OlapException {
    final Preparing preparing =
        new Preparing(this, handleGenerator.getAndIncrement());
    final ValidatedQuery q = preparing.withNode(mdx).validate();
    try {
      return getFactory()
          .newPreparedStatement(this,
              new Meta.StatementHandle(id, q.h, q.signature), q.signature,
              q.resultSetType, q.resultSetConcurrency, q.resultSetHoldability);
    } catch (SQLException e) {
      throw olapHelper.toOlap(e);
    }
  }

  public MdxParserFactory getParserFactory() {
    return new MdxParserFactory() {
      public MdxParser createMdxParser(OlapConnection olapConnection) {
        return new DefaultMdxParserImpl();
      }

      public MdxValidator createMdxValidator(OlapConnection olapConnection) {
        throw new UnsupportedOperationException();
      }
    };
  }

  public void setSchema(String schemaName) throws OlapException {
    // no op.
  }

  public String getSchema() {
    return currentSchema.getName();
  }

  public Schema getOlapSchema() throws OlapException {
    return currentSchema;
  }

  public NamedList<Schema> getOlapSchemas() throws OlapException {
    return getOlapCatalog().getSchemas();
  }

  public void setCatalog(String catalogName) throws OlapException {
    // no op
  }

  public String getCatalog() {
    return currentSchema.getCatalog().getName();
  }

  public Catalog getOlapCatalog() throws OlapException {
    return currentSchema.getCatalog();
  }

  public NamedList<Catalog> getOlapCatalogs() throws OlapException {
    return getOlapDatabase().getCatalogs();
  }

  public void setDatabase(String databaseName) throws OlapException {
    // no op.
  }

  public String getDatabase() throws OlapException {
    return getOlapDatabase().getName();
  }

  public Database getOlapDatabase() throws OlapException {
    // It is assumed that Hirundo supports only a single database.
    return databases.get(0);
  }

  public NamedList<Database> getOlapDatabases() throws OlapException {
    return Olap4jUtil.cast(databases);
  }

  public void setLocale(Locale locale) {
    throw new UnsupportedOperationException();
  }

  public Locale getLocale() {
    throw new UnsupportedOperationException();
  }

  public void setRoleName(String roleName) throws OlapException {
    throw new UnsupportedOperationException();
  }

  public String getRoleName() {
    throw new UnsupportedOperationException();
  }

  public List<String> getAvailableRoleNames() throws OlapException {
    throw new UnsupportedOperationException();
  }

  public Scenario createScenario() throws OlapException {
    throw new UnsupportedOperationException();
  }

  public void setScenario(Scenario scenario) throws OlapException {
    throw new UnsupportedOperationException();
  }

  public Scenario getScenario() throws OlapException {
    throw new UnsupportedOperationException();
  }

  // do not make public
  HirundoJdbc41Factory getFactory() {
    return (HirundoJdbc41Factory) factory;
  }

  String getUrl() {
    // TODO: make AvaticaConnection.url protected
    throw new UnsupportedOperationException();
  }

  HirundoMeta getMeta() {
    return (HirundoMeta) meta;
  }

  @Override protected ResultSet
  createResultSet(Meta.MetaResultSet metaResultSet, QueryState state)
      throws SQLException {
    return super.createResultSet(metaResultSet, state);
  }

  /** Helps create exceptions. */
  static class OlapHelper {
    static final OlapHelper INSTANCE = new OlapHelper();

    OlapException toOlap(SQLException e) {
      return e instanceof OlapException
          ? (OlapException) e
          : new OlapException(e);
    }

    public OlapException createOlapException(String message, Throwable e) {
      return new OlapException(message, e);
    }
  }

  // do not make public
  Trojan getTrojan() {
    return new Trojan();
  }

  /** Allows acccess to private and protected members of connection
   * to other classes in this package.
   *
   * <p>Do not make public. */
  class Trojan {
    long getMaxRetriesPerExecute() {
      return maxRetriesPerExecute;
    }

    Meta.ExecuteResult prepareAndExecuteInternal(HirundoStatement statement,
        String mdx, long maxRowCount)
        throws SQLException, NoSuchStatementException {
      return HirundoConnection.this.prepareAndExecuteInternal(statement, mdx,
          maxRowCount);
    }
  }
}

// End HirundoConnection.java
