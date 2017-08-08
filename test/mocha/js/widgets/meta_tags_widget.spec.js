define([
  'underscore',
  'jquery',
  'backbone',
  'es6!js/widgets/meta_tags/widget',
  'js/widgets/base/base_widget',
  'js/bugutils/minimal_pubsub',
  'hbs!js/widgets/meta_tags/template/metatags'
], function (
  _, $, Backbone, MetaTagsWidget, BaseWidget, MinimalPubSub, MetaTagsTemplate
) {

  var mockDocs = {
    "pubdate": "2017-06-00",
    "first_author": "Hennig, C.",
    "abstract": "We study the galaxy populations in 74 Sunyaev-Zeldovich effect selected clusters from the South Pole Telescope survey, which have been imaged in the science verification phase of the Dark Energy Survey. The sample extends up to z ∼ 1.1 with 4 × 10<SUP>14</SUP> M<SUB>☉</SUB> ≤ M<SUB>200</SUB> ≤ 3 × 10<SUP>15</SUP>M<SUB>☉</SUB>. Using the band containing the 4000 Å break and its redward neighbour, we study the colour-magnitude distributions of cluster galaxies to ∼m<SUB>*</SUB> + 2, finding that: (1)The intrinsic rest frame g - r colour width of the red sequence (RS) population is ∼0.03 out to z ∼ 0.85 with a preference for an increase to ∼0.07 at z = 1, and (2) the prominence of the RS declines beyond z ∼ 0.6. The spatial distribution of cluster galaxies is well described by the NFW profile out to 4R<SUB>200</SUB> with a concentration of c<SUB>g</SUB> = 3.59^{+0.20}_{-0.18}, 5.37^{+0.27}_{-0.24} and 1.38^{+0.21}_{-0.19} for the full, the RS and the blue non-RS populations, respectively, but with ∼40 per cent to 55 per cent cluster to cluster variation and no statistically significant redshift or mass trends. The number of galaxies within the virial region N<SUB>200</SUB> exhibits a mass trend indicating that the number of galaxies per unit total mass is lower in the most massive clusters, and shows no significant redshift trend. The RS fraction within R<SUB>200</SUB> is (68 ± 3) per cent at z = 0.46, varies from ∼55 per cent at z = 1 to ∼80 per cent at z = 0.1 and exhibits intrinsic variation among clusters of ∼14 per cent. We discuss a model that suggests that the observed redshift trend in RS fraction favours a transformation time-scale for infalling field galaxies to become RS galaxies of 2-3 Gyr.",
    "links_data": [
      "{\"title\":\"\", \"type\":\"pdf\", \"instances\":\"\", \"access\":\"\"}",
      "{\"title\":\"\", \"type\":\"ned\", \"instances\":\"1\", \"access\":\"\"}",
      "{\"title\":\"\", \"type\":\"preprint\", \"instances\":\"\", \"access\":\"open\"}",
      "{\"title\":\"\", \"type\":\"electr\", \"instances\":\"\", \"access\":\"\"}"
    ],
    "year": "2017",
    "bibcode": "2017MNRAS.467.4015H",
    "aff": [
      "Faculty of Physics, Ludwig-Maximilians-Universität, Scheinerstr. 1, D-81679 Munich, Germany; Excellence Cluster Universe, Boltzmannstr. 2, D-85748 Garching, Germany",
      "Faculty of Physics, Ludwig-Maximilians-Universität, Scheinerstr. 1, D-81679 Munich, Germany; Excellence Cluster Universe, Boltzmannstr. 2, D-85748 Garching, Germany; Max Planck Institute for Extraterrestrial Physics, Giessenbachstrasse, D-85748 Garching, Germany",
      "Cerro Tololo Inter-American Observatory, National Optical Astronomy Observatory, Casilla 603, La Serena, Chile; Faculty of Physics, Ludwig-Maximilians-Universität, Scheinerstr. 1, D-81679 Munich, Germany",
      "Faculty of Physics, Ludwig-Maximilians-Universität, Scheinerstr. 1, D-81679 Munich, Germany; Excellence Cluster Universe, Boltzmannstr. 2, D-85748 Garching, Germany",
      "Faculty of Physics, Ludwig-Maximilians-Universität, Scheinerstr. 1, D-81679 Munich, Germany; Excellence Cluster Universe, Boltzmannstr. 2, D-85748 Garching, Germany",
      "Faculty of Physics, Ludwig-Maximilians-Universität, Scheinerstr. 1, D-81679 Munich, Germany; Excellence Cluster Universe, Boltzmannstr. 2, D-85748 Garching, Germany",
      "Faculty of Physics, Ludwig-Maximilians-Universität, Scheinerstr. 1, D-81679 Munich, Germany; Excellence Cluster Universe, Boltzmannstr. 2, D-85748 Garching, Germany",
      "Faculty of Physics, Ludwig-Maximilians-Universität, Scheinerstr. 1, D-81679 Munich, Germany; Excellence Cluster Universe, Boltzmannstr. 2, D-85748 Garching, Germany",
      "Cerro Tololo Inter-American Observatory, National Optical Astronomy Observatory, Casilla 603, La Serena, Chile",
      "Department of Physics and Astronomy, University College London, Gower Street, London WC1E 6BT, UK; Department of Physics and Electronics, Rhodes University, PO Box 94, Grahamstown, 6140, South Africa",
      "Department of Physics and Astronomy, Colby College, 5800 Mayflower Hill, Waterville, Maine 04901, USA; Department of Physics, Harvard University, 17 Oxford Street, Cambridge, MA 02138, USA",
      "CNRS, UMR 7095, Institut d'Astrophysique de Paris, F-75014 Paris, France; Department of Physics and Astronomy, University College London, Gower Street, London WC1E 6BT, UK; Sorbonne Universités, UPMC Univ Paris 06, UMR 7095, Institut d'Astrophysique de Paris, F-75014 Paris, France",
      "Carnegie Observatories, 813 Santa Barbara St., Pasadena, CA 91101, USA",
      "CNRS, UMR 7095, Institut d'Astrophysique de Paris, F-75014 Paris, France; Sorbonne Universités, UPMC Univ Paris 06, UMR 7095, Institut d'Astrophysique de Paris, F-75014 Paris, France",
      "Department of Physics and Astronomy, University College London, Gower Street, London WC1E 6BT, UK",
      "Faculty of Physics, Ludwig-Maximilians-Universität, Scheinerstr. 1, D-81679 Munich, Germany; Excellence Cluster Universe, Boltzmannstr. 2, D-85748 Garching, Germany",
      "Institute of Cosmology and Gravitation, University of Portsmouth, Portsmouth PO1 3FX, UK",
      "Laboratório Interinstitucional de e-Astronomia - LIneA, Rua Gal. José Cristino 77, Rio de Janeiro, RJ - 20921-400, Brazil; Observatório Nacional, Rua Gal. José Cristino 77, Rio de Janeiro, RJ - 20921-400, Brazil",
      "Department of Astronomy, University of Illinois, 1002 W. Green Street, Urbana, IL 61801, USA; National Center for Supercomputing Applications, 1205 West Clark St., Urbana, IL 61801, USA",
      "Institut de Ciències de l'Espai, IEEC-CSIC, Campus UAB, Carrer de Can Magrans, s/n, Barcelona, E-08193 Bellaterra, Spain; Institut de Física d'Altes Energies (IFAE), The Barcelona Institute of Science and Technology, Campus UAB, E-08193 Bellaterra (Barcelona), Spain",
      "Faculty of Physics, Ludwig-Maximilians-Universität, Scheinerstr. 1, D-81679 Munich, Germany; Excellence Cluster Universe, Boltzmannstr. 2, D-85748 Garching, Germany",
      "Institute of Cosmology and Gravitation, University of Portsmouth, Portsmouth PO1 3FX, UK; School of Physics and Astronomy, University of Southampton, Southampton SO17 1BJ, UK",
      "Laboratório Interinstitucional de e-Astronomia - LIneA, Rua Gal. José Cristino 77, Rio de Janeiro, RJ - 20921-400, Brazil; Observatório Nacional, Rua Gal. José Cristino 77, Rio de Janeiro, RJ - 20921-400, Brazil",
      "Fermi National Accelerator Laboratory, PO Box 500, Batavia, IL 60510, USA",
      "Department of Physics and Astronomy, University College London, Gower Street, London WC1E 6BT, UK",
      "Department of Physics and Astronomy, University of Pennsylvania, Philadelphia, PA 19104, USA; Jet Propulsion Laboratory, California Institute of Technology, 4800 Oak Grove Dr., Pasadena, CA 91109, USA",
      "Department of Astronomy, University of Michigan, Ann Arbor, MI 48109, USA; Department of Physics, University of Michigan, Ann Arbor, MI 48109, USA",
      "Laboratório Interinstitucional de e-Astronomia - LIneA, Rua Gal. José Cristino 77, Rio de Janeiro, RJ - 20921-400, Brazil",
      "Institut de Ciències de l'Espai, IEEC-CSIC, Campus UAB, Carrer de Can Magrans, s/n, Barcelona, E-08193 Bellaterra, Spain",
      "Fermi National Accelerator Laboratory, PO Box 500, Batavia, IL 60510, USA; Kavli Institute for Cosmological Physics, University of Chicago, Chicago, IL 60637, USA",
      "Faculty of Physics, Ludwig-Maximilians-Universität, Scheinerstr. 1, D-81679 Munich, Germany; Excellence Cluster Universe, Boltzmannstr. 2, D-85748 Garching, Germany",
      "Department of Astronomy, University of Florida, Gainesville, FL 32611, USA",
      "Kavli Institute for Particle Astrophysics and Cosmology, PO Box 2450, Stanford University, Stanford, CA 94305, USA; SLAC National Accelerator Laboratory, Menlo Park, CA 94025, USA",
      "Department of Astronomy, University of Illinois, 1002 W. Green Street, Urbana, IL 61801, USA; National Center for Supercomputing Applications, 1205 West Clark St., Urbana, IL 61801, USA",
      "Faculty of Physics, Ludwig-Maximilians-Universität, Scheinerstr. 1, D-81679 Munich, Germany; Excellence Cluster Universe, Boltzmannstr. 2, D-85748 Garching, Germany; Max Planck Institute for Extraterrestrial Physics, Giessenbachstrasse, D-85748 Garching, Germany",
      "Fermi National Accelerator Laboratory, PO Box 500, Batavia, IL 60510, USA",
      "Center for Cosmology and Astro-Particle Physics, The Ohio State University, Columbus, OH 43210, USA; Department of Physics, The Ohio State University, Columbus, OH 43210, USA",
      "Département de Physique, Université de Montréal, C.P. 6128, Succ. Centre-Ville, Montréal, Québec H3C 3J7, Canada",
      "Cerro Tololo Inter-American Observatory, National Optical Astronomy Observatory, Casilla 603, La Serena, Chile",
      "Department of Physics and Astronomy, University of Pennsylvania, Philadelphia, PA 19104, USA",
      "Fermi National Accelerator Laboratory, PO Box 500, Batavia, IL 60510, USA",
      "Department of Physics and Astronomy, University College London, Gower Street, London WC1E 6BT, UK",
      "Department of Physics and Astronomy, University of Pennsylvania, Philadelphia, PA 19104, USA",
      "George P. and Cynthia Woods Mitchell Institute for Fundamental Physics and Astronomy, Department of Physics and Astronomy, Texas A&M University, College Station, TX 77843, USA",
      "Center for Cosmology and Astro-Particle Physics, The Ohio State University, Columbus, OH 43210, USA; Department of Astronomy, The Ohio State University, Columbus, OH 43210, USA",
      "Kavli Institute for Astrophysics and Space Research, Massachusetts Institute of Technology, 77 Massachusetts Avenue, Cambridge, MA 02139, USA",
      "Department of Astrophysical Sciences, Princeton University, Peyton Hall, Princeton, NJ 08544, USA",
      "Department of Astronomy, University of Michigan, Ann Arbor, MI 48109, USA; Department of Physics, University of Michigan, Ann Arbor, MI 48109, USA",
      "Institució Catalana de Recerca i Estudis Avançats, E-08010 Barcelona, Spain; Institut de Física d'Altes Energies (IFAE), The Barcelona Institute of Science and Technology, Campus UAB, E-08193 Bellaterra (Barcelona), Spain",
      "Fermi National Accelerator Laboratory, PO Box 500, Batavia, IL 60510, USA",
      "Fermi National Accelerator Laboratory, PO Box 500, Batavia, IL 60510, USA",
      "Laboratório Interinstitucional de e-Astronomia - LIneA, Rua Gal. José Cristino 77, Rio de Janeiro, RJ - 20921-400, Brazil; Observatório Nacional, Rua Gal. José Cristino 77, Rio de Janeiro, RJ - 20921-400, Brazil",
      "Jet Propulsion Laboratory, California Institute of Technology, 4800 Oak Grove Dr., Pasadena, CA 91109, USA",
      "School of Physics, University of Melbourne, Parkville, VIC 3010, Australia",
      "Department of Physics and Astronomy, Pevensey Building, University of Sussex, Brighton BN1 9QH, UK",
      "Department of Physics, University of Arizona, Tucson, AZ 85721, USA",
      "Kavli Institute for Particle Astrophysics and Cosmology, PO Box 2450, Stanford University, Stanford, CA 94305, USA; SLAC National Accelerator Laboratory, Menlo Park, CA 94025, USA",
      "Centro de Investigaciones Energéticas, Medioambientales y Tecnológicas (CIEMAT), Complutense, 40 E-28040 Madrid, Spain",
      "Instituto de Física, UFRGS, Caixa Postal 15051, Porto Alegre, RS - 91501-970, Brazil; Laboratório Interinstitucional de e-Astronomia - LIneA, Rua Gal. José Cristino 77, Rio de Janeiro, RJ - 20921-400, Brazil",
      "Department of Physics, University of Michigan, Ann Arbor, MI 48109, USA",
      "Centro de Investigaciones Energéticas, Medioambientales y Tecnológicas (CIEMAT), Complutense, 40 E-28040 Madrid, Spain",
      "Cerro Tololo Inter-American Observatory, National Optical Astronomy Observatory, Casilla 603, La Serena, Chile",
      "Fermi National Accelerator Laboratory, PO Box 500, Batavia, IL 60510, USA",
      "Laboratório Interinstitucional de e-Astronomia - LIneA, Rua Gal. José Cristino 77, Rio de Janeiro, RJ - 20921-400, Brazil",
      "Institute for Astronomy, University of Hawaii at Manoa, Honolulu, HI 96822, USA; Harvard-Smithsonian Center for Astrophysics, 60 Garden Street, Cambridge, MA 02138, USA",
      "Physics Department, University of California, Davis, CA 95616, USA",
      "Department of Physics and Astronomy, University of Pennsylvania, Philadelphia, PA 19104, USA",
      "National Center for Supercomputing Applications, 1205 West Clark St., Urbana, IL 61801, USA",
      "Department of Physics, University of Michigan, Ann Arbor, MI 48109, USA",
      "Institute of Cosmology and Gravitation, University of Portsmouth, Portsmouth PO1 3FX, UK",
      "Argonne National Laboratory, 9700 South Cass Avenue, Lemont, IL 60439, USA",
      "Cerro Tololo Inter-American Observatory, National Optical Astronomy Observatory, Casilla 603, La Serena, Chile",
      "Fermi National Accelerator Laboratory, PO Box 500, Batavia, IL 60510, USA"
    ],
    "issue": "4",
    "pub": "Monthly Notices of the Royal Astronomical Society",
    "volume": "467",
    "doi": [
      "10.1093/mnras/stx175"
    ],
    "keyword": [
      "galaxies: clusters: general",
      "galaxies: clusters: individual",
      "galaxies: evolution",
      "galaxies: formation",
      "galaxies: luminosity function",
      "mass function",
      "Astrophysics - Astrophysics of Galaxies",
      "Astrophysics - Cosmology and Nongalactic Astrophysics"
    ],
    "author": [
      "Hennig, C.",
      "Mohr, J. J.",
      "Zenteno, A.",
      "Desai, S.",
      "Dietrich, J. P.",
      "Bocquet, S.",
      "Strazzullo, V.",
      "Saro, A.",
      "Abbott, T. M. C.",
      "Abdalla, F. B.",
      "Bayliss, M.",
      "Benoit-Lévy, A.",
      "Bernstein, R. A.",
      "Bertin, E.",
      "Brooks, D.",
      "Capasso, R.",
      "Capozzi, D.",
      "Carnero, A.",
      "Carrasco Kind, M.",
      "Carretero, J.",
      "Chiu, I.",
      "D'Andrea, C. B.",
      "daCosta, L. N.",
      "Diehl, H. T.",
      "Doel, P.",
      "Eifler, T. F.",
      "Evrard, A. E.",
      "Fausti-Neto, A.",
      "Fosalba, P.",
      "Frieman, J.",
      "Gangkofner, C.",
      "Gonzalez, A.",
      "Gruen, D.",
      "Gruendl, R. A.",
      "Gupta, N.",
      "Gutierrez, G.",
      "Honscheid, K.",
      "Hlavacek-Larrondo, J.",
      "James, D. J.",
      "Kuehn, K.",
      "Kuropatkin, N.",
      "Lahav, O.",
      "March, M.",
      "Marshall, J. L.",
      "Martini, P.",
      "McDonald, M.",
      "Melchior, P.",
      "Miller, C. J.",
      "Miquel, R.",
      "Neilsen, E.",
      "Nord, B.",
      "Ogando, R.",
      "Plazas, A. A.",
      "Reichardt, C.",
      "Romer, A. K.",
      "Rozo, E.",
      "Rykoff, E. S.",
      "Sanchez, E.",
      "Santiago, B.",
      "Schubnell, M.",
      "Sevilla-Noarbe, I.",
      "Smith, R. C.",
      "Soares-Santos, M.",
      "Sobreira, F.",
      "Stalder, B.",
      "Stanford, S. A.",
      "Suchyta, E.",
      "Swanson, M. E. C.",
      "Tarle, G.",
      "Thomas, D.",
      "Vikram, V.",
      "Walker, A. R.",
      "Zhang, Y."
    ],
    "title": [
      "Galaxy populations in massive galaxy clusters to z = 1.1: colour distribution, concentration, halo occupation number and red sequence fraction"
    ],
    "property": [
      "OPENACCESS",
      "REFEREED",
      "EPRINT_OPENACCESS",
      "ARTICLE"
    ],
    "page": [
      "4015"
    ],
    "[citations]": {
      "num_references": 97,
      "num_citations": 8
    }
  };

  var mockFullTextSources = [
    {
      "openAccess": false,
      "title": "Publisher Article",
      "link": "http://adsabs.harvard.edu/cgi-bin/nph-data_query?bibcode=2017MNRAS.467.4015H&link_type=EJOURNAL"
    },
    {
      "openAccess": false,
      "title": "Publisher PDF",
      "link": "http://adsabs.harvard.edu/cgi-bin/nph-data_query?bibcode=2017MNRAS.467.4015H&link_type=ARTICLE"
    },
    {
      "openAccess": true,
      "title": "arXiv e-print",
      "link": "http://adsabs.harvard.edu/cgi-bin/nph-data_query?bibcode=2017MNRAS.467.4015H&link_type=PREPRINT"
    }
  ];

  var mockDocStashController = {
    getDocs: function () {
      return [
        mockDocs
      ];
    },
    getHardenedInstance: function () {
      return this;
    }
  };

  describe('Meta Tags Widget', function () {
    var sandbox, widget, minsub, beehive;

    beforeEach(function () {
      sandbox = sinon.sandbox.create();
      widget = new MetaTagsWidget();
      minsub = new MinimalPubSub();
      minsub.beehive.addObject('DocStashController', mockDocStashController);
      beehive = minsub.beehive.getHardenedInstance();
    });

    afterEach(function () {
      sandbox.restore();
    });

    it('extends from BaseWidget', function () {
      expect(widget).to.be.instanceOf(BaseWidget);
    });

    it('can get docs from cache (if available)', function () {
      widget.activate(beehive);
      var cachedDocs = widget.getCachedDoc(mockDocs.bibcode);
      expect(cachedDocs).to.deep.equal(_.extend(mockDocs, {
        issn: undefined,
        isbn: undefined
      }));
      minsub.beehive.removeObject('DocStashController');
      minsub.beehive.addObject('DocStashController',
        _.extend(mockDocStashController, {
          getDocs: function () {
            return [{}];
          }
        })
      );
      var nullDocs = widget.getCachedDoc(mockDocs.bibcode);
      expect(nullDocs).to.be.null;
    });

    it('updates the page meta tags', function () {
      widget.activate(beehive);
      var zoteroSpy = sandbox.spy();
      $(document).on('ZoteroItemUpdated', zoteroSpy);

      widget.updateMetaTags(mockDocs);

      // Attempt to dynamically generate tags to make sure it's the same on head
      var tags = MetaTagsTemplate(mockDocs);
      tags = _(tags.split('\n'))
        .filter(function (n) {
          return n.length;
        })
        .map(function (line) {
          return line.match(/name="(.*?)"/)[1]
        })
        .value();

      var metas = $('meta[data-highwire="true"]').map(function () {
        return $(this).attr('name');
      }).toArray();

      for(var i = 0; i < tags.length; i++) {
        expect(tags[i]).to.equal(metas[i]);
      }
      expect(zoteroSpy.called).to.be.true;
    });
  });
});
