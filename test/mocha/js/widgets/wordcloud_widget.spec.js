define([
    'backbone',
    'js/widgets/base/base_widget',
    'js/widgets/wordcloud/widget',
   'js/components/api_query'
  ],
  function(
    Backbone,
    BaseWidget,
    WordCloud,
    ApiQuery
    ){

    var w, fakeWordCloudData;

    //for query "author:kurtz,m"

    fakeWordCloudData = {
      "AAS": {
        "idf": 0.0006326327944081641,
        "record_count": 4,
        "total_occurrences": 4
      },
      "ADS": {
        "idf": 0.0005928086116725534,
        "record_count": 23,
        "total_occurrences": 52
      },
      "IRAF": {
        "idf": 0.002330016583747927,
        "record_count": 3,
        "total_occurrences": 3
      },
      "NASA": {
        "idf": 6.221267937003598e-05,
        "record_count": 23,
        "total_occurrences": 26
      },
      "NCC5": {
        "idf": 0.008695652173913044,
        "record_count": 3,
        "total_occurrences": 3
      },
      "NCC5189": {
        "idf": 0.023809523809523808,
        "record_count": 3,
        "total_occurrences": 3
      },
      "ability": {
        "idf": 3.247253114732661e-05,
        "record_count": 6,
        "total_occurrences": 8
      },
      "abstract": {
        "idf": 9.644397059711254e-05,
        "record_count": 14,
        "total_occurrences": 24
      },
      "access": {
        "idf": 0.00012216919497490906,
        "record_count": 19,
        "total_occurrences": 29
      },
      "accurate": {
        "idf": 5.569907670455529e-06,
        "record_count": 5,
        "total_occurrences": 5
      },
      "achieve": {
        "idf": 1.4267198874761415e-05,
        "record_count": 5,
        "total_occurrences": 7
      },
      "addition": {
        "idf": 1.869308837039255e-05,
        "record_count": 5,
        "total_occurrences": 7
      },
      "adopted": {
        "idf": 3.013008540796951e-05,
        "record_count": 3,
        "total_occurrences": 3
      },
      "age": {
        "idf": 9.563635402850705e-06,
        "record_count": 5,
        "total_occurrences": 8
      },
      "allow": {
        "idf": 4.155800375459664e-06,
        "record_count": 5,
        "total_occurrences": 5
      },
      "analysis": {
        "idf": 1.7239918129097277e-06,
        "record_count": 8,
        "total_occurrences": 14
      },
      "analyzing": {
        "idf": 1.0995499577259426e-05,
        "record_count": 4,
        "total_occurrences": 4
      },
      "announces": {
        "idf": 0.0015658912841304841,
        "record_count": 4,
        "total_occurrences": 4
      },
      "appear": {
        "idf": 8.50087985830534e-06,
        "record_count": 5,
        "total_occurrences": 5
      },
      "apply": {
        "idf": 1.1724133852208092e-05,
        "record_count": 4,
        "total_occurrences": 5
      },
      "archive": {
        "idf": 0.00038849602584228974,
        "record_count": 5,
        "total_occurrences": 6
      },
      "articles": {
        "idf": 8.812387823775668e-05,
        "record_count": 18,
        "total_occurrences": 44
      },
      "arxiv": {
        "idf": 7.432733759476735e-05,
        "record_count": 3,
        "total_occurrences": 4
      },
      "aspect": {
        "idf": 1.227697995691991e-05,
        "record_count": 4,
        "total_occurrences": 4
      },
      "assess": {
        "idf": 2.189342446889828e-05,
        "record_count": 3,
        "total_occurrences": 3
      },
      "associated": {
        "idf": 1.6879773811030932e-06,
        "record_count": 3,
        "total_occurrences": 3
      },
      "astronomical": {
        "idf": 7.439428295119037e-05,
        "record_count": 20,
        "total_occurrences": 35
      },
      "astronomy": {
        "idf": 2.3631301184159526e-05,
        "record_count": 23,
        "total_occurrences": 32
      },
      "astrophysics": {
        "idf": 5.4919040353399144e-05,
        "record_count": 24,
        "total_occurrences": 30
      },
      "authors": {
        "idf": 3.347659618959714e-05,
        "record_count": 8,
        "total_occurrences": 15
      },
      "automatic": {
        "idf": 5.0533124463085555e-05,
        "record_count": 3,
        "total_occurrences": 3
      },
      "automation": {
        "idf": 0.00030374272554526,
        "record_count": 6,
        "total_occurrences": 8
      },
      "based": {
        "idf": 6.316353291325373e-07,
        "record_count": 4,
        "total_occurrences": 4
      },
      "basis": {
        "idf": 2.973137700872616e-06,
        "record_count": 5,
        "total_occurrences": 6
      },
      "bibliographic": {
        "idf": 0.0010080645161290322,
        "record_count": 6,
        "total_occurrences": 7
      },
      "bibliometric": {
        "idf": 0.007148956029677713,
        "record_count": 6,
        "total_occurrences": 7
      },
      "building": {
        "idf": 4.123035087208658e-05,
        "record_count": 5,
        "total_occurrences": 9
      },
      "built": {
        "idf": 1.068284761986155e-05,
        "record_count": 3,
        "total_occurrences": 3
      },
      "capable": {
        "idf": 1.2433761059187136e-05,
        "record_count": 6,
        "total_occurrences": 6
      },
      "center": {
        "idf": 1.1767571844136518e-05,
        "record_count": 8,
        "total_occurrences": 10
      },
      "central": {
        "idf": 3.349354411937099e-06,
        "record_count": 4,
        "total_occurrences": 5
      },
      "certain": {
        "idf": 3.081470182973875e-05,
        "record_count": 3,
        "total_occurrences": 3
      },
      "change": {
        "idf": 5.914138641977365e-06,
        "record_count": 8,
        "total_occurrences": 15
      },
      "citations": {
        "idf": 0.0005238106808743789,
        "record_count": 12,
        "total_occurrences": 27
      },
      "cited": {
        "idf": 0.0014636472494790925,
        "record_count": 8,
        "total_occurrences": 22
      },
      "classification": {
        "idf": 3.5637831691281136e-05,
        "record_count": 11,
        "total_occurrences": 12
      },
      "classifying": {
        "idf": 7.529329800070575e-05,
        "record_count": 4,
        "total_occurrences": 4
      },
      "cluster": {
        "idf": 9.550316177443126e-06,
        "record_count": 6,
        "total_occurrences": 12
      },
      "collaboration": {
        "idf": 0.0001822160159848221,
        "record_count": 8,
        "total_occurrences": 12
      },
      "collection": {
        "idf": 5.3186016547298297e-05,
        "record_count": 4,
        "total_occurrences": 7
      },
      "combined": {
        "idf": 1.1323507021316612e-05,
        "record_count": 7,
        "total_occurrences": 8
      },
      "comments": {
        "idf": 6.99640370079741e-05,
        "record_count": 3,
        "total_occurrences": 3
      },
      "compare": {
        "idf": 3.934807829897711e-06,
        "record_count": 10,
        "total_occurrences": 13
      },
      "complex": {
        "idf": 2.226635909402638e-06,
        "record_count": 3,
        "total_occurrences": 3
      },
      "component": {
        "idf": 2.9284158044498837e-06,
        "record_count": 8,
        "total_occurrences": 9
      },
      "computer": {
        "idf": 8.271420905856358e-06,
        "record_count": 3,
        "total_occurrences": 3
      },
      "concerning": {
        "idf": 1.1075055652154654e-05,
        "record_count": 3,
        "total_occurrences": 3
      },
      "contents": {
        "idf": 1.4214866314354812e-05,
        "record_count": 3,
        "total_occurrences": 3
      },
      "continue": {
        "idf": 4.427958272099596e-05,
        "record_count": 3,
        "total_occurrences": 3
      },
      "correspond": {
        "idf": 8.490634361122552e-06,
        "record_count": 3,
        "total_occurrences": 3
      },
      "covering": {
        "idf": 1.8546430273233145e-05,
        "record_count": 3,
        "total_occurrences": 3
      },
      "create": {
        "idf": 1.6046120259922448e-05,
        "record_count": 6,
        "total_occurrences": 8
      },
      "cross-correlation": {
        "idf": 5.95699052838506e-05,
        "record_count": 4,
        "total_occurrences": 6
      },
      "current": {
        "idf": 4.02980246249853e-06,
        "record_count": 16,
        "total_occurrences": 25
      },
      "custom": {
        "idf": 0.0012333306998225672,
        "record_count": 3,
        "total_occurrences": 5
      },
      "data": {
        "idf": 1.9479929264569927e-06,
        "record_count": 36,
        "total_occurrences": 49
      },
      "database": {
        "idf": 4.3669855795944735e-05,
        "record_count": 4,
        "total_occurrences": 7
      },
      "decomposition": {
        "idf": 5.738749158139869e-05,
        "record_count": 3,
        "total_occurrences": 3
      },
      "deep": {
        "idf": 4.618127072384524e-06,
        "record_count": 3,
        "total_occurrences": 3
      },
      "demonstrate": {
        "idf": 2.757041677839324e-06,
        "record_count": 7,
        "total_occurrences": 9
      },
      "describe": {
        "idf": 2.8792197616577523e-06,
        "record_count": 7,
        "total_occurrences": 7
      },
      "detail": {
        "idf": 0.0011209723958260256,
        "record_count": 8,
        "total_occurrences": 8
      },
      "determine": {
        "idf": 7.775453460438545e-06,
        "record_count": 5,
        "total_occurrences": 8
      },
      "develop": {
        "idf": 6.461589091164084e-06,
        "record_count": 19,
        "total_occurrences": 30
      },
      "different": {
        "idf": 2.3168497730185505e-06,
        "record_count": 9,
        "total_occurrences": 16
      },
      "difficult": {
        "idf": 8.155609020103576e-06,
        "record_count": 3,
        "total_occurrences": 3
      },
      "digital": {
        "idf": 1.7308276750582426e-05,
        "record_count": 8,
        "total_occurrences": 9
      },
      "direct": {
        "idf": 3.448789439584518e-06,
        "record_count": 4,
        "total_occurrences": 4
      },
      "disciplines": {
        "idf": 0.00014358976259661193,
        "record_count": 3,
        "total_occurrences": 3
      },
      "discovery": {
        "idf": 3.151434763840919e-05,
        "record_count": 6,
        "total_occurrences": 8
      },
      "discuss": {
        "idf": 5.983233761092861e-06,
        "record_count": 16,
        "total_occurrences": 23
      },
      "distributed": {
        "idf": 1.558985388175292e-05,
        "record_count": 6,
        "total_occurrences": 6
      },
      "dominant": {
        "idf": 6.333563452805452e-06,
        "record_count": 3,
        "total_occurrences": 3
      },
      "e-mail": {
        "idf": 0.0003489183531053733,
        "record_count": 4,
        "total_occurrences": 4
      },
      "e-print": {
        "idf": 0.10308886971527179,
        "record_count": 3,
        "total_occurrences": 11
      },
      "early": {
        "idf": 4.080733226146074e-06,
        "record_count": 3,
        "total_occurrences": 4
      },
      "earth": {
        "idf": 3.6316622844608434e-06,
        "record_count": 3,
        "total_occurrences": 3
      },
      "edu": {
        "idf": 0.0001260716086737267,
        "record_count": 7,
        "total_occurrences": 8
      },
      "effect": {
        "idf": 2.7947711015247345e-05,
        "record_count": 9,
        "total_occurrences": 9
      },
      "efficient": {
        "idf": 4.394574908699395e-06,
        "record_count": 3,
        "total_occurrences": 3
      },
      "electronic": {
        "idf": 4.3525750183112705e-06,
        "record_count": 7,
        "total_occurrences": 11
      },
      "entire": {
        "idf": 1.3852699100885736e-05,
        "record_count": 3,
        "total_occurrences": 3
      },
      "environment": {
        "idf": 1.2036284385073316e-05,
        "record_count": 6,
        "total_occurrences": 6
      },
      "era": {
        "idf": 9.245636693448725e-05,
        "record_count": 4,
        "total_occurrences": 4
      },
      "error": {
        "idf": 1.0667649332124732e-05,
        "record_count": 7,
        "total_occurrences": 13
      },
      "essentially": {
        "idf": 1.0978997178397725e-05,
        "record_count": 3,
        "total_occurrences": 3
      },
      "estimator": {
        "idf": 4.8142648192760264e-05,
        "record_count": 4,
        "total_occurrences": 7
      },
      "examine": {
        "idf": 1.7123622933220518e-05,
        "record_count": 6,
        "total_occurrences": 7
      },
      "examples": {
        "idf": 4.355078308825651e-06,
        "record_count": 5,
        "total_occurrences": 5
      },
      "exists": {
        "idf": 7.1302351916180946e-06,
        "record_count": 4,
        "total_occurrences": 4
      },
      "exponentials": {
        "idf": 0.00024261789907476593,
        "record_count": 3,
        "total_occurrences": 3
      },
      "extended": {
        "idf": 3.6112946852576117e-06,
        "record_count": 4,
        "total_occurrences": 4
      },
      "factor": {
        "idf": 2.6664106912403074e-06,
        "record_count": 3,
        "total_occurrences": 3
      },
      "far": {
        "idf": 4.16345733497096e-06,
        "record_count": 3,
        "total_occurrences": 3
      },
      "field": {
        "idf": 1.315195324782691e-06,
        "record_count": 5,
        "total_occurrences": 7
      },
      "find": {
        "idf": 8.887169738777426e-05,
        "record_count": 8,
        "total_occurrences": 9
      },
      "first": {
        "idf": 8.400685831991324e-07,
        "record_count": 11,
        "total_occurrences": 14
      },
      "fitting": {
        "idf": 1.3587372111891358e-05,
        "record_count": 5,
        "total_occurrences": 9
      },
      "follows": {
        "idf": 8.236252190853968e-06,
        "record_count": 5,
        "total_occurrences": 6
      },
      "form": {
        "idf": 2.597096486473177e-06,
        "record_count": 5,
        "total_occurrences": 5
      },
      "format": {
        "idf": 0.00033258919970932085,
        "record_count": 3,
        "total_occurrences": 4
      },
      "four": {
        "idf": 2.401675408765154e-06,
        "record_count": 7,
        "total_occurrences": 8
      },
      "free": {
        "idf": 2.103867961246752e-06,
        "record_count": 3,
        "total_occurrences": 3
      },
      "fully": {
        "idf": 2.9624150336583216e-05,
        "record_count": 3,
        "total_occurrences": 3
      },
      "function": {
        "idf": 2.0520805959790547e-06,
        "record_count": 8,
        "total_occurrences": 17
      },
      "funded": {
        "idf": 0.00012864475933914234,
        "record_count": 4,
        "total_occurrences": 4
      },
      "future": {
        "idf": 1.0322759658204568e-05,
        "record_count": 8,
        "total_occurrences": 8
      },
      "galaxies": {
        "idf": 6.750788632459004e-06,
        "record_count": 10,
        "total_occurrences": 21
      },
      "general": {
        "idf": 2.4090594165650427e-06,
        "record_count": 5,
        "total_occurrences": 6
      },
      "generation": {
        "idf": 2.6924165516062924e-05,
        "record_count": 4,
        "total_occurrences": 6
      },
      "goal": {
        "idf": 2.062150865158078e-05,
        "record_count": 4,
        "total_occurrences": 4
      },
      "grant": {
        "idf": 4.462492748449283e-05,
        "record_count": 3,
        "total_occurrences": 3
      },
      "harvard": {
        "idf": 0.00015629884338855892,
        "record_count": 7,
        "total_occurrences": 9
      },
      "heart": {
        "idf": 6.650262685376072e-05,
        "record_count": 3,
        "total_occurrences": 3
      },
      "heavily": {
        "idf": 3.268828451882845e-05,
        "record_count": 3,
        "total_occurrences": 3
      },
      "high": {
        "idf": 1.3543998706626272e-06,
        "record_count": 3,
        "total_occurrences": 4
      },
      "higher": {
        "idf": 1.5041303419189094e-06,
        "record_count": 3,
        "total_occurrences": 3
      },
      "historical": {
        "idf": 4.33068705271435e-05,
        "record_count": 5,
        "total_occurrences": 5
      },
      "history": {
        "idf": 2.017128288516673e-05,
        "record_count": 3,
        "total_occurrences": 3
      },
      "html": {
        "idf": 0.0001383891502906172,
        "record_count": 3,
        "total_occurrences": 3
      },
      "human": {
        "idf": 1.9048833393652633e-05,
        "record_count": 5,
        "total_occurrences": 6
      },
      "images": {
        "idf": 1.4522110251156589e-05,
        "record_count": 3,
        "total_occurrences": 3
      },
      "impact": {
        "idf": 7.772744826089757e-06,
        "record_count": 4,
        "total_occurrences": 4
      },
      "implements": {
        "idf": 8.982978371370069e-05,
        "record_count": 4,
        "total_occurrences": 4
      },
      "important": {
        "idf": 1.5273144923817552e-06,
        "record_count": 6,
        "total_occurrences": 7
      },
      "improved": {
        "idf": 9.047263891080596e-06,
        "record_count": 4,
        "total_occurrences": 4
      },
      "include": {
        "idf": 4.114799809551995e-06,
        "record_count": 10,
        "total_occurrences": 17
      },
      "increase": {
        "idf": 1.3556392053955957e-05,
        "record_count": 9,
        "total_occurrences": 13
      },
      "index": {
        "idf": 0.00024194327192804128,
        "record_count": 7,
        "total_occurrences": 12
      },
      "individual": {
        "idf": 1.4820949095346555e-05,
        "record_count": 7,
        "total_occurrences": 13
      },
      "information": {
        "idf": 9.62008489760045e-06,
        "record_count": 18,
        "total_occurrences": 24
      },
      "intelligent": {
        "idf": 0.00016726545004858829,
        "record_count": 5,
        "total_occurrences": 5
      },
      "interact": {
        "idf": 1.50692158011014e-05,
        "record_count": 4,
        "total_occurrences": 4
      },
      "interesting": {
        "idf": 6.4133173173571736e-06,
        "record_count": 4,
        "total_occurrences": 7
      },
      "international": {
        "idf": 1.055934464727285e-05,
        "record_count": 5,
        "total_occurrences": 5
      },
      "introduce": {
        "idf": 6.5652654773334714e-06,
        "record_count": 3,
        "total_occurrences": 3
      },
      "investigate": {
        "idf": 1.0363340530735209e-05,
        "record_count": 5,
        "total_occurrences": 5
      },
      "issues": {
        "idf": 1.1711602242781915e-05,
        "record_count": 3,
        "total_occurrences": 3
      },
      "journals": {
        "idf": 0.0001471129610692253,
        "record_count": 17,
        "total_occurrences": 34
      },
      "knowledge": {
        "idf": 7.853187185207428e-05,
        "record_count": 6,
        "total_occurrences": 6
      },
      "known": {
        "idf": 1.891528411702508e-06,
        "record_count": 3,
        "total_occurrences": 3
      },
      "large": {
        "idf": 1.5670525794243826e-06,
        "record_count": 8,
        "total_occurrences": 12
      },
      "last": {
        "idf": 5.810136363900461e-06,
        "record_count": 3,
        "total_occurrences": 3
      },
      "less": {
        "idf": 2.2690606769515624e-06,
        "record_count": 4,
        "total_occurrences": 4
      },
      "level": {
        "idf": 2.0744960415975916e-06,
        "record_count": 3,
        "total_occurrences": 3
      },
      "library": {
        "idf": 0.00019808551940228079,
        "record_count": 5,
        "total_occurrences": 8
      },
      "line": {
        "idf": 3.5006235840165133e-06,
        "record_count": 5,
        "total_occurrences": 10
      },
      "links": {
        "idf": 1.9513412542063005e-05,
        "record_count": 4,
        "total_occurrences": 8
      },
      "lists": {
        "idf": 0.0001253420116645479,
        "record_count": 10,
        "total_occurrences": 23
      },
      "literature": {
        "idf": 3.09691305183552e-05,
        "record_count": 16,
        "total_occurrences": 28
      },
      "logs": {
        "idf": 0.00017975912277548087,
        "record_count": 3,
        "total_occurrences": 5
      },
      "long": {
        "idf": 1.69849360602082e-06,
        "record_count": 4,
        "total_occurrences": 4
      },
      "machines": {
        "idf": 5.164796910784366e-05,
        "record_count": 3,
        "total_occurrences": 4
      },
      "main": {
        "idf": 2.2871571551424443e-06,
        "record_count": 3,
        "total_occurrences": 3
      },
      "major": {
        "idf": 1.0172109879869033e-05,
        "record_count": 6,
        "total_occurrences": 8
      },
      "many": {
        "idf": 1.82471096578302e-06,
        "record_count": 5,
        "total_occurrences": 5
      },
      "map": {
        "idf": 2.0677872826393333e-05,
        "record_count": 3,
        "total_occurrences": 10
      },
      "mean": {
        "idf": 2.6486462614082084e-06,
        "record_count": 9,
        "total_occurrences": 14
      },
      "measure": {
        "idf": 7.211700194797735e-06,
        "record_count": 10,
        "total_occurrences": 24
      },
      "methodology": {
        "idf": 2.5989579722775106e-05,
        "record_count": 5,
        "total_occurrences": 5
      },
      "methods": {
        "idf": 1.9784654860549285e-06,
        "record_count": 16,
        "total_occurrences": 34
      },
      "model": {
        "idf": 9.74906020568687e-07,
        "record_count": 9,
        "total_occurrences": 11
      },
      "modern": {
        "idf": 1.2558238832586118e-05,
        "record_count": 3,
        "total_occurrences": 3
      },
      "month": {
        "idf": 3.635509735329652e-05,
        "record_count": 6,
        "total_occurrences": 12
      },
      "more": {
        "idf": 6.458042993652879e-06,
        "record_count": 11,
        "total_occurrences": 19
      },
      "most": {
        "idf": 1.192382583106085e-06,
        "record_count": 8,
        "total_occurrences": 15
      },
      "name": {
        "idf": 0.0001294728295540219,
        "record_count": 3,
        "total_occurrences": 4
      },
      "nature": {
        "idf": 3.526851106275036e-06,
        "record_count": 6,
        "total_occurrences": 6
      },
      "ncc5-189": {
        "idf": 0.023809523809523808,
        "record_count": 3,
        "total_occurrences": 3
      },
      "nearly": {
        "idf": 3.5645528349046472e-06,
        "record_count": 12,
        "total_occurrences": 13
      },
      "necessary": {
        "idf": 4.834350964211299e-06,
        "record_count": 3,
        "total_occurrences": 3
      },
      "network": {
        "idf": 5.628653779261094e-06,
        "record_count": 4,
        "total_occurrences": 5
      },
      "new": {
        "idf": 9.451857280420114e-07,
        "record_count": 19,
        "total_occurrences": 29
      },
      "normal": {
        "idf": 2.2092681970047618e-05,
        "record_count": 5,
        "total_occurrences": 6
      },
      "normative": {
        "idf": 0.0005376344086021505,
        "record_count": 3,
        "total_occurrences": 3
      },
      "number": {
        "idf": 1.1718150360274533e-06,
        "record_count": 7,
        "total_occurrences": 21
      },
      "object": {
        "idf": 2.0555002068374608e-05,
        "record_count": 4,
        "total_occurrences": 7
      },
      "observations": {
        "idf": 1.4423169862573016e-05,
        "record_count": 10,
        "total_occurrences": 12
      },
      "obtain": {
        "idf": 1.6522856685123575e-05,
        "record_count": 7,
        "total_occurrences": 12
      },
      "on-line": {
        "idf": 2.8010039275063888e-05,
        "record_count": 7,
        "total_occurrences": 9
      },
      "one": {
        "idf": 6.164365406321064e-07,
        "record_count": 9,
        "total_occurrences": 9
      },
      "open": {
        "idf": 1.8053809445773997e-05,
        "record_count": 5,
        "total_occurrences": 6
      },
      "operates": {
        "idf": 3.565427735866811e-05,
        "record_count": 4,
        "total_occurrences": 4
      },
      "optical": {
        "idf": 1.3005321777671423e-06,
        "record_count": 3,
        "total_occurrences": 4
      },
      "order": {
        "idf": 4.367844243354917e-06,
        "record_count": 5,
        "total_occurrences": 5
      },
      "organism": {
        "idf": 8.717740883089108e-05,
        "record_count": 5,
        "total_occurrences": 6
      },
      "over": {
        "idf": 1.0103183816316036e-06,
        "record_count": 3,
        "total_occurrences": 4
      },
      "page": {
        "idf": 0.002022624930749678,
        "record_count": 3,
        "total_occurrences": 3
      },
      "papers": {
        "idf": 2.7093620399782362e-05,
        "record_count": 12,
        "total_occurrences": 19
      },
      "parameters": {
        "idf": 1.5072989077974346e-06,
        "record_count": 4,
        "total_occurrences": 4
      },
      "particular": {
        "idf": 2.793249287347041e-06,
        "record_count": 4,
        "total_occurrences": 5
      },
      "past": {
        "idf": 6.7411791670599024e-06,
        "record_count": 3,
        "total_occurrences": 3
      },
      "pattern": {
        "idf": 5.4445719885845476e-06,
        "record_count": 5,
        "total_occurrences": 6
      },
      "per": {
        "idf": 4.404413222048492e-06,
        "record_count": 7,
        "total_occurrences": 12
      },
      "permit": {
        "idf": 3.7826174991440744e-05,
        "record_count": 3,
        "total_occurrences": 4
      },
      "photographic": {
        "idf": 5.9049654984760985e-05,
        "record_count": 3,
        "total_occurrences": 3
      },
      "photometric": {
        "idf": 2.914477981989933e-05,
        "record_count": 3,
        "total_occurrences": 3
      },
      "physics": {
        "idf": 6.619980143193781e-06,
        "record_count": 7,
        "total_occurrences": 14
      },
      "policy": {
        "idf": 6.579738957155608e-05,
        "record_count": 3,
        "total_occurrences": 3
      },
      "powerful": {
        "idf": 1.2576243476073698e-05,
        "record_count": 5,
        "total_occurrences": 5
      },
      "present": {
        "idf": 2.745028612815438e-06,
        "record_count": 5,
        "total_occurrences": 6
      },
      "previous": {
        "idf": 2.585217392343991e-06,
        "record_count": 3,
        "total_occurrences": 3
      },
      "problems": {
        "idf": 3.3493667427265962e-06,
        "record_count": 5,
        "total_occurrences": 6
      },
      "procedure": {
        "idf": 1.010061872052617e-05,
        "record_count": 4,
        "total_occurrences": 4
      },
      "process": {
        "idf": 2.341443564350511e-06,
        "record_count": 3,
        "total_occurrences": 6
      },
      "productivity": {
        "idf": 3.031022366118067e-05,
        "record_count": 3,
        "total_occurrences": 7
      },
      "program": {
        "idf": 1.971090710896563e-05,
        "record_count": 4,
        "total_occurrences": 4
      },
      "progress": {
        "idf": 1.970720648402135e-05,
        "record_count": 5,
        "total_occurrences": 5
      },
      "project": {
        "idf": 7.36084325820366e-06,
        "record_count": 4,
        "total_occurrences": 4
      },
      "properly": {
        "idf": 2.3111234371027756e-05,
        "record_count": 3,
        "total_occurrences": 3
      },
      "provide": {
        "idf": 1.7237959003942336e-05,
        "record_count": 17,
        "total_occurrences": 22
      },
      "publication": {
        "idf": 6.352457589568647e-05,
        "record_count": 6,
        "total_occurrences": 7
      },
      "published": {
        "idf": 0.00036760500731061356,
        "record_count": 6,
        "total_occurrences": 8
      },
      "queries": {
        "idf": 0.00016025641025641026,
        "record_count": 4,
        "total_occurrences": 6
      },
      "radial": {
        "idf": 4.067024564828372e-05,
        "record_count": 3,
        "total_occurrences": 3
      },
      "ranking": {
        "idf": 0.00012972591812983176,
        "record_count": 3,
        "total_occurrences": 3
      },
      "rate": {
        "idf": 1.5392050929218116e-06,
        "record_count": 5,
        "total_occurrences": 10
      },
      "reached": {
        "idf": 1.2731814471831398e-05,
        "record_count": 3,
        "total_occurrences": 3
      },
      "readership": {
        "idf": 0.013187954309449636,
        "record_count": 6,
        "total_occurrences": 11
      },
      "reads": {
        "idf": 0.03186228245191909,
        "record_count": 10,
        "total_occurrences": 31
      },
      "recent": {
        "idf": 1.8893640525740915e-06,
        "record_count": 9,
        "total_occurrences": 12
      },
      "recognition": {
        "idf": 2.8114896209174827e-05,
        "record_count": 5,
        "total_occurrences": 6
      },
      "redshift": {
        "idf": 9.111535077411884e-05,
        "record_count": 7,
        "total_occurrences": 16
      },
      "reduce": {
        "idf": 6.443091395251442e-06,
        "record_count": 3,
        "total_occurrences": 3
      },
      "reduction": {
        "idf": 1.6822054473443903e-05,
        "record_count": 3,
        "total_occurrences": 4
      },
      "reference": {
        "idf": 1.2305997170393443e-05,
        "record_count": 4,
        "total_occurrences": 6
      },
      "regular": {
        "idf": 4.252580112484158e-05,
        "record_count": 4,
        "total_occurrences": 4
      },
      "relative": {
        "idf": 3.540329432325968e-05,
        "record_count": 8,
        "total_occurrences": 8
      },
      "release": {
        "idf": 1.653047044401525e-05,
        "record_count": 5,
        "total_occurrences": 9
      },
      "relevant": {
        "idf": 0.0006355650248230412,
        "record_count": 3,
        "total_occurrences": 6
      },
      "report": {
        "idf": 1.0927552669557002e-05,
        "record_count": 6,
        "total_occurrences": 6
      },
      "research": {
        "idf": 8.518912367013913e-06,
        "record_count": 15,
        "total_occurrences": 29
      },
      "results": {
        "idf": 1.1311417546117498e-06,
        "record_count": 6,
        "total_occurrences": 7
      },
      "retrieval": {
        "idf": 6.096072264514432e-05,
        "record_count": 7,
        "total_occurrences": 7
      },
      "review": {
        "idf": 8.369521257777636e-06,
        "record_count": 6,
        "total_occurrences": 9
      },
      "sample": {
        "idf": 2.5514499890287646e-06,
        "record_count": 3,
        "total_occurrences": 3
      },
      "scale": {
        "idf": 3.2054181895022178e-06,
        "record_count": 3,
        "total_occurrences": 3
      },
      "scholarly": {
        "idf": 0.0023029150085224254,
        "record_count": 4,
        "total_occurrences": 7
      },
      "science": {
        "idf": 7.995306806706416e-06,
        "record_count": 4,
        "total_occurrences": 8
      },
      "scientific": {
        "idf": 9.90148027130056e-06,
        "record_count": 7,
        "total_occurrences": 10
      },
      "search": {
        "idf": 1.218862511705757e-05,
        "record_count": 11,
        "total_occurrences": 12
      },
      "second": {
        "idf": 8.60347525183236e-06,
        "record_count": 5,
        "total_occurrences": 5
      },
      "service": {
        "idf": 4.121286315286631e-05,
        "record_count": 17,
        "total_occurrences": 22
      },
      "set": {
        "idf": 1.0016543623831611e-05,
        "record_count": 8,
        "total_occurrences": 10
      },
      "several": {
        "idf": 1.3673829930372858e-06,
        "record_count": 5,
        "total_occurrences": 6
      },
      "significantly": {
        "idf": 2.5528785133095338e-05,
        "record_count": 3,
        "total_occurrences": 4
      },
      "similarities": {
        "idf": 1.6694712466560283e-05,
        "record_count": 6,
        "total_occurrences": 7
      },
      "simple": {
        "idf": 1.7425731532209722e-06,
        "record_count": 5,
        "total_occurrences": 5
      },
      "singular": {
        "idf": 1.878428131339695e-05,
        "record_count": 3,
        "total_occurrences": 3
      },
      "small": {
        "idf": 1.191606798355106e-06,
        "record_count": 4,
        "total_occurrences": 4
      },
      "smithsonian/nasa": {
        "idf": 0.15131578947368418,
        "record_count": 4,
        "total_occurrences": 4
      },
      "sophisticated": {
        "idf": 0.00017883763523298408,
        "record_count": 3,
        "total_occurrences": 3
      },
      "specific": {
        "idf": 2.794318591439884e-06,
        "record_count": 3,
        "total_occurrences": 4
      },
      "spectra": {
        "idf": 2.53398409596637e-06,
        "record_count": 7,
        "total_occurrences": 20
      },
      "spectral": {
        "idf": 6.320443363535223e-06,
        "record_count": 6,
        "total_occurrences": 9
      },
      "standard": {
        "idf": 9.372420297895787e-06,
        "record_count": 3,
        "total_occurrences": 4
      },
      "stars": {
        "idf": 3.622689629688666e-06,
        "record_count": 4,
        "total_occurrences": 7
      },
      "statistics": {
        "idf": 9.214393634697866e-06,
        "record_count": 9,
        "total_occurrences": 11
      },
      "status": {
        "idf": 1.5157486282474912e-05,
        "record_count": 3,
        "total_occurrences": 3
      },
      "structure": {
        "idf": 1.3350869731679599e-06,
        "record_count": 4,
        "total_occurrences": 4
      },
      "study": {
        "idf": 1.1055790463742904e-06,
        "record_count": 4,
        "total_occurrences": 5
      },
      "subject": {
        "idf": 3.00918646536296e-05,
        "record_count": 3,
        "total_occurrences": 4
      },
      "substantially": {
        "idf": 1.3203586093983125e-05,
        "record_count": 10,
        "total_occurrences": 10
      },
      "sum": {
        "idf": 3.6204271817897584e-05,
        "record_count": 4,
        "total_occurrences": 6
      },
      "surveys": {
        "idf": 2.1688697208611512e-05,
        "record_count": 9,
        "total_occurrences": 15
      },
      "syntactic": {
        "idf": 0.0005002501250625312,
        "record_count": 4,
        "total_occurrences": 4
      },
      "synthetic": {
        "idf": 1.2622278321236984e-05,
        "record_count": 3,
        "total_occurrences": 3
      },
      "system": {
        "idf": 1.5869086612577421e-06,
        "record_count": 31,
        "total_occurrences": 49
      },
      "systematic": {
        "idf": 3.535250984156843e-05,
        "record_count": 5,
        "total_occurrences": 5
      },
      "technical": {
        "idf": 2.6004561726726803e-05,
        "record_count": 10,
        "total_occurrences": 11
      },
      "techniques": {
        "idf": 3.655413511709254e-06,
        "record_count": 12,
        "total_occurrences": 18
      },
      "technologies": {
        "idf": 1.573662681144755e-05,
        "record_count": 6,
        "total_occurrences": 7
      },
      "ten": {
        "idf": 1.7346053772766696e-05,
        "record_count": 3,
        "total_occurrences": 3
      },
      "term": {
        "idf": 2.6232625120276932e-06,
        "record_count": 5,
        "total_occurrences": 9
      },
      "test": {
        "idf": 2.5241642833810174e-05,
        "record_count": 4,
        "total_occurrences": 6
      },
      "text": {
        "idf": 6.542855723918062e-05,
        "record_count": 6,
        "total_occurrences": 9
      },
      "theory": {
        "idf": 1.0423300663234622e-06,
        "record_count": 3,
        "total_occurrences": 3
      },
      "three": {
        "idf": 9.910950113232605e-07,
        "record_count": 4,
        "total_occurrences": 4
      },
      "through": {
        "idf": 2.6776183974916867e-06,
        "record_count": 6,
        "total_occurrences": 7
      },
      "time": {
        "idf": 2.3110055287044414e-06,
        "record_count": 14,
        "total_occurrences": 19
      },
      "tool": {
        "idf": 7.910667134174434e-06,
        "record_count": 3,
        "total_occurrences": 4
      },
      "total": {
        "idf": 2.062115028900542e-06,
        "record_count": 3,
        "total_occurrences": 6
      },
      "true": {
        "idf": 1.1414482695644234e-05,
        "record_count": 4,
        "total_occurrences": 4
      },
      "two": {
        "idf": 4.3985751764881984e-07,
        "record_count": 9,
        "total_occurrences": 10
      },
      "type": {
        "idf": 1.6685219850250307e-06,
        "record_count": 6,
        "total_occurrences": 6
      },
      "under": {
        "idf": 2.7365981753258345e-06,
        "record_count": 4,
        "total_occurrences": 4
      },
      "unique": {
        "idf": 5.575752168967594e-06,
        "record_count": 5,
        "total_occurrences": 9
      },
      "universe": {
        "idf": 9.897014798455852e-06,
        "record_count": 4,
        "total_occurrences": 4
      },
      "usage": {
        "idf": 0.00021966271050192522,
        "record_count": 3,
        "total_occurrences": 3
      },
      "use": {
        "idf": 2.1975817277539333e-06,
        "record_count": 22,
        "total_occurrences": 34
      },
      "users": {
        "idf": 2.17187869552088e-05,
        "record_count": 8,
        "total_occurrences": 15
      },
      "value": {
        "idf": 1.8003420649923483e-06,
        "record_count": 5,
        "total_occurrences": 6
      },
      "velocities": {
        "idf": 4.023654628263188e-05,
        "record_count": 3,
        "total_occurrences": 4
      },
      "view": {
        "idf": 6.116582053948253e-06,
        "record_count": 3,
        "total_occurrences": 4
      },
      "ways": {
        "idf": 1.4964907292399323e-05,
        "record_count": 4,
        "total_occurrences": 4
      },
      "well": {
        "idf": 6.970166294227448e-07,
        "record_count": 5,
        "total_occurrences": 6
      },
      "whole": {
        "idf": 7.025135936380368e-06,
        "record_count": 4,
        "total_occurrences": 7
      },
      "work": {
        "idf": 6.254869158898443e-06,
        "record_count": 8,
        "total_occurrences": 8
      },
      "world": {
        "idf": 8.737210907534097e-06,
        "record_count": 3,
        "total_occurrences": 3
      },
      "worldwide": {
        "idf": 0.00015922944696245749,
        "record_count": 4,
        "total_occurrences": 5
      },
      "years": {
        "idf": 0.000284762155690129,
        "record_count": 9,
        "total_occurrences": 17
      }
    };


    describe("WordCloud Widget (Visualization Widget)", function(){

      afterEach(function(){
        $("#test").empty()
      });


      it("should request wordcloud data from pubsub", function(){

        w = new WordCloud();
        $("#test").append(w.render().el);

        w.model.set("tfidfData", fakeWordCloudData);
      });

      it("should consist of a controller, a word cloud view, and a list view, (the views have corresponding models)", function(){

        w = new WordCloud();
        $("#test").append(w.render().el);
        w.model.set("tfidfData", fakeWordCloudData);

        expect(w).to.be.instanceof(BaseWidget);
        expect(w.view).to.be.instanceof(Backbone.View);
        expect(w.view.model).to.be.instanceof(Backbone.Model);

        expect(w.listView).to.be.instanceof(Backbone.View);
        expect(w.listView.model).to.be.instanceof(Backbone.Model);

      });

      it("should have a buildWCDict function on the word cloud model that listens for slider changes and re-processes keyword dict", function(){

        w = new WordCloud();
        $("#test").append(w.render().el);
        w.model.set("tfidfData", fakeWordCloudData);

        //resetting slider, recomputing processedWordList
        w.model.set("currentSliderVal", 5);
        var processed = w.model.get("processedWordList");
        expect(_.findWhere(processed, {text: "abstract"}).origSize).to.eql(2.7685459940652817);
        expect(_.findWhere(processed, {text: "abstract"}).size).to.eql(47);

        //this more rare word is not shown in the frequency word cloud
        expect(_.findWhere(processed, {text: "e-print"})).to.be.undefined;

        //resetting slider, recomputing processedWordList
        w.model.set("currentSliderVal", 1);
        var processed = w.model.get("processedWordList");

        //since we are looking at rarer words, the size of the word "abstract" has shrunk
        expect(_.findWhere(processed, {text: "abstract"}).origSize).to.eql(0.07704642165507428);
        expect(_.findWhere(processed, {text: "e-print"}).size).to.eql(67);

        //eprint is a rare word, so it is larger in this word cloud version
        expect(_.findWhere(processed, {text: "e-print"}).origSize).to.eql(82.35484784432593);

      });

      it("should have a draw function on the word cloud view that takes care of the word cloud rendering", function(done){

        this.timeout(3000); // this test was often timing out on travis

        w = new WordCloud();
        $("#test").append(w.render().el);

        //this change triggers the data processing step, which in turn triggers the cloud layout
        w.model.set("tfidfData", fakeWordCloudData);

        setTimeout(function(){

          //word cloud has 50 words
          expect(d3.selectAll(".s-wordcloud-text")[0].length).to.eql(50);

          //in this particular cloud, smithsonian/nasa is one of the dominant words
          expect(d3.selectAll(".s-wordcloud-text").filter(function(d){return d.text == "smithsonian/nasa"}).style("font-size")).to.eql('70px');

          //"iraf", meanwhile, should be smaller
          expect(d3.selectAll(".s-wordcloud-text").filter(function(d){return d.text == "IRAF"}).style("font-size")).to.eql('31px');
          done();

        }, 1500);
      });

      it("should have a list view that listens for click events on word cloud words and adds/removes the word from the list", function(){

        w = new WordCloud();
        $("#test").append(w.render().el);

        w.model.set("tfidfData", fakeWordCloudData);
        w.listView.model.trigger("selected", "optical");
        expect(w.listView.model.get("selectedWords")[0]).to.eql("optical");

        w.listView.model.trigger("unselected", "optical");
        expect(w.listView.model.get("selectedWords")[0]).to.eql(undefined)

      });


      it("should have a submit button that emits a start_search with the new filter", function(){

        w = new WordCloud();
        $("#test").append(w.render().el);

        w.model.set("tfidfData", fakeWordCloudData);
        w.pubsub = {publish : function(){}};
        var pubsubStub = sinon.stub(w.pubsub, "publish");

        //for testing
        w.getCurrentQuery = function(){return new ApiQuery()};
        w.listView.model.set("selectedWords",["fakeA", "fakeB"]);

        //clicking submit button
        $(".apply-vis-facet").click();
        expect(pubsubStub.args[0][1].get("q")).to.eql(["undefined AND (\"fakeA\" OR \"fakeB\")"])
      })
    });

    it("doesnt allow to request more than max_rows", function() {
      var w = new WordCloud();
      var q = w.customizeQuery(new ApiQuery({'q': 'foo:bar', 'rows': 3000}));
      expect(q.get('rows')).to.be.eql([150]);
    });

});