/**
 *    Copyright (c) 2009, Adobe Systems, Incorporated
 *    All rights reserved.
 *
 *    Redistribution  and  use  in  source  and  binary  forms, with or without
 *    modification,  are  permitted  provided  that  the  following  conditions
 *    are met:
 *
 *      * Redistributions  of  source  code  must  retain  the  above copyright
 *        notice, this list of conditions and the following disclaimer.
 *      * Redistributions  in  binary  form  must reproduce the above copyright
 *        notice,  this  list  of  conditions  and  the following disclaimer in
 *        the    documentation   and/or   other  materials  provided  with  the
 *        distribution.
 *      * Neither the name of the Adobe Systems, Incorporated. nor the names of
 *        its  contributors  may be used to endorse or promote products derived
 *        from this software without specific prior written permission.
 *
 *    THIS  SOFTWARE  IS  PROVIDED  BY THE  COPYRIGHT  HOLDERS AND CONTRIBUTORS
 *    "AS IS"  AND  ANY  EXPRESS  OR  IMPLIED  WARRANTIES,  INCLUDING,  BUT NOT
 *    LIMITED  TO,  THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 *    PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER
 *    OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,  INCIDENTAL,  SPECIAL,
 *    EXEMPLARY,  OR  CONSEQUENTIAL  DAMAGES  (INCLUDING,  BUT  NOT  LIMITED TO,
 *    PROCUREMENT  OF  SUBSTITUTE   GOODS  OR   SERVICES;  LOSS  OF  USE,  DATA,
 *    OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *    LIABILITY,  WHETHER  IN  CONTRACT,  STRICT  LIABILITY, OR TORT (INCLUDING
 *    NEGLIGENCE  OR  OTHERWISE)  ARISING  IN  ANY  WAY  OUT OF THE USE OF THIS
 *    SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.bokelberg.flex.parser;

import org.junit.Test;

import com.adobe.ac.pmd.parser.exceptions.TokenException;

public class TestUnaryExpression extends AbstractStatementTest
{
   @Test
   public void testArrayAccess() throws TokenException
   {
      assertStatement( "1",
                       "x[0]",
                       "<arr-acc line=\"1\"><primary line=\"1\">x<"
                             + "/primary><primary line=\"1\">0</primary></arr-acc>" );
   }

   @Test
   public void testComplex() throws TokenException
   {
      assertStatement( "1",
                       "a.b['c'].d.e(1)",
                       "<dot line=\"1\"><primary line=\"1\">a"
                             + "</primary><dot line=\"1\"><arr-acc line=\"1\">"
                             + "<primary line=\"1\">b</primary><primary line=\"1\">"
                             + "'c'</primary></arr-acc><dot line=\"1\"><primary line=\"1\">"
                             + "d</primary><call line=\"1\"><primary line=\"1\">e"
                             + "</primary><arguments line=\"1\"><primary line=\"1\">1"
                             + "</primary></arguments></call></dot></dot></dot>" );

      assertStatement( "2",
                       "a.b['c']['d'].e(1)",
                       "<dot line=\"1\"><primary line=\"1\">a"
                             + "</primary><dot line=\"1\"><arr-acc line=\"1\">"
                             + "<primary line=\"1\">b</primary><primary line=\"1\">"
                             + "'c'</primary><primary line=\"1\">'d'</primary>"
                             + "</arr-acc><call line=\"1\"><primary line=\"1\">"
                             + "e</primary><arguments line=\"1\"><primary line=\"1\">1"
                             + "</primary></arguments></call></dot></dot>" );
   }

   @Test
   public void testMethodCall() throws TokenException
   {
      assertStatement( "1",
                       "method()",
                       "<call line=\"1\"><primary line=\"1\">"
                             + "method</primary><arguments line=\"1\"></arguments></call>" );

      assertStatement( "2",
                       "method( 1, \"two\" )",
                       "<call line=\"1\"><primary line=\"1\">"
                             + "method</primary><arguments line=\"1\"><primary line=\"1\">1"
                             + "</primary><primary line=\"1\">\"two\"</primary></arguments></call>" );
   }

   @Test
   public void testMultipleMethodCall() throws TokenException
   {
      assertStatement( "1",
                       "method()()",
                       "<call line=\"1\"><primary line=\"1\">"
                             + "method</primary><arguments line=\"1\"></arguments>"
                             + "<arguments line=\"1\"></arguments></call>" );
   }

   @Test
   public void testParseUnaryExpressions() throws TokenException
   {
      assertStatement( "1",
                       "++x",
                       "<pre-inc line=\"1\"><primary line=\"1\">x</primary></pre-inc>" );
      assertStatement( "2",
                       "x++",
                       "<post-inc line=\"2\"><primary line=\"1\">x</primary></post-inc>" );
      assertStatement( "3",
                       "--x",
                       "<pre-dec line=\"1\"><primary line=\"1\">x</primary></pre-dec>" );
      assertStatement( "4",
                       "x--",
                       "<post-dec line=\"2\"><primary line=\"1\">x</primary></post-dec>" );
      assertStatement( "5",
                       "+x",
                       "<plus line=\"1\"><primary line=\"1\">x</primary></plus>" );
      assertStatement( "6",
                       "+ x",
                       "<plus line=\"1\"><primary line=\"1\">x</primary></plus>" );
      assertStatement( "7",
                       "-x",
                       "<minus line=\"1\"><primary line=\"1\">x</primary></minus>" );
      assertStatement( "8",
                       "- x",
                       "<minus line=\"1\"><primary line=\"1\">x</primary></minus>" );
      assertStatement( "9",
                       "delete x",
                       "<delete line=\"1\"><primary line=\"1\">x</primary></delete>" );
      assertStatement( "a",
                       "void x",
                       "<void line=\"1\"><primary line=\"1\">x</primary></void>" );
      assertStatement( "b",
                       "typeof x",
                       "<typeof line=\"1\"><primary line=\"1\">x</primary></typeof>" );
      assertStatement( "c",
                       "! x",
                       "<not line=\"1\"><primary line=\"1\">x</primary></not>" );
      assertStatement( "d",
                       "~ x",
                       "<b-not line=\"1\"><primary line=\"1\">x</primary></b-not>" );
      assertStatement( "e",
                       "x++",
                       "<post-inc line=\"2\"><primary line=\"1\">x</primary></post-inc>" );
      assertStatement( "f",
                       "x--",
                       "<post-dec line=\"2\"><primary line=\"1\">x</primary></post-dec>" );
   }
}
