/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.common.test.annotations;

/**
 * Annotation that describes the author of a test with according information
 * like email, affiliation and so on.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public @interface TestAuthor {
    String email();

    String affiliation();
}
