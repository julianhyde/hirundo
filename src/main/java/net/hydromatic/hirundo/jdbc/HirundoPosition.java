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

import net.hydromatic.hirundo.prepare.ResultAxis;

import org.olap4j.Position;
import org.olap4j.metadata.Member;

import java.util.List;

/** Implementation of {@link Position} for Hirundo. */
class HirundoPosition implements Position {
  private final ResultAxis axis;
  final List<Member> members;
  final int ordinal;

  HirundoPosition(ResultAxis axis, List<Member> members,
      int ordinal) {
    this.axis = axis;
    this.members = members;
    this.ordinal = ordinal;
  }

  public List<Member> getMembers() {
    return members;
  }

  public int getOrdinal() {
    return ordinal;
  }

  int getAxisOrdinal() {
    return axis.location.axisOrdinal();
  }
}

// End HirundoPosition.java
