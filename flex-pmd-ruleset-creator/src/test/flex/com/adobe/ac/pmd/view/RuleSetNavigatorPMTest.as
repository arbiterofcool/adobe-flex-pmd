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
 package com.adobe.ac.pmd.view
{
   import com.adobe.ac.pmd.control.events.GetRootRulesetEvent;
   import com.adobe.ac.pmd.model.RootRuleset;
   import com.adobe.ac.pmd.model.Ruleset;
   import com.adobe.ac.pmd.model.events.RulesetReceivedEvent;

   import flexunit.framework.CairngormEventSource;
   import flexunit.framework.EventfulTestCase;

   import mx.collections.ArrayCollection;

   public class RuleSetNavigatorPMTest extends EventfulTestCase
   {
      private var model : RuleSetNavigatorPM;
      
      public function RuleSetNavigatorPMTest()
      {
      }

      override public function setUp() : void
      {
         model = new RuleSetNavigatorPM();
      }

      public function testGetRootRuleset() : void
      {
         listenForEvent( CairngormEventSource.instance, GetRootRulesetEvent.EVENT_NAME );

         model.getRootRuleset();

         assertEvents();
      }

      public function testOnReceiveRootRuleset() : void
      {
         var emptyRootRuleset : RootRuleset = new RootRuleset();

         listenForEvent( model, RuleSetNavigatorPM.ROOT_RULESET_RECEIVED );

         model.onReceiveRootRuleset( emptyRootRuleset );

         assertEvents();
         assertEquals( emptyRootRuleset, model.rootRuleset );

         var rootRuleset : RootRuleset = new RootRuleset();

         rootRuleset.rulesets = new ArrayCollection();
         rootRuleset.rulesets.addItem( new Ruleset() );
         rootRuleset.rulesets.addItem( new Ruleset() );

         model.onReceiveRootRuleset( rootRuleset );

         assertEquals( rootRuleset, model.rootRuleset );

         for each( var childRuleset : Ruleset in rootRuleset.rulesets )
         {
            assertTrue( childRuleset.hasEventListener( RulesetReceivedEvent.EVENT_NAME ) );
         }
      }
   }
}