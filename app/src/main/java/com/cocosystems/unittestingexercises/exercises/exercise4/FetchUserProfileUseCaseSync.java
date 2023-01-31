package com.cocosystems.unittestingexercises.exercises.exercise4;

import com.cocosystems.unittestingexercises.exercises.example4.networking.NetworkErrorException;
import com.cocosystems.unittestingexercises.exercises.exercise4.networking.UserProfileHttpEndpointSync;
import com.cocosystems.unittestingexercises.exercises.exercise4.users.User;
import com.cocosystems.unittestingexercises.exercises.exercise4.users.UsersCache;

public class FetchUserProfileUseCaseSync {

    public enum UseCaseResult {
        SUCCESS,
        FAILURE,
        NETWORK_ERROR
    }

    private final UserProfileHttpEndpointSync mUserProfileHttpEndpointSync;
    private final UsersCache mUsersCache;

    public FetchUserProfileUseCaseSync(UserProfileHttpEndpointSync userProfileHttpEndpointSync,
                                       UsersCache usersCache) {
        mUserProfileHttpEndpointSync = userProfileHttpEndpointSync;
        mUsersCache = usersCache;
    }

    public UseCaseResult fetchUserProfileSync(String userId) {
        UserProfileHttpEndpointSync.EndpointResult endpointResult;
        try {
            endpointResult = mUserProfileHttpEndpointSync.getUserProfile(userId);
        } catch (NetworkErrorException e) {
            return UseCaseResult.NETWORK_ERROR;
        }

        if (isSuccessfulEndpointResult(endpointResult)) {
            mUsersCache.cacheUser(
                new User(
                    userId, endpointResult.getFullName(), endpointResult.getImageUrl()
                )
            );

            return UseCaseResult.SUCCESS;
        }

        return UseCaseResult.FAILURE;
    }

    private boolean isSuccessfulEndpointResult(UserProfileHttpEndpointSync.EndpointResult endpointResult) {
        return endpointResult.getStatus() == UserProfileHttpEndpointSync.EndpointResultStatus.SUCCESS;
    }
}
