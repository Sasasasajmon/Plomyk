package szymon.swic.plomyk.features.songs.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import szymon.swic.plomyk.features.songs.data.repository.SongRepositoryImpl
import szymon.swic.plomyk.features.songs.details.presentation.SongDetailsViewModel
import szymon.swic.plomyk.features.songs.domain.GetSongsUseCase
import szymon.swic.plomyk.features.songs.domain.SongRepository
import szymon.swic.plomyk.features.songs.list.presentation.SongBookViewModel
import szymon.swic.plomyk.features.songs.list.presentation.SongListAdapter
import szymon.swic.plomyk.features.songs.list.presentation.SongListFragment
import szymon.swic.plomyk.features.songs.navigation.SongNavigator
import szymon.swic.plomyk.features.songs.navigation.SongNavigatorImpl

val songModule = module {

    // data
    factory<SongRepository> { SongRepositoryImpl(get(), get(), get(), get()) }

    // domain
    factory { GetSongsUseCase(get()) }

    // navigation
    factory<SongNavigator> { SongNavigatorImpl(get()) }

    // presentation
    factory { SongListFragment() }
    factory { SongListAdapter() }
    viewModel { SongBookViewModel(get(), get(), get()) }
    viewModel { SongDetailsViewModel() }
}