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
import org.olap4j.impl.Olap4jUtil;
import org.olap4j.metadata.Hierarchy;
import org.olap4j.metadata.Level;
import org.olap4j.metadata.Member;
import org.olap4j.metadata.NamedList;

/** Implementation of {@link Hierarchy} for Hirundo. */
class HirundoHierarchy extends HirundoElement implements Hierarchy {
  final HirundoDimension dimension;
  final NamedList<HirundoLevel> levels;
  final boolean hasAll;
  final HirundoMember defaultMember;
  final NamedList<HirundoMember> rootMembers;

  protected HirundoHierarchy(HirundoDimension dimension, String name,
      String uniqueName, Larder larder, NamedList<HirundoLevel> levels,
      boolean hasAll, HirundoMember defaultMember,
      NamedList<HirundoMember> rootMembers) {
    super(name, uniqueName, larder);
    this.dimension = dimension;
    this.levels = levels;
    this.hasAll = hasAll;
    this.defaultMember = defaultMember;
    this.rootMembers = rootMembers;
  }

  public HirundoDimension getDimension() {
    return dimension;
  }

  public NamedList<Level> getLevels() {
    return Olap4jUtil.cast(levels);
  }

  public boolean hasAll() {
    return hasAll;
  }

  public Member getDefaultMember() throws OlapException {
    return defaultMember;
  }

  public NamedList<Member> getRootMembers() throws OlapException {
    return Olap4jUtil.cast(rootMembers);
  }
}

// End HirundoHierarchy.java
