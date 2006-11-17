/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.solr.search;

import org.apache.lucene.search.*;
import org.apache.solr.search.function.*;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;
import org.apache.solr.core.SolrCore;
import org.apache.solr.core.SolrException;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.schema.FieldType;
import org.apache.solr.request.SolrParams;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.logging.Level;
import java.io.IOException;

/**
 * Collection of static utilities usefull for query parsing.
 *
 * @author yonik
 * @version $Id: QueryParsing.java 472574 2006-11-08 18:25:52Z yonik $
 */
public class QueryParsing {
  /** the SolrParam used to override the QueryParser "default operator" */
  public static final String OP = "q.op";

  /** 
   * Helper utility for parsing a query using the Lucene QueryParser syntax. 
   * @param qs query expression in standard Lucene syntax
   * @param schema used for default operator (overridden by params) and passed to the query parser for field format analysis information
   */
  public static Query parseQuery(String qs, IndexSchema schema) {
    return parseQuery(qs, null, schema);
  }

  /** 
   * Helper utility for parsing a query using the Lucene QueryParser syntax. 
   * @param qs query expression in standard Lucene syntax
   * @param defaultField default field used for unqualified search terms in the query expression
   * @param schema used for default operator (overridden by params) and passed to the query parser for field format analysis information
   */
  public static Query parseQuery(String qs, String defaultField, IndexSchema schema) {
    try {
      Query query = new SolrQueryParser(schema, defaultField).parse(qs);

      if (SolrCore.log.isLoggable(Level.FINEST)) {
        SolrCore.log.finest("After QueryParser:" + query);
      }

      return query;

    } catch (ParseException e) {
      SolrCore.log(e);
      throw new SolrException(400,"Error parsing Lucene query",e);
    }
  }

  /**
   * Helper utility for parsing a query using the Lucene QueryParser syntax. 
   * @param qs query expression in standard Lucene syntax
   * @param defaultField default field used for unqualified search terms in the query expression
   * @param params used to determine the default operator, overriding the schema specified operator
   * @param schema used for default operator (overridden by params) and passed to the query parser for field format analysis information
   */
  public static Query parseQuery(String qs, String defaultField, SolrParams params, IndexSchema schema) {
    try {
      String opParam = params.get(OP, schema.getQueryParserDefaultOperator());
      QueryParser.Operator defaultOperator = "AND".equals(opParam) ? QueryParser.Operator.AND : QueryParser.Operator.OR;
      SolrQueryParser parser = new SolrQueryParser(schema, defaultField);
      parser.setDefaultOperator(defaultOperator);
      Query query = parser.parse(qs);

      if (SolrCore.log.isLoggable(Level.FINEST)) {
        SolrCore.log.finest("After QueryParser:" + query);
      }

      return query;

    } catch (ParseException e) {
      SolrCore.log(e);
      throw new SolrException(400,"Error parsing Lucene query",e);
    }
  }

  /***
   * SortSpec encapsulates a Lucene Sort and a count of the number of documents
   * to return.
   */
  public static class SortSpec {
    private final Sort sort;
    private final int num;

    SortSpec(Sort sort, int num) {
      this.sort=sort;
      this.num=num;
    }

    /**
     * Gets the Lucene Sort object, or null for the default sort
     * by score descending.
     */
    public Sort getSort() { return sort; }

    /**
     * Gets the number of documens to return after sorting.
     *
     * @return number of docs to return, or -1 for no cut off (just sort)
     */
    public int getCount() { return num; }
  }


  private static Pattern sortSeparator = Pattern.compile("[\\s,]+");

  /**
   * Returns null if the sortSpec string doesn't look like a sort specification,
   * or if the sort specification couldn't be converted into a Lucene Sort
   * (because of a field not being indexed or undefined, etc).
   *
   * <p>
   * The form of the sort specification string currently parsed is:
   * </p>
   * <pre>>
   * SortSpec ::= SingleSort [, SingleSort]* <number>?
   * SingleSort ::= <fieldname> SortDirection
   * SortDirection ::= top | desc | bottom | asc
   * </pre>
   * Examples:
   * <pre>
   *   top 10                        #take the top 10 by score
   *   desc 10                       #take the top 10 by score
   *   score desc 10                 #take the top 10 by score
   *   weight bottom 10              #sort by weight ascending and take the first 10
   *   weight desc                   #sort by weight descending
   *   height desc,weight desc       #sort by height descending, and use weight descending to break any ties
   *   height desc,weight asc top 20 #sort by height descending, using weight ascending as a tiebreaker
   * </pre>
   *
   */
  public static SortSpec parseSort(String sortSpec, IndexSchema schema) {
    if (sortSpec==null || sortSpec.length()==0) return null;

    // I wonder how fast the regex is??? as least we cache the pattern.
    String[] parts = sortSeparator.split(sortSpec.trim(),0);
    if (parts.length == 0) return null;

    ArrayList<SortField> lst = new ArrayList<SortField>();
    int num=-1;

    int pos=0;
    String fn;
    boolean top=true;
    boolean normalSortOnScore=false;

    while (pos < parts.length) {
      String str=parts[pos];
      if ("top".equals(str) || "bottom".equals(str) || "asc".equals(str) || "desc".equals(str)) {
        // if the field name seems to be missing, default to "score".
        // note that this will mess up a field name that has the same name
        // as a sort direction specifier.
        fn="score";
      } else {
        fn=str;
        pos++;
      }

      // get the direction of the sort
      str=parts[pos];
      if ("top".equals(str) || "desc".equals(str)) {
        top=true;
      } else if ("bottom".equals(str) || "asc".equals(str)) {
        top=false;
      }  else {
        return null;  // must not be a sort command
      }

      // get the field to sort on
      // hmmm - should there be a fake/pseudo-field named "score" in the schema?
      if ("score".equals(fn)) {
        if (top) {
          normalSortOnScore=true;
          lst.add(SortField.FIELD_SCORE);
        } else {
          lst.add(new SortField(null, SortField.SCORE, true));
        }
      } else {
        // getField could throw an exception if the name isn't found
        try {
          SchemaField f = schema.getField(fn);
          if (f == null || !f.indexed()) return null;
          lst.add(f.getType().getSortField(f,top));
        } catch (Exception e) {
          return null;
        }
      }
      pos++;

      // If there is a leftover part, assume it is a count
      if (pos+1 == parts.length) {
        try {
          num = Integer.parseInt(parts[pos]);
        } catch (Exception e) {
          return null;
        }
        pos++;
      }
    }

    Sort sort;
    if (normalSortOnScore && lst.size() == 1) {
      // Normalize the default sort on score descending to sort=null
      sort=null;
    } else {
      sort = new Sort((SortField[]) lst.toArray(new SortField[lst.size()]));
    }
    return new SortSpec(sort,num);
  }


  ///////////////////////////
  ///////////////////////////
  ///////////////////////////

  static FieldType writeFieldName(String name, IndexSchema schema, Appendable out, int flags) throws IOException {
    FieldType ft = null;
    ft = schema.getFieldTypeNoEx(name);
    out.append(name);
    if (ft==null) {
      out.append("(UNKNOWN FIELD "+name+')');
    }
    out.append(':');
    return ft;
  }

  static void writeFieldVal(String val, FieldType ft, Appendable out, int flags) throws IOException {
    if (ft!=null) {
      out.append(ft.toExternal(new Field("",val, Field.Store.YES, Field.Index.UN_TOKENIZED)));
    } else {
      out.append(val);
    }
  }
  /** @see #toString(Query,IndexSchema) */
  public static void toString(Query query, IndexSchema schema, Appendable out, int flags) throws IOException {
    boolean writeBoost=true;

    if (query instanceof TermQuery) {
      TermQuery q = (TermQuery)query;
      Term t = q.getTerm();
      FieldType ft = writeFieldName(t.field(), schema, out, flags);
      writeFieldVal(t.text(), ft, out, flags);
    } else if (query instanceof RangeQuery) {
      RangeQuery q = (RangeQuery)query;
      String fname = q.getField();
      FieldType ft = writeFieldName(fname, schema, out, flags);
      out.append( q.isInclusive() ? '[' : '{' );
      Term lt = q.getLowerTerm();
      Term ut = q.getUpperTerm();
      if (lt==null) {
        out.append('*');
      } else {
        writeFieldVal(lt.text(), ft, out, flags);
      }

      out.append(" TO ");

      if (ut==null) {
        out.append('*');
      } else {
        writeFieldVal(ut.text(), ft, out, flags);
      }

      out.append( q.isInclusive() ? ']' : '}' );

    } else if (query instanceof ConstantScoreRangeQuery) {
      ConstantScoreRangeQuery q = (ConstantScoreRangeQuery)query;
      String fname = q.getField();
      FieldType ft = writeFieldName(fname, schema, out, flags);
      out.append( q.includesLower() ? '[' : '{' );
      String lt = q.getLowerVal();
      String ut = q.getUpperVal();
      if (lt==null) {
        out.append('*');
      } else {
        writeFieldVal(lt, ft, out, flags);
      }

      out.append(" TO ");

      if (ut==null) {
        out.append('*');
      } else {
        writeFieldVal(ut, ft, out, flags);
      }

      out.append( q.includesUpper() ? ']' : '}' );
    } else if (query instanceof BooleanQuery) {
      BooleanQuery q = (BooleanQuery)query;
      boolean needParens=false;

      if (q.getBoost() != 1.0 || q.getMinimumNumberShouldMatch() != 0) {
        needParens=true;
      }
      if (needParens) {
        out.append('(');
      }
      BooleanClause[] clauses = q.getClauses();
      boolean first=true;
      for (BooleanClause c : clauses) {
        if (!first) {
          out.append(' ');
        } else {
          first=false;
        }

        if (c.isProhibited()) {
          out.append('-');
        } else if (c.isRequired()) {
          out.append('+');
        }
        Query subQuery = c.getQuery();
        boolean wrapQuery=false;

        // TODO: may need to put parens around other types
        // of queries too, depending on future syntax.
        if (subQuery instanceof BooleanQuery) {
          wrapQuery=true;
        }

        if (wrapQuery) {
          out.append('(');
        }

        toString(subQuery, schema, out, flags);

        if (wrapQuery) {
          out.append(')');
        }
      }

      if (needParens) {
        out.append(')');
      }
      if (q.getMinimumNumberShouldMatch()>0) {
        out.append('~');
        out.append(Integer.toString(q.getMinimumNumberShouldMatch()));
      }

    } else if (query instanceof PrefixQuery) {
      PrefixQuery q = (PrefixQuery)query;
      Term prefix = q.getPrefix();
      FieldType ft = writeFieldName(prefix.field(), schema, out, flags);
      out.append(prefix.text());
      out.append('*');
    } else if (query instanceof ConstantScorePrefixQuery) {
      ConstantScorePrefixQuery q = (ConstantScorePrefixQuery)query;
      Term prefix = q.getPrefix();
      FieldType ft = writeFieldName(prefix.field(), schema, out, flags);
      out.append(prefix.text());
      out.append('*');
    } else if (query instanceof WildcardQuery) {
      out.append(query.toString());
      writeBoost=false;
    } else if (query instanceof FuzzyQuery) {
      out.append(query.toString());
      writeBoost=false;      
    } else if (query instanceof ConstantScoreQuery) {
      out.append(query.toString());
      writeBoost=false;
    } else {
      out.append(query.getClass().getSimpleName()
              + '(' + query.toString() + ')' );
      writeBoost=false;
    }

    if (writeBoost && query.getBoost() != 1.0f) {
      out.append("^");
      out.append(Float.toString(query.getBoost()));
    }

  }
  
  /**
   * Formats a Query for debugging, using the IndexSchema to make 
   * complex field types readable.
   *
   * <p>
   * The benefit of using this method instead of calling 
   * <code>Query.toString</code> directly is that it knows about the data
   *  types of each field, so any field which is encoded in a particularly 
   * complex way is still readable.  The downside is thta it only knows 
   * about built in Query types, and will not be able to format custom 
   * Query classes.
   * </p>
   */
  public static String toString(Query query, IndexSchema schema) {
    try {
      StringBuilder sb = new StringBuilder();
      toString(query, schema, sb, 0);
      return sb.toString();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }




  // simple class to help with parsing a string
  private static class StrParser {
    String val;
    int pos;
    int end;

    StrParser(String val) {this.val = val; end=val.length(); }

    void eatws() {
      while (pos<end && Character.isWhitespace(val.charAt(pos))) pos++;
    }

    boolean opt(String s) {
      eatws();
      int slen=s.length();
      if (val.regionMatches(pos, s, 0, slen)) {
        pos+=slen;
        return true;
      }
      return false;
    }

    void expect(String s) throws ParseException {
      eatws();
      int slen=s.length();
      if (val.regionMatches(pos, s, 0, slen)) {
        pos+=slen;
      } else {
        throw new ParseException("Expected '"+s+"' at position " + pos + " in '"+val+"'");
      }
    }

    float getFloat() throws ParseException {
      eatws();
      char[] arr = new char[end-pos];
      int i;
      for (i=0; i<arr.length; i++) {
        char ch = val.charAt(pos);
        if ( (ch>='0' && ch<='9')
             || ch=='+' || ch=='-'
             || ch=='.' || ch=='e' || ch=='E'
        ) {
          pos++;
          arr[i]=ch;
        } else {
          break;
        }
      }

      return Float.parseFloat(new String(arr,0,i));
    }

    String getId() throws ParseException {
      eatws();
      int id_start=pos;
      while (pos<end && Character.isJavaIdentifierPart(val.charAt(pos))) pos++;
      return val.substring(id_start, pos);
    }

    char peek() {
      eatws();
      return pos<end ? val.charAt(pos) : 0;
    }

    public String toString() {
      return "'" + val + "'" + ", pos=" + pos;
    }

  }


  private static ValueSource parseValSource(StrParser sp, IndexSchema schema) throws ParseException {
    String id = sp.getId();
    if (sp.opt("(")) {
      // a function: could contain a fieldname or another function.
      ValueSource vs=null;
      if (id.equals("ord")) {
        String field = sp.getId();
        vs = new OrdFieldSource(field);
      } else if (id.equals("rord")) {
        String field = sp.getId();
        vs = new ReverseOrdFieldSource(field);
      } else if (id.equals("linear")) {
        ValueSource source = parseValSource(sp, schema);
        sp.expect(",");
        float slope = sp.getFloat();
        sp.expect(",");
        float intercept = sp.getFloat();
        vs = new LinearFloatFunction(source,slope,intercept);
      } else if (id.equals("max")) {
        ValueSource source = parseValSource(sp, schema);
        sp.expect(",");
        float val = sp.getFloat();
        vs = new MaxFloatFunction(source,val);
      } else if (id.equals("recip")) {
        ValueSource source = parseValSource(sp,schema);
        sp.expect(",");
        float m = sp.getFloat();
        sp.expect(",");
        float a = sp.getFloat();
        sp.expect(",");
        float b = sp.getFloat();
        vs = new ReciprocalFloatFunction(source,m,a,b);
      } else {
        throw new ParseException("Unknown function " + id + " in FunctionQuery(" + sp + ")");
      }
      sp.expect(")");
      return vs;
    }

    SchemaField f = schema.getField(id);
    return f.getType().getValueSource(f);
  }

  /** 
   * Parse a function, returning a FunctionQuery
   *
   * <p>
   * Syntax Examples....
   * </p>
   *
   * <pre>
   * // Numeric fields default to correct type
   * // (ie: IntFieldSource or FloatFieldSource)
   * // Others use implicit ord(...) to generate numeric field value
   * myfield
   *
   * // OrdFieldSource
   * ord(myfield)
   *
   * // ReverseOrdFieldSource
   * rord(myfield)
   *
   * // LinearFloatFunction on numeric field value
   * linear(myfield,1,2)
   *
   * // MaxFloatFunction of LinearFloatFunction on numeric field value or constant
   * max(linear(myfield,1,2),100)
   *
   * // ReciprocalFloatFunction on numeric field value
   * recip(myfield,1,2,3)
   *
   * // ReciprocalFloatFunction on ReverseOrdFieldSource
   * recip(rord(myfield),1,2,3)
   *
   * // ReciprocalFloatFunction on LinearFloatFunction on ReverseOrdFieldSource
   * recip(linear(rord(myfield),1,2),3,4,5)
   * </pre>
   */
  public static FunctionQuery parseFunction(String func, IndexSchema schema) throws ParseException {
    return new FunctionQuery(parseValSource(new StrParser(func), schema));
  }

}
