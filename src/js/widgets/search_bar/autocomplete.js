define([

], function(){

  var autoList = [
    {value: "author:\"\"" , label : "Author", match: "author:\""},

    {value: "author:\"^\"" , label : "First Author", match: "author:\""},
    {value: "author:\"^\"" , label : "First Author", match: "first author"},
    {value: "author:\"^\"" , label : "First Author", match: "^author"},
    {value: "author:\"^\"" , label : "First Author", match: "author:\"^"},

    {value: "bibstem:\"\"" , label : "Publication", match: "bibstem:\""},
    {value: "bibstem:\"\"", label : "Publication", match: "publication (bibstem)"},

    {value: "arXiv:" , label : "arXiv ID", match: "arxiv:"},
    {value: "doi:" , label : "DOI", match: "doi:"},

    {value: "full:()" , label : "Full text search", desc: "title, abstract, and body", match: "full:("},
    {value: "full:()" , label : "Full text search", desc: "itle, abstract, and body", match: "fulltext"},
    {value: "full:()" , label : "Full text search", desc: "title, abstract, and body", match: "text"},

    {value: "year:" , label : "Year", match: "year"},
    {value: "year:" , label : "Year Range", desc: "e.g. 1999-2005", match: "year range"},

    {value: "aff:\"\"" , label : "Affiliation", match: "affiliation"},
    {value: "aff:\"\"" , label : "Affiliation", match: "aff:"},

    {value: "abs:()" , label : "Search abstract + title + keywords", match: "abs:("},

    {value: "abstract:()" , label : "Abstract", match: "abstract:("},

    {value: "title:()" , label : "Title", match: "title:("},

    {value: "citations()" , label : "Citations", desc: "Get papers citing your search result set", match: "citations("},
    {value: "references()" , label : "References", desc: "Get papers referenced by your search result set", match: "references("},


    {value: "trending()" , label : "Trending", desc: "the list of documents most read by users who read recent papers on the topic being researched",  match: "trending("},
    {value: "reviews()" , label : "Review Articles", desc : "review articles citing most relevant papers",  match: "reviews("},

    {value: "useful()" , label : "Useful", desc: "documents frequently cited by the most relevant papers on the topic being researched", match: "useful("},

    {value: "property:refereed" , label : "Limit to refereed", desc : "(property:refereed)", match: "refereed"},
    {value: "property:refereed" , label : "Limit to refereed", desc : "(property:refereed)", match: "property:refereed"},

    {value: "property:notrefereed" , label : "Limit to non-refereed", desc : "(property:notrefereed)", match: "non-refereed" },
    {value: "property:notrefereed" , label : "Limit to non-refereed", desc : "(property:notrefereed)", match: "property:notrefereed"},
    {value: "property:notrefereed" , label : "Limit to non-refereed", desc : "(property:notrefereed)", match: "notrefereed"},

    {value: "property:eprint" , label : "Limit to eprints", desc : "(property:eprint)", match: "eprint"},
    {value: "property:eprint" , label : "Limit to eprints", desc : "(property:eprint)", match: "property:eprint"},

    {value: "property:openaccess" , label : "Limit to open access", desc : "(property:openaccess)", match: "open access"},
    {value: "property:openaccess" , label : "Limit to open access", desc : "(property:openaccess)", match: "property:openaccess"},
    {value: "property:openaccess" , label : "Limit to open access", desc : "(property:openaccess)", match: "openaccess"},

    {value: "property:software" , label : "Limit to software", desc : "(property:software)", match: "software"},
    {value: "property:software" , label : "Limit to software", desc : "(property:software)", match: "property:software"},

    {value: "property:inproceedings" , label : "Limit to papers in conference proceedings", desc : "(property:inproceedings)", match: "conference proceedings"},
    {value: "property:inproceedings" , label : "Limit to papers in conference proceedings", desc : "(property:inproceedings)", match: "property:inproceedings"}


  ]

  return autoList


})