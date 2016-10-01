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

import net.hydromatic.hirundo.prepare.Result;
import net.hydromatic.hirundo.prepare.ResultAxis;
import net.hydromatic.hirundo.prepare.ValidatedQuery;

import org.apache.calcite.avatica.AvaticaStatement;
import org.apache.calcite.avatica.Meta;
import org.apache.calcite.jdbc.CalciteOlapStatement;
import org.apache.calcite.jdbc.CalcitePrepare;
import org.apache.calcite.jdbc.CalciteResultSet;

import org.olap4j.Cell;
import org.olap4j.CellSet;
import org.olap4j.CellSetAxis;
import org.olap4j.CellSetMetaData;
import org.olap4j.Position;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

/** Implementation of {@link CellSet} for Hirundo. */
public class HirundoCellSet extends CalciteResultSet
    implements CellSet, Executable {
  final ValidatedQuery query;
  private Result result;
  private final HirundoCellSetMetaData metaData;
  private final List<CellSetAxis> axisList = new ArrayList<>();
  private CellSetAxis filterAxis;
  private final List<Integer> emptyCoordinates;

  /**
   * Creates a HirundoCellSet.
   *
   * @param statement Statement
   */
  protected HirundoCellSet(AvaticaStatement statement,
      CalcitePrepare.CalciteSignature calciteSignature,
      ResultSetMetaData resultSetMetaData, TimeZone timeZone,
      Meta.Frame firstFrame) {
    super(statement, calciteSignature, resultSetMetaData, timeZone, firstFrame);
    this.metaData = (HirundoCellSetMetaData) resultSetMetaData;
    this.query = metaData.statement.getQuery();
    assert query != null;
    this.emptyCoordinates = Collections.nCopies(query.getAxes().size(), -1);
  }

  @Override public CalciteOlapStatement getStatement() {
    return (CalciteOlapStatement) super.getStatement();
  }

  public CellSetMetaData getMetaData() {
    return metaData;
  }

  public List<CellSetAxis> getAxes() {
    return axisList;
  }

  public CellSetAxis getFilterAxis() {
    return filterAxis;
  }

  @Override protected HirundoCellSet execute() throws SQLException {
    super.execute();
    result = (Result) cursor;
    for (ResultAxis axis : result.axes) {
      axisList.add(new HirundoCellSetAxis(this, axis));
    }
    filterAxis = new HirundoCellSetAxis(this, result.slicerAxis);
    return this;
  }

  public Cell getCell(List<Integer> coordinates) {
    return getCellInternal(coordinates);
  }

  public Cell getCell(int ordinal) {
    return getCellInternal(ordinalToCoordinates(ordinal));
  }

  public List<Integer> ordinalToCoordinates(int ordinal) {
    final List<Integer> pos = new ArrayList<>();
    int modulo = 1;
    for (ResultAxis axis : result.axes) {
      int prevModulo = modulo;
      modulo *= axis.positions.size();
      pos.add((ordinal % modulo) / prevModulo);
    }
    if (ordinal < 0 || ordinal >= modulo) {
      throw new IndexOutOfBoundsException("Cell ordinal " + ordinal
          + ") lies outside CellSet bounds ("
          + getBoundsAsString() + ")");
    }
    return pos;
  }

  public Cell getCell(Position... positions) {
    if (positions.length != axisList.size()) {
      throw new IllegalArgumentException(
          "Coordinates have different dimension (" + positions.length
              + ") than axes (" + axisList.size() + ")");
    }
    final List<Integer> coordinates = new ArrayList<>(emptyCoordinates);
    for (Position position : positions) {
      final int ordinal =
          ((HirundoPosition) position).getAxisOrdinal();
      if (coordinates.get(ordinal) != -1) {
        throw new IllegalArgumentException("Coordinates contain axis "
            + ordinal + " more than once");
      }
      coordinates.set(ordinal, position.getOrdinal());
    }
    return getCellInternal(coordinates);
  }

  private Cell getCellInternal(List<Integer> pos) {
    Cell cell = result.getCell(pos);
    if (cell == null) {
      if (pos.size() != result.axes.size()) {
        throw new IllegalArgumentException("Cell coordinates should have "
            + "dimension " + axisList.size());
      }
      List<Integer> dimensions = new ArrayList<>();
      for (CellSetAxis axis : axisList) {
        dimensions.add(axis.getPositions().size());
      }
      throw new IndexOutOfBoundsException("Cell coordinates ("
          + getCoordinatesAsString(pos)
              + ") fall outside CellSet bounds ("
              + getCoordinatesAsString(dimensions) + ")");
    }
    return cell;
  }

  private String getBoundsAsString() {
    StringBuilder buf = new StringBuilder();
    int i = 0;
    for (ResultAxis axis : result.axes) {
      if (i++ > 0) {
        buf.append(", ");
      }
      buf.append(axis.positions.size());
    }
    return buf.toString();
  }

  private static String getCoordinatesAsString(List<Integer> pos) {
    StringBuilder buf = new StringBuilder();
    for (int i = 0; i < pos.size(); i++) {
      int po = pos.get(i);
      if (i > 0) {
        buf.append(", ");
      }
      buf.append(po);
    }
    return buf.toString();
  }

  public int coordinatesToOrdinal(List<Integer> coordinates) {
    List<CellSetAxis> axes = getAxes();
    if (coordinates.size() != axes.size()) {
      throw new IllegalArgumentException(
          "Coordinates have different dimension (" + coordinates.size()
              + ") than axes (" + axes.size() + ")");
    }
    int modulo = 1;
    int ordinal = 0;
    int k = 0;
    for (CellSetAxis axis : axes) {
      final Integer coordinate = coordinates.get(k++);
      if (coordinate < 0 || coordinate >= axis.getPositionCount()) {
        throw new IndexOutOfBoundsException(
            "Coordinate " + coordinate
                + " of axis " + k
                + " is out of range ("
                + getBoundsAsString() + ")");
      }
      ordinal += coordinate * modulo;
      modulo *= axis.getPositionCount();
    }
    return ordinal;
  }

  public boolean next() throws SQLException {
    throw new UnsupportedOperationException();
  }

}

// End HirundoCellSet.java
