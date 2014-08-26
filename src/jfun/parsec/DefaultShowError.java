/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on Nov 18, 2004
 *
 * Author Ben Yu
 */
package jfun.parsec;
import java.util.Arrays;
import java.util.TreeSet;


/**
 * This class gives the default behavior of reporting parser errors. 
 * @author Ben Yu
 *
 * Nov 18, 2004
 */
final class DefaultShowError implements java.io.Serializable{

  /*
   * @see jfun.parsec.ShowError#show(jfun.parsec.ParseError)
   */
  static String show(final ParseError err, final Pos pos) {
    return toErrorStr(err, pos);
  }
  private static String toErrorStr(final ParseError err, final Pos pos){
    final StringBuffer buf = new StringBuffer();
    if(pos != null)
      buf.append("line " + pos.getLineNo() + ", column " + pos.getColumnNo());
    if(err!=null){
      buf.append(":\n");
      showExpecting(buf, err.getExpecting());
      showUnexpected(buf, err.getUnexpected());
      showMessages(buf, err.getMessages());
      showEncountered(buf, err.getEncountered());
    }
    return buf.toString();
  }
  private static void showEncountered(final StringBuffer buf, final String s){
    if(s==null) return;
    buf.append(s).append(" encountered.\n");
  }
  private static String[] unique(final String[] msgs){
    if(msgs.length <= 1) return msgs;
    final TreeSet set = new TreeSet(Arrays.asList(msgs));
    final String[] umsgs = new String[set.size()];
    set.toArray(umsgs);
    return umsgs;
  }
  private static void showList(final StringBuffer buf, final String[] msgs){
    if(msgs.length==0) return;
    for(int i=0; i<msgs.length-1; i++){
      buf.append(msgs[i]).append(' ');
    }
    if(msgs.length > 1) buf.append("or ");
    buf.append(msgs[msgs.length-1]);
  }
  private static void showExpecting(final StringBuffer buf, final String[] msgs) {
    if(msgs==null || msgs.length == 0) return;
    final String[] umsgs = unique(msgs);
    buf.append("expecting ");
    showList(buf, umsgs);
    buf.append(".\n");
  }
  private static void showUnexpected(final StringBuffer buf, final String[] msgs) {
    if(msgs==null || msgs.length == 0) return;
    showList(buf, unique(msgs));
    buf.append(" unexpected.\n");
  }
  private static void showMessages(final StringBuffer buf, final String[] msgs){
    if(msgs==null || msgs.length == 0) return;
    buf.append(msgs[0]);
    for(int i=1; i<msgs.length; i++){
      buf.append(" or \n").append(msgs[i]);
    }
    buf.append("\n");
  }
  private DefaultShowError(){}
}
