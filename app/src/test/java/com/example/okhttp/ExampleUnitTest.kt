package com.example.okhttp

import com.example.okhttp.domain.usecases.GetUserPrefsUseCase
import com.example.okhttp.domain.usecases.LogoutUseCase
import com.example.okhttp.profile.ProfileViewModel
import org.junit.Test

import org.junit.Assert.*
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun checkProfileLogout(){
        val mockLogoutUseCase = mock(LogoutUseCase::class.java)
        val getUserPrefsUseCase = mock(GetUserPrefsUseCase::class.java)
        val viewModel = ProfileViewModel(getUserPrefsUseCase, mockLogoutUseCase)
        viewModel.logout()
        assertEquals(viewModel.state.value,ProfileViewModel.State.LoggedOut)
        verify(mockLogoutUseCase).logout()
    }

    @Test
    fun checkProfile(){
        val mockGetUserPrefsUseCase = mock(GetUserPrefsUseCase::class.java)
        val mockLogoutUseCase = mock(LogoutUseCase::class.java)
        given(mockGetUserPrefsUseCase.getUid()).willReturn("123")
        given(mockGetUserPrefsUseCase.getUsername()).willReturn("John Doe")
        given(mockGetUserPrefsUseCase.getSession()).willReturn("ABC")

        val viewModel = ProfileViewModel(mockGetUserPrefsUseCase, mockLogoutUseCase)

        assertEquals(viewModel.state.value,ProfileViewModel.State.Empty)

        viewModel.getProfile()

        val expectedProfile = ProfileViewModel.State.Profile(
            id = "123",
            username = "John Doe",
            session = "ABC"
        )
        assertEquals(viewModel.state.value, expectedProfile)

        verify(mockGetUserPrefsUseCase).getUid()
        verify(mockGetUserPrefsUseCase).getUsername()
        verify(mockGetUserPrefsUseCase).getSession()
    }
}