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

import org.olap4j.Axis;
import org.olap4j.CellSet;
import org.olap4j.CellSetAxis;
import org.olap4j.CellSetAxisMetaData;
import org.olap4j.Position;
import org.olap4j.query.QueryAxis;

import java.util.List;
import java.util.ListIterator;

/** Implementation of {@link CellSetAxis} for Hirundo. */
class HirundoCellSetAxis implements CellSetAxis {
  final HirundoCellSet cellSet;
  final ResultAxis axis;
  final QueryAxis queryAxis;

  HirundoCellSetAxis(HirundoCellSet cellSet, ResultAxis axis) {
    this.cellSet = cellSet;
    this.axis = axis;
    this.queryAxis = cellSet.query.getAxes().get(axis.location);
  }

  public Axis getAxisOrdinal() {
    throw new UnsupportedOperationException();
  }

  public CellSet getCellSet() {
    throw new UnsupportedOperationException();
  }

  public CellSetAxisMetaData getAxisMetaData() {
    throw new UnsupportedOperationException();
  }

  public List<Position> getPositions() {
    throw new UnsupportedOperationException();
  }

  public int getPositionCount() {
    throw new UnsupportedOperationException();
  }

  public ListIterator<Position> iterator() {
    throw new UnsupportedOperationException();
  }
}

// End HirundoCellSetAxis.java
