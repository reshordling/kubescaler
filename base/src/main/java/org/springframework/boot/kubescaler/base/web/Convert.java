package org.springframework.boot.kubescaler.base.web;

import org.springframework.boot.kubescaler.base.model.Profile;
import org.springframework.boot.kubescaler.base.model.User;

class Convert {
  static org.springframework.boot.kubescaler.api.User api(User user) {
    return new org.springframework.boot.kubescaler.api.User(user.getId(), user.getName());
  }

  static User base(org.springframework.boot.kubescaler.api.User user) {
    return User.builder().id(user.getId()).name(user.getName()).build();
  }


  static org.springframework.boot.kubescaler.api.Profile api(Profile profile) {
    return new org.springframework.boot.kubescaler.api.Profile(profile.getId(), profile.getUsers());
  }

  static Profile base(org.springframework.boot.kubescaler.api.Profile profile) {
    return Profile.builder().id(profile.getId()).users(profile.getUsers()).build();
  }
}
