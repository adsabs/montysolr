define([
  'js/widgets/bubble_chart/widget',
    'js/components/api_response',
    'js/components/api_query',
    'js/bugutils/minimal_pubsub'
],
  function(
  BubbleChart,
  ApiResponse,
  ApiQuery,
  MinSub
  ){

   var data =  {
     "responseHeader":{
       "status":0,
       "QTime":1478,
       "params":{
         "fl":"bibcode,title,pubdate,citation_count,read_count",
         "indent":"true",
         "q":"\"brown dwarf\"",
         "wt":"json",
         "rows":"1000"}},
     "response":{"numFound":14758,"start":0,"docs":[
       {
         "pubdate":"2000-07-00",
         "bibcode":"2000hst..prop.8482L",
         "citation_count":0,
         "read_count":0,
         "title":["STIS Observations of a Brown Dwarf"]},

       {
         "pubdate":"1999-01-00",
         "bibcode":"1999AJ....117..343R",
         "citation_count":44,
         "read_count":3.0,
         "title":["Brown Dwarfs in the Hyades and Beyond?"]},
       {
         "pubdate":"1998-06-00",
         "bibcode":"1998AsNow..12...24M",
         "read_count":7,
         citation_count: 0,
         "title":["Brown dwarfs."]},
       {
         "read_count":0.0,
         "pubdate":"1994-00-00",
         "bibcode":"1994coun.conf...13F",
         "title":["Brown dwarfs"]},
       {
         "read_count":0.0,
         "pubdate":"1994-00-00",
         "bibcode":"1994rppp.conf...43D",
         "title":["Brown Dwarfs"]},
       {
         "read_count":0.0,
         "pubdate":"1989-09-00",
         "bibcode":"1989SciAm.261c..28J",
         "title":["Brown Dwarfs Here..."]},
       {
         "pubdate":"1997-12-00",
         "bibcode":"1997ConPh..38..395J",
         "citation_count":2,
         "read_count":0.0,
         "title":["Brown dwarfs."]},
       {
         "pubdate":"1989-00-00",
         "bibcode":"1989GriO...53a...9K",
         "citation_count":0,
         "read_count":0.0,
         "title":["Brown dwarf found."]},
       {
         "read_count":0.0,
         "pubdate":"2005-11-00",
         "bibcode":"2005ccsf.conf..177R",
         "citation_count":0,
         "title":["Brown Dwarfs"]},
       {
         "pubdate":"2003-06-00",
         "bibcode":"2003IAUS..211.....M",
         "citation_count":0,
         "read_count":0.0,
         "title":["Brown Dwarfs"]},
       {
         "read_count":0.0,
         "pubdate":"2007-00-00",
         "bibcode":"2007SchpJ...2.4475A",
         "citation_count":0,
         "title":["Brown dwarfs"]},
       {
         "read_count":0.0,
         "pubdate":"1985-03-00",
         "bibcode":"1985Sci...227.1154G",
         "citation_count":30,
         "title":["Brown Dwarfs"]},
       {
         "read_count":0.0,
         "bibcode":"1989SciAm.261Q..28.",
         "pubdate":"1989-09-00",
         "title":["Brown Dwarfs"]},
       {
         "read_count":0.0,
         "pubdate":"1989-11-00",
         "bibcode":"1989S&T....78..482F",
         "title":["Brown Dwarfs Coming and Going"]},
       {
         "pubdate":"1989-11-00",
         "bibcode":"1989S&T....78..482T",
         "citation_count":0,
         "read_count":0.0,
         "title":["Brown dwarfs coming and going."]},
       {
         "read_count":2.0,
         "pubdate":"2010-00-00",
         "bibcode":"2010fee..book..145L",
         "citation_count":0,
         "title":["Brown Dwarfs"]},
       {
         "read_count":0.0,
         "pubdate":"2013-00-00",
         "bibcode":"2013pss4.book..337R",
         "citation_count":0,
         "title":["Brown Dwarfs"]}]
     }};

   var dataOneYear = {
     "responseHeader":{
       "status":0,
       "QTime":108,
       "params":{
         "fl":"pubdate,citation_count,read_count,bibcode,title",
         "sort":"pubdate desc",
         "indent":"true",
         "q":"planet",
         "wt":"json",
         "rows":"1000"}},
     "response":{"numFound":565460,"start":0,"docs":[
       {
         "pubdate":"2015-10-00",
         "bibcode":"2015cshn.conv..194S",
         "citation_count":0,
         "read_count":11.0,
         "title":["Dynamically generated resonances from the vector octet-baryon octet interaction with strangeness zero"]},
       {
         "read_count":0.0,
         "pubdate":"2015-05-00",
         "bibcode":"2015CNSNS..22....1G",
         "title":["Preface"]},
       {
         "read_count":0.0,
         "pubdate":"2015-05-00",
         "bibcode":"2015CNSNS..22...13D",
         "title":["Sensitivity and resilience of the climate system: A conditional nonlinear optimization approach"]},
       {
         "read_count":0.0,
         "pubdate":"2015-05-00",
         "bibcode":"2015CNSNS..22..396T",
         "title":["Pseudo Phase Plane and Fractional Calculus modeling of western global economic downturn"]},
       {
         "read_count":0.0,
         "pubdate":"2015-05-00",
         "bibcode":"2015CNSNS..22..526C",
         "title":["Enthalpy balance methods versus temperature models in ice sheets"]},
       {
         "pubdate":"2015-05-00",
         "read_count":18.0,
         "bibcode":"2015CNSNS..22...70S",
         "citation_count":0,
         "title":["Arctic melt ponds and bifurcations in the climate system"]},
       {
         "pubdate":"2015-05-00",
         "read_count":46.0,
         "bibcode":"2015CNSNS..22.1360M",
         "citation_count":0,
         "title":["Dust-acoustic solitary waves in a magnetized dusty plasma with nonthermal electrons and trapped ions"]},
       {
         "read_count":0.0,
         "pubdate":"2015-04-00",
         "bibcode":"2015IJAEO..36...69M",
         "title":["Monitoring land-use change by combining participatory land-use maps with standard remote sensing techniques: Showcase from a remote forest catchment on Mindanao, Philippines"]},
       {
         "read_count":0.0,
         "pubdate":"2015-04-00",
         "bibcode":"2015JMS...144....1F",
         "title":["Anaerobic nitrification-denitrification mediated by Mn-oxides in meso-tidal sediments: Implications for N<SUB>2</SUB> and N<SUB>2</SUB>O production"]},
       {
         "read_count":3.0,
         "pubdate":"2015-04-00",
         "bibcode":"2015Icar..250....7J",
         "citation_count":0,
         "title":["Study of phyllosilicates and carbonates from the Capri Chasma region of Valles Marineris on Mars based on Mars Reconnaissance Orbiter-Compact Reconnaissance Imaging Spectrometer for Mars (MRO-CRISM) observations"]},
       {
         "read_count":7.0,
         "pubdate":"2015-04-00",
         "bibcode":"2015Icar..250..262L",
         "citation_count":0,
         "title":["Meteorological insights from planetary heat flow measurements"]},
       {
         "pubdate":"2015-04-00",
         "read_count":158.0,
         "bibcode":"2015Icar..250..249L",
         "citation_count":0,
         "title":["The effect of Poynting-Robertson drag on the triangular Lagrangian points"]},
       {
         "read_count":10.0,
         "pubdate":"2015-04-00",
         "bibcode":"2015Icar..250..395D",
         "citation_count":0,
         "title":["Insolation patterns on eccentric exoplanets"]},
       {
         "read_count":7.0,
         "pubdate":"2015-04-00",
         "bibcode":"2015Icar..250..294K",
         "citation_count":0,
         "title":["Uranus' southern circulation revealed by Voyager 2: Unique characteristics"]},
       {
         "read_count":0.0,
         "pubdate":"2015-04-00",
         "bibcode":"2015Icar..250..438K",
         "citation_count":0,
         "title":["The resurfacing history of Venus: Constraints from buffered crater densities"]},
       {
         "read_count":7.0,
         "pubdate":"2015-04-00",
         "bibcode":"2015Icar..250..222M",
         "citation_count":0,
         "title":["Compaction of porous ices rich in water by swift heavy ions"]},
       {
         "read_count":14.0,
         "pubdate":"2015-04-00",
         "bibcode":"2015NewA...36..139S",
         "citation_count":0,
         "title":["Reprint of: Multiwavelength modeling the SED of supersoft X-ray sources III. RS Ophiuchi: The supersoft X-ray phase and beyond"]},
       {
         "pubdate":"2015-04-00",
         "bibcode":"2015Icar..250..357C",
         "citation_count":0,
         "read_count":257.0,
         "title":["XUV-driven mass loss from extrasolar giant planets orbiting active stars"]},
       {
         "read_count":7.0,
         "pubdate":"2015-04-00",
         "bibcode":"2015Icar..250..400W",
         "citation_count":0,
         "title":["Diffusivity of heavy elements in Jupiter and Saturn"]},
       {
         "read_count":3.0,
         "pubdate":"2015-04-00",
         "bibcode":"2015Icar..250....1S",
         "citation_count":0,
         "title":["Dynamic water loss of antigorite by impact process"]},
       {
         "read_count":2.0,
         "pubdate":"2015-04-00",
         "bibcode":"2015Icar..250...18S",
         "citation_count":0,
         "title":["Volcanism-induced, local wet-based glacial conditions recorded in the Late Amazonian Arsia Mons tropical mountain glacier deposits"]},
       {
         "read_count":10.0,
         "pubdate":"2015-04-00",
         "bibcode":"2015Icar..250...32T",
         "citation_count":0,
         "title":["Keeping Enceladus warm"]},
       {
         "read_count":9.0,
         "pubdate":"2015-04-00",
         "bibcode":"2015Icar..250...43S",
         "citation_count":0,
         "title":["Extensive computation of albedo contrast between martian dust devil tracks and their neighboring regions"]},
       {
         "read_count":4.0,
         "pubdate":"2015-04-00",
         "bibcode":"2015Icar..250..204H",
         "citation_count":0,
         "title":["Visible-near infrared spectra of hydrous carbonates, with implications for the detection of carbonates in hyperspectral data of Mars"]},
       {
         "read_count":10.0,
         "pubdate":"2015-04-00",
         "bibcode":"2015Icar..250..215K",
         "citation_count":0,
         "title":["Crater-ray formation by impact-induced ejecta particles"]},
       {
         "read_count":5.0,
         "pubdate":"2015-04-00",
         "bibcode":"2015Icar..250..230K",
         "citation_count":0,
         "title":["Impact vaporization as a possible source of Mercury's calcium exosphere"]},
       {
         "read_count":12.0,
         "pubdate":"2015-04-00",
         "bibcode":"2015Icar..250..238N",
         "citation_count":0,
         "title":["Electrons on closed field lines of lunar crustal fields in the solar wind wake"]},
       {
         "pubdate":"2015-04-00",
         "read_count":95.0,
         "bibcode":"2015Icar..250..268C",
         "citation_count":0,
         "title":["Modelling circumplanetary ejecta clouds at low altitudes: A probabilistic approach"]},
       {
         "read_count":9.0,
         "pubdate":"2015-04-00",
         "bibcode":"2015Icar..250..280F",
         "citation_count":0,
         "title":["The potentially hazardous Asteroid (214869) 2007 PA8: An unweathered L chondrite analog surface"]},
       {
         "pubdate":"2015-04-00",
         "read_count":279.0,
         "bibcode":"2015Icar..250..287S",
         "citation_count":0,
         "title":["On the roles of escape erosion and the viscous relaxation of craters on Pluto"]},
       {
         "pubdate":"2015-04-00",
         "read_count":87.0,
         "bibcode":"2015APh....64....4A",
         "citation_count":0,
         "title":["Milagro limits and HAWC sensitivity for the rate-density of evaporating Primordial Black Holes"]},
       {
         "read_count":18.0,
         "pubdate":"2015-04-00",
         "bibcode":"2015NewA...36...26C",
         "citation_count":0,
         "title":["Analytical representations for simple and composite polytropes and their moments of inertia"]},
       {
         "pubdate":"2015-04-00",
         "read_count":56.0,
         "bibcode":"2015NewA...36..116S",
         "citation_count":3,
         "title":["Multiwavelength modelling the SED of supersoft X-ray sources I. The method and examples"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015JMMM..378..298P",
         "title":["Electrical, magnetic, and direct and converse magnetoelectric properties of (1-x)Pb(Zr<SUB>0.52</SUB>Ti<SUB>0.48</SUB>)O<SUB>3</SUB>-(x)CoFe<SUB>2</SUB>O<SUB>4</SUB> (PZT-CFO) magnetoelectric composites"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015SedG..316....1D",
         "title":["Provenance of sands from the confluence of the Amazon and Madeira rivers based on detrital heavy minerals and luminescence of quartz and feldspar"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015AtmRe.154...14W",
         "title":["Improvement of forecast skill for severe weather by merging radar-based extrapolation and storm-scale NWP corrected forecast"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015AtmRe.154...60C",
         "title":["Atmospheric conditions of thunderstorms in the European part of the Arctic derived from sounding and reanalysis data"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015IJAEO..35..329J",
         "title":["An assessment of a collaborative mapping approach for exploring land use patterns for several European metropolises"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015IJAEO..35..350D",
         "title":["Brown and green LAI mapping through spectral indices"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015IJAEO..35....1V",
         "title":["Introduction to the special issue on GOCE Earth science applications and models"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015AeoRe..16...35R",
         "title":["Dust-storm dynamics over Sistan region, Iran: Seasonality, transport characteristics and affected areas"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015IJAEO..35...96P",
         "citation_count":1,
         "title":["Geological appraisal over the Singhbhum-Orissa Craton, India using GOCE, EIGEN6-C2 and in situ gravity data"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015AtmRe.154...73A",
         "title":["Large-eddy simulation of the hurricane boundary layer: Evaluation of the planetary boundary-layer parametrizations"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015IJAEO..35..217P",
         "title":["Remote sensing and GIS analysis for mapping spatio-temporal changes of erosion and deposition of two Mediterranean river deltas: The case of the Axios and Aliakmonas rivers, Greece"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015JMS...143...62P",
         "title":["Contrasting optical properties of surface waters across the Fram Strait and its potential biological implications"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015AeoRe..16....1W",
         "title":["Regional transport of a chemically distinctive dust: Gypsum from White Sands, New Mexico (USA)"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015IJAEO..35....4V",
         "citation_count":3,
         "title":["GOCE data, models, and applications: A review"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015IJAEO..35...16B",
         "citation_count":3,
         "title":["GOCE gravity gradient data for lithospheric modeling"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015IJAEO..35...54F",
         "citation_count":3,
         "title":["Perturbing effects of sub-lithospheric mass anomalies in GOCE gravity gradient and other gravity data modelling: Application to the Atlantic-Mediterranean transition zone"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015IJAEO..35...70M",
         "citation_count":3,
         "title":["A refined model of sedimentary rock cover in the southeastern part of the Congo basin from GOCE gravity and vertical gravity gradient observations"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015JMMM..377...65F",
         "citation_count":0,
         "title":["Hybrid chitosan-Pluronic F-127 films with BaTiO<SUB>3</SUB>:Co nanoparticles: Synthesis and properties"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015AeoRe..16...75F",
         "title":["Late Amazonian aeolian features, gradation, wind regimes, and Sediment State in the Vicinity of the Mars Exploration Rover Opportunity, Meridiani Planum, Mars"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015MSSP...54..457H",
         "citation_count":0,
         "title":["Automatic fault feature extraction of mechanical anomaly on induction motor bearing using ensemble super-wavelet transform"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015AtmRe.155...13L",
         "title":["Long-term energy flux and radiation balance observations over Lake Ngoring, Tibetan Plateau"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015AtmRe.155...73L",
         "title":["Aircraft measurements of the vertical distribution and activation property of aerosol particles over the Loess Plateau in China"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015MSSP...54..277C",
         "title":["Planetary gearbox condition monitoring of ship-based satellite communication antennas using ensemble multiwavelet analysis method"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015AtmRe.154..116A",
         "title":["Effect of the temperature variation between Mediterranean Sea and Syrian deserts on the dust storm occurrence in the western half of Iran"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015AtmRe.155....1G",
         "title":["Optical properties of size-resolved particles at a Hong Kong urban site during winter"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015JMS...143...98J",
         "title":["Phytoplankton size structure in the southern Bay of Bengal modified by the Summer Monsoon Current and associated eddies: Implications on the vertical biogenic flux"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015MSSP...54..491B",
         "title":["Modelling of a chaotic load of wind turbines drivetrain"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015HEDP...14....1H",
         "citation_count":0,
         "title":["Electron-ion temperature equilibration in warm dense tantalum"]},
       {
         "read_count":13.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015Icar..248..392D",
         "citation_count":0,
         "title":["Mineralogy of Marcia, the youngest large crater of Vesta: Character and distribution of pyroxenes and hydrated material"]},
       {
         "read_count":6.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015APh....63....1D",
         "citation_count":0,
         "title":["Foreword: Dark energy and CMB"]},
       {
         "read_count":2.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015DyAtO..69....1I",
         "citation_count":0,
         "title":["Splitting phenomenon of a higher-order shallow water theory associated with a longitudinal planetary waves"]},
       {
         "read_count":13.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015Icar..248..135B",
         "citation_count":0,
         "title":["Corrigendum to “Comets formed in solar-nebula instabilities! - An experimental and modeling attempt to relate the activity of comets to their formation process” [Icarus 235 (2014) 156-169]"]},
       {
         "read_count":7.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015Icar..248...72B",
         "citation_count":0,
         "title":["A comparative study of iron abundance estimation methods: Application to the western nearside of the Moon"]},
       {
         "read_count":8.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015Icar..248..165J",
         "citation_count":0,
         "title":["Spatially resolved HST/STIS observations of Io's dayside equatorial atmosphere"]},
       {
         "read_count":8.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015Icar..248..221K",
         "citation_count":0,
         "title":["Corrigendum to “Relationship between regolith particle size and porosity on small bodies” [Icarus 239 (2014) 291-293]"]},
       {
         "read_count":7.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015Icar..248..407R",
         "citation_count":0,
         "title":["New insights into the Late Amazonian zonal shrinkage of the martian south polar plateau"]},
       {
         "read_count":4.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015Icar..248..526C",
         "citation_count":0,
         "title":["To what extent can intracrater layered deposits that lack clear sedimentary textures be used to infer depositional environments?"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015IJAEO..35..128G",
         "citation_count":2,
         "title":["Comparison of GGMs based on one year GOCE observations with the EGM08 and terrestrial data over the area of Sudan"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015IJAEO..35...88B",
         "citation_count":3,
         "title":["Exploration of tectonic structures with GOCE in Africa and across-continents"]},
       {
         "read_count":12.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015Icar..248..230P",
         "citation_count":0,
         "title":["Verifying single-station seismic approaches using Earth-based data: Preparation for data return from the InSight mission to Mars"]},
       {
         "read_count":3.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015DyAtO..69...26B",
         "citation_count":0,
         "title":["Temperature lapse rates at restricted thermodynamic equilibrium in the Earth system"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015JMMM..378...73S",
         "title":["Reversal modes in FeCoNi nanowire arrays: Correlation between magnetostatic interactions and nanowires length"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015IJAEO..35..338M",
         "citation_count":0,
         "title":["SAR interferometry and optical remote sensing for analysis of co-seismic deformation, source characteristics and mass wasting pattern of Lushan (China, April 2013) earthquake"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015JMMM..377..460L",
         "title":["Antiferromagnetic superexchange interaction between Fe<SUP>3+</SUP> ions in Fe<SUP>3+</SUP> doped wide-gap oxide of β-Ga<SUB>2-x</SUB>Fe<SUB>x</SUB>O<SUB>3</SUB> (x=0.1, 0.2, 0.3, 0.4)"]},
       {
         "read_count":6.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015Icar..248...25J",
         "citation_count":0,
         "title":["Spectrophotometric properties of materials observed by Pancam on the Mars Exploration Rovers: 3. Sols 500-1525"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015IJAEO..35...31R",
         "citation_count":2,
         "title":["GEMMA: An Earth crustal model based on GOCE satellite data"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015IJAEO..35..140B",
         "citation_count":3,
         "title":["A comparison of GOCE and drifter-based estimates of the North Atlantic steady-state surface circulation"]},
       {
         "read_count":10.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015Icar..248..190V",
         "citation_count":0,
         "title":["Expanded secondary craters in the Arcadia Planitia region, Mars: Evidence for tens of Myr-old shallow subsurface ice"]},
       {
         "read_count":7.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015Icar..248..205H",
         "citation_count":0,
         "title":["Thermal degradation of organics for pyrolysis in space: Titan's atmospheric aerosol case study"]},
       {
         "read_count":5.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015A&C.....9...34S",
         "citation_count":0,
         "title":["Earth conical shadow modeling for LEO satellite using reference frame transformation technique: A comparative study with existing earth conical shadow models"]},
       {
         "read_count":2.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015AeoRe..16...55C",
         "title":["An empirical equation to estimate mineral dust concentrations from visibility observations in Northern Africa"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015JPRS..101....1V",
         "title":["Canonical information analysis"]},
       {
         "read_count":9.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015Icar..248..448S",
         "citation_count":0,
         "title":["Spectral probing of impact-generated vapor in laboratory experiments"]},
       {
         "read_count":3.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015PEPI..240...70Y",
         "citation_count":0,
         "title":["Impact of phase change kinetics on the Mariana slab within the framework of 2-D mantle convection"]},
       {
         "read_count":5.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015PEPI..240...82J",
         "citation_count":0,
         "title":["Degradation of the mechanical properties imaged by seismic tomography during an EGS creation at The Geysers (California) and geomechanical modeling"]},
       {
         "read_count":12.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015Icar..248..340J",
         "citation_count":0,
         "title":["Gas phase dicyanoacetylene (C<SUB>4</SUB>N<SUB>2</SUB>) on Titan: New experimental and theoretical spectroscopy results applied to Cassini CIRS data"]},
       {
         "read_count":6.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015PEPI..240...43K",
         "citation_count":0,
         "title":["First-principles computation of mantle materials in crystalline and amorphous phases"]},
       {
         "read_count":9.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015Icar..248..162L",
         "citation_count":0,
         "title":["Solar panel clearing events, dust devil tracks, and in-situ vortex detections on Mars"]},
       {
         "read_count":13.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015Icar..248..269F",
         "citation_count":0,
         "title":["Embedded clays and sulfates in Meridiani Planum, Mars"]},
       {
         "read_count":11.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015Icar..248..357B",
         "citation_count":0,
         "title":["Lunar surface roughness derived from LRO Diviner Radiometer observations"]},
       {
         "read_count":14.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015Icar..248..222M",
         "citation_count":0,
         "title":["Space weathering and the color-color diagram of Plutinos and Jupiter Trojans"]},
       {
         "read_count":8.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015Icar..248..478G",
         "citation_count":0,
         "title":["Carbon monoxide and temperature in the upper atmosphere of Venus from VIRTIS/Venus Express non-LTE limb measurements"]},
       {
         "read_count":8.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015MNRAS.447.2170K",
         "citation_count":0,
         "title":["Gravitational lens modelling in a citizen science context"]},
       {
         "pubdate":"2015-03-00",
         "read_count":64.0,
         "bibcode":"2015Icar..248..463R",
         "citation_count":0,
         "title":["A passive probe for subsurface oceans and liquid water in Jupiter's icy moons"]},
       {
         "read_count":0.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015JMS...143...24P",
         "title":["Spatial and temporal variability of sea surface temperature and warming trends in the Yellow Sea"]},
       {
         "pubdate":"2015-03-00",
         "read_count":35.0,
         "bibcode":"2015APh....62...12A",
         "citation_count":2,
         "title":["Search for neutrino emission from relic dark matter in the sun with the Baikal NT200 detector"]},
       {
         "pubdate":"2015-03-00",
         "read_count":225.0,
         "bibcode":"2015MNRAS.448L..25N",
         "citation_count":0,
         "title":["Positive metallicity correlation for coreless giant planets"]},
       {
         "pubdate":"2015-03-00",
         "read_count":100.0,
         "bibcode":"2015Icar..248....1M",
         "citation_count":2,
         "title":["Titan's atmosphere as observed by Cassini/VIMS solar occultations: CH<SUB>4</SUB>, CO and evidence for C<SUB>2</SUB>H<SUB>6</SUB> absorption"]},
       {
         "pubdate":"2015-03-00",
         "read_count":145.0,
         "bibcode":"2015Icar..248...89R",
         "citation_count":0,
         "title":["Accretion and differentiation of the terrestrial planets with implications for the compositions of early-formed Solar System bodies and accretion of water"]},
       {
         "pubdate":"2015-03-00",
         "read_count":37.0,
         "bibcode":"2015Icar..248..109B",
         "citation_count":0,
         "title":["Tides on Europa: The membrane paradigm"]},
       {
         "pubdate":"2015-03-00",
         "read_count":61.0,
         "bibcode":"2015Icar..248..137H",
         "citation_count":0,
         "title":["Corrugations and eccentric spirals in Saturn's D ring: New insights into what happened at Saturn in 1983"]},
       {
         "read_count":11.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015Icar..248..213S",
         "citation_count":0,
         "title":["Venus' clouds as inferred from the phase curves acquired by IR1 and IR2 on board Akatsuki"]},
       {
         "read_count":12.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015Icar..248..243T",
         "citation_count":0,
         "title":["Non-detection of post-eclipse changes in Io's Jupiter-facing atmosphere: Evidence for volcanic support?"]},
       {
         "read_count":10.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015Icar..248..254D",
         "citation_count":0,
         "title":["Mercury's inner core size and core-crystallization regime"]},
       {
         "pubdate":"2015-03-00",
         "read_count":122.0,
         "bibcode":"2015Icar..248..289H",
         "citation_count":1,
         "title":["The main-belt comets: The Pan-STARRS1 perspective"]},
       {
         "pubdate":"2015-03-00",
         "read_count":174.0,
         "bibcode":"2015Icar..248..313S",
         "citation_count":0,
         "title":["Obliquities of “top-shaped” asteroids may not imply reshaping by YORP spin-up"]},
       {
         "pubdate":"2015-03-00",
         "read_count":156.0,
         "bibcode":"2015Icar..248..318Q",
         "citation_count":0,
         "title":["Dynamical evolution of the Earth-Moon progenitors - Whence Theia?"]},
       {
         "pubdate":"2015-03-00",
         "read_count":150.0,
         "bibcode":"2015Icar..248..347S",
         "citation_count":0,
         "title":["Component periods of non-principal-axis rotation and their manifestations in the lightcurves of asteroids and bare cometary nuclei"]},
       {
         "read_count":11.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015Icar..248..373C",
         "citation_count":0,
         "title":["Widespread surface weathering on early Mars: A case for a warmer and wetter climate"]},
       {
         "read_count":6.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015Icar..248..383I",
         "citation_count":0,
         "title":["Evidence for large reservoirs of water/mud in Utopia and Acidalia Planitiae on Mars"]},
       {
         "read_count":28.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015Icar..248..499B",
         "citation_count":0,
         "title":["Physical modeling of triple near-Earth Asteroid (153591) 2001 SN<SUB>263</SUB> from radar and optical light curve observations"]},
       {
         "pubdate":"2015-03-00",
         "read_count":280.0,
         "bibcode":"2015Icar..248..516C",
         "citation_count":0,
         "title":["The small binary asteroid (939) Isberga"]},
       {
         "read_count":12.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015Icar..248..539V",
         "citation_count":0,
         "title":["Ionization balance in Titan's nightside ionosphere"]},
       {
         "read_count":7.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015Icar..248..547C",
         "citation_count":1,
         "title":["Mercury's seasonal sodium exosphere: MESSENGER orbital observations"]},
       {
         "pubdate":"2015-03-00",
         "read_count":152.0,
         "bibcode":"2015MNRAS.447.2348M",
         "citation_count":2,
         "title":["Kepler photometry of RRc stars: peculiar double-mode pulsations and period doubling"]},
       {
         "pubdate":"2015-03-00",
         "read_count":111.0,
         "bibcode":"2015MNRAS.447.2568T",
         "citation_count":1,
         "title":["The fraction of type Ia supernovae exploding inside planetary nebulae (SNIPs)"]},
       {
         "pubdate":"2015-03-00",
         "bibcode":"2015APh....62..125A",
         "citation_count":0,
         "read_count":56.0,
         "title":["VAMOS: A pathfinder for the HAWC gamma-ray observatory"]},
       {
         "pubdate":"2015-03-00",
         "bibcode":"2015APh....63...66A",
         "citation_count":4,
         "read_count":134.0,
         "title":["Neutrino physics from the cosmic microwave background and large scale structure"]},
       {
         "pubdate":"2015-03-00",
         "read_count":14.0,
         "bibcode":"2015CNSNS..20.1057A",
         "citation_count":0,
         "title":["Dynamics of a dumbbell satellite under the zonal harmonic effect of an oblate body"]},
       {
         "pubdate":"2015-03-00",
         "read_count":60.0,
         "bibcode":"2015MNRAS.447.2181I",
         "citation_count":2,
         "title":["On the role of recombination in common-envelope ejections"]},
       {
         "pubdate":"2015-03-00",
         "bibcode":"2015MNRAS.447.2880A",
         "citation_count":0,
         "read_count":352.0,
         "title":["Precise time series photometry for the Kepler-2.0 mission"]},
       {
         "read_count":16.0,
         "pubdate":"2015-03-00",
         "bibcode":"2015MNRAS.447.2894N",
         "citation_count":0,
         "title":["The VAMPIRES instrument: imaging the innermost regions of protoplanetary discs with polarimetric interferometry"]},
       {
         "pubdate":"2015-03-00",
         "bibcode":"2015MNRAS.448L..58R",
         "citation_count":0,
         "read_count":369.0,
         "title":["Reanalysis of radial velocity data from the resonant planetary system HD128311"]},
       {
         "pubdate":"2015-03-00",
         "bibcode":"2015MNRAS.447.3422N",
         "citation_count":0,
         "read_count":139.0,
         "title":["An exploration of double diffusive convection in Jupiter as a result of hydrogen-helium phase separation"]},
       {
         "pubdate":"2015-03-00",
         "read_count":264.0,
         "bibcode":"2015MNRAS.447.2322R",
         "citation_count":1,
         "title":["Spatial distribution of Galactic Wolf-Rayet stars and implications for the global population"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JAESc..98....1G",
         "title":["Magma mixing recorded by Sr isotopes of plagioclase from dacites of the Quaternary Tengchong volcanic field, SE Tibetan Plateau"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JAESc..98...26Z",
         "title":["The 2011 Tohoku earthquake (Mw 9.0) sequence and subduction dynamics in Western Pacific and East Asia"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JAESc..98...61S",
         "title":["Zircon U-Pb ages, geochemistry, and Nd-Hf isotopes of the TTG gneisses from the Jiaobei terrane: Implications for Neoarchean crustal evolution in the North China Craton"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015CSR....93...17B",
         "title":["Biogenic effects on cohesive sediment erodibility resulting from recurring seasonal hypoxia on the Louisiana shelf"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015ECSS..153...10D",
         "title":["Polynya impacts on water properties in a Northeast Greenland fjord"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JAESc..98...50C",
         "title":["Frequent excitations of T waves by earthquakes in the South Mariana Arc"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JAESc..98...98Z",
         "title":["Stress changes induced by the 2008 M<SUB>w</SUB> 7.9 Wenchuan earthquake, China"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JAESc..98..105K",
         "title":["Non-double-couple mechanism of moderate earthquakes occurred in Lower Siang region of Arunachal Himalaya: Evidence of factors affecting non-DC"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JAESc..98..116S",
         "citation_count":0,
         "title":["Tectonic controls of the North Anatolian Fault System (NAFS) on the geomorphic evolution of the alluvial fans and fan catchments in Erzincan pull-apart basin; Turkey"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JAESc..98..152H",
         "title":["Detrital records for Upper Permian-Lower Triassic succession in the Shiwandashan Basin, South China and implication for Permo-Triassic (Indosinian) orogeny"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JAfES.102...70S",
         "title":["Fracturing of doleritic intrusions and associated contact zones: Implications for fluid flow in volcanic basins"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JAfES.102..102G",
         "title":["Petrogenesis of carbonated meta-ultramafic lenses from the Neoproterozoic Heiani ophiolite, South Eastern Desert, Egypt: A natural analogue to CO<SUB>2</SUB> sequestration"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JAfES.102..116O",
         "title":["Mineralogy, geochemistry and genesis of the modern sediments of Seyfe Lake, Kırşehir, central Anatolia, Turkey"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JAfES.102..131V",
         "title":["Thermochronology and geochemistry of the Pan-African basement below the Sab'atayn Basin, Yemen"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JAfES.102..166A",
         "title":["Geochemistry and geochronology of granitoids in the Kibi-Asamankese area of the Kibi-Winneba volcanic belt, southern Ghana"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JHyd..521...84S",
         "title":["Impact of climate and land cover changes on snow cover in a small Pyrenean catchment"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JHyd..521...46K",
         "title":["Separating snow, clean and debris covered ice in the Upper Indus Basin, Hindukush-Karakoram-Himalayas, using Landsat images between 1998 and 2002"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JQSRT.152...28Y",
         "title":["A theoretical room-temperature line list for <SUP>15</SUP>NH<SUB>3</SUB>"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015MSSP...52..700K",
         "title":["Mechatronic design of strongly nonlinear systems on a basis of three wheeled mobile platform"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015MSSP...52..217M",
         "title":["Waveform-based selection of acoustic emission events generated by damage in composite materials"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015MSSP...52..228S",
         "title":["Significance, interpretation, and quantification of uncertainty in prognostics and remaining useful life prediction"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JMMM..375..100Q",
         "title":["Greatly enhanced microwave absorbing properties of planar anisotropy carbonyl-iron particle composites"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JPS...275..284T",
         "title":["All-solid-state sodium batteries using amorphous TiS<SUB>3</SUB> electrode with high capacity"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JPS...275....1Y",
         "title":["Amorphous polymeric anode materials from poly(acrylic acid) and tin(II) oxide for lithium ion batteries"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015IJAEO..34..147B",
         "title":["Environmental assessment and land change analysis in seminatural land covers applicable to land management"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JMMM..375..234D",
         "title":["Kinetics of tetrataenite disordering"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JAESc..98...18L",
         "title":["How does crustal shortening contribute to the uplift of the eastern margin of the Tibetan Plateau?"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AcSpA.136..113L",
         "citation_count":1,
         "title":["Isolated glyoxylic acid-water 1:1 complexes in low temperature argon matrices"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AcSpA.137..271B",
         "title":["Theoretical spectroscopic studies and identification of metal-citrate (Cd and Pb) complexes by ESI-MS in aqueous solution"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AcSpA.137..338P",
         "title":["Enhancement in photocatalytic activity of NiO by supporting onto an Iranian clinoptilolite nano-particles of aqueous solution of cefuroxime pharmaceutical capsule"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AcSpA.137..471C",
         "title":["Study of luminescence, color and paramagnetic centers properties of albite"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015SurSc.632....1C",
         "title":["Self-propelled motion of Au-Si droplets on Si(111) mediated by monoatomic step dissolution"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015CG.....75...24H",
         "title":["Dynamic simulation of landslide based on thermo-poro-elastic approach"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AtmRe.153...98M",
         "title":["12 September 2012: A supercell outbreak in NE Italy?"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JMS...142...75A",
         "title":["The role of the wind in the formation of coherent eddies in the Gulf of Eilat/Aqaba"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AtmRe.153..360M",
         "title":["Influence of physics parameterization schemes on the simulation of a tropical-like cyclone in the Mediterranean Sea"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AtmRe.153..553G",
         "title":["Comparison of the surface energy budget between regions of seasonally frozen ground and permafrost on the Tibetan Plateau"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015PhyA..420...59Z",
         "title":["Statistical analysis on the evolution of OpenStreetMap road networks in Beijing"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015PhyA..420..134W",
         "title":["A comparative analysis of intra-city human mobility by taxi"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015PhyA..419..603S",
         "title":["Predicting fracture of mortar beams under three-point bending using non-extensive statistical modeling of electric emissions"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015PhyA..419..668D",
         "title":["Is there enough fertile soil to feed a planet of growing cities?"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015PhyE...66...59S",
         "title":["Microstructure characterization and electrical transport of nanocrystalline CdZnS quantum dots"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AtmRe.153...59G",
         "title":["Common summertime total cloud cover and aerosol optical depth weekly variabilities over Europe: Sign of the aerosol indirect effects?"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JQSRT.152...16K",
         "citation_count":0,
         "title":["Modelling radiometric properties of inhomogeneous mineral dust particles: Applicability and limitations of effective medium theories"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AcSpA.137..397P",
         "title":["Impact of sediment characteristics on the heavy metal concentration and their ecological risk level of surface sediments of Vaigai river, Tamilnadu, India"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015IGRSL..12..364H",
         "citation_count":0,
         "title":["Body Wave Separation in the Time-Frequency Domain"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015IJAEO..34..122P",
         "title":["Sequence-based mapping approach to spatio-temporal snow patterns from MODIS time-series applied to Scotland"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AtmRe.153..318Z",
         "title":["Ensemble simulations of the urban effect on a summer rainfall event in the Great Beijing Metropolitan Area"]},
       {
         "read_count":3.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AtmRe.153..392A",
         "title":["A case study for Saharan dust transport over Turkey via RegCM4.1 model"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JPS...275....6T",
         "citation_count":0,
         "title":["Fast lithium intercalation chemistry of the hierarchically porous Li<SUB>2</SUB>FeP<SUB>2</SUB>O<SUB>7</SUB>/C composite prepared by an iron-reduction method"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JMMM..375...54K",
         "title":["Crystallographic and magnetic properties of Cu<SUB>2</SUB>U-type hexaferrite"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JMS...142...16H",
         "title":["Multidecadal spatial reorganisation of plankton communities in the North East Atlantic"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015MSSP...52..360F",
         "title":["Iterative generalized synchrosqueezing transform for fault diagnosis of wind turbine planetary gearbox under nonstationary conditions"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AcSpA.137..817D",
         "citation_count":0,
         "title":["Micas from mariupolite of the Oktiabrski massif (SE Ukraine): An insight into the host rock evolution - Geochemical data supported by Raman microspectroscopy"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JGCD...38..192C",
         "title":["Optical Navigation Using Planet's Centroid and Apparent Diameter in Image"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AtmEn.102...11K",
         "title":["Sensitivity of population smoke exposure to fire locations in Equatorial Asia"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AtmEn.102..112W",
         "title":["Analysis of a severe prolonged regional haze episode in the Yangtze River Delta, China"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AtmEn.102..136C",
         "title":["Partitioning of Black Carbon between ultrafine and fine particle modes in an urban airport vs. urban background environment"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JAESc..98..188C",
         "title":["A mixture of mantle and crustal derived He-Ar-C-S ore-forming fluids at the Baogutu reduced porphyry Cu deposit, western Junggar"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JAESc..98..198C",
         "title":["Geometry and kinematics of the Arlar strike-slip fault, SW Qaidam basin, China: New insights from 3-D seismic data"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JAESc..98..225B",
         "title":["Terrestrial paleoenvironment characterization across the Permian-Triassic boundary in South China"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JAESc..98..247L",
         "title":["Zircon U-Pb geochronological, geochemical, and Sr-Nd isotope data for Early Cretaceous mafic dykes in the Tancheng-Lujiang Fault area of the Shandong Province, China: Constraints on the timing of magmatism and magma genesis"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JAESc..98..272Z",
         "title":["Geology, isotope geochemistry and geochronology of the Jinshachang carbonate-hosted Pb-Zn deposit, southwest China"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JAESc..98..285L",
         "title":["An 80-year summer temperature history from the Xiao Dongkemadi ice core in the central Tibetan Plateau and its association with atmospheric circulation"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JAfES.102...18F",
         "title":["Characteristics of marine CSEM responses in complex geologic terrain of Niger Delta Oil province: Insight from 2.5D finite element forward modeling"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JAfES.102...41R",
         "title":["Petrological and geochemical Highlights in the floating fragments of the October 2011 submarine eruption offshore El Hierro (Canary Islands): Relevance of submarine hydrothermal processes"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JQSRT.152..140C",
         "title":["The absorption spectrum of <SUP>13</SUP>CH<SUB>4</SUB> in the region of the 2ν<SUB>3</SUB> band at 1.66 μm: Empirical line lists and temperature dependence"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JPS...276..113T",
         "title":["Characterization of mixed titanium-niobium oxide Ti<SUB>2</SUB>Nb<SUB>10</SUB>O<SUB>29</SUB> annealed in vacuum as anode material for lithium-ion battery"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015PhyD..292...70R",
         "title":["Mixed mode oscillations in a conceptual climate model"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AcAau.107...70L",
         "title":["Nonlinearity analysis of measurement model for vision-based optical navigation system"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AcAau.107..177A",
         "title":["A phenomenological performance model for applied-field MPD thrusters"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AcAau.107..218P",
         "title":["Designing solar sail formations in sun-synchronous orbits for geomagnetic tail exploration"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AcAau.107..247W",
         "title":["Target selection for a hypervelocity asteroid intercept vehicle flight validation mission"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015CG.....75...57F",
         "title":["A parallel algorithm for viewshed analysis in three-dimensional Digital Earth"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AtmEn.102..260L",
         "title":["How well do satellite AOD observations represent the spatial and temporal variability of PM<SUB>2.5</SUB> concentration for the United States?"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AtmEn.102..311L",
         "title":["Radioactivity impacts of the Fukushima Nuclear Accident on the atmosphere"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015NIMPA.774...29B",
         "title":["Characterization of room temperature AlGaAs soft X-ray mesa photodiodes"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015SSCom.203....1O",
         "title":["Relationship between structural variation and spin transition of iron under high pressures and high temperatures"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015Geomo.230...13W",
         "title":["Aeolian responses to climate variability during the past century on Mesquite Lake Playa, Mojave Desert"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015Geomo.230...37D",
         "title":["Assessment of a rapid method for quantitative reach-scale estimates of deposited fine sediment in rivers"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015Geomo.230...51S",
         "title":["Geologic controls on bedrock channel width in large, slowly-eroding catchments: Case study of the New River in eastern North America"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015Geomo.230...75S",
         "title":["Architectural-landsystem analysis of a modern glacial landscape, Sólheimajökull, southern Iceland"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015Geomo.230..138B",
         "title":["Seismically-induced mass movements and volumetric fluxes resulting from the 2010 M<SUB>w</SUB> = 7.2 earthquake in the Sierra Cucapah, Mexico"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AtmEn.102..331S",
         "title":["Ship-based MAX-DOAS measurements of tropospheric NO<SUB>2</SUB> and SO<SUB>2</SUB> in the South China and Sulu Sea"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AtmEn.102..366M",
         "title":["An approach to investigate new particle formation in the vertical direction on the basis of high time-resolution measurements at ground level and sea level"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AtmEn.103....1C",
         "title":["UV absorption cross sections between 290 and 380 nm of a series of furanaldehydes: Estimation of their photolysis lifetimes"]},
       {
         "read_count":3.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015Icar..247..103L",
         "citation_count":0,
         "title":["A 10 km-resolution synthetic Venus gravity field model based on topography"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AdSpR..55.1190P",
         "title":["On the use of Very Low Frequency transmitter data for remote sensing of atmospheric gravity and planetary waves"]},
       {
         "pubdate":"2015-02-00",
         "bibcode":"2015NewA...35....8H",
         "citation_count":0,
         "read_count":12.0,
         "title":["Indian summer monsoon rainfall: Dancing with the tunes of the sun"]},
       {
         "read_count":9.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015Icar..247...77D",
         "citation_count":0,
         "title":["Improved techniques for size-frequency distribution analysis in the planetary sciences: Application to blocks on 25143 Itokawa"]},
       {
         "read_count":20.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015Icar..247..137A",
         "citation_count":0,
         "title":["Properties of craters on the Achaia region of Asteroid (21) Lutetia"]},
       {
         "pubdate":"2015-02-00",
         "read_count":150.0,
         "bibcode":"2015MNRAS.447.1154M",
         "citation_count":0,
         "title":["A geometrically thin accretion disc around a Maclaurin spheroid"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AcAau.107....1P",
         "title":["A new approach to trajectory optimization based on direct transcription and differential flatness"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AcAau.107..196M",
         "citation_count":0,
         "title":["Determination of temperature variation on lunar surface and subsurface for habitat analysis and design"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JQSRT.152...45T",
         "citation_count":0,
         "title":["CDSD-296, high resolution carbon dioxide spectroscopic databank: Version for atmospheric applications"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AtmRe.153...10L",
         "title":["A study of longitudinal and altitudinal variations in surface water stable isotopes in West Pamir, Tajikistan"]},
       {
         "read_count":2.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015CoPhC.187..137M",
         "citation_count":0,
         "title":["A finite volume formulation of the multi-moment advection scheme for Vlasov simulations of magnetized plasma"]},
       {
         "read_count":8.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015Icar..247..248C",
         "citation_count":0,
         "title":["Iron snow dynamo models for Ganymede"]},
       {
         "read_count":9.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015E&PSL.411...11P",
         "citation_count":0,
         "title":["The origin of chondrules: Constraints from matrix composition and matrix-chondrule complementarity"]},
       {
         "read_count":5.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015Icar..247..126B",
         "citation_count":0,
         "title":["Self-consistent modeling of induced magnetic field in Titan's atmosphere accounting for the generation of Schumann resonance"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JAESc..98...75M",
         "title":["Magnetotelluric studies in the epicenter zone of 2001, Bhuj earthquake"]},
       {
         "read_count":2.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015GeoJI.200..880K",
         "citation_count":0,
         "title":["Use of along-track magnetic field differences in lithospheric field modelling"]},
       {
         "read_count":2.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JASTP.123....1K",
         "citation_count":0,
         "title":["Climatology of radar anomalous propagation over West Africa"]},
       {
         "read_count":2.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JASTP.123...13N",
         "citation_count":0,
         "title":["A southern Africa harmonic spline core field model derived from CHAMP satellite data"]},
       {
         "read_count":2.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015CG.....75...17L",
         "citation_count":0,
         "title":["A progressive black top hat transformation algorithm for estimating valley volumes on Mars"]},
       {
         "read_count":12.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015Icar..247...18S",
         "citation_count":0,
         "title":["Subsurface failure in spherical bodies: A formation scenario for linear troughs on Vesta's surface"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AcAau.107..163M",
         "citation_count":0,
         "title":["Physicochemical properties of respirable-size lunar dust"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AnPhy.353...71C",
         "citation_count":1,
         "title":["Time dependent Schrödinger equation for black hole evaporation: No information loss"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AtmRe.153..451E",
         "citation_count":0,
         "title":["The influence of Sardinia on Corsican rainfall in the western Mediterranean Sea: A numerical sensitivity study"]},
       {
         "read_count":2.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JPS...275..384P",
         "citation_count":0,
         "title":["Effects of short-side-chain perfluorosulfonic acid ionomers as binders on the performance of low Pt loading fuel cell cathodes"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JMMM..375..177D",
         "title":["Preparation and characterization of anion exchange resin decorated with magnetite nanoparticles for removal of p-toluic acid from aqueous solution"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015APh....61...17K",
         "citation_count":0,
         "title":["Directivity function of muon detector"]},
       {
         "read_count":2.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015GeCoA.150..125K",
         "citation_count":0,
         "title":["Influence of deep-water derived isoprenoid tetraether lipids on the 〈mml:msubsup〉TEX 86 H paleothermometer in the Mediterranean Sea"]},
       {
         "read_count":2.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015GeCoA.151...34L",
         "citation_count":0,
         "title":["A kinetic pressure effect on the experimental abiotic reduction of aqueous CO<SUB>2</SUB> to methane from 1 to 3.5 kbar at 300 °C"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015GeCoA.150..253S",
         "title":["Sulfur-controlled iron isotope fractionation experiments of core formation in planetary bodies"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015PEPI..239....1H",
         "title":["Introduction: GEM catalog"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015PhyA..419...23V",
         "citation_count":0,
         "title":["Analysis of natural time domain entropy fluctuations of synthetic seismicity generated by a simple stick-slip system with asperities"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015E&PSL.412...42C",
         "title":["Anomalous elastic properties of coesite at high pressure and implications for the upper mantle X-discontinuity"]},
       {
         "read_count":3.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015E&PSL.412...52K",
         "citation_count":0,
         "title":["Paleosecular variation of the earth magnetic field at the Canary Islands over the last 15 ka"]},
       {
         "read_count":2.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015GeoJI.200.1066K",
         "citation_count":0,
         "title":["Linear analysis on the onset of thermal convection of highly compressible fluids: implications for the mantle convection of super-Earths"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015IJAEO..34..113S",
         "citation_count":0,
         "title":["Woody vegetation and land cover changes in the Sahel of Mali (1967-2011)"]},
       {
         "read_count":10.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015Icar..247..301S",
         "citation_count":0,
         "title":["Molecular nitrogen and methane density retrievals from Cassini UVIS dayglow observations of Titan's upper atmosphere"]},
       {
         "read_count":11.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015Icar..247...35B",
         "citation_count":0,
         "title":["Fault geometries on Uranus' satellite Miranda: Implications for internal structure and heat flow"]},
       {
         "read_count":4.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015Icar..247...71J",
         "citation_count":0,
         "title":["Aerogel dust collection for in situ mass spectrometry analysis"]},
       {
         "read_count":4.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015APh....61...82B",
         "citation_count":0,
         "title":["Search for time modulations in the decay rate of 40K and 232Th"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015PhyA..419..698S",
         "title":["Characterizing Detrended Fluctuation Analysis of multifractional Brownian motion"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015PhyA..419...40T",
         "title":["Morisita-based space-clustering analysis of Swiss seismicity"]},
       {
         "read_count":3.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015E&PSL.411....1S",
         "citation_count":0,
         "title":["Nitrogen isotope evidence for alkaline lakes on late Archean continents"]},
       {
         "read_count":4.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015E&PSL.411...20B",
         "citation_count":0,
         "title":["A power spectrum for the geomagnetic dipole moment"]},
       {
         "read_count":3.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015E&PSL.411...27F",
         "citation_count":0,
         "title":["Rate of fluvial incision in the Central Alps constrained through joint inversion of detrital <SUP>10</SUP>Be and thermochronometric data"]},
       {
         "read_count":2.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015E&PSL.411...37B",
         "citation_count":0,
         "title":["A numerical approach to melting in warm subduction zones"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015E&PSL.411..142E",
         "title":["Partitioning of light lithophile elements during basalt eruptions on Earth and application to Martian shergottites"]},
       {
         "read_count":2.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015E&PSL.411..253G",
         "citation_count":0,
         "title":["Landform assemblage in Isidis Planitia, Mars: Evidence for a 3 Ga old polythermal ice sheet"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015E&PSL.411..281L",
         "title":["Sustained high magnitude erosional forcing generates an organic carbon sink: Test and implications in the Loess Plateau, China"]},
       {
         "read_count":2.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015E&PSL.412....1G",
         "citation_count":0,
         "title":["Electrical conductivity of albite-(quartz)-water and albite-water-NaCl systems and its implication to the high conductivity anomalies in the continental crust"]},
       {
         "read_count":3.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015E&PSL.412...10D",
         "citation_count":0,
         "title":["High-resolution sequence stratigraphy of fluvio-deltaic systems: Prospects of system-wide chronostratigraphic correlation"]},
       {
         "read_count":2.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015E&PSL.412...25R",
         "citation_count":0,
         "title":["Detection and characterization of transient forcing episodes affecting earthquake activity in the Aleutian Arc system"]},
       {
         "read_count":2.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015E&PSL.412...35I",
         "citation_count":0,
         "title":["Strength characteristics of Japan Trench borehole samples in the high-slip region of the 2011 Tohoku-Oki earthquake"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015E&PSL.412...61T",
         "title":["Geology of the Wilkes land sub-basin and stability of the East Antarctic Ice Sheet: Insights from rock magnetism at IODP Site U1361"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015E&PSL.412...70P",
         "title":["Crustal thickening and clay: Controls on O isotope variation in global magmatism and siliciclastic sedimentary rocks"]},
       {
         "read_count":2.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015GeCoA.150...26S",
         "citation_count":0,
         "title":["Texture-specific Si isotope variations in Barberton Greenstone Belt cherts record low temperature fractionations in early Archean seawater"]},
       {
         "read_count":2.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015GeCoA.150...53C",
         "citation_count":0,
         "title":["Extreme Nb/Ta fractionation in metamorphic titanite from ultrahigh-pressure metagranite"]},
       {
         "read_count":2.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015GeCoA.150...90L",
         "citation_count":0,
         "title":["Marine redox conditions in the middle Proterozoic ocean and isotopic constraints on authigenic carbonate formation: Insights from the Chuanlinggou Formation, Yanshan Basin, North China"]},
       {
         "read_count":2.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015GeCoA.150..106L",
         "citation_count":0,
         "title":["Bicarbonate impact on U(VI) bioreduction in a shallow alluvial aquifer"]},
       {
         "read_count":2.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015GeCoA.150..142M",
         "citation_count":0,
         "title":["Insights into iron sources and pathways in the Amazon River provided by isotopic and spectroscopic studies"]},
       {
         "read_count":2.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015GeCoA.150..160W",
         "citation_count":0,
         "title":["Isotope fractionation during oxidation of tetravalent uranium by dissolved oxygen"]},
       {
         "read_count":2.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015GeCoA.150..171U",
         "citation_count":0,
         "title":["Experimental evidence for kinetic effects on B/Ca in synthetic calcite: Implications for potential B(OH)<SUB>4</SUB><SUP>-</SUP> and B(OH)<SUB>3</SUB> incorporation"]},
       {
         "read_count":2.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015GeCoA.150..192G",
         "citation_count":0,
         "title":["Enhanced recycling of organic matter and Os-isotopic evidence for multiple magmatic or meteoritic inputs to the Late Permian Panthalassic Ocean, Opal Creek, Canada"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015GeCoA.151....1B",
         "title":["Quantification of colloidal and aqueous element transfer in soils: The dual-phase mass balance model"]},
       {
         "read_count":2.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015GeCoA.151...19B",
         "citation_count":0,
         "title":["Reduced partition function ratios of iron and oxygen in goethite"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015OptCo.336...24S",
         "citation_count":0,
         "title":["Scattering of polarized Gaussian light by a spheroidal particle"]},
       {
         "read_count":3.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015A&A...574A...9L",
         "citation_count":0,
         "title":["Simulated low-intensity optical pulsar observation with single-photon detector"]},
       {
         "read_count":0.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AtmRe.153..235S",
         "title":["A study of aerosol optical properties during ozone pollution episodes in 2013 over Shanghai, China"]},
       {
         "read_count":3.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015E&PSL.412...18S",
         "citation_count":0,
         "title":["Changes in the ENSO/SPCZ relationship from past to future climates"]},
       {
         "read_count":37.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015Icar..247..191B",
         "citation_count":1,
         "title":["In search of the source of asteroid (101955) Bennu: Applications of the stochastic YORP model"]},
       {
         "read_count":7.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015Icar..247..260J",
         "citation_count":0,
         "title":["Using martian single and double layered ejecta craters to probe subsurface stratigraphy"]},
       {
         "pubdate":"2015-02-00",
         "read_count":72.0,
         "bibcode":"2015Icar..247..172M",
         "citation_count":0,
         "title":["Re-examining the main asteroid belt as the primary source of ancient lunar craters"]},
       {
         "read_count":17.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015Icar..247..218L",
         "citation_count":0,
         "title":["The neutral photochemistry of nitriles, amines and imines in the atmosphere of Titan"]},
       {
         "read_count":3.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015GeCoA.150...11F",
         "citation_count":0,
         "title":["Redox controls on Ni-Fe-PGE mineralization and Re/Os fractionation during serpentinization of abyssal peridotite"]},
       {
         "pubdate":"2015-02-00",
         "read_count":130.0,
         "bibcode":"2015MNRAS.447.1141B",
         "citation_count":0,
         "title":["Tidal interactions of a Maclaurin spheroid - II. Resonant excitation of modes by a close, misaligned orbit"]},
       {
         "read_count":13.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015MNRAS.447.1242D",
         "citation_count":0,
         "title":["Small molecules from the decomposition of interstellar carbons"]},
       {
         "pubdate":"2015-02-00",
         "read_count":189.0,
         "bibcode":"2015MNRAS.447.1439S",
         "citation_count":0,
         "title":["On the location of the ice line in circumbinary discs"]},
       {
         "read_count":2.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JASTP.123...22W",
         "citation_count":0,
         "title":["Mesospheric and thermospheric observations of the January 2010 stratospheric warming event"]},
       {
         "pubdate":"2015-02-00",
         "read_count":300.0,
         "bibcode":"2015MNRAS.446.3788B",
         "citation_count":0,
         "title":["Re-assessing the formation of the inner Oort cloud in an embedded star cluster - II. Probing the inner edge"]},
       {
         "pubdate":"2015-02-00",
         "read_count":255.0,
         "bibcode":"2015MNRAS.446.3797H",
         "citation_count":0,
         "title":["Reddening, distance, and stellar content of the young open cluster Westerlund 2"]},
       {
         "pubdate":"2015-02-00",
         "bibcode":"2015MNRAS.447..836F",
         "citation_count":0,
         "read_count":252.0,
         "title":["The dynamical fate of self-gravitating disc fragments after tidal downsizing"]},
       {
         "pubdate":"2015-02-00",
         "read_count":136.0,
         "bibcode":"2015MNRAS.447..993G",
         "citation_count":0,
         "title":["Discovery of true, likely and possible symbiotic stars in the dwarf spheroidal NGC 205"]},
       {
         "pubdate":"2015-02-00",
         "read_count":89.0,
         "bibcode":"2015MNRAS.446.3461U",
         "citation_count":0,
         "title":["The almost ubiquitous association of 6.7-GHz methanol masers with dust"]},
       {
         "pubdate":"2015-02-00",
         "read_count":325.0,
         "bibcode":"2015MNRAS.446.4039E",
         "citation_count":0,
         "title":["Composite bulges: the coexistence of classical bulges and discy pseudo-bulges in S0 and spiral galaxies"]},
       {
         "pubdate":"2015-02-00",
         "read_count":136.0,
         "bibcode":"2015MNRAS.446.4132J",
         "citation_count":4,
         "title":["Hubble Frontier Fields: the geometry and dynamics of the massive galaxy cluster merger MACSJ0416.1-2403"]},
       {
         "pubdate":"2015-02-00",
         "read_count":72.0,
         "bibcode":"2015MNRAS.446.4260Z",
         "citation_count":0,
         "title":["Effect of dynamical interactions on integrated properties of globular clusters"]},
       {
         "pubdate":"2015-02-00",
         "read_count":270.0,
         "bibcode":"2015MNRAS.446.4278P",
         "citation_count":0,
         "title":["Primordial mass segregation in simulations of star formation?"]},
       {
         "pubdate":"2015-02-00",
         "bibcode":"2015MNRAS.447..817B",
         "citation_count":0,
         "read_count":13.0,
         "title":["Echelle spectroscopy and photoionization modelling of the entire planetary nebula NGC 6210"]},
       {
         "pubdate":"2015-02-00",
         "read_count":113.0,
         "bibcode":"2015MNRAS.447.1673V",
         "citation_count":3,
         "title":["New light on Galactic post-asymptotic giant branch stars - I. First distance catalogue"]},
       {
         "pubdate":"2015-02-00",
         "read_count":83.0,
         "bibcode":"2015MNRAS.447L..45R",
         "citation_count":1,
         "title":["Non-linear mirror instability"]},
       {
         "pubdate":"2015-02-00",
         "read_count":390.0,
         "bibcode":"2015MNRAS.447L..60S",
         "citation_count":2,
         "title":["The nature of ULX source M101 X-1: optically thick outflow from a stellar mass black hole"]},
       {
         "pubdate":"2015-02-00",
         "bibcode":"2015NewA...35...53D",
         "citation_count":0,
         "read_count":299.0,
         "title":["Deuterium enrichment of the interstellar medium"]},
       {
         "pubdate":"2015-02-00",
         "read_count":191.0,
         "bibcode":"2015PhyA..420...41Z",
         "citation_count":0,
         "title":["The stationary state and gravitational temperature in a pure self-gravitating system"]},
       {
         "pubdate":"2015-02-00",
         "read_count":205.0,
         "bibcode":"2015A&A...574A..10L",
         "citation_count":1,
         "title":["Rossby wave instability does not require sharp resistivity gradients"]},
       {
         "pubdate":"2015-02-00",
         "read_count":75.0,
         "bibcode":"2015A&A...574A..18P",
         "citation_count":0,
         "title":["Gap interpolation by inpainting methods: Application to ground and space-based asteroseismic data"]},
       {
         "pubdate":"2015-02-00",
         "read_count":231.0,
         "bibcode":"2015A&A...574A..21R",
         "citation_count":0,
         "title":["Isolated elliptical galaxies and their globular cluster systems. II. NGC 7796 - globular clusters, dynamics, companion"]},
       {
         "pubdate":"2015-02-00",
         "read_count":31.0,
         "bibcode":"2015A&A...574A..22M",
         "citation_count":1,
         "title":["Impact induced surface heating by planetesimals on early Mars"]},
       {
         "pubdate":"2015-02-00",
         "read_count":36.0,
         "bibcode":"2015AJ....149...42P",
         "citation_count":1,
         "title":["Proper Motion of the Draco Dwarf Galaxy Based On Hubble Space Telescope Imaging"]},
       {
         "pubdate":"2015-02-00",
         "read_count":38.0,
         "bibcode":"2015APh....61...47K",
         "citation_count":3,
         "title":["Magnetic deflections of ultra-high energy cosmic rays from Centaurus A"]},
       {
         "pubdate":"2015-02-00",
         "bibcode":"2015Icar..247....1S",
         "citation_count":0,
         "read_count":42.0,
         "title":["Landslides and Mass shedding on spinning spheroidal asteroids"]},
       {
         "pubdate":"2015-02-00",
         "bibcode":"2015Icar..247...53L",
         "citation_count":0,
         "read_count":70.0,
         "title":["Composition, mineralogy, and porosity of multiple asteroid systems from visible and near-infrared spectral data"]},
       {
         "pubdate":"2015-02-00",
         "read_count":154.0,
         "bibcode":"2015Icar..247...81S",
         "citation_count":3,
         "title":["Atmospheric mass loss during planet formation: The importance of planetesimal impacts"]},
       {
         "pubdate":"2015-02-00",
         "read_count":77.0,
         "bibcode":"2015Icar..247..112P",
         "citation_count":0,
         "title":["The intrinsic Neptune Trojan orbit distribution: Implications for the primordial disk and planet migration"]},
       {
         "read_count":5.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015Icar..247..150W",
         "citation_count":0,
         "title":["Lunar cryptomaria: Physical characteristics, distribution, and implications for ancient volcanism"]},
       {
         "read_count":15.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015Icar..247..279T",
         "citation_count":0,
         "title":["Distribution of boulders and the gravity potential on asteroid Itokawa"]},
       {
         "pubdate":"2015-02-00",
         "read_count":70.0,
         "bibcode":"2015Icar..247..291B",
         "citation_count":0,
         "title":["A collisional origin to Earth's non-chondritic composition?"]},
       {
         "read_count":31.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015Icar..247..313S",
         "citation_count":0,
         "title":["Observations of sodium in the coma of Comet C/2012 S1 (ISON) during outburst"]},
       {
         "pubdate":"2015-02-00",
         "read_count":416.0,
         "bibcode":"2015MNRAS.446.4078K",
         "citation_count":1,
         "title":["New white dwarf stars in the Sloan Digital Sky Survey Data Release 10"]},
       {
         "pubdate":"2015-02-00",
         "read_count":142.0,
         "bibcode":"2015MNRAS.447..287S",
         "citation_count":2,
         "title":["Gaia's potential for the discovery of circumbinary planets"]},
       {
         "pubdate":"2015-02-00",
         "read_count":304.0,
         "bibcode":"2015MNRAS.447..463N",
         "citation_count":0,
         "title":["HST hot-Jupiter transmission spectral survey: haze in the atmosphere of WASP-6b"]},
       {
         "pubdate":"2015-02-00",
         "read_count":83.0,
         "bibcode":"2015MNRAS.447.1922M",
         "citation_count":0,
         "title":["SN Hunt 248: a super-Eddington outburst from a massive cool hypergiant"]},
       {
         "pubdate":"2015-02-00",
         "read_count":270.0,
         "bibcode":"2015MNRAS.447.1996W",
         "citation_count":0,
         "title":["The James Clerk Maxwell telescope Legacy Survey of the Gould Belt: a molecular line study of the Ophiuchus molecular cloud"]},
       {
         "pubdate":"2015-02-00",
         "read_count":50.0,
         "bibcode":"2015A&A...574A...1G",
         "citation_count":0,
         "title":["XMM-Newton RGS observations of the Cat's Eye Nebula"]},
       {
         "pubdate":"2015-02-00",
         "read_count":89.0,
         "bibcode":"2015A&A...574A...5D",
         "citation_count":1,
         "title":["ALMA data suggest the presence of spiral structure in the inner wind of CW Leonis"]},
       {
         "pubdate":"2015-02-00",
         "read_count":197.0,
         "bibcode":"2015A&A...574A...6A",
         "citation_count":0,
         "title":["Are the orbital poles of binary stars in the solar neighbourhood anisotropically distributed?"]},
       {
         "pubdate":"2015-02-00",
         "read_count":96.0,
         "bibcode":"2015A&A...574A..35P",
         "citation_count":4,
         "title":["A non-grey analytical model for irradiated atmospheres. II. Analytical vs. numerical solutions"]},
       {
         "pubdate":"2015-02-00",
         "bibcode":"2015A&A...574A..50J",
         "citation_count":0,
         "read_count":195.0,
         "title":["Stellar parameters and chemical abundances of 223 evolved stars with and without planets"]},
       {
         "pubdate":"2015-02-00",
         "bibcode":"2015A&A...574A..52D",
         "citation_count":1,
         "read_count":415.0,
         "title":["Migration of massive planets in accreting disks"]},
       {
         "read_count":4.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AcAau.107...50B",
         "citation_count":0,
         "title":["The Mars imperative: Species survival and inspiring a globalized culture"]},
       {
         "read_count":9.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015AcSpA.137..516K",
         "title":["The origin and determination of silica types in the silica occurrences from Altintaş region (Uşak-Western Anatolia) using multianalytical techniques"]},
       {
         "pubdate":"2015-02-00",
         "read_count":23.0,
         "bibcode":"2015AdSpR..55..843D",
         "citation_count":0,
         "title":["Theory of fossil magnetic field"]},
       {
         "pubdate":"2015-02-00",
         "read_count":431.0,
         "bibcode":"2015AJ....149...55E",
         "citation_count":0,
         "title":["High-Resolution Multi-Band Imaging for Validation and Characterization of Small Kepler Planets"]},
       {
         "pubdate":"2015-02-00",
         "read_count":116.0,
         "bibcode":"2015ApJ...799..118P",
         "citation_count":4,
         "title":["Secular Evolution of Binaries near Massive Black Holes: Formation of Compact Binaries, Merger/Collision Products and G2-like Objects"]},
       {
         "pubdate":"2015-02-00",
         "read_count":752.0,
         "bibcode":"2015ApJ...799..120B",
         "citation_count":2,
         "title":["Chaotic Disintegration of the Inner Solar System"]},
       {
         "read_count":3.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015E&PSL.411..199M",
         "citation_count":1,
         "title":["Catastrophic emplacement of giant landslides aided by thermal decomposition: Heart Mountain, Wyoming"]},
       {
         "read_count":5.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015JCoPh.282..157I",
         "citation_count":0,
         "title":["High-order central ENO finite-volume scheme for hyperbolic conservation laws on three-dimensional cubed-sphere grids"]},
       {
         "pubdate":"2015-02-00",
         "read_count":299.0,
         "bibcode":"2015MNRAS.447..577M",
         "citation_count":0,
         "title":["Stirring in massive, young debris discs from spatially resolved Herschel images"]},
       {
         "pubdate":"2015-02-00",
         "read_count":357.0,
         "bibcode":"2015MNRAS.446.3676A",
         "citation_count":0,
         "title":["On the stability of extrasolar planetary systems and other closely orbiting pairs"]},
       {
         "pubdate":"2015-02-00",
         "read_count":192.0,
         "bibcode":"2015MNRAS.447..417P",
         "citation_count":0,
         "title":["A volume-limited sample of X-ray galaxy groups and clusters - III. Central abundance drops"]},
       {
         "pubdate":"2015-02-00",
         "read_count":147.0,
         "bibcode":"2015MNRAS.447..446W",
         "citation_count":3,
         "title":["Impact of instrumental systematic errors on fine-structure constant measurements with quasar spectra"]},
       {
         "pubdate":"2015-02-00",
         "read_count":109.0,
         "bibcode":"2015MNRAS.446.4271S",
         "citation_count":0,
         "title":["The observational effects and signatures of tidally distorted solid exoplanets"]},
       {
         "pubdate":"2015-02-00",
         "bibcode":"2015MNRAS.447..722D",
         "citation_count":0,
         "read_count":211.0,
         "title":["The JCMT Gould Belt Survey: low-mass protoplanetary discs from a SCUBA-2 census of NGC 1333"]},
       {
         "pubdate":"2015-02-00",
         "bibcode":"2015MNRAS.447..846B",
         "citation_count":0,
         "read_count":560.0,
         "title":["Stellar diameters and temperatures - VI. High angular resolution measurements of the transiting exoplanet host stars HD 189733 and HD 209458 and implications for models of cool dwarfs"]},
       {
         "pubdate":"2015-02-00",
         "read_count":299.0,
         "bibcode":"2015MNRAS.447.1059K",
         "citation_count":4,
         "title":["The dynamical evolution of molecular clouds near the Galactic Centre - I. Orbital structure and evolutionary timeline"]},
       {
         "pubdate":"2015-02-00",
         "read_count":174.0,
         "bibcode":"2015MNRAS.447.1080G",
         "citation_count":0,
         "title":["Imaging the transition between pre-planetary and planetary nebulae: integral field spectroscopy of hot post-AGB stars with NIFS"]},
       {
         "pubdate":"2015-02-00",
         "read_count":448.0,
         "bibcode":"2015MNRAS.447..711S",
         "citation_count":1,
         "title":["High-precision photometry by telescope defocusing - VII. The ultrashort period planet WASP-103"]},
       {
         "pubdate":"2015-02-00",
         "read_count":217.0,
         "bibcode":"2015A&A...574L...1C",
         "citation_count":0,
         "title":["Constraints on the gas content of the Fomalhaut debris belt. Can gas-dust interactions explain the belt's morphology?"]},
       {
         "pubdate":"2015-02-00",
         "bibcode":"2015AdSpR..55..817B",
         "citation_count":0,
         "read_count":8.0,
         "title":["Meridional flow velocities on solar-like stars with known activity cycles"]},
       {
         "pubdate":"2015-02-00",
         "read_count":472.0,
         "bibcode":"2015MNRAS.447.1049V",
         "citation_count":0,
         "title":["Detectable close-in planets around white dwarfs through late unpacking"]},
       {
         "pubdate":"2015-02-00",
         "read_count":242.0,
         "bibcode":"2015MNRAS.447.1267M",
         "citation_count":0,
         "title":["New low-mass members of the Octans stellar association and an updated 30-40 Myr lithium age"]},
       {
         "pubdate":"2015-02-00",
         "read_count":119.0,
         "bibcode":"2015MNRAS.447.1749M",
         "citation_count":1,
         "title":["Kepler and the seven dwarfs: detection of low-level day-time-scale periodic photometric variations in white dwarfs"]},
       {
         "pubdate":"2015-02-00",
         "read_count":400.0,
         "bibcode":"2015MNRAS.447L..75K",
         "citation_count":0,
         "title":["Nature or nurture of coplanar Tatooines: the aligned circumbinary Kuiper belt analogue around HD 131511"]},
       {
         "read_count":23.0,
         "pubdate":"2015-02-00",
         "bibcode":"2015NewA...35...36D",
         "citation_count":0,
         "title":["New upper limits on the power of general relativity from solar system dynamics"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22513701L",
         "title":["Predicting Ground Illuminance"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22513905R",
         "title":["Spatially Resolved Far-Infrared Spectroscopic Analysis of Planetary Nebulae"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22521901H",
         "title":["Aiming for the next bright super earth — Synergies of Ground and Space based Transiting Planets Survey"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22521905M",
         "title":["Defining A Risk Analysis Strategy for Exo-Earth Yields from a Future Large Aperture UVOIR Space Telescope"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22523903T",
         "title":["The Effects of Clouds and Hazes on the Spectra of Terrestrial and Sub-Neptune Planets"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525738M",
         "title":["Modelling Phase Curves and Occultations in KOI Light Curve"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525808W",
         "title":["Calibrating the pixel-level Kepler imaging data with a causal data-driven model"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525812E",
         "title":["Advances in Focal Plane Wavefront Estimation for Directly Imaging Exoplanets"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22530607B",
         "title":["Accurate Stellar Parameters for Exoplanet Host Stars"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525604B",
         "title":["An Investigation of Three Methods for Determining Young Star Spectral Types"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525724D",
         "title":["A secular model for efficient exploration of mutually-inclined planetary systems"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22534908F",
         "title":["Effects of dust feedback on vortices in protoplanetary disks〈!--EndFragment--〉"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22533821D",
         "title":["Potential of a Future Large Aperture UVOIR Space Observatory for Breakthrough Observations of Star and Planet Formation"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22533824G",
         "title":["Measurements of High-Contrast Starshade Performance"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AtmRe.152...14P",
         "citation_count":1,
         "title":["Cloud effects on the solar and thermal radiation budgets of the Mediterranean basin"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JQSRT.150..114X",
         "citation_count":1,
         "title":["Study of the scanning lidar on the atmospheric detection"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015DSRI...95...37S",
         "title":["A unique Fe-rich carbonate chimney associated with cold seeps in the Northern Okinawa Trough, East China Sea"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAESc..97...38L",
         "title":["U-Pb zircon geochronology, geochemical and Sr-Nd-Hf isotopic compositions of the Early Indosinian Tongren Pluton in West Qinling: Petrogenesis and geodynamic implications"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Geomo.228..512A",
         "title":["Late Quaternary alluvial fans of Emli Valley in the Ecemiş Fault Zone, south central Turkey: Insights from cosmogenic nuclides"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Geomo.228..568D",
         "title":["What was the transport mode of large boulders in the Campine Plateau and the lower Meuse valley during the mid-Pleistocene?"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAESc..97..169G",
         "title":["Study on crustal magnetic anomalies and Curie surface in Southeast Tibet"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JSV...335...34M",
         "title":["First-order optimal linear and nonlinear detuning of centrifugal pendulum vibration absorbers"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JSG....70....1F",
         "title":["Quartz c-axis fabric development associated with shear deformation along an extensional detachment shear zone: Chapedony Metamorphic Core Complex, Central-East Iranian Microcontinent"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015ECSS..152...56Z",
         "title":["Concentrations and fluxes of uranium in two major Chinese rivers: The Changjiang River and the Huanghe River"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015ECSS..152...73A",
         "title":["Role of two co-occurring Mediterranean sea urchins in the formation of barren from Cystoseira canopy"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAESc..97..393M",
         "title":["Early Cretaceous high-Mg diorites in the Yanji area, northeastern China: Petrogenesis and tectonic implications"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAESc..97..406W",
         "title":["Late Triassic bimodal igneous rocks in eastern Heilongjiang Province, NE China: Implications for the initiation of subduction of the Paleo-Pacific Plate beneath Eurasia"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAESc..97..424N",
         "title":["Geology and origin of Ag-Pb-Zn deposits occurring in the Ulaan-Jiawula metallogenic province, northeast Asia"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAESc..97..442H",
         "title":["Metallogenic events and tectonic setting of the Duobaoshan ore field in Heilongjiang Province, NE China"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAESc..97..472Z",
         "title":["Geochemistry and geochronology of the Dongshanwan porphyry Mo-W deposit, Northeast China: Implications for the Late Jurassic tectonic setting"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAESc..97..486Z",
         "title":["Ore genesis and fluid evolution of the Daheishan giant porphyry molybdenum deposit, NE China"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JGeo...83...28B",
         "title":["On the residual isostatic topography effect in the gravimetric Moho determination"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JGeo...83...37B",
         "title":["Shortening of the European Dauphinois margin (Oisans Massif, Western Alps): New insights from RSCM maximum temperature estimates and <SUP>40</SUP>Ar/<SUP>39</SUP>Ar in situ dating"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JPS...273..324C",
         "title":["Multi-component nanoporous platinum-ruthenium-copper-osmium-iridium alloy with enhanced electrocatalytic activity towards methanol oxidation and oxygen reduction"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015OcMod..85...56B",
         "title":["Lagrangian water mass tracing from pseudo-Argo, model-derived salinity, tracer and velocity data: An application to Antarctic Intermediate Water in the South Atlantic Ocean"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015PhyA..417..304V",
         "title":["Capital market based warning indicators of bank runs"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015SedG..315...64L",
         "title":["Petrographic and geochemical features of sinkhole-filling deposits associated with an erosional unconformity on Grand Cayman"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Tectp.638...43Z",
         "title":["Fault slip and earthquake recurrence along strike-slip faults - Contributions of high-resolution geomorphic data"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Tectp.638...94M",
         "title":["Heterogeneities and diagenetic control on the spatial distribution of carbonate rocks acoustic properties at the outcrop scale"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Tectp.638..126B",
         "title":["Low Angle Normal Fault (LANF)-zone architecture and permeability features in bedded carbonate from inner Northern Apennines (Rapolano Terme, Central Italy)"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Tectp.638..147K",
         "title":["Back-arc rifting in the Korea Plateau in the East Sea (Japan Sea) and the separation of the southwestern Japan Arc from the Korean margin"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Tectp.638..177B",
         "title":["Complex deformation pattern of the Pamir-Hindu Kush region inferred from multi-scale double-difference earthquake relocations"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JMoSp.307...27D",
         "title":["Analysis of the rotational spectrum of the ground and first torsional excited states of monodeuterated ethane, CH<SUB>3</SUB>CH<SUB>2</SUB>D"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Litho.212..189D",
         "title":["Characterization of hydration in the mantle lithosphere: Peridotite xenoliths from the Ontong Java Plateau as an example"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Litho.212..202V",
         "title":["Geochemical and Sr-Nd isotopic constraints on the mantle source of Neoproterozoic mafic dikes of the rifted eastern Laurentian margin, north-central Appalachians, USA"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Litho.212..214H",
         "title":["Cocos Plate Seamounts offshore NW Costa Rica and SW Nicaragua: Implications for large-scale distribution of Galápagos plume material in the upper mantle"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Litho.212..231W",
         "title":["Geochronological and geochemical constraints on the origin of clastic meta-sedimentary rocks associated with the Yuanjiacun BIF from the Lüliang Complex, North China"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Litho.212..280L",
         "title":["UHP impure marbles from the Dabie Mountains: Metamorphic evolution and carbon cycling in continental subduction zones"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Litho.212..298B",
         "title":["Spatial, temporal, mineralogical, and compositional variations in Mesozoic kimberlitic magmatism in New York State"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Litho.212..311M",
         "title":["Reconstruction of magmatic variables governing recent Etnean eruptions: Constraints from mineral chemistry and P-T-fO<SUB>2</SUB>-H<SUB>2</SUB>O modeling"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Litho.212..321K",
         "title":["Mode and timing of granitoid magmatism in the Västervik area (SE Sweden, Baltic Shield): Sr-Nd isotope and SIMS U-Pb age constraints"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Litho.212..338Z",
         "title":["Geochronological and He-Ar-S isotopic constraints on the origin of the Sandaowanzi gold-telluride deposit, northeastern China"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Litho.212..353W",
         "title":["Late Triassic adakitic plutons within the Archean terrane of the North China Craton: Melting of the ancient lower crust at the onset of the lithospheric destruction"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Litho.212..368S",
         "title":["Sr, Nd, Pb and Hf isotopic constraints on mantle sources and crustal contaminants in the Payenia volcanic province, Argentina"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Litho.212..379C",
         "title":["Zircon U-Pb ages, geochemistry, and Sr-Nd-Pb-Hf isotopes of the Nuri intrusive rocks in the Gangdese area, southern Tibet: Constraints on timing, petrogenesis, and tectonic transformation"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AtmRe.152....4P",
         "title":["Air quality and thermal comfort levels under extreme hot weather"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAESc..97....1C",
         "title":["Low deformation rate in the Koyna-Warna region, a reservoir triggered earthquake site in west-central stable India"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAESc..97..150C",
         "title":["Silicate melt inclusions in clinopyroxene phenocrysts from mafic dikes in the eastern North China Craton: Constraints on melt evolution"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JPCS...76..120O",
         "title":["In situ Raman spectroscopy of cubic boron nitride to 90 GPa and 800 K"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AtmRe.152...82K",
         "title":["Studying the urban thermal environment under a human-biometeorological point of view: The case of a large coastal metropolitan city, Athens"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AtmRe.152..105S",
         "title":["Influence of orography on precipitation amount and distribution in NW Greece; A case study"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAfES.101..274O",
         "title":["Electrical conductivity of mantle in the North Central region of Nigeria"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AtmEn.101...41S",
         "title":["On the evaluation of global sea-salt aerosol models at coastal/orographic sites"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AtmRe.151..176D",
         "title":["Fog events and local atmospheric features simulated by regional climate model for the metropolitan area of São Paulo, Brazil"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAESc..97..102U",
         "title":["Zircon U-Pb ages and Hf isotopic compositions of alkaline silicic magmatic rocks in the Phan Si Pan-Tu Le region, northern Vietnam: Identification of a displaced western extension of the Emeishan Large Igneous Province"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AcAau.106..120D",
         "title":["Computational study of the effect of using open isogrids on the natural frequencies of a small satellite structure"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AtmEn.100..117T",
         "title":["Spatiotemporal inhomogeneity in NO<SUB>2</SUB> over Fukuoka observed by ground-based MAX-DOAS"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AtmEn.101..177D",
         "title":["The effect of SRTM and Corine Land Cover data on calculated gas and PM10 concentrations in WRF-Chem"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JQSRT.150...95Y",
         "title":["Retrievals and uncertainty analysis of aerosol single scattering albedo from MFRSR measurements"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JSV...335..350Y",
         "title":["Vibration signal analysis using parameterized time-frequency method for features extraction of varying-speed rotary machinery"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Litho.212....1Z",
         "title":["Long-lived high-temperature granulite-facies metamorphism in the Eastern Himalayan orogen, south Tibet"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Litho.212...16T",
         "title":["Petrogenesis of the Ni-Cu-PGE sulfide-bearing Tamarack Intrusive Complex, Midcontinent Rift System, Minnesota"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Litho.212...32F",
         "title":["Fe isotopes and the contrasting petrogenesis of A-, I- and S-type granite"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Litho.212...45Z",
         "title":["Paleocene adakitic porphyry in the northern Qiangtang area, north-central Tibet: Evidence for early uplift of the Tibetan Plateau"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Litho.212...59B",
         "title":["Petrogenesis and tectonic implications of the early Jurassic Fe-Ti oxide-bearing Xialan mafic intrusion in SE China: Constraints from zircon Hf-O isotopes, mineral compositions and whole-rock geochemistry"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Litho.212...74E",
         "title":["Petrogenesis and U-Pb zircon chronology of felsic tuffs interbedded with turbidites (Eastern Pontides Orogenic Belt, NE Turkey): Implications for Mesozoic geodynamic evolution of the eastern Mediterranean region and accumulation rates of turbidite sequences"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Litho.212...93C",
         "title":["Petrogenesis of metaluminous A-type granitoids in the Tengchong-Lianghe tin belt of southwestern China: Evidences from zircon U-Pb ages and Hf-O isotopes, and whole-rock Sr-Nd isotopes"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015MSSP...50..467G",
         "title":["Applying robust variant of Principal Component Analysis as a damage detector in the presence of outliers"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015NIMPB.343...30M",
         "title":["Excitation functions for the production of radionuclides by neutron-induced reactions on C, O, Mg, Al, Si, Fe, Co, Ni, Cu, Ag, Te, Pb, and U up to 180 MeV"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015QSRv..107..214B",
         "title":["Late Holocene sea-level change in Arctic Norway"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015SedG..315...29K",
         "title":["Cyclostratigraphy of an orbitally-driven Tithonian-Valanginian carbonate ramp succession, Southern Mendoza, Argentina: Implications for the Jurassic-Cretaceous boundary in the Neuquén Basin"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015SedG..315...47F",
         "title":["Provenance of the southern Junggar Basin in the Jurassic: Evidence from detrital zircon geochronology and depositional environments"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JMMM..373...68M",
         "title":["Investigation of phase formation of (Zn, Mg)<SUB>0.5</SUB>Co<SUB>0.5</SUB>Fe<SUB>2</SUB>O<SUB>4</SUB> nanoferrites"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015QSRv..107...81C",
         "title":["Quaternary uplift rates of the Central Anatolian Plateau, Turkey: insights from cosmogenic isochron-burial nuclide dating of the Kızılırmak River terraces"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAESc..97..125A",
         "title":["Mid-Paleozoic arc granitoids in SW Japan with Neoproterozoic xenocrysts from South China: New zircon U-Pb ages by LA-ICP-MS"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAfES.101...19E",
         "title":["Metamorphic modifications of the Muremera mafic-ultramafic intrusions, eastern Burundi, and their effect on chromite compositions"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JPS...274.1267H",
         "title":["Evidence of loss of active lithium in titanium-doped LiNi<SUB>0.5</SUB>Mn<SUB>1.5</SUB>O<SUB>4</SUB>/graphite cells"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AcSpA.134..621P",
         "title":["The effect of SiO<SUB>2</SUB>/Al<SUB>2</SUB>O<SUB>3</SUB> ratio on the structure and microstructure of the glazes from SiO<SUB>2</SUB>-Al<SUB>2</SUB>O<SUB>3</SUB>-CaO-MgO-Na<SUB>2</SUB>O-K<SUB>2</SUB>O system"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015QSRv..107..231S",
         "title":["Early break-up of the Norwegian Channel Ice Stream during the Last Glacial Maximum"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AtmRe.152..159R",
         "title":["Relationship between the Indian summer monsoon and the large-scale circulation variability over the Mediterranean"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JPS...274.1200L",
         "title":["Direct synthesis of trirutile-type LiMgFeF<SUB>6</SUB> and its electrochemical characterization as positive electrode in lithium-ion batteries"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JQSRT.150..107S",
         "citation_count":1,
         "title":["Detection of internally mixed Asian dust with air pollution aerosols using a polarization optical particle counter and a polarization-sensitive two-wavelength lidar"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AcAau.106...33V",
         "title":["When Policy Structures Technology: Balancing upfront decomposition and in-process coordination in Europe's decentralized space technology ecosystem"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AcAau.106...47A",
         "title":["Model of medical supply and astronaut health for long-duration human space flight"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AcAau.106..101I",
         "title":["Longitudinal stability analysis of a suborbital re-entry demonstrator for a deployable capsule"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AtmEn.101...10S",
         "title":["Surface ozone concentration trends and its relationship with weather types in Spain (2001-2010)"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AtmEn.101...23C",
         "title":["Long-term surface ozone variability at Mt. Cimone WMO/GAW global station (2165 m a.s.l., Italy)"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AtmEn.101..246C",
         "title":["Impact of particle nonsphericity on the development and properties of aerosol models for East Asia"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AtmEn.101..257S",
         "title":["Tracking potential sources of peak ozone concentrations in the upper troposphere over the Arabian Gulf region"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AtmEn.100...37J",
         "title":["Influence of springtime biomass burning in South Asia on regional ozone (O<SUB>3</SUB>): A model based case study"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JCHyd.172...33Y",
         "title":["Experimental investigation of compound-specific dilution of solute plumes in saturated porous media: 2-D vs. 3-D flow-through systems"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JPS...274..542L",
         "title":["SiO<SUB>x</SUB>-C dual-phase glass for lithium ion battery anode with high capacity and stable cycling performance"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015SedG..315...14J",
         "title":["Silurian carbonate high-energy deposits of potential tsunami origin: Distinguishing lateral redeposition and time averaging using carbon isotope chemostratigraphy"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015QSRv..107...25M",
         "title":["Paleoclimate and paleoceanography over the past 20,000 yr in the Mediterranean Sea Basins as indicated by sediment elemental proxies"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015QSRv..107..149M",
         "title":["The Taimyr Peninsula and the Severnaya Zemlya archipelago, Arctic Russia: a synthesis of glacial history and palaeo-environmental change during the Last Glacial cycle (MIS 5e-2)"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015CG.....74...71L",
         "title":["Interactive editing of 3D geological structures and tectonic history sketching via a rigid element method"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015CG.....74...97Y",
         "title":["Fuzzification of continuous-value spatial evidence for mineral prospectivity mapping"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JGeo...83....1C",
         "title":["Spatial distribution of the contemporary stress field in the Kurile Wadati-Benioff zone by inversion of earthquake focal mechanisms"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JPS...274..265C",
         "title":["Polyhedral ordered LiNi<SUB>0.5</SUB>Mn<SUB>1.5</SUB>O<SUB>4</SUB> spinel with excellent electrochemical properties in extreme conditions"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015NatCC...5R..19B",
         "title":["Biodiversity: Planetary boundaries"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JSR....95...56M",
         "title":["Interactions between trophic levels in upwelling and non-upwelling regions during summer monsoon"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAfES.101..232M",
         "title":["History of the development of the East African Rift System: A series of interpreted maps through time"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAfES.101..266O",
         "title":["Field and petrochemical studies of pegmatites in parts of Lokoja, Central Nigeria"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAfES.101..333A",
         "title":["Diagenetic origin of ironstone crusts in the Lower Cenomanian Bahariya Formation, Bahariya Depression, Western Desert, Egypt"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Geomo.228...87G",
         "title":["Sediment transport during recent cave flooding events and characterization of speleothem archives of past flooding"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Geomo.228..134B",
         "title":["Gaining insight into regional coastal changes on La Réunion island through a Bayesian data mining approach"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Geomo.228..234V",
         "title":["Transient river response, captured by channel steepness and its concavity"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Geomo.228..263L",
         "title":["Advantages of beachrock slabs for interpreting high-energy wave transport: Evidence from Ludao Island in south-eastern Taiwan"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Geomo.228..320D",
         "title":["Geomorpho-tectonic evolution of the Jamaican restraining bend"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Geomo.228..335B",
         "title":["Upper Pleistocene interstratal piping-cave speleogenesis: The Seso Cave System (Central Pyrenees, Northern Spain)"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Geomo.228..345E",
         "title":["Recent evolution and degradation of the bent Jatunraju glacier (Cordillera Blanca, Peru)"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Geomo.228..382D",
         "title":["Geomorphology and weathering characteristics of erratic boulder trains on Tierra del Fuego, southernmost South America: Implications for dating of glacial deposits"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Geomo.228..409T",
         "title":["Quantifying variable erosion rates to understand the coupling of surface processes in the Teton Range, Wyoming"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Geomo.228..432D",
         "title":["Volcanic geomorphological classification of the cinder cones of Tenerife (Canary Islands, Spain)"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Geomo.228..470K",
         "title":["Modern depositional processes in a confined, flood-prone setting: Benches on the Shoalhaven River, NSW, Australia"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Geomo.228..486X",
         "title":["Vegetated dune morphodynamics during recent stabilization of the Mu Us dune field, north-central China"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Geomo.228..579W",
         "title":["Geomorphological evidence of neotectonic deformation in the Carnarvon Basin, Western Australia"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Geomo.228..637D",
         "title":["A critical appraisal of allometric growth among alpine cirques based on multivariate statistics and spatial analysis"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015NIMPB.342..321P",
         "title":["A new IBA-AMS laboratory at the Comenius University in Bratislava (Slovakia)"]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015PApGe.172...57F",
         "read_count":0.0,
         "title":["Earth's Rotation: A Challenging Problem in Mathematics and Physics"]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015PApGe.172...75D",
         "read_count":0.0,
         "title":["Geosystemics: A Systemic View of the Earth's Magnetic Field and the Possibilities for an Imminent Geomagnetic Transition"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015NatSR...5E7640L",
         "title":["High-Pressure Orthorhombic Ferromagnesite as a Potential Deep-Mantle Carbon Carrier"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015PhyB..457..225C",
         "citation_count":0,
         "title":["Electrical transport properties of nanocrystalline nonstoichiometric nickel ferrite at and above room temperature"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015SCPMA..58....7C",
         "title":["Solar sailing trajectory optimization with planetary gravity assist"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22534922W",
         "title":["New Data Reduction Techniques for Circumstellar Disk Imaging with the Hubble DICE Survey"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015ECSS..152...11L",
         "title":["Community, trophic structure and functioning in two contrasting Laminaria hyperborea forests"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Geomo.228..597R",
         "citation_count":0,
         "title":["Construction and destruction rates of volcanoes within tropical environment: Examples from the Basse-Terre Island (Guadeloupe, Lesser Antilles)"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JMMM..374..225L",
         "title":["Microwave absorbing properties and enhanced infrared reflectance of FeAl mixture synthesized by two-step ball-milling method"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JQSRT.150...68V",
         "citation_count":1,
         "title":["Mixing rules and morphology dependence of the scatterer"]},
       {
         "read_count":16.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015SedG..315....1W",
         "title":["Early Eocene sedimentary recycling in the Kailas area, southwestern Tibet: Implications for the initial India-Asia collision"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22543807T",
         "title":["First Semester Science Operations with the Gemini Planet Imager"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015MSSP...50...70M",
         "title":["Envelope calculation of the multi-component signal and its application to the deterministic component cancellation in bearing fault diagnosis"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015RaPC..106...88M",
         "citation_count":0,
         "title":["A complexity analysis of <SUP>222</SUP>Rn concentration variation: A case study for Domica cave, Slovakia for the period June 2010-June 2011"]},
       {
         "read_count":0.0,
         "bibcode":"2015tybp.conf.....L",
         "pubdate":"2015-01-00",
         "title":["Thirty years of beta Pic and debris disks studies"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22542001M",
         "citation_count":0,
         "title":["Detailed Chemical Abundances of Planet-Hosting Wide Binary Systems"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525709W",
         "citation_count":0,
         "title":["High-Resolution Abundance Analysis of Stars with Small Planets Discovered by Kepler"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AcAau.106..139B",
         "citation_count":0,
         "title":["Conceptual design of a flight validation mission for a Hypervelocity Asteroid Intercept Vehicle"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AtmEn.100....1A",
         "title":["Changes in shape and composition of sea-salt particles upon aging in an urban atmosphere"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AtmEn.100..167K",
         "title":["Urban air quality simulation in a high-rise building area using a CFD model coupled with mesoscale meteorological and chemistry-transport models"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AtmEn.101..134B",
         "title":["On the capabilities and limitations of GCCM simulations of summertime regional air quality: A diagnostic analysis of ozone and temperature simulations in the US using CESM CAM-Chem"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AtmRe.151..101H",
         "title":["The role of dew in the monsoon season assessed via stable isotopes in an alpine meadow in Northern Tibet"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015NIMPA.771....1G",
         "citation_count":0,
         "title":["A transportable source of gamma rays with discrete energies and wide range for calibration and on-site testing of gamma-ray detectors"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAfES.101...70S",
         "title":["Cu-Mn-Fe alloys and Mn-rich amphiboles in ancient copper slags from the Jabal Samran area, Saudi Arabia: With synopsis on chemistry of Fe-Mn(III) oxyhydroxides in alteration zones"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAfES.101..430.",
         "citation_count":0,
         "title":["Current Literature Survey"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAfES.101..405S",
         "citation_count":0,
         "title":["The paleoecology of the Upper Laetolil Beds, Laetoli Tanzania: A review and synthesis"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JPCS...76..192B",
         "title":["Formation of self-supporting porous graphite structures by Spark Plasma Sintering of nickel-amorphous carbon mixtures"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JQSRT.150...42Z",
         "citation_count":3,
         "title":["Effect of morphology on light scattering by agglomerates"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JQSRT.150..148M",
         "citation_count":2,
         "title":["Application of surface pressure measurements from O<SUB>2</SUB>-band differential absorption radar system in three-dimensional data assimilation on hurricane: Part I - An observing system simulation experiments study"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Geomo.228...41B",
         "citation_count":0,
         "title":["Field measurement and analysis of climatic factors affecting dune mobility near Grand Falls on the Navajo Nation, southwestern United States"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Geomo.228..372N",
         "title":["Relationship of runoff, erosion and sediment yield to weather types in the Iberian Peninsula"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Geomo.228..275W",
         "title":["Contrasting patterns of wood storage in mountain watercourses narrower and wider than the height of riparian trees"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015QSRv..107...98C",
         "citation_count":0,
         "title":["Hydroclimatic changes in China and surroundings during the Medieval Climate Anomaly and Little Ice Age: spatial patterns and possible mechanisms"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015QSRv..107..129L",
         "citation_count":0,
         "title":["The influence of meltwater on the Labrador Current in Heinrich event 1 and the Younger Dryas"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22510504K",
         "citation_count":0,
         "title":["Kepler’s Missing Planets: Using QATS to Search for Planets with TTVs"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22510801K",
         "title":["ChanPlaNS: The Chandra Planetary Nebula Survey"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22513826P",
         "title":["HAZMAT II: Modeling the Evolution of Extreme-UV Radiation from M Stars"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22520707G",
         "citation_count":0,
         "title":["The dynamical effects of an outer planet on the evolution and observability of Kepler-11-like systems"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22521902H",
         "citation_count":0,
         "title":["Transits and Occultations of Hot Jupiters"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525824B",
         "citation_count":0,
         "title":["Spotting Spots: Simulating Stellar Noise for Spot Detection"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525826N",
         "title":["Optimization of the MINERVA Exoplanet Search Strategy via Simulations"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22510203S",
         "citation_count":0,
         "title":["The Best and Brightest Metal-Poor Stars"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22533829N",
         "citation_count":0,
         "title":["Integrated Modeling of the WFIRST AFTA Coronagraph Instrument"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AdSpR..55..231C",
         "citation_count":0,
         "title":["Variations of total electron content in the equatorial anomaly region in Thailand"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525723D",
         "citation_count":0,
         "title":["The Orbital Architectures of Planet-Hosting Binary Systems"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22543808G",
         "citation_count":0,
         "title":["Measuring the Mass of Kepler-78b Using a Gaussian Process Model"]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22512206M",
         "citation_count":0,
         "read_count":2.0,
         "title":["A Transit Timing Posterior Distribution Catalog for all Kepler Planet Candidates"]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22514128M",
         "citation_count":0,
         "read_count":0.0,
         "title":["Simulations of the Dynamics of Precursor Organic and Prebiotic Carbon-rich Moleculess"]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525731P",
         "citation_count":0,
         "read_count":0.0,
         "title":["Characterizing the Hot Kepler Objects of Interest"]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525739G",
         "citation_count":0,
         "read_count":2.0,
         "title":["Characterizing Retired A Stars"]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525740C",
         "citation_count":0,
         "read_count":2.0,
         "title":["Young Nearby Suns and Stellar Jitter Dependence on Age"]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525822G",
         "citation_count":0,
         "read_count":2.0,
         "title":["Retrieval of Precise Radial Velocities from High Resolution Near-Infrared Spectra of M Dwarfs"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015CliPD..11...27B",
         "title":["How might the North American ice sheet influence the Northwestern Eurasian climate?"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015EnMan..55..113F",
         "title":["Local Perceptions, Ruslefac Mapping, and Field Results: The Sediment Budget of Cocagne River, New Brunswick, Canada"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAtS...72..389L",
         "title":["Scaling of Off-Equatorial Jets in Giant Planet Atmospheres"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JEMat..44..457B",
         "title":["Arc-Erosion Behavior of Boric Oxide-Reinforced Silver-Based Electrical Contact Materials Produced by Mechanical Alloying"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.409..225Z",
         "citation_count":0,
         "title":["Refertilization-driven destabilization of subcontinental mantle and the importance of initial lithospheric thickness for the fate of continents"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GeCoA.149..131G",
         "citation_count":0,
         "title":["The magnesium isotope (δ<SUP>26</SUP>Mg) signature of dolomites"]},
       {
         "read_count":8.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Icar..246..345T",
         "citation_count":0,
         "title":["Limits on Pluto's ring system from the June 12 2006 stellar occultation and implications for the New Horizons impact hazard"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAfES.101..383A",
         "citation_count":0,
         "title":["Litho-structural control on interbasin groundwater transfer in central Ethiopia"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JMS...141....3L",
         "citation_count":1,
         "title":["Comparative biogeochemistry-ecosystem-human interactions on dynamic continental margins"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Litho.212..397K",
         "citation_count":0,
         "title":["Zircon Th/U ratios in magmatic environs"]},
       {
         "read_count":6.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015MNRAS.446.2738A",
         "citation_count":0,
         "title":["The role of molecular quadrupole transitions in the depopulation of metastable helium"]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22522805C",
         "read_count":0.0,
         "title":["NASA SOFIA International Year of Light (IYL) Event: Infrared Light: Hanging out in the Stratosphere"]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22524004G",
         "read_count":0.0,
         "title":["Preparing new Earth Science teachers via a collaborative program between Research Scientists and Educators"]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22524201S",
         "read_count":0.0,
         "title":["Modern Publishing Approach of Journal of Astronomy & Earth Sciences Education"]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22513822O",
         "citation_count":0,
         "read_count":2.0,
         "title":["Connecting Flares and Transient Mass Loss Events in Active Stars"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015ACPD...15..123A",
         "title":["High spatial resolution aerosol retrievals used for daily particulate matter monitoring over Po valley, northern Italy"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015ACPD...15..393S",
         "title":["Observations of PW activity in the MLT during SSW events using a chain of SuperDARN radars and SD-WACCM"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015BGeo...12..103C",
         "title":["Two perspectives on the coupled carbon, water and energy exchange in the planetary boundary layer"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Freq...69...57B",
         "title":["Radar based Ground Level Reconstruction Utilizing a Hypocycloid Antenna Positioning System"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GMDD....8..141M",
         "title":["CH<SUB>4</SUB> parameter estimation in CLM4.5bgc using surrogate global optimization"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015PPCF...57a4003A",
         "title":["Studying planetary matter using intense x-ray pulses"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015RuPhJ.tmp...12B",
         "title":["Influence of the Methods of Constructing Ephemerides of Major Planets and the Moon on the Accuracy of Predicting Motion of Asteroids"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AtmEn.101..338C",
         "title":["Regional characteristics of the relationship between columnar AOD and surface PM<SUB>2.5</SUB>: Application of lidar aerosol extinction profiles over Baltimore-Washington Corridor during DISCOVER-AQ"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015CSR....92...59P",
         "title":["Controls on the distribution and fractionation of yttrium and rare earth elements in core sediments from the Mandovi estuary, western India"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015CSR....92...87R",
         "title":["A comparison of the annual cycle of sea level in coastal areas from gridded satellite altimetry and tide gauges"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015CSR....92...98Y",
         "title":["Assessing dissolved organic matter dynamics and source strengths in a subtropical estuary: Application of stable carbon isotopes and optical properties"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015DSRI...95..122K",
         "title":["Variability in gas and solute fluxes through deep-sea chemosynthetic ecosystems inhabited by vesicomyid bivalves in the Gulf of Guinea"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015DSRI...95..131M",
         "title":["Temperature control of microbial respiration and growth efficiency in the mesopelagic zone of the South Atlantic and Indian Oceans"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAG...112...91M",
         "title":["A rock magnetic profile through the ejecta flap of the Lockne impact crater (central Sweden) and implications for the impact excavation process"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAG...112..206D",
         "title":["Quality-factor and reflection-coefficient estimation using surface-wave ghost reflections from subvertical structures"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JGeo...83...65S",
         "title":["The Internal Subbetic of the Velez Rubio area (SE Spain): Is it tectonically detached or not?"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JGeo...83...76O",
         "title":["The relation between gravity rate of change and vertical displacement in previously glaciated areas"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JGeo...83...85S",
         "title":["Modern observations of the effect of earthquakes on the Chandler wobble"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JHyd..520..157K",
         "title":["Evolution of the Yellow River Delta and its relationship with runoff and sediment load from 1983 to 2011"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JHyd..520..268T",
         "title":["Comparing statistical post-processing of regional and global climate scenarios for hydrological impacts assessment: A case study of two Canadian catchments"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JHyd..520..489B",
         "title":["Potential relation between equatorial sea surface temperatures and historic water level variability for Lake Turkana, Kenya"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JSAES..57...12J",
         "title":["Magnetic stratigraphy of the Bucaramanga alluvial fan: Evidence for a ≤3 mm/yr slip rate for the Bucaramanga-Santa Marta Fault, Colombia"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JSAES..57...23M",
         "title":["U-Th dating of broken speleothems from Cacahuamilpa cave, Mexico: Are they recording past seismic events?"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JPS...273.1073H",
         "citation_count":0,
         "title":["Development of Cu<SUB>1.3</SUB>Mn<SUB>1.7</SUB>O<SUB>4</SUB> spinel coating on ferritic stainless steel for solid oxide fuel cell interconnects"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JPS...273..608K",
         "title":["Application of a bio-derivative, rosin, as a binder additive for lithium titanium oxide electrodes in lithium-ion batteries"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JPS...273..688D",
         "title":["Novel composite proton-exchange membrane based on proton-conductive glass powders and sulfonated poly (ether ether ketone)"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JPS...273..716M",
         "title":["Sulfur and carbon tolerance of BaCeO<SUB>3</SUB>-BaZrO<SUB>3</SUB> proton-conducting materials"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015PhyC..508...49W",
         "title":["Effects of three different homemade nanocarbons doping on the superconducting properties of MgB<SUB>2</SUB> tapes"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015PhyC..508...62F",
         "title":["XPS study of the chemical stability of DyBa<SUB>2</SUB>Cu<SUB>3</SUB>O<SUB>6+δ</SUB> superconductor"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015QSRv..108...23S",
         "title":["Sea-level changes in Iceland and the influence of the North Atlantic Oscillation during the last half millennium"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015QSRv..108...76K",
         "title":["Sedimentary response to Milankovitch-type climatic oscillations and formation of sediment undulations: evidence from a shallow-shelf setting at Gela Basin on the Sicilian continental margin"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015QSRv..108...95F",
         "title":["The Marine Isotope Stage 19 in the mid-latitude North Atlantic Ocean: astronomical signature and intra-interglacial variability"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015QSRv..108..111C",
         "title":["Role of seasonal transitions and westerly jets in East Asian paleoclimate"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015QSRv..108..130V",
         "title":["Marine productivity leads organic matter preservation in sapropel S1: palynological evidence from a core east of the Nile River outflow"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Litho.212..111H",
         "title":["Geochronology and isotope geochemistry of Eocene dykes intruding the Ladakh Batholith"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Litho.212..145N",
         "title":["Hydrothermally-induced changes in mineralogy and magnetic properties of oxidized A-type granites"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Litho.212..158W",
         "title":["Water-fluxed melting of the continental crust: A review"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015NIMPB.343..146W",
         "title":["Xe- and U-tracks in apatite and muscovite near the etching threshold"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Tectp.638...63H",
         "title":["Complex local Moho topography in the Western Carpathians: Indication of the ALCAPA and the European Plate contact"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Tectp.638...82C",
         "title":["Recent seismicity of Italy: Active tectonics of the central Mediterranean region and seismicity rate changes after the Mw 6.3 L'Aquila earthquake"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AtmEn.101..286M",
         "title":["Characterisation and distribution of deposited trace elements transported over long and intermediate distances in north-eastern France using Sphagnum peatlands as a sentinel ecosystem"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015ApSS..325...20T",
         "title":["Oxidation and microstructure evolution of Al-Si coated Ni<SUB>3</SUB>Al based single crystal superalloy with high Mo content"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JHyd..520..342M",
         "title":["Transient simulations of large-scale hydrogeological processes causing temperature and salinity anomalies in the Tiberias Basin"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JMS...141..167L",
         "title":["In search of the dead zone: Use of otoliths for tracking fish exposure to hypoxia"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JPS...273..823R",
         "title":["A high power density miniaturized microbial fuel cell having carbon nanotube anodes"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAESc..97..197Z",
         "title":["Geochemistry and U-Pb zircon dating of the Toudaoqiao blueschists in the Great Xing'an Range, northeast China, and tectonic implications"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAESc..97..211H",
         "title":["U-Pb age and Hf isotopic data of detrital zircons from the Devonian and Carboniferous sandstones in Yimin area, NE China: New evidences to the collision timing between the Xing'an and Erguna blocks in eastern segment of Central Asian Orogenic Belt"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAESc..97..229W",
         "title":["Geochronology, geochemistry, and Sr-Nd-Hf isotopes of the early Paleozoic igneous rocks in the Duobaoshan area, NE China, and their geological significance"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAESc..97..251L",
         "title":["U-Pb, <SUP>39</SUP>Ar/<SUP>40</SUP>Ar geochronology of the metamorphosed volcanic rocks of the Bainaimiao Group in central Inner Mongolia and its implications for ore genesis and geodynamic setting"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAESc..97..260W",
         "title":["Geochronology and geochemistry of Late Devonian and early Carboniferous igneous rocks of central Jilin Province, NE China: Implications for the tectonic evolution of the eastern Central Asian Orogenic Belt"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAESc..97..279Z",
         "title":["Geochronology and geochemistry of the Eastern Erenhot ophiolitic complex: Implications for the tectonic evolution of the Inner Mongolia-Daxinganling Orogenic Belt"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAESc..97..294H",
         "title":["Geochemistry and zircon U-Pb-Hf isotopes of the granitoids of Baolidao and Halatu plutons in Sonidzuoqi area, Inner Mongolia: Implications for petrogenesis and geodynamic setting"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAESc..97..307C",
         "title":["Geochronology, geochemistry, and its geological significance of the Damaoqi Permian volcanic sequences on the northern margin of the North China Block"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAESc..97..320T",
         "title":["Permian alkaline granites in the Erenhot-Hegenshan belt, northern Inner Mongolia, China: Model of generation, time of emplacement and regional tectonic significance"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAESc..97..337W",
         "title":["Geochronology, geochemistry and origins of the Paleozoic-Triassic plutons in the Langshan area, western Inner Mongolia, China"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAESc..97..352Z",
         "title":["Blueschist metamorphism and its tectonic implication of Late Paleozoic-Early Mesozoic metabasites in the mélange zones, central Inner Mongolia, China"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAESc..97..365W",
         "title":["Timing and evolution of Jurassic-Cretaceous granitoid magmatisms in the Mongol-Okhotsk belt and adjacent areas, NE Asia: Implications for transition from contractional crustal thickening to extensional thinning and geodynamic settings"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GPC...124...10M",
         "title":["Ocean bottom pressure variability in the Mediterranean Sea and its relationship with sea level from a numerical model"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GPC...124...22W",
         "title":["The environmental implications for dust in high-alpine snow and ice cores in Asian mountains"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GPC...124...30E",
         "title":["Impact of tropospheric sulphate aerosols on the terrestrial carbon cycle"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GPC...124...41C",
         "title":["Assessing the impact of El Niño Modoki on seasonal precipitation in Colombia"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GPC...124...62S",
         "title":["Future projection of Indian summer monsoon variability under climate change scenario: An assessment from CMIP5 climate models"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GPC...124...95D",
         "title":["Drought-induced weakening of growth-temperature associations in high-elevation Iberian pines"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Geomo.228..746S",
         "title":["Can the collapse of a fly ash heap develop into an air-fluidized flow? - Reanalysis of the Jupille accident (1961)"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Geomo.228..756C",
         "title":["Average landslide erosion rate at the watershed scale in southern Taiwan estimated from magnitude and frequency of rainfall"]},
       {
         "read_count":0.0,
         "bibcode":"2015Geomo.228..807.",
         "pubdate":"2015-01-00",
         "title":["International Association of Geomorphologists Association Internationale des Géomorphologues"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Geomo.229...17D",
         "title":["Condensation-corrosion speleogenesis above a carbonate-saturated aquifer: Devils Hole Ridge, Nevada"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Geomo.229...73O",
         "title":["Cave development by frost weathering"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Geomo.229..112K",
         "title":["Submerged karst landforms observed by multibeam bathymetric survey in Nagura Bay, Ishigaki Island, southwestern Japan"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Geomo.229..134P",
         "title":["Sulphuric acid speleogenesis and landscape evolution: Montecchio cave, Albegna river valley (Southern Tuscany, Italy)"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JMS...141...18E",
         "title":["The North Sea - A shelf sea in the Anthropocene"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JMS...141...45S",
         "title":["Net anthropogenic nitrogen inputs and nitrogen fluxes from Indian watersheds: An initial assessment"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JMS...141...59L",
         "title":["Response of nutrient transports to water-sediment regulation events in the Huanghe basin and its impact on the biogeochemistry of the Bohai"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JMS...141...71R",
         "title":["Rapid transport and high accumulation of amorphous silica in the Congo deep-sea fan: A preliminary budget"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JMS...141...80C",
         "title":["Source and distribution of organic matter in sediments in the SE Brazilian continental shelf influenced by river discharges: An approach using stable isotopes and molecular markers"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015PrOce.130...47C",
         "title":["Quasi-synoptic transport, budgets and water mass transformation in the Azores-Gibraltar Strait region during summer 2009"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GPC...124..107L",
         "title":["Future joint probability behaviors of precipitation extremes across China: Spatiotemporal patterns and implications for flood and drought hazards"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GPC...124..123Y",
         "title":["Parabolic dunes and their transformations under environmental and climatic changes: Towards a conceptual framework for understanding and prediction"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JPS...273...14B",
         "title":["The Solid Electrolyte Interphase a key parameter of the high performance of Sb in sodium-ion batteries: Comparative X-ray Photoelectron Spectroscopy study of Sb/Na-ion and Sb/Li-ion batteries"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JSG....70...12L",
         "title":["Lattice-preferred orientation of olivine found in diamond-bearing garnet peridotites in Finsch, South Africa and implications for seismic anisotropy"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JSG....70...23B",
         "title":["Fluid systems and fracture development during syn-depositional fold growth: An example from the Pico del Aguila anticline, Sierras Exteriores, southern Pyrenees, Spain"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JSG....70...39V",
         "title":["Exploring the relative contribution of mineralogy and CPO to the seismic velocity anisotropy of evaporites"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JSG....70...56K",
         "title":["Correlation of shape and size of methane bubbles in fine-grained muddy aquatic sediments with sediment fracture toughness"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JSG....70...65M",
         "title":["Primary vs. secondary curved fold axes: Deciphering the origin of the Aït Attab syncline (Moroccan High Atlas) using paleomagnetic data"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JSR....95..139B",
         "title":["Glycymeris bimaculata (Poli, 1795) - A new sclerochronological archive for the Mediterranean?"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015PrOce.130...91M",
         "title":["Measuring the Atlantic Meridional Overturning Circulation at 26°N"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015PrOce.130..205T",
         "title":["Zooplankton fecal pellets, marine snow, phytodetritus and the ocean's biological pump"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22514052P",
         "citation_count":0,
         "title":["3D Versus 1D Radiative Transfer Modeling of Planetary Nebulae"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22520205C",
         "citation_count":0,
         "title":["Humans Need Not Apply: Robotization of Kepler Planet Candidate Vetting"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015MPBu...42....1S",
         "title":["NIR Minor Planet Photometry from Burleith Observatory: 2014 February - June"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015MPBu...42...11M",
         "title":["My Thirty Years' Experience with the ALPO Minor Planet Section"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JPS...273..396B",
         "title":["Synthesis and electrochemical properties of novel LiFeTiO<SUB>4</SUB> and Li<SUB>2</SUB>FeTiO<SUB>4</SUB> polymorphs with the CaFe<SUB>2</SUB>O<SUB>4</SUB>-type structures"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525832L",
         "citation_count":0,
         "title":["The Subaru SEEDS Direct Imaging Survey for Planets of Early-Type Stars"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22533644B",
         "title":["Recovering Astrophysical Signals Lost in Noise: Light Curves of Background Objects in Kepler Data"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22533847G",
         "citation_count":0,
         "title":["SubLymE: The Sub-Lyman α Explorer"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22530606K",
         "citation_count":0,
         "title":["Deciphering thermal phase curves of tidally locked terrestrial planets"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.410..197K",
         "citation_count":0,
         "title":["The halogen (F, Cl, Br, I) and H<SUB>2</SUB>O systematics of Samoan lavas: Assimilated-seawater, EM2 and high-<SUP>3</SUP>He/<SUP>4</SUP>He components"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GeCoA.149....1V",
         "citation_count":0,
         "title":["Density and compressibility of the molten lunar picritic glasses: Implications for the roles of Ti and Fe in the structures of silicate melts"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GeCoA.149...32B",
         "citation_count":0,
         "title":["Strontium isotopes in otoliths of a non-migratory fish (slimy sculpin): Implications for provenance studies"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GeCoA.149..115D",
         "citation_count":0,
         "title":["Quantitative textural analysis of ilmenite in Apollo 17 high-titanium mare basalts"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GeCoA.149...21L",
         "citation_count":0,
         "title":["Empirical calibration of the oxygen isotope fractionation between quartz and Fe-Mg-chlorite"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GeCoA.149...46G",
         "citation_count":0,
         "title":["Textural properties of iron-rich phases in H ordinary chondrites and quantitative links to the degree of thermal metamorphism"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JASTP.122....9M",
         "citation_count":0,
         "title":["The year-to-year variability of the autumn transition dates in the mesosphere/lower thermosphere wind regime and its coupling with the dynamics of the stratosphere and troposphere"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JASTP.122...45P",
         "citation_count":0,
         "title":["Onset, advance and withdrawal of southwest monsoon over Indian subcontinent: A study from precipitable water measurement using ground based GPS receivers"]},
       {
         "read_count":8.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22510806P",
         "citation_count":0,
         "title":["The new MQ/AAO/Strasbourg mutli-wavelength and spectroscopic PNe database: MASPN"]},
       {
         "read_count":9.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22513401W",
         "citation_count":0,
         "title":["Back to the Beginning: The Rosetta Mission at Comet 67P/Churyumov-Gerasimenko"]},
       {
         "read_count":7.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22513906B",
         "citation_count":0,
         "title":["HST Search for Planetary Nebulae in Local Group Globular Clusters"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22514047H",
         "citation_count":0,
         "title":["Central Star Properties and C-N-O Abundances in Eight Galactic Planetary Nebulae from New HST/STIS Observations"]},
       {
         "read_count":5.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015A&A...573A.131T",
         "citation_count":0,
         "title":["Influence of celestial light on lunar surface brightness determinations: Application to earthshine studies"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22510501F",
         "citation_count":0,
         "title":["Increasing the sensitivity of Kepler to Earth-like exoplanets"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22510506R",
         "citation_count":0,
         "title":["Dissecting Kepler’s Objects of Interest: Complete Uniform MCMC modeling of the KOI Database"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22510508S",
         "citation_count":0,
         "title":["Planet Hunters 2 in the K2 Era"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22510704C",
         "citation_count":0,
         "title":["Features in the broad-band eclipse spectra of exoplanets: signal or noise?"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22510805A",
         "citation_count":0,
         "title":["Herschel Planetary Nebula Survey (HerPlaNS): First Detection of OH<SUP>+</SUP> in Planetary Nebulae"]},
       {
         "read_count":5.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22510809F",
         "citation_count":0,
         "title":["Binary Interactions and the Formation of Planetary Nebula"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525726R",
         "citation_count":0,
         "title":["High-precision ground-based observations of transiting exoplanets to detect their magnetic fields and undiscovered companions"]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22532801H",
         "citation_count":0,
         "read_count":5.0,
         "title":["Monitoring All the Sky All the Time with the Owens Valley Long Wavelength Array"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22533005P",
         "citation_count":0,
         "title":["Gemini Planet Imager Polarimetry of the Circumstellar Ring around HR 4796A"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AtmEn.100..193R",
         "citation_count":0,
         "title":["The photochemical production of organic nitrates from α-pinene and loss via acid-dependent particle phase hydrolysis"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015IJAsB..14..115B",
         "citation_count":0,
         "title":["Impact shocked rocks as protective habitats on an anoxic early Earth"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAESc..97...10Z",
         "citation_count":0,
         "title":["Evolution of the NE Qinghai-Tibetan Plateau, constrained by the apatite fission track ages of the mountain ranges around the Xining Basin in NW China"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAESc..97...51W",
         "title":["Early Cretaceous adakitic magmatism in the Dachagou area, northern Lhasa terrane, Tibet: Implications for slab roll-back and subsequent slab break-off of the lithosphere of the Bangong-Nujiang Ocean"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAfES.101..350M",
         "citation_count":0,
         "title":["Structural control of fluvial drainage in the western domain of the Cape Fold Belt, South Africa"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JPS...273..389S",
         "citation_count":0,
         "title":["Lithium ion conductive Li<SUB>1.5</SUB>Al<SUB>0.5</SUB>Ge<SUB>1.5</SUB>(PO<SUB>4</SUB>)<SUB>3</SUB> based inorganic-organic composite separator with enhanced thermal stability and excellent electrochemical performances in 5 V lithium ion batteries"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JQSRT.150....1V",
         "citation_count":0,
         "title":["Topical issue on optical particle characterization and remote sensing of the atmosphere: Part I"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22520701D",
         "citation_count":0,
         "title":["The orbital dynamics and long-term stability of planetary systems"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22522401J",
         "citation_count":0,
         "title":["Debris from giant impacts - signatures of forming and dynamic planetary systems"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525820G",
         "citation_count":0,
         "title":["Illumination Profile & Dispersion Variation Effects on Radial Velocity Measurements"]},
       {
         "read_count":5.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525905S",
         "citation_count":0,
         "title":["High Contrast Science Program for the Exo-C Space Telescope Mission"]},
       {
         "read_count":5.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22530604J",
         "citation_count":0,
         "title":["Validation of a Warm Jupiter Transiting a Rapidly Rotating Star"]},
       {
         "read_count":5.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22522301S",
         "citation_count":0,
         "title":["Caught in the Act: Imaging the Disk and Outflows in V Hya, a carbon-rich AGB star in transition to a Bipolar Pre-Planetary Nebula"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22513720M",
         "citation_count":0,
         "title":["Planetary Embryo Bow Shocks as a Mechanism for Chondrule Formation"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22514048M",
         "citation_count":0,
         "title":["Analysis of Co-spatial UV-Optical STIS Spectra of Planetary Nebulae From HST Cycle 19 GO 12600"]},
       {
         "read_count":7.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22514053R",
         "citation_count":0,
         "title":["The Close Binary Central Star of the Planetary Nebula PHR J1602-4127"]},
       {
         "read_count":5.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525819T",
         "citation_count":0,
         "title":["Giant Planet Candidates, Brown Dwarfs, and Binaries from the SDSS-III MARVELS Planet Survey."]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22534923V",
         "citation_count":0,
         "title":["Herschel Observations of Dusty Debris Disks"]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22540602Z",
         "citation_count":0,
         "read_count":3.0,
         "title":["Uncovering the Chemistry of Earth-like Planets"]},
       {
         "read_count":7.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22540603P",
         "citation_count":0,
         "title":["The Prevalence of Earth-size Planets Orbiting Sun-like Stars"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22540901M",
         "citation_count":0,
         "title":["Early Science Results from Dharma Planet Survey (DPS), a Robotic, High Cadence and High Doppler Precision Survey of Close-in Super-Earths"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22540903G",
         "citation_count":0,
         "title":["The SDSS-III DR12 MARVELS radial velocity data release: the first data release from the multiple object Doppler exoplanet survey"]},
       {
         "read_count":5.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22541405P",
         "citation_count":0,
         "title":["New Exozodi and Asteroid Belt Analogs using WISE"]},
       {
         "read_count":6.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22541601W",
         "citation_count":0,
         "title":["ALMA Presents a Transformational View of the Universe"]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22542002M",
         "citation_count":0,
         "read_count":5.0,
         "title":["The Occurrence of Compact Multiple Exoplanetary Systems Orbiting Mid-M Dwarf Stars"]},
       {
         "read_count":6.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.410..140U",
         "citation_count":0,
         "title":["Meteoritic evidence for a previously unrecognized hydrogen reservoir on Mars"]},
       {
         "read_count":7.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Icar..246..247F",
         "citation_count":0,
         "title":["Seasonal variations in Pluto's atmospheric tides"]},
       {
         "pubdate":"2015-01-00",
         "read_count":34.0,
         "bibcode":"2015Icar..246..360P",
         "citation_count":0,
         "title":["Ejecta transfer in the Pluto system"]},
       {
         "read_count":14.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Icar..245...80N",
         "citation_count":0,
         "title":["Ionization of the venusian atmosphere from solar and galactic cosmic rays"]},
       {
         "read_count":9.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Icar..245..177A",
         "citation_count":0,
         "title":["Search for hydrogen peroxide in the Martian atmosphere by the Planetary Fourier Spectrometer onboard Mars Express"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Icar..245..247B",
         "citation_count":0,
         "title":["Forming Ganymede's grooves at smaller strain: Toward a self-consistent local and global strain history for Ganymede"]},
       {
         "read_count":8.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Icar..246..197T",
         "citation_count":0,
         "title":["On the state of methane and nitrogen ice on Pluto and Triton: Implications of the binary phase diagram"]},
       {
         "read_count":5.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Icar..246..298S",
         "citation_count":0,
         "title":["Transient atmospheres on Charon and water-ice covered KBOs resulting from comet impacts"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JPS...274..471N",
         "title":["Li<SUB>2</SUB>S nanocomposites underlying high-capacity and cycling stability in all-solid-state lithium-sulfur batteries"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015P&SS..105...65W",
         "citation_count":0,
         "title":["Dynamical modelling of river deltas on Titan and Earth"]},
       {
         "pubdate":"2015-00-00",
         "bibcode":"2015ASSL..415..381G",
         "citation_count":0,
         "read_count":41.0,
         "title":["The Dynamics of Eruptive Prominences"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GeCoA.148..496C",
         "citation_count":0,
         "title":["Corrigendum to 'Evidence for shock heating and constraints on Martian surface temperatures revealed by <SUP>40</SUP>Ar/<SUP>39</SUP>Ar thermochronometry of Martian meteorites' [Geochim. Cosmochim. Acta (2010) 6900-6920]"]},
       {
         "read_count":8.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Icar..246..206S",
         "citation_count":0,
         "title":["Pluto and Charon's UV spectra from IUE to New Horizons"]},
       {
         "read_count":19.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Icar..246..220O",
         "citation_count":1,
         "title":["Evidence that Pluto's atmosphere does not collapse from occultations including the 2013 May 04 event"]},
       {
         "read_count":10.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Natur.517..339J",
         "citation_count":0,
         "title":["Impact jetting as the origin of chondrules"]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015NewA...34...27Y",
         "citation_count":0,
         "read_count":29.0,
         "title":["Deep optical survey of the stellar content of Sh2-311 region"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015ApSS..324..380K",
         "title":["Facile fabrication of hydrophobic surfaces on mechanically alloyed-Mg/HA/TiO<SUB>2</SUB>/MgO bionanocomposites"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015QSRv..107...11P",
         "title":["Quaternary glaciations: from observations to theories"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015QSRv..107..182D",
         "citation_count":0,
         "title":["Tropical tales of polar ice: evidence of Last Interglacial polar ice sheet retreat recorded by fossil reefs of the granitic Seychelles islands"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22520704B",
         "citation_count":0,
         "title":["Long-lived Chaotic Orbital Evolution of Exoplanets in Mean Motion Resonances with Mutual Inclinations"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525814A",
         "citation_count":0,
         "title":["Direct Imaging of Radial Velocity Exoplanets with the WFIRST-AFTA Coronagraph"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22522404H",
         "citation_count":0,
         "title":["Structures, Cooling, and Mass Loss for Super-Earths and Sub-Neptunes"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Geomo.228..765E",
         "title":["Large-scale dam removal on the Elwha River, Washington, USA: River channel and floodplain geomorphic change"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JSSCh.221..132B",
         "title":["Synthesis, structural and electrical properties of a new cobalt arsenate NaCo<SUB>2</SUB>As<SUB>3</SUB>O<SUB>10</SUB>"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JSSCh.221..278B",
         "citation_count":0,
         "title":["Synthesis, crystal structure and electrical proprieties of new phosphate KCoP<SUB>3</SUB>O<SUB>9</SUB>"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22544902D",
         "citation_count":0,
         "title":["The Ages of Early-Type Stars: Strömgren Photometric Methods Calibrated, Validated, Tested, and Applied to Hosts and Prospective Hosts of Directly Imaged Exoplanets"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015CG.....74...87R",
         "citation_count":0,
         "title":["Inflow-outflow boundary conditions along arbitrary directions in Cartesian lake models"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AtmRe.152...43N",
         "citation_count":1,
         "title":["Regional climate model simulations of extreme air temperature in Greece. Abnormal or common records in the future climate?"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Geomo.228....1G",
         "citation_count":0,
         "title":["Nile Delta exhibited a spatial reversal in the rates of shoreline retreat on the Rosetta promontory comparing pre- and post-beach protection"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015QSRv..107....1A",
         "citation_count":0,
         "title":["A perspective on model-data surface temperature comparison at the Last Glacial Maximum"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015QSRv..107..243L",
         "citation_count":0,
         "title":["Toward a late Holocene glacial chronology for the eastern Nyainqêntanglha Range, southeastern Tibet"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.409..290R",
         "citation_count":0,
         "title":["No late Quaternary strike-slip motion along the northern Karakoram fault"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.409..319X",
         "citation_count":0,
         "title":["Origin of Fe<SUP>3+</SUP> in Fe-containing, Al-free mantle silicate perovskite"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.410...84G",
         "citation_count":1,
         "title":["Cadmium-isotopic evidence for increasing primary productivity during the Late Permian anoxic event"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.409..203B",
         "citation_count":0,
         "title":["Evolution of basal crevasses links ice shelf stability to ocean forcing"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.409..232H",
         "citation_count":0,
         "title":["Late Devonian carbonate magnetostratigraphy from the Oscar and Horse Spring Ranges, Lennard Shelf, Canning Basin, Western Australia"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.409..265M",
         "citation_count":0,
         "title":["Density-depth model of the continental wedge at the maximum slip segment of the Maule Mw8.8 megathrust earthquake"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.409..193S",
         "citation_count":0,
         "title":["Topography of upper mantle seismic discontinuities beneath the North Atlantic: The Azores, Canary and Cape Verde plumes"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.409..212L",
         "citation_count":0,
         "title":["Processes controlling δ<SUP>7</SUP>Li in rivers illuminated by study of streams and groundwaters draining basalts"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.409..278M",
         "citation_count":0,
         "title":["Stability of rift axis magma reservoirs: Spatial and temporal evolution of magma supply in the Dabbahu rift segment (Afar, Ethiopia) over the past 30 kyr"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.410...97A",
         "citation_count":0,
         "title":["Synchroneity of cratonic burial phases and gaps in the kimberlite record: Episodic magmatism or preservational bias?"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.410..128W",
         "citation_count":0,
         "title":["Disequilibrium degassing model determination of the <SUP>3</SUP>He concentration and <SUP>3</SUP>He/<SUP>22</SUP>Ne of the MORB and OIB mantle sources"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AcAau.106...63K",
         "citation_count":0,
         "title":["A lander mission to probe subglacial water on Saturn's moon Enceladus for life"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AdSpR..55...24B",
         "citation_count":0,
         "title":["Global geodetic observatories"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AdSpR..55...60L",
         "citation_count":0,
         "title":["Evapotranspiration estimated by using datasets from the Chinese FengYun-2D geostationary meteorological satellite over the Yellow River source area"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AdSpR..55...72T",
         "citation_count":0,
         "title":["Empirical model of the gravitational field generated by the oceanic lithosphere"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AdSpR..55..184K",
         "citation_count":0,
         "title":["Spatio-temporal characteristics of the Equatorial Ionization Anomaly (EIA) in the East African region via ionospheric tomography during the year 2012"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AdSpR..55..428S",
         "citation_count":0,
         "title":["Magnetic bubble for CR experiments in space"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AdSpR..55..491C",
         "citation_count":0,
         "title":["Navigation of Chang'E-2 asteroid exploration mission and the minimum distance estimation during its fly-by of Toutatis"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.409...61P",
         "citation_count":0,
         "title":["Mantle temperature as a control on the time scale of thermal evolution of extensional basins"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.409...96Q",
         "citation_count":0,
         "title":["Slab detachment under the Eastern Alps seen by seismic anisotropy"]},
       {
         "read_count":5.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.409..133L",
         "citation_count":0,
         "title":["The surge of great earthquakes from 2004 to 2014"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.409..339W",
         "citation_count":0,
         "title":["Isotope fractionation induced by phase transformation: First-principles investigation for Mg<SUB>2</SUB>SiO<SUB>4</SUB>"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.410...12M",
         "citation_count":0,
         "title":["Lithospheric expression of cenozoic subduction, mesozoic rifting and the Precambrian Shield in Venezuela"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.410..117D",
         "citation_count":0,
         "title":["Slow and delayed deformation and uplift of the outermost subduction prism following ETS and seismogenic slip events beneath Nicoya Peninsula, Costa Rica"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.410..186M",
         "citation_count":0,
         "title":["Post-depositional remanent magnetization lock-in depth in precisely dated varved sediments assessed by archaeomagnetic field models"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Geomo.228..356G",
         "title":["Direct reconstruction of glacier bedrock from known free surface data using the one-dimensional shallow ice approximation"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Geomo.228..628D",
         "citation_count":0,
         "title":["Estimating long-term sediment export using a seasonal rainfall-dependent hydrological model in the Glonn River basin, Germany"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22533704G",
         "citation_count":0,
         "title":["Preliminary Design of the iLocater Acquisition Camera for the LBT"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22512207S",
         "citation_count":0,
         "title":["Statistical Eclipses of Kepler Neptune-like Candidates"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AdSpR..55..297S",
         "citation_count":0,
         "title":["Effects of perturbations in Coriolis and centrifugal forces on the locations and stability of libration points in Robe's circular restricted three-body problem under oblate-triaxial primaries"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AdSpR..55..477D",
         "citation_count":0,
         "title":["On the size distribution functions and their application in regolith studies"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.409...23L",
         "citation_count":0,
         "title":["High silica granites: Terminal porosity and crystal settling in shallow magma chambers"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.409...49E",
         "citation_count":0,
         "title":["Dynamics of an open basaltic magma system: The 2008 activity of the Halema'uma'u Overlook vent, Kīlauea Caldera"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.409..147F",
         "citation_count":0,
         "title":["An observational and thermodynamic investigation of carbonate partial melting"]},
       {
         "read_count":15.0,
         "pubdate":"2015-00-00",
         "bibcode":"2015ASSL..411...37L",
         "citation_count":0,
         "title":["Magnetic Fields and Winds of Planet Hosting Stars"]},
       {
         "read_count":10.0,
         "pubdate":"2015-00-00",
         "bibcode":"2015ASSL..411..239B",
         "citation_count":0,
         "title":["Alfv&eacute;n Radius: A Key Parameter for Astrophysical Magnetospheres"]},
       {
         "pubdate":"2015-00-00",
         "read_count":44.0,
         "bibcode":"2015ASSL..412..113O",
         "citation_count":2,
         "title":["Instabilities in the Envelopes and Winds of Very Massive Stars"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.409..175W",
         "citation_count":0,
         "title":["The Eltanin impact and its tsunami along the coast of South America: Insights for potential deposits"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GeCoA.148..378F",
         "citation_count":0,
         "title":["Calcium isotopic evidence for rapid recrystallization of bulk marine carbonates and implications for geochemical proxies"]},
       {
         "pubdate":"2015-01-00",
         "read_count":108.0,
         "bibcode":"2015Icar..246..285J",
         "citation_count":0,
         "title":["Production of N<SUB>2</SUB> Vegard-Kaplan and Lyman-Birge-Hopfield emissions on Pluto"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015ECSS..152...65W",
         "title":["Evidencing a regime shift in the North Sea using early-warning signals as indicators of critical transitions"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Icar..246...93Z",
         "citation_count":0,
         "title":["A meta-analysis of coordinate systems and bibliography of their use on Pluto from Charon's discovery to the present day"]},
       {
         "read_count":8.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Icar..246..369B",
         "citation_count":0,
         "title":["New Horizons: Long-range Kuiper Belt targets observed by the Hubble Space Telescope"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GeCoA.149..152P",
         "citation_count":0,
         "title":["Mass-dependent sulfur isotope fractionation during reoxidative sulfur cycling: A case study from Mangrove Lake, Bermuda"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Geomo.228..398W",
         "citation_count":0,
         "title":["Landslide inventories for climate impacts research in the European Alps"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Geomo.228..421W",
         "title":["Bedrock fault scarp history: Insight from t-LiDAR backscatter behaviour and analysis of structure changes"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Icar..246..226G",
         "citation_count":0,
         "title":["Observations of a successive stellar occultation by Charon and graze by Pluto in 2011: Multiwavelength SpeX and MORIS data from the IRTF"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22512401F",
         "citation_count":0,
         "title":["Super-Earths, Warm Neptunes, and Hot Jupiters: Transmission Spectroscopy for Comparative Planetology"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22513713T",
         "citation_count":0,
         "title":["A Hazy Situation: Using exoplanet retrieval techniques to characterize Titan's atmosphere from a Cassini transit spectrum"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22512407H",
         "citation_count":0,
         "title":["Highly Evolved Exoplanet Atmospheres"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22513827M",
         "citation_count":0,
         "title":["A comprehensive statistical assessment of star-planet interaction"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525733C",
         "citation_count":0,
         "title":["Inferring Planet Occurrence Rates With a Q1-Q16 Kepler Planet Candidate Catalog Produced by a Machine Learning Classifier"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525735G",
         "citation_count":0,
         "title":["Identifying transiting planets candidates in Kepler data using PyKE"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525719W",
         "citation_count":0,
         "title":["Effects of Photoevaporation on Planet Migration"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525816L",
         "citation_count":0,
         "title":["Finding the Needle in the Haystack: High-Fidelity Models of Planetary Systems for Simulating Exoplanet Observations"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525904C",
         "title":["Imaging Exoplanets with the Exo-S Starshade Mission: Baseline Design"]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22526004M",
         "citation_count":0,
         "read_count":3.0,
         "title":["Habitability of Planets Orbiting Binaries Consisting of Solar Mass Twins"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22531102B",
         "citation_count":0,
         "title":["Space mission and instrument design to image the Habitable Zone of Alpha Centauri"]},
       {
         "read_count":5.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22531308S",
         "citation_count":0,
         "title":["Observational Signatures of MRI-driven Turbulence in Protoplanetary Disks: Connecting Numerical Simulations with ALMA"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22543809S",
         "citation_count":0,
         "title":["Thermal Structure of WASP-43b from Phase-Resolved Emission Spectroscopy"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAfES.101..253J",
         "citation_count":0,
         "title":["Petrogenesis of synorogenic diorite-granodiorite-granite complexes in the Damara Belt, Namibia: Constraints from U-Pb zircon ages and Sr-Nd-Pb isotopes"]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015OcDyn..65...49K",
         "read_count":0.0,
         "title":["Optimized boundary conditions at staircase-shaped coastlines"]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22524204R",
         "read_count":0.0,
         "title":["An Update on the NASA Planetary Science Division Research and Analysis Program"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AdSpR..55....2C",
         "citation_count":1,
         "title":["Towards the 1 mm/y stability of the radial orbit error at regional scales"]},
       {
         "read_count":0.0,
         "pubdate":"2015-00-00",
         "bibcode":"2015hae..book.1855S",
         "title":["Babylonian Observational and Predictive Astronomy"]},
       {
         "read_count":0.0,
         "pubdate":"2015-00-00",
         "bibcode":"2015hae..book.1863O",
         "title":["Babylonian Mathematical Astronomy"]},
       {
         "read_count":0.0,
         "pubdate":"2015-00-00",
         "bibcode":"2015hae..book.1991S",
         "title":["Vākya System of Astronomy"]},
       {
         "read_count":0.0,
         "pubdate":"2015-00-00",
         "bibcode":"2015hae..book.2001R",
         "title":["Kerala School of Astronomy"]},
       {
         "read_count":0.0,
         "pubdate":"2015-00-00",
         "bibcode":"2015hae..book.2043S",
         "citation_count":0,
         "title":["Observation of Celestial Phenomena in Ancient China"]},
       {
         "read_count":0.0,
         "pubdate":"2015-00-00",
         "bibcode":"2015hae..book.2059S",
         "title":["Chinese Calendar and Mathematical Astronomy"]},
       {
         "read_count":0.0,
         "pubdate":"2015-00-00",
         "bibcode":"2015hae..book.2223C",
         "title":["Australian Aboriginal Astronomy and Cosmology"]},
       {
         "read_count":0.0,
         "pubdate":"2015-00-00",
         "bibcode":"2015hae..book.1625L",
         "title":["Greco-Roman Astrometeorology"]},
       {
         "read_count":0.0,
         "pubdate":"2015-00-00",
         "bibcode":"2015hae..book.1743I",
         "title":["Light - Shadow Interactions in Italian Medieval Churches"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.409...43S",
         "citation_count":0,
         "title":["Nanoscale evidence for uranium mobility in zircon and the discordance of U-Pb chronometers"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.409...71F",
         "citation_count":0,
         "title":["The Canary Islands hot spot: New insights from 3D coupled geophysical-petrological modelling of the lithosphere and uppermost mantle"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GeoJI.200..664T",
         "citation_count":0,
         "title":["Geodynamo model and error parameter estimation using geomagnetic data assimilation"]},
       {
         "read_count":0.0,
         "pubdate":"2015-00-00",
         "bibcode":"2015epl..book.....H",
         "title":["Encyclopedia of Planetary Landforms"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22512201W",
         "citation_count":0,
         "title":["The Power of a Planet Population: Kepler's Super-Earth Compositions, Mass-Radius Relation, and Host Star Multiplicity"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22512404M",
         "citation_count":0,
         "title":["The Thermal Emission and Albedo of Super-Earths with Flat Transmission Spectra"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22513804W",
         "citation_count":0,
         "title":["Dynamical Evolution of the Alpha and Proxima Centauri Triple System"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22520703V",
         "citation_count":0,
         "title":["Crushed Exoplanet systems: Did it happen here?"]},
       {
         "read_count":5.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22522406T",
         "citation_count":0,
         "title":["Planets migrating into stars: Rates and Signature"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22513001M",
         "citation_count":0,
         "title":["Reliable Radii for M Dwarf Stars"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525702V",
         "citation_count":0,
         "title":["Fundamental Parameters of the Two Hall-of-Famers HD 189733 and HD 209458"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525711Z",
         "citation_count":0,
         "title":["Dayside emission spectrum of Kepler-13Ab from HST and ground-based observations"]},
       {
         "read_count":5.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525727E",
         "citation_count":0,
         "title":["Connecting historical disk interactions with current planetary system architectures"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22510502M",
         "citation_count":0,
         "title":["Implications for the False-positive Rate in Kepler Planet Systems from Transit Duration Ratios"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22510802M",
         "title":["Emerging Trends Gleaned from Central Star and Hot Bubble X-ray Emission of ChanPlaNS Planetary Nebulae"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22511503K",
         "citation_count":0,
         "title":["Comparing Accretion Histories of Earth, Mars, and Theia Analogs"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525718T",
         "citation_count":0,
         "title":["Examining the Relative Compositions of Giant Planets and their Parent Stars"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525721K",
         "citation_count":0,
         "title":["The Impact of Stellar Multiplicity on Planet Occurrence"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525730S",
         "citation_count":0,
         "title":["Analyzing Mass Loss and Tidal Circularization as a Source for Sustained Eccentric Orbits in Hot Jupiters"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525811N",
         "citation_count":0,
         "title":["Performance characterization of a PIAA complex focal plane mask"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525830M",
         "citation_count":0,
         "title":["Differential Astrometry to detect giant planets around A-stars"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525708V",
         "citation_count":0,
         "title":["Metallicity Analysis of Kepler-65, Kepler-93, Kepler-99, Kepler-102, Kepler-406, and Kepler-409"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22533006N",
         "citation_count":0,
         "title":["Modeling Collisions in Circumstellar Debris Disks with SMACK"]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22534911M",
         "citation_count":0,
         "read_count":6.0,
         "title":["Understanding Planetary Compositions Using Elemental Ratios in Protoplanetary Disks"]},
       {
         "read_count":5.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22534915P",
         "citation_count":0,
         "title":["Dust Depletion and Large Scale Asymmetries in Transitional Disks"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22545302N",
         "citation_count":0,
         "title":["To the origin problem of the Moon"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AdSpR..55..211S",
         "citation_count":0,
         "title":["The storm of March 1989 revisited: A fresh look at the event"]},
       {
         "read_count":5.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22540802D",
         "citation_count":0,
         "title":["Short-period terrestrial planets and radial velocity stellar jitter."]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22541004M",
         "read_count":2.0,
         "title":["Engaging Scientists in Meaningful E/PO: How the NASA SMD E/PO Community Addresses the needs of Underrepresented Audiences through NASA Science4Girls and Their Families"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22541501C",
         "citation_count":0,
         "title":["A Joint Approach to the Study of S-Type and P-Type Habitable Zones in Binary Systems: New Results in the View of 3-D Planetary Climate Models"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22542006Q",
         "citation_count":0,
         "title":["Constraints on planet formation from Kepler’s multiple planet systems"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22542305J",
         "citation_count":0,
         "title":["Data reduction and astrometric calibration of a starshade test using real starlight"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.409...32S",
         "citation_count":0,
         "title":["Timing and mechanism for intratest Mg/Ca variability in a living planktic foraminifer"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GeCoA.149..103W",
         "citation_count":0,
         "title":["Net subterranean estuarine export fluxes of dissolved inorganic C, N, P, Si, and total alkalinity into the Jiulong River estuary, China"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015MPBu...42...22P",
         "citation_count":0,
         "title":["Minor Planets at Unusually Favorable Elongations in 2015"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015MPBu...42...75B",
         "title":["Rotation Period Determinations for 1095 Tulipa, 1626 Sadeya 2132 Zhukov, and 7173 Sepkoski"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015PEPI..238...23K",
         "citation_count":0,
         "title":["Tornillos modeled as self-oscillations of fluid filling a cavity: Application to the 1992-1993 activity at Galeras volcano, Colombia"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015PEPI..238...51K",
         "citation_count":0,
         "title":["A detailed seismic anisotropy study during the 2011-2012 unrest period in the Santorini Volcanic Complex"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22510804L",
         "title":["The HerPlaNS far-IR photometric survey of Planetary Nebulae and its contribution to the Emerging Multi-wavelength View"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525902T",
         "title":["Exoplanet Science with a Starshade: Exo-S Study Results"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22526001D",
         "citation_count":0,
         "title":["On the thermal, magnetic, and orbital evolution of tidally heated Earth-mass exoplanets"]},
       {
         "read_count":0.0,
         "pubdate":"2015-00-00",
         "bibcode":"2015ASSL..409.....F",
         "title":["Camille Flammarions' The Planet Mars"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AtmRe.151..200A",
         "citation_count":1,
         "title":["A climatological study of fog in Japan based on event data"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015CG.....74..110M",
         "title":["A visual LISP program for voxelizing AutoCAD solid models"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.409...89A",
         "citation_count":0,
         "title":["Basin-scale partitioning of Greenland ice sheet mass balance components (2007-2011)"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.410...25W",
         "citation_count":0,
         "title":["High-temperature miscibility of iron and rock during terrestrial planet formation"]},
       {
         "read_count":9.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Icar..245..112R",
         "citation_count":0,
         "title":["Space erosion and cosmic ray exposure ages of stony meteorites"]},
       {
         "read_count":6.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Icar..245..263B",
         "citation_count":0,
         "title":["Origin of the outer layer of martian low-aspect ratio layered ejecta craters"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JMMM..374..427S",
         "citation_count":0,
         "title":["Processing of Mn-Al nanostructured magnets by spark plasma sintering and subsequent rapid thermal annealing"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015PEPI..238....1J",
         "citation_count":0,
         "title":["Detection of metastable olivine wedge in the western Pacific slab and its geodynamic implications"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015PEPI..238...34L",
         "citation_count":0,
         "title":["Dynamic triggering of earthquakes is promoted by crustal heterogeneities and bimaterial faults"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015PEPI..238....8B",
         "citation_count":0,
         "title":["Assimilating lithosphere and slab history in 4-D Earth models"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015PEPI..238...42M",
         "citation_count":0,
         "title":["Elasticity of superhydrous phase, B, Mg<SUB>10</SUB>Si<SUB>3</SUB>O<SUB>14</SUB>(OH)<SUB>4</SUB>"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015QSRv..107..270S",
         "title":["Response to: Comment on 'Synchronous records of pCO<SUB>2</SUB> and Δ<SUP>14</SUP>C suggest rapid, ocean-derived pCO<SUB>2</SUB> fluctuations at the onset of Younger Dryas' (Steinthorsdottir et al., 2014, Quaternary Science Reviews 99, 84-96)"]},
       {
         "read_count":0.0,
         "pubdate":"2015-00-00",
         "bibcode":"2015pmpp.book.....R",
         "title":["Planet Mercury: From Pale Pink Dot to Dynamic World"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015NIMPB.342..200S",
         "citation_count":0,
         "title":["An analysis of radiation effects on NdFeB permanent magnets"]},
       {
         "read_count":6.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015PEPI..238...89G",
         "citation_count":0,
         "title":["New Late Neolithic (c. 7000-5000 BC) archeointensity data from Syria. Reconstructing 9000 years of archeomagnetic field intensity variations in the Middle East"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22512208P",
         "citation_count":0,
         "title":["Preparing for the Kepler K2 Microlensing Survey: A Call to Arms"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22513902D",
         "citation_count":0,
         "title":["Cospatial Longslit UV-Optical Spectra of Ten Galactic Planetary Nebulae with HST STIS: Description of observations, global emission-line measurements, and empirical CNO abundances"]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22520202P",
         "citation_count":0,
         "read_count":4.0,
         "title":["Target Selection for the TESS Mission"]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525704N",
         "citation_count":0,
         "read_count":5.0,
         "title":["A Pair of Massive Planets Orbiting an Oscillating Kepler Red Giant in a Binary System"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525725T",
         "citation_count":0,
         "title":["Direct imaging of exoplanets around multiple star systems"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525802J",
         "title":["Exoplanets with LSST: Period Recoverability of Transiting Hot Jupiters"]},
       {
         "read_count":5.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22530605D",
         "citation_count":0,
         "title":["Deriving stellar inclination of slow rotators using stellar activity signal"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22531106P",
         "citation_count":0,
         "title":["An Accurate Flux Density Scale from 50 MHz to 50 GHz"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22532301Z",
         "citation_count":0,
         "title":["Constraining the Thermal Structure, Abundances, and Dynamics of the Exoplanet HD 209458b"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525713D",
         "citation_count":0,
         "title":["Secondary Eclipse Observations of the Hot-Jupiter WASP-26b"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22510707C",
         "title":["Magnetohydrodynamic Simulations of Hot Jupiter Thermospheres"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22510807K",
         "citation_count":0,
         "title":["What Are M31 Disk Planetary Nebulae Trying to Tell Us?"]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22513817N",
         "citation_count":0,
         "read_count":3.0,
         "title":["Rotation periods for nearby, mid-to-late M dwarfs estimated from the MEarth Project"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22513903S",
         "citation_count":0,
         "title":["New CNO Elemental Abundances in Planetary Nebulae from Spatially Resolved UV/Optical Emission Lines"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22513907M",
         "citation_count":0,
         "title":["Exploring the Late Evolutionary Stages of Sun-like Stars with LSST"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22520702N",
         "citation_count":0,
         "title":["Orbital Architectures of Dynamically Complex Exoplanet Systems"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22541307S",
         "citation_count":0,
         "title":["On-sky validation of an optimal LQG control with vibration mitigation: from the CANARY Multi-Object Adaptive Optics demonstrator to the Gemini Multi-Conjugated Adaptive Optics facility."]},
       {
         "read_count":5.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22534924R",
         "citation_count":0,
         "title":["Stellar Multiplicity in the DEBRIS disk sample"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22540704L",
         "citation_count":0,
         "title":["Extreme Water Loss and Abiotic O<SUB>2</SUB> Buildup On Planets Throughout the Habitable Zones of M Dwarfs"]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22542005N",
         "citation_count":0,
         "read_count":8.0,
         "title":["Friends of hot Jupiters II: No correspondence between hot Jupiter spin-orbit misalignment and the incidence of directly imaged stellar companions"]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22511201D",
         "citation_count":0,
         "read_count":0.0,
         "title":["Absolute Optical Photometry and a Photometric Metallicity Relation for the Nearby Cool Stars from the MEarth Project"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525741S",
         "citation_count":0,
         "title":["KIC 12557548 and Similar Stars as SETI Targets"]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22533836S",
         "citation_count":0,
         "read_count":5.0,
         "title":["Astro-H: New Spectral Features Seen in High-Resolution X-rays"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22543811M",
         "citation_count":0,
         "title":["Building massive, tightly packed planetary systems by in-situ accretion of pebbles"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...225.9102G",
         "title":["John Herschel, Charles Lyell, and the planet Earth"]},
       {
         "read_count":7.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22513843A",
         "citation_count":0,
         "title":["Brown dwarf science at Project 1640: the case of HD 19467 B"]},
       {
         "read_count":8.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22520201R",
         "citation_count":0,
         "title":["The Transiting Exoplanet Survey Satellite: Mission Status"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22533828Z",
         "citation_count":0,
         "title":["A Shaped Pupil Lyot Coronagraph for WFIRST-AFTA"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22533842P",
         "citation_count":0,
         "title":["An Evolvable Space Telescope for Future Astronomical Missions"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22534514S",
         "citation_count":0,
         "title":["Hydrodynamic Simulations of the Interaction between Giant Stars and Planets"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22534906F",
         "citation_count":0,
         "title":["Characterizing a Young Protoplanetary Disk in the Orion Nebula Cluster"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22540801S",
         "citation_count":0,
         "title":["Characterizing the shortest-period planets found by Kepler"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Tectp.638....1R",
         "citation_count":1,
         "title":["A review of mechanical models of dike propagation: Schools of thought, results and future directions"]},
       {
         "read_count":8.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.409..252H",
         "citation_count":0,
         "title":["Water in the Moon's interior: Truth and consequences"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.410...34B",
         "citation_count":0,
         "title":["Pb-isotopic evidence for an early, enriched crust on Mars"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Icar..245..379V",
         "citation_count":0,
         "title":["Io: Heat flow from small volcanic features"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.410...42M",
         "citation_count":0,
         "title":["Constraints on the mantle mineralogy of an ultra-slow ridge: Hafnium isotopes in abyssal peridotites and basalts from the 9-25°E Southwest Indian Ridge"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JQSRT.150...87V",
         "citation_count":1,
         "title":["Light-scattering evolution from particles to regolith"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22534916L",
         "citation_count":0,
         "title":["AU Mic’s Debris Disk Chemistry Revealed Using Spatially Resolved Spectroscopy"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22510505S",
         "citation_count":0,
         "title":["The distribution of period ratios in Kepler planetary systems"]},
       {
         "read_count":6.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22510702B",
         "citation_count":0,
         "title":["Observations and Thermochemical Calculations for Hot-Jupiter Atmospheres"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525705F",
         "citation_count":0,
         "title":["The Properties of Exomoons Around the Habitable Zone Planet, Kepler 22b"]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525732B",
         "citation_count":0,
         "read_count":4.0,
         "title":["MINERVA-Red: A Census of Planets Orbiting the Nearest Low-mass Stars to the Sun"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22534407G",
         "citation_count":0,
         "title":["Searching for IR Excesses around Li-Rich and Rapidly Rotating K Giants Using WISE"]},
       {
         "read_count":5.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AdSpR..55..170G",
         "citation_count":0,
         "title":["Validation of plasmasphere electron density reconstructions derived from data on board CHAMP by IMAGE/RPI data"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.409..299G",
         "citation_count":0,
         "title":["Stability of iron crystal structures at 0.3-1.5 TPa"]},
       {
         "read_count":6.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GeoJI.200..173B",
         "citation_count":0,
         "title":["On the decorrelation filtering of RL05 GRACE data for global applications"]},
       {
         "read_count":6.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Icar..245..198W",
         "citation_count":0,
         "title":["Minimum effective area for high resolution crater counting of martian terrains"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Icar..246..339G",
         "citation_count":0,
         "title":["The sailboat island and the New Horizons trajectory"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JPS...274..424Y",
         "title":["Formulation of flowable anolyte for redox flow batteries: Rheo-electrical study"]},
       {
         "read_count":12.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015MNRAS.446..705V",
         "citation_count":0,
         "title":["A fast method for estimation of the impact probability of near-Earth objects"]},
       {
         "read_count":9.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22511202W",
         "citation_count":0,
         "title":["M Dwarf Multiplicity in the Solar Neighborhood"]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22513808S",
         "citation_count":0,
         "read_count":4.0,
         "title":["Preliminary M-dwarf Binary Statistics from Kepler"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22514049M",
         "citation_count":0,
         "title":["The Detection of Neutron-Capture Elements in Magellanic Cloud Planetary Nebulae"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525734L",
         "citation_count":0,
         "title":["Estimates of Planetary System Properties using TTV data and Least-Excited Orbital Configurations"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525817W",
         "citation_count":0,
         "title":["A re-analysis of planet candidates common to the HARPS and Anglo-Australian Planet Search"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22513811V",
         "citation_count":0,
         "title":["Accurate Alpha Abundance and C/O of Low-mass Stars"]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22524109R",
         "citation_count":0,
         "read_count":0.0,
         "title":["A Planetary System Exploration Project for Introductory Astronomy and Astrobiology Courses"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22533620G",
         "citation_count":0,
         "title":["Low Mass Stellar Companions to Nearby A and B Stars"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22540808L",
         "citation_count":0,
         "title":["Many Ultra-Short-Period Rocky Planets are Evaporated Sub-Neptunes"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22543803D",
         "citation_count":0,
         "title":["Multifractal structures in radial velocity measurements for exoplanets"]},
       {
         "read_count":8.0,
         "pubdate":"2015-00-00",
         "bibcode":"2015ASSL..411..189A",
         "citation_count":0,
         "title":["Magnetosphere Environment from Solar System Planets/Moons to Exoplanets"]},
       {
         "read_count":0.0,
         "pubdate":"2015-00-00",
         "bibcode":"2015hae..book..117G",
         "title":["Astronomy, Astrology, and Medicine"]},
       {
         "read_count":0.0,
         "pubdate":"2015-00-00",
         "bibcode":"2015hae..book..315I",
         "title":["Cultural Interpretation of Archaeological Evidence Relating to Astronomy"]},
       {
         "read_count":0.0,
         "pubdate":"2015-00-00",
         "bibcode":"2015hae..book..473R",
         "title":["Long-Term Changes in the Appearance of the Sky"]},
       {
         "read_count":0.0,
         "pubdate":"2015-00-00",
         "bibcode":"2015hae..book..507S",
         "title":["Alignments upon Venus (and Other Planets) - Identification and Analysis"]},
       {
         "read_count":0.0,
         "pubdate":"2015-00-00",
         "bibcode":"2015hae..book..773S",
         "title":["Governor's Palace at Uxmal"]},
       {
         "read_count":0.0,
         "pubdate":"2015-00-00",
         "bibcode":"2015hae..book..807G",
         "title":["Chankillo"]},
       {
         "read_count":0.0,
         "pubdate":"2015-00-00",
         "bibcode":"2015hae..book..967P",
         "title":["\"Chiriguano\" Astronomy - Venus and a Guarani New Year"]},
       {
         "read_count":12.0,
         "pubdate":"2015-00-00",
         "bibcode":"2015ASSL..411..253F",
         "citation_count":0,
         "title":["Living with Stars: Future Space-Based Exoplanet Search and Characterization Missions"]},
       {
         "read_count":10.0,
         "pubdate":"2015-00-00",
         "bibcode":"2015ASSL..411..275S",
         "citation_count":0,
         "title":["The World Space Observatory&ndash;UV Project as a Tool for Exoplanet Science"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.409..109T",
         "citation_count":0,
         "title":["Antarctic Ice Sheet response to a long warm interval across Marine Isotope Stage 31: A cross-latitudinal study of iceberg-rafted debris"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.409..157D",
         "citation_count":0,
         "title":["New bulk sulfur measurements of Martian meteorites and modeling the fate of sulfur during melting and crystallization - Implications for sulfur transfer from Martian mantle to crust-atmosphere system"]},
       {
         "read_count":6.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.409..168V",
         "citation_count":0,
         "title":["Direct evidence of ancient shock metamorphism at the site of the 1908 Tunguska event"]},
       {
         "read_count":5.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.409..182I",
         "citation_count":0,
         "title":["Timing of global crustal metamorphism on Vesta as revealed by high-precision U-Pb dating and trace element chemistry of eucrite zircon"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.409..307H",
         "citation_count":0,
         "title":["Evolution of Mediterranean sea surface temperatures 3.5-1.5 Ma: Regional and hemispheric influences"]},
       {
         "read_count":11.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.410....1N",
         "citation_count":0,
         "title":["Cometary dust in Antarctic ice and snow: Past and present chondritic porous micrometeorites preserved on the Earth's surface"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GeCoA.148..159L",
         "citation_count":1,
         "title":["The paradox between low shock-stage and evidence for compaction in CM carbonaceous chondrites explained by multiple low-intensity impacts"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GeCoA.148..191T",
         "citation_count":0,
         "title":["Fe<SUP>2+</SUP> catalyzed iron atom exchange and re-crystallization in a tropical soil"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GeCoA.148..284M",
         "citation_count":0,
         "title":["Chemical and mineralogical characterisation of illite-smectite: Implications for episodic tectonism and associated fluid flow, central Australia"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GeCoA.148..457G",
         "citation_count":0,
         "title":["Discriminating secondary from magmatic water in rhyolitic matrix-glass of volcanic pyroclasts using thermogravimetric analysis"]},
       {
         "read_count":8.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GeCoA.149...88S",
         "citation_count":0,
         "title":["Evidence for nucleosynthetic enrichment of the protosolar molecular cloud core by multiple supernova events"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015IAUS..307..150Y",
         "title":["Variational approach for rotating-stellar evolution in Lagrange scheme"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GPC...124....1S",
         "citation_count":0,
         "title":["Can mountain glacier melting explains the GRACE-observed mass loss in the southeast Tibetan Plateau: From a climate perspective?"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GeCoA.148...81T",
         "citation_count":0,
         "title":["Copper partitioning between felsic melt and H<SUB>2</SUB>O-CO<SUB>2</SUB> bearing saline fluids"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GeCoA.148..145G",
         "citation_count":1,
         "title":["The uranium isotopic composition of the Earth and the Solar System"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GeCoA.148..219S",
         "citation_count":0,
         "title":["Viscosity of liquid fayalite up to 9 GPa"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GeCoA.148..269C",
         "citation_count":0,
         "title":["Biogeochemical cycling of cadmium isotopes along a high-resolution section through the North Atlantic Ocean"]},
       {
         "read_count":6.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GeCoA.148..402V",
         "citation_count":0,
         "title":["Replacement of olivine by serpentine in the Queen Alexandra Range 93005 carbonaceous chondrite (CM2): Reactant-product compositional relations, and isovolumetric constraints on reaction stoichiometry and elemental mobility during aqueous alteration"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015IJAsB..14..129N",
         "title":["Survival of Spores of Trichoderma longibrachiatum in Space: data from the Space Experiment SPORES on EXPOSE-R"]},
       {
         "read_count":23.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Icar..245..348K",
         "citation_count":0,
         "title":["Comet 9P/Tempel 1: Evolution of the surface"]},
       {
         "read_count":12.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Icar..245..367H",
         "citation_count":0,
         "title":["Micrometer-scale U-Pb age domains in eucrite zircons, impact re-setting, and the thermal history of the HED parent body"]},
       {
         "read_count":5.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Icar..246...37D",
         "citation_count":0,
         "title":["Density of Charon formed from a disk generated by the impact of partially differentiated bodies"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JGeo...83...18K",
         "title":["A method for reconstructing global ocean-induced surface displacements from land-based in-situ stations"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JPRS...99...58C",
         "title":["Estimating wide range Total Suspended Solids concentrations from MODIS 250-m imageries: An improved method"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015MPBu...42...35F",
         "citation_count":0,
         "title":["Lightcurve and Rotation Period Determination for Minor Planet 4910 Kawasato"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Natur.517..472B",
         "title":["Long-lived magnetism from solidification-driven convection on the pallasite parent body"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015P&SS..105..159H",
         "citation_count":0,
         "title":["Lower atmosphere minor gas abundances as retrieved from Venus Express VIRTIS-M-IR data at 2.3 μm"]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015PhTea..53...54S",
         "citation_count":0,
         "read_count":0.0,
         "title":["Quickly creating interactive astronomy illustrations"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JPCS...76...82S",
         "title":["Synthesis, structure and conductivity studies of co-doped ceria: CeO<SUB>2</SUB>-Sm<SUB>2</SUB>O<SUB>3</SUB>-Ta<SUB>2</SUB>O<SUB>5</SUB> (Nb<SUB>2</SUB>O<SUB>5</SUB>) solid solution"]},
       {
         "read_count":9.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015P&SS..105...26W",
         "citation_count":0,
         "title":["Groove formation on Phobos: Testing the Stickney ejecta emplacement model for a subset of the groove population"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015QSRv..107..274C",
         "title":["Book Review: Late Cenozoic Climate Change in Asia: Loess, Monsoon and Monsoon-arid Environment Evolution"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22510507A",
         "citation_count":0,
         "title":["Delivering on the promise of transit timing variations"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22510701H",
         "citation_count":0,
         "title":["An Open-Source Bayesian Atmospheric Radiative Transfer (BART) Code, with Application to WASP-12b"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22510705S",
         "citation_count":0,
         "title":["Balancing the Energy Budget of Short-Period Giant Planets"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22510706B",
         "citation_count":0,
         "title":["The Elemental Compositions and Cloud Properties of Hot Jupiters: A Comprehensive Atmospheric Retrieval Study of Hot Jupiter Transmission Spectra"]},
       {
         "pubdate":"2015-01-00",
         "read_count":19.0,
         "bibcode":"2015AdSpR..55..243N",
         "citation_count":0,
         "title":["Local changes in the total electron content immediately before the 2009 Abruzzo earthquake"]},
       {
         "read_count":16.0,
         "pubdate":"2015-00-00",
         "bibcode":"2015ASSL..411...19W",
         "citation_count":0,
         "title":["Stellar Winds in Time"]},
       {
         "read_count":12.0,
         "pubdate":"2015-00-00",
         "bibcode":"2015ASSL..411...81B",
         "citation_count":0,
         "title":["Types of Hot Jupiter Atmospheres"]},
       {
         "read_count":21.0,
         "pubdate":"2015-00-00",
         "bibcode":"2015ASSL..411..169G",
         "citation_count":0,
         "title":["The Effects of Close-in Exoplanets on Their Host Stars"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AtmEn.100...25T",
         "title":["A budget analysis of the formation of haze in Beijing"]},
       {
         "read_count":5.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.410...62C",
         "citation_count":0,
         "title":["An early solar system magnetic field recorded in CM chondrites"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.410...75L",
         "citation_count":0,
         "title":["Experimental constraints on the composition and dynamics of Titan's polar lakes"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.410..152T",
         "citation_count":0,
         "title":["Disparities in glacial advection of Southern Ocean Intermediate Water to the South Pacific Gyre"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.410..174C",
         "citation_count":0,
         "title":["Constraints on the role of tectonic and climate on erosion revealed by two time series analysis of marine cores around New Zealand"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AtmEn.100..202P",
         "citation_count":0,
         "title":["Ice core profiles of saturated fatty acids (C<SUB>12:0</SUB>-C<SUB>30:0</SUB>) and oleic acid (C<SUB>18:1</SUB>) from southern Alaska since 1734 AD: A link to climate change in the Northern Hemisphere"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015E&PSL.409...15H",
         "citation_count":0,
         "title":["Glacial cooling as inferred from marine temperature proxies TEX<SUP>H</SUP><SUB>86</SUB> and U<SUP>K′</SUP><SUB>37</SUB>"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GeCoA.148...62N",
         "citation_count":0,
         "title":["Distinguishing between basalts produced by endogenic volcanism and impact processes: A non-destrwuctive method using quantitative petrography of lunar basaltic samples"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GeCoA.148..113S",
         "citation_count":0,
         "title":["Aggregation of nanoscale iron oxyhydroxides and corresponding effects on metal uptake, retention, and speciation: II. Temperature and time"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GeCoA.148..130C",
         "citation_count":0,
         "title":["Direct measurement of neon production rates by (α,n) reactions in minerals"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015GeCoA.148..179W",
         "citation_count":0,
         "title":["Isotopic composition of skeleton-bound organic nitrogen in reef-building symbiotic corals: A new method and proxy evaluation at Bermuda"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015IJAsB..14...89B",
         "title":["The AMINO experiment: exposure of amino acids in the EXPOSE-R experiment on the International Space Station and in laboratory"]},
       {
         "read_count":5.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Icar..246..192W",
         "citation_count":0,
         "title":["Pluto's implications for a Snowball Titan"]},
       {
         "read_count":8.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Icar..246..310P",
         "citation_count":0,
         "title":["Pluto's plasma wake oriented away from the ecliptic plane"]},
       {
         "read_count":9.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Icar..246..352P",
         "citation_count":0,
         "title":["Interplanetary dust influx to the Pluto-Charon system"]},
       {
         "read_count":28.0,
         "pubdate":"2015-00-00",
         "bibcode":"2015ASSL..411..289G",
         "citation_count":0,
         "title":["Ground-Based Exoplanet Projects"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22510808S",
         "citation_count":0,
         "title":["Observing Planetary Nebulae with JWST and Extremely Large Telescopes"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22511206T",
         "citation_count":0,
         "title":["Characterizing M dwarf planet hosts and enabling precise radial velocities in the near-infrared"]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22513718Z",
         "citation_count":0,
         "read_count":2.0,
         "title":["Near-Earth Asteroid Characterisation: Gotta catch 'em All!"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22513712J",
         "citation_count":0,
         "title":["Update on VLBA Astrometry of Cassini"]},
       {
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22513813H",
         "citation_count":0,
         "read_count":3.0,
         "title":["SME@XSEDE: An automated spectral synthesis tool for stellar characterization"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22521906W",
         "title":["Visible Wavelength Exoplanet Phase Curves from Global Albedo Maps"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22525907C",
         "citation_count":0,
         "title":["Enabling Technologies for Characterizing Exoplanet Systems with Exo-C"]},
       {
         "read_count":3.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22532302S",
         "citation_count":0,
         "title":["The Unusual Disintegrating Planet Candidate KIC 125557548b and Hot Jupiter CoRoT-1b in Transmission"]},
       {
         "read_count":5.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22540707D",
         "citation_count":0,
         "title":["Mechanisms for Generating False Positives for Extrasolar Life"]},
       {
         "read_count":8.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015Icar..245..122S",
         "citation_count":0,
         "title":["Amazonian modification of Moreux crater: Record of recent and episodic glaciation in the Protonilus Mensae region of Mars"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JAfES.101..148Z",
         "citation_count":0,
         "title":["Integrated provenance analysis of Zakeen (Devonian) and Faraghan (early Permian) sandstones in the Zagros belt, SW Iran"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015JPRS...99...14S",
         "citation_count":0,
         "title":["Seeing through shadow: Modelling surface irradiance for topographic correction of Landsat ETM+ data"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015NatCC...5...23T",
         "title":["Livelihood resilience in the face of climate change"]},
       {
         "read_count":0.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015QSRv..108....1L",
         "citation_count":0,
         "title":["Calendar-dated glacier variations in the western European Alps during the Neoglacial: the Mer de Glace record, Mont Blanc massif"]},
       {
         "read_count":5.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22512204B",
         "citation_count":0,
         "title":["Planet Population Statistics With Kepler Q1-Q16: Stellar Effective Temperature Dependence"]},
       {
         "read_count":4.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22513715P",
         "citation_count":0,
         "title":["Trio of stellar occultations by Pluto One Year Prior to New Horizons' Arrival"]},
       {
         "read_count":7.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22513802H",
         "citation_count":0,
         "title":["The RECONS 25 Parsec Database"]},
       {
         "read_count":2.0,
         "pubdate":"2015-01-00",
         "bibcode":"2015AAS...22513828W",
         "citation_count":0,
         "title":["Constraining Kepler Eclipsing Binary Properties with Time-Series and Multi-band Photometry"]}]
     }};

   var dataNoXOrYLog =  {
     "responseHeader":{
       "status":0,
       "QTime":1478,
       "params":{
         "fl":"bibcode,title,pubdate,citation_count,read_count",
         "indent":"true",
         "q":"\"brown dwarf\"",
         "wt":"json",
         "rows":"1000"}},
     "response":{"numFound":14758,"start":0,"docs":[
       {
         "pubdate":"2000-07-00",
         "bibcode":"2000hst..prop.8482L",
         "citation_count":0,
         "read_count":0.0,
         "title":["STIS Observations of a Brown Dwarf"]
       },
       {
         "pubdate":"1998-06-00",
         "bibcode":"1998AsNow..12...24M",
         "read_count":0.0,
         "title":["Brown dwarfs."]},
       {
         "read_count":0.0,
         "pubdate":"1994-00-00",
         "bibcode":"1994coun.conf...13F",
         "title":["Brown dwarfs"]},
       {
         "read_count":0.0,
         "pubdate":"1994-00-00",
         "bibcode":"1994rppp.conf...43D",
         "title":["Brown Dwarfs"]},
       {
         "read_count":0.0,
         "pubdate":"1989-09-00",
         "bibcode":"1989SciAm.261c..28J",
         "title":["Brown Dwarfs Here..."]},
       {
         "pubdate":"1997-12-00",
         "bibcode":"1997ConPh..38..395J",
         "citation_count":2,
         "read_count":0.0,
         "title":["Brown dwarfs."]},
       {
         "pubdate":"1989-00-00",
         "bibcode":"1989GriO...53a...9K",
         "citation_count":0,
         "read_count":0.0,
         "title":["Brown dwarf found."]},
       {
         "read_count":0.0,
         "pubdate":"2005-11-00",
         "bibcode":"2005ccsf.conf..177R",
         "citation_count":0,
         "title":["Brown Dwarfs"]},
       {
         "pubdate":"2003-06-00",
         "bibcode":"2003IAUS..211.....M",
         "citation_count":0,
         "read_count":0.0,
         "title":["Brown Dwarfs"]},
       {
         "read_count":0.0,
         "pubdate":"2007-00-00",
         "bibcode":"2007SchpJ...2.4475A",
         "citation_count":0,
         "title":["Brown dwarfs"]},
       {
         "read_count":0.0,
         "pubdate":"1985-03-00",
         "bibcode":"1985Sci...227.1154G",
         "citation_count":30,
         "title":["Brown Dwarfs"]}]
     }};

  afterEach(function() {

    $("#test").empty();

  });

  describe("Bubble Chart (UI Widget)", function(){

//
//    var bubble = new BubbleChart({testing : true});
//    $("#test").append(bubble.view.render().el);
//    bubble.setCurrentQuery(new ApiQuery({q:"fake"}));
//    bubble.processResponse(new ApiResponse(data))


   /*
   * bibcodes being used for verification:
   * 2000hst..prop.8482L — 0 citations, 0 reads
   *  1999AJ....117..343R - 44reads,  3 citations
   *
   * */

    it("should parse each record", function(){

      var bubble = new BubbleChart({testing : true });
      $("#test").append(bubble.view.render().el);
      bubble.setCurrentQuery(new ApiQuery({q:"fake"}));
      bubble.processResponse(new ApiResponse(data));

      var dataParsed = true;

      _.each(bubble.model.get("data"), function(d){
        //valid date
        if (!(Object.prototype.toString.call(d.date) === "[object Date]") ||
           (d.citation_count === NaN) ||
          (d.read_count === NaN) ||
          !(d.pub === d.bibcode.slice(4, 9).replace(/\./g, ''))){

          dataParsed = false;
        }

      },this);

      expect(dataParsed).to.eql(true);

    });


    it("should request data and render when it receives data in return ", function(){

      var bubble = new BubbleChart({testing : true });
      $("#test").append(bubble.view.render().el);
      bubble.setCurrentQuery(new ApiQuery({q:"fake"}));

      var minsub = new (MinSub.extend({
        request: function (apiRequest) {
        }
      }))({verbose: false});

      bubble.activate(minsub.beehive.getHardenedInstance());
      sinon.stub(bubble.getPubSub(), "publish");

      bubble.onShow();

      expect(bubble.getPubSub().publish.args[0][0]).to.eql('[PubSub]-New-Request');
      expect(bubble.getPubSub().publish.args[0][1].get("query").toJSON()).to.eql({
        "q": [
          "fake"
        ],
        "rows": [
          1000
        ],
        "fl": [
          "title,bibcode,citation_count,read_count,pubdate"
        ]
      });


    })

    it("should have a reads vs time version", function(){

     var bubble = new BubbleChart({testing : true });
     $("#test").append(bubble.view.render().el);
     bubble.setCurrentQuery(new ApiQuery({q:"fake"}));
     bubble.processResponse(new ApiResponse(data));

      //reads v time
      //should be default

      var expected = _.filter(data.response.docs, function(d){
        if (d.read_count >= 1){
          return true}
      }).length;

      expect(d3.selectAll(".paper-circle")[0].length).to.eql(expected);

      //checking individual circles
      expect(d3.selectAll(".paper-circle").filter(function(d){return d.bibcode == "2000hst..prop.8482L"})[0].length).to.eql(0);
      var testBib= d3.selectAll(".paper-circle").filter(function(d){return d.bibcode == "1999AJ....117..343R"});
      expect(parseFloat(testBib.attr("cx"))).to.eql(bubble.view.scales.xScale(testBib.data()[0].date));
      expect(parseFloat(testBib.attr("cy"))).to.eql(bubble.view.scales.yScale(testBib.data()[0].read_count));

      var logLinearRadio= d3.select(".y-label .linear");

      logLinearRadio.on("click").call(logLinearRadio.node(), logLinearRadio.datum());

      expect(d3.selectAll(".paper-circle")[0].length).to.eql(17);

      expect(d3.select(".y-label .axis-title").text()).to.eql("90 Day Read Count");
      expect(d3.select(".x-label .axis-title").text()).to.eql("Date");

      //testing reads vs time again to make sure the click event works
      bubble.view.$(".citations-vs-time").trigger("click");
      bubble.view.$(".reads-vs-time").trigger("click");

      var expected = _.filter(data.response.docs, function(d){
        if (d.read_count >= 1){
          return true}
      }).length;

      expect(d3.selectAll(".paper-circle")[0].length).to.eql(expected);
      bubble.model.set("yScale", "linear");
      expect(d3.selectAll(".paper-circle")[0].length).to.eql(17);

    });

    it("should have a citations vs time version", function(done){

      var bubble = new BubbleChart({testing : true });
      $("#test").append(bubble.view.render().el);
      bubble.setCurrentQuery(new ApiQuery({q:"fake"}));
      bubble.processResponse(new ApiResponse(data));

      bubble.view.$(".citations-vs-time").trigger("click");

      //checking individual circles
      expect(d3.selectAll(".paper-circle").filter(function(d){return d.bibcode == "2000hst..prop.8482L"})[0].length).to.eql(0);
      setTimeout(function(){

        var testBib= d3.selectAll(".paper-circle").filter(function(d){return d.bibcode == "1999AJ....117..343R"});
        expect(parseFloat(testBib.attr("cx"))).to.eql(bubble.view.scales.xScale(testBib.data()[0].date));
        expect(parseFloat(testBib.attr("cy"))).to.eql(bubble.view.scales.yScale(testBib.data()[0].citation_count));

        var expected = _.filter(data.response.docs, function(d){
          if (d.citation_count >= 1){
            return true}
        }).length;

        expect(d3.selectAll(".paper-circle")[0].length).to.eql(expected);
        bubble.model.set("yScale", "linear");
        expect(d3.selectAll(".paper-circle")[0].length).to.eql(17);

        expect(d3.select(".y-label .axis-title").text()).to.eql("Citation Count");
        expect(d3.select(".x-label .axis-title").text()).to.eql("Date");

        done();

      }, 1000)

    })

    it("should have a citations vs reads version", function(done){

      var bubble = new BubbleChart({testing : true });
      $("#test").append(bubble.view.render().el);
      bubble.setCurrentQuery(new ApiQuery({q:"fake"}));
      bubble.processResponse(new ApiResponse(data));

      bubble.view.$(".reads-vs-citations").trigger("click");

      setTimeout(function(){

        //checking individual circles
        expect(d3.selectAll(".paper-circle").filter(function(d){return d.bibcode == "2000hst..prop.8482L"})[0].length).to.eql(0);
        var testBib= d3.selectAll(".paper-circle").filter(function(d){return d.bibcode == "1999AJ....117..343R"});
        expect(parseFloat(testBib.attr("cx"))).to.eql(bubble.view.scales.xScale(testBib.data()[0].citation_count));
        expect(parseFloat(testBib.attr("cy"))).to.eql(bubble.view.scales.yScale(testBib.data()[0].read_count));

        var expected = _.chain(data.response.docs).filter(function(d){
          if (d.read_count >= 1){
            return true
          }
        }).filter(function(d){
          if (d.citation_count >= 1){
            return true
          }
        }).value().length;

        expect(d3.selectAll(".paper-circle")[0].length).to.eql(expected);
        bubble.model.set({yScale : "linear", xScale : "linear"});
        expect(d3.selectAll(".paper-circle")[0].length).to.eql(17);

        expect(d3.select(".y-label .axis-title").text()).to.eql("90 Day Read Count");
        expect(d3.select(".x-label .axis-title").text()).to.eql("Citation Count");

        done();

      }, 1000)

    });

    it("should have methods to reset the view and the model in preparation for the next data inflow", function(){

      var bubble = new BubbleChart({testing : true });
      $("#test").append(bubble.view.render().el);
      bubble.setCurrentQuery(new ApiQuery({q:"fake"}));
      bubble.processResponse(new ApiResponse(data));

      bubble.model.reset();
      expect(JSON.stringify(bubble.model.toJSON())).to.eql(JSON.stringify(bubble.model.defaults()));

      bubble.view.reset();
      expect(bubble.view.scales).to.eql({});
      expect(bubble.view.cache).to.eql({});

    });

    it("should allow the user to tag multiple bubbles for observation", function(done){

      var bubble = new BubbleChart({testing : true });
      $("#test").append(bubble.view.render().el);
      bubble.setCurrentQuery(new ApiQuery({q:"fake"}));
      bubble.processResponse(new ApiResponse(data));

      bubble.model.toggleTracked("2000hst..prop.8482L");
      bubble.model.toggleTracked("2000hst..prop.8482L");
      bubble.model.toggleTracked("2000hst..prop.8482L");

      //this will be invisible, then it should be visible, with the tracking class,
      //this is checking to make sure the entering selection is getting tagged with tracking class

      expect(d3.selectAll(".paper-circle.tracked")[0].length).to.eql(0);

      bubble.view.$(".reads-vs-citations").trigger("click");
      bubble.model.set({yScale : "linear", xScale : "linear"});

      setTimeout(function(){
        expect(d3.selectAll(".paper-circle.tracked")[0].length).to.eql(1);

        done();
      }, 1000);

    });

    it("should allow the user to select bibodes and filter on them", function(){

      var bubble = new BubbleChart({testing : true });
      $("#test").append(bubble.view.render().el);
      bubble.setCurrentQuery(new ApiQuery({q:"fake"}));
      bubble.processResponse(new ApiResponse(data));

      var minsub = new (MinSub.extend({
        request: function (apiRequest) {
        }
      }))({verbose: false});

      bubble.activate(minsub.beehive.getHardenedInstance());
      sinon.spy(bubble.getPubSub(), "publish");

      bubble.model.set("selectedBibs", ["2000hst..prop.8482L","1999AJ....117..343R" ]);
      bubble.view.$(".submit").click();

      expect(bubble.getPubSub().publish.args[0][0]).to.eql("[PubSub]-New-Query");
      expect(bubble.getPubSub().publish.args[0][1].toJSON()).to.eql({
        "q": [
          "fake"
        ],
        "fq_bubble_chart": [
          "(bibcode:(2000hst..prop.8482L OR 1999AJ....117..343R))"
        ],
        "__bubble_chart_fq_bubble_chart": [
          "AND",
          "bibcode:(2000hst..prop.8482L OR 1999AJ....117..343R)"
        ],
        "fq": [
          "{!type=aqp v=$fq_bubble_chart}"
        ]
      });

    });


    it("should also be able to deal with results sets within the same year", function(){

      var bubble = new BubbleChart({testing : true });
      $("#test").append(bubble.view.render().el);
      bubble.setCurrentQuery(new ApiQuery({q:"fake"}));
      bubble.processResponse(new ApiResponse(dataOneYear));

      //the actual titles are unpredictable (d3 shows what it wants, so it might show
      // the padding labels and might not) so just make sure
      //the necessary labels are present
      var labels = []
      d3.select(".x-axis").selectAll("text").each(function(d){
        labels.push(this.textContent)
      });

      expect(_.contains(labels, "Jan-2015")).to.be.true;
      expect(_.contains(labels, "Oct-2015")).to.be.true;

    });

    it("should allow the user to isolate bubbles by publication", function(){

      var bubble = new BubbleChart({testing : true });
      $("#test").append(bubble.view.render().el);
      bubble.setCurrentQuery(new ApiQuery({q:"fake"}));
      bubble.processResponse(new ApiResponse(data));

      bubble.model.set("yScale", "linear");

      expect(d3.selectAll(".paper-circle")[0].length).to.eql(17);


      var ST =  d3.select(".journal-name-key text");
      ST.on("click").call(ST.node(), ST.datum());
      expect(d3.selectAll(".paper-circle")[0].length).to.eql(2);

    })

    it("should have a tooltip", function(){
      var bubble = new BubbleChart({testing : true });
      $("#test").append(bubble.view.render().el);
      bubble.setCurrentQuery(new ApiQuery({q:"fake"}));
      bubble.processResponse(new ApiResponse(data));

      var realSvg =  d3.select(".bubble-chart-svg");

      //mocking variables accessible to the event listener
      d3.event = {}
      d3.event.target =  d3.select(".paper-circle").node();

      realSvg.on("mouseover.tooltip").call(realSvg.node());

      expect($(".d3-tooltip").css("display")).to.eql("block");
      expect($(".d3-tooltip").text()).to.eql('\nBrown Dwarfs in the Hyades and Beyond?\n(1999AJ....117..343R)\nCitations: 44,\nReads: 3\n\n\n\n');

    });


    it("should detect if a log scale is impossible, and both default to linear and remove the log option entirely", function(){

      var bubble = new BubbleChart({testing : true });
      $("#test").append(bubble.view.render().el);
      bubble.setCurrentQuery(new ApiQuery({q:"fake"}));
      bubble.processResponse(new ApiResponse(dataNoXOrYLog));

      expect(bubble.model.get("yScale")).to.eql("linear");
      expect(d3.select(".y-label .log").classed("hidden")).to.eql(true);
      var xPositions = [];
      expect(_.pluck(d3.selectAll(".paper-bubble").each(function(){
        xPositions.push(d3.select(this).attr("cx"))
      })));
      expect(d3.sum(xPositions)).to.eql(0);

    })


  });


})