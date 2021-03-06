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
package com.adobe.ac.pmd.model
{
	import com.adobe.ac.model.IDomainModel;
	
	import flash.events.Event;
	import flash.events.EventDispatcher;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ListCollectionView;
	import mx.events.CollectionEvent;

	public class RootRuleset  extends EventDispatcher implements IDomainModel
	{
		public static const CUSTOM_RULESET_NAME : String = "Parameterized rules";
		private static const RULES_CHANGED : String = "rulesChange";
		public var name : String;
		public var description : String;
		[Bindable]
		public var rulesets : ListCollectionView = new ArrayCollection();
		
		private var _customRuleset : Ruleset = null;
		
		public function RootRuleset()
		{
			rulesets.addEventListener(CollectionEvent.COLLECTION_CHANGE, handleRulesetChange);
		}
		

		public function get customRuleset():Ruleset
		{
			return _customRuleset;
		}

		public function addRegExpBasedRule( rule : Rule ) : void
		{
			if ( ! customRuleset )
			{
				_customRuleset = new Ruleset();
				_customRuleset.name = CUSTOM_RULESET_NAME;
				rulesets.addItem( _customRuleset );
			}
			
			rule.ruleset = _customRuleset;
			_customRuleset.rules.addItem( rule );
			rulesChanged();
		}
		
		private function handleRulesetChange( event : CollectionEvent ) : void
		{
			for each ( var ruleset : Ruleset in rulesets )
			{
				ruleset.rules.addEventListener(CollectionEvent.COLLECTION_CHANGE, handleRulesChange);
			}
		}
		
		private function handleRulesChange( event : CollectionEvent ) : void
		{
			rulesChanged();
		}
		
		public function rulesChanged() : void
		{
			dispatchEvent( new Event( RULES_CHANGED ) );			
		}
		
		[Bindable("rulesChange")]
		public function get rulesNb() : Number
		{
			var result : Number = 0;
			
			for each ( var ruleset : Ruleset in rulesets )
			{
				for each ( var rule : Rule in ruleset.rules )
				{
					if ( !rule.deleted )
					{
						result++;
					}
				}
			}
			
			return result;
		}
	}
}