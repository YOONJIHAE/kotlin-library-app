package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor(
    private val userService: UserService,
    private val userRepository: UserRepository
) {

    @AfterEach
    fun clean() {
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("유저 저장")
    fun saveUserTest() {

        val request = UserCreateRequest("윤지혜", null)

        userService.saveUser(request)

        val users = userRepository.findAll()
        Assertions.assertThat(users).hasSize(1)
        Assertions.assertThat(users[0].name).isEqualTo("윤지혜")
        Assertions.assertThat(users[0].age).isNull()
    }

    @Test
    @DisplayName("유저 조회")
    fun getUsersTest() {
        userRepository.saveAll(listOf(
            User("A", 20),
            User("B", null)
        ))

        val results = userService.getUsers()

        Assertions.assertThat(results).hasSize(2)
        Assertions.assertThat(results).extracting("name").containsExactlyInAnyOrder("A", "B")
        Assertions.assertThat(results).extracting("age").containsExactlyInAnyOrder(20, null)
    }

    @Test
    @DisplayName("유저 업데이트")
    fun updateUserNameTest() {

        val saveUser = userRepository.save(User("A", null))
        val request = UserUpdateRequest(saveUser.id!!, "B")

        userService.updateUserName(request)

        val result = userRepository.findAll()[0]
        Assertions.assertThat(request.name).isEqualTo("B")
    }

    @Test
    @DisplayName("유저 삭제")
    fun deleteUserTest() {
        userRepository.save(User("A", null))
        userService.deleteUser("A")

        Assertions.assertThat(userRepository.findAll()).isEmpty()
    }
}