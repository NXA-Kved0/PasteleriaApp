package com.example.sqlite.data.repository

import com.example.sqlite.data.local.User
import com.example.sqlite.data.local.UserDao
import kotlinx.coroutines.flow.Flow

class UserRepository(private val dao: UserDao) {
    val users: Flow<List<User>> = dao.getAllUsers()

    suspend fun insert(user: User) = dao.insertUser(user)
    suspend fun update(user: User) = dao.updateUser(user)
    suspend fun delete(user: User) = dao.deleteUser(user)

    suspend fun login(email: String, password: String): User? {
        return dao.getUserByEmailAndPassword(email, password)
    }
    suspend fun getUserByEmail(email: String): User? {
        return dao.getUserByEmail(email)
    }
}
