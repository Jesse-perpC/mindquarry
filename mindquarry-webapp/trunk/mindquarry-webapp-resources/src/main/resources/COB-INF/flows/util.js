/**
 * Resolves the 'uri' with the cocoon source resolver, so any registered
 * protocols are available. It will return the source. Note that you will
 * have to release the source after your are finished, using releaseSource().
 */
function resolveSource(uri) {
	var srcResolver;
	try {
		srcResolver = cocoon.getComponent(
				Packages.org.apache.cocoon.environment.SourceResolver.ROLE);
		return srcResolver.resolveURI(uri);		
	} finally {
		cocoon.releaseComponent(srcResolver);
	}
}

/**
 * Releases a source which was obtained by resolveSource().
 * FIXME: don't know if it works if the sourceresolver component is retrieved
 *        again (and not the exact same object when the source was resolved)
 */
function releaseSource(source) {
    if (source != null) {
    	var srcResolver;
    	try {
    		srcResolver = cocoon.getComponent(
    				Packages.org.apache.cocoon.environment.SourceResolver.ROLE);
   			srcResolver.release(source);
    	} finally {
    		cocoon.releaseComponent(srcResolver);
    	}
    }
}

/**
 * Resolves the 'uri' with the cocoon source resolver, so any registered
 * protocols are available. It will return the InputStream of the source.
 */
function resolveSourceInputStream(uri) {
	var source;
	var srcResolver;
	try {
		srcResolver = cocoon.getComponent(
				Packages.org.apache.cocoon.environment.SourceResolver.ROLE);
		source = srcResolver.resolveURI(uri);		
    	return source.getInputStream();
	} finally {
	    // FIXME: is it ok to resolve the source before the inputstream was
	    // actually read by the callee?
		if (source != null) {
			srcResolver.release(source);
		}
		cocoon.releaseComponent(srcResolver);
	}
}

/**
 * Resolves the 'uri' with the cocoon source resolver, so any registered
 * protocols are available. This expects the stream to be a string and returns
 * it as a Javascript string.
 */
function resolveSourceAsString(uri) {
    var stream = resolveSourceInputStream(uri);
    // make it a real javascript string by appending an (empty) string
    return org.apache.commons.io.IOUtils.toString(stream) + "";
}

/**
 * Resolves the 'uri' with the cocoon source resolver, so any registered
 * protocols are available. This expects source to retrieve a Javascript
 * file or snippet. It will return the result of the last expression in the
 * Javascript.
 */
function evalJavaScriptSource(uri) {
    return eval(resolveSourceAsString(uri));
}

