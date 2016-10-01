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
import org.olap4j.mdx.ParseTreeNode;
import org.olap4j.metadata.Dimension;
import org.olap4j.metadata.Hierarchy;
import org.olap4j.metadata.Level;
import org.olap4j.metadata.Member;
import org.olap4j.metadata.NamedList;
import org.olap4j.metadata.Property;

import java.util.List;

/** Implementation of {@link Member} for Hirundo. */
class HirundoMember extends HirundoElement implements Member {
  final HirundoLevel level;
  final HirundoMember parentMember; // may be null
  private Type memberType;

  protected HirundoMember(HirundoMember parentMember, HirundoLevel level,
      String name, String uniqueName, Larder larder) {
    super(name, uniqueName, larder);
    this.parentMember = parentMember;
    this.level = level;
  }

  public NamedList<? extends Member> getChildMembers() throws OlapException {
    throw new UnsupportedOperationException(); // TODO
  }

  public int getChildMemberCount() throws OlapException {
    throw new UnsupportedOperationException(); // TODO
  }

  public Member getParentMember() {
    return parentMember;
  }

  public Level getLevel() {
    return level;
  }

  public Hierarchy getHierarchy() {
    return level.hierarchy;
  }

  public Dimension getDimension() {
    return level.hierarchy.dimension;
  }

  public Type getMemberType() {
    return memberType;
  }

  public boolean isAll() {
    return memberType == Type.ALL;
  }

  public boolean isChildOrEqualTo(Member member) {
    throw new UnsupportedOperationException(); // TODO
  }

  public boolean isCalculated() {
    return memberType == Type.FORMULA;
  }

  public int getSolveOrder() {
    return larder.getInt(LocalizedProperty.SOLVE_ORDER);
  }

  public ParseTreeNode getExpression() {
    throw new UnsupportedOperationException(); // TODO
  }

  public List<Member> getAncestorMembers() {
    throw new UnsupportedOperationException(); // TODO
  }

  public boolean isCalculatedInQuery() {
    throw new UnsupportedOperationException(); // TODO
  }

  public Object getPropertyValue(Property property) {
    if (property instanceof Property.StandardMemberProperty) {
      Member parentMember;
      switch ((Property.StandardMemberProperty) property) {
      case DESCRIPTION:
        return getDescription();

      case CATALOG_NAME:
        return level.hierarchy.dimension.cube.schema.catalog.name;

      case SCHEMA_NAME:
        return level.hierarchy.dimension.cube.schema.name;

      case CUBE_NAME:
        return level.hierarchy.dimension.cube.name;

      case DIMENSION_UNIQUE_NAME:
        return level.hierarchy.dimension.uniqueName;

      case HIERARCHY_UNIQUE_NAME:
        return level.hierarchy.uniqueName;

      case LEVEL_UNIQUE_NAME:
        return level.uniqueName;

      case LEVEL_NUMBER:
        return level.getDepth();

      case MEMBER_UNIQUE_NAME:
        return uniqueName;

      case MEMBER_NAME:
        return name;

      case MEMBER_TYPE:
        return memberType;

      case MEMBER_GUID:
        return null;

      case MEMBER_CAPTION:
        return getCaption();

      case MEMBER_ORDINAL:
        return getOrdinal();

      case CHILDREN_CARDINALITY:
        throw new UnsupportedOperationException();

      case PARENT_LEVEL:
        parentMember = getParentMember();
        return parentMember == null
            ? 0
            : parentMember.getLevel().getDepth();

      case PARENT_UNIQUE_NAME:
        parentMember = getParentMember();
        return parentMember == null
            ? null
            : parentMember.getUniqueName();

      case PARENT_COUNT:
        parentMember = getParentMember();
        return parentMember == null ? 0 : 1;

      default:
        break;
      // fall through
      }
    }
    return larder.get(property.getName());
  }

  public String getPropertyFormattedValue(Property property)
      throws OlapException {
    return null;
  }

  public void setProperty(Property property, Object value)
      throws OlapException {
  }

  public NamedList<Property> getProperties() {
    return null;
  }

  public int getOrdinal() {
    return 0;
  }

  public boolean isHidden() {
    return false;
  }

  public int getDepth() {
    return 0;
  }

  public Member getDataMember() {
    return null;
  }

}

// End HirundoMember.java
