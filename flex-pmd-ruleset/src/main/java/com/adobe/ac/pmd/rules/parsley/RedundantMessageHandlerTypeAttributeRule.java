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
package com.adobe.ac.pmd.rules.parsley;

import java.util.List;

import com.adobe.ac.pmd.nodes.IFunction;
import com.adobe.ac.pmd.nodes.IIdentifierNode;
import com.adobe.ac.pmd.nodes.IMetaData;
import com.adobe.ac.pmd.nodes.IParameter;
import com.adobe.ac.pmd.rules.core.AbstractFlexMetaDataRule;
import com.adobe.ac.pmd.rules.core.ViolationPriority;

public class RedundantMessageHandlerTypeAttributeRule extends AbstractFlexMetaDataRule
{
   @Override
   protected void findViolationsFromFunctionMetaData( final IFunction function )
   {
      final List< IParameter > params = function.getParameters();

      if ( params.size() == 1 )
      {
         final IIdentifierNode parameterType = params.get( 0 ).getType();
         findMessageHandlersContainingType( function,
                                            parameterType );
      }
   }

   @Override
   protected ViolationPriority getDefaultPriority()
   {
      return ViolationPriority.NORMAL;
   }

   private void findMessageHandlersContainingType( final IFunction function,
                                                   final IIdentifierNode type )
   {
      final List< IMetaData > handlers = ParsleyMetaData.MESSAGE_HANDLER.getMetaDataList( function );

      for ( final IMetaData handler : handlers )
      {
         final String parameterType = type.toString();
         final String metaDataType = getMessageHandlerType( handler );

         if ( metaDataType != null
               && metaDataType.equals( parameterType ) )
         {
            addViolation( handler );
         }
      }
   }

   private String getMessageHandlerType( final IMetaData handler )
   {
      String type = MetaDataUtil.getPropertyString( handler,
                                                    "type" );

      if ( type != null
            && type.contains( "." ) )
      {
         final int index = type.lastIndexOf( '.' ) + 1;

         if ( index < type.length() )
         {
            type = type.substring( index );
         }
      }

      return type;
   }
}