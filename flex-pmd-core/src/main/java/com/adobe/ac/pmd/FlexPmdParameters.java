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
package com.adobe.ac.pmd;

import java.io.File;

public class FlexPmdParameters
{
   private final String  excludePackage;
   private final boolean failOnError;
   private final File    outputDirectory;
   private final File    ruleSet;
   private final File    source;

   public FlexPmdParameters( final String excludePackageToBeSet,
                             final boolean failOnErrorToBeSet,
                             final File outputDirectoryToBeSet,
                             final File ruleSetToBeSet,
                             final File sourceToBeSet )
   {
      super();
      excludePackage = excludePackageToBeSet;
      failOnError = failOnErrorToBeSet;
      outputDirectory = outputDirectoryToBeSet;
      ruleSet = ruleSetToBeSet;
      source = sourceToBeSet;
   }

   public FlexPmdParameters( final String excludePackageToBeSet,
                             final File outputDirectoryToBeSet,
                             final File ruleSetToBeSet,
                             final File sourceToBeSet )
   {
      this( excludePackageToBeSet, false, outputDirectoryToBeSet, ruleSetToBeSet, sourceToBeSet );
   }

   public final String getExcludePackage()
   {
      return excludePackage;
   }

   public final File getOutputDirectory()
   {
      return outputDirectory;
   }

   public final File getRuleSet()
   {
      return ruleSet;
   }

   public final File getSource()
   {
      return source;
   }

   public final boolean isFailOnError()
   {
      return failOnError;
   }
}
