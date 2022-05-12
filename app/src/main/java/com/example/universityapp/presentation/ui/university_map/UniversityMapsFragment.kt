package com.example.universityapp.presentation.ui.university_map

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.universityapp.R
import com.example.universityapp.common.BindingFragment
import com.example.universityapp.common.Constant
import com.example.universityapp.common.Resource
import com.example.universityapp.data.model.university.UniversityItem
import com.example.universityapp.databinding.FragmentUniversityMapsBinding
import com.example.universityapp.presentation.MainActivity
import com.example.universityapp.util.Utils
import com.example.universityapp.viewmodel.SharedViewModel
import com.example.universityapp.viewmodel.UniversityListViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UniversityMapsFragment : BindingFragment<FragmentUniversityMapsBinding>(),
    OnMapReadyCallback {
    private val universityListViewModel: UniversityListViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private lateinit var map: GoogleMap

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentUniversityMapsBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
        requestApi()
    }


    private fun requestApi() {
        lifecycleScope.launch {
            sharedViewModel.readGlobalToken.observe(viewLifecycleOwner) { token ->
                requestUniversityData(token)
            }
        }
    }


    private fun requestUniversityData(token: String) {
        lifecycleScope.launch {
            universityListViewModel.getUniversityList(token)
            universityListViewModel.universityListResponse.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    is Resource.Error -> {
                        binding.progressBar.isVisible = false
                        if (result.message == "Token Expire") {
                            requestGlobalToken()
                        } else {
                            Utils.showErrorSnackBar(
                                requireActivity(),
                                result.message.toString(),
                                true
                            )
                        }
                    }
                    is Resource.Success -> {
                        binding.progressBar.isVisible = false
                        result.data?.data?.let { universityList ->
                            initMarker(universityList)


                            map.setOnInfoWindowClickListener { marker ->
                                universityList.filter {
                                    marker.title == it.name
                                }.also {
                                    navigateFragment(it[0].name!!, it[0].id!!)
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    private fun requestGlobalToken() {
        lifecycleScope.launch {
            sharedViewModel.getGlobalToken(sharedViewModel.applyGlobalTokenQueries())
            sharedViewModel.tokenResponse.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    is Resource.Error -> {
                        Utils.showErrorSnackBar(requireActivity(), result.message.toString(), true)
                    }
                    is Resource.Success -> {
                        binding.progressBar.isVisible = false
                        val token = Constant.BEARER + result.data!!.access_token
                        requestUniversityData(token)
                    }
                }
            }
        }
    }

    private fun initMarker(universityList: List<UniversityItem>) {
        universityList.filter {
            it.url!!.isNotEmpty()
        }.forEach {
            val location = LatLng(it.lat!!, it.lng!!)
            map.addMarker(
                MarkerOptions().position(location)
                    .title(it.name)
                    .icon(
                        fromVectorToBitmap(
                            R.drawable.ic_school_marker
                        )
                    )
            )
        }
    }

    private fun fromVectorToBitmap(id: Int): BitmapDescriptor {
        val vectorDrawable: Drawable? = ResourcesCompat.getDrawable(resources, id, null)
        if (vectorDrawable == null) {
            Log.d("MapsActivity", "Resource not found")
            return BitmapDescriptorFactory.defaultMarker()
        }
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)

    }


    private fun navigateFragment(uniTitle: String, uniId: Int) {
        (activity as MainActivity).hideBottomNavigation()
        sharedViewModel.readLoginStatus.observe(viewLifecycleOwner) { loginStatus ->
            if (!loginStatus) {
                findNavController().navigate(
                    UniversityMapsFragmentDirections.actionUniversityMapToLoginFragment(
                        uniTitle, uniId
                    )
                )
            } else {
                findNavController().navigate(
                    UniversityMapsFragmentDirections.actionUniversityMapToUniversityDetail(
                        uniTitle, uniId
                    )
                )
            }
        }
    }
}