define([
  'marionette',
  'js/widgets/network_vis/network_widget',
  'js/bugutils/minimal_pubsub',
  'js/components/api_query',
  'js/components/json_response'

], function (
  Marionette,
  NetworkWidget,
  MinimalPubsub,
  ApiQuery,
  JsonResponse
  ) {


  describe("Network Visualization Widget", function () {

    var testDataSmall, testDataLarge, testDataEmpty, networkWidget;

    testDataSmall = {"fullGraph": {"links": [
      {"source": 10, "target": 15, "value": 13.509433962264154},
      {"source": 9, "target": 11, "value": 28.962264150943401},
      {"source": 18, "target": 9, "value": 32.641509433962256},
      {"source": 1, "target": 3, "value": 40.0},
      {"source": 12, "target": 1, "value": 1.0},
      {"source": 2, "target": 8, "value": 17.924528301886795},
      {"source": 2, "target": 13, "value": 7.622641509433965},
      {"source": 14, "target": 3, "value": 17.924528301886795},
      {"source": 10, "target": 18, "value": 32.641509433962256},
      {"source": 8, "target": 3, "value": 17.924528301886795},
      {"source": 3, "target": 7, "value": 38.528301886792462},
      {"source": 12, "target": 7, "value": 1.0},
      {"source": 0, "target": 6, "value": 1.0},
      {"source": 10, "target": 0, "value": 23.075471698113216},
      {"source": 17, "target": 3, "value": 24.54716981132076},
      {"source": 3, "target": 16, "value": 13.509433962264154},
      {"source": 12, "target": 3, "value": 1.0},
      {"source": 12, "target": 0, "value": 1.0},
      {"source": 12, "target": 9, "value": 1.0},
      {"source": 0, "target": 11, "value": 23.075471698113216},
      {"source": 8, "target": 13, "value": 7.622641509433965},
      {"source": 0, "target": 1, "value": 23.075471698113216},
      {"source": 0, "target": 9, "value": 23.075471698113216},
      {"source": 18, "target": 1, "value": 32.641509433962256},
      {"source": 3, "target": 5, "value": 7.622641509433965},
      {"source": 18, "target": 7, "value": 32.641509433962256},
      {"source": 0, "target": 7, "value": 23.075471698113216},
      {"source": 2, "target": 5, "value": 7.622641509433965},
      {"source": 7, "target": 6, "value": 1.0},
      {"source": 2, "target": 16, "value": 13.509433962264154},
      {"source": 1, "target": 6, "value": 1.0},
      {"source": 6, "target": 11, "value": 1.0},
      {"source": 15, "target": 3, "value": 13.509433962264154},
      {"source": 2, "target": 14, "value": 17.924528301886795},
      {"source": 3, "target": 6, "value": 1.0},
      {"source": 3, "target": 13, "value": 7.622641509433965},
      {"source": 10, "target": 7, "value": 38.528301886792462},
      {"source": 18, "target": 3, "value": 32.641509433962256},
      {"source": 15, "target": 1, "value": 13.509433962264154},
      {"source": 9, "target": 1, "value": 37.056603773584911},
      {"source": 8, "target": 5, "value": 7.622641509433965},
      {"source": 10, "target": 3, "value": 39.264150943396231},
      {"source": 12, "target": 6, "value": 1.0},
      {"source": 14, "target": 5, "value": 7.622641509433965},
      {"source": 14, "target": 13, "value": 7.622641509433965},
      {"source": 9, "target": 6, "value": 1.0},
      {"source": 3, "target": 11, "value": 28.962264150943401},
      {"source": 12, "target": 11, "value": 1.0},
      {"source": 9, "target": 3, "value": 37.056603773584911},
      {"source": 10, "target": 12, "value": 1.0},
      {"source": 1, "target": 11, "value": 28.962264150943401},
      {"source": 10, "target": 6, "value": 1.0},
      {"source": 15, "target": 9, "value": 13.509433962264154},
      {"source": 14, "target": 8, "value": 17.924528301886795},
      {"source": 10, "target": 9, "value": 37.056603773584911},
      {"source": 10, "target": 1, "value": 37.056603773584911},
      {"source": 8, "target": 16, "value": 13.509433962264154},
      {"source": 2, "target": 3, "value": 17.924528301886795},
      {"source": 10, "target": 11, "value": 28.962264150943401},
      {"source": 4, "target": 3, "value": 24.54716981132076},
      {"source": 13, "target": 5, "value": 7.622641509433965},
      {"source": 0, "target": 3, "value": 23.075471698113216},
      {"source": 14, "target": 16, "value": 13.509433962264154},
      {"source": 7, "target": 11, "value": 28.962264150943401},
      {"source": 1, "target": 7, "value": 34.113207547169807},
      {"source": 9, "target": 7, "value": 34.113207547169807},
      {"source": 18, "target": 11, "value": 25.283018867924529},
      {"source": 0, "target": 18, "value": 18.660377358490567}
    ], "nodes": [
      {"nodeName": "Bohlen, E", "nodeWeight": 13.936607317550379},
      {"nodeName": "Henneken, E", "nodeWeight": 37.994521620035215},
      {"nodeName": "Dell\u0027Antonio, I", "nodeWeight": 11.090132394182483},
      {"nodeName": "Kurtz, M", "nodeWeight": 150.0},
      {"nodeName": "Brody, T", "nodeWeight": 14.26759277375595},
      {"nodeName": "Utsumi, Y", "nodeWeight": 6.3239418248222785},
      {"nodeName": "Rosvall, M", "nodeWeight": 5.0},
      {"nodeName": "Murray, S", "nodeWeight": 29.256505576208173},
      {"nodeName": "Geller, M", "nodeWeight": 11.090132394182483},
      {"nodeName": "Grant, C", "nodeWeight": 26.079045196634706},
      {"nodeName": "Accomazzi, A", "nodeWeight": 34.022696145568375},
      {"nodeName": "Thompson, D", "nodeWeight": 17.341029152807668},
      {"nodeName": "Bergstrom, C", "nodeWeight": 5.0},
      {"nodeName": "Miyazaki, S", "nodeWeight": 6.3239418248222785},
      {"nodeName": "Fabricant, D", "nodeWeight": 11.090132394182483},
      {"nodeName": "Di Milia, G", "nodeWeight": 7.1183069197156463},
      {"nodeName": "Wyatt, W", "nodeWeight": 7.1183069197156463},
      {"nodeName": "Bollen, J", "nodeWeight": 14.26759277375595},
      {"nodeName": "Eichhorn, G", "nodeWeight": 18.664970977629945}
    ]}}

    testDataLarge =  {
      "fullGraph": {
        "directed": false,
        "graph": [],
        "links": [
          {
            "source": 0,
            "target": 3,
            "weight": 20.714285714285722
          },
          {
            "source": 0,
            "target": 206,
            "weight": 20.714285714285722
          },
          {
            "source": 0,
            "target": 211,
            "weight": 20.714285714285722
          },
          {
            "source": 0,
            "target": 121,
            "weight": 20.714285714285722
          },
          {
            "source": 0,
            "target": 103,
            "weight": 20.714285714285722
          },
          {
            "source": 0,
            "target": 155,
            "weight": 20.714285714285722
          },
          {
            "source": 1,
            "target": 32,
            "weight": 32.193877551020414
          },
          {
            "source": 1,
            "target": 207,
            "weight": 32.193877551020414
          },
          {
            "source": 1,
            "target": 34,
            "weight": 32.193877551020414
          },
          {
            "source": 1,
            "target": 197,
            "weight": 32.193877551020414
          },
          {
            "source": 2,
            "target": 196,
            "weight": 35.98979591836735
          },
          {
            "source": 2,
            "target": 144,
            "weight": 25.489795918367356
          },
          {
            "source": 2,
            "target": 62,
            "weight": 35.98979591836735
          },
          {
            "source": 2,
            "target": 201,
            "weight": 35.98979591836735
          },
          {
            "source": 3,
            "target": 203,
            "weight": 1.0
          },
          {
            "source": 3,
            "target": 206,
            "weight": 20.714285714285722
          },
          {
            "source": 3,
            "target": 208,
            "weight": 22.61224489795919
          },
          {
            "source": 3,
            "target": 113,
            "weight": 33.938775510204096
          },
          {
            "source": 3,
            "target": 11,
            "weight": 1.0
          },
          {
            "source": 3,
            "target": 121,
            "weight": 20.714285714285722
          },
          {
            "source": 3,
            "target": 155,
            "weight": 20.714285714285722
          },
          {
            "source": 3,
            "target": 51,
            "weight": 22.61224489795919
          },
          {
            "source": 3,
            "target": 62,
            "weight": 22.61224489795919
          },
          {
            "source": 3,
            "target": 67,
            "weight": 8.53061224489796
          },
          {
            "source": 3,
            "target": 211,
            "weight": 20.714285714285722
          },
          {
            "source": 3,
            "target": 74,
            "weight": 8.53061224489796
          },
          {
            "source": 3,
            "target": 86,
            "weight": 22.61224489795919
          },
          {
            "source": 3,
            "target": 90,
            "weight": 1.0
          },
          {
            "source": 3,
            "target": 92,
            "weight": 22.61224489795919
          },
          {
            "source": 3,
            "target": 192,
            "weight": 1.0
          },
          {
            "source": 3,
            "target": 97,
            "weight": 22.61224489795919
          },
          {
            "source": 3,
            "target": 98,
            "weight": 29.9591836734694
          },
          {
            "source": 3,
            "target": 103,
            "weight": 20.714285714285722
          },
          {
            "source": 3,
            "target": 104,
            "weight": 1.0
          },
          {
            "source": 4,
            "target": 106,
            "weight": 20.714285714285722
          },
          {
            "source": 4,
            "target": 7,
            "weight": 20.714285714285722
          },
          {
            "source": 4,
            "target": 34,
            "weight": 20.714285714285722
          },
          {
            "source": 4,
            "target": 187,
            "weight": 20.714285714285722
          },
          {
            "source": 4,
            "target": 73,
            "weight": 20.714285714285722
          },
          {
            "source": 4,
            "target": 207,
            "weight": 20.714285714285722
          },
          {
            "source": 5,
            "target": 23,
            "weight": 35.1326530612245
          },
          {
            "source": 5,
            "target": 84,
            "weight": 35.1326530612245
          },
          {
            "source": 5,
            "target": 207,
            "weight": 35.1326530612245
          },
          {
            "source": 5,
            "target": 34,
            "weight": 35.1326530612245
          },
          {
            "source": 6,
            "target": 51,
            "weight": 29.9591836734694
          },
          {
            "source": 6,
            "target": 78,
            "weight": 29.9591836734694
          },
          {
            "source": 6,
            "target": 29,
            "weight": 29.9591836734694
          },
          {
            "source": 6,
            "target": 60,
            "weight": 29.9591836734694
          },
          {
            "source": 6,
            "target": 62,
            "weight": 29.9591836734694
          },
          {
            "source": 6,
            "target": 123,
            "weight": 29.9591836734694
          },
          {
            "source": 7,
            "target": 106,
            "weight": 20.714285714285722
          },
          {
            "source": 7,
            "target": 207,
            "weight": 20.714285714285722
          },
          {
            "source": 7,
            "target": 34,
            "weight": 20.714285714285722
          },
          {
            "source": 7,
            "target": 187,
            "weight": 20.714285714285722
          },
          {
            "source": 7,
            "target": 73,
            "weight": 20.714285714285722
          },
          {
            "source": 8,
            "target": 27,
            "weight": 1.0
          },
          {
            "source": 8,
            "target": 105,
            "weight": 1.0
          },
          {
            "source": 8,
            "target": 140,
            "weight": 1.0
          },
          {
            "source": 8,
            "target": 210,
            "weight": 1.0
          },
          {
            "source": 8,
            "target": 64,
            "weight": 1.0
          },
          {
            "source": 8,
            "target": 93,
            "weight": 1.0
          },
          {
            "source": 8,
            "target": 96,
            "weight": 1.0
          },
          {
            "source": 8,
            "target": 194,
            "weight": 1.0
          },
          {
            "source": 8,
            "target": 72,
            "weight": 1.0
          },
          {
            "source": 8,
            "target": 152,
            "weight": 1.0
          },
          {
            "source": 8,
            "target": 127,
            "weight": 1.0
          },
          {
            "source": 8,
            "target": 128,
            "weight": 1.0
          },
          {
            "source": 9,
            "target": 51,
            "weight": 21.785714285714295
          },
          {
            "source": 9,
            "target": 77,
            "weight": 21.785714285714295
          },
          {
            "source": 9,
            "target": 108,
            "weight": 21.785714285714295
          },
          {
            "source": 9,
            "target": 135,
            "weight": 21.785714285714295
          },
          {
            "source": 9,
            "target": 62,
            "weight": 38.19387755102043
          },
          {
            "source": 10,
            "target": 51,
            "weight": 37.24489795918368
          },
          {
            "source": 10,
            "target": 78,
            "weight": 15.724489795918373
          },
          {
            "source": 10,
            "target": 54,
            "weight": 26.530612244897974
          },
          {
            "source": 10,
            "target": 181,
            "weight": 29.9591836734694
          },
          {
            "source": 10,
            "target": 134,
            "weight": 27.6326530612245
          },
          {
            "source": 10,
            "target": 74,
            "weight": 32.59183673469388
          },
          {
            "source": 10,
            "target": 167,
            "weight": 15.724489795918373
          },
          {
            "source": 10,
            "target": 89,
            "weight": 25.489795918367356
          },
          {
            "source": 10,
            "target": 213,
            "weight": 25.489795918367356
          },
          {
            "source": 10,
            "target": 214,
            "weight": 15.724489795918373
          },
          {
            "source": 10,
            "target": 71,
            "weight": 29.9591836734694
          },
          {
            "source": 10,
            "target": 62,
            "weight": 38.25510204081635
          },
          {
            "source": 10,
            "target": 196,
            "weight": 35.19387755102042
          },
          {
            "source": 10,
            "target": 175,
            "weight": 29.9591836734694
          },
          {
            "source": 10,
            "target": 198,
            "weight": 38.01020408163267
          },
          {
            "source": 11,
            "target": 51,
            "weight": 1.0
          },
          {
            "source": 11,
            "target": 203,
            "weight": 1.0
          },
          {
            "source": 11,
            "target": 208,
            "weight": 1.0
          },
          {
            "source": 11,
            "target": 113,
            "weight": 1.0
          },
          {
            "source": 11,
            "target": 62,
            "weight": 1.0
          },
          {
            "source": 11,
            "target": 90,
            "weight": 1.0
          },
          {
            "source": 11,
            "target": 92,
            "weight": 1.0
          },
          {
            "source": 11,
            "target": 86,
            "weight": 1.0
          },
          {
            "source": 11,
            "target": 192,
            "weight": 1.0
          },
          {
            "source": 11,
            "target": 97,
            "weight": 1.0
          },
          {
            "source": 11,
            "target": 104,
            "weight": 1.0
          },
          {
            "source": 12,
            "target": 51,
            "weight": 26.530612244897974
          },
          {
            "source": 12,
            "target": 78,
            "weight": 26.530612244897974
          },
          {
            "source": 12,
            "target": 62,
            "weight": 26.530612244897974
          },
          {
            "source": 13,
            "target": 179,
            "weight": 15.724489795918373
          },
          {
            "source": 13,
            "target": 159,
            "weight": 37.428571428571445
          },
          {
            "source": 13,
            "target": 32,
            "weight": 37.428571428571445
          },
          {
            "source": 13,
            "target": 34,
            "weight": 37.428571428571445
          },
          {
            "source": 13,
            "target": 61,
            "weight": 35.98979591836735
          },
          {
            "source": 13,
            "target": 126,
            "weight": 15.724489795918373
          },
          {
            "source": 13,
            "target": 65,
            "weight": 37.428571428571445
          },
          {
            "source": 13,
            "target": 197,
            "weight": 37.428571428571445
          },
          {
            "source": 13,
            "target": 75,
            "weight": 27.6326530612245
          },
          {
            "source": 13,
            "target": 76,
            "weight": 32.193877551020414
          },
          {
            "source": 14,
            "target": 77,
            "weight": 27.142857142857157
          },
          {
            "source": 14,
            "target": 214,
            "weight": 34.153061224489804
          },
          {
            "source": 14,
            "target": 29,
            "weight": 32.193877551020414
          },
          {
            "source": 14,
            "target": 182,
            "weight": 27.142857142857157
          },
          {
            "source": 14,
            "target": 102,
            "weight": 11.102040816326532
          },
          {
            "source": 14,
            "target": 88,
            "weight": 11.102040816326532
          },
          {
            "source": 14,
            "target": 62,
            "weight": 34.48979591836736
          },
          {
            "source": 14,
            "target": 40,
            "weight": 11.102040816326532
          },
          {
            "source": 14,
            "target": 119,
            "weight": 20.714285714285722
          },
          {
            "source": 14,
            "target": 120,
            "weight": 11.102040816326532
          },
          {
            "source": 14,
            "target": 172,
            "weight": 20.714285714285722
          },
          {
            "source": 14,
            "target": 24,
            "weight": 20.714285714285722
          },
          {
            "source": 14,
            "target": 124,
            "weight": 32.193877551020414
          },
          {
            "source": 14,
            "target": 196,
            "weight": 11.102040816326532
          },
          {
            "source": 15,
            "target": 207,
            "weight": 32.193877551020414
          },
          {
            "source": 16,
            "target": 25,
            "weight": 8.53061224489796
          },
          {
            "source": 16,
            "target": 79,
            "weight": 8.53061224489796
          },
          {
            "source": 16,
            "target": 160,
            "weight": 8.53061224489796
          },
          {
            "source": 16,
            "target": 137,
            "weight": 8.53061224489796
          },
          {
            "source": 16,
            "target": 142,
            "weight": 8.53061224489796
          },
          {
            "source": 16,
            "target": 117,
            "weight": 8.53061224489796
          },
          {
            "source": 16,
            "target": 17,
            "weight": 8.53061224489796
          },
          {
            "source": 16,
            "target": 18,
            "weight": 8.53061224489796
          },
          {
            "source": 16,
            "target": 43,
            "weight": 8.53061224489796
          },
          {
            "source": 17,
            "target": 25,
            "weight": 8.53061224489796
          },
          {
            "source": 17,
            "target": 79,
            "weight": 8.53061224489796
          },
          {
            "source": 17,
            "target": 160,
            "weight": 8.53061224489796
          },
          {
            "source": 17,
            "target": 137,
            "weight": 8.53061224489796
          },
          {
            "source": 17,
            "target": 142,
            "weight": 8.53061224489796
          },
          {
            "source": 17,
            "target": 117,
            "weight": 8.53061224489796
          },
          {
            "source": 17,
            "target": 18,
            "weight": 8.53061224489796
          },
          {
            "source": 17,
            "target": 43,
            "weight": 8.53061224489796
          },
          {
            "source": 18,
            "target": 25,
            "weight": 8.53061224489796
          },
          {
            "source": 18,
            "target": 79,
            "weight": 8.53061224489796
          },
          {
            "source": 18,
            "target": 160,
            "weight": 8.53061224489796
          },
          {
            "source": 18,
            "target": 137,
            "weight": 8.53061224489796
          },
          {
            "source": 18,
            "target": 142,
            "weight": 8.53061224489796
          },
          {
            "source": 18,
            "target": 117,
            "weight": 8.53061224489796
          },
          {
            "source": 18,
            "target": 43,
            "weight": 8.53061224489796
          },
          {
            "source": 19,
            "target": 193,
            "weight": 29.9591836734694
          },
          {
            "source": 19,
            "target": 62,
            "weight": 29.9591836734694
          },
          {
            "source": 20,
            "target": 107,
            "weight": 3.020408163265307
          },
          {
            "source": 20,
            "target": 53,
            "weight": 3.020408163265307
          },
          {
            "source": 20,
            "target": 204,
            "weight": 3.020408163265307
          },
          {
            "source": 20,
            "target": 162,
            "weight": 3.020408163265307
          },
          {
            "source": 20,
            "target": 139,
            "weight": 3.020408163265307
          },
          {
            "source": 20,
            "target": 130,
            "weight": 3.020408163265307
          },
          {
            "source": 20,
            "target": 26,
            "weight": 3.020408163265307
          },
          {
            "source": 20,
            "target": 52,
            "weight": 3.020408163265307
          },
          {
            "source": 20,
            "target": 176,
            "weight": 3.020408163265307
          },
          {
            "source": 20,
            "target": 147,
            "weight": 3.020408163265307
          },
          {
            "source": 21,
            "target": 158,
            "weight": 20.714285714285722
          },
          {
            "source": 21,
            "target": 32,
            "weight": 20.714285714285722
          },
          {
            "source": 21,
            "target": 207,
            "weight": 20.714285714285722
          },
          {
            "source": 21,
            "target": 34,
            "weight": 20.714285714285722
          },
          {
            "source": 21,
            "target": 197,
            "weight": 20.714285714285722
          },
          {
            "source": 21,
            "target": 138,
            "weight": 20.714285714285722
          },
          {
            "source": 22,
            "target": 131,
            "weight": 15.724489795918373
          },
          {
            "source": 22,
            "target": 64,
            "weight": 15.724489795918373
          },
          {
            "source": 22,
            "target": 116,
            "weight": 15.724489795918373
          },
          {
            "source": 22,
            "target": 212,
            "weight": 15.724489795918373
          },
          {
            "source": 22,
            "target": 188,
            "weight": 15.724489795918373
          },
          {
            "source": 22,
            "target": 66,
            "weight": 15.724489795918373
          },
          {
            "source": 22,
            "target": 195,
            "weight": 15.724489795918373
          },
          {
            "source": 23,
            "target": 84,
            "weight": 35.1326530612245
          },
          {
            "source": 23,
            "target": 207,
            "weight": 35.1326530612245
          },
          {
            "source": 23,
            "target": 34,
            "weight": 35.1326530612245
          },
          {
            "source": 24,
            "target": 51,
            "weight": 27.020408163265316
          },
          {
            "source": 24,
            "target": 77,
            "weight": 32.59183673469388
          },
          {
            "source": 24,
            "target": 214,
            "weight": 36.57142857142858
          },
          {
            "source": 24,
            "target": 134,
            "weight": 27.020408163265316
          },
          {
            "source": 24,
            "target": 182,
            "weight": 20.714285714285722
          },
          {
            "source": 24,
            "target": 56,
            "weight": 21.785714285714295
          },
          {
            "source": 24,
            "target": 141,
            "weight": 27.020408163265316
          },
          {
            "source": 24,
            "target": 200,
            "weight": 21.785714285714295
          },
          {
            "source": 24,
            "target": 186,
            "weight": 21.785714285714295
          },
          {
            "source": 24,
            "target": 63,
            "weight": 27.020408163265316
          },
          {
            "source": 24,
            "target": 119,
            "weight": 20.714285714285722
          },
          {
            "source": 24,
            "target": 172,
            "weight": 29.98979591836736
          },
          {
            "source": 24,
            "target": 62,
            "weight": 32.59183673469388
          },
          {
            "source": 24,
            "target": 196,
            "weight": 21.785714285714295
          },
          {
            "source": 24,
            "target": 135,
            "weight": 27.020408163265316
          },
          {
            "source": 25,
            "target": 79,
            "weight": 8.53061224489796
          },
          {
            "source": 25,
            "target": 160,
            "weight": 8.53061224489796
          },
          {
            "source": 25,
            "target": 137,
            "weight": 8.53061224489796
          },
          {
            "source": 25,
            "target": 142,
            "weight": 8.53061224489796
          },
          {
            "source": 25,
            "target": 117,
            "weight": 8.53061224489796
          },
          {
            "source": 25,
            "target": 43,
            "weight": 8.53061224489796
          },
          {
            "source": 26,
            "target": 107,
            "weight": 3.020408163265307
          },
          {
            "source": 26,
            "target": 53,
            "weight": 3.020408163265307
          },
          {
            "source": 26,
            "target": 204,
            "weight": 3.020408163265307
          },
          {
            "source": 26,
            "target": 162,
            "weight": 3.020408163265307
          },
          {
            "source": 26,
            "target": 130,
            "weight": 3.020408163265307
          },
          {
            "source": 26,
            "target": 139,
            "weight": 3.020408163265307
          },
          {
            "source": 26,
            "target": 52,
            "weight": 3.020408163265307
          },
          {
            "source": 26,
            "target": 147,
            "weight": 3.020408163265307
          },
          {
            "source": 26,
            "target": 176,
            "weight": 3.020408163265307
          },
          {
            "source": 27,
            "target": 152,
            "weight": 1.0
          },
          {
            "source": 27,
            "target": 140,
            "weight": 1.0
          },
          {
            "source": 27,
            "target": 210,
            "weight": 1.0
          },
          {
            "source": 27,
            "target": 64,
            "weight": 1.0
          },
          {
            "source": 27,
            "target": 93,
            "weight": 1.0
          },
          {
            "source": 27,
            "target": 96,
            "weight": 1.0
          },
          {
            "source": 27,
            "target": 194,
            "weight": 1.0
          },
          {
            "source": 27,
            "target": 72,
            "weight": 1.0
          },
          {
            "source": 27,
            "target": 128,
            "weight": 1.0
          },
          {
            "source": 27,
            "target": 127,
            "weight": 1.0
          },
          {
            "source": 27,
            "target": 105,
            "weight": 1.0
          },
          {
            "source": 28,
            "target": 188,
            "weight": 20.714285714285722
          },
          {
            "source": 28,
            "target": 57,
            "weight": 20.714285714285722
          },
          {
            "source": 28,
            "target": 87,
            "weight": 20.714285714285722
          },
          {
            "source": 28,
            "target": 64,
            "weight": 20.714285714285722
          },
          {
            "source": 28,
            "target": 44,
            "weight": 20.714285714285722
          },
          {
            "source": 28,
            "target": 195,
            "weight": 20.714285714285722
          },
          {
            "source": 29,
            "target": 51,
            "weight": 34.122448979591844
          },
          {
            "source": 29,
            "target": 78,
            "weight": 29.9591836734694
          },
          {
            "source": 29,
            "target": 85,
            "weight": 37.06122448979593
          },
          {
            "source": 29,
            "target": 60,
            "weight": 29.9591836734694
          },
          {
            "source": 29,
            "target": 202,
            "weight": 33.051020408163275
          },
          {
            "source": 29,
            "target": 62,
            "weight": 38.40816326530615
          },
          {
            "source": 29,
            "target": 91,
            "weight": 37.06122448979593
          },
          {
            "source": 29,
            "target": 214,
            "weight": 38.10204081632655
          },
          {
            "source": 29,
            "target": 123,
            "weight": 29.9591836734694
          },
          {
            "source": 29,
            "target": 124,
            "weight": 38.10204081632655
          },
          {
            "source": 29,
            "target": 196,
            "weight": 33.051020408163275
          },
          {
            "source": 30,
            "target": 174,
            "weight": 25.489795918367356
          },
          {
            "source": 30,
            "target": 164,
            "weight": 25.489795918367356
          },
          {
            "source": 30,
            "target": 62,
            "weight": 33.051020408163275
          },
          {
            "source": 30,
            "target": 214,
            "weight": 26.530612244897974
          },
          {
            "source": 30,
            "target": 124,
            "weight": 26.530612244897974
          },
          {
            "source": 30,
            "target": 196,
            "weight": 25.489795918367356
          },
          {
            "source": 31,
            "target": 111,
            "weight": 25.489795918367356
          },
          {
            "source": 31,
            "target": 63,
            "weight": 25.489795918367356
          },
          {
            "source": 31,
            "target": 77,
            "weight": 25.489795918367356
          },
          {
            "source": 31,
            "target": 62,
            "weight": 25.489795918367356
          },
          {
            "source": 32,
            "target": 205,
            "weight": 25.489795918367356
          },
          {
            "source": 32,
            "target": 110,
            "weight": 20.714285714285722
          },
          {
            "source": 32,
            "target": 112,
            "weight": 20.714285714285722
          },
          {
            "source": 32,
            "target": 207,
            "weight": 39.78571428571429
          },
          {
            "source": 32,
            "target": 115,
            "weight": 20.714285714285722
          },
          {
            "source": 32,
            "target": 132,
            "weight": 20.714285714285722
          },
          {
            "source": 32,
            "target": 133,
            "weight": 25.489795918367356
          },
          {
            "source": 32,
            "target": 138,
            "weight": 20.714285714285722
          },
          {
            "source": 32,
            "target": 34,
            "weight": 39.81632653061225
          },
          {
            "source": 32,
            "target": 35,
            "weight": 15.724489795918373
          },
          {
            "source": 32,
            "target": 141,
            "weight": 20.714285714285722
          },
          {
            "source": 32,
            "target": 143,
            "weight": 15.724489795918373
          },
          {
            "source": 32,
            "target": 39,
            "weight": 11.102040816326532
          },
          {
            "source": 32,
            "target": 126,
            "weight": 15.724489795918373
          },
          {
            "source": 32,
            "target": 42,
            "weight": 20.714285714285722
          },
          {
            "source": 32,
            "target": 151,
            "weight": 25.489795918367356
          },
          {
            "source": 32,
            "target": 50,
            "weight": 36.57142857142858
          },
          {
            "source": 32,
            "target": 158,
            "weight": 20.714285714285722
          },
          {
            "source": 32,
            "target": 159,
            "weight": 39.540816326530624
          },
          {
            "source": 32,
            "target": 163,
            "weight": 15.724489795918373
          },
          {
            "source": 32,
            "target": 61,
            "weight": 37.826530612244916
          },
          {
            "source": 32,
            "target": 168,
            "weight": 32.193877551020414
          },
          {
            "source": 32,
            "target": 65,
            "weight": 39.60204081632655
          },
          {
            "source": 32,
            "target": 69,
            "weight": 15.724489795918373
          },
          {
            "source": 32,
            "target": 75,
            "weight": 27.6326530612245
          },
          {
            "source": 32,
            "target": 76,
            "weight": 39.14285714285717
          },
          {
            "source": 32,
            "target": 179,
            "weight": 15.724489795918373
          },
          {
            "source": 32,
            "target": 81,
            "weight": 32.193877551020414
          },
          {
            "source": 32,
            "target": 197,
            "weight": 39.87755102040817
          },
          {
            "source": 32,
            "target": 187,
            "weight": 20.714285714285722
          },
          {
            "source": 32,
            "target": 189,
            "weight": 20.714285714285722
          },
          {
            "source": 32,
            "target": 94,
            "weight": 38.8979591836735
          },
          {
            "source": 32,
            "target": 99,
            "weight": 11.102040816326532
          },
          {
            "source": 32,
            "target": 33,
            "weight": 30.357142857142872
          },
          {
            "source": 33,
            "target": 110,
            "weight": 29.9591836734694
          },
          {
            "source": 33,
            "target": 207,
            "weight": 34.122448979591844
          },
          {
            "source": 33,
            "target": 34,
            "weight": 34.122448979591844
          },
          {
            "source": 33,
            "target": 187,
            "weight": 29.9591836734694
          },
          {
            "source": 33,
            "target": 189,
            "weight": 29.9591836734694
          },
          {
            "source": 33,
            "target": 190,
            "weight": 20.714285714285722
          },
          {
            "source": 33,
            "target": 197,
            "weight": 25.489795918367356
          },
          {
            "source": 34,
            "target": 106,
            "weight": 20.714285714285722
          },
          {
            "source": 34,
            "target": 205,
            "weight": 25.489795918367356
          },
          {
            "source": 34,
            "target": 110,
            "weight": 36.23469387755103
          },
          {
            "source": 34,
            "target": 112,
            "weight": 20.714285714285722
          },
          {
            "source": 34,
            "target": 115,
            "weight": 20.714285714285722
          },
          {
            "source": 34,
            "target": 125,
            "weight": 15.724489795918373
          },
          {
            "source": 34,
            "target": 126,
            "weight": 15.724489795918373
          },
          {
            "source": 34,
            "target": 132,
            "weight": 20.714285714285722
          },
          {
            "source": 34,
            "target": 133,
            "weight": 25.489795918367356
          },
          {
            "source": 34,
            "target": 138,
            "weight": 20.714285714285722
          },
          {
            "source": 34,
            "target": 35,
            "weight": 15.724489795918373
          },
          {
            "source": 34,
            "target": 141,
            "weight": 20.714285714285722
          },
          {
            "source": 34,
            "target": 143,
            "weight": 15.724489795918373
          },
          {
            "source": 34,
            "target": 39,
            "weight": 11.102040816326532
          },
          {
            "source": 34,
            "target": 42,
            "weight": 20.714285714285722
          },
          {
            "source": 34,
            "target": 146,
            "weight": 15.724489795918373
          },
          {
            "source": 34,
            "target": 151,
            "weight": 25.489795918367356
          },
          {
            "source": 34,
            "target": 207,
            "weight": 39.75510204081634
          },
          {
            "source": 34,
            "target": 50,
            "weight": 36.57142857142858
          },
          {
            "source": 34,
            "target": 158,
            "weight": 20.714285714285722
          },
          {
            "source": 34,
            "target": 159,
            "weight": 39.540816326530624
          },
          {
            "source": 34,
            "target": 163,
            "weight": 15.724489795918373
          },
          {
            "source": 34,
            "target": 58,
            "weight": 26.530612244897974
          },
          {
            "source": 34,
            "target": 61,
            "weight": 36.72448979591837
          },
          {
            "source": 34,
            "target": 62,
            "weight": 15.724489795918373
          },
          {
            "source": 34,
            "target": 168,
            "weight": 32.193877551020414
          },
          {
            "source": 34,
            "target": 65,
            "weight": 39.57142857142859
          },
          {
            "source": 34,
            "target": 169,
            "weight": 15.724489795918373
          },
          {
            "source": 34,
            "target": 69,
            "weight": 15.724489795918373
          },
          {
            "source": 34,
            "target": 73,
            "weight": 20.714285714285722
          },
          {
            "source": 34,
            "target": 75,
            "weight": 27.6326530612245
          },
          {
            "source": 34,
            "target": 76,
            "weight": 39.14285714285717
          },
          {
            "source": 34,
            "target": 179,
            "weight": 15.724489795918373
          },
          {
            "source": 34,
            "target": 81,
            "weight": 32.193877551020414
          },
          {
            "source": 34,
            "target": 84,
            "weight": 35.1326530612245
          },
          {
            "source": 34,
            "target": 197,
            "weight": 39.84693877551021
          },
          {
            "source": 34,
            "target": 187,
            "weight": 33.142857142857146
          },
          {
            "source": 34,
            "target": 189,
            "weight": 29.9591836734694
          },
          {
            "source": 34,
            "target": 190,
            "weight": 35.438775510204096
          },
          {
            "source": 34,
            "target": 94,
            "weight": 38.8979591836735
          },
          {
            "source": 34,
            "target": 95,
            "weight": 15.724489795918373
          },
          {
            "source": 34,
            "target": 99,
            "weight": 11.102040816326532
          },
          {
            "source": 34,
            "target": 100,
            "weight": 15.724489795918373
          },
          {
            "source": 34,
            "target": 196,
            "weight": 15.724489795918373
          },
          {
            "source": 35,
            "target": 163,
            "weight": 15.724489795918373
          },
          {
            "source": 35,
            "target": 207,
            "weight": 15.724489795918373
          },
          {
            "source": 35,
            "target": 143,
            "weight": 15.724489795918373
          },
          {
            "source": 35,
            "target": 69,
            "weight": 15.724489795918373
          },
          {
            "source": 35,
            "target": 197,
            "weight": 15.724489795918373
          },
          {
            "source": 36,
            "target": 189,
            "weight": 32.193877551020414
          },
          {
            "source": 36,
            "target": 197,
            "weight": 29.9591836734694
          },
          {
            "source": 36,
            "target": 49,
            "weight": 29.9591836734694
          },
          {
            "source": 37,
            "target": 157,
            "weight": 8.53061224489796
          },
          {
            "source": 37,
            "target": 178,
            "weight": 8.53061224489796
          },
          {
            "source": 37,
            "target": 166,
            "weight": 8.53061224489796
          },
          {
            "source": 37,
            "target": 114,
            "weight": 8.53061224489796
          },
          {
            "source": 37,
            "target": 38,
            "weight": 8.53061224489796
          },
          {
            "source": 37,
            "target": 68,
            "weight": 8.53061224489796
          },
          {
            "source": 37,
            "target": 148,
            "weight": 8.53061224489796
          },
          {
            "source": 37,
            "target": 173,
            "weight": 8.53061224489796
          },
          {
            "source": 37,
            "target": 82,
            "weight": 8.53061224489796
          },
          {
            "source": 38,
            "target": 157,
            "weight": 8.53061224489796
          },
          {
            "source": 38,
            "target": 178,
            "weight": 8.53061224489796
          },
          {
            "source": 38,
            "target": 166,
            "weight": 8.53061224489796
          },
          {
            "source": 38,
            "target": 114,
            "weight": 8.53061224489796
          },
          {
            "source": 38,
            "target": 68,
            "weight": 8.53061224489796
          },
          {
            "source": 38,
            "target": 148,
            "weight": 8.53061224489796
          },
          {
            "source": 38,
            "target": 173,
            "weight": 8.53061224489796
          },
          {
            "source": 38,
            "target": 82,
            "weight": 8.53061224489796
          },
          {
            "source": 39,
            "target": 159,
            "weight": 11.102040816326532
          },
          {
            "source": 39,
            "target": 207,
            "weight": 11.102040816326532
          },
          {
            "source": 39,
            "target": 65,
            "weight": 11.102040816326532
          },
          {
            "source": 39,
            "target": 99,
            "weight": 11.102040816326532
          },
          {
            "source": 39,
            "target": 197,
            "weight": 11.102040816326532
          },
          {
            "source": 39,
            "target": 76,
            "weight": 11.102040816326532
          },
          {
            "source": 40,
            "target": 77,
            "weight": 11.102040816326532
          },
          {
            "source": 40,
            "target": 182,
            "weight": 11.102040816326532
          },
          {
            "source": 40,
            "target": 196,
            "weight": 30.51020408163266
          },
          {
            "source": 40,
            "target": 88,
            "weight": 30.51020408163266
          },
          {
            "source": 40,
            "target": 120,
            "weight": 11.102040816326532
          },
          {
            "source": 40,
            "target": 214,
            "weight": 30.51020408163266
          },
          {
            "source": 40,
            "target": 102,
            "weight": 11.102040816326532
          },
          {
            "source": 41,
            "target": 207,
            "weight": 29.9591836734694
          },
          {
            "source": 41,
            "target": 153,
            "weight": 29.9591836734694
          },
          {
            "source": 42,
            "target": 132,
            "weight": 20.714285714285722
          },
          {
            "source": 42,
            "target": 207,
            "weight": 20.714285714285722
          },
          {
            "source": 42,
            "target": 115,
            "weight": 20.714285714285722
          },
          {
            "source": 42,
            "target": 197,
            "weight": 20.714285714285722
          },
          {
            "source": 43,
            "target": 79,
            "weight": 8.53061224489796
          },
          {
            "source": 43,
            "target": 160,
            "weight": 8.53061224489796
          },
          {
            "source": 43,
            "target": 137,
            "weight": 8.53061224489796
          },
          {
            "source": 43,
            "target": 142,
            "weight": 8.53061224489796
          },
          {
            "source": 43,
            "target": 117,
            "weight": 8.53061224489796
          },
          {
            "source": 44,
            "target": 64,
            "weight": 20.714285714285722
          },
          {
            "source": 44,
            "target": 57,
            "weight": 20.714285714285722
          },
          {
            "source": 44,
            "target": 87,
            "weight": 20.714285714285722
          },
          {
            "source": 44,
            "target": 188,
            "weight": 20.714285714285722
          },
          {
            "source": 44,
            "target": 195,
            "weight": 20.714285714285722
          },
          {
            "source": 45,
            "target": 161,
            "weight": 20.714285714285722
          },
          {
            "source": 45,
            "target": 196,
            "weight": 30.357142857142872
          },
          {
            "source": 45,
            "target": 59,
            "weight": 30.357142857142872
          },
          {
            "source": 45,
            "target": 62,
            "weight": 30.357142857142872
          },
          {
            "source": 45,
            "target": 70,
            "weight": 20.714285714285722
          },
          {
            "source": 45,
            "target": 125,
            "weight": 30.357142857142872
          },
          {
            "source": 46,
            "target": 196,
            "weight": 20.714285714285722
          },
          {
            "source": 46,
            "target": 86,
            "weight": 20.714285714285722
          },
          {
            "source": 46,
            "target": 62,
            "weight": 20.714285714285722
          },
          {
            "source": 46,
            "target": 150,
            "weight": 20.714285714285722
          },
          {
            "source": 46,
            "target": 119,
            "weight": 20.714285714285722
          },
          {
            "source": 46,
            "target": 125,
            "weight": 20.714285714285722
          },
          {
            "source": 47,
            "target": 97,
            "weight": 37.70408163265308
          },
          {
            "source": 47,
            "target": 149,
            "weight": 36.142857142857146
          },
          {
            "source": 47,
            "target": 199,
            "weight": 37.70408163265308
          },
          {
            "source": 48,
            "target": 201,
            "weight": 25.489795918367356
          },
          {
            "source": 48,
            "target": 136,
            "weight": 26.530612244897974
          },
          {
            "source": 48,
            "target": 86,
            "weight": 26.530612244897974
          },
          {
            "source": 48,
            "target": 62,
            "weight": 33.051020408163275
          },
          {
            "source": 48,
            "target": 144,
            "weight": 25.489795918367356
          },
          {
            "source": 48,
            "target": 196,
            "weight": 25.489795918367356
          },
          {
            "source": 49,
            "target": 197,
            "weight": 29.9591836734694
          },
          {
            "source": 50,
            "target": 207,
            "weight": 36.57142857142858
          },
          {
            "source": 50,
            "target": 197,
            "weight": 36.57142857142858
          },
          {
            "source": 51,
            "target": 202,
            "weight": 25.489795918367356
          },
          {
            "source": 51,
            "target": 203,
            "weight": 1.0
          },
          {
            "source": 51,
            "target": 208,
            "weight": 22.61224489795919
          },
          {
            "source": 51,
            "target": 108,
            "weight": 21.785714285714295
          },
          {
            "source": 51,
            "target": 214,
            "weight": 15.724489795918373
          },
          {
            "source": 51,
            "target": 123,
            "weight": 29.9591836734694
          },
          {
            "source": 51,
            "target": 134,
            "weight": 34.48979591836736
          },
          {
            "source": 51,
            "target": 135,
            "weight": 33.051020408163275
          },
          {
            "source": 51,
            "target": 141,
            "weight": 27.020408163265316
          },
          {
            "source": 51,
            "target": 175,
            "weight": 29.9591836734694
          },
          {
            "source": 51,
            "target": 55,
            "weight": 32.193877551020414
          },
          {
            "source": 51,
            "target": 165,
            "weight": 29.9591836734694
          },
          {
            "source": 51,
            "target": 60,
            "weight": 29.9591836734694
          },
          {
            "source": 51,
            "target": 167,
            "weight": 15.724489795918373
          },
          {
            "source": 51,
            "target": 62,
            "weight": 38.8979591836735
          },
          {
            "source": 51,
            "target": 63,
            "weight": 27.020408163265316
          },
          {
            "source": 51,
            "target": 198,
            "weight": 37.24489795918368
          },
          {
            "source": 51,
            "target": 170,
            "weight": 32.193877551020414
          },
          {
            "source": 51,
            "target": 71,
            "weight": 29.9591836734694
          },
          {
            "source": 51,
            "target": 74,
            "weight": 34.36734693877552
          },
          {
            "source": 51,
            "target": 77,
            "weight": 33.051020408163275
          },
          {
            "source": 51,
            "target": 78,
            "weight": 35.591836734693885
          },
          {
            "source": 51,
            "target": 181,
            "weight": 29.9591836734694
          },
          {
            "source": 51,
            "target": 86,
            "weight": 22.61224489795919
          },
          {
            "source": 51,
            "target": 113,
            "weight": 22.61224489795919
          },
          {
            "source": 51,
            "target": 90,
            "weight": 1.0
          },
          {
            "source": 51,
            "target": 92,
            "weight": 22.61224489795919
          },
          {
            "source": 51,
            "target": 191,
            "weight": 32.193877551020414
          },
          {
            "source": 51,
            "target": 192,
            "weight": 1.0
          },
          {
            "source": 51,
            "target": 97,
            "weight": 22.61224489795919
          },
          {
            "source": 51,
            "target": 67,
            "weight": 8.53061224489796
          },
          {
            "source": 51,
            "target": 196,
            "weight": 35.19387755102042
          },
          {
            "source": 51,
            "target": 104,
            "weight": 1.0
          },
          {
            "source": 52,
            "target": 130,
            "weight": 3.020408163265307
          },
          {
            "source": 52,
            "target": 53,
            "weight": 3.020408163265307
          },
          {
            "source": 52,
            "target": 204,
            "weight": 3.020408163265307
          },
          {
            "source": 52,
            "target": 162,
            "weight": 3.020408163265307
          },
          {
            "source": 52,
            "target": 139,
            "weight": 3.020408163265307
          },
          {
            "source": 52,
            "target": 107,
            "weight": 3.020408163265307
          },
          {
            "source": 52,
            "target": 147,
            "weight": 3.020408163265307
          },
          {
            "source": 52,
            "target": 176,
            "weight": 3.020408163265307
          },
          {
            "source": 53,
            "target": 204,
            "weight": 3.020408163265307
          },
          {
            "source": 53,
            "target": 162,
            "weight": 3.020408163265307
          },
          {
            "source": 53,
            "target": 130,
            "weight": 3.020408163265307
          },
          {
            "source": 53,
            "target": 139,
            "weight": 3.020408163265307
          },
          {
            "source": 53,
            "target": 107,
            "weight": 3.020408163265307
          },
          {
            "source": 53,
            "target": 147,
            "weight": 3.020408163265307
          },
          {
            "source": 53,
            "target": 176,
            "weight": 3.020408163265307
          },
          {
            "source": 54,
            "target": 198,
            "weight": 26.530612244897974
          },
          {
            "source": 54,
            "target": 62,
            "weight": 26.530612244897974
          },
          {
            "source": 55,
            "target": 164,
            "weight": 25.489795918367356
          },
          {
            "source": 55,
            "target": 62,
            "weight": 38.683673469387784
          },
          {
            "source": 55,
            "target": 125,
            "weight": 26.530612244897974
          },
          {
            "source": 55,
            "target": 170,
            "weight": 35.438775510204096
          },
          {
            "source": 55,
            "target": 191,
            "weight": 38.346938775510225
          },
          {
            "source": 55,
            "target": 196,
            "weight": 26.530612244897974
          },
          {
            "source": 55,
            "target": 171,
            "weight": 25.489795918367356
          },
          {
            "source": 56,
            "target": 200,
            "weight": 21.785714285714295
          },
          {
            "source": 56,
            "target": 186,
            "weight": 21.785714285714295
          },
          {
            "source": 56,
            "target": 172,
            "weight": 21.785714285714295
          },
          {
            "source": 56,
            "target": 196,
            "weight": 21.785714285714295
          },
          {
            "source": 57,
            "target": 64,
            "weight": 20.714285714285722
          },
          {
            "source": 57,
            "target": 87,
            "weight": 20.714285714285722
          },
          {
            "source": 57,
            "target": 188,
            "weight": 20.714285714285722
          },
          {
            "source": 57,
            "target": 195,
            "weight": 20.714285714285722
          },
          {
            "source": 58,
            "target": 207,
            "weight": 26.530612244897974
          },
          {
            "source": 58,
            "target": 197,
            "weight": 26.530612244897974
          },
          {
            "source": 59,
            "target": 161,
            "weight": 20.714285714285722
          },
          {
            "source": 59,
            "target": 196,
            "weight": 30.357142857142872
          },
          {
            "source": 59,
            "target": 62,
            "weight": 30.357142857142872
          },
          {
            "source": 59,
            "target": 70,
            "weight": 20.714285714285722
          },
          {
            "source": 59,
            "target": 125,
            "weight": 30.357142857142872
          },
          {
            "source": 60,
            "target": 78,
            "weight": 29.9591836734694
          },
          {
            "source": 60,
            "target": 62,
            "weight": 29.9591836734694
          },
          {
            "source": 60,
            "target": 123,
            "weight": 29.9591836734694
          },
          {
            "source": 61,
            "target": 179,
            "weight": 15.724489795918373
          },
          {
            "source": 61,
            "target": 159,
            "weight": 36.72448979591837
          },
          {
            "source": 61,
            "target": 65,
            "weight": 37.826530612244916
          },
          {
            "source": 61,
            "target": 197,
            "weight": 37.826530612244916
          },
          {
            "source": 61,
            "target": 76,
            "weight": 34.48979591836736
          },
          {
            "source": 62,
            "target": 63,
            "weight": 33.23469387755103
          },
          {
            "source": 62,
            "target": 67,
            "weight": 8.53061224489796
          },
          {
            "source": 62,
            "target": 70,
            "weight": 20.714285714285722
          },
          {
            "source": 62,
            "target": 71,
            "weight": 29.9591836734694
          },
          {
            "source": 62,
            "target": 74,
            "weight": 34.36734693877552
          },
          {
            "source": 62,
            "target": 77,
            "weight": 36.29591836734695
          },
          {
            "source": 62,
            "target": 78,
            "weight": 35.591836734693885
          },
          {
            "source": 62,
            "target": 80,
            "weight": 15.724489795918373
          },
          {
            "source": 62,
            "target": 85,
            "weight": 37.06122448979593
          },
          {
            "source": 62,
            "target": 86,
            "weight": 36.72448979591837
          },
          {
            "source": 62,
            "target": 88,
            "weight": 21.785714285714295
          },
          {
            "source": 62,
            "target": 89,
            "weight": 25.489795918367356
          },
          {
            "source": 62,
            "target": 90,
            "weight": 1.0
          },
          {
            "source": 62,
            "target": 91,
            "weight": 37.06122448979593
          },
          {
            "source": 62,
            "target": 92,
            "weight": 22.61224489795919
          },
          {
            "source": 62,
            "target": 95,
            "weight": 15.724489795918373
          },
          {
            "source": 62,
            "target": 97,
            "weight": 22.61224489795919
          },
          {
            "source": 62,
            "target": 100,
            "weight": 15.724489795918373
          },
          {
            "source": 62,
            "target": 101,
            "weight": 15.724489795918373
          },
          {
            "source": 62,
            "target": 104,
            "weight": 1.0
          },
          {
            "source": 62,
            "target": 108,
            "weight": 21.785714285714295
          },
          {
            "source": 62,
            "target": 109,
            "weight": 25.489795918367356
          },
          {
            "source": 62,
            "target": 111,
            "weight": 25.489795918367356
          },
          {
            "source": 62,
            "target": 113,
            "weight": 22.61224489795919
          },
          {
            "source": 62,
            "target": 118,
            "weight": 35.98979591836735
          },
          {
            "source": 62,
            "target": 119,
            "weight": 34.122448979591844
          },
          {
            "source": 62,
            "target": 122,
            "weight": 29.9591836734694
          },
          {
            "source": 62,
            "target": 123,
            "weight": 29.9591836734694
          },
          {
            "source": 62,
            "target": 124,
            "weight": 39.204081632653086
          },
          {
            "source": 62,
            "target": 125,
            "weight": 38.5918367346939
          },
          {
            "source": 62,
            "target": 129,
            "weight": 35.98979591836735
          },
          {
            "source": 62,
            "target": 134,
            "weight": 34.48979591836736
          },
          {
            "source": 62,
            "target": 135,
            "weight": 33.051020408163275
          },
          {
            "source": 62,
            "target": 136,
            "weight": 26.530612244897974
          },
          {
            "source": 62,
            "target": 141,
            "weight": 27.020408163265316
          },
          {
            "source": 62,
            "target": 144,
            "weight": 34.36734693877552
          },
          {
            "source": 62,
            "target": 145,
            "weight": 25.489795918367356
          },
          {
            "source": 62,
            "target": 146,
            "weight": 15.724489795918373
          },
          {
            "source": 62,
            "target": 150,
            "weight": 20.714285714285722
          },
          {
            "source": 62,
            "target": 154,
            "weight": 37.91836734693879
          },
          {
            "source": 62,
            "target": 156,
            "weight": 25.489795918367356
          },
          {
            "source": 62,
            "target": 161,
            "weight": 20.714285714285722
          },
          {
            "source": 62,
            "target": 164,
            "weight": 37.520408163265316
          },
          {
            "source": 62,
            "target": 165,
            "weight": 29.9591836734694
          },
          {
            "source": 62,
            "target": 167,
            "weight": 15.724489795918373
          },
          {
            "source": 62,
            "target": 169,
            "weight": 15.724489795918373
          },
          {
            "source": 62,
            "target": 170,
            "weight": 35.438775510204096
          },
          {
            "source": 62,
            "target": 171,
            "weight": 25.489795918367356
          },
          {
            "source": 62,
            "target": 172,
            "weight": 32.193877551020414
          },
          {
            "source": 62,
            "target": 174,
            "weight": 32.193877551020414
          },
          {
            "source": 62,
            "target": 175,
            "weight": 29.9591836734694
          },
          {
            "source": 62,
            "target": 177,
            "weight": 15.724489795918373
          },
          {
            "source": 62,
            "target": 180,
            "weight": 29.9591836734694
          },
          {
            "source": 62,
            "target": 181,
            "weight": 29.9591836734694
          },
          {
            "source": 62,
            "target": 182,
            "weight": 20.714285714285722
          },
          {
            "source": 62,
            "target": 184,
            "weight": 15.724489795918373
          },
          {
            "source": 62,
            "target": 185,
            "weight": 21.785714285714295
          },
          {
            "source": 62,
            "target": 191,
            "weight": 38.50000000000003
          },
          {
            "source": 62,
            "target": 192,
            "weight": 1.0
          },
          {
            "source": 62,
            "target": 193,
            "weight": 29.9591836734694
          },
          {
            "source": 62,
            "target": 196,
            "weight": 39.32653061224492
          },
          {
            "source": 62,
            "target": 198,
            "weight": 38.316326530612265
          },
          {
            "source": 62,
            "target": 201,
            "weight": 37.153061224489804
          },
          {
            "source": 62,
            "target": 202,
            "weight": 33.051020408163275
          },
          {
            "source": 62,
            "target": 203,
            "weight": 1.0
          },
          {
            "source": 62,
            "target": 208,
            "weight": 22.61224489795919
          },
          {
            "source": 62,
            "target": 209,
            "weight": 15.724489795918373
          },
          {
            "source": 62,
            "target": 213,
            "weight": 25.489795918367356
          },
          {
            "source": 62,
            "target": 214,
            "weight": 38.95918367346942
          },
          {
            "source": 63,
            "target": 77,
            "weight": 33.23469387755103
          },
          {
            "source": 63,
            "target": 134,
            "weight": 27.020408163265316
          },
          {
            "source": 63,
            "target": 135,
            "weight": 27.020408163265316
          },
          {
            "source": 63,
            "target": 141,
            "weight": 27.020408163265316
          },
          {
            "source": 63,
            "target": 111,
            "weight": 25.489795918367356
          },
          {
            "source": 64,
            "target": 210,
            "weight": 1.0
          },
          {
            "source": 64,
            "target": 127,
            "weight": 1.0
          },
          {
            "source": 64,
            "target": 212,
            "weight": 15.724489795918373
          },
          {
            "source": 64,
            "target": 116,
            "weight": 15.724489795918373
          },
          {
            "source": 64,
            "target": 128,
            "weight": 1.0
          },
          {
            "source": 64,
            "target": 131,
            "weight": 15.724489795918373
          },
          {
            "source": 64,
            "target": 140,
            "weight": 1.0
          },
          {
            "source": 64,
            "target": 152,
            "weight": 1.0
          },
          {
            "source": 64,
            "target": 66,
            "weight": 15.724489795918373
          },
          {
            "source": 64,
            "target": 72,
            "weight": 1.0
          },
          {
            "source": 64,
            "target": 87,
            "weight": 20.714285714285722
          },
          {
            "source": 64,
            "target": 188,
            "weight": 27.6326530612245
          },
          {
            "source": 64,
            "target": 93,
            "weight": 1.0
          },
          {
            "source": 64,
            "target": 96,
            "weight": 1.0
          },
          {
            "source": 64,
            "target": 194,
            "weight": 1.0
          },
          {
            "source": 64,
            "target": 195,
            "weight": 27.6326530612245
          },
          {
            "source": 64,
            "target": 105,
            "weight": 1.0
          },
          {
            "source": 65,
            "target": 179,
            "weight": 15.724489795918373
          },
          {
            "source": 65,
            "target": 159,
            "weight": 39.418367346938794
          },
          {
            "source": 65,
            "target": 81,
            "weight": 32.193877551020414
          },
          {
            "source": 65,
            "target": 207,
            "weight": 39.38775510204084
          },
          {
            "source": 65,
            "target": 126,
            "weight": 15.724489795918373
          },
          {
            "source": 65,
            "target": 168,
            "weight": 32.193877551020414
          },
          {
            "source": 65,
            "target": 94,
            "weight": 33.87755102040817
          },
          {
            "source": 65,
            "target": 99,
            "weight": 11.102040816326532
          },
          {
            "source": 65,
            "target": 197,
            "weight": 39.63265306122451
          },
          {
            "source": 65,
            "target": 75,
            "weight": 27.6326530612245
          },
          {
            "source": 65,
            "target": 76,
            "weight": 38.98979591836738
          },
          {
            "source": 66,
            "target": 131,
            "weight": 15.724489795918373
          },
          {
            "source": 66,
            "target": 188,
            "weight": 15.724489795918373
          },
          {
            "source": 66,
            "target": 212,
            "weight": 15.724489795918373
          },
          {
            "source": 66,
            "target": 116,
            "weight": 15.724489795918373
          },
          {
            "source": 66,
            "target": 195,
            "weight": 15.724489795918373
          },
          {
            "source": 67,
            "target": 208,
            "weight": 8.53061224489796
          },
          {
            "source": 67,
            "target": 86,
            "weight": 8.53061224489796
          },
          {
            "source": 67,
            "target": 113,
            "weight": 8.53061224489796
          },
          {
            "source": 67,
            "target": 92,
            "weight": 8.53061224489796
          },
          {
            "source": 67,
            "target": 97,
            "weight": 8.53061224489796
          },
          {
            "source": 67,
            "target": 74,
            "weight": 8.53061224489796
          },
          {
            "source": 68,
            "target": 178,
            "weight": 8.53061224489796
          },
          {
            "source": 68,
            "target": 157,
            "weight": 8.53061224489796
          },
          {
            "source": 68,
            "target": 166,
            "weight": 8.53061224489796
          },
          {
            "source": 68,
            "target": 114,
            "weight": 8.53061224489796
          },
          {
            "source": 68,
            "target": 148,
            "weight": 8.53061224489796
          },
          {
            "source": 68,
            "target": 173,
            "weight": 8.53061224489796
          },
          {
            "source": 68,
            "target": 82,
            "weight": 8.53061224489796
          },
          {
            "source": 69,
            "target": 163,
            "weight": 15.724489795918373
          },
          {
            "source": 69,
            "target": 207,
            "weight": 15.724489795918373
          },
          {
            "source": 69,
            "target": 143,
            "weight": 15.724489795918373
          },
          {
            "source": 69,
            "target": 197,
            "weight": 15.724489795918373
          },
          {
            "source": 70,
            "target": 161,
            "weight": 20.714285714285722
          },
          {
            "source": 70,
            "target": 196,
            "weight": 20.714285714285722
          },
          {
            "source": 70,
            "target": 125,
            "weight": 20.714285714285722
          },
          {
            "source": 71,
            "target": 181,
            "weight": 29.9591836734694
          },
          {
            "source": 71,
            "target": 175,
            "weight": 29.9591836734694
          },
          {
            "source": 71,
            "target": 198,
            "weight": 29.9591836734694
          },
          {
            "source": 72,
            "target": 140,
            "weight": 1.0
          },
          {
            "source": 72,
            "target": 210,
            "weight": 1.0
          },
          {
            "source": 72,
            "target": 93,
            "weight": 1.0
          },
          {
            "source": 72,
            "target": 96,
            "weight": 1.0
          },
          {
            "source": 72,
            "target": 194,
            "weight": 1.0
          },
          {
            "source": 72,
            "target": 152,
            "weight": 1.0
          },
          {
            "source": 72,
            "target": 128,
            "weight": 1.0
          },
          {
            "source": 72,
            "target": 127,
            "weight": 1.0
          },
          {
            "source": 72,
            "target": 105,
            "weight": 1.0
          },
          {
            "source": 73,
            "target": 106,
            "weight": 20.714285714285722
          },
          {
            "source": 73,
            "target": 187,
            "weight": 20.714285714285722
          },
          {
            "source": 73,
            "target": 207,
            "weight": 20.714285714285722
          },
          {
            "source": 74,
            "target": 78,
            "weight": 15.724489795918373
          },
          {
            "source": 74,
            "target": 208,
            "weight": 8.53061224489796
          },
          {
            "source": 74,
            "target": 134,
            "weight": 27.6326530612245
          },
          {
            "source": 74,
            "target": 86,
            "weight": 8.53061224489796
          },
          {
            "source": 74,
            "target": 113,
            "weight": 8.53061224489796
          },
          {
            "source": 74,
            "target": 167,
            "weight": 15.724489795918373
          },
          {
            "source": 74,
            "target": 92,
            "weight": 8.53061224489796
          },
          {
            "source": 74,
            "target": 214,
            "weight": 15.724489795918373
          },
          {
            "source": 74,
            "target": 97,
            "weight": 8.53061224489796
          },
          {
            "source": 74,
            "target": 196,
            "weight": 32.59183673469388
          },
          {
            "source": 74,
            "target": 198,
            "weight": 32.59183673469388
          },
          {
            "source": 75,
            "target": 159,
            "weight": 27.6326530612245
          },
          {
            "source": 75,
            "target": 126,
            "weight": 15.724489795918373
          },
          {
            "source": 75,
            "target": 197,
            "weight": 27.6326530612245
          },
          {
            "source": 76,
            "target": 94,
            "weight": 15.724489795918373
          },
          {
            "source": 76,
            "target": 159,
            "weight": 39.14285714285717
          },
          {
            "source": 76,
            "target": 81,
            "weight": 32.193877551020414
          },
          {
            "source": 76,
            "target": 207,
            "weight": 38.43877551020411
          },
          {
            "source": 76,
            "target": 168,
            "weight": 32.193877551020414
          },
          {
            "source": 76,
            "target": 99,
            "weight": 11.102040816326532
          },
          {
            "source": 76,
            "target": 197,
            "weight": 39.14285714285717
          },
          {
            "source": 77,
            "target": 108,
            "weight": 21.785714285714295
          },
          {
            "source": 77,
            "target": 111,
            "weight": 25.489795918367356
          },
          {
            "source": 77,
            "target": 120,
            "weight": 11.102040816326532
          },
          {
            "source": 77,
            "target": 214,
            "weight": 11.102040816326532
          },
          {
            "source": 77,
            "target": 134,
            "weight": 27.020408163265316
          },
          {
            "source": 77,
            "target": 135,
            "weight": 33.051020408163275
          },
          {
            "source": 77,
            "target": 141,
            "weight": 27.020408163265316
          },
          {
            "source": 77,
            "target": 172,
            "weight": 20.714285714285722
          },
          {
            "source": 77,
            "target": 182,
            "weight": 27.142857142857157
          },
          {
            "source": 77,
            "target": 102,
            "weight": 11.102040816326532
          },
          {
            "source": 77,
            "target": 88,
            "weight": 11.102040816326532
          },
          {
            "source": 77,
            "target": 119,
            "weight": 20.714285714285722
          },
          {
            "source": 77,
            "target": 196,
            "weight": 11.102040816326532
          },
          {
            "source": 78,
            "target": 134,
            "weight": 15.724489795918373
          },
          {
            "source": 78,
            "target": 123,
            "weight": 29.9591836734694
          },
          {
            "source": 78,
            "target": 196,
            "weight": 15.724489795918373
          },
          {
            "source": 78,
            "target": 198,
            "weight": 15.724489795918373
          },
          {
            "source": 79,
            "target": 160,
            "weight": 8.53061224489796
          },
          {
            "source": 79,
            "target": 137,
            "weight": 8.53061224489796
          },
          {
            "source": 79,
            "target": 142,
            "weight": 8.53061224489796
          },
          {
            "source": 79,
            "target": 117,
            "weight": 8.53061224489796
          },
          {
            "source": 80,
            "target": 184,
            "weight": 15.724489795918373
          },
          {
            "source": 80,
            "target": 209,
            "weight": 15.724489795918373
          },
          {
            "source": 80,
            "target": 144,
            "weight": 15.724489795918373
          },
          {
            "source": 80,
            "target": 191,
            "weight": 15.724489795918373
          },
          {
            "source": 80,
            "target": 101,
            "weight": 15.724489795918373
          },
          {
            "source": 80,
            "target": 177,
            "weight": 15.724489795918373
          },
          {
            "source": 81,
            "target": 159,
            "weight": 32.193877551020414
          },
          {
            "source": 81,
            "target": 168,
            "weight": 32.193877551020414
          },
          {
            "source": 81,
            "target": 197,
            "weight": 32.193877551020414
          },
          {
            "source": 82,
            "target": 178,
            "weight": 8.53061224489796
          },
          {
            "source": 82,
            "target": 157,
            "weight": 8.53061224489796
          },
          {
            "source": 82,
            "target": 166,
            "weight": 8.53061224489796
          },
          {
            "source": 82,
            "target": 114,
            "weight": 8.53061224489796
          },
          {
            "source": 82,
            "target": 148,
            "weight": 8.53061224489796
          },
          {
            "source": 82,
            "target": 173,
            "weight": 8.53061224489796
          },
          {
            "source": 83,
            "target": 134,
            "weight": 32.193877551020414
          },
          {
            "source": 84,
            "target": 207,
            "weight": 35.1326530612245
          },
          {
            "source": 85,
            "target": 91,
            "weight": 37.06122448979593
          },
          {
            "source": 85,
            "target": 214,
            "weight": 37.06122448979593
          },
          {
            "source": 85,
            "target": 124,
            "weight": 37.06122448979593
          },
          {
            "source": 86,
            "target": 203,
            "weight": 1.0
          },
          {
            "source": 86,
            "target": 208,
            "weight": 22.61224489795919
          },
          {
            "source": 86,
            "target": 113,
            "weight": 22.61224489795919
          },
          {
            "source": 86,
            "target": 119,
            "weight": 20.714285714285722
          },
          {
            "source": 86,
            "target": 125,
            "weight": 32.193877551020414
          },
          {
            "source": 86,
            "target": 136,
            "weight": 26.530612244897974
          },
          {
            "source": 86,
            "target": 150,
            "weight": 20.714285714285722
          },
          {
            "source": 86,
            "target": 90,
            "weight": 1.0
          },
          {
            "source": 86,
            "target": 92,
            "weight": 22.61224489795919
          },
          {
            "source": 86,
            "target": 192,
            "weight": 1.0
          },
          {
            "source": 86,
            "target": 97,
            "weight": 22.61224489795919
          },
          {
            "source": 86,
            "target": 196,
            "weight": 32.193877551020414
          },
          {
            "source": 86,
            "target": 104,
            "weight": 1.0
          },
          {
            "source": 87,
            "target": 188,
            "weight": 20.714285714285722
          },
          {
            "source": 87,
            "target": 195,
            "weight": 20.714285714285722
          },
          {
            "source": 88,
            "target": 191,
            "weight": 21.785714285714295
          },
          {
            "source": 88,
            "target": 182,
            "weight": 11.102040816326532
          },
          {
            "source": 88,
            "target": 164,
            "weight": 21.785714285714295
          },
          {
            "source": 88,
            "target": 185,
            "weight": 21.785714285714295
          },
          {
            "source": 88,
            "target": 196,
            "weight": 34.36734693877552
          },
          {
            "source": 88,
            "target": 214,
            "weight": 30.51020408163266
          },
          {
            "source": 88,
            "target": 102,
            "weight": 11.102040816326532
          },
          {
            "source": 88,
            "target": 120,
            "weight": 11.102040816326532
          },
          {
            "source": 89,
            "target": 213,
            "weight": 25.489795918367356
          },
          {
            "source": 89,
            "target": 196,
            "weight": 25.489795918367356
          },
          {
            "source": 90,
            "target": 203,
            "weight": 1.0
          },
          {
            "source": 90,
            "target": 113,
            "weight": 1.0
          },
          {
            "source": 90,
            "target": 92,
            "weight": 1.0
          },
          {
            "source": 90,
            "target": 192,
            "weight": 1.0
          },
          {
            "source": 90,
            "target": 97,
            "weight": 1.0
          },
          {
            "source": 90,
            "target": 208,
            "weight": 1.0
          },
          {
            "source": 90,
            "target": 104,
            "weight": 1.0
          },
          {
            "source": 91,
            "target": 214,
            "weight": 37.06122448979593
          },
          {
            "source": 91,
            "target": 124,
            "weight": 37.06122448979593
          },
          {
            "source": 92,
            "target": 203,
            "weight": 1.0
          },
          {
            "source": 92,
            "target": 208,
            "weight": 22.61224489795919
          },
          {
            "source": 92,
            "target": 113,
            "weight": 22.61224489795919
          },
          {
            "source": 92,
            "target": 192,
            "weight": 1.0
          },
          {
            "source": 92,
            "target": 97,
            "weight": 22.61224489795919
          },
          {
            "source": 92,
            "target": 104,
            "weight": 1.0
          },
          {
            "source": 93,
            "target": 152,
            "weight": 1.0
          },
          {
            "source": 93,
            "target": 140,
            "weight": 1.0
          },
          {
            "source": 93,
            "target": 210,
            "weight": 1.0
          },
          {
            "source": 93,
            "target": 96,
            "weight": 1.0
          },
          {
            "source": 93,
            "target": 194,
            "weight": 1.0
          },
          {
            "source": 93,
            "target": 128,
            "weight": 1.0
          },
          {
            "source": 93,
            "target": 127,
            "weight": 1.0
          },
          {
            "source": 93,
            "target": 105,
            "weight": 1.0
          },
          {
            "source": 94,
            "target": 159,
            "weight": 15.724489795918373
          },
          {
            "source": 94,
            "target": 207,
            "weight": 38.8979591836735
          },
          {
            "source": 94,
            "target": 141,
            "weight": 20.714285714285722
          },
          {
            "source": 94,
            "target": 112,
            "weight": 20.714285714285722
          },
          {
            "source": 94,
            "target": 197,
            "weight": 38.8979591836735
          },
          {
            "source": 95,
            "target": 196,
            "weight": 15.724489795918373
          },
          {
            "source": 95,
            "target": 169,
            "weight": 15.724489795918373
          },
          {
            "source": 95,
            "target": 146,
            "weight": 15.724489795918373
          },
          {
            "source": 95,
            "target": 100,
            "weight": 15.724489795918373
          },
          {
            "source": 95,
            "target": 125,
            "weight": 15.724489795918373
          },
          {
            "source": 96,
            "target": 140,
            "weight": 1.0
          },
          {
            "source": 96,
            "target": 210,
            "weight": 1.0
          },
          {
            "source": 96,
            "target": 194,
            "weight": 1.0
          },
          {
            "source": 96,
            "target": 152,
            "weight": 1.0
          },
          {
            "source": 96,
            "target": 128,
            "weight": 1.0
          },
          {
            "source": 96,
            "target": 127,
            "weight": 1.0
          },
          {
            "source": 96,
            "target": 105,
            "weight": 1.0
          },
          {
            "source": 97,
            "target": 203,
            "weight": 1.0
          },
          {
            "source": 97,
            "target": 113,
            "weight": 22.61224489795919
          },
          {
            "source": 97,
            "target": 192,
            "weight": 1.0
          },
          {
            "source": 97,
            "target": 149,
            "weight": 36.142857142857146
          },
          {
            "source": 97,
            "target": 208,
            "weight": 22.61224489795919
          },
          {
            "source": 97,
            "target": 104,
            "weight": 1.0
          },
          {
            "source": 97,
            "target": 199,
            "weight": 37.70408163265308
          },
          {
            "source": 98,
            "target": 113,
            "weight": 29.9591836734694
          },
          {
            "source": 99,
            "target": 159,
            "weight": 11.102040816326532
          },
          {
            "source": 99,
            "target": 207,
            "weight": 11.102040816326532
          },
          {
            "source": 99,
            "target": 197,
            "weight": 11.102040816326532
          },
          {
            "source": 100,
            "target": 196,
            "weight": 15.724489795918373
          },
          {
            "source": 100,
            "target": 169,
            "weight": 15.724489795918373
          },
          {
            "source": 100,
            "target": 146,
            "weight": 15.724489795918373
          },
          {
            "source": 100,
            "target": 125,
            "weight": 15.724489795918373
          },
          {
            "source": 101,
            "target": 184,
            "weight": 15.724489795918373
          },
          {
            "source": 101,
            "target": 209,
            "weight": 15.724489795918373
          },
          {
            "source": 101,
            "target": 144,
            "weight": 15.724489795918373
          },
          {
            "source": 101,
            "target": 191,
            "weight": 15.724489795918373
          },
          {
            "source": 101,
            "target": 177,
            "weight": 15.724489795918373
          },
          {
            "source": 102,
            "target": 182,
            "weight": 11.102040816326532
          },
          {
            "source": 102,
            "target": 120,
            "weight": 11.102040816326532
          },
          {
            "source": 102,
            "target": 214,
            "weight": 11.102040816326532
          },
          {
            "source": 102,
            "target": 196,
            "weight": 11.102040816326532
          },
          {
            "source": 103,
            "target": 206,
            "weight": 20.714285714285722
          },
          {
            "source": 103,
            "target": 211,
            "weight": 20.714285714285722
          },
          {
            "source": 103,
            "target": 121,
            "weight": 20.714285714285722
          },
          {
            "source": 103,
            "target": 155,
            "weight": 20.714285714285722
          },
          {
            "source": 104,
            "target": 203,
            "weight": 1.0
          },
          {
            "source": 104,
            "target": 208,
            "weight": 1.0
          },
          {
            "source": 104,
            "target": 113,
            "weight": 1.0
          },
          {
            "source": 104,
            "target": 192,
            "weight": 1.0
          },
          {
            "source": 105,
            "target": 140,
            "weight": 1.0
          },
          {
            "source": 105,
            "target": 210,
            "weight": 1.0
          },
          {
            "source": 105,
            "target": 194,
            "weight": 1.0
          },
          {
            "source": 105,
            "target": 152,
            "weight": 1.0
          },
          {
            "source": 105,
            "target": 127,
            "weight": 1.0
          },
          {
            "source": 105,
            "target": 128,
            "weight": 1.0
          },
          {
            "source": 106,
            "target": 187,
            "weight": 20.714285714285722
          },
          {
            "source": 106,
            "target": 207,
            "weight": 20.714285714285722
          },
          {
            "source": 107,
            "target": 130,
            "weight": 3.020408163265307
          },
          {
            "source": 107,
            "target": 204,
            "weight": 3.020408163265307
          },
          {
            "source": 107,
            "target": 162,
            "weight": 3.020408163265307
          },
          {
            "source": 107,
            "target": 139,
            "weight": 3.020408163265307
          },
          {
            "source": 107,
            "target": 147,
            "weight": 3.020408163265307
          },
          {
            "source": 107,
            "target": 176,
            "weight": 3.020408163265307
          },
          {
            "source": 108,
            "target": 135,
            "weight": 21.785714285714295
          },
          {
            "source": 109,
            "target": 125,
            "weight": 25.489795918367356
          },
          {
            "source": 109,
            "target": 119,
            "weight": 25.489795918367356
          },
          {
            "source": 109,
            "target": 196,
            "weight": 25.489795918367356
          },
          {
            "source": 110,
            "target": 187,
            "weight": 29.9591836734694
          },
          {
            "source": 110,
            "target": 189,
            "weight": 29.9591836734694
          },
          {
            "source": 110,
            "target": 190,
            "weight": 35.438775510204096
          },
          {
            "source": 110,
            "target": 207,
            "weight": 36.23469387755103
          },
          {
            "source": 112,
            "target": 207,
            "weight": 20.714285714285722
          },
          {
            "source": 112,
            "target": 141,
            "weight": 20.714285714285722
          },
          {
            "source": 112,
            "target": 197,
            "weight": 20.714285714285722
          },
          {
            "source": 113,
            "target": 203,
            "weight": 1.0
          },
          {
            "source": 113,
            "target": 208,
            "weight": 22.61224489795919
          },
          {
            "source": 113,
            "target": 192,
            "weight": 1.0
          },
          {
            "source": 114,
            "target": 178,
            "weight": 8.53061224489796
          },
          {
            "source": 114,
            "target": 157,
            "weight": 8.53061224489796
          },
          {
            "source": 114,
            "target": 166,
            "weight": 8.53061224489796
          },
          {
            "source": 114,
            "target": 148,
            "weight": 8.53061224489796
          },
          {
            "source": 114,
            "target": 173,
            "weight": 8.53061224489796
          },
          {
            "source": 115,
            "target": 132,
            "weight": 20.714285714285722
          },
          {
            "source": 115,
            "target": 207,
            "weight": 20.714285714285722
          },
          {
            "source": 115,
            "target": 197,
            "weight": 20.714285714285722
          },
          {
            "source": 116,
            "target": 131,
            "weight": 15.724489795918373
          },
          {
            "source": 116,
            "target": 212,
            "weight": 15.724489795918373
          },
          {
            "source": 116,
            "target": 188,
            "weight": 15.724489795918373
          },
          {
            "source": 116,
            "target": 195,
            "weight": 15.724489795918373
          },
          {
            "source": 117,
            "target": 160,
            "weight": 8.53061224489796
          },
          {
            "source": 117,
            "target": 137,
            "weight": 8.53061224489796
          },
          {
            "source": 117,
            "target": 142,
            "weight": 8.53061224489796
          },
          {
            "source": 118,
            "target": 145,
            "weight": 25.489795918367356
          },
          {
            "source": 118,
            "target": 124,
            "weight": 35.98979591836735
          },
          {
            "source": 118,
            "target": 214,
            "weight": 35.98979591836735
          },
          {
            "source": 119,
            "target": 125,
            "weight": 30.357142857142872
          },
          {
            "source": 119,
            "target": 172,
            "weight": 20.714285714285722
          },
          {
            "source": 119,
            "target": 150,
            "weight": 20.714285714285722
          },
          {
            "source": 119,
            "target": 196,
            "weight": 30.357142857142872
          },
          {
            "source": 119,
            "target": 182,
            "weight": 20.714285714285722
          },
          {
            "source": 120,
            "target": 182,
            "weight": 11.102040816326532
          },
          {
            "source": 120,
            "target": 214,
            "weight": 11.102040816326532
          },
          {
            "source": 120,
            "target": 196,
            "weight": 11.102040816326532
          },
          {
            "source": 121,
            "target": 206,
            "weight": 20.714285714285722
          },
          {
            "source": 121,
            "target": 211,
            "weight": 20.714285714285722
          },
          {
            "source": 121,
            "target": 155,
            "weight": 20.714285714285722
          },
          {
            "source": 122,
            "target": 180,
            "weight": 29.9591836734694
          },
          {
            "source": 124,
            "target": 145,
            "weight": 25.489795918367356
          },
          {
            "source": 124,
            "target": 214,
            "weight": 38.92857142857146
          },
          {
            "source": 124,
            "target": 154,
            "weight": 37.91836734693879
          },
          {
            "source": 124,
            "target": 198,
            "weight": 26.530612244897974
          },
          {
            "source": 125,
            "target": 129,
            "weight": 35.98979591836735
          },
          {
            "source": 125,
            "target": 146,
            "weight": 15.724489795918373
          },
          {
            "source": 125,
            "target": 150,
            "weight": 20.714285714285722
          },
          {
            "source": 125,
            "target": 156,
            "weight": 25.489795918367356
          },
          {
            "source": 125,
            "target": 161,
            "weight": 20.714285714285722
          },
          {
            "source": 125,
            "target": 164,
            "weight": 33.051020408163275
          },
          {
            "source": 125,
            "target": 169,
            "weight": 15.724489795918373
          },
          {
            "source": 125,
            "target": 172,
            "weight": 26.530612244897974
          },
          {
            "source": 125,
            "target": 174,
            "weight": 25.489795918367356
          },
          {
            "source": 125,
            "target": 196,
            "weight": 38.5918367346939
          },
          {
            "source": 126,
            "target": 159,
            "weight": 15.724489795918373
          },
          {
            "source": 126,
            "target": 197,
            "weight": 15.724489795918373
          },
          {
            "source": 127,
            "target": 140,
            "weight": 1.0
          },
          {
            "source": 127,
            "target": 210,
            "weight": 1.0
          },
          {
            "source": 127,
            "target": 194,
            "weight": 1.0
          },
          {
            "source": 127,
            "target": 152,
            "weight": 1.0
          },
          {
            "source": 127,
            "target": 128,
            "weight": 1.0
          },
          {
            "source": 128,
            "target": 140,
            "weight": 1.0
          },
          {
            "source": 128,
            "target": 210,
            "weight": 1.0
          },
          {
            "source": 128,
            "target": 194,
            "weight": 1.0
          },
          {
            "source": 128,
            "target": 152,
            "weight": 1.0
          },
          {
            "source": 129,
            "target": 156,
            "weight": 25.489795918367356
          },
          {
            "source": 129,
            "target": 196,
            "weight": 35.98979591836735
          },
          {
            "source": 130,
            "target": 204,
            "weight": 3.020408163265307
          },
          {
            "source": 130,
            "target": 162,
            "weight": 3.020408163265307
          },
          {
            "source": 130,
            "target": 139,
            "weight": 3.020408163265307
          },
          {
            "source": 130,
            "target": 147,
            "weight": 3.020408163265307
          },
          {
            "source": 130,
            "target": 176,
            "weight": 3.020408163265307
          },
          {
            "source": 131,
            "target": 212,
            "weight": 15.724489795918373
          },
          {
            "source": 131,
            "target": 188,
            "weight": 15.724489795918373
          },
          {
            "source": 131,
            "target": 195,
            "weight": 15.724489795918373
          },
          {
            "source": 132,
            "target": 207,
            "weight": 20.714285714285722
          },
          {
            "source": 132,
            "target": 197,
            "weight": 20.714285714285722
          },
          {
            "source": 133,
            "target": 207,
            "weight": 25.489795918367356
          },
          {
            "source": 133,
            "target": 197,
            "weight": 25.489795918367356
          },
          {
            "source": 134,
            "target": 135,
            "weight": 27.020408163265316
          },
          {
            "source": 134,
            "target": 141,
            "weight": 27.020408163265316
          },
          {
            "source": 134,
            "target": 196,
            "weight": 27.6326530612245
          },
          {
            "source": 134,
            "target": 198,
            "weight": 27.6326530612245
          },
          {
            "source": 135,
            "target": 141,
            "weight": 27.020408163265316
          },
          {
            "source": 137,
            "target": 160,
            "weight": 8.53061224489796
          },
          {
            "source": 137,
            "target": 142,
            "weight": 8.53061224489796
          },
          {
            "source": 138,
            "target": 158,
            "weight": 20.714285714285722
          },
          {
            "source": 138,
            "target": 207,
            "weight": 20.714285714285722
          },
          {
            "source": 138,
            "target": 197,
            "weight": 20.714285714285722
          },
          {
            "source": 139,
            "target": 204,
            "weight": 3.020408163265307
          },
          {
            "source": 139,
            "target": 162,
            "weight": 3.020408163265307
          },
          {
            "source": 139,
            "target": 147,
            "weight": 3.020408163265307
          },
          {
            "source": 139,
            "target": 176,
            "weight": 3.020408163265307
          },
          {
            "source": 140,
            "target": 152,
            "weight": 1.0
          },
          {
            "source": 140,
            "target": 210,
            "weight": 1.0
          },
          {
            "source": 140,
            "target": 194,
            "weight": 1.0
          },
          {
            "source": 141,
            "target": 207,
            "weight": 20.714285714285722
          },
          {
            "source": 141,
            "target": 197,
            "weight": 20.714285714285722
          },
          {
            "source": 142,
            "target": 160,
            "weight": 8.53061224489796
          },
          {
            "source": 143,
            "target": 163,
            "weight": 15.724489795918373
          },
          {
            "source": 143,
            "target": 207,
            "weight": 15.724489795918373
          },
          {
            "source": 143,
            "target": 197,
            "weight": 15.724489795918373
          },
          {
            "source": 144,
            "target": 201,
            "weight": 32.193877551020414
          },
          {
            "source": 144,
            "target": 184,
            "weight": 15.724489795918373
          },
          {
            "source": 144,
            "target": 209,
            "weight": 15.724489795918373
          },
          {
            "source": 144,
            "target": 191,
            "weight": 15.724489795918373
          },
          {
            "source": 144,
            "target": 196,
            "weight": 32.193877551020414
          },
          {
            "source": 144,
            "target": 177,
            "weight": 15.724489795918373
          },
          {
            "source": 145,
            "target": 214,
            "weight": 25.489795918367356
          },
          {
            "source": 146,
            "target": 196,
            "weight": 15.724489795918373
          },
          {
            "source": 146,
            "target": 169,
            "weight": 15.724489795918373
          },
          {
            "source": 147,
            "target": 204,
            "weight": 3.020408163265307
          },
          {
            "source": 147,
            "target": 162,
            "weight": 3.020408163265307
          },
          {
            "source": 147,
            "target": 176,
            "weight": 3.020408163265307
          },
          {
            "source": 148,
            "target": 157,
            "weight": 8.53061224489796
          },
          {
            "source": 148,
            "target": 166,
            "weight": 8.53061224489796
          },
          {
            "source": 148,
            "target": 178,
            "weight": 8.53061224489796
          },
          {
            "source": 148,
            "target": 173,
            "weight": 8.53061224489796
          },
          {
            "source": 149,
            "target": 199,
            "weight": 36.142857142857146
          },
          {
            "source": 150,
            "target": 196,
            "weight": 20.714285714285722
          },
          {
            "source": 151,
            "target": 207,
            "weight": 25.489795918367356
          },
          {
            "source": 151,
            "target": 197,
            "weight": 25.489795918367356
          },
          {
            "source": 152,
            "target": 210,
            "weight": 1.0
          },
          {
            "source": 152,
            "target": 194,
            "weight": 1.0
          },
          {
            "source": 153,
            "target": 207,
            "weight": 29.9591836734694
          },
          {
            "source": 154,
            "target": 198,
            "weight": 26.530612244897974
          },
          {
            "source": 155,
            "target": 206,
            "weight": 20.714285714285722
          },
          {
            "source": 155,
            "target": 211,
            "weight": 20.714285714285722
          },
          {
            "source": 156,
            "target": 196,
            "weight": 25.489795918367356
          },
          {
            "source": 157,
            "target": 178,
            "weight": 8.53061224489796
          },
          {
            "source": 157,
            "target": 166,
            "weight": 8.53061224489796
          },
          {
            "source": 157,
            "target": 173,
            "weight": 8.53061224489796
          },
          {
            "source": 158,
            "target": 197,
            "weight": 20.714285714285722
          },
          {
            "source": 158,
            "target": 207,
            "weight": 20.714285714285722
          },
          {
            "source": 159,
            "target": 179,
            "weight": 15.724489795918373
          },
          {
            "source": 159,
            "target": 207,
            "weight": 39.234693877551045
          },
          {
            "source": 159,
            "target": 197,
            "weight": 39.540816326530624
          },
          {
            "source": 159,
            "target": 168,
            "weight": 32.193877551020414
          },
          {
            "source": 161,
            "target": 196,
            "weight": 20.714285714285722
          },
          {
            "source": 162,
            "target": 204,
            "weight": 3.020408163265307
          },
          {
            "source": 162,
            "target": 176,
            "weight": 3.020408163265307
          },
          {
            "source": 163,
            "target": 207,
            "weight": 15.724489795918373
          },
          {
            "source": 163,
            "target": 197,
            "weight": 15.724489795918373
          },
          {
            "source": 164,
            "target": 185,
            "weight": 21.785714285714295
          },
          {
            "source": 164,
            "target": 191,
            "weight": 30.57142857142858
          },
          {
            "source": 164,
            "target": 174,
            "weight": 32.193877551020414
          },
          {
            "source": 164,
            "target": 196,
            "weight": 36.602040816326536
          },
          {
            "source": 164,
            "target": 171,
            "weight": 25.489795918367356
          },
          {
            "source": 166,
            "target": 178,
            "weight": 8.53061224489796
          },
          {
            "source": 166,
            "target": 173,
            "weight": 8.53061224489796
          },
          {
            "source": 167,
            "target": 214,
            "weight": 15.724489795918373
          },
          {
            "source": 167,
            "target": 196,
            "weight": 15.724489795918373
          },
          {
            "source": 167,
            "target": 198,
            "weight": 15.724489795918373
          },
          {
            "source": 168,
            "target": 197,
            "weight": 32.193877551020414
          },
          {
            "source": 169,
            "target": 196,
            "weight": 15.724489795918373
          },
          {
            "source": 170,
            "target": 191,
            "weight": 35.438775510204096
          },
          {
            "source": 171,
            "target": 191,
            "weight": 25.489795918367356
          },
          {
            "source": 172,
            "target": 200,
            "weight": 21.785714285714295
          },
          {
            "source": 172,
            "target": 182,
            "weight": 20.714285714285722
          },
          {
            "source": 172,
            "target": 186,
            "weight": 21.785714285714295
          },
          {
            "source": 172,
            "target": 196,
            "weight": 32.22448979591837
          },
          {
            "source": 173,
            "target": 178,
            "weight": 8.53061224489796
          },
          {
            "source": 174,
            "target": 196,
            "weight": 32.193877551020414
          },
          {
            "source": 175,
            "target": 181,
            "weight": 29.9591836734694
          },
          {
            "source": 175,
            "target": 198,
            "weight": 29.9591836734694
          },
          {
            "source": 176,
            "target": 204,
            "weight": 3.020408163265307
          },
          {
            "source": 177,
            "target": 184,
            "weight": 15.724489795918373
          },
          {
            "source": 177,
            "target": 209,
            "weight": 15.724489795918373
          },
          {
            "source": 177,
            "target": 191,
            "weight": 15.724489795918373
          },
          {
            "source": 179,
            "target": 197,
            "weight": 15.724489795918373
          },
          {
            "source": 181,
            "target": 198,
            "weight": 29.9591836734694
          },
          {
            "source": 182,
            "target": 214,
            "weight": 11.102040816326532
          },
          {
            "source": 182,
            "target": 196,
            "weight": 11.102040816326532
          },
          {
            "source": 183,
            "target": 196,
            "weight": 32.193877551020414
          },
          {
            "source": 184,
            "target": 209,
            "weight": 15.724489795918373
          },
          {
            "source": 184,
            "target": 191,
            "weight": 15.724489795918373
          },
          {
            "source": 185,
            "target": 191,
            "weight": 21.785714285714295
          },
          {
            "source": 185,
            "target": 196,
            "weight": 21.785714285714295
          },
          {
            "source": 186,
            "target": 200,
            "weight": 21.785714285714295
          },
          {
            "source": 186,
            "target": 196,
            "weight": 21.785714285714295
          },
          {
            "source": 187,
            "target": 207,
            "weight": 33.142857142857146
          },
          {
            "source": 187,
            "target": 189,
            "weight": 29.9591836734694
          },
          {
            "source": 187,
            "target": 190,
            "weight": 20.714285714285722
          },
          {
            "source": 188,
            "target": 212,
            "weight": 15.724489795918373
          },
          {
            "source": 188,
            "target": 195,
            "weight": 27.6326530612245
          },
          {
            "source": 189,
            "target": 190,
            "weight": 20.714285714285722
          },
          {
            "source": 189,
            "target": 207,
            "weight": 29.9591836734694
          },
          {
            "source": 190,
            "target": 207,
            "weight": 35.438775510204096
          },
          {
            "source": 191,
            "target": 209,
            "weight": 15.724489795918373
          },
          {
            "source": 191,
            "target": 196,
            "weight": 21.785714285714295
          },
          {
            "source": 192,
            "target": 203,
            "weight": 1.0
          },
          {
            "source": 192,
            "target": 208,
            "weight": 1.0
          },
          {
            "source": 194,
            "target": 210,
            "weight": 1.0
          },
          {
            "source": 195,
            "target": 212,
            "weight": 15.724489795918373
          },
          {
            "source": 196,
            "target": 200,
            "weight": 21.785714285714295
          },
          {
            "source": 196,
            "target": 201,
            "weight": 37.153061224489804
          },
          {
            "source": 196,
            "target": 202,
            "weight": 33.051020408163275
          },
          {
            "source": 196,
            "target": 213,
            "weight": 25.489795918367356
          },
          {
            "source": 196,
            "target": 214,
            "weight": 36.29591836734695
          },
          {
            "source": 196,
            "target": 198,
            "weight": 32.59183673469388
          },
          {
            "source": 197,
            "target": 205,
            "weight": 25.489795918367356
          },
          {
            "source": 197,
            "target": 207,
            "weight": 39.72448979591838
          },
          {
            "source": 198,
            "target": 214,
            "weight": 15.724489795918373
          },
          {
            "source": 203,
            "target": 208,
            "weight": 1.0
          },
          {
            "source": 205,
            "target": 207,
            "weight": 25.489795918367356
          },
          {
            "source": 206,
            "target": 211,
            "weight": 20.714285714285722
          }
        ],
        "multigraph": false,
        "nodes": [
          {
            "delete": false,
            "group": 1,
            "id": 1,
            "nodeName": "Pasian, F",
            "nodeWeight": 5.0893462403817304
          },
          {
            "delete": false,
            "group": 2,
            "id": 2,
            "nodeName": "Rey Bacaicoa, V",
            "nodeWeight": 5.436803841866236
          },
          {
            "delete": false,
            "group": 3,
            "id": 3,
            "nodeName": "Carter, B",
            "nodeWeight": 5.8259563555288825
          },
          {
            "delete": false,
            "group": 1,
            "id": 5,
            "nodeName": "da Costa, L",
            "nodeWeight": 5.7770415451380766
          },
          {
            "delete": false,
            "group": 2,
            "id": 7,
            "nodeName": "Good, J",
            "nodeWeight": 5.0893462403817304
          },
          {
            "delete": false,
            "group": 2,
            "id": 8,
            "nodeName": "Powell, A",
            "nodeWeight": 5.71476992305384
          },
          {
            "delete": false,
            "group": 5,
            "id": 9,
            "nodeName": "Gioia, I",
            "nodeWeight": 5.297820801272433
          },
          {
            "delete": false,
            "group": 2,
            "id": 10,
            "nodeName": "Nousek, J",
            "nodeWeight": 5.0893462403817304
          },
          {
            "delete": false,
            "group": 6,
            "id": 11,
            "nodeName": "Li, W",
            "nodeWeight": 5.0
          },
          {
            "delete": false,
            "group": 5,
            "id": 12,
            "nodeName": "de Lapparent, V",
            "nodeWeight": 6.786924807634601
          },
          {
            "delete": false,
            "group": 5,
            "id": 13,
            "nodeName": "Wegner, G",
            "nodeWeight": 6.822001479784465
          },
          {
            "delete": false,
            "group": 1,
            "id": 14,
            "nodeName": "Smith, C",
            "nodeWeight": 5.0
          },
          {
            "delete": false,
            "group": 5,
            "id": 16,
            "nodeName": "Bothun, G",
            "nodeWeight": 5.2144309769161525
          },
          {
            "delete": false,
            "group": 2,
            "id": 19,
            "nodeName": "Luker, J",
            "nodeWeight": 6.224374405231115
          },
          {
            "delete": false,
            "group": 5,
            "id": 20,
            "nodeName": "Roll, J",
            "nodeWeight": 5.873828291733414
          },
          {
            "delete": false,
            "group": 2,
            "id": 21,
            "nodeName": "Coletti, D",
            "nodeWeight": 5.436803841866236
          },
          {
            "delete": false,
            "group": 9,
            "id": 22,
            "nodeName": "Ildefonse, B",
            "nodeWeight": 5.032489541956993
          },
          {
            "delete": false,
            "group": 9,
            "id": 23,
            "nodeName": "France, L",
            "nodeWeight": 5.032489541956993
          },
          {
            "delete": false,
            "group": 9,
            "id": 24,
            "nodeName": "Python, M",
            "nodeWeight": 5.032489541956993
          },
          {
            "delete": false,
            "group": 5,
            "id": 26,
            "nodeName": "Odewahn, S",
            "nodeWeight": 5.297820801272433
          },
          {
            "delete": false,
            "group": 11,
            "id": 28,
            "nodeName": "Wilmer, H",
            "nodeWeight": 5.019854720084829
          },
          {
            "delete": false,
            "group": 2,
            "id": 29,
            "nodeName": "Green, D",
            "nodeWeight": 5.0893462403817304
          },
          {
            "delete": false,
            "group": 6,
            "id": 30,
            "nodeName": "Olivera, A",
            "nodeWeight": 5.066182400282763
          },
          {
            "delete": false,
            "group": 2,
            "id": 32,
            "nodeName": "Schulman, E",
            "nodeWeight": 5.71476992305384
          },
          {
            "delete": false,
            "group": 5,
            "id": 33,
            "nodeName": "Mink, D",
            "nodeWeight": 11.875248349374363
          },
          {
            "delete": false,
            "group": 9,
            "id": 34,
            "nodeName": "Abily, B",
            "nodeWeight": 5.032489541956993
          },
          {
            "delete": false,
            "group": 11,
            "id": 35,
            "nodeName": "Muhler, M",
            "nodeWeight": 5.019854720084829
          },
          {
            "delete": false,
            "group": 6,
            "id": 36,
            "nodeName": "Dahl, A",
            "nodeWeight": 5.0
          },
          {
            "delete": false,
            "group": 6,
            "id": 37,
            "nodeName": "Broach, J",
            "nodeWeight": 5.0893462403817304
          },
          {
            "delete": false,
            "group": 5,
            "id": 38,
            "nodeName": "Beers, T",
            "nodeWeight": 7.6565615473501065
          },
          {
            "delete": false,
            "group": 3,
            "id": 39,
            "nodeName": "Kewley, L",
            "nodeWeight": 5.492397058103757
          },
          {
            "delete": false,
            "group": 5,
            "id": 40,
            "nodeName": "Prosser, C",
            "nodeWeight": 5.158837760678631
          },
          {
            "delete": false,
            "group": 2,
            "id": 41,
            "nodeName": "Grant, C",
            "nodeWeight": 44.42419402443904
          },
          {
            "delete": false,
            "group": 2,
            "id": 42,
            "nodeName": "Watson, J",
            "nodeWeight": 5.575786882460038
          },
          {
            "delete": false,
            "group": 2,
            "id": 43,
            "nodeName": "Murray, S",
            "nodeWeight": 44.859674218299624
          },
          {
            "delete": false,
            "group": 2,
            "id": 44,
            "nodeName": "Egret, D",
            "nodeWeight": 5.066182400282763
          },
          {
            "delete": false,
            "group": 2,
            "id": 45,
            "nodeName": "Mussio, P",
            "nodeWeight": 5.853752963647643
          },
          {
            "delete": false,
            "group": 12,
            "id": 46,
            "nodeName": "Gui-ru, L",
            "nodeWeight": 5.032489541956993
          },
          {
            "delete": false,
            "group": 12,
            "id": 47,
            "nodeName": "Mendenhall, M",
            "nodeWeight": 5.032489541956993
          },
          {
            "delete": false,
            "group": 2,
            "id": 48,
            "nodeName": "Warner, S",
            "nodeWeight": 5.04765132820359
          },
          {
            "delete": false,
            "group": 5,
            "id": 49,
            "nodeName": "Moran, S",
            "nodeWeight": 5.381210625628715
          },
          {
            "delete": false,
            "group": 2,
            "id": 50,
            "nodeName": "Barbato, L",
            "nodeWeight": 5.297820801272433
          },
          {
            "delete": false,
            "group": 2,
            "id": 51,
            "nodeName": "Mortellaro, J",
            "nodeWeight": 5.0893462403817304
          },
          {
            "delete": false,
            "group": 9,
            "id": 52,
            "nodeName": "Oizumi, R",
            "nodeWeight": 5.032489541956993
          },
          {
            "delete": false,
            "group": 6,
            "id": 53,
            "nodeName": "Nickels, J",
            "nodeWeight": 5.0893462403817304
          },
          {
            "delete": false,
            "group": 3,
            "id": 55,
            "nodeName": "Miyazaki, S",
            "nodeWeight": 5.367312321569335
          },
          {
            "delete": false,
            "group": 3,
            "id": 58,
            "nodeName": "Wittman, D",
            "nodeWeight": 5.0893462403817304
          },
          {
            "delete": false,
            "group": 1,
            "id": 59,
            "nodeName": "Szalay, A",
            "nodeWeight": 6.29849869354781
          },
          {
            "delete": false,
            "group": 3,
            "id": 60,
            "nodeName": "Mahdavi, A",
            "nodeWeight": 5.528653503476053
          },
          {
            "delete": false,
            "group": 2,
            "id": 62,
            "nodeName": "Delfini, D",
            "nodeWeight": 5.297820801272433
          },
          {
            "delete": false,
            "group": 2,
            "id": 63,
            "nodeName": "Rey Bakaikoa, V",
            "nodeWeight": 5.992736004241445
          },
          {
            "delete": false,
            "group": 5,
            "id": 64,
            "nodeName": "Huchra, J",
            "nodeWeight": 9.091456151298846
          },
          {
            "delete": false,
            "group": 11,
            "id": 66,
            "nodeName": "Rabe, S",
            "nodeWeight": 5.019854720084829
          },
          {
            "delete": false,
            "group": 11,
            "id": 67,
            "nodeName": "Driess, M",
            "nodeWeight": 5.019854720084829
          },
          {
            "delete": false,
            "group": 5,
            "id": 68,
            "nodeName": "Ringwald, F",
            "nodeWeight": 5.2144309769161525
          },
          {
            "delete": false,
            "group": 3,
            "id": 70,
            "nodeName": "Diaferio, A",
            "nodeWeight": 8.780856653371027
          },
          {
            "delete": false,
            "group": 3,
            "id": 71,
            "nodeName": "Torres, G",
            "nodeWeight": 5.119128320508973
          },
          {
            "delete": false,
            "group": 6,
            "id": 72,
            "nodeName": "Tu, Z",
            "nodeWeight": 5.0893462403817304
          },
          {
            "delete": false,
            "group": 2,
            "id": 73,
            "nodeName": "Stern Grant, C",
            "nodeWeight": 5.2144309769161525
          },
          {
            "delete": false,
            "group": 3,
            "id": 74,
            "nodeName": "Utsumi, Y",
            "nodeWeight": 5.367312321569335
          },
          {
            "delete": false,
            "group": 5,
            "id": 75,
            "nodeName": "Stauffer, J",
            "nodeWeight": 5.297820801272433
          },
          {
            "delete": false,
            "group": 2,
            "id": 76,
            "nodeName": "Di Milia, G",
            "nodeWeight": 6.372622981864504
          },
          {
            "delete": false,
            "group": 5,
            "id": 79,
            "nodeName": "Geller, M",
            "nodeWeight": 29.321507178948714
          },
          {
            "delete": false,
            "group": 5,
            "id": 80,
            "nodeName": "Berlind, P",
            "nodeWeight": 5.529459202262104
          },
          {
            "delete": false,
            "group": 6,
            "id": 81,
            "nodeName": "Mandala, S",
            "nodeWeight": 5.39378528168244
          },
          {
            "delete": false,
            "group": 2,
            "id": 82,
            "nodeName": "Henneken, E",
            "nodeWeight": 21.096221572770787
          },
          {
            "delete": false,
            "group": 6,
            "id": 83,
            "nodeName": "Galve-Roperh, I",
            "nodeWeight": 5.066182400282763
          },
          {
            "delete": false,
            "group": 1,
            "id": 84,
            "nodeName": "Calderon, J",
            "nodeWeight": 5.032489541956993
          },
          {
            "delete": false,
            "group": 12,
            "id": 85,
            "nodeName": "Shapiro, M",
            "nodeWeight": 5.032489541956993
          },
          {
            "delete": false,
            "group": 2,
            "id": 86,
            "nodeName": "Lesteven, S",
            "nodeWeight": 5.066182400282763
          },
          {
            "delete": false,
            "group": 3,
            "id": 88,
            "nodeName": "Hamana, T",
            "nodeWeight": 5.0893462403817304
          },
          {
            "delete": false,
            "group": 5,
            "id": 89,
            "nodeName": "McMahan, R",
            "nodeWeight": 5.297820801272433
          },
          {
            "delete": false,
            "group": 6,
            "id": 90,
            "nodeName": "Foor, F",
            "nodeWeight": 5.0
          },
          {
            "delete": false,
            "group": 2,
            "id": 91,
            "nodeName": "Brugel, E",
            "nodeWeight": 5.0893462403817304
          },
          {
            "delete": false,
            "group": 5,
            "id": 93,
            "nodeName": "Marzke, R",
            "nodeWeight": 5.611585544431168
          },
          {
            "delete": false,
            "group": 2,
            "id": 94,
            "nodeName": "Chyla, R",
            "nodeWeight": 5.274656961173466
          },
          {
            "delete": false,
            "group": 2,
            "id": 95,
            "nodeName": "Bohlen, E",
            "nodeWeight": 10.484535511432568
          },
          {
            "delete": false,
            "group": 5,
            "id": 97,
            "nodeName": "Tokarz, S",
            "nodeWeight": 6.204740293147228
          },
          {
            "delete": false,
            "group": 5,
            "id": 98,
            "nodeName": "Schild, R",
            "nodeWeight": 5.8166908194892954
          },
          {
            "delete": false,
            "group": 9,
            "id": 100,
            "nodeName": "Godard, M",
            "nodeWeight": 5.032489541956993
          },
          {
            "delete": false,
            "group": 3,
            "id": 101,
            "nodeName": "CLASH Team",
            "nodeWeight": 5.066182400282763
          },
          {
            "delete": false,
            "group": 2,
            "id": 102,
            "nodeName": "Rosvall, M",
            "nodeWeight": 5.436803841866236
          },
          {
            "delete": false,
            "group": 12,
            "id": 103,
            "nodeName": "Epstein, S",
            "nodeWeight": 5.032489541956993
          },
          {
            "delete": false,
            "group": 5,
            "id": 104,
            "nodeName": "Schneps, M",
            "nodeWeight": 5.436803841866236
          },
          {
            "delete": false,
            "group": 2,
            "id": 105,
            "nodeName": "French, J",
            "nodeWeight": 5.71476992305384
          },
          {
            "delete": false,
            "group": 5,
            "id": 106,
            "nodeName": "Allende Prieto, C",
            "nodeWeight": 6.07215488458076
          },
          {
            "delete": false,
            "group": 3,
            "id": 107,
            "nodeName": "Ramella, M",
            "nodeWeight": 6.027211018206921
          },
          {
            "delete": false,
            "group": 6,
            "id": 108,
            "nodeName": "Menzeleev, R",
            "nodeWeight": 5.0893462403817304
          },
          {
            "delete": false,
            "group": 5,
            "id": 110,
            "nodeName": "Chilingarian, I",
            "nodeWeight": 5.619467266646661
          },
          {
            "delete": false,
            "group": 5,
            "id": 111,
            "nodeName": "Zabludoff, A",
            "nodeWeight": 5.158837760678631
          },
          {
            "delete": false,
            "group": 1,
            "id": 112,
            "nodeName": "Lipari, S",
            "nodeWeight": 5.0
          },
          {
            "delete": false,
            "group": 5,
            "id": 113,
            "nodeName": "Wilhelm, R",
            "nodeWeight": 6.07215488458076
          },
          {
            "delete": false,
            "group": 1,
            "id": 114,
            "nodeName": "Latham, D",
            "nodeWeight": 5.151617862465966
          },
          {
            "delete": false,
            "group": 6,
            "id": 115,
            "nodeName": "Baginsky, W",
            "nodeWeight": 5.0
          },
          {
            "delete": false,
            "group": 2,
            "id": 116,
            "nodeName": "Demleitner, M",
            "nodeWeight": 9.086763217460614
          },
          {
            "delete": false,
            "group": 3,
            "id": 117,
            "nodeName": "Starikova, S",
            "nodeWeight": 5.066182400282763
          },
          {
            "delete": false,
            "group": 6,
            "id": 118,
            "nodeName": "Douglas, C",
            "nodeWeight": 5.0
          },
          {
            "delete": false,
            "group": 1,
            "id": 119,
            "nodeName": "Willmer, C",
            "nodeWeight": 6.56924487652275
          },
          {
            "delete": false,
            "group": 1,
            "id": 120,
            "nodeName": "Alonso, V",
            "nodeWeight": 5.297820801272433
          },
          {
            "delete": false,
            "group": 2,
            "id": 121,
            "nodeName": "Ginsparg, P",
            "nodeWeight": 5.04765132820359
          },
          {
            "delete": false,
            "group": 3,
            "id": 122,
            "nodeName": "Forman, W",
            "nodeWeight": 5.066182400282763
          },
          {
            "delete": false,
            "group": 3,
            "id": 123,
            "nodeName": "Nonino, M",
            "nodeWeight": 5.102438845655059
          },
          {
            "delete": false,
            "group": 5,
            "id": 124,
            "nodeName": "Matthews, A",
            "nodeWeight": 5.04765132820359
          },
          {
            "delete": false,
            "group": 1,
            "id": 125,
            "nodeName": "Colless, M",
            "nodeWeight": 5.0893462403817304
          },
          {
            "delete": false,
            "group": 1,
            "id": 126,
            "nodeName": "Rite, C",
            "nodeWeight": 5.0
          },
          {
            "delete": false,
            "group": 6,
            "id": 127,
            "nodeName": "El-Sherbeini, M",
            "nodeWeight": 5.0
          },
          {
            "delete": false,
            "group": 2,
            "id": 129,
            "nodeName": "Farris, A",
            "nodeWeight": 5.0893462403817304
          },
          {
            "delete": false,
            "group": 11,
            "id": 130,
            "nodeName": "Hinrichsen, O",
            "nodeWeight": 5.019854720084829
          },
          {
            "delete": false,
            "group": 5,
            "id": 131,
            "nodeName": "Horine, E",
            "nodeWeight": 5.119128320508973
          },
          {
            "delete": false,
            "group": 3,
            "id": 134,
            "nodeName": "Woods, D",
            "nodeWeight": 5.220608000942543
          },
          {
            "delete": false,
            "group": 2,
            "id": 135,
            "nodeName": "Karakashian, T",
            "nodeWeight": 5.964939396122684
          },
          {
            "delete": false,
            "group": 5,
            "id": 136,
            "nodeName": "Garnavich, P",
            "nodeWeight": 5.158837760678631
          },
          {
            "delete": false,
            "group": 2,
            "id": 137,
            "nodeName": "Martimbeau, N",
            "nodeWeight": 5.0893462403817304
          },
          {
            "delete": false,
            "group": 1,
            "id": 141,
            "nodeName": "Pellegrini, P",
            "nodeWeight": 5.568566984247373
          },
          {
            "delete": false,
            "group": 12,
            "id": 142,
            "nodeName": "Jones, V",
            "nodeWeight": 5.032489541956993
          },
          {
            "delete": false,
            "group": 2,
            "id": 143,
            "nodeName": "McMahon, S",
            "nodeWeight": 5.0893462403817304
          },
          {
            "delete": false,
            "group": 6,
            "id": 145,
            "nodeName": "Peterson, C",
            "nodeWeight": 5.066182400282763
          },
          {
            "delete": false,
            "group": 9,
            "id": 146,
            "nodeName": "Abe, N",
            "nodeWeight": 5.032489541956993
          },
          {
            "delete": false,
            "group": 5,
            "id": 147,
            "nodeName": "Bromley, B",
            "nodeWeight": 5.8259563555288825
          },
          {
            "delete": false,
            "group": 3,
            "id": 148,
            "nodeName": "Caldwell, N",
            "nodeWeight": 5.63755712272395
          },
          {
            "delete": false,
            "group": 5,
            "id": 149,
            "nodeName": "McLeod, B",
            "nodeWeight": 5.109421568467502
          },
          {
            "delete": false,
            "group": 1,
            "id": 151,
            "nodeName": "Rowan-Robinson, M",
            "nodeWeight": 5.0893462403817304
          },
          {
            "delete": false,
            "group": 5,
            "id": 152,
            "nodeName": "Kannappan, S",
            "nodeWeight": 5.297820801272433
          },
          {
            "delete": false,
            "group": 5,
            "id": 153,
            "nodeName": "Maccacaro, T",
            "nodeWeight": 5.297820801272433
          },
          {
            "delete": false,
            "group": 5,
            "id": 155,
            "nodeName": "Kenyon, S",
            "nodeWeight": 11.158934170313925
          },
          {
            "delete": false,
            "group": 3,
            "id": 156,
            "nodeName": "Dell'Antonio, I",
            "nodeWeight": 8.658121871629254
          },
          {
            "delete": false,
            "group": 2,
            "id": 157,
            "nodeName": "Holachek, A",
            "nodeWeight": 5.066182400282763
          },
          {
            "delete": false,
            "group": 6,
            "id": 158,
            "nodeName": "Frommer, B",
            "nodeWeight": 5.0
          },
          {
            "delete": false,
            "group": 6,
            "id": 159,
            "nodeName": "Nielsen, J",
            "nodeWeight": 5.0
          },
          {
            "delete": false,
            "group": 3,
            "id": 161,
            "nodeName": "Westra, E",
            "nodeWeight": 5.8259563555288825
          },
          {
            "delete": false,
            "group": 11,
            "id": 162,
            "nodeName": "Gruenert, W",
            "nodeWeight": 5.019854720084829
          },
          {
            "delete": false,
            "group": 6,
            "id": 163,
            "nodeName": "Bergstrom, J",
            "nodeWeight": 5.121775616520284
          },
          {
            "delete": false,
            "group": 2,
            "id": 164,
            "nodeName": "Hughes, J",
            "nodeWeight": 5.0893462403817304
          },
          {
            "delete": false,
            "group": 2,
            "id": 165,
            "nodeName": "Rey Bacaico, V",
            "nodeWeight": 5.158837760678631
          },
          {
            "delete": false,
            "group": 5,
            "id": 166,
            "nodeName": "Falco, E",
            "nodeWeight": 8.702905295820589
          },
          {
            "delete": false,
            "group": 5,
            "id": 167,
            "nodeName": "Peters, J",
            "nodeWeight": 5.489749762092446
          },
          {
            "delete": false,
            "group": 3,
            "id": 168,
            "nodeName": "Boehringer, H",
            "nodeWeight": 5.2144309769161525
          },
          {
            "delete": false,
            "group": 9,
            "id": 169,
            "nodeName": "Payot, B",
            "nodeWeight": 5.032489541956993
          },
          {
            "delete": false,
            "group": 2,
            "id": 170,
            "nodeName": "Marsden, B",
            "nodeWeight": 5.0893462403817304
          },
          {
            "delete": false,
            "group": 11,
            "id": 171,
            "nodeName": "Woell, C",
            "nodeWeight": 5.019854720084829
          },
          {
            "delete": false,
            "group": 6,
            "id": 172,
            "nodeName": "Marrinan, J",
            "nodeWeight": 5.0
          },
          {
            "delete": false,
            "group": 5,
            "id": 173,
            "nodeName": "Elwell, B",
            "nodeWeight": 5.459967681965203
          },
          {
            "delete": false,
            "group": 9,
            "id": 174,
            "nodeName": "Alt, J",
            "nodeWeight": 5.032489541956993
          },
          {
            "delete": false,
            "group": 2,
            "id": 175,
            "nodeName": "Wenger, M",
            "nodeWeight": 5.066182400282763
          },
          {
            "delete": false,
            "group": 3,
            "id": 178,
            "nodeName": "Postman, M",
            "nodeWeight": 5.658371008030269
          },
          {
            "delete": false,
            "group": 5,
            "id": 179,
            "nodeName": "Barcikowski, E",
            "nodeWeight": 5.158837760678631
          },
          {
            "delete": false,
            "group": 3,
            "id": 180,
            "nodeName": "Vikhlinin, A",
            "nodeWeight": 5.066182400282763
          },
          {
            "delete": false,
            "group": 11,
            "id": 181,
            "nodeName": "Klementiev, K",
            "nodeWeight": 5.019854720084829
          },
          {
            "delete": false,
            "group": 12,
            "id": 182,
            "nodeName": "Melvin, J",
            "nodeWeight": 5.032489541956993
          },
          {
            "delete": false,
            "group": 1,
            "id": 183,
            "nodeName": "Ellman, N",
            "nodeWeight": 5.881549571766404
          },
          {
            "delete": false,
            "group": 3,
            "id": 184,
            "nodeName": "Tyson, J",
            "nodeWeight": 5.0893462403817304
          },
          {
            "delete": false,
            "group": 2,
            "id": 186,
            "nodeName": "ReyBacaicoa, V",
            "nodeWeight": 5.158837760678631
          },
          {
            "delete": false,
            "group": 6,
            "id": 187,
            "nodeName": "Clemas, J",
            "nodeWeight": 5.0
          },
          {
            "delete": false,
            "group": 2,
            "id": 188,
            "nodeName": "Mason, B",
            "nodeWeight": 5.297820801272433
          },
          {
            "delete": false,
            "group": 5,
            "id": 189,
            "nodeName": "Kleyna, J",
            "nodeWeight": 6.465278342260373
          },
          {
            "delete": false,
            "group": 1,
            "id": 190,
            "nodeName": "Renzini, A",
            "nodeWeight": 5.0893462403817304
          },
          {
            "delete": false,
            "group": 3,
            "id": 192,
            "nodeName": "Freedman Woods, D",
            "nodeWeight": 5.158837760678631
          },
          {
            "delete": false,
            "group": 12,
            "id": 193,
            "nodeName": "Masdea, D",
            "nodeWeight": 5.032489541956993
          },
          {
            "delete": false,
            "group": 2,
            "id": 195,
            "nodeName": "Williams, G",
            "nodeWeight": 5.0893462403817304
          },
          {
            "delete": false,
            "group": 2,
            "id": 196,
            "nodeName": "Thompson, D",
            "nodeWeight": 14.965084010575627
          },
          {
            "delete": false,
            "group": 9,
            "id": 197,
            "nodeName": "Koepke, J",
            "nodeWeight": 5.032489541956993
          },
          {
            "delete": false,
            "group": 3,
            "id": 198,
            "nodeName": "Oguri, M",
            "nodeWeight": 5.0893462403817304
          },
          {
            "delete": false,
            "group": 11,
            "id": 199,
            "nodeName": "Birkner, A",
            "nodeWeight": 5.019854720084829
          },
          {
            "delete": false,
            "group": 2,
            "id": 200,
            "nodeName": "Genova, F",
            "nodeWeight": 5.066182400282763
          },
          {
            "delete": false,
            "group": 3,
            "id": 201,
            "nodeName": "Hwang, H",
            "nodeWeight": 6.286585861496913
          },
          {
            "delete": false,
            "group": 5,
            "id": 202,
            "nodeName": "Ostriker, E",
            "nodeWeight": 5.297820801272433
          },
          {
            "delete": false,
            "group": 12,
            "id": 203,
            "nodeName": "Rice, A",
            "nodeWeight": 5.032489541956993
          },
          {
            "delete": false,
            "group": 5,
            "id": 206,
            "nodeName": "Sakai, S",
            "nodeWeight": 5.066182400282763
          },
          {
            "delete": false,
            "group": 2,
            "id": 209,
            "nodeName": "Bergstrom, C",
            "nodeWeight": 5.436803841866236
          },
          {
            "delete": false,
            "group": 3,
            "id": 211,
            "nodeName": "Jones, C",
            "nodeWeight": 5.066182400282763
          },
          {
            "delete": false,
            "group": 3,
            "id": 212,
            "nodeName": "Jarrett, T",
            "nodeWeight": 5.770363139291361
          },
          {
            "delete": false,
            "group": 3,
            "id": 213,
            "nodeName": "Coe, D",
            "nodeWeight": 5.1950942060509275
          },
          {
            "delete": false,
            "group": 3,
            "id": 214,
            "nodeName": "Wyatt, W",
            "nodeWeight": 5.722932419088714
          },
          {
            "delete": false,
            "group": 12,
            "id": 218,
            "nodeName": "Tombrello, T",
            "nodeWeight": 5.032489541956993
          },
          {
            "delete": false,
            "group": 3,
            "id": 219,
            "nodeName": "Zahid, H",
            "nodeWeight": 5.436803841866236
          },
          {
            "delete": false,
            "group": 5,
            "id": 221,
            "nodeName": "Hamwey, R",
            "nodeWeight": 5.297820801272433
          },
          {
            "delete": false,
            "group": 11,
            "id": 222,
            "nodeName": "Merz, K",
            "nodeWeight": 5.019854720084829
          },
          {
            "delete": false,
            "group": 3,
            "id": 223,
            "nodeName": "Ford, H",
            "nodeWeight": 5.102438845655059
          },
          {
            "delete": false,
            "group": 12,
            "id": 225,
            "nodeName": "Fong-liang, J",
            "nodeWeight": 5.032489541956993
          },
          {
            "delete": false,
            "group": 2,
            "id": 226,
            "nodeName": "Thiell, B",
            "nodeWeight": 5.066182400282763
          },
          {
            "delete": false,
            "group": 5,
            "id": 228,
            "nodeName": "Barton, E",
            "nodeWeight": 5.297820801272433
          },
          {
            "delete": false,
            "group": 5,
            "id": 230,
            "nodeName": "Boley, F",
            "nodeWeight": 5.297820801272433
          },
          {
            "delete": false,
            "group": 5,
            "id": 231,
            "nodeName": "Conroy, M",
            "nodeWeight": 5.317896129358205
          },
          {
            "delete": false,
            "group": 3,
            "id": 233,
            "nodeName": "Kent, S",
            "nodeWeight": 5.436803841866236
          },
          {
            "delete": false,
            "group": 3,
            "id": 234,
            "nodeName": "Medezinski, E",
            "nodeWeight": 5.102438845655059
          },
          {
            "delete": false,
            "group": 3,
            "id": 235,
            "nodeName": "Del'Antonio, I",
            "nodeWeight": 5.119128320508973
          },
          {
            "delete": false,
            "group": 3,
            "id": 237,
            "nodeName": "Kriss, G",
            "nodeWeight": 5.119128320508973
          },
          {
            "delete": false,
            "group": 2,
            "id": 240,
            "nodeName": "Stoner, J",
            "nodeWeight": 5.5062953621631365
          },
          {
            "delete": false,
            "group": 6,
            "id": 241,
            "nodeName": "Thornton, R",
            "nodeWeight": 5.274656961173466
          },
          {
            "delete": false,
            "group": 2,
            "id": 242,
            "nodeName": "Ossorio, P",
            "nodeWeight": 6.687651207210456
          },
          {
            "delete": false,
            "group": 2,
            "id": 243,
            "nodeName": "Stern, C",
            "nodeWeight": 5.756464835231981
          },
          {
            "delete": false,
            "group": 3,
            "id": 245,
            "nodeName": "Rines, K",
            "nodeWeight": 8.314932555380375
          },
          {
            "delete": false,
            "group": 1,
            "id": 246,
            "nodeName": "Chaves, O",
            "nodeWeight": 5.0
          },
          {
            "delete": false,
            "group": 5,
            "id": 248,
            "nodeName": "Windhorst, R",
            "nodeWeight": 5.297820801272433
          },
          {
            "delete": false,
            "group": 6,
            "id": 249,
            "nodeName": "Mazur, P",
            "nodeWeight": 5.0
          },
          {
            "delete": false,
            "group": 6,
            "id": 251,
            "nodeName": "Spiegel, S",
            "nodeWeight": 5.274656961173466
          },
          {
            "delete": false,
            "group": 3,
            "id": 252,
            "nodeName": "Fabricant, D",
            "nodeWeight": 13.57745968464703
          },
          {
            "delete": false,
            "group": 2,
            "id": 253,
            "nodeName": "Accomazzi, A",
            "nodeWeight": 45.67377790759604
          },
          {
            "delete": false,
            "group": 5,
            "id": 254,
            "nodeName": "Thorstensen, J",
            "nodeWeight": 6.877594696021986
          },
          {
            "delete": false,
            "group": 1,
            "id": 255,
            "nodeName": "Koo, D",
            "nodeWeight": 6.29849869354781
          },
          {
            "delete": false,
            "group": 3,
            "id": 256,
            "nodeName": "Tonry, J",
            "nodeWeight": 5.119128320508973
          },
          {
            "delete": false,
            "group": 3,
            "id": 257,
            "nodeName": "McLean, B",
            "nodeWeight": 6.103922436716487
          },
          {
            "delete": false,
            "group": 5,
            "id": 258,
            "nodeName": "Gorenstein, P",
            "nodeWeight": 5.492397058103757
          },
          {
            "delete": false,
            "group": 1,
            "id": 259,
            "nodeName": "Maia, M",
            "nodeWeight": 5.0
          },
          {
            "delete": false,
            "group": 11,
            "id": 260,
            "nodeName": "Tkachenko, O",
            "nodeWeight": 5.019854720084829
          },
          {
            "delete": false,
            "group": 2,
            "id": 262,
            "nodeName": "Reybacaicoa, V",
            "nodeWeight": 5.158837760678631
          },
          {
            "delete": false,
            "group": 1,
            "id": 265,
            "nodeName": "Vettolani, P",
            "nodeWeight": 5.0893462403817304
          },
          {
            "delete": false,
            "group": 2,
            "id": 266,
            "nodeName": "Eichhorn, G",
            "nodeWeight": 47.07347550775805
          },
          {
            "delete": false,
            "group": 1,
            "id": 267,
            "nodeName": "Fairall, A",
            "nodeWeight": 5.151617862465966
          },
          {
            "delete": false,
            "group": 3,
            "id": 268,
            "nodeName": "Lemze, D",
            "nodeWeight": 5.102438845655059
          },
          {
            "delete": false,
            "group": 6,
            "id": 269,
            "nodeName": "Morin, N",
            "nodeWeight": 5.0
          },
          {
            "delete": false,
            "group": 1,
            "id": 270,
            "nodeName": "Plionis, M",
            "nodeWeight": 5.0893462403817304
          },
          {
            "delete": false,
            "group": 6,
            "id": 271,
            "nodeName": "Poulton, S",
            "nodeWeight": 5.066182400282763
          },
          {
            "delete": false,
            "group": 5,
            "id": 273,
            "nodeName": "Mack, P",
            "nodeWeight": 5.158837760678631
          },
          {
            "delete": false,
            "group": 5,
            "id": 278,
            "nodeName": "Brown, W",
            "nodeWeight": 11.850760861269741
          }
        ]
      },
      "summaryGraph": {
        "directed": false,
        "graph": [],
        "links": [
          {
            "source": 0,
            "target": 0,
            "weight": 1031.5612244897964
          },
          {
            "source": 0,
            "target": 2,
            "weight": 126.5918367346939
          },
          {
            "source": 0,
            "target": 3,
            "weight": 304.36734693877554
          },
          {
            "source": 0,
            "target": 8,
            "weight": 432.00000000000017
          },
          {
            "source": 1,
            "target": 8,
            "weight": 1441.0714285714291
          },
          {
            "source": 1,
            "target": 1,
            "weight": 5894.714285714282
          },
          {
            "source": 1,
            "target": 2,
            "weight": 94.34693877551024
          },
          {
            "source": 1,
            "target": 3,
            "weight": 140.0102040816327
          },
          {
            "source": 2,
            "target": 2,
            "weight": 2765.0918367346953
          },
          {
            "source": 2,
            "target": 3,
            "weight": 1959.4693877551024
          },
          {
            "source": 2,
            "target": 8,
            "weight": 1082.683673469388
          },
          {
            "source": 3,
            "target": 3,
            "weight": 5105.142857142865
          },
          {
            "source": 3,
            "target": 8,
            "weight": 1504.8367346938783
          },
          {
            "source": 4,
            "target": 8,
            "weight": 259.3163265306123
          },
          {
            "source": 4,
            "target": 4,
            "weight": 926.8673469387752
          },
          {
            "source": 5,
            "target": 5,
            "weight": 383.8775510204079
          },
          {
            "source": 5,
            "target": 8,
            "weight": 85.30612244897958
          },
          {
            "source": 6,
            "target": 8,
            "weight": 33.22448979591838
          },
          {
            "source": 6,
            "target": 6,
            "weight": 166.12244897959172
          },
          {
            "source": 7,
            "target": 8,
            "weight": 85.30612244897958
          },
          {
            "source": 7,
            "target": 7,
            "weight": 383.8775510204079
          },
          {
            "source": 8,
            "target": 8,
            "weight": 0
          }
        ],
        "multigraph": false,
        "nodes": [
          {
            "authorCount": 21,
            "id": 1,
            "nodeName": {
              "Koo, D": 6.29849869354781,
              "Szalay, A": 6.29849869354781,
              "Willmer, C": 6.56924487652275
            },
            "size": 112.56302387522196
          },
          {
            "authorCount": 51,
            "id": 2,
            "nodeName": {
              "Accomazzi, A": 45.67377790759604,
              "Eichhorn, G": 47.07347550775805,
              "Grant, C": 44.42419402443904,
              "Murray, S": 44.859674218299624
            },
            "size": 468.8241444713776
          },
          {
            "authorCount": 40,
            "id": 3,
            "nodeName": {
              "Dell'Antonio, I": 8.658121871629254,
              "Diaferio, A": 8.780856653371027,
              "Fabricant, D": 13.57745968464703,
              "Rines, K": 8.314932555380375
            },
            "size": 232.18304594227612
          },
          {
            "authorCount": 48,
            "id": 5,
            "nodeName": {
              "Brown, W": 11.850760861269741,
              "Geller, M": 29.321507178948714,
              "Kenyon, S": 11.158934170313925,
              "Mink, D": 11.875248349374363
            },
            "size": 316.6546987101776
          },
          {
            "authorCount": 24,
            "id": 6,
            "nodeName": {
              "Mandala, S": 5.39378528168244,
              "Spiegel, S": 5.274656961173466,
              "Thornton, R": 5.274656961173466
            },
            "size": 121.68698938320762
          },
          {
            "authorCount": 10,
            "id": 9,
            "nodeName": {
              "Ildefonse, B": 5.032489541956993
            },
            "size": 50.32489541956992
          },
          {
            "authorCount": 11,
            "id": 11,
            "nodeName": {
              "Muhler, M": 5.019854720084829,
              "Wilmer, H": 5.019854720084829
            },
            "size": 55.21840192093312
          },
          {
            "authorCount": 10,
            "id": 12,
            "nodeName": {
              "Gui-ru, L": 5.032489541956993
            },
            "size": 50.32489541956992
          },
          {
            "connector": true,
            "id": "Kurtz, M",
            "nodeName": {
              "Kurtz, M": 150.0
            },
            "size": 150.0
          }
        ]
      }
    };

    testDataEmpty = {"fullGraph": {"nodes": [], "links": []}};


    afterEach(function(){

      $("#test").empty();


    })


    it("renders different views with different types of graphs based on the data it is given", function(){

      var networkWidget = new NetworkWidget({networkType: "author", endpoint : "author-network", helpText : "test"});
      $("#test").append(networkWidget.view.el);

      //this should show a single, simple graph
      networkWidget.processResponse(new JsonResponse(testDataSmall));

      expect($("#test").find("svg").length).to.eql(1);

      expect($("#test").find("text.detail-node").length).to.eql(19)
      expect($("#test").find("line.detail-link").length).to.eql(68)

      //this should show two graphs, a summary and a detail graph
      networkWidget.processResponse(new JsonResponse(testDataLarge));

      expect($("#test").find(".summary-node-group").length).to.eql(9);

      networkWidget.view.model.set("currentGroup", 2);

      expect($("#test").find("svg").length).to.eql(2);

      //this should show just a message;

      networkWidget.processResponse(new JsonResponse(testDataEmpty));

      expect($("#test").find(".author-graph").text()).to.eql("There wasn't enough data returned by your search to form a visualization.")


    })

    it("listens to the graph view for node selection events (adding and removing) and updates the selected nodes list collection", function(){

      var networkWidget = new NetworkWidget({networkType: "author", endpoint : "author-network", helpText : "test"});
      $("#test").append(networkWidget.view.el);

      networkWidget.processResponse(new JsonResponse(testDataSmall));

      //testing view method instead of click

      networkWidget.view.updateSingleName("Bohlen, Ex");

      expect(networkWidget.view.$("ul.dropdown-menu").find("li").length).to.eql(1);



      //the x is from the close button
      expect(networkWidget.view.$("ul.dropdown-menu").find("li").text().trim()).to.eql("Bohlen, Exx");

      networkWidget.view.updateSingleName("Bohlen, Ex");

      expect(networkWidget.view.$("ul.dropdown-menu").find("li").length).to.eql(1);

      expect(networkWidget.view.$("ul.dropdown-menu").find("li").text().trim()).to.eql('Click on a node in the detail view to add it to this list. You can then filter your current search to include only the selected items.');

      networkWidget.view.$("button.update-all").click();

      expect(networkWidget.view.$("ul.dropdown-menu").find("li").length).to.eql(19);

      networkWidget.view.$("button.update-all").click();

      expect(networkWidget.view.$("ul.dropdown-menu").find("li").length).to.eql(1);

      expect(networkWidget.view.$("ul.dropdown-menu").find("li").text().trim()).to.eql('Click on a node in the detail view to add it to this list. You can then filter your current search to include only the selected items.');



    })



    it("communicates with pubsub to get current query info and to request network data", function(){

      var networkWidget = new NetworkWidget({networkType: "author", endpoint : "author-network", helpText : "test"});
      $("#test").append(networkWidget.view.el);

      var minsub = new MinimalPubsub({verbose: false});

      var q =  new ApiQuery({q: 'star'});

      networkWidget.activate(minsub.beehive.getHardenedInstance());

      minsub.publish(minsub.START_SEARCH, q);

      expect(networkWidget.getCurrentQuery().get("q")).to.eql(q.get("q"));

      networkWidget.pubsub.publish = sinon.stub()

      networkWidget.onShow();

      expect (networkWidget.pubsub.publish.calledOnce).to.be.true;

      expect(networkWidget.pubsub.publish.args[0][1].url()).to.eql("author-network?q=star");


    })

    it("communicates a filtered request to pubsub", function(){

      var networkWidget = new NetworkWidget({networkType: "author", endpoint : "author-network", helpText : "test"});
      $("#test").append(networkWidget.view.el);

      var minsub = new MinimalPubsub({verbose: false});

      networkWidget.setCurrentQuery(new ApiQuery({q : "original search"}))

      networkWidget.activate(minsub.beehive.getHardenedInstance());

      networkWidget.pubsub.publish = sinon.stub();

      networkWidget.view.trigger("filterSearch", ["testName1", "testName2"]);

      expect(networkWidget.pubsub.publish.called).to.be.true;

      expect(networkWidget.pubsub.publish.args[0][0]).to.eql("[PubSub]-New-Query");

      expect(networkWidget.pubsub.publish.args[0][1].url()).to.eql( "__fq_fq=AND&__fq_fq=author%3A(%22testName1%22+OR+%22testName2%22)&fq=(author%3A(%22testName1%22+OR+%22testName2%22))&q=original+search");

    })

    it("should show a loading view while it waits for data from pubsub", function(){

      var networkWidget = new NetworkWidget({networkType: "author", endpoint : "author-network", helpText : "test"});
      $("#test").append(networkWidget.view.el);

      var minsub = new MinimalPubsub({verbose: false});

      networkWidget.activate(minsub.beehive.getHardenedInstance());

      networkWidget.pubsub.publish = sinon.stub();

      networkWidget.startWidgetLoad();

      expect(networkWidget.view.$(".s-loading").length).to.eql(1);

      networkWidget.processResponse(new JsonResponse(testDataEmpty))

      expect(networkWidget.view.$(".s-loading").length).to.eql(0);



    })


    it("has a method to completely remove the graphView", function(){
      var networkWidget = new NetworkWidget({networkType: "author", endpoint : "author-network", helpText : "test"});

      $("#test").append(networkWidget.view.el);

      var minsub = new MinimalPubsub({verbose: false});


      networkWidget.processResponse(new JsonResponse(testDataLarge));

      expect(networkWidget.resetWidget).to.be.instanceof(Function);

      networkWidget.resetWidget();


    })

    it("has a help popover that is accessible by hovering over the question mark icon", function(){

      var networkWidget = new NetworkWidget({networkType: "author", endpoint : "author-network", helpText : "test"});

      $("#test").append(networkWidget.view.el);

      var minsub = new MinimalPubsub({verbose: false});


      networkWidget.processResponse(new JsonResponse(testDataLarge));

      //it uses the standard bootstrap popover, you just need to make sure the data-content attribute is correct

      expect(networkWidget.view.$(".icon-help").attr("data-content")).to.eql("test");

      expect($("div.popover").length).to.eql(0);

      networkWidget.view.$(".icon-help").mouseover();

      expect($("div.popover").length).to.eql(1);

    })


    it("should show a detail network for the summary graph node that was clicked", function(){

      var networkWidget = new NetworkWidget({networkType: "author", endpoint : "author-network", helpText : "test"});
      $("#test").append(networkWidget.view.el);

      networkWidget.processResponse(new JsonResponse(testDataLarge));

      $(".summary-node-group:first").click()


      var names = $(".detail-graph-container .detail-node").map(function(){return d3.select(this).text()})


      names = names.sort();


      // now get names from testDataLarge

      var testNames=[];

      _.each(testDataLarge.fullGraph.nodes, function(n){

        if (n.group == 0){
          testNames.push(n.nodeName)
        }

      });

      testNames = testNames.sort();

      _.each(names, function(n, i){
        expect(n).to.eql(testNames[i])
      });


    })

    it("should have an extractGraphData method that extracts the apprpriate sub network, with optional limiting of nodes", function(){

      var networkWidget = new NetworkWidget({networkType: "author", endpoint : "author-network", helpText : "test"});

      $("#test").append(networkWidget.view.el);

      //this should show two graphs, a summary and a detail graph
      networkWidget.processResponse(new JsonResponse(testDataLarge));

      // using group 2, the ads group, as an example

      var detailGraph = networkWidget.view.extractGraphData(2, 5);


      expect(detailGraph.nodes).to.eql([
        {
          "delete": false,
          "group": 2,
          "id": 41,
          "nodeName": "Grant, C",
          "nodeWeight": 44.42419402443904
        },
        {
          "delete": false,
          "group": 2,
          "id": 43,
          "nodeName": "Murray, S",
          "nodeWeight": 44.859674218299624
        },
        {
          "delete": false,
          "group": 2,
          "id": 82,
          "nodeName": "Henneken, E",
          "nodeWeight": 21.096221572770787
        },
        {
          "delete": false,
          "group": 2,
          "id": 253,
          "nodeName": "Accomazzi, A",
          "nodeWeight": 45.67377790759604
        },
        {
          "delete": false,
          "group": 2,
          "id": 266,
          "nodeName": "Eichhorn, G",
          "nodeWeight": 47.07347550775805
        }
      ]);

      expect(detailGraph.links).to.eql( [
        {
          "source": 0,
          "target": 4,
          "weight": 39.78571428571429
        },
        {
          "source": 0,
          "target": 1,
          "weight": 39.81632653061225
        },
        {
          "source": 0,
          "target": 2,
          "weight": 39.60204081632655
        },
        {
          "source": 0,
          "target": 3,
          "weight": 39.87755102040817
        },
        {
          "source": 1,
          "target": 4,
          "weight": 39.75510204081634
        },
        {
          "source": 1,
          "target": 2,
          "weight": 39.57142857142859
        },
        {
          "source": 1,
          "target": 3,
          "weight": 39.84693877551021
        },
        {
          "source": 2,
          "target": 4,
          "weight": 39.38775510204084
        },
        {
          "source": 2,
          "target": 3,
          "weight": 39.63265306122451
        },
        {
          "source": 3,
          "target": 4,
          "weight": 39.72448979591838
        }
      ]);



    })


  })


})