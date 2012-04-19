package org.apache.lucene.queryParser.aqp;

import org.apache.lucene.queryParser.aqp.builders.AqpAdslabsIdentifierNodeBuilder;
import org.apache.lucene.queryParser.aqp.builders.AqpFieldQueryNodeBuilder;
import org.apache.lucene.queryParser.aqp.builders.AqpFunctionQueryNodeBuilder;
import org.apache.lucene.queryParser.aqp.AqpStandardQueryTreeBuilder;
import org.apache.lucene.queryParser.aqp.builders.InvenioQueryNodeBuilder;
import org.apache.lucene.queryParser.aqp.nodes.AqpAdslabsIdentifierNode;
import org.apache.lucene.queryParser.aqp.nodes.AqpFunctionQueryNode;
import org.apache.lucene.queryParser.aqp.nodes.AqpNearQueryNode;
import org.apache.lucene.queryParser.aqp.nodes.InvenioQueryNode;
import org.apache.lucene.queryParser.aqp.nodes.NonAnalyzedQueryNode;
import org.apache.lucene.queryParser.core.nodes.BooleanQueryNode;
import org.apache.lucene.queryParser.core.nodes.BoostQueryNode;
import org.apache.lucene.queryParser.core.nodes.FieldQueryNode;
import org.apache.lucene.queryParser.core.nodes.FuzzyQueryNode;
import org.apache.lucene.queryParser.core.nodes.GroupQueryNode;
import org.apache.lucene.queryParser.core.nodes.MatchAllDocsQueryNode;
import org.apache.lucene.queryParser.core.nodes.MatchNoDocsQueryNode;
import org.apache.lucene.queryParser.core.nodes.ModifierQueryNode;
import org.apache.lucene.queryParser.core.nodes.SlopQueryNode;
import org.apache.lucene.queryParser.core.nodes.TokenizedPhraseQueryNode;
import org.apache.lucene.queryParser.standard.builders.BooleanQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.BoostQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.FuzzyQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.GroupQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.MatchAllDocsQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.MatchNoDocsQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.ModifierQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.MultiPhraseQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.PhraseQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.PrefixWildcardQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.RangeQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.SlopQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.StandardBooleanQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.WildcardQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.nodes.MultiPhraseQueryNode;
import org.apache.lucene.queryParser.standard.nodes.PrefixWildcardQueryNode;
import org.apache.lucene.queryParser.standard.nodes.RangeQueryNode;
import org.apache.lucene.queryParser.standard.nodes.StandardBooleanQueryNode;
import org.apache.lucene.queryParser.standard.nodes.WildcardQueryNode;

public class AqpAdslabsQueryTreeBuilder extends AqpStandardQueryTreeBuilder {

	public void init() {
		setBuilder(GroupQueryNode.class, new GroupQueryNodeBuilder());
		setBuilder(AqpAdslabsIdentifierNode.class, new AqpAdslabsIdentifierNodeBuilder());
		setBuilder(FieldQueryNode.class, new AqpFieldQueryNodeBuilder());
		setBuilder(NonAnalyzedQueryNode.class, new AqpFieldQueryNodeBuilder());
		setBuilder(InvenioQueryNode.class, new InvenioQueryNodeBuilder(this));
		setBuilder(BooleanQueryNode.class, new BooleanQueryNodeBuilder());
		setBuilder(FuzzyQueryNode.class, new FuzzyQueryNodeBuilder());
		setBuilder(BoostQueryNode.class, new BoostQueryNodeBuilder());
		setBuilder(ModifierQueryNode.class, new ModifierQueryNodeBuilder());
		setBuilder(WildcardQueryNode.class, new WildcardQueryNodeBuilder());
		setBuilder(TokenizedPhraseQueryNode.class, new PhraseQueryNodeBuilder());
		setBuilder(MatchNoDocsQueryNode.class, new MatchNoDocsQueryNodeBuilder());
		setBuilder(PrefixWildcardQueryNode.class, new PrefixWildcardQueryNodeBuilder());
		setBuilder(RangeQueryNode.class, new RangeQueryNodeBuilder());
		setBuilder(SlopQueryNode.class, new SlopQueryNodeBuilder());
		setBuilder(StandardBooleanQueryNode.class, new StandardBooleanQueryNodeBuilder());
		setBuilder(MultiPhraseQueryNode.class, new MultiPhraseQueryNodeBuilder());
		setBuilder(MatchAllDocsQueryNode.class,	new MatchAllDocsQueryNodeBuilder());
		setBuilder(AqpNearQueryNode.class,	new AqpNearQueryNodeBuilder());
		setBuilder(AqpFunctionQueryNode.class,	new AqpFunctionQueryNodeBuilder());

	}

}
