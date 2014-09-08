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

    fakeWordCloudData = {
      'GRB': {'idf': 4.6855964764314496e-05, 'total_occurences': 437},
      'HST': {'idf': 3.6708024374128185e-05, 'total_occurences': 24},
      'II': {'idf': 5.997648921622724e-06, 'total_occurences': 27},
      'IR': {'idf': 1.0991426687183997e-05, 'total_occurences': 16},
      'LCO': {'idf': 0.018518518518518517, 'total_occurences': 17},
      'NEAR': {'idf': 0.0014367816091954023, 'total_occurences': 25},
      'NGC': {'idf': 2.709733362237156e-05, 'total_occurences': 35},
      'NIR': {'idf': 0.0002891008962127783, 'total_occurences': 16},
      'PSN': {'idf': 0.0013812154696132596, 'total_occurences': 15},
      'RA': {'idf': 6.151574803149606e-05, 'total_occurences': 15},
      'SN': {'idf': 3.50495951771757e-05, 'total_occurences': 62},
      'STARRS': {'idf': 0.0030303030303030303, 'total_occurences': 15},
      'TOO': {'idf': 0.0013568521031207597, 'total_occurences': 15},
      'UT': {'idf': 3.094059405940594e-05, 'total_occurences': 31},
      'XRT': {'idf': 0.0003485535029627048, 'total_occurences': 18},
      'absorption': {'idf': 3.323274888005636e-06, 'total_occurences': 40},
      'activity': {'idf': 4.59166337597458e-06, 'total_occurences': 42},
      'additional': {'idf': 0.00021654395842355997, 'total_occurences': 41},
      'address': {'idf': 1.4526016094825833e-05, 'total_occurences': 17},
      'afterglow': {'idf': 0.00012169891687963977, 'total_occurences': 166},
      'age': {'idf': 9.611595428725214e-06, 'total_occurences': 30},
      'allow': {'idf': 5.637137461597001e-06, 'total_occurences': 41},
      'analysis': {'idf': 1.0673281254836331e-06, 'total_occurences': 25},
      'angle': {'idf': 4.3236325431174254e-06, 'total_occurences': 21},
      'appears': {'idf': 6.572720580502681e-06, 'total_occurences': 30},
      'approach': {'idf': 2.0491593323838894e-06, 'total_occurences': 22},
      'approximately': {'idf': 5.116398055768738e-06, 'total_occurences': 21},
      'array': {'idf': 7.107270026510117e-06, 'total_occurences': 30},
      'associated': {'idf': 2.172486758693206e-06, 'total_occurences': 67},
      'astronomy': {'idf': 2.182214948172395e-05, 'total_occurences': 23},
      'astrophysics': {'idf': 2.308242734805992e-05, 'total_occurences': 17},
      'band': {'idf': 2.856620503679327e-06, 'total_occurences': 23},
      'based': {'idf': 8.137385100122386e-07, 'total_occurences': 29},
      'beginning': {'idf': 2.3578788521845746e-05, 'total_occurences': 15},
      'berger': {'idf': 0.0008103727714748784, 'total_occurences': 20},
      'binary': {'idf': 7.5815011372251705e-06, 'total_occurences': 24},
      'blue': {'idf': 1.583857326132062e-05, 'total_occurences': 16},
      'break': {'idf': 1.5914950504503932e-05, 'total_occurences': 19},
      'bright': {'idf': 1.500487658489009e-05, 'total_occurences': 45},
      'broad': {'idf': 2.3276022593259263e-05, 'total_occurences': 15},
      'broadband': {'idf': 2.1555905240240565e-05, 'total_occurences': 16},
      'bursts': {'idf': 6.093288243000335e-05, 'total_occurences': 182},
      'candidate': {'idf': 0.00021263023601956197, 'total_occurences': 23},
      'case': {'idf': 1.8406037180195105e-06, 'total_occurences': 17},
      'central': {'idf': 2.1675047685104907e-05, 'total_occurences': 19},
      'chandra': {'idf': 4.1727519298977675e-05, 'total_occurences': 17},
      'characterize': {'idf': 1.0584922835912526e-05, 'total_occurences': 26},
      'circumburst': {'idf': 0.002173913043478261, 'total_occurences': 19},
      'classes': {'idf': 5.547727096208683e-06, 'total_occurences': 26},
      'clear': {'idf': 8.394050297149381e-06, 'total_occurences': 16},
      'cluster': {'idf': 1.76541204717181e-05, 'total_occurences': 23},
      'colors': {'idf': 4.5733101618951796e-05, 'total_occurences': 19},
      'combined': {'idf': 5.8290683982885856e-06, 'total_occurences': 40},
      'compact': {'idf': 8.777858729141614e-06, 'total_occurences': 19},
      'compared': {'idf': 9.055838298951334e-06, 'total_occurences': 51},
      'comparison': {'idf': 3.079083172194647e-06, 'total_occurences': 26},
      'complete': {'idf': 5.1099142556387905e-06, 'total_occurences': 15},
      'conclude': {'idf': 1.260096523393692e-05, 'total_occurences': 22},
      'confirmation': {'idf': 0.0015503875968992248, 'total_occurences': 43},
      'consistent': {'idf': 2.562768610184955e-06, 'total_occurences': 38},
      'constrain': {'idf': 1.849317601804934e-05, 'total_occurences': 31},
      'constraints': {'idf': 2.327584200358448e-05, 'total_occurences': 30},
      'continue': {'idf': 3.607243344636029e-05, 'total_occurences': 29},
      'contribution': {'idf': 5.617030837499298e-06, 'total_occurences': 16},
      'correlation': {'idf': 8.898538859919202e-06, 'total_occurences': 22},
      'corresponding': {'idf': 3.04700616409347e-06, 'total_occurences': 15},
      'cosmic': {'idf': 1.0035827905623074e-05, 'total_occurences': 23},
      'cosmological': {'idf': 0.0005861664712778429, 'total_occurences': 28},
      'counterparts': {'idf': 4.190763557120107e-05, 'total_occurences': 28},
      'current': {'idf': 9.372334742307657e-06, 'total_occurences': 34},
      'curves': {'idf': 7.676953784738215e-06, 'total_occurences': 41},
      'data': {'idf': 6.741429115558206e-07, 'total_occurences': 58},
      'date': {'idf': 1.7451093310995933e-05, 'total_occurences': 31},
      'days': {'idf': 8.99911808642753e-06, 'total_occurences': 52},
      'death': {'idf': 0.0005099439061703213, 'total_occurences': 18},
      'decade': {'idf': 2.8133352088901393e-05, 'total_occurences': 15},
      'deep': {'idf': 5.775072477159589e-06, 'total_occurences': 26},
      'demonstrated': {'idf': 1.7125340366139776e-05, 'total_occurences': 16},
      'density': {'idf': 1.3833191082018374e-06, 'total_occurences': 52},
      'derived': {'idf': 6.766037199672524e-06, 'total_occurences': 25},
      'detailed': {'idf': 7.081902198930633e-06, 'total_occurences': 29},
      'detected': {'idf': 4.689727620619794e-06, 'total_occurences': 150},
      'determine': {'idf': 2.0272502985126065e-06, 'total_occurences': 45},
      'different': {'idf': 9.330133719476467e-07, 'total_occurences': 38},
      'direct': {'idf': 2.2974245870379304e-05, 'total_occurences': 15},
      'discovered': {'idf': 1.3235741797149022e-05, 'total_occurences': 20},
      'discovery': {'idf': 1.5598190609889253e-05, 'total_occurences': 53},
      'discuss': {'idf': 3.4037570670506104e-06, 'total_occurences': 25},
      'distance': {'idf': 1.156336725254394e-05, 'total_occurences': 16},
      'distant': {'idf': 3.176115610608226e-05, 'total_occurences': 16},
      'distinguish': {'idf': 2.56160663968441e-05, 'total_occurences': 19},
      'distribution': {'idf': 1.6417557592792035e-06, 'total_occurences': 29},
      'dominated': {'idf': 9.04216360890834e-06, 'total_occurences': 17},
      'during': {'idf': 1.2942085461767138e-06, 'total_occurences': 23},
      'dust': {'idf': 1.0088883059756454e-05, 'total_occurences': 15},
      'dwarf': {'idf': 4.1511000415110007e-05, 'total_occurences': 40},
      'early': {'idf': 5.01474334543558e-06, 'total_occurences': 35},
      'effects': {'idf': 1.3726892492350689e-06, 'total_occurences': 23},
      'ejecta': {'idf': 6.893223960846487e-05, 'total_occurences': 29},
      'emission': {'idf': 2.42844584322925e-06, 'total_occurences': 96},
      'end': {'idf': 6.151612645254953e-06, 'total_occurences': 19},
      'energetic': {'idf': 1.7136785824450765e-05, 'total_occurences': 24},
      'energy': {'idf': 8.216683812146888e-07, 'total_occurences': 68},
      'engine': {'idf': 4.773953310736621e-05, 'total_occurences': 15},
      'environments': {'idf': 5.545204507142223e-06, 'total_occurences': 52},
      'epoch': {'idf': 8.08865162177465e-05, 'total_occurences': 25},
      'erg': {'idf': 7.266913741733885e-05, 'total_occurences': 37},
      'error': {'idf': 4.819044865307696e-05, 'total_occurences': 15},
      'estimated': {'idf': 4.498769586518087e-06, 'total_occurences': 37},
      'even': {'idf': 2.942569863964995e-06, 'total_occurences': 21},
      'events': {'idf': 4.845195988177722e-06, 'total_occurences': 86},
      'evidence': {'idf': 3.3637981317465176e-06, 'total_occurences': 51},
      'evolution': {'idf': 2.781703069887508e-06, 'total_occurences': 37},
      'exhibit': {'idf': 6.005897791631382e-06, 'total_occurences': 35},
      'existing': {'idf': 5.0091416835725195e-06, 'total_occurences': 26},
      'expected': {'idf': 3.893899038985717e-06, 'total_occurences': 32},
      'explained': {'idf': 6.325390751013644e-06, 'total_occurences': 22},
      'exploring': {'idf': 1.1621285546607165e-05, 'total_occurences': 23},
      'explosion': {'idf': 4.3605284960537216e-05, 'total_occurences': 69},
      'extended': {'idf': 4.7420783581027895e-06, 'total_occurences': 27},
      'extreme': {'idf': 1.3300171572213281e-05, 'total_occurences': 24},
      'facilities': {'idf': 3.513086246267346e-05, 'total_occurences': 24},
      'factor': {'idf': 3.472258391580468e-06, 'total_occurences': 25},
      'features': {'idf': 2.9082871642746007e-06, 'total_occurences': 24},
      'few': {'idf': 4.052684903748733e-06, 'total_occurences': 22},
      'field': {'idf': 8.167005461276552e-07, 'total_occurences': 48},
      'find': {'idf': 2.2387742263355965e-06, 'total_occurences': 72},
      'first': {'idf': 1.2693254804396944e-05, 'total_occurences': 69},
      'fit': {'idf': 9.507149376331e-06, 'total_occurences': 28},
      'flares': {'idf': 3.772161448509996e-05, 'total_occurences': 21},
      'flux': {'idf': 3.5859645348107507e-06, 'total_occurences': 39},
      'follow-up': {'idf': 6.855419208884623e-05, 'total_occurences': 31},
      'following': {'idf': 4.5077533357374685e-06, 'total_occurences': 17},
      'formation': {'idf': 2.076981232397584e-06, 'total_occurences': 42},
      'forming': {'idf': 1.006248805079544e-05, 'total_occurences': 17},
      'fraction': {'idf': 6.371212314279161e-06, 'total_occurences': 33},
      'frequency': {'idf': 1.9897092238940203e-06, 'total_occurences': 17},
      'fully': {'idf': 7.044734061289187e-06, 'total_occurences': 18},
      'galactic': {'idf': 8.657783780507866e-06, 'total_occurences': 17},
      'galaxy': {'idf': 7.208090360620761e-06, 'total_occurences': 172},
      'gamma': {'idf': 1.1912136084242627e-05, 'total_occurences': 28},
      'gamma-ray': {'idf': 3.2772916461835936e-05, 'total_occurences': 131},
      'gemini': {'idf': 0.00017667844522968197, 'total_occurences': 49},
      'generally': {'idf': 6.583408493913639e-06, 'total_occurences': 30},
      'ghz': {'idf': 1.3365410318096765e-05, 'total_occurences': 22},
      'halpha': {'idf': 3.4627237785241874e-05, 'total_occurences': 22},
      'high': {'idf': 6.496788637376544e-07, 'total_occurences': 49},
      'high-redshift': {'idf': 0.00012377769525931428, 'total_occurences': 20},
      'highest': {'idf': 1.0300146262076921e-05, 'total_occurences': 24},
      'host': {'idf': 1.3893713094824592e-05, 'total_occurences': 137},
      'hubble': {'idf': 2.7694693696687715e-05, 'total_occurences': 20},
      'ibc': {'idf': 0.00425531914893617, 'total_occurences': 18},
      'identification': {'idf': 1.1792174712860546e-05, 'total_occurences': 22},
      'identify': {'idf': 7.481278101550869e-06, 'total_occurences': 47},
      'imaging': {'idf': 1.0295798284720005e-05, 'total_occurences': 72},
      'implications': {'idf': 1.804760959410926e-05, 'total_occurences': 20},
      'important': {'idf': 1.910099267858951e-06, 'total_occurences': 19},
      'including': {'idf': 2.230479954676647e-06, 'total_occurences': 29},
      'increase': {'idf': 2.4791257610916086e-06, 'total_occurences': 15},
      'indicating': {'idf': 9.646271233854553e-06, 'total_occurences': 68},
      'inferred': {'idf': 1.547269070091289e-05, 'total_occurences': 35},
      'interpret': {'idf': 8.094413235984523e-06, 'total_occurences': 17},
      'jet': {'idf': 2.4219525781685195e-05, 'total_occurences': 35},
      'keck': {'idf': 0.0005078720162519045, 'total_occurences': 17},
      'kinetic': {'idf': 8.098083993327179e-06, 'total_occurences': 15},
      'known': {'idf': 2.4791441994223595e-06, 'total_occurences': 30},
      'lack': {'idf': 1.3805099603793642e-05, 'total_occurences': 25},
      'large': {'idf': 1.0271475086537178e-06, 'total_occurences': 57},
      'larger': {'idf': 3.4893539810039567e-06, 'total_occurences': 18},
      'late-time': {'idf': 0.001652892561983471, 'total_occurences': 18},
      'leading': {'idf': 4.737966748949356e-06, 'total_occurences': 17},
      'level': {'idf': 2.068603155033532e-06, 'total_occurences': 18},
      'light': {'idf': 6.720610769106697e-06, 'total_occurences': 63},
      'likely': {'idf': 6.278843437038897e-06, 'total_occurences': 26},
      'limit': {'idf': 3.58512888538343e-05, 'total_occurences': 68},
      'lines': {'idf': 2.6457756223525707e-06, 'total_occurences': 41},
      'local': {'idf': 9.62213861652891e-06, 'total_occurences': 45},
      'located': {'idf': 0.0004149377593360996, 'total_occurences': 27},
      'long': {'idf': 2.142548003788025e-06, 'total_occurences': 23},
      'long-duration': {'idf': 0.00031436655139893113, 'total_occurences': 26},
      'low': {'idf': 9.672124646604745e-07, 'total_occurences': 34},
      'lower': {'idf': 5.804841237592152e-05, 'total_occurences': 21},
      'luminosity': {'idf': 4.485310607759587e-05, 'total_occurences': 71},
      'luminous': {'idf': 0.0001547029702970297, 'total_occurences': 29},
      'mag': {'idf': 3.0627871362940275e-05, 'total_occurences': 53},
      'magellan': {'idf': 0.000774593338497289, 'total_occurences': 28},
      'magnetic': {'idf': 1.437016982666701e-06, 'total_occurences': 19},
      'magnitude': {'idf': 3.6070741939090947e-06, 'total_occurences': 42},
      'many': {'idf': 2.3158816213950408e-06, 'total_occurences': 22},
      'mass': {'idf': 6.89684900211216e-06, 'total_occurences': 49},
      'massive': {'idf': 1.0130070100085092e-05, 'total_occurences': 54},
      'maximum': {'idf': 3.0931597864482485e-06, 'total_occurences': 17},
      'measure': {'idf': 4.321763971182478e-06, 'total_occurences': 73},
      'mechanism': {'idf': 5.140595280933532e-06, 'total_occurences': 31},
      'medium': {'idf': 4.977031001926111e-06, 'total_occurences': 37},
      'mergers': {'idf': 7.536930961712391e-05, 'total_occurences': 23},
      'metallicity': {'idf': 0.00019727756954034326, 'total_occurences': 35},
      'model': {'idf': 1.649095553543659e-06, 'total_occurences': 84},
      'months': {'idf': 2.0127609041321982e-05, 'total_occurences': 17},
      'more': {'idf': 1.1034994173523077e-06, 'total_occurences': 35},
      'most': {'idf': 1.5216207086491965e-06, 'total_occurences': 47},
      'multiwavelength': {'idf': 6.384065372829418e-05, 'total_occurences': 17},
      'narrow': {'idf': 8.877604467210569e-06, 'total_occurences': 21},
      'nature': {'idf': 4.082382478414403e-06, 'total_occurences': 38},
      'near-infrared': {'idf': 3.1425788001634144e-05, 'total_occurences': 22},
      'nearby': {'idf': 1.5948708952010335e-05, 'total_occurences': 24},
      'new': {'idf': 1.0417838673517437e-06, 'total_occurences': 41},
      'normal': {'idf': 4.252749402488709e-06, 'total_occurences': 17},
      'object': {'idf': 6.48643038762908e-06, 'total_occurences': 51},
      'observations': {'idf': 8.641809940673975e-07, 'total_occurences': 366},
      'observatory': {'idf': 5.8878944889307586e-05, 'total_occurences': 35},
      'occur': {'idf': 5.307461228995722e-06, 'total_occurences': 21},
      'offset': {'idf': 3.0450669914738125e-05, 'total_occurences': 16},
      'one': {'idf': 7.968159235694166e-07, 'total_occurences': 30},
      'opening': {'idf': 3.464283239797686e-05, 'total_occurences': 28},
      'opportunity': {'idf': 2.355268736162796e-05, 'total_occurences': 21},
      'optical': {'idf': 1.5711289346960258e-06, 'total_occurences': 154},
      'order': {'idf': 1.178439275024158e-05, 'total_occurences': 27},
      'origin': {'idf': 5.2968907251443404e-05, 'total_occurences': 41},
      'outflow': {'idf': 3.558845510516389e-05, 'total_occurences': 15},
      'over': {'idf': 1.2905323962347426e-06, 'total_occurences': 33},
      'pan-starrs': {'idf': 0.006802721088435374, 'total_occurences': 24},
      'parameters': {'idf': 1.5852910356547807e-06, 'total_occurences': 22},
      'past': {'idf': 8.076500613814046e-06, 'total_occurences': 18},
      'peak': {'idf': 4.370648473113956e-06, 'total_occurences': 30},
      'period': {'idf': 3.4346320822113536e-06, 'total_occurences': 24},
      'photometric': {'idf': 1.8834519908087542e-05, 'total_occurences': 25},
      'photometry': {'idf': 2.1190032208848956e-05, 'total_occurences': 17},
      'physics': {'idf': 5.125497813975182e-06, 'total_occurences': 40},
      'place': {'idf': 5.127679212388473e-05, 'total_occurences': 27},
      'point': {'idf': 6.500724830818636e-06, 'total_occurences': 16},
      'population': {'idf': 1.7064555212368388e-05, 'total_occurences': 51},
      'position': {'idf': 5.304083083157415e-06, 'total_occurences': 47},
      'potential': {'idf': 1.8172071343552094e-06, 'total_occurences': 16},
      'powered': {'idf': 2.2911764503719725e-06, 'total_occurences': 21},
      'predicted': {'idf': 5.415885876452811e-06, 'total_occurences': 27},
      'present': {'idf': 9.514856496934313e-07, 'total_occurences': 93},
      'previous': {'idf': 3.1353466439249525e-06, 'total_occurences': 24},
      'previously': {'idf': 3.9521161605981925e-06, 'total_occurences': 16},
      'probe': {'idf': 3.171783811215428e-05, 'total_occurences': 38},
      'process': {'idf': 1.5978014252388713e-05, 'total_occurences': 16},
      'produce': {'idf': 5.436200748021223e-06, 'total_occurences': 29},
      'progenitor': {'idf': 0.0005521811154058532, 'total_occurences': 88},
      'program': {'idf': 6.183450612779956e-06, 'total_occurences': 27},
      'prompt': {'idf': 8.457374830852504e-05, 'total_occurences': 17},
      'properties': {'idf': 1.1774984751394746e-06, 'total_occurences': 53},
      'propose': {'idf': 5.135711167603934e-06, 'total_occurences': 50},
      'provide': {'idf': 2.1521204843131937e-06, 'total_occurences': 83},
      'radio': {'idf': 1.2309661853588882e-05, 'total_occurences': 132},
      'range': {'idf': 1.1313330610591767e-06, 'total_occurences': 69},
      'rapid': {'idf': 3.6477712117895966e-05, 'total_occurences': 28},
      'rate': {'idf': 3.961933740620122e-06, 'total_occurences': 51},
      'ratio': {'idf': 2.752417310502949e-06, 'total_occurences': 18},
      'recent': {'idf': 2.278184560287598e-06, 'total_occurences': 42},
      'redshift': {'idf': 1.5993858358390378e-05, 'total_occurences': 161},
      'regions': {'idf': 1.647902549634825e-06, 'total_occurences': 20},
      'reionization': {'idf': 0.00021172983273343214, 'total_occurences': 27},
      'relation': {'idf': 5.145753466951399e-06, 'total_occurences': 29},
      'relative': {'idf': 4.70962789230023e-06, 'total_occurences': 25},
      'relativistic': {'idf': 9.455817691834902e-06, 'total_occurences': 24},
      'release': {'idf': 1.5222090297439645e-05, 'total_occurences': 19},
      'remain': {'idf': 9.856489512695158e-06, 'total_occurences': 34},
      'report': {'idf': 3.123057848400526e-06, 'total_occurences': 62},
      'represent': {'idf': 1.3688879154574824e-05, 'total_occurences': 15},
      'request': {'idf': 0.00012330456226880394, 'total_occurences': 15},
      'required': {'idf': 4.375620791199751e-06, 'total_occurences': 36},
      'respectively': {'idf': 2.978725939341225e-06, 'total_occurences': 24},
      'results': {'idf': 1.9188296674092536e-06, 'total_occurences': 78},
      'reveal': {'idf': 7.294956996228507e-06, 'total_occurences': 43},
      'rotation': {'idf': 6.7581723198778124e-06, 'total_occurences': 15},
      'rule': {'idf': 1.8581834398691837e-05, 'total_occurences': 16},
      'sample': {'idf': 3.352610845696086e-06, 'total_occurences': 50},
      'scale': {'idf': 2.177022399383467e-06, 'total_occurences': 15},
      'science': {'idf': 7.110403230967228e-06, 'total_occurences': 16},
      'search': {'idf': 4.466080121477379e-05, 'total_occurences': 27},
      'seen': {'idf': 6.7498245045628815e-06, 'total_occurences': 16},
      'set': {'idf': 2.6335887914461036e-06, 'total_occurences': 21},
      'several': {'idf': 1.7022232738179336e-06, 'total_occurences': 33},
      'shock': {'idf': 2.4456455281371517e-05, 'total_occurences': 17},
      'short': {'idf': 2.139266231682533e-05, 'total_occurences': 21},
      'short-duration': {'idf': 0.0005797101449275362, 'total_occurences': 21},
      'sigma': {'idf': 0.00034638032559750607, 'total_occurences': 18},
      'significant': {'idf': 2.2501237568066244e-06, 'total_occurences': 28},
      'significantly': {'idf': 3.626341746446185e-06, 'total_occurences': 18},
      'similar': {'idf': 2.1792142189369357e-06, 'total_occurences': 54},
      'sky': {'idf': 1.3933010087499304e-05, 'total_occurences': 15},
      'small': {'idf': 1.5218059575659627e-06, 'total_occurences': 16},
      'sne': {'idf': 0.00014126289023873428, 'total_occurences': 26},
      'source': {'idf': 3.5803667011575327e-06, 'total_occurences': 61},
      'space': {'idf': 1.7326007897194399e-06, 'total_occurences': 30},
      'spectra': {'idf': 2.2368612363132054e-06, 'total_occurences': 32},
      'spectral': {'idf': 2.863270255489605e-06, 'total_occurences': 25},
      'spectroscopic': {'idf': 9.33663227673778e-06, 'total_occurences': 40},
      'spectroscopy': {'idf': 3.501756130699546e-06, 'total_occurences': 60},
      'spectrum': {'idf': 2.65093074178344e-06, 'total_occurences': 41},
      'spitzer': {'idf': 0.0001235712079085573, 'total_occurences': 16},
      'standard': {'idf': 3.4420564221888724e-06, 'total_occurences': 22},
      'star': {'idf': 4.610504573620537e-06, 'total_occurences': 120},
      'star-forming': {'idf': 0.00020161290322580645, 'total_occurences': 40},
      'stellar': {'idf': 6.998586285570314e-06, 'total_occurences': 45},
      'strong': {'idf': 2.3674242424242424e-05, 'total_occurences': 30},
      'structure': {'idf': 1.0907349699447978e-06, 'total_occurences': 19},
      'study': {'idf': 2.702359430018349e-06, 'total_occurences': 87},
      'supernova': {'idf': 7.8125e-05, 'total_occurences': 117},
      'support': {'idf': 5.768775923436806e-06, 'total_occurences': 17},
      'survey': {'idf': 1.9151951583866395e-05, 'total_occurences': 49},
      'swift': {'idf': 8.692628650904033e-05, 'total_occurences': 32},
      'systematic': {'idf': 1.997722596240286e-05, 'total_occurences': 15},
      'systems': {'idf': 8.739944693629978e-07, 'total_occurences': 36},
      'telescope': {'idf': 6.145072880564363e-06, 'total_occurences': 70},
      'test': {'idf': 3.6040451803103805e-06, 'total_occurences': 17},
      'theoretical': {'idf': 2.520891891551231e-06, 'total_occurences': 16},
      'three': {'idf': 1.265564865894419e-06, 'total_occurences': 34},
      'through': {'idf': 1.4497207837770445e-06, 'total_occurences': 28},
      'time': {'idf': 3.453861589950644e-06, 'total_occurences': 78},
      'total': {'idf': 2.6782294760579678e-06, 'total_occurences': 20},
      'transient': {'idf': 1.0028581457152886e-05, 'total_occurences': 53},
      'two': {'idf': 5.383275777143149e-07, 'total_occurences': 57},
      'type': {'idf': 1.7535922336907155e-06, 'total_occurences': 57},
      'typical': {'idf': 6.035257977102231e-06, 'total_occurences': 27},
      'uncertainty': {'idf': 1.2774165527636907e-05, 'total_occurences': 16},
      'underlying': {'idf': 9.941543722909294e-06, 'total_occurences': 21},
      'understanding': {'idf': 4.375754817706054e-06, 'total_occurences': 20},
      'unique': {'idf': 6.934572310252765e-06, 'total_occurences': 32},
      'universe': {'idf': 1.2829230117900625e-05, 'total_occurences': 51},
      'unprecedented': {'idf': 4.3393360815795186e-05, 'total_occurences': 15},
      'unusually': {'idf': 5.912262031453234e-05, 'total_occurences': 30},
      'upper': {'idf': 2.8440601803134153e-05, 'total_occurences': 18},
      'use': {'idf': 1.4182948691764812e-06, 'total_occurences': 31},
      'value': {'idf': 2.399658288659695e-06, 'total_occurences': 15},
      'variable': {'idf': 7.357972731353058e-06, 'total_occurences': 30},
      'velocity': {'idf': 8.694896095991653e-06, 'total_occurences': 29},
      'wave': {'idf': 1.966382721001754e-06, 'total_occurences': 22},
      'wavelengths': {'idf': 4.5582591096808305e-06, 'total_occurences': 19},
      'weeks': {'idf': 6.070908207867897e-05, 'total_occurences': 16},
      'well': {'idf': 9.006469346931902e-07, 'total_occurences': 41},
      'wide': {'idf': 3.76876373243285e-06, 'total_occurences': 36},
      'x-ray': {'idf': 2.6988950723573768e-06, 'total_occurences': 63},
      'years': {'idf': 3.6110599544284236e-06, 'total_occurences': 23},
      'yield': {'idf': 6.692634086923931e-06, 'total_occurences': 15}
    };



    describe("WordCloud Widget (Visualization Widget)", function(){

      beforeEach(function(){
        w = new WordCloud();

        $("#test").append(w.render().el);

        w.model.set("tfidfData", fakeWordCloudData);


      })

      afterEach(function(){
        $("#test").empty()
      })


      it("should request wordcloud data from pubsub")

      it("should consist of a controller, a word cloud view, and a list view, (the views have corresponding models)", function(){

        expect(w).to.be.instanceof(BaseWidget);
        expect(w.view).to.be.instanceof(Backbone.View);
        expect(w.view.model).to.be.instanceof(Backbone.Model);

        expect(w.listView).to.be.instanceof(Backbone.View);
        expect(w.listView.model).to.be.instanceof(Backbone.Model);

      })

      it("should have a buildWCDict function on the word cloud model that listens for slider changes and re-processes keyword dict", function(){

        //resetting slider, recomputing processedWordList

        w.model.set("currentSliderVal", 1)

        var processed = w.model.get("processedWordList")

        expect(_.findWhere(processed, {text: "reionization"}).origSize).to.eql(1.324118996861761)

        expect(_.findWhere(processed, {text: "reionization"}).size).to.eql(21)

        //resetting slider, recomputing processedWordList

        w.model.set("currentSliderVal", 5)

        var processed = w.model.get("processedWordList")

        expect(_.findWhere(processed, {text: "reionization"})).to.eql(undefined);

        expect(_.findWhere(processed, {text: "imaging"}).size).to.eql(19);

        expect(_.findWhere(processed, {text: "imaging"}).origSize).to.eql(1.937015350530147);


      })

      it("should have a draw function on the word cloud view that takes care of the word cloud rendering", function(){

        expect(w.view.draw).to.be.instanceof(Function)

        //how to best test the layout? leave this for later

      })

      it("should have a list view that listens for click events on word cloud words and adds/removes the word from the list", function(){

        w.listView.model.trigger("selected", "optical");

        expect(w.listView.model.get("selectedWords")[0]).to.eql("optical")

        w.listView.model.trigger("unselected", "optical");

        expect(w.listView.model.get("selectedWords")[0]).to.eql(undefined)

      })


      it("should have a submit button that emits a start_search with the new filter", function(){

        w.pubsub = {publish : function(){}}

        var pubsubStub = sinon.stub(w.pubsub, "publish")

        //for testing
        w.getCurrentQuery = function(){return new ApiQuery()};

        w.listView.model.set("selectedWords",["fakeA", "fakeB"]);

        //clicking submit button

        $(".apply-vis-facet").click();

        expect(pubsubStub.args[0][1].get("q")).to.eql(["undefined AND (fakeA OR fakeB)"])








      })






    })



})