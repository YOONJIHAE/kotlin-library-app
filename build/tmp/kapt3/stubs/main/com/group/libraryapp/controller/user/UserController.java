package com.group.libraryapp.controller.user;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0017\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0012\u0010\n\u001a\u00020\u000b2\b\b\u0001\u0010\f\u001a\u00020\rH\u0017J\u000e\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0006H\u0017J\u0012\u0010\u0010\u001a\u00020\u000b2\b\b\u0001\u0010\u0011\u001a\u00020\u0012H\u0017J\u0012\u0010\u0013\u001a\u00020\u000b2\b\b\u0001\u0010\u0011\u001a\u00020\u0014H\u0017R\u000e\u0010\u0002\u001a\u00020\u0003X\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00068WX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\t\u00a8\u0006\u0015"}, d2 = {"Lcom/group/libraryapp/controller/user/UserController;", "", "userService", "Lcom/group/libraryapp/service/user/UserService;", "(Lcom/group/libraryapp/service/user/UserService;)V", "users", "", "Lcom/group/libraryapp/dto/user/response/UserResponse;", "getUsers", "()Ljava/util/List;", "deleteUser", "", "name", "", "getUserLoanHistories", "Lcom/group/libraryapp/dto/user/response/UserLoanHistoryResponse;", "saveUser", "request", "Lcom/group/libraryapp/dto/user/request/UserCreateRequest;", "updateUserName", "Lcom/group/libraryapp/dto/user/request/UserUpdateRequest;", "library-app"})
@org.springframework.web.bind.annotation.RestController()
public class UserController {
    private final com.group.libraryapp.service.user.UserService userService = null;
    
    public UserController(@org.jetbrains.annotations.NotNull()
    com.group.libraryapp.service.user.UserService userService) {
        super();
    }
    
    @org.springframework.web.bind.annotation.PostMapping(value = {"/user"})
    public void saveUser(@org.jetbrains.annotations.NotNull()
    @org.springframework.web.bind.annotation.RequestBody()
    com.group.libraryapp.dto.user.request.UserCreateRequest request) {
    }
    
    @org.jetbrains.annotations.NotNull()
    @org.springframework.web.bind.annotation.GetMapping(value = {"/user"})
    public java.util.List<com.group.libraryapp.dto.user.response.UserResponse> getUsers() {
        return null;
    }
    
    @org.springframework.web.bind.annotation.PutMapping(value = {"/user"})
    public void updateUserName(@org.jetbrains.annotations.NotNull()
    @org.springframework.web.bind.annotation.RequestBody()
    com.group.libraryapp.dto.user.request.UserUpdateRequest request) {
    }
    
    @org.springframework.web.bind.annotation.DeleteMapping(value = {"/user"})
    public void deleteUser(@org.jetbrains.annotations.NotNull()
    @org.springframework.web.bind.annotation.RequestParam()
    java.lang.String name) {
    }
    
    @org.jetbrains.annotations.NotNull()
    @org.springframework.web.bind.annotation.GetMapping(value = {"/user/loan"})
    public java.util.List<com.group.libraryapp.dto.user.response.UserLoanHistoryResponse> getUserLoanHistories() {
        return null;
    }
}