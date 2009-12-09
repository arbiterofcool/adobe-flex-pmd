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
package com.adobe.ac.pmd.metrics.engine;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.adobe.ac.ncss.filters.FlexFilter;
import com.adobe.ac.ncss.utils.FileUtils;
import com.adobe.ac.pmd.metrics.ClassMetrics;
import com.adobe.ac.pmd.metrics.FunctionMetrics;
import com.adobe.ac.pmd.metrics.PackageMetrics;
import com.adobe.ac.pmd.metrics.ProjectMetrics;

public abstract class AbstractMetrics
{
   private static class DirectoryFilter implements FileFilter
   {
      public boolean accept( final File file )
      {
         return file.isDirectory();
      }
   }

   private static Collection< File > listFiles( final File directory,
                                                final FilenameFilter filter,
                                                final boolean recurse )
   {
      final Collection< File > files = new ArrayList< File >();
      final File[] entries = directory.listFiles();
      for ( final File entry : entries )
      {
         if ( filter == null
               || filter.accept( directory,
                                 entry.getName() ) )
         {
            files.add( entry );
         }
         if ( recurse
               && entry.isDirectory() )
         {
            files.addAll( listFiles( entry,
                                     filter,
                                     recurse ) );
         }
      }
      return files;
   }

   private static Collection< File > listNonEmptyDirectories( final File directory,
                                                              final boolean recurse )
   {
      final Collection< File > files = new ArrayList< File >();
      final File[] entries = directory.listFiles( new DirectoryFilter() );
      final FlexFilter flexFilter = new FlexFilter();

      for ( final File entry : entries )
      {
         if ( entry.isDirectory()
               && !listFiles( entry,
                              flexFilter,
                              false ).isEmpty() )
         {
            files.add( entry );
         }
         if ( recurse
               && entry.isDirectory() )
         {
            files.addAll( listNonEmptyDirectories( entry,
                                                   recurse ) );
         }
      }
      return files;
   }

   protected Collection< File > filePaths           = null;
   protected Collection< File > nonEmptyDirectories = null;
   protected File               sourceDirectory     = null;

   public AbstractMetrics( final File sourceDirectoryPath )
   {
      super();
      if ( sourceDirectoryPath != null )
      {
         this.filePaths = FileUtils.listFiles( sourceDirectoryPath,
                                               new FlexFilter(),
                                               true );
         this.nonEmptyDirectories = listNonEmptyDirectories( sourceDirectoryPath,
                                                             true );
         this.sourceDirectory = sourceDirectoryPath;
      }
   }

   public void execute( final File outputFile ) throws DocumentException,
                                               IOException
   {
      final String builtReport = buildReport( loadMetrics() );
      final Document document = DocumentHelper.parseText( builtReport );
      final OutputFormat format = OutputFormat.createPrettyPrint();

      if ( !outputFile.exists() )
      {
         outputFile.createNewFile();
      }

      final XMLWriter writer = new XMLWriter( new FileOutputStream( outputFile ), format );
      writer.write( document );
      writer.close();
   }

   public abstract ProjectMetrics loadMetrics();

   private String addFunctions( final ProjectMetrics metrics )
   {
      final StringBuffer buffer = new StringBuffer( 25 );

      buffer.append( "<functions>" );

      for ( final FunctionMetrics functionMetrics : metrics.getFunctions() )
      {
         buffer.append( functionMetrics.toXmlString() );
      }

      buffer.append( MessageFormat.format( "<function_averages>"
                                                 + "<ncss>{0}</ncss>"
                                                 + "<javadocs/><javadoc_lines/><single_comment_lines/><multi_comment_lines/>"
                                                 + "</function_averages><ncss>{1}</ncss>" + "</functions>",
                                           String.valueOf( metrics.getAverageFunctions()
                                                                  .getAverageStatements() ),
                                           String.valueOf( metrics.getTotalPackages().getTotalStatements() ) ) );

      return buffer.toString();
   }

   private String addObjects( final ProjectMetrics metrics )
   {
      final StringBuffer buffer = new StringBuffer( 20 );

      buffer.append( "<objects>" );

      for ( final ClassMetrics classMetrics : metrics.getClasses() )
      {
         buffer.append( classMetrics.toXmlString() );
      }

      buffer.append( MessageFormat.format( "<averages>"
                                                 + "<classes>{0}</classes>"
                                                 + "<functions>{1}</functions>"
                                                 + "<ncss>{2}</ncss>"
                                                 + "<javadocs/><javadoc_lines/><single_comment_lines/><multi_comment_lines/>"
                                                 + "</averages></objects>",
                                           String.valueOf( metrics.getClasses().size() ),
                                           String.valueOf( metrics.getAverageObjects().getAverageFunctions() ),
                                           String.valueOf( metrics.getAverageObjects().getAverageStatements() ) ) );
      return buffer.toString();
   }

   private String addPackages( final ProjectMetrics metrics )
   {
      final StringBuffer buffer = new StringBuffer( 228 );

      buffer.append( "<packages>" );

      for ( final PackageMetrics packageMetrics : metrics.getPackages() )
      {
         buffer.append( packageMetrics.toXmlString() );
      }

      buffer.append( MessageFormat.format( "<total>"
                                                 + "<classes>{0}</classes>"
                                                 + "<functions>{1}</functions>"
                                                 + "<ncss>{2}</ncss>"
                                                 + "<javadocs/><javadoc_lines/><single_comment_lines/><multi_comment_lines/>"
                                                 + "</total></packages>",
                                           String.valueOf( metrics.getTotalPackages().getTotalClasses() ),
                                           String.valueOf( metrics.getTotalPackages().getTotalFunctions() ),
                                           String.valueOf( metrics.getTotalPackages().getTotalStatements() ) ) );

      return buffer.toString();
   }

   private String buildReport( final ProjectMetrics metrics )
   {
      final StringBuffer buf = new StringBuffer( 70 );

      buf.append( "<?xml version=\"1.0\"?><javancss><date>"
            + metrics.getDate() + "</date><time>" + metrics.getTime() + "</time>" );

      buf.append( addPackages( metrics ) );
      buf.append( addObjects( metrics ) );
      buf.append( addFunctions( metrics ) );

      buf.append( "</javancss>" );

      return buf.toString();
   }
}