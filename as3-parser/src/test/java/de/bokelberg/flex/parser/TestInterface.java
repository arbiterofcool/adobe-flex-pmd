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

public class TestInterface extends AbstractAs3ParserTest
{
   @Test
   public void testExtends() throws TokenException
   {
      assertPackageContent( "1",
                            "public interface A extends B { } ",
                            "<content line=\"2\"><interface line=\"2\">"
                                  + "<name line=\"2\">A</name><mod-list line=\"2\">"
                                  + "<mod line=\"2\">public</mod>"
                                  + "</mod-list><extends line=\"2\">B</extends>"
                                  + "<content line=\"2\"></content></interface></content>" );

      assertPackageContent( "",
                            "   public interface ITimelineEntryRenderer extends IFlexDisplayObject, IDataRenderer{}",
                            "<content line=\"2\"><interface line=\"2\"><name line=\"2\""
                                  + ">ITimelineEntryRenderer</name><mod-list line=\"2\">"
                                  + "<mod line=\"2\">public</mod></mod-list><extends line=\"2\""
                                  + ">IFlexDisplayObject</extends><extends line=\"2\">"
                                  + "IDataRenderer</extends><content line=\"2\">"
                                  + "</content></interface></content>" );
   }

   @Test
   public void testInclude() throws TokenException
   {
      assertPackageContent( "1",
                            "public interface A extends B { include \"ITextFieldInterface.asz\" } ",
                            "<content line=\"2\"><interface line=\"2\">"
                                  + "<name line=\"2\">A</name><mod-list line=\"2\">"
                                  + "<mod line=\"2\">public</mod></mod-list>" + "<extends line=\"2\">"
                                  + "B</extends><content line=\"2\"><include line=\"2\">"
                                  + "<primary line=\"2\">\"ITextFieldInterface.asz\"</primary>"
                                  + "</include></content></interface></content>" );
   }

   private void assertPackageContent( final String message,
                                      final String input,
                                      final String expected ) throws TokenException
   {
      scn.setLines( new String[]
      { "{",
                  input,
                  "}",
                  "__END__" } );
      asp.nextToken(); // first call
      asp.nextToken(); // skip {
      final String result = new ASTToXMLConverter().convert( asp.parsePackageContent() );
      assertEquals( message,
                    expected,
                    result );
   }

}
