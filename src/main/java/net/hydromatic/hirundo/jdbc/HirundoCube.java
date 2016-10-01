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

import com.google.common.base.Preconditions;

import org.olap4j.OlapException;
import org.olap4j.impl.Named;
import org.olap4j.impl.Olap4jUtil;
import org.olap4j.mdx.IdentifierSegment;
import org.olap4j.metadata.Cube;
import org.olap4j.metadata.Dimension;
import org.olap4j.metadata.Hierarchy;
import org.olap4j.metadata.Measure;
import org.olap4j.metadata.Member;
import org.olap4j.metadata.NamedList;
import org.olap4j.metadata.NamedSet;
import org.olap4j.metadata.Schema;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/** Implementation of {@link Cube} for Hirundo. */
class HirundoCube extends HirundoElement implements Cube, Named {
  final HirundoSchema schema;
  final NamedList<HirundoDimension> dimensions;
  final NamedList<HirundoHierarchy> hierarchies;
  final NamedList<HirundoMeasure> measures;
  final NamedList<HirundoNamedSet> namedSets;
  final boolean drillThroughEnabled;

  protected HirundoCube(HirundoSchema schema, String name, String uniqueName,
      Larder larder, NamedList<HirundoDimension> dimensions,
      NamedList<HirundoHierarchy> hierarchies,
      NamedList<HirundoMeasure> measures, NamedList<HirundoNamedSet> namedSets,
      boolean drillThroughEnabled) {
    super(name, uniqueName, larder);
    this.schema = Preconditions.checkNotNull(schema);
    this.dimensions = dimensions;
    this.hierarchies = hierarchies;
    this.measures = measures;
    this.namedSets = namedSets;
    this.drillThroughEnabled = drillThroughEnabled;
  }

  public Schema getSchema() {
    return schema;
  }

  public NamedList<Dimension> getDimensions() {
    return Olap4jUtil.cast(dimensions);
  }

  public NamedList<Hierarchy> getHierarchies() {
    return Olap4jUtil.cast(hierarchies);
  }

  public List<Measure> getMeasures() {
    return Olap4jUtil.cast(measures);
  }

  public NamedList<NamedSet> getSets() {
    return Olap4jUtil.cast(namedSets);
  }

  public Collection<Locale> getSupportedLocales() {
    return schema.supportedLocales;
  }

  public Member lookupMember(List<IdentifierSegment> nameParts)
      throws OlapException {
    throw new UnsupportedOperationException(); // TODO
  }

  public List<Member> lookupMembers(Set<Member.TreeOp> treeOps,
      List<IdentifierSegment> nameParts) throws OlapException {
    throw new UnsupportedOperationException(); // TODO
  }

  public boolean isDrillThroughEnabled() {
    return drillThroughEnabled;
  }
}

// End HirundoCube.java
