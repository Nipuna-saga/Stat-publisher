/* Generated By:JavaCC: Do not edit this line. SelectorParser.java */
/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package org.wso2.andes.server.filter.jms.selector;

import java.io.StringReader;
import java.util.ArrayList;

import org.wso2.andes.AMQInvalidArgumentException;
import org.wso2.andes.server.filter.ArithmeticExpression;
import org.wso2.andes.server.filter.BooleanExpression;
import org.wso2.andes.server.filter.ComparisonExpression;
import org.wso2.andes.server.filter.ConstantExpression;
import org.wso2.andes.server.filter.Expression;
import org.wso2.andes.server.filter.LogicExpression;
import org.wso2.andes.server.filter.PropertyExpression;
import org.wso2.andes.server.filter.UnaryExpression;

/**
 * JMS Selector Parser generated by JavaCC
 *
 * Do not edit this .java file directly - it is autogenerated from SelectorParser.jj
 */
public class SelectorParser implements SelectorParserConstants {

    public SelectorParser() {
        this(new StringReader(""));
    }

    public BooleanExpression parse(String sql) throws AMQInvalidArgumentException {
        this.ReInit(new StringReader(sql));

        try {
            return this.JmsSelector();
        }
        catch (Throwable e) {
                throw (AMQInvalidArgumentException)new AMQInvalidArgumentException(sql,e);
        }

    }

    private BooleanExpression asBooleanExpression(Expression value) throws ParseException  {
        if (value instanceof BooleanExpression) {
            return (BooleanExpression) value;
        }
        if (value instanceof PropertyExpression) {
            return UnaryExpression.createBooleanCast( value );
        }
        throw new ParseException("Expression will not result in a boolean value: " + value);
    }

// ----------------------------------------------------------------------------
// Grammer
// ----------------------------------------------------------------------------
  final public BooleanExpression JmsSelector() throws ParseException {
    Expression left=null;
    left = orExpression();
        {if (true) return asBooleanExpression(left);}
    throw new Error("Missing return statement in function");
  }

  final public Expression orExpression() throws ParseException {
    Expression left;
    Expression right;
    left = andExpression();
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case OR:
        ;
        break;
      default:
        break label_1;
      }
      jj_consume_token(OR);
      right = andExpression();
                left = LogicExpression.createOR(asBooleanExpression(left), asBooleanExpression(right));
    }
        {if (true) return left;}
    throw new Error("Missing return statement in function");
  }

  final public Expression andExpression() throws ParseException {
    Expression left;
    Expression right;
    left = equalityExpression();
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case AND:
        ;
        break;
      default:
        break label_2;
      }
      jj_consume_token(AND);
      right = equalityExpression();
                left = LogicExpression.createAND(asBooleanExpression(left), asBooleanExpression(right));
    }
        {if (true) return left;}
    throw new Error("Missing return statement in function");
  }

  final public Expression equalityExpression() throws ParseException {
    Expression left;
    Expression right;
    left = comparisonExpression();
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case IS:
      case 29:
      case 30:
        ;
        break;
      default:
        break label_3;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 29:
        jj_consume_token(29);
        right = comparisonExpression();
                left = ComparisonExpression.createEqual(left, right);
        break;
      case 30:
        jj_consume_token(30);
        right = comparisonExpression();
                left = ComparisonExpression.createNotEqual(left, right);
        break;
      default:
        if (jj_2_1(2)) {
          jj_consume_token(IS);
          jj_consume_token(NULL);
                left = ComparisonExpression.createIsNull(left);
        } else {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case IS:
            jj_consume_token(IS);
            jj_consume_token(NOT);
            jj_consume_token(NULL);
                left = ComparisonExpression.createIsNotNull(left);
            break;
          default:
            jj_consume_token(-1);
            throw new ParseException();
          }
        }
      }
    }
        {if (true) return left;}
    throw new Error("Missing return statement in function");
  }

  final public Expression comparisonExpression() throws ParseException {
    Expression left;
    Expression right;
    Expression low;
    Expression high;
    String t, u;
        boolean not;
        ArrayList list;
    left = addExpression();
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NOT:
      case BETWEEN:
      case LIKE:
      case IN:
      case 31:
      case 32:
      case 33:
      case 34:
        ;
        break;
      default:
        break label_4;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 31:
        jj_consume_token(31);
        right = addExpression();
                    left = ComparisonExpression.createGreaterThan(left, right);
        break;
      case 32:
        jj_consume_token(32);
        right = addExpression();
                    left = ComparisonExpression.createGreaterThanEqual(left, right);
        break;
      case 33:
        jj_consume_token(33);
        right = addExpression();
                    left = ComparisonExpression.createLessThan(left, right);
        break;
      case 34:
        jj_consume_token(34);
        right = addExpression();
                    left = ComparisonExpression.createLessThanEqual(left, right);
        break;
      case LIKE:
                                        u=null;
        jj_consume_token(LIKE);
        t = stringLitteral();
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case ESCAPE:
          jj_consume_token(ESCAPE);
          u = stringLitteral();
          break;
        default:
          ;
        }
                    left = ComparisonExpression.createLike(left, t, u);
        break;
      default:
        if (jj_2_2(2)) {
                                        u=null;
          jj_consume_token(NOT);
          jj_consume_token(LIKE);
          t = stringLitteral();
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case ESCAPE:
            jj_consume_token(ESCAPE);
            u = stringLitteral();
            break;
          default:
            ;
          }
                    left = ComparisonExpression.createNotLike(left, t, u);
        } else {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case BETWEEN:
            jj_consume_token(BETWEEN);
            low = addExpression();
            jj_consume_token(AND);
            high = addExpression();
                                        left = ComparisonExpression.createBetween(left, low, high);
            break;
          default:
            if (jj_2_3(2)) {
              jj_consume_token(NOT);
              jj_consume_token(BETWEEN);
              low = addExpression();
              jj_consume_token(AND);
              high = addExpression();
                                        left = ComparisonExpression.createNotBetween(left, low, high);
            } else {
              switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
              case IN:
                jj_consume_token(IN);
                jj_consume_token(35);
                t = stringLitteral();
                                    list = new ArrayList();
                                    list.add( t );
                label_5:
                while (true) {
                  switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
                  case 36:
                    ;
                    break;
                  default:
                    break label_5;
                  }
                  jj_consume_token(36);
                  t = stringLitteral();
                                            list.add( t );
                }
                jj_consume_token(37);
                           left = ComparisonExpression.createInFilter(left, list);
                break;
              default:
                if (jj_2_4(2)) {
                  jj_consume_token(NOT);
                  jj_consume_token(IN);
                  jj_consume_token(35);
                  t = stringLitteral();
                                    list = new ArrayList();
                                    list.add( t );
                  label_6:
                  while (true) {
                    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
                    case 36:
                      ;
                      break;
                    default:
                      break label_6;
                    }
                    jj_consume_token(36);
                    t = stringLitteral();
                                            list.add( t );
                  }
                  jj_consume_token(37);
                           left = ComparisonExpression.createNotInFilter(left, list);
                } else {
                  jj_consume_token(-1);
                  throw new ParseException();
                }
              }
            }
          }
        }
      }
    }
        {if (true) return left;}
    throw new Error("Missing return statement in function");
  }

  final public Expression addExpression() throws ParseException {
    Expression left;
    Expression right;
    left = multExpr();
    label_7:
    while (true) {
      if (jj_2_5(2147483647)) {
        ;
      } else {
        break label_7;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 38:
        jj_consume_token(38);
        right = multExpr();
                    left = ArithmeticExpression.createPlus(left, right);
        break;
      case 39:
        jj_consume_token(39);
        right = multExpr();
                    left = ArithmeticExpression.createMinus(left, right);
        break;
      default:
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
        {if (true) return left;}
    throw new Error("Missing return statement in function");
  }

  final public Expression multExpr() throws ParseException {
    Expression left;
    Expression right;
    left = unaryExpr();
    label_8:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 40:
      case 41:
      case 42:
        ;
        break;
      default:
        break label_8;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 40:
        jj_consume_token(40);
        right = unaryExpr();
                left = ArithmeticExpression.createMultiply(left, right);
        break;
      case 41:
        jj_consume_token(41);
        right = unaryExpr();
                left = ArithmeticExpression.createDivide(left, right);
        break;
      case 42:
        jj_consume_token(42);
        right = unaryExpr();
                left = ArithmeticExpression.createMod(left, right);
        break;
      default:
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
        {if (true) return left;}
    throw new Error("Missing return statement in function");
  }

  final public Expression unaryExpr() throws ParseException {
    String s=null;
    Expression left=null;
    if (jj_2_6(2147483647)) {
      jj_consume_token(38);
      left = unaryExpr();
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 39:
        jj_consume_token(39);
        left = unaryExpr();
                left = UnaryExpression.createNegate(left);
        break;
      case NOT:
        jj_consume_token(NOT);
        left = unaryExpr();
                    left = UnaryExpression.createNOT( asBooleanExpression(left) );
        break;
      case XPATH:
        jj_consume_token(XPATH);
        s = stringLitteral();
                    left = UnaryExpression.createXPath( s );
        break;
      case XQUERY:
        jj_consume_token(XQUERY);
        s = stringLitteral();
                    left = UnaryExpression.createXQuery( s );
        break;
      case TRUE:
      case FALSE:
      case NULL:
      case DECIMAL_LITERAL:
      case HEX_LITERAL:
      case OCTAL_LITERAL:
      case FLOATING_POINT_LITERAL:
      case STRING_LITERAL:
      case ID:
      case QUOTED_ID:
      case 35:
        left = primaryExpr();
        break;
      default:
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
        {if (true) return left;}
    throw new Error("Missing return statement in function");
  }

  final public Expression primaryExpr() throws ParseException {
    Expression left=null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case TRUE:
    case FALSE:
    case NULL:
    case DECIMAL_LITERAL:
    case HEX_LITERAL:
    case OCTAL_LITERAL:
    case FLOATING_POINT_LITERAL:
    case STRING_LITERAL:
      left = literal();
      break;
    case ID:
    case QUOTED_ID:
      left = variable();
      break;
    case 35:
      jj_consume_token(35);
      left = orExpression();
      jj_consume_token(37);
      break;
    default:
      jj_consume_token(-1);
      throw new ParseException();
    }
        {if (true) return left;}
    throw new Error("Missing return statement in function");
  }

  final public ConstantExpression literal() throws ParseException {
    Token t;
    String s;
    ConstantExpression left=null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case STRING_LITERAL:
      s = stringLitteral();
                left = new ConstantExpression(s);
      break;
    case DECIMAL_LITERAL:
      t = jj_consume_token(DECIMAL_LITERAL);
                left = ConstantExpression.createFromDecimal(t.image);
      break;
    case HEX_LITERAL:
      t = jj_consume_token(HEX_LITERAL);
                left = ConstantExpression.createFromHex(t.image);
      break;
    case OCTAL_LITERAL:
      t = jj_consume_token(OCTAL_LITERAL);
                left = ConstantExpression.createFromOctal(t.image);
      break;
    case FLOATING_POINT_LITERAL:
      t = jj_consume_token(FLOATING_POINT_LITERAL);
                left = ConstantExpression.createFloat(t.image);
      break;
    case TRUE:
      jj_consume_token(TRUE);
                left = ConstantExpression.TRUE;
      break;
    case FALSE:
      jj_consume_token(FALSE);
                left = ConstantExpression.FALSE;
      break;
    case NULL:
      jj_consume_token(NULL);
                left = ConstantExpression.NULL;
      break;
    default:
      jj_consume_token(-1);
      throw new ParseException();
    }
        {if (true) return left;}
    throw new Error("Missing return statement in function");
  }

  final public String stringLitteral() throws ParseException {
    Token t;
    StringBuffer rc = new StringBuffer();
    boolean first=true;
    t = jj_consume_token(STRING_LITERAL);
        // Decode the sting value.
        String image = t.image;
        for( int i=1; i < image.length()-1; i++ ) {
                char c = image.charAt(i);
                if( c == '\'' )
                        i++;
                        rc.append(c);
        }
            {if (true) return rc.toString();}
    throw new Error("Missing return statement in function");
  }

  final public PropertyExpression variable() throws ParseException {
    Token t;
    StringBuffer rc = new StringBuffer();
    PropertyExpression left=null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case ID:
      t = jj_consume_token(ID);
            left = new PropertyExpression(t.image);
      break;
    case QUOTED_ID:
      t = jj_consume_token(QUOTED_ID);
            // Decode the sting value.
            String image = t.image;
            for( int i=1; i < image.length()-1; i++ ) {
                char c = image.charAt(i);
                if( c == '"' )
                    i++;
                rc.append(c);
            }
            {if (true) return new PropertyExpression(rc.toString());}
      break;
    default:
      jj_consume_token(-1);
      throw new ParseException();
    }
        {if (true) return left;}
    throw new Error("Missing return statement in function");
  }

  final private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_2(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_2(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_3(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_3(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_4(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_4(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_5(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_5(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_6(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_6(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_3R_14() {
    if (jj_scan_token(NOT)) return true;
    if (jj_3R_10()) return true;
    return false;
  }

  final private boolean jj_3R_36() {
    if (jj_scan_token(NULL)) return true;
    return false;
  }

  final private boolean jj_3R_12() {
    if (jj_scan_token(38)) return true;
    if (jj_3R_10()) return true;
    return false;
  }

  final private boolean jj_3R_57() {
    if (jj_scan_token(IN)) return true;
    if (jj_scan_token(35)) return true;
    if (jj_3R_21()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_62()) { jj_scanpos = xsp; break; }
    }
    if (jj_scan_token(37)) return true;
    return false;
  }

  final private boolean jj_3R_49() {
    if (jj_scan_token(IS)) return true;
    if (jj_scan_token(NOT)) return true;
    if (jj_scan_token(NULL)) return true;
    return false;
  }

  final private boolean jj_3R_13() {
    if (jj_scan_token(39)) return true;
    if (jj_3R_10()) return true;
    return false;
  }

  final private boolean jj_3R_35() {
    if (jj_scan_token(FALSE)) return true;
    return false;
  }

  final private boolean jj_3_1() {
    if (jj_scan_token(IS)) return true;
    if (jj_scan_token(NULL)) return true;
    return false;
  }

  final private boolean jj_3R_10() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_12()) {
    jj_scanpos = xsp;
    if (jj_3R_13()) {
    jj_scanpos = xsp;
    if (jj_3R_14()) {
    jj_scanpos = xsp;
    if (jj_3R_15()) {
    jj_scanpos = xsp;
    if (jj_3R_16()) {
    jj_scanpos = xsp;
    if (jj_3R_17()) return true;
    }
    }
    }
    }
    }
    return false;
  }

  final private boolean jj_3R_34() {
    if (jj_scan_token(TRUE)) return true;
    return false;
  }

  final private boolean jj_3R_48() {
    if (jj_scan_token(30)) return true;
    if (jj_3R_43()) return true;
    return false;
  }

  final private boolean jj_3_3() {
    if (jj_scan_token(NOT)) return true;
    if (jj_scan_token(BETWEEN)) return true;
    if (jj_3R_45()) return true;
    if (jj_scan_token(AND)) return true;
    if (jj_3R_45()) return true;
    return false;
  }

  final private boolean jj_3R_47() {
    if (jj_scan_token(29)) return true;
    if (jj_3R_43()) return true;
    return false;
  }

  final private boolean jj_3R_44() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_47()) {
    jj_scanpos = xsp;
    if (jj_3R_48()) {
    jj_scanpos = xsp;
    if (jj_3_1()) {
    jj_scanpos = xsp;
    if (jj_3R_49()) return true;
    }
    }
    }
    return false;
  }

  final private boolean jj_3R_33() {
    if (jj_scan_token(FLOATING_POINT_LITERAL)) return true;
    return false;
  }

  final private boolean jj_3R_56() {
    if (jj_scan_token(BETWEEN)) return true;
    if (jj_3R_45()) return true;
    if (jj_scan_token(AND)) return true;
    if (jj_3R_45()) return true;
    return false;
  }

  final private boolean jj_3R_60() {
    if (jj_scan_token(ESCAPE)) return true;
    if (jj_3R_21()) return true;
    return false;
  }

  final private boolean jj_3R_32() {
    if (jj_scan_token(OCTAL_LITERAL)) return true;
    return false;
  }

  final private boolean jj_3R_20() {
    if (jj_scan_token(42)) return true;
    if (jj_3R_10()) return true;
    return false;
  }

  final private boolean jj_3R_41() {
    if (jj_3R_43()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_44()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3_2() {
    if (jj_scan_token(NOT)) return true;
    if (jj_scan_token(LIKE)) return true;
    if (jj_3R_21()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_61()) jj_scanpos = xsp;
    return false;
  }

  final private boolean jj_3R_55() {
    if (jj_scan_token(LIKE)) return true;
    if (jj_3R_21()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_60()) jj_scanpos = xsp;
    return false;
  }

  final private boolean jj_3R_31() {
    if (jj_scan_token(HEX_LITERAL)) return true;
    return false;
  }

  final private boolean jj_3R_19() {
    if (jj_scan_token(41)) return true;
    if (jj_3R_10()) return true;
    return false;
  }

  final private boolean jj_3R_18() {
    if (jj_scan_token(40)) return true;
    if (jj_3R_10()) return true;
    return false;
  }

  final private boolean jj_3R_11() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_18()) {
    jj_scanpos = xsp;
    if (jj_3R_19()) {
    jj_scanpos = xsp;
    if (jj_3R_20()) return true;
    }
    }
    return false;
  }

  final private boolean jj_3R_42() {
    if (jj_scan_token(AND)) return true;
    if (jj_3R_41()) return true;
    return false;
  }

  final private boolean jj_3R_30() {
    if (jj_scan_token(DECIMAL_LITERAL)) return true;
    return false;
  }

  final private boolean jj_3R_9() {
    if (jj_3R_10()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_11()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_38() {
    if (jj_scan_token(QUOTED_ID)) return true;
    return false;
  }

  final private boolean jj_3R_29() {
    if (jj_3R_21()) return true;
    return false;
  }

  final private boolean jj_3R_59() {
    if (jj_scan_token(39)) return true;
    if (jj_3R_9()) return true;
    return false;
  }

  final private boolean jj_3R_39() {
    if (jj_3R_41()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_42()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3_5() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(38)) {
    jj_scanpos = xsp;
    if (jj_scan_token(39)) return true;
    }
    if (jj_3R_9()) return true;
    return false;
  }

  final private boolean jj_3R_26() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_29()) {
    jj_scanpos = xsp;
    if (jj_3R_30()) {
    jj_scanpos = xsp;
    if (jj_3R_31()) {
    jj_scanpos = xsp;
    if (jj_3R_32()) {
    jj_scanpos = xsp;
    if (jj_3R_33()) {
    jj_scanpos = xsp;
    if (jj_3R_34()) {
    jj_scanpos = xsp;
    if (jj_3R_35()) {
    jj_scanpos = xsp;
    if (jj_3R_36()) return true;
    }
    }
    }
    }
    }
    }
    }
    return false;
  }

  final private boolean jj_3R_37() {
    if (jj_scan_token(ID)) return true;
    return false;
  }

  final private boolean jj_3R_54() {
    if (jj_scan_token(34)) return true;
    if (jj_3R_45()) return true;
    return false;
  }

  final private boolean jj_3R_58() {
    if (jj_scan_token(38)) return true;
    if (jj_3R_9()) return true;
    return false;
  }

  final private boolean jj_3R_27() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_37()) {
    jj_scanpos = xsp;
    if (jj_3R_38()) return true;
    }
    return false;
  }

  final private boolean jj_3R_53() {
    if (jj_scan_token(33)) return true;
    if (jj_3R_45()) return true;
    return false;
  }

  final private boolean jj_3R_63() {
    if (jj_scan_token(36)) return true;
    if (jj_3R_21()) return true;
    return false;
  }

  final private boolean jj_3R_50() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_58()) {
    jj_scanpos = xsp;
    if (jj_3R_59()) return true;
    }
    return false;
  }

  final private boolean jj_3R_40() {
    if (jj_scan_token(OR)) return true;
    if (jj_3R_39()) return true;
    return false;
  }

  final private boolean jj_3R_52() {
    if (jj_scan_token(32)) return true;
    if (jj_3R_45()) return true;
    return false;
  }

  final private boolean jj_3R_25() {
    if (jj_scan_token(35)) return true;
    if (jj_3R_28()) return true;
    if (jj_scan_token(37)) return true;
    return false;
  }

  final private boolean jj_3R_24() {
    if (jj_3R_27()) return true;
    return false;
  }

  final private boolean jj_3R_51() {
    if (jj_scan_token(31)) return true;
    if (jj_3R_45()) return true;
    return false;
  }

  final private boolean jj_3R_46() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_51()) {
    jj_scanpos = xsp;
    if (jj_3R_52()) {
    jj_scanpos = xsp;
    if (jj_3R_53()) {
    jj_scanpos = xsp;
    if (jj_3R_54()) {
    jj_scanpos = xsp;
    if (jj_3R_55()) {
    jj_scanpos = xsp;
    if (jj_3_2()) {
    jj_scanpos = xsp;
    if (jj_3R_56()) {
    jj_scanpos = xsp;
    if (jj_3_3()) {
    jj_scanpos = xsp;
    if (jj_3R_57()) {
    jj_scanpos = xsp;
    if (jj_3_4()) return true;
    }
    }
    }
    }
    }
    }
    }
    }
    }
    return false;
  }

  final private boolean jj_3R_23() {
    if (jj_3R_26()) return true;
    return false;
  }

  final private boolean jj_3R_45() {
    if (jj_3R_9()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_50()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_28() {
    if (jj_3R_39()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_40()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_22() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_23()) {
    jj_scanpos = xsp;
    if (jj_3R_24()) {
    jj_scanpos = xsp;
    if (jj_3R_25()) return true;
    }
    }
    return false;
  }

  final private boolean jj_3R_17() {
    if (jj_3R_22()) return true;
    return false;
  }

  final private boolean jj_3R_21() {
    if (jj_scan_token(STRING_LITERAL)) return true;
    return false;
  }

  final private boolean jj_3R_62() {
    if (jj_scan_token(36)) return true;
    if (jj_3R_21()) return true;
    return false;
  }

  final private boolean jj_3R_16() {
    if (jj_scan_token(XQUERY)) return true;
    if (jj_3R_21()) return true;
    return false;
  }

  final private boolean jj_3R_43() {
    if (jj_3R_45()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_46()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_61() {
    if (jj_scan_token(ESCAPE)) return true;
    if (jj_3R_21()) return true;
    return false;
  }

  final private boolean jj_3_4() {
    if (jj_scan_token(NOT)) return true;
    if (jj_scan_token(IN)) return true;
    if (jj_scan_token(35)) return true;
    if (jj_3R_21()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_63()) { jj_scanpos = xsp; break; }
    }
    if (jj_scan_token(37)) return true;
    return false;
  }

  final private boolean jj_3_6() {
    if (jj_scan_token(38)) return true;
    if (jj_3R_10()) return true;
    return false;
  }

  final private boolean jj_3R_15() {
    if (jj_scan_token(XPATH)) return true;
    if (jj_3R_21()) return true;
    return false;
  }

  public SelectorParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  public Token token, jj_nt;
  private int jj_ntk;
  private Token jj_scanpos, jj_lastpos;
  private int jj_la;
  public boolean lookingAhead = false;
  private boolean jj_semLA;

  public SelectorParser(java.io.InputStream stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new SelectorParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
  }

  public void ReInit(java.io.InputStream stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
  }

  public SelectorParser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new SelectorParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
  }

  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
  }

  public SelectorParser(SelectorParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
  }

  public void ReInit(SelectorParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
  }

  final private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      return token;
    }
    token = oldToken;
    throw generateParseException();
  }

  static private final class LookaheadSuccess extends java.lang.Error { }
  final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  final private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }

  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    return token;
  }

  final public Token getToken(int index) {
    Token t = lookingAhead ? jj_scanpos : token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  final private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  public ParseException generateParseException() {
    Token errortok = token.next;
    int line = errortok.beginLine, column = errortok.beginColumn;
    String mess = (errortok.kind == 0) ? tokenImage[0] : errortok.image;
    return new ParseException("Parse error at line " + line + ", column " + column + ".  Encountered: " + mess);
  }

  final public void enable_tracing() {
  }

  final public void disable_tracing() {
  }

}
