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

package org.apache.solr.util;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

/**
 * @author yonik
 * @version $Id: StrUtils.java 472574 2006-11-08 18:25:52Z yonik $
 */
public class StrUtils {

  /**
   * Split a string based on a separator, but don't split if it's inside
   * a string.  Assume '\' escapes the next char both inside and
   * outside strings.
   */
  public static List<String> splitSmart(String s, char separator) {
    ArrayList<String> lst = new ArrayList<String>(4);
    int pos=0, start=0, end=s.length();
    char inString=0;
    while (pos < end) {
      char ch = s.charAt(pos++);
      if (ch=='\\') {    // skip escaped chars
        pos++;
      } else if (inString != 0 && ch==inString) {
        inString=0;
      } else if (ch=='\'' || ch=='"') {
        inString=ch;
      } else if (ch==separator && inString==0) {
        lst.add(s.substring(start,pos-1));
        start=pos;
      }
    }
    if (start < end) {
      lst.add(s.substring(start,end));
    }

    /***
    if (SolrCore.log.isLoggable(Level.FINEST)) {
      SolrCore.log.finest("splitCommand=" + lst);
    }
    ***/

    return lst;
  }

  /** Splits a backslash escaped string on the separator.
   * <p>
   * Current backslash escaping supported:
   * <br> \n \t \r \b \f are escaped the same as a Java String
   * <br> Other characters following a backslash are produced verbatim (\c => c)
   *
   * @param s  the string to split
   * @param separator the separator to split on
   * @param decode decode backslash escaping
   */
  public static List<String> splitSmart(String s, String separator, boolean decode) {
    ArrayList<String> lst = new ArrayList<String>(2);
    StringBuilder sb = new StringBuilder();
    int pos=0, end=s.length();
    while (pos < end) {
      if (s.startsWith(separator,pos)) {
        if (sb.length() > 0) {
          lst.add(sb.toString());
          sb=new StringBuilder();
        }
        pos+=separator.length();
        continue;
      }

      char ch = s.charAt(pos++);
      if (ch=='\\') {
        if (!decode) sb.append(ch);
        if (pos>=end) break;  // ERROR, or let it go?
        ch = s.charAt(pos++);
        if (decode) {
          switch(ch) {
            case 'n' : ch='\n';
            case 't' : ch='\t';
            case 'r' : ch='\r';
            case 'b' : ch='\b';
            case 'f' : ch='\f';
          }
        }
      }

      sb.append(ch);
    }

    if (sb.length() > 0) {
      lst.add(sb.toString());
    }

    return lst;
  }



  public static List<String> splitWS(String s, boolean decode) {
    ArrayList<String> lst = new ArrayList<String>(2);
    StringBuilder sb = new StringBuilder();
    int pos=0, end=s.length();
    while (pos < end) {
      char ch = s.charAt(pos++);
      if (Character.isWhitespace(ch)) {
        if (sb.length() > 0) {
          lst.add(sb.toString());
          sb=new StringBuilder();
        }
        continue;
      }

      if (ch=='\\') {
        if (!decode) sb.append(ch);
        if (pos>=end) break;  // ERROR, or let it go?
        ch = s.charAt(pos++);
        if (decode) {
          switch(ch) {
            case 'n' : ch='\n';
            case 't' : ch='\t';
            case 'r' : ch='\r';
            case 'b' : ch='\b';
            case 'f' : ch='\f';
          }
        }
      }

      sb.append(ch);
    }

    if (sb.length() > 0) {
      lst.add(sb.toString());
    }

    return lst;
  }

  public static List<String> toLower(List<String> strings) {
    ArrayList<String> ret = new ArrayList<String>(strings.size());
    for (String str : strings) {
      ret.add(str.toLowerCase());
    }
    return ret;
  }



  /** Return if a string starts with '1', 't', or 'T'
   *  and return false otherwise.
   */
  public static boolean parseBoolean(String s) {
    char ch = s.length()>0 ? s.charAt(0) : 0;
    return (ch=='1' || ch=='t' || ch=='T');
  }

  /**
   * URLEncodes a value, replacing only enough chars so that
   * the URL may be unambiguously pasted back into a browser.
   * <p>
   * Characters with a numeric value less than 32 are encoded.
   * &amp;,=,%,+,space are encoded.
   * <p>
   */
  public static void partialURLEncodeVal(Appendable dest, String val) throws IOException {
    for (int i=0; i<val.length(); i++) {
      char ch = val.charAt(i);
      if (ch < 32) {
        dest.append('%');
        // Hmmm, if we used StringBuilder rather than Appendable, it
        // could add an integer more efficiently.
        dest.append(Integer.toString(ch));
      } else {
        switch (ch) {
          case ' ': dest.append('+'); break;
          case '&': dest.append("%26"); break;
          case '%': dest.append("%25"); break;
          case '=': dest.append("%3D"); break;
          case '+': dest.append("%2B"); break;
          default : dest.append(ch); break;
        }
      }
    }
  }

}
