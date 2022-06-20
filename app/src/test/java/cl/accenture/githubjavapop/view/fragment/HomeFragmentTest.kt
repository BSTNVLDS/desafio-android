package cl.accenture.githubjavapop.view.fragment

import cl.accenture.githubjavapop.viewmodel.HomeViewModel
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class HomeFragmentTest{
    @RelaxedMockK
    private lateinit var homeFragment: HomeFragment

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

}