package org.michenux.drodrolib.security;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserHelper {

    private User mCurrentUser;

    @Inject public UserHelper() {
    }

    public User getCurrentUser() {
        return mCurrentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.mCurrentUser = currentUser;
    }
}
