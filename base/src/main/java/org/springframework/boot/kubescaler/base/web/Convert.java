package org.springframework.boot.kubescaler.base.web;

import org.springframework.boot.kubescaler.base.model.Profile;
import org.springframework.boot.kubescaler.base.model.User;

class Convert {
  static org.springframework.boot.kubescaler.api.User api(User user) {
    return new org.springframework.boot.kubescaler.api.User(user.getId());
  }

  static User base(org.springframework.boot.kubescaler.api.User user) {
    return new User(user.getId());
  }


  static org.springframework.boot.kubescaler.api.Profile api(Profile profile) {
    return new org.springframework.boot.kubescaler.api.Profile(profile.getId());
  }

  static Profile base(org.springframework.boot.kubescaler.api.Profile profile) {
    return new Profile(profile.getId());
  }
}
