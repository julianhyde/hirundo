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
package net.hydromatic.hirundo.prepare;

import org.olap4j.OlapConnection;
import org.olap4j.mdx.SelectNode;
import org.olap4j.mdx.parser.MdxParserFactory;

/** The process of getting an MDX statement ready to execute. */
public class Preparing {
  private final OlapConnection c;
  private final int h;

  public Preparing(OlapConnection c, int h) {
    this.c = c;
    this.h = h;
  }

  public Parsed withMdx(String mdx) {
    final MdxParserFactory parserFactory = c.getParserFactory();
    final SelectNode selectNode =
        parserFactory.createMdxParser(c).parseSelect(mdx);
    return new Parsed(mdx, selectNode);
  }

  public Parsed withNode(SelectNode selectNode) {
    return new Parsed("?", selectNode);
  }

  /** Stage in preparation where a statement has been parsed successfully. */
  public class Parsed {
    private final String mdx;
    private final SelectNode selectNode;

    Parsed(String mdx, SelectNode selectNode) {
      this.mdx = mdx;
      this.selectNode = selectNode;
    }

    public ValidatedQuery validate() {
      return new ValidatedQuery(h, mdx);
    }
  }
}

// End Preparing.java
