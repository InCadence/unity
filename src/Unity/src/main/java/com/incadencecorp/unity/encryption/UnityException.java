package com.incadencecorp.unity.encryption;

/*-----------------------------------------------------------------------------'
 Copyright 2014 - InCadence Strategic Solutions Inc., All Rights Reserved

 Notwithstanding any contractor copyright notice, the Government has Unlimited
 Rights in this work as defined by DFARS 252.227-7013 and 252.227-7014.  Use
 of this work other than as specifically authorized by these DFARS Clauses may
 violate Government rights in this work.

 DFARS Clause reference: 252.227-7013 (a)(16) and 252.227-7014 (a)(16)
 Unlimited Rights. The Government has the right to use, modify, reproduce,
 perform, display, release or disclose this computer software and to have or
 authorize others to do so.

 Distribution Statement D. Distribution authorized to the Department of
 Defense and U.S. DoD contractors only in support of U.S. DoD efforts.
 -----------------------------------------------------------------------------*/

/**
 * Base exception used by Coalesce.
 */
public class UnityException extends Exception {

    private static final long serialVersionUID = -675879916269321942L;

    /**
     * Constructor function. Sets the message and exception to the parameter passed in.
     * 
     * @param message allowed object is {@link String }
     * @param ex allowed object is {@link Exception }
     */
    public UnityException(final String message, final Exception ex)
    {
        super(message, ex);
    }

    /**
     * Constructor function. Sets the message to the parameter passed in.
     * 
     * @param message allowed object is {@link String }
     */
    public UnityException(final String message)
    {
        super(message);
    }
}
