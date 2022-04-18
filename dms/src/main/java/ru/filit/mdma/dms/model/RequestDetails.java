package ru.filit.mdma.dms.model;

public class RequestDetails {

  private final String userRole;
  private final String userName;
  private final String uri;

  private RequestDetails(String userRole, String userName, String uri) {
    this.userRole = userRole;
    this.userName = userName;
    this.uri = uri;
  }

  public static RequestDetails create(String userRole, String userName, String uri) {
    return new RequestDetails(userRole, userName, uri);
  }

  public String getUserRole() {
    return userRole;
  }

  public String getUserName() {
    return userName;
  }

  public String getUri() {
    return uri;
  }

}
