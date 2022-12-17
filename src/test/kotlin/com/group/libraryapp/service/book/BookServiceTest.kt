package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.book.BookType
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import com.group.libraryapp.dto.book.response.BookStatResponse
import org.assertj.core.api.Assertions
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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
        val request = BookRequest("이상한 나라의 엘리스", BookType.COMPUTER)

        bookService.saveBook(request)

        val books = bookRepository.findAll()
        Assertions.assertThat(books).hasSize(1)
        Assertions.assertThat(books[0].name).isEqualTo("이상한 나라의 엘리스")
        Assertions.assertThat(books[0].type).isEqualTo(BookType.COMPUTER)
    }

    @Test
    @DisplayName("책이 대출되어있으면 신규대출이 실패")
    fun loanBookTest() {
        val user = userRepository.save(User("윤지혜", 27))
        bookRepository.save(Book.fixture("이상한 나라의 엘리스"))
        userLoanHistoryRepository.save(UserLoanHistory.fixture(user, "이상한 나라의 엘리스"))

        val request = BookLoanRequest("윤지혜", "이상한 나라의 엘리스")

        val message = assertThrows<IllegalArgumentException> {
            bookService.loanBook(request)
        }.message
        assertThat(message).isEqualTo("진작 대출되어 있는 책입니다")
    }

    @Test
    @DisplayName("책반납")
    fun returnBookTest() {
        bookRepository.save(Book.fixture("이상한 나라의 엘리스"))

        val savedUser = userRepository.save(User("윤지혜", 27))
        userLoanHistoryRepository.save(UserLoanHistory.fixture(savedUser, "이상한 나라의 엘리스"))

        val request = BookReturnRequest("윤지혜", "이상한 나라의 엘리스")
        bookService.returnBook(request)

        val results = userLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].status).isEqualTo(UserLoanStatus.RETURNED)
    }

    @Test
    @DisplayName("책 대여 권수를 정상 확인한다")
    fun countLoanedBookTest() {
        val savedUser = userRepository.save(User("윤지혜", null))
        userLoanHistoryRepository.saveAll(
            listOf(
                UserLoanHistory.fixture(savedUser, "A"),
                UserLoanHistory.fixture(savedUser, "B", UserLoanStatus.RETURNED),
                UserLoanHistory.fixture(savedUser, "C", UserLoanStatus.RETURNED)
            )
        )

        val result = bookService.countLoanedBook()

        assertThat(result).isEqualTo(1)
    }

    @Test
    @DisplayName("분야별 책 권수를 정상 확인한다")
    fun getBookStatisticsTest() {

        bookRepository.saveAll(
            listOf(
                Book.fixture("A", BookType.COMPUTER),
                Book.fixture("B", BookType.COMPUTER),
                Book.fixture("C", BookType.SCIENCE),
            )
        )

        val result = bookService.getBookStatistics()
        assertThat(result).hasSize(2)
        assertCount(result, BookType.COMPUTER, 2)
        assertCount(result, BookType.SCIENCE, 1)
    }

    private fun assertCount(result: List<BookStatResponse>, type: BookType, expectedCount: Long) {
        assertThat(result.first { it.type == type }.count).isEqualTo(expectedCount)
    }
}