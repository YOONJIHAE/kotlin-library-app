package com.group.libraryapp.dto.user.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory

data class UserLoanHistoryResponse(
    val name: String,
    val books: List<BookHistoryResponse>
) {
    companion object {
        fun of(user: User): UserLoanHistoryResponse {
            return UserLoanHistoryResponse(
                name = user.name,
                books = user.userLoanHistories.map(BookHistoryResponse::of)
            )
        }
    }
}

data class BookHistoryResponse(
    val name: String,
    @get:JsonProperty("isReturn")
    val isReturn: Boolean
) {
    companion object {
        fun of(userLoanHistory: UserLoanHistory): BookHistoryResponse {
            return BookHistoryResponse(
                name = userLoanHistory.bookName,
                isReturn = userLoanHistory.isReturn
            )
        }
    }
}