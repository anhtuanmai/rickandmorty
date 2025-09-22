package demo.at.ram.presentation.testConfig

import dagger.hilt.android.testing.CustomTestApplication
import demo.at.ram.presentation.RickAndMortyApplication

@Suppress("unused")
@CustomTestApplication(RickAndMortyApplication::class)
interface RickAndMortyTestApplication