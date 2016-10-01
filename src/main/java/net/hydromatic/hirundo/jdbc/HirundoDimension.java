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
import org.olap4j.impl.Named;
import org.olap4j.impl.Olap4jUtil;
import org.olap4j.metadata.Dimension;
import org.olap4j.metadata.Hierarchy;
import org.olap4j.metadata.NamedList;

/** Implementation of {@link Dimension} for Hirundo. */
class HirundoDimension extends HirundoElement implements Dimension, Named {
  /** Every dimension belongs to a cube. "Shared" dimensions belong to a funny
   * cube. */
  final HirundoCube cube;
  final NamedList<HirundoHierarchy> hierarchies;
  final Type dimensionType;
  final HirundoHierarchy defaultHierarchy;

  protected HirundoDimension(HirundoCube cube, String name,
      String uniqueName, Larder larder,
      NamedList<HirundoHierarchy> hierarchies,
      HirundoHierarchy defaultHierarchy, Type dimensionType) {
    super(name, uniqueName, larder);
    this.cube = cube;
    this.hierarchies = hierarchies;
    this.defaultHierarchy = defaultHierarchy;
    this.dimensionType = dimensionType;
  }

  public NamedList<Hierarchy> getHierarchies() {
    return Olap4jUtil.cast(hierarchies);
  }

  public Type getDimensionType() throws OlapException {
    return dimensionType;
  }

  public Hierarchy getDefaultHierarchy() {
    return defaultHierarchy;
  }
}

// End HirundoDimension.java
