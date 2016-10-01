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

import com.google.common.collect.ImmutableList;

import org.olap4j.OlapException;
import org.olap4j.impl.Named;
import org.olap4j.impl.Olap4jUtil;
import org.olap4j.metadata.Catalog;
import org.olap4j.metadata.Cube;
import org.olap4j.metadata.Dimension;
import org.olap4j.metadata.NamedList;
import org.olap4j.metadata.Schema;

import java.util.Collection;
import java.util.Locale;

/** Implementation of {@link Schema} for Hirundo. */
class HirundoSchema extends HirundoElement implements Schema, Named {
  final HirundoCatalog catalog;
  final NamedList<HirundoCube> cubes;
  final NamedList<HirundoDimension> sharedDimensions;
  final ImmutableList<Locale> supportedLocales;

  HirundoSchema(HirundoCatalog catalog, String name,
      NamedList<HirundoCube> cubes,
      NamedList<HirundoDimension> sharedDimensions,
      ImmutableList<Locale> supportedLocales) {
    super(name, null, null);
    this.catalog = catalog;
    this.cubes = cubes;
    this.sharedDimensions = sharedDimensions;
    this.supportedLocales = supportedLocales;
  }

  public Catalog getCatalog() {
    return catalog;
  }

  public NamedList<Cube> getCubes() throws OlapException {
    return Olap4jUtil.cast(cubes);
  }

  public NamedList<Dimension> getSharedDimensions() throws OlapException {
    return Olap4jUtil.cast(sharedDimensions);
  }

  public Collection<Locale> getSupportedLocales() throws OlapException {
    return supportedLocales;
  }
}

// End HirundoSchema.java
