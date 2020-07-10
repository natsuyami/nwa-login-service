package com.natsuyami.project.nwa.config.dto;

import java.io.Serializable;

public class NwaLoginCredentialDto implements Serializable {

  private static final long serialVersionUID = 4968957243360627632L;

  private String username;

  private String passcode;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPasscode() {
    return passcode;
  }

  public void setPasscode(String passcode) {
    this.passcode = passcode;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("{");
    builder.append("\"username\" : \"");
    builder.append(username);
    builder.append("\", \"passcode\" : \"");
    builder.append(passcode);
    builder.append("\"}");
    return builder.toString();
  }
}
