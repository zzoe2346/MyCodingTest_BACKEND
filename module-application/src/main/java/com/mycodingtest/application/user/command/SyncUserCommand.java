package com.mycodingtest.application.user.command;

/**
 * 이거 채점이랑 ㄱ같은거긴한데 하 일단 보류
 */
public record SyncUserCommand(
        String name,
        String email,
        String picture,
        String provider,
        String oauthId
) {
    public static SyncUserCommand from(
            String name,
            String email,
            String picture,
            String provider,
            String oauthId
    ) {
        return new SyncUserCommand(
                name,
                email,
                picture,
                provider,
                oauthId
        );
    }
}
