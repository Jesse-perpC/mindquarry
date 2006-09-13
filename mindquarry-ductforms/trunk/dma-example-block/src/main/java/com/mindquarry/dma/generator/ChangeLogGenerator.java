package com.mindquarry.dma.generator;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.components.source.SourceUtil;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.generation.ServiceableGenerator;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.SourceValidity;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.mindquarry.dma.source.Change;
import com.mindquarry.dma.source.ChangeableSource;

public class ChangeLogGenerator extends ServiceableGenerator implements CacheableProcessingComponent, Generator {
    /** The URI of the namespace of this generator. */
    protected static final String URI = "http://mindquarry.com/ns/schema/changelog";

    /** The namespace prefix for this namespace. */
    protected static final String PREFIX = "log";

	private static final String CHANGELOG = "changelog";

	private static final String BASE = "base";

	private static final String AUTHOR = "author";

	private static final String REVISION = "revision";

	private static final String DATE = "date";

	private static final String MESSAGE = "message";

	private static final String CHANGE = "change";

	private static final String PATH = "path";

	private static final String SRC = "src";
	/**
	 * The changeable source that is subject to log generation
	 */
	private ChangeableSource source;
	/**
	 * The caching of the data depends on the parameters given to this
	 * generator, so they are saved here for better cache key generation.
	 */
	private List cacheKeyParameters;
	/**
	 * The revision up to which logs shall be generated
	 */
	private long revision;
	/**
	 * The number of log entries to be generated
	 */
	private long size;
	/**
	 * provide date formatting capabilities
	 */
	private SimpleDateFormat dateFormatter;
	@Override
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters params) throws ProcessingException, SAXException, IOException {
		if (src == null) {
            throw new ProcessingException("No src attribute pointing to a source to be logged specified.");
        }
		super.setup(resolver, objectModel, src, params);
		
		//initialize the source, but use only changeable sources
		try {
            this.source = (ChangeableSource) this.resolver.resolveURI(src);
        } catch (SourceException se) {
            throw SourceUtil.handle(se);
        } catch (ClassCastException cce) {
        	throw new ProcessingException("Specified source is not changeable and has no history", cce);
        }
        //if no revision is specified, use the head revision
        this.revision = params.getParameterAsLong("revision", Long.parseLong(this.source.getLatestSourceRevision()));
        //if no maximum number is specified, get all
        this.size = params.getParameterAsLong("entries", -1);
        
        //save the parameters for generation to provide caching capabilities
        this.cacheKeyParameters = new ArrayList();
        this.cacheKeyParameters.add(this.source.getURI());
        this.cacheKeyParameters.add(new Long(this.revision));
        this.cacheKeyParameters.add(new Long(this.size));
        
        String dateFormatString = params.getParameter("dateFormat", null);
        this.cacheKeyParameters.add(dateFormatString);
        if (dateFormatString != null) {
            this.dateFormatter = new SimpleDateFormat(dateFormatString);
        } else {
            this.dateFormatter = new SimpleDateFormat();
        }
	}

	public Serializable getKey() {
        StringBuffer buffer = new StringBuffer();
        for (Object key : this.cacheKeyParameters) {
            buffer.append(key.toString() + ":");
        }
        return buffer.toString();
	}
    

	public SourceValidity getValidity() {
		return this.source.getValidity();
	}

	public void generate() throws IOException, SAXException,
			ProcessingException {
		this.contentHandler.startDocument();
		this.contentHandler.startPrefixMapping(PREFIX, URI);
		
		AttributesImpl attributes = new AttributesImpl();
		attributes.addAttribute("", BASE, BASE, "CDATA", this.source.getURI());
		this.contentHandler.startElement(URI, CHANGELOG, PREFIX + ":" + CHANGELOG, attributes);
		this.generateChanges();
		
		this.contentHandler.endElement(URI, CHANGELOG, PREFIX+":" + CHANGELOG);
		
		this.contentHandler.endPrefixMapping(PREFIX);
		this.contentHandler.endDocument();
	}

	private void generateChanges() throws SourceException, SAXException {
		for (Change change : this.source.changesFrom(this.revision, this.size)) {
			AttributesImpl attributes = new AttributesImpl();
			attributes.addAttribute("", AUTHOR, AUTHOR, "CDATA", change.author_);
			attributes.addAttribute("", REVISION, REVISION, "CDATA", change.revision_);
			attributes.addAttribute("", DATE, DATE, "CDATA", this.dateFormatter.format(change.date_));
			attributes.addAttribute("", MESSAGE, MESSAGE, "CDATA", change.message_);
			this.contentHandler.startElement(URI, CHANGE, PREFIX + ":"+ CHANGE, attributes);
			this.generatePaths(change);
			this.contentHandler.endElement(URI, CHANGE, PREFIX + ":" + CHANGE);
		}
	}

	private void generatePaths(Change change) throws SAXException {
		for (String path : change.changedPaths_) {
			AttributesImpl attributes = new AttributesImpl();
			attributes.addAttribute("", SRC, SRC, "CDATA", path);
			this.contentHandler.startElement(URI, PATH, PREFIX + ":" + PATH, null);
			this.contentHandler.endElement(URI, PATH, PREFIX + ":" + PATH);
		}
	}
}
