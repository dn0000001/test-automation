package com.taf.automation.api.rest;

import org.apache.http.Header;
import org.apache.http.StatusLine;

/**
 * Generic HTTP Response interface
 *
 * @param <T> - Entity that will be returned
 */
public interface GenericHttpResponse<T> {
    /**
     * Get the Status of the response
     *
     * @return StatusLine
     */
    StatusLine getStatus();

    /**
     * Get the response as an entity
     *
     * @return entity
     */
    T getEntity();

    /**
     * Get the response as String
     *
     * @return String
     */
    String getEntityAsString();

    /**
     * Get All Headers
     *
     * @return Header[]
     */
    Header[] getAllHeaders();

    /**
     * Get Header
     *
     * @param name - Name of the header to get
     * @return Header
     */
    Header getHeader(String name);
}
