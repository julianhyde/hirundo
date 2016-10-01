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

import org.apache.calcite.avatica.AvaticaConnection;
import org.apache.calcite.avatica.AvaticaDatabaseMetaData;
import org.apache.calcite.avatica.Meta;
import org.apache.calcite.avatica.QueryState;
import org.apache.calcite.avatica.remote.MetaDataOperation;

import org.olap4j.CellSetListener;
import org.olap4j.OlapConnection;
import org.olap4j.OlapDatabaseMetaData;
import org.olap4j.OlapException;
import org.olap4j.metadata.Member;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

/** Implementation of {@link OlapDatabaseMetaData} for Hirundo. */
class HirundoDatabaseMetaData extends AvaticaDatabaseMetaData
    implements OlapDatabaseMetaData {
  private final HirundoConnection connection;

  /** Creates a HirundoDatabaseMetaData. */
  HirundoDatabaseMetaData(HirundoConnection connection) {
    super(connection);
    this.connection = connection;
  }

  private static Meta.Pat pat(String schemaPattern) {
    return Meta.Pat.of(schemaPattern);
  }

  private Meta.MetaResultSet empty(Class<?> metaClass) {
    return connection.getMeta().createEmptyResultSet(metaClass);
  }

  public OlapConnection getConnection() throws SQLException {
    return connection;
  }

  public Set<CellSetListener.Granularity>
  getSupportedCellSetListenerGranularities() throws OlapException {
    throw new UnsupportedOperationException();
  }

  public ResultSet getActions(final String catalog, final String schemaPattern,
      final String cubeNamePattern, final String actionNamePattern)
      throws OlapException {
    try {
      return connection.invokeWithRetries(
          new AvaticaConnection.CallableWithoutException<ResultSet>() {
            public ResultSet call() {
              try {
                return connection.createResultSet(
                    empty(HirundoMeta.MetaAction.class),
                    new QueryState(MetaDataOperation.GET_UDTS, "GET_ACTIONS",
                        catalog, schemaPattern, cubeNamePattern,
                        actionNamePattern));
              } catch (SQLException e) {
                throw new RuntimeException(e);
              }
            }
          });
    } catch (RuntimeException e) {
      Throwable cause = e.getCause();
      if (cause instanceof OlapException) {
        throw (OlapException) cause;
      }
      throw e;
    }
  }

  public ResultSet getDatabases() throws OlapException {
    try {
      return connection.invokeWithRetries(
          new AvaticaConnection.CallableWithoutException<ResultSet>() {
            public ResultSet call() {
              try {
                return connection.createResultSet(
                    empty(HirundoMeta.MetaDatabase.class),
                    new QueryState(MetaDataOperation.GET_UDTS,
                        "GET_DATABASES"));
              } catch (SQLException e) {
                throw new RuntimeException(e);
              }
            }
          });
    } catch (RuntimeException e) {
      Throwable cause = e.getCause();
      if (cause instanceof OlapException) {
        throw (OlapException) cause;
      }
      throw e;
    }
  }

  public ResultSet getLiterals() throws OlapException {
    try {
      return connection.invokeWithRetries(
          new AvaticaConnection.CallableWithoutException<ResultSet>() {
            public ResultSet call() {
              try {
                return connection.createResultSet(
                    empty(HirundoMeta.MetaLiteral.class),
                    new QueryState(MetaDataOperation.GET_UDTS,
                        "GET_LITERALS"));
              } catch (SQLException e) {
                throw new RuntimeException(e);
              }
            }
          });
    } catch (RuntimeException e) {
      Throwable cause = e.getCause();
      if (cause instanceof OlapException) {
        throw (OlapException) cause;
      }
      throw e;
    }
  }

  public ResultSet getDatabaseProperties(final String dataSourceName,
      final String propertyNamePattern) throws OlapException {
    try {
      return connection.invokeWithRetries(
          new AvaticaConnection.CallableWithoutException<ResultSet>() {
            public ResultSet call() {
              try {
                return connection.createResultSet(
                    empty(HirundoMeta.MetaDatabaseProperty.class),
                    new QueryState(MetaDataOperation.GET_UDTS,
                        "GET_DATABASE_PROPERTIES", dataSourceName,
                        propertyNamePattern));
              } catch (SQLException e) {
                throw new RuntimeException(e);
              }
            }
          });
    } catch (RuntimeException e) {
      Throwable cause = e.getCause();
      if (cause instanceof OlapException) {
        throw (OlapException) cause;
      }
      throw e;
    }
  }

  public ResultSet getProperties(final String catalog,
      final String schemaPattern, final String cubeNamePattern,
      final String dimensionUniqueName, final String hierarchyUniqueName,
      final String levelUniqueName, final String memberUniqueName,
      final String propertyNamePattern) throws OlapException {
    try {
      return connection.invokeWithRetries(
          new AvaticaConnection.CallableWithoutException<ResultSet>() {
            public ResultSet call() {
              try {
                return connection.createResultSet(
                    empty(HirundoMeta.MetaProperty.class),
                    new QueryState(MetaDataOperation.GET_UDTS,
                        "GET_PROPERTIES", catalog, schemaPattern,
                        cubeNamePattern, dimensionUniqueName,
                        hierarchyUniqueName, levelUniqueName,
                        memberUniqueName, propertyNamePattern));
              } catch (SQLException e) {
                throw new RuntimeException(e);
              }
            }
          });
    } catch (RuntimeException e) {
      Throwable cause = e.getCause();
      if (cause instanceof OlapException) {
        throw (OlapException) cause;
      }
      throw e;
    }
  }

  public String getMdxKeywords() throws OlapException {
    throw new UnsupportedOperationException();
  }

  public ResultSet getCubes(final String catalog, final String schemaPattern,
      final String cubeNamePattern) throws OlapException {
    try {
      return connection.invokeWithRetries(
          new AvaticaConnection.CallableWithoutException<ResultSet>() {
            public ResultSet call() {
              try {
                return connection.createResultSet(
                    connection.getMeta().getCubes(connection.handle, catalog,
                        pat(schemaPattern), pat(cubeNamePattern)),
                    new QueryState(MetaDataOperation.GET_UDTS, "GET_CUBES",
                        catalog, schemaPattern, cubeNamePattern));
              } catch (SQLException e) {
                throw new RuntimeException(e);
              }
            }
          });
    } catch (RuntimeException e) {
      Throwable cause = e.getCause();
      if (cause instanceof OlapException) {
        throw (OlapException) cause;
      }
      throw e;
    }
  }

  public ResultSet getDimensions(final String catalog,
      final String schemaPattern, final String cubeNamePattern,
      final String dimensionNamePattern) throws OlapException {
    try {
      return connection.invokeWithRetries(
          new AvaticaConnection.CallableWithoutException<ResultSet>() {
            public ResultSet call() {
              try {
                return connection.createResultSet(
                    empty(HirundoMeta.MetaDimension.class),
                    new QueryState(MetaDataOperation.GET_UDTS,
                        "GET_DIMENSIONS", catalog, schemaPattern,
                        cubeNamePattern, dimensionNamePattern));
              } catch (SQLException e) {
                throw new RuntimeException(e);
              }
            }
          });
    } catch (RuntimeException e) {
      Throwable cause = e.getCause();
      if (cause instanceof OlapException) {
        throw (OlapException) cause;
      }
      throw e;
    }
  }

  public ResultSet getOlapFunctions(final String functionNamePattern)
      throws OlapException {
    try {
      return connection.invokeWithRetries(
          new AvaticaConnection.CallableWithoutException<ResultSet>() {
            public ResultSet call() {
              try {
                return connection.createResultSet(
                    empty(HirundoMeta.MetaOlapFunction.class),
                    new QueryState(MetaDataOperation.GET_UDTS,
                        "GET_OLAP_FUNCTIONS", functionNamePattern));
              } catch (SQLException e) {
                throw new RuntimeException(e);
              }
            }
          });
    } catch (RuntimeException e) {
      Throwable cause = e.getCause();
      if (cause instanceof OlapException) {
        throw (OlapException) cause;
      }
      throw e;
    }
  }

  public ResultSet getHierarchies(final String catalog,
      final String schemaPattern, final String cubeNamePattern,
      final String dimensionUniqueName, final String hierarchyNamePattern)
      throws OlapException {
    try {
      return connection.invokeWithRetries(
          new AvaticaConnection.CallableWithoutException<ResultSet>() {
            public ResultSet call() {
              try {
                return connection.createResultSet(
                    empty(HirundoMeta.MetaHierarchy.class),
                    new QueryState(MetaDataOperation.GET_UDTS,
                        "GET_HIERARCHIES", catalog, schemaPattern,
                        cubeNamePattern, dimensionUniqueName,
                        hierarchyNamePattern));
              } catch (SQLException e) {
                throw new RuntimeException(e);
              }
            }
          });
    } catch (RuntimeException e) {
      Throwable cause = e.getCause();
      if (cause instanceof OlapException) {
        throw (OlapException) cause;
      }
      throw e;
    }
  }

  public ResultSet getLevels(final String catalog, final String schemaPattern,
      final String cubeNamePattern, final String dimensionUniqueName,
      final String hierarchyUniqueName, final String levelNamePattern)
      throws OlapException {
    try {
      return connection.invokeWithRetries(
          new AvaticaConnection.CallableWithoutException<ResultSet>() {
            public ResultSet call() {
              try {
                return connection.createResultSet(
                    empty(HirundoMeta.MetaLevel.class),
                    new QueryState(MetaDataOperation.GET_UDTS, "GET_LEVELS",
                        catalog, schemaPattern, cubeNamePattern,
                        dimensionUniqueName, hierarchyUniqueName,
                        levelNamePattern));
              } catch (SQLException e) {
                throw new RuntimeException(e);
              }
            }
          });
    } catch (RuntimeException e) {
      Throwable cause = e.getCause();
      if (cause instanceof OlapException) {
        throw (OlapException) cause;
      }
      throw e;
    }
  }

  public ResultSet getMeasures(final String catalog, final String schemaPattern,
      final String cubeNamePattern, final String measureNamePattern,
      final String measureUniqueName) throws OlapException {
    try {
      return connection.invokeWithRetries(
          new AvaticaConnection.CallableWithoutException<ResultSet>() {
            public ResultSet call() {
              try {
                return connection.createResultSet(
                    empty(HirundoMeta.MetaMeasure.class),
                    new QueryState(MetaDataOperation.GET_UDTS, "GET_MEASURES",
                        catalog, schemaPattern, cubeNamePattern,
                        measureNamePattern, measureUniqueName));
              } catch (SQLException e) {
                throw new RuntimeException(e);
              }
            }
          });
    } catch (RuntimeException e) {
      Throwable cause = e.getCause();
      if (cause instanceof OlapException) {
        throw (OlapException) cause;
      }
      throw e;
    }
  }

  public ResultSet getMembers(final String catalog, final String schemaPattern,
      final String cubeNamePattern, final String dimensionUniqueName,
      final String hierarchyUniqueName, final String levelUniqueName,
      final String memberUniqueName, final Set<Member.TreeOp> treeOps)
      throws OlapException {
    try {
      return connection.invokeWithRetries(
          new AvaticaConnection.CallableWithoutException<ResultSet>() {
            public ResultSet call() {
              try {
                return connection.createResultSet(
                    empty(HirundoMeta.MetaMember.class),
                    new QueryState(MetaDataOperation.GET_UDTS, "GET_MEMBERS",
                        catalog, schemaPattern, cubeNamePattern,
                        dimensionUniqueName, hierarchyUniqueName,
                        levelUniqueName, memberUniqueName, treeOps));
              } catch (SQLException e) {
                throw new RuntimeException(e);
              }
            }
          });
    } catch (RuntimeException e) {
      Throwable cause = e.getCause();
      if (cause instanceof OlapException) {
        throw (OlapException) cause;
      }
      throw e;
    }
  }

  public ResultSet getSets(final String catalog, final String schemaPattern,
      final String cubeNamePattern, final String setNamePattern)
      throws OlapException {
    try {
      return connection.invokeWithRetries(
          new AvaticaConnection.CallableWithoutException<ResultSet>() {
            public ResultSet call() {
              try {
                return connection.createResultSet(
                    empty(HirundoMeta.MetaSet.class),
                    new QueryState(MetaDataOperation.GET_UDTS, "GET_SETS",
                        catalog, schemaPattern, cubeNamePattern,
                        setNamePattern));
              } catch (SQLException e) {
                throw new RuntimeException(e);
              }
            }
          });
    } catch (RuntimeException e) {
      Throwable cause = e.getCause();
      if (cause instanceof OlapException) {
        throw (OlapException) cause;
      }
      throw e;
    }
  }

}

// End HirundoDatabaseMetaData.java
