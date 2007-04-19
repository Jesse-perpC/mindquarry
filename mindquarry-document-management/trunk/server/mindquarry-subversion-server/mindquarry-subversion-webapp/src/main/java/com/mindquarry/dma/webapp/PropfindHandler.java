package com.mindquarry.dma.webapp;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import dax.Transformer;

public class PropfindHandler extends Transformer implements RequestHandler{
	private Document output;
	private Node current;
	private HttpServletResponse response;

	public boolean handle(HttpServletRequest request,
			HttpServletResponse response) throws DocumentException, IOException {
		String method = request.getMethod();
		if (!method.equals("PROPFIND")) {
			return false;
		}
		SAXReader reader = new SAXReader();
		Document input = reader.read(request.getInputStream());
		this.output = DocumentFactory.getInstance().createDocument();
		this.current = this.output;
		this.response = response;
		this.execute(input);
		Writer writer = response.getWriter();
		output.write(writer);
		writer.close();
		return true;
	}

}
