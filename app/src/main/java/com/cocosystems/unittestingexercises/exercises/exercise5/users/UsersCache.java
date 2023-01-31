package com.cocosystems.unittestingexercises.exercises.exercise5.users;

public interface UsersCache {

    void cacheUser(User user);

    User getUser(String userId);

}
