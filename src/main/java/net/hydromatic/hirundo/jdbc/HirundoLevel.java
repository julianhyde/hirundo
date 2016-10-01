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

import org.olap4j.OlapException;
import org.olap4j.metadata.Dimension;
import org.olap4j.metadata.Level;
import org.olap4j.metadata.Member;
import org.olap4j.metadata.NamedList;
import org.olap4j.metadata.Property;

import java.util.List;

/** Implementation of {@link Level} for Hirundo. */
class HirundoLevel extends HirundoElement implements Level {
  final HirundoHierarchy hierarchy;

  protected HirundoLevel(HirundoHierarchy hierarchy, String name,
      String uniqueName, Larder larder) {
    super(name, uniqueName, larder);
    this.hierarchy = hierarchy;
  }

  public HirundoHierarchy getHierarchy() {
    return hierarchy;
  }

  public int getDepth() {
    throw new UnsupportedOperationException();
  }

  public Dimension getDimension() {
    throw new UnsupportedOperationException();
  }

  public Type getLevelType() {
    throw new UnsupportedOperationException();
  }

  public boolean isCalculated() {
    throw new UnsupportedOperationException();
  }

  public NamedList<Property> getProperties() {
    throw new UnsupportedOperationException();
  }

  public List<Member> getMembers() throws OlapException {
    throw new UnsupportedOperationException();
  }

  public int getCardinality() {
    throw new UnsupportedOperationException();
  }
}

// End HirundoLevel.java
