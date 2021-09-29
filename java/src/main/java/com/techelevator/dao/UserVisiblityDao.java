package com.techelevator.dao;

public interface UserVisiblityDao {
    void grantAccessTo(int userGettingAccess, int userGrantingAccess);
}
