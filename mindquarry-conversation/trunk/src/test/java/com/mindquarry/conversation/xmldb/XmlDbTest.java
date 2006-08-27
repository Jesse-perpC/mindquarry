/*
 * Copyright (C) 2006, Mindquarry GmbH 
 */
package com.mindquarry.conversation.xmldb;

import junit.framework.TestCase;

import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XPathQueryService;

/**
 * @author <a hef="mailto:alexander(dot)saar(at)mindquarry(dot)com</a>
 */
public class XmlDbTest extends TestCase {
	private static final String URI = "xmldb:exist://localhost:8080/exist/xmlrpc/db";

	public void testDatabaseConnection() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, XMLDBException {
		String driver = "org.exist.xmldb.DatabaseImpl";
		Class clazz = Class.forName(driver);

		Database database = (Database) clazz.newInstance();
		System.out.println("Using Core Level " + database.getConformanceLevel()
				+ " XML:DB API driver...");

		DatabaseManager.registerDatabase(database);
		Collection col = DatabaseManager.getCollection(URI);
		if (database.getConformanceLevel().equals("1")) {
			String xpath = "/address[@id = 1]";
			XPathQueryService service = (XPathQueryService) col.getService(
					"XPathQueryService", "1.0");
			service.setProperty("indent", "yes");

			ResourceSet resultSet = service.query(xpath);
			ResourceIterator results = resultSet.getIterator();
			while (results.hasMoreResources()) {
				Resource res = results.nextResource();
				System.out.println(res.getContent());
			}
		} else {
			 XMLResource res = (XMLResource)col.getChildCollection("addresses");
			 if(res == null)
				 System.out.println("document not found!");
			 else
				 System.out.println(res.getContent());
//			Collection root = DatabaseManager.getCollection(URI);
//			CollectionManagementService mgtService = (CollectionManagementService) root
//					.getService("CollectionManagementService", "1.0");
//
//			col = mgtService.createCollection("test");
//			XMLResource document = (XMLResource) col.createResource(null,
//					"XMLResource");
		}
		col.close();
	}
}
