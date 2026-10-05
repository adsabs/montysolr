# Second-order query quality reports

All queries use `/select`, `defType=aqp`, `boost=add(earth_science_final_boost, 1)`, and `sort=score desc,date desc`, unless noted. Development core: `/mnt/index/collection1/`; index: `/mnt/index/collection1/conf/data/`.

## Report 1: useful

- Query: `useful(author:"Kelbert, A" AND (*:* NOT author_facet_hier:"1\/Kelbert, A\/Kelbert, Arnaud" NOT author_facet_hier:"1\/Kelbert, A\/Kelbert, A  J"))`
- Report: SciPy, NumPy, and Matplotlib rank first despite only tangential relevance to Anna Kelbert's papers.
- Expected relevant documents: `1997GeoJI.130..475E`, `1953Geop...18..605C`, `1993GeoJI.115..215M`.
- Setting `boost=1` reportedly does not fix the issue; many scores appear to be zero.
- Status: resolved. Baseline: 1,189 results; SciPy ranks first with the reported boost (75.03999), and Matplotlib ranks first with `boost=1` (18.9). After the fix, the first result is `2015GeoRL..4210160B` (score 40 with the reported boost, 20 with `boost=1`); the top results are domain papers rather than the software packages.
- Every one of the 1,189 unboosted scores equals the number of references to that record from the 155 matching Anna Kelbert records. All are finite and positive.
- The three reported relevant records are present. With `boost=1`, their ranks/scores are: `1997GeoJI.130..475E`: 57 / 6; `1953Geop...18..605C`: 416 / 2; `1993GeoJI.115..215M`: 116 / 4. They are not forced to the top: the supplied index contains more frequently referenced domain papers, and the requested date-descending tie-break also favors newer records.

## Report 2: reviews

- Query: `reviews(author:"Kelbert, A" AND (*:* NOT author_facet_hier:"1\/Kelbert, A\/Kelbert, Arnaud" NOT author_facet_hier:"1\/Kelbert, A\/Kelbert, A  J"))`
- Report: the top 20 do not reflect papers that cite Anna Kelbert's work extensively.
- Status: resolved. The corrected query returns 1,621 documents without the backwards DocValues exception. Every unboosted score equals the number of references to matching Anna Kelbert records in that result's reference list; all scores are finite and positive.
- The first result is `2026RvGeo..6400850K`, which references 29 matching records (score 29 with `boost=1`, 58 with the reported boost). The next two records each reference 13 matching records. Repeated requests preserve membership, ranking, and scores.

## Report 3: topn

- Query: `topn(1, =title:"Attention Is All You Need")`
- Expected sole result: `2017arXiv170603762V`.
- Report: returns papers that do not contain all title-search terms.
- Status: resolved. Both the reported boost and `boost=1` return exactly `2017arXiv170603762V`; repeated requests return the same record and score. The unboosted score is 6.3272567, matching the standalone AQP seed query.

## Shared causes and corrections

- Default classic relevance was applied to second-order outputs instead of bootstrap seeds. The scoring builder now pushes that boost to first-order seeds through second-order stages and standard boost, function-score, constant-score, Boolean, and disjunction-max wrappers. Ordinary scored branches in mixed queries retain classic weighting; `useful` and `reviews` retain relationship-count scores. `topn` no longer adds an extra classic output multiplier.
- Lucene 9.10 rewriting and Solr's sorted-score population can copy/reexecute collectors. Rewritten collectors now preserve their aggregation type, and the DocValues accessor reopens its cursor for backwards document access rather than replaying a forward-only cursor.
- The second-order weight previously identified/explained the inner query, including no-match/zero explanations for actual outer results. It now identifies the outer query and explains collected membership and scores.
- Compatibility checks cover explicit query boosts, compound/field-only `topn` sorts, ordinary positional/author queries, and nonnegative public search scores. Collector-owned initialization scoring preserves the complete seed Weight needed to rescore selected records without scoring every field-sorted match. TopN materialization reserves only the selected-result count; explanations agree with returned scores.
- The title analyzer removes stopwords, leaving `title:attention` for the reported exact-title query. Title-analysis semantics are unchanged; corrected bootstrap scoring selects the expected record.

## Verification

- Exercised the actual `/select` handler through embedded Solr on the supplied index, with the reported parameters, `boost=1`, and repeated requests. Independently checked all 1,189 `useful` and 1,621 `reviews` scores against the retrieved reference graph: zero mismatches and zero nonpositive/nonfinite scores.
- The supplied core configuration lacks `citations-cache`. Verification used a temporary configuration copy with the repository's citation cache built from `reference`, disabled remote replication, and issued no document updates. The supplied configuration was not edited.
- Passed 25 tests across `TestSecondOrderWeight`, `TestSecondOrderQueryTypesAds`, `TestAqpAdsabsSolrSearch`, the focused `TestAqpSecondOrderScoring`, and `TestAdsAllFields` after the final review revisions.
- The initial ranking/explanation regressions failed against the original built jar. Review smoke additionally reproduced boosted TopN selecting the raw-BM25 seed and mixed Boolean scoring losing the ordinary branch's classic weight before their corrections.
- Direct CLI smoke: one-vote Agresti–Coull output is 0 with matching explanation; a query boost multiplies a real collected score from 0.31506687 to 0.9452006, with matching explanation.
- Final direct CLI smoke exercised all five supported wrapper types, root/intermediate boosts, field-only TopN rescoring, mixed ordinary/second-order branches, and backwards/reclaimed numeric cursors. The standalone seed scored 12.519036; root/intermediate and field-sorted query boosts produced 25.038073 without changing membership. Mixed Boolean weighting produced 25.038073; mixed disjunction weighting produced 28.167831. Cursor reads preserved 100 → 0 → 100, including after reference reclamation.
- The same critical production reviewer rejected two revisions, then explicitly accepted all five requested criteria: placement, modularity, correctness, WHY comments, and configured/derived/structural values. Original production and regression reviews also approved the report fixes.
