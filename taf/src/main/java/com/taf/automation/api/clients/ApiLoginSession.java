package com.taf.automation.api.clients;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import com.taf.automation.api.ApiUtils;

/**
 * Class to handle API Login and store the session
 */
public class ApiLoginSession {
    private ApiClient client = new ApiClient();
    private List<Header> headers = new ArrayList<>();

    //
    // Application specific information returned by login to send future requests in the session
    //

    private String userId;
    private String token;

    public GenericResponse<UserLogin> login(String userEmail, String password) {
        //
        // Application specific information to login
        //

        NameValuePair userEmailPair = new BasicNameValuePair("username", userEmail);
        NameValuePair passwordPair = new BasicNameValuePair("password", password);
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(userEmailPair);
        parameters.add(passwordPair);

        HttpEntity entity = ApiUtils.getFormHttpEntity(parameters);

        //
        // Application specific resource path to login
        //
        String resourcePath = "/api/user/login";

        GenericResponse<UserLogin> apiResponse = (GenericResponse<UserLogin>) client.post(resourcePath, entity, UserLogin.class, headers);
        if (apiResponse.getEntity() != null) {
            UserLogin userLogins = apiResponse.getEntity();
            this.token = userLogins.getToken();
            this.userId = userLogins.getUserID();
        }

        return apiResponse;
    }

    /**
     * Set the authentication headers to prevent cross-site forgery request errors. This is application
     * specific and may need to be updated
     *
     * @param userId
     * @param token
     */
    private void setAuthHeaders(String userId, String token) {
        if (userId != null && token != null) {
            String value = String.format("\"id=%s \", \"token=%s\"", userId, token);
            BasicHeader header = new BasicHeader("XSFR", value);
            headers.add(header);
        }
    }

    public <T> GenericResponse<T> post(String resourcePath, HttpEntity entity, Class<T> responseEntityType) {
        return post(resourcePath, entity, responseEntityType, userId, token);
    }

    public <T> GenericResponse<T> post(String resourcePath, HttpEntity entity, Class<T> responseEntityType, String userId, String token) {
        setAuthHeaders(userId, token);
        return (GenericResponse<T>) client.post(resourcePath, entity, responseEntityType, headers);
    }

    public <T> GenericResponse<T> get(String resourcePath, Class<T> responseEntityType) {
        return get(resourcePath, responseEntityType, userId, token);
    }

    public <T> GenericResponse<T> get(String resourcePath, Class<T> responseEntityType, String userId, String token) {
        setAuthHeaders(userId, token);
        return (GenericResponse<T>) client.get(resourcePath, responseEntityType, headers);
    }

    public <T> GenericResponse<T> put(String resourcePath, HttpEntity entity, Class<T> responseEntityType) {
        return put(resourcePath, entity, responseEntityType, userId, token);
    }

    public <T> GenericResponse<T> put(String resourcePath, HttpEntity entity, Class<T> responseEntityType, String userId, String token) {
        setAuthHeaders(userId, token);
        return (GenericResponse<T>) client.put(resourcePath, entity, responseEntityType, headers);
    }

    public <T> GenericResponse<T> delete(String resourcePath, Class<T> responseEntityType) {
        return delete(resourcePath, responseEntityType, userId, token);
    }

    public <T> GenericResponse<T> delete(String resourcePath, Class<T> responseEntityType, String userId, String token) {
        setAuthHeaders(userId, token);
        return (GenericResponse<T>) client.delete(resourcePath, responseEntityType, headers);
    }

    public <T> GenericResponse<T> delete(String resourcePath, Object entity, Class<T> responseEntityType) {
        return delete(resourcePath, entity, responseEntityType, userId, token);
    }

    public <T> GenericResponse<T> delete(String resourcePath, Object entity, Class<T> responseEntityType, String userId, String token) {
        setAuthHeaders(userId, token);
        return (GenericResponse<T>) client.delete(resourcePath, entity, responseEntityType, headers);
    }

    public <T> GenericResponse<T> patch(String resourcePath, HttpEntity entity, Class<T> responseEntityType) {
        return patch(resourcePath, entity, responseEntityType, userId, token);
    }

    public <T> GenericResponse<T> patch(String resourcePath, HttpEntity entity, Class<T> responseEntityType, String userId, String token) {
        setAuthHeaders(userId, token);
        return (GenericResponse<T>) client.patch(resourcePath, entity, responseEntityType, headers);
    }

    public void addHeaders(List<BasicHeader> headers) {
        this.headers.addAll(headers);
    }

    public void addHeader(BasicHeader header) {
        this.headers.add(header);
    }

    public String getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

}
