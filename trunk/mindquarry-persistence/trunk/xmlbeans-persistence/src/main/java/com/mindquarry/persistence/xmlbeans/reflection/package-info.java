/**
 * The *ReflectionData classes use the Java Reflection mechanism
 * to validate configured entity types and to load used methods beforehand.
 * The validation encompasses the existence of the entity types
 * (if the classloader is able to load them) and if they look like
 * generated xmlbeans types.
 * 
 * The methods loaded beforehand are used while persisting and loading entities.
 * For the example entity type 'User', they look like:
 * - User.Factory.parse(InputStream, XmlOptions)
 * - User.Factory.newInstance()
 * - UserDocument.setUser(User)
 * - UserDocument.getUser()
 * 
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
package com.mindquarry.persistence.xmlbeans.reflection;