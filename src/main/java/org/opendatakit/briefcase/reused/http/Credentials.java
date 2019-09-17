/*
 * Copyright (C) 2018 Nafundi
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.opendatakit.briefcase.reused.http;

import static org.opendatakit.briefcase.reused.api.Json.get;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Objects;
import org.opendatakit.briefcase.reused.BriefcaseException;
import org.opendatakit.briefcase.reused.api.OptionalProduct;

/**
 * Stores a username/password pair.
 */
public class Credentials {
  private final String username;
  private final String password;

  public Credentials(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public static Credentials from(String username, String password) {
    if (Objects.requireNonNull(username).isEmpty() || Objects.requireNonNull(password).isEmpty())
      throw new IllegalArgumentException("You need to provide non-empty username and password.");

    return new Credentials(username, password);
  }

  public static Credentials from(JsonNode root) {
    return OptionalProduct.all(
        get(root, "username").map(JsonNode::asText),
        get(root, "password").map(JsonNode::asText)
    ).map(Credentials::new).orElseThrow(BriefcaseException::new);
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  //region JSON serialization

  public ObjectNode asJson(ObjectMapper mapper) {
    ObjectNode root = mapper.createObjectNode();
    root.put("username", username);
    root.put("password", password);
    return root;
  }

  //endregion

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Credentials that = (Credentials) o;
    return Objects.equals(username, that.username) &&
        Objects.equals(password, that.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, password);
  }

  @Override
  public String toString() {
    return "Credentials(" + username + ", ***)";
  }
}
