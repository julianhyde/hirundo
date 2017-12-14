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
import org.apache.calcite.jdbc.CalciteMetaImpl;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Linq4j;
import org.apache.calcite.linq4j.function.Function1;
import org.apache.calcite.linq4j.function.Predicate1;

/** Implementation of {@link Meta} for Hirundo, plus some olap4j extensions. */
class HirundoMeta extends CalciteMetaImpl {
  protected HirundoMeta(HirundoConnection connection) {
    super(connection);
  }

  Enumerable<MetaCube> cubes(final MetaSchema schema) {
    return Linq4j.asEnumerable(((HirundoMetaSchema) schema).hirundoSchema.cubes)
        .select(
            new Function1<HirundoCube, MetaCube>() {
              public MetaCube apply(HirundoCube cube) {
                return new MetaCube(cube, schema.tableCatalog,
                    schema.tableSchem, cube.name);
              }
            });
  }

  Enumerable<MetaCube> cubes(final MetaSchema schema,
      final Predicate1<String> matcher) {
    return cubes(schema)
        .where(
            new Predicate1<MetaCube>() {
              public boolean apply(MetaCube v1) {
                return matcher.apply(v1.name);
              }
            });
  }

  public MetaResultSet getCubes(ConnectionHandle ch, String catalog,
      Pat schemaPattern, final Pat cubeNamePattern) {
    final Predicate1<MetaSchema> schemaMatcher = namedMatcher(schemaPattern);
    return createResultSet(schemas(catalog)
            .where(schemaMatcher)
            .selectMany(
                new Function1<MetaSchema, Enumerable<MetaCube>>() {
                  public Enumerable<MetaCube> apply(MetaSchema schema) {
                    return cubes(schema, matcher(cubeNamePattern));
                  }
                }),
        MetaCube.class,
        "CATALOG_NAME",
        "SCHEMA_NAME",
        "CUBE_NAME",
        "CUBE_GUID",
        "CREATED_ON",
        "LAST_SCHEMA_UPDATE",
        "SCHEMA_UPDATED_BY",
        "LAST_DATA_UPDATE",
        "DATA_UPDATED_BY",
        "IS_DRILLTHROUGH_ENABLED",
        "IS_WRITE_ENABLED",
        "IS_LINKABLE",
        "IS_SQL_ENABLED",
        "DESCRIPTION",
        "CUBE_CAPTION");
  }

  @Override protected <E> MetaResultSet createEmptyResultSet(Class<E> clazz) {
    return super.createEmptyResultSet(clazz);
  }

  /** Metadata describing an action. */
  static class MetaAction {
  }

  /** Metadata describing an action. */
  static class MetaDatabase {
  }

  /** Metadata describing a literal. */
  static class MetaLiteral {
  }

  /** Metadata describing a database property. */
  static class MetaDatabaseProperty {
  }

  /** Metadata describing a property. */
  static class MetaProperty {
  }

  /** Metadata describing a dimension. */
  static class MetaDimension {
  }

  /** Metadata describing a function. */
  static class MetaOlapFunction {
  }

  /** Metadata describing a hierarchy. */
  static class MetaHierarchy {
  }

  /** Metadata describing a level. */
  static class MetaLevel {
  }

  /** Metadata describing a measure. */
  static class MetaMeasure {
  }

  /** Metadata describing a member. */
  static class MetaMember {
  }

  /** Metadata describing a named set. */
  static class MetaSet {
  }

  /** Metadata describing a cube. */
  private static class MetaCube {
    public final HirundoCube cube;
    public final String tableCatalog;
    public final String tableSchem;
    public final String name;

    MetaCube(HirundoCube cube, String tableCatalog, String tableSchem,
        String name) {
      this.cube = cube;
      this.tableCatalog = tableCatalog;
      this.tableSchem = tableSchem;
      this.name = name;
    }
  }

  /** Metadata describing a schema. */
  private static class HirundoMetaSchema extends MetaSchema {
    final HirundoSchema hirundoSchema;

    HirundoMetaSchema(String tableCatalog, String tableSchem,
        HirundoSchema hirundoSchema) {
      super(tableCatalog, tableSchem);
      this.hirundoSchema = hirundoSchema;
    }
  }
}

// End HirundoMeta.java
