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

import com.google.common.collect.ImmutableList;

import org.olap4j.Cell;
import org.olap4j.CellSet;
import org.olap4j.CellSetAxis;
import org.olap4j.CellSetMetaData;
import org.olap4j.OlapException;
import org.olap4j.Position;
import org.olap4j.query.Query;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Implementation of {@link CellSet} for Hirundo. */
public class HirundoCellSet extends AbstractResultSet
    implements CellSet, Executable {
  final Query query;
  private Result result;
  protected boolean closed;
  private final HirundoCellSetMetaData metaData;
  private final List<CellSetAxis> axisList = new ArrayList<>();
  private CellSetAxis filterAxis;
  private final List<Integer> emptyCoordinates;

  /**
   * Creates a HirundoCellSet.
   *
   * @param statement Statement
   */
  public HirundoCellSet(HirundoStatement statement) {
    super(statement);
    this.query = statement.getQuery();
    assert query != null;
    this.closed = false;
    if (statement instanceof HirundoPreparedStatement) {
      this.metaData = ((HirundoPreparedStatement) statement).cellSetMetaData;
    } else {
      this.metaData = new HirundoCellSetMetaData(statement, query);
    }
    this.emptyCoordinates = Collections.nCopies(query.getAxes().size(), -1);
  }

  /**
   * Executes a query. Not part of the olap4j API; internal to the mondrian
   * driver.
   *
   * <p>This method may take some time. While it is executing, a client may
   * execute {@link HirundoStatement#cancel()}.
   *
   * @throws org.olap4j.OlapException on error
   */
  void execute() throws OlapException {
    result = statement.connection.executor.execute(this);

    // initialize axes
    for (ResultAxis axis : result.axes) {
      axisList.add(new HirundoCellSetAxis(this, axis));
    }

    // initialize filter axis
    final ResultAxis axis = result.slicerAxis;
    filterAxis = new HirundoCellSetAxis(this, axis);
  }

  public <T> T unwrap(Class<T> iface) throws SQLException {
    if (iface.isInstance(this)) {
      return iface.cast(this);
    }
    throw statement.connection.factory.createException("cannot cast");
  }

  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return iface.isInstance(this);
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

  public Cell getCell(List<Integer> coordinates) {
    return getCellInternal(coordinates);
  }

  public Cell getCell(int ordinal) {
    return getCellInternal(ordinalToCoordinates(ordinal));
  }

  public List<Integer> ordinalToCoordinates(int ordinal) {
    ImmutableList<ResultAxis> axes = result.axes;
    final List<Integer> pos = new ArrayList<>();
    int modulo = 1;
    for (ResultAxis axis : axes) {
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
          + getCoordsAsString(pos)
              + ") fall outside CellSet bounds ("
              + getCoordsAsString(dimensions) + ")");
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

  private static String getCoordsAsString(List<Integer> pos) {
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

  public void close() throws SQLException {
    if (closed) {
      return;
    }
    this.closed = true;
    if (this.result != null) {
      this.result.close();
    }
    statement.onResultSetClose(this);
  }

}

// End HirundoCellSet.java
