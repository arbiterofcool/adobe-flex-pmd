/**
 *    Copyright (c) 2008. Adobe Systems Incorporated.
 *    All rights reserved.
 *
 *    Redistribution and use in source and binary forms, with or without
 *    modification, are permitted provided that the following conditions
 *    are met:
 *
 *      * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in
 *        the documentation and/or other materials provided with the
 *        distribution.
 *      * Neither the name of Adobe Systems Incorporated nor the names of
 *        its contributors may be used to endorse or promote products derived
 *        from this software without specific prior written permission.
 *
 *    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *    "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *    LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 *    PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
 *    OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 *    EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *    PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 *    PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *    LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *    NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *    SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.adobe.ac.pmd.nodes.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import net.sourceforge.pmd.PMDException;

import org.junit.Before;
import org.junit.Test;

import com.adobe.ac.pmd.FlexPmdTestBase;
import com.adobe.ac.pmd.files.FileSetUtils;
import com.adobe.ac.pmd.nodes.IClass;
import com.adobe.ac.pmd.nodes.IFunction;
import com.adobe.ac.pmd.nodes.Modifier;
import com.adobe.ac.pmd.parser.IParserNode;
import com.adobe.ac.pmd.parser.exceptions.TokenException;

public class FunctionNodeTest extends FlexPmdTestBase
{
   private IFunction constructor;
   private IFunction drawHighlightIndicator;
   private IFunction drawRowBackground;
   private IFunction drawSelectionIndicator;
   private IFunction getHeight;
   private IFunction isTrueGetter;
   private IFunction isTrueSetter;
   private IFunction placeSortArrow;

   @Test
   public void modifiers()
   {
      assertTrue( constructor.contains( Modifier.PUBLIC ) );
   }

   @Before
   public void setup() throws IOException,
                      TokenException,
                      PMDException
   {
      final IParserNode dataGridAst = FileSetUtils.buildAst( testFiles.get( "RadonDataGrid.as" ) );
      final IParserNode modelLocatorAst = FileSetUtils.buildAst( testFiles.get( "cairngorm."
            + "NonBindableModelLocator.as" ) );
      final IClass radonDataGrid = NodeFactory.createPackage( dataGridAst ).getClassNode();
      final IClass nonBindableModelLocator = NodeFactory.createPackage( modelLocatorAst ).getClassNode();

      constructor = radonDataGrid.getFunctions().get( 0 );
      drawHighlightIndicator = radonDataGrid.getFunctions().get( 1 );
      drawSelectionIndicator = radonDataGrid.getFunctions().get( 2 );
      drawRowBackground = radonDataGrid.getFunctions().get( 3 );
      placeSortArrow = radonDataGrid.getFunctions().get( 4 );
      isTrueGetter = radonDataGrid.getFunctions().get( 5 );
      isTrueSetter = radonDataGrid.getFunctions().get( 6 );
      getHeight = nonBindableModelLocator.getFunctions().get( 2 );
   }

   @Test
   public void testFindPrimaryStatementFromName()
   {
      assertEquals( 0,
                    constructor.findPrimaryStatementsInBody( "" ).size() );
      assertEquals( 1,
                    drawHighlightIndicator.findPrimaryStatementInBody( new String[]
                    { "super",
                                "" } ).size() );
   }

   @Test
   public void testGetCyclomaticComplexity()
   {
      assertEquals( 2,
                    constructor.getCyclomaticComplexity() );
      assertEquals( 1,
                    drawHighlightIndicator.getCyclomaticComplexity() );
      assertEquals( 1,
                    drawSelectionIndicator.getCyclomaticComplexity() );
      assertEquals( 4,
                    drawRowBackground.getCyclomaticComplexity() );
      assertEquals( 13,
                    placeSortArrow.getCyclomaticComplexity() );
   }

   @Test
   public void testGetMetaData()
   {
      assertEquals( 1,
                    getHeight.getMetaDataCount() );
      assertEquals( 0,
                    isTrueGetter.getMetaDataCount() );
   }

   @Test
   public void testGetName()
   {
      assertEquals( "RadonDataGrid",
                    constructor.getName() );
      assertEquals( "drawHighlightIndicator",
                    drawHighlightIndicator.getName() );
      assertEquals( "drawSelectionIndicator",
                    drawSelectionIndicator.getName() );
      assertEquals( "drawRowBackground",
                    drawRowBackground.getName() );
      assertEquals( "placeSortArrow",
                    placeSortArrow.getName() );
   }

   @Test
   public void testGetParameters()
   {
      assertEquals( 0,
                    constructor.getParameters().size() );
      assertEquals( 7,
                    drawHighlightIndicator.getParameters().size() );
      assertEquals( 7,
                    drawSelectionIndicator.getParameters().size() );
      assertEquals( 6,
                    drawRowBackground.getParameters().size() );
      assertEquals( 0,
                    placeSortArrow.getParameters().size() );
   }

   @Test
   public void testGetReturnType()
   {
      assertEquals( "",
                    constructor.getReturnType().getInternalNode().getStringValue() );
      assertEquals( "void",
                    drawHighlightIndicator.getReturnType().getInternalNode().getStringValue() );
      assertEquals( "void",
                    drawSelectionIndicator.getReturnType().getInternalNode().getStringValue() );
      assertEquals( "void",
                    drawRowBackground.getReturnType().getInternalNode().getStringValue() );
      assertEquals( "void",
                    placeSortArrow.getReturnType().getInternalNode().getStringValue() );
   }

   @Test
   public void testIsGetter()
   {
      assertFalse( constructor.isGetter() );
      assertFalse( drawHighlightIndicator.isGetter() );
      assertFalse( isTrueSetter.isGetter() );
      assertTrue( isTrueGetter.isGetter() );
   }

   @Test
   public void testIsSetter()
   {
      assertFalse( constructor.isSetter() );
      assertFalse( drawHighlightIndicator.isSetter() );
      assertFalse( isTrueGetter.isSetter() );
      assertTrue( isTrueSetter.isSetter() );
   }

   @Test
   public void testLocalVariables()
   {
      assertEquals( 0,
                    constructor.getLocalVariables().size() );
      assertEquals( 2,
                    drawHighlightIndicator.getLocalVariables().size() );
      assertEquals( 13,
                    drawSelectionIndicator.getLocalVariables().size() );
      assertEquals( 5,
                    drawRowBackground.getLocalVariables().size() );
   }

   @Test
   public void testOverride()
   {
      assertTrue( drawHighlightIndicator.isOverriden() );
      assertFalse( isTrueGetter.isOverriden() );
   }

   @Test
   public void testSuperCall()
   {
      assertNotNull( constructor.getSuperCall() );
      assertNotNull( drawHighlightIndicator.getSuperCall() );
      assertNotNull( placeSortArrow.getSuperCall() );
      assertNull( drawRowBackground.getSuperCall() );
   }

   @Test
   public void testVisibility()
   {
      assertTrue( constructor.isPublic() );
      assertTrue( drawHighlightIndicator.isProtected() );
      assertTrue( drawSelectionIndicator.isProtected() );
      assertTrue( drawRowBackground.isProtected() );
      assertTrue( isTrueGetter.isPrivate() );
   }
}