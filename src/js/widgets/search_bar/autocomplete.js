define([

], function(){

  var autoList = [
    {value: "author:\"" , label : "Author", match: "author:\"", end : '"'},

    {value: "author:\"^" , label : "First Author", match: "author:\"",  end : '"'},
    {value: "author:\"^" , label : "First Author", match: "first author",  end : '"'},
    {value: "author:\"^" , label : "First Author", match: "^author",  end : '"'},
    {value: "author:\"^" , label : "First Author", match: "author:\"^",  end : '"'},


    {value: "bibstem:\"" , label : "Publication", match: "bibstem:\"", end : '"'},
    {value: "bibstem:\"" , label : "Publication", match: "publication (bibstem)",  end : '"'},

    {value: "arXiv:" , label : "arXiv ID", match: "arxiv:",  end : ''},
    {value: "doi:" , label : "DOI", match: "doi:",  end : ''},

    {value: "full:(" , label : "Full text search", desc: "title, abstract, and body", match: "full:(",  end : ')'},
    {value: "full:(" , label : "Full text search", desc: "itle, abstract, and body", match: "fulltext",  end : ')'},
    {value: "full:(" , label : "Full text search", desc: "title, abstract, and body", match: "text",  end : ')'},

    {value: "year:" , label : "Year", match: "year",  end : ''},
    {value: "year:" , label : "Year Range", desc: "e.g. 1999-2005", match: "year range",  end : ''},

    {value: "aff:\"" , label : "Affiliation", match: "affiliation",  end : '"'},
    {value: "aff:\"" , label : "Affiliation", match: "aff:",  end : '"'},


    {value: "abs:(" , label : "Abstract", match: "abstract",  end : ')'},
    {value: "abs:(" , label : "Abstract", match: "abs:(",  end : ')'},

    {value: "title:(" , label : "Title", match: "title:(",  end : ')'},

    {value: "citations(" , label : "Citations", desc: "Get papers citing your search result set", match: "citations(",  end : ')'},
    {value: "references(" , label : "References", desc: "Get papers referenced by your search result set", match: "references(", end : ')'},

    {value: "trending(" , label : "Trending", desc: "the list of documents most read by users who read recent papers on the topic being researched",  match: "trending(", end : ')'},
    {value: "instructive(" , label : "Instructive", desc : "review articles citing most relevant papers",  match: "instructive(", end : ')'},
    {value: "useful(" , label : "Useful", desc: "documents frequently cited by the most relevant papers on the topic being researched", match: "useful(", end : ')'},

    {value: "property:refereed" , label : "Limit to refereed", desc : "(property:refereed)", match: "refereed", end : ''},
    {value: "property:refereed" , label : "Limit to refereed", desc : "(property:refereed)", match: "property", end : ''},

    {value: "property:eprint" , label : "Limit to eprints", desc : "(property:eprint)", match: "eprint", end : ''},
    {value: "property:eprint" , label : "Limit to eprints", desc : "(property:eprint)", match: "property", end : ''},

    {value: "property:openaccess" , label : "Limit to open access", desc : "(property:openaccess)", match: "open access", end : ''},
    {value: "property:openaccess" , label : "Limit to open access", desc : "(property:openaccess)", match: "property", end : ''},
    {value: "property:openaccess" , label : "Limit to open access", desc : "(property:openaccess)", match: "openaccess", end : ''},

    {value: "property:catalog" , label : "Limit to astronomical catalogs (e.g. Vizier)", desc : "(property:catalog)",  match: "catalog", end : ''},
    {value: "property:catalog" , label : "Limit to astronomical catalogs (e.g. Vizier)", desc : "(property:catalog)", match: "property", end : ''},

    {value: "property:software" , label : "Limit to software", match: "software", desc : "(property:software)",  end : ''},
    {value: "property:software" , label : "Limit to software", match: "property", desc : "(property:software)",  end : ''}

  ]

  return autoList


})