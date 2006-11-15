package com.mindquarry.search.cocoon;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.generation.FileGenerator;
import org.apache.cocoon.generation.Generator;
import org.apache.excalibur.source.SourceNotFoundException;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.mindquarry.search.cocoon.filters.TextFilter;

public class TextFilterGenerator extends FileGenerator implements Generator {
	private Map<String, TextFilter> textFilters;

	public void generate() throws IOException, SAXException,
			ProcessingException {
		Map contents = filter();
		this.contentHandler.startDocument();
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute(null, "mime-type", "mime-type", "CDATA",
				this.inputSource.getMimeType());
		this.contentHandler.startElement(null, "document", "document",
				new AttributesImpl());
		for (Iterator i = contents.keySet().iterator(); i.hasNext();) {
			Object key = i.next();
			Reader value = (Reader) contents.get(key);
			atts = new AttributesImpl();
			// atts.addAttribute(null, "text-filter", "text-filter", "CDATA",
			// value)
			this.contentHandler.startElement(null, key.toString(), key
					.toString(), atts);

			int read = 0;
			char[] cont = {};

			while ((read = value.read(cont, 0, 1024)) != -1) {
				this.contentHandler.characters(cont, 0, read);
			}

			this.contentHandler
					.endElement(null, key.toString(), key.toString());
		}
		this.contentHandler.endElement(null, "document", "document");
		this.contentHandler.endDocument();
	}

	private Map filter() {
		TextFilter filter = this.textFilters.get(this.inputSource
				.getMimeType());
		if (filter == null) {
			try {
				filter = (TextFilter) this.manager
						.lookup("com.mindquarry.search.TextFilter/"
								+ this.inputSource.getMimeType());
				this.textFilters.put(this.inputSource.getMimeType(), filter);
				return filter.doFilter(this.inputSource.getInputStream());
			} catch (ServiceException e) {
				getLogger().error("Could not lookup filter", e);
			} catch (SourceNotFoundException e) {
				getLogger().error("Document not found", e);
			} catch (IOException e) {
				getLogger().error("Could not read document", e);
			} catch (Exception e) {
				getLogger().error("Extracing content failed", e);
			}
		}
		return new HashMap();
	}

	public void dispose() {
		super.dispose();
		for (Iterator<TextFilter> i = this.textFilters.values().iterator(); i.hasNext();) {
			this.manager.release(i.next());
		}
		this.textFilters.clear();
	}

}
