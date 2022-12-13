package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import org.assertj.core.api.Assertions
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor(
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
) {

    @AfterEach
    fun clean() {
        println("UserServiceTest CLEAN")
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
        userRepository.saveAll(
            listOf(
                User("A", 20),
                User("B", null)
            )
        )

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

    @Test
    @DisplayName("대출 기록이 없는 유저도 응답에 포함")
    fun getUserLoanHistoriesTest1() {

        userRepository.save(User("A", null))

        val results = userService.getUserLoanHistories()

        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("A")
        assertThat(results[0].books).isEmpty()
    }

    @Test
    @DisplayName("대출 기록이 많은 유저의 응답이 정상 동작")
    fun getUserLoanHistoriesTest2() {
        val savedUser = userRepository.save(User("A", null))

        userLoanHistoryRepository.saveAll(
            listOf(
                UserLoanHistory(savedUser, "1", UserLoanStatus.LOANED),
                UserLoanHistory(savedUser, "2", UserLoanStatus.LOANED),
                UserLoanHistory(savedUser, "3", UserLoanStatus.RETURNED),
            )
        )

        val results = userService.getUserLoanHistories()

        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("A")
        assertThat(results[0].books).hasSize(3)
        assertThat(results[0].books).extracting("name").containsExactlyInAnyOrder("1", "2", "3")
        assertThat(results[0].books).extracting("isReturn").containsExactlyInAnyOrder(false, false, true)
    }

    @Test
    @DisplayName("위에 테스트 두개 합치기")
    fun getUserLoanHistoriesTest3() {

        val savedUsers = userRepository.saveAll(
            listOf(
                User("A", null),
                User("B", null),
            )
        )

        userLoanHistoryRepository.saveAll(
            listOf(
                UserLoanHistory(savedUsers[0], "1", UserLoanStatus.LOANED),
                UserLoanHistory(savedUsers[0], "2", UserLoanStatus.LOANED),
                UserLoanHistory(savedUsers[0], "3", UserLoanStatus.RETURNED),
            )
        )

        val results = userService.getUserLoanHistories()

        assertThat(results).hasSize(2)

        val userAResult = results.first { it.name == "A" }

        assertThat(userAResult.books).hasSize(3)
        assertThat(userAResult.books).extracting("name").containsExactlyInAnyOrder("1", "2", "3")
        assertThat(userAResult.books).extracting("isReturn").containsExactlyInAnyOrder(false, false, true)

        val userBResult = results.first { it.name == "B" }

        assertThat(userBResult.books).isEmpty()
    }
}