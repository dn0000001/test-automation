package com.automation.common.ui.app.domainObjects;

import com.taf.automation.api.html.HtmlUtils;
import com.taf.automation.ui.support.DomainObject;
import com.taf.automation.ui.support.util.Utils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.http.HttpStatus;
import org.jsoup.Connection;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;

/**
 * Class to hold all links to validate
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
@XStreamAlias("links-do")
public class LinksDO extends DomainObject {
    private static final String USER_AGENT = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:63.0) Gecko/20100101 Firefox/63.0";
    private List<String> links;

    public List<String> getLinks() {
        if (links == null) {
            links = new ArrayList<>();
        }

        return links;
    }

    public static void validateUrl(String link) {
        HtmlUtils htmlUtils = new HtmlUtils();
        int statusCode = 1000;
        for (int i = 0; i < 3; i++) {
            Connection.Response response = htmlUtils.navigate(link, USER_AGENT);
            statusCode = response.statusCode();
            if (statusCode > HttpStatus.SC_OK) {
                Utils.sleep(1000);
            } else {
                break;
            }
        }

        assertThat(statusCode, lessThan(HttpStatus.SC_BAD_REQUEST));
    }

}
