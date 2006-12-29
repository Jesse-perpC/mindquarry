// just a dummy implementation
// available variables are
//   baseURI_ => the base URI before the documentID
//   suffix_ => the file suffix (typically .xml)
//   form_ => the javascript cocoon form object
function createUniqueName(baseURI) {
    return "dforms_" + baseURI;
}

createUniqueName(baseURI_);