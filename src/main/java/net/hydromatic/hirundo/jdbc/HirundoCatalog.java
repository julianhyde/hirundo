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

import org.olap4j.OlapDatabaseMetaData;
import org.olap4j.OlapException;
import org.olap4j.impl.Named;
import org.olap4j.impl.Olap4jUtil;
import org.olap4j.metadata.Catalog;
import org.olap4j.metadata.Database;
import org.olap4j.metadata.NamedList;
import org.olap4j.metadata.Schema;

/** Implementation of {@link Schema} for Hirundo. */
class HirundoCatalog extends HirundoElement implements Catalog, Named {
  final HirundoDatabase database;
  final NamedList<HirundoSchema> schemas;
  final NamedList<HirundoDimension> sharedDimensions;
  final HirundoDatabaseMetaData databaseMetaData;

  HirundoCatalog(HirundoDatabase database, String name,
      NamedList<HirundoSchema> schemas,
      NamedList<HirundoDimension> sharedDimensions,
      HirundoDatabaseMetaData databaseMetaData) {
    super(name, null, null);
    this.database = database;
    this.schemas = schemas;
    this.sharedDimensions = sharedDimensions;
    this.databaseMetaData = databaseMetaData;
  }

  public Database getDatabase() {
    return database;
  }

  public NamedList<Schema> getSchemas() throws OlapException {
    return Olap4jUtil.cast(schemas);
  }

  public OlapDatabaseMetaData getMetaData() {
    return databaseMetaData;
  }
}

// End HirundoCatalog.java
