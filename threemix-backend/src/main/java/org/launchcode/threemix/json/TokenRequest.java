package org.launchcode.threemix.json;

public record TokenRequest(String grant_type, String code, String redirect_uri) {
}
