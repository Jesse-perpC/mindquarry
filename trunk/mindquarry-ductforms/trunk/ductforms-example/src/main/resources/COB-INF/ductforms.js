cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");

var myform;
var mypage;
var pipeline = false;

function edit(form, page) {
	form.loadXML("cocoon:/" + page + ".xml.plain");
	form.showForm(page+".xml.instance");
	//the form includes all possible fields, but only some of them are
	//actually used. the best solution would be to strip out forms
	form.saveXML("xmldb:xindice-embed:///db/test/" + page + ".xml","xslt/ductforms/saveclean.xsl");
	//form.saveXML("xmldb:xindice-embed:///db/test/" + page + ".xml");
	myform = null;
	mypage = null;
	cocoon.redirectTo(page+".xml", false);
}

function editPage(form) {
	print("editing page from cocoon");
	mypage = cocoon.parameters["page"];
	myform = form;
	edit(myform, mypage);
}

function showPage(form) {
	myform = null;
	mypage = null;
	var page = cocoon.parameters["page"];
	//set status to output only.
	form.lookupWidget("/").setState(Packages.org.apache.cocoon.forms.formmodel.WidgetState.OUTPUT);
	form.loadXML("cocoon:/" + page + ".xml.plain");
	form.showForm(page+".xml.instance");
}

function upd(event) {
	if (myform&&mypage) {
		myform.saveXML("xmldb:xindice-embed:///db/test/" + mypage + ".xml","xslt/ductforms/saveclean.xsl");
	}
	cocoon.exit();
}

//ok, I admit Javascript seriously rocks. I am overloading this function
//with another parameter to support transformations of the xml document
//before actually saving
Form.prototype.saveXML = function(uri, xsluri) {
    var source = null;
    var resolver = null;
    var outputStream = null;
    var xslsource = null;
    try {
        resolver = cocoon.getComponent(Packages.org.apache.cocoon.environment.SourceResolver.ROLE);
        source = resolver.resolveURI(uri);
        xslsource = new Packages.javax.xml.transform.stream.StreamSource(resolver.resolveURI(xsluri).getInputStream());
		//print(xslsource);

        var tf = Packages.javax.xml.transform.TransformerFactory.newInstance();

        if (source instanceof Packages.org.apache.excalibur.source.ModifiableSource
            && tf.getFeature(Packages.javax.xml.transform.sax.SAXTransformerFactory.FEATURE)) {

            outputStream = source.getOutputStream();
            var transformerHandler = tf.newTransformerHandler(xslsource);
            var transformer = transformerHandler.getTransformer();
            transformer.setOutputProperty(Packages.javax.xml.transform.OutputKeys.INDENT, "true");
            transformer.setOutputProperty(Packages.javax.xml.transform.OutputKeys.METHOD, "xml");
            transformerHandler.setResult(new Packages.javax.xml.transform.stream.StreamResult(outputStream));
            this.getXML().toSAX(transformerHandler);
        } else {
            throw new Packages.org.apache.cocoon.ProcessingException("Cannot write to source " + uri);
        }

    } finally {
        if (source != null)
            resolver.release(source);

        cocoon.releaseComponent(resolver);

        if (outputStream != null) {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (error) {
                cocoon.log.error("Could not flush/close outputstream: " + error);
            }
        }
    }
}