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

import org.apache.calcite.avatica.AvaticaResultSetMetaData;
import org.apache.calcite.avatica.AvaticaStatement;
import org.apache.calcite.avatica.Meta;

import org.olap4j.CellSetAxisMetaData;
import org.olap4j.CellSetMetaData;
import org.olap4j.metadata.Cube;
import org.olap4j.metadata.NamedList;
import org.olap4j.metadata.Property;

/** Implementation of {@link CellSetMetaData} for Hirundo. */
class HirundoCellSetMetaData extends AvaticaResultSetMetaData
    implements CellSetMetaData {
  final HirundoStatement statement;

  HirundoCellSetMetaData(AvaticaStatement statement, Object query,
      Meta.Signature signature) {
    super(statement, query, signature);
    this.statement = (HirundoStatement) statement;
  }

  public NamedList<Property> getCellProperties() {
    return null;
  }

  public Cube getCube() {
    return null;
  }

  public NamedList<CellSetAxisMetaData> getAxesMetaData() {
    return null;
  }

  public CellSetAxisMetaData getFilterAxisMetaData() {
    return null;
  }
}

// End HirundoCellSetMetaData.java
