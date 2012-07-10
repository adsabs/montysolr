package org.apache.lucene.queryparser.flexible.aqp;

import org.apache.lucene.queryparser.flexible.aqp.builders.AqpFieldQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.builders.InvenioQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNonAnalyzedQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.InvenioQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.BooleanQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.BoostQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FuzzyQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.GroupQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.MatchAllDocsQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.MatchNoDocsQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.ModifierQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.SlopQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.TokenizedPhraseQueryNode;
import org.apache.lucene.queryparser.flexible.standard.builders.BooleanQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.BoostQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.FuzzyQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.GroupQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.MatchAllDocsQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.MatchNoDocsQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.ModifierQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.MultiPhraseQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.PhraseQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.PrefixWildcardQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.SlopQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.StandardBooleanQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.TermRangeQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.WildcardQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.nodes.MultiPhraseQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.PrefixWildcardQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.StandardBooleanQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.TermRangeQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.WildcardQueryNode;

public class AqpInvenioQueryTreeBuilder extends AqpStandardQueryTreeBuilder {
	public void init() {
		setBuilder(GroupQueryNode.class, new GroupQueryNodeBuilder());
		setBuilder(FieldQueryNode.class, new AqpFieldQueryNodeBuilder());
		setBuilder(AqpNonAnalyzedQueryNode.class, new AqpFieldQueryNodeBuilder());
		setBuilder(InvenioQueryNode.class,
				new InvenioQueryNodeBuilder(this));
		setBuilder(BooleanQueryNode.class, new BooleanQueryNodeBuilder());
		setBuilder(FuzzyQueryNode.class, new FuzzyQueryNodeBuilder());
		setBuilder(BoostQueryNode.class, new BoostQueryNodeBuilder());
		setBuilder(ModifierQueryNode.class, new ModifierQueryNodeBuilder());
		setBuilder(WildcardQueryNode.class, new WildcardQueryNodeBuilder());
		setBuilder(TokenizedPhraseQueryNode.class,
				new PhraseQueryNodeBuilder());
		setBuilder(MatchNoDocsQueryNode.class,
				new MatchNoDocsQueryNodeBuilder());
		setBuilder(PrefixWildcardQueryNode.class,
				new PrefixWildcardQueryNodeBuilder());
		setBuilder(TermRangeQueryNode.class, new TermRangeQueryNodeBuilder());
		setBuilder(SlopQueryNode.class, new SlopQueryNodeBuilder());
		setBuilder(StandardBooleanQueryNode.class,
				new StandardBooleanQueryNodeBuilder());
		setBuilder(MultiPhraseQueryNode.class,
				new MultiPhraseQueryNodeBuilder());
		setBuilder(MatchAllDocsQueryNode.class,
				new MatchAllDocsQueryNodeBuilder());

	}
}
