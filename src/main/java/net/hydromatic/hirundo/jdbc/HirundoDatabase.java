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

import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.impl.Named;
import org.olap4j.impl.Olap4jUtil;
import org.olap4j.metadata.Catalog;
import org.olap4j.metadata.Database;
import org.olap4j.metadata.NamedList;

import java.util.List;

/** Implementation of {@link Database} for Hirundo. */
class HirundoDatabase extends HirundoElement implements Database, Named {
  final HirundoConnection connection;
  final NamedList<HirundoCatalog> catalogs;

  protected HirundoDatabase(HirundoConnection connection, String name,
      NamedList<HirundoCatalog> catalogs) {
    super(name, null, null);
    this.connection = connection;
    this.catalogs = catalogs;
  }

  public OlapConnection getOlapConnection() {
    return connection;
  }

  public String getURL() throws OlapException {
    return connection.getUrl();
  }

  public String getDataSourceInfo() throws OlapException {
    throw new UnsupportedOperationException();
  }

  public String getProviderName() throws OlapException {
    throw new UnsupportedOperationException();
  }

  public List<ProviderType> getProviderTypes() throws OlapException {
    throw new UnsupportedOperationException();
  }

  public List<AuthenticationMode> getAuthenticationModes()
      throws OlapException {
    throw new UnsupportedOperationException();
  }

  public NamedList<Catalog> getCatalogs() throws OlapException {
    return Olap4jUtil.cast(catalogs);
  }
}

// End HirundoDatabase.java
