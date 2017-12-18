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
package net.hydromatic.hirundo.prepare;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import org.apache.calcite.avatica.AvaticaParameter;
import org.apache.calcite.avatica.ColumnMetaData;
import org.apache.calcite.avatica.Meta;
import org.apache.calcite.jdbc.CalcitePrepare;
import org.apache.calcite.rel.RelCollation;

import org.olap4j.Axis;

import java.sql.ResultSet;
import java.util.Map;

/** MDX query that has been validated and is ready to execute. */
public class ValidatedQuery {
  private final Map<Axis, ValidatedQueryAxis> axisMap = ImmutableMap.of();
  public final int h;
  public final Meta.Signature signature;
  public final int resultSetType;
  public final int resultSetConcurrency;
  public final int resultSetHoldability;

  ValidatedQuery(int h, String mdx) {
    this.h = h;
    this.signature = new CalcitePrepare.CalciteSignature<Object>(mdx,
        ImmutableList.<AvaticaParameter>of(), ImmutableMap.<String, Object>of(),
        null, ImmutableList.<ColumnMetaData>of(), null, null,
        ImmutableList.<RelCollation>of(), -1L, null, Meta.StatementType.SELECT);
    this.resultSetType = ResultSet.TYPE_FORWARD_ONLY;
    this.resultSetConcurrency = ResultSet.CONCUR_READ_ONLY;
    this.resultSetHoldability = ResultSet.CLOSE_CURSORS_AT_COMMIT;
  }

  public Map<Axis, ValidatedQueryAxis> getAxes() {
    return axisMap;
  }
}

// End ValidatedQuery.java
