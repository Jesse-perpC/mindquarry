package com.mindquarry.persistence.jcr;

import com.mindquarry.persistence.api.Entity;
import com.mindquarry.persistence.api.NamedQuery;

@Entity(parentFolder="extendedUsers")
@NamedQuery(name="extendedUserByLogin", query="/extendedUsers/{$userId}")
public class ExtendedUser extends User {

    private String nameExtension;

    public String getNameExtension() {
        return nameExtension;
    }

    public void setNameExtension(String nameExtension) {
        this.nameExtension = nameExtension;
    }
}
