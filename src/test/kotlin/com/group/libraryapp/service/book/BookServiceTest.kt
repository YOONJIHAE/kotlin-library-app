package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import org.assertj.core.api.Assertions
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BookServiceTest @Autowired constructor(
    private val bookService: BookService,
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository
) {
    @AfterEach
    fun clean() {
        bookRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("이상한 나라의 엘리스")
    fun saveBookTest() {
        val request = BookRequest("이상한 나라의 엘리스")

        bookService.saveBook(request)

        val books = bookRepository.findAll()
        Assertions.assertThat(books).hasSize(1)
        Assertions.assertThat(books[0].name).isEqualTo("이상한 나라의 엘리스")
    }

    @Test
    @DisplayName("책이 대출되어있으면 신규대출이 실패")
    fun loanBookTest() {
        bookRepository.save(Book("이상한 나라의 엘리스"))
        val savedUser = userRepository.save(User("윤지혜", 27))
        val request = BookLoanRequest("윤지혜", "이상한 나라의 엘리스")

        bookService.loanBook(request)

        val results = userLoanHistoryRepository.findAll()

        Assertions.assertThat(results).hasSize(1)
        assertThat(results[0].bookName).isEqualTo("이상한 나라의 엘리스")
        assertThat(results[0].user.id).isEqualTo(savedUser.id)
        assertThat(results[0].isReturn).isFalse
    }

    @Test
    @DisplayName("책반납")
    fun returnBookTest() {
        bookRepository.save(Book("이상한 나라의 엘리스"))

        val savedUser = userRepository.save(User("윤지혜", 27))
        userLoanHistoryRepository.save(UserLoanHistory(savedUser, "이상한 나라의 엘리스", false))

        val request = BookReturnRequest("윤지혜", "이상한 나라의 엘리스")
        bookService.returnBook(request)

        val results = userLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].isReturn).isTrue
    }
}