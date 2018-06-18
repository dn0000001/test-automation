package com.taf.automation.api.rest;

import org.apache.http.Header;

import java.util.List;

/**
 * Generic HTTP Interface
 */
public interface GenericHttpInterface {
    /**
     * Performs a POST request
     *
     * @param resourcePath       - Relative Resource Path
     * @param entity             - Parameters to be sent
     * @param responseEntityType - Response Entity type
     * @param headers            - Headers
     * @return GenericHttpResponse
     */
    <T> GenericHttpResponse<T> post(String resourcePath, Object entity, Class<T> responseEntityType, List<Header> headers);

    /**
     * Performs a GET request
     *
     * @param resourcePath       - Relative Resource Path
     * @param responseEntityType - Response Entity type
     * @param headers            - Headers
     * @return GenericHttpResponse
     */
    <T> GenericHttpResponse<T> get(String resourcePath, Class<T> responseEntityType, List<Header> headers);

    /**
     * Performs a PUT request
     *
     * @param resourcePath       - Relative Resource Path
     * @param entity             - Parameters to be sent
     * @param responseEntityType - Response Entity type
     * @param headers            - Headers
     * @return GenericHttpResponse
     */
    <T> GenericHttpResponse<T> put(String resourcePath, Object entity, Class<T> responseEntityType, List<Header> headers);

    /**
     * Performs a DELETE request
     *
     * @param resourcePath       - Relative Resource Path
     * @param responseEntityType - Response Entity type
     * @param headers            - Headers
     * @return GenericHttpResponse
     */
    <T> GenericHttpResponse<T> delete(String resourcePath, Class<T> responseEntityType, List<Header> headers);

    /**
     * Performs a PATDH request
     *
     * @param resourcePath       - Relative Resource Path
     * @param entity             - Parameters to be sent
     * @param responseEntityType - Response Entity type
     * @param headers            - Headers
     * @return GenericHttpResponse
     */
    <T> GenericHttpResponse<T> patch(String resourcePath, Object entity, Class<T> responseEntityType, List<Header> headers);

    /**
     * Performs a DELETE request
     *
     * @param resourcePath       - Relative Resource Path
     * @param entity             - Parameters to be sent
     * @param responseEntityType - Response Entity type
     * @param headers            - Headers
     * @return GenericHttpResponse
     */
    <T> GenericHttpResponse<T> delete(String resourcePath, Object entity, Class<T> responseEntityType, List<Header> headers);
}
