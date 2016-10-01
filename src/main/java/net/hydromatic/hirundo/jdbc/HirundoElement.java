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

import org.olap4j.metadata.Cube;
import org.olap4j.metadata.MetadataElement;

/** Implementation of {@link Cube} for Hirundo. */
abstract class HirundoElement implements MetadataElement {
  final String name;
  final String uniqueName;
  final Larder larder;

  protected HirundoElement(String name, String uniqueName, Larder larder) {
    this.name = name;
    this.uniqueName = uniqueName;
    this.larder = larder;
  }

  public String getName() {
    return name;
  }

  public String getUniqueName() {
    return uniqueName;
  }

  public String getCaption() {
    return larder.getLocalString(LocalizedProperty.CAPTION);
  }

  public String getDescription() {
    return larder.getLocalString(LocalizedProperty.DESCRIPTION);
  }

  public boolean isVisible() {
    return larder.getBoolean(LocalizedProperty.VISIBLE);
  }

  /** Property that can be localized. */
  enum LocalizedProperty {
    CAPTION,
    DESCRIPTION, SOLVE_ORDER, VISIBLE
  }

  /** Compact data structure holding various properties. */
  static class Larder {
    String getLocalString(LocalizedProperty property) {
      return null;
    }

    boolean getBoolean(LocalizedProperty property) {
      return true;
    }

    public int getInt(LocalizedProperty property) {
      return 0;
    }

    public Object get(String name) {
      return null;
    }
  }
}

// End HirundoElement.java
