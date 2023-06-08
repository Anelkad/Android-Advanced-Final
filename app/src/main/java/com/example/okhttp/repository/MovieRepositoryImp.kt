package com.example.okhttp.repository
import com.example.okhttp.api.RetrofitService
import com.example.okhttp.models.MovieDetails
import javax.inject.Inject

class MovieRepositoryImp @Inject constructor(): MovieRepository{

    private val service = RetrofitService()
    override suspend fun getMovie(movieId: Int): MovieDetails = service.getMovie(movieId)
    override suspend fun getMovieList(page: Int) = service.getMovieList(page)

}