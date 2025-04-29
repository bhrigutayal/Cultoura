package com.tourismclient.cultoura.fragments

import com.tourismclient.cultoura.adapters.ItineraryAdapter
import com.tourismclient.cultoura.databinding.FragmentSavedItinerariesBinding
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tourismclient.cultoura.R
import com.tourismclient.cultoura.models.Itinerary
import com.tourismclient.cultoura.viewmodels.ItineraryViewModel
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class SavedItinerariesFragment : Fragment() {

    private var _binding: FragmentSavedItinerariesBinding? = null
    private val binding get() = _binding!!

    private lateinit var itineraryViewModel: ItineraryViewModel
    private lateinit var itineraryAdapter: ItineraryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedItinerariesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupRecyclerView()
        setupEmptyState()

        // Observe itineraries from database
        itineraryViewModel.getAllItineraries().observe(viewLifecycleOwner) { itineraries ->
            if (itineraries.isEmpty()) {
                showEmptyState(true)
            } else {
                showEmptyState(false)
                itineraryAdapter.submitList(itineraries)
            }
        }
    }

    private fun setupViewModel() {
        itineraryViewModel = ViewModelProvider(requireActivity())[ItineraryViewModel::class.java]
    }

    private fun setupRecyclerView() {
        itineraryAdapter = ItineraryAdapter(
            onItemClick = { itinerary ->
                // Navigate to detail view if needed
            },
            onShareClick = { itinerary, itemView ->
                shareItineraryAsImage(itinerary, itemView)
            },
            onDeleteClick = { itinerary ->
                showDeleteConfirmationDialog(itinerary)
            }
        )

        binding.rvItineraries.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = itineraryAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupEmptyState() {
        binding.btnCreateItinerary.setOnClickListener {
            // Navigate to create itinerary screen
            // findNavController().navigate(R.id.action_savedItinerariesFragment_to_createItineraryFragment)
        }
    }

    private fun showEmptyState(show: Boolean) {
        binding.emptyStateGroup.visibility = if (show) View.VISIBLE else View.GONE
        binding.rvItineraries.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun shareItineraryAsImage(itinerary: Itinerary, itemView: View) {
        // Create bitmap from the expanded details view
        val detailsView = itemView.findViewById<ViewGroup>(R.id.itinerary_details_container)
        val bitmap = getBitmapFromView(detailsView)

        // Save bitmap to cache directory
        val imagePath = saveBitmapToCache(bitmap, itinerary.title)

        if (imagePath != null) {
            // Create file URI through FileProvider
            val contentUri = FileProvider.getUriForFile(
                requireContext(),
                "com.example.yourapp.fileprovider",
                imagePath
            )

            // Share the image
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, contentUri)
                putExtra(Intent.EXTRA_SUBJECT, "My ${itinerary.title} Itinerary")
                putExtra(Intent.EXTRA_TEXT, "Check out my itinerary for ${itinerary.title}!")
                type = "image/png"
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }

            startActivity(Intent.createChooser(shareIntent, "Share Itinerary"))
        } else {
            Toast.makeText(context, "Failed to share itinerary", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getBitmapFromView(view: View): Bitmap {
        // Make sure the view is visible and laid out
        if (view.visibility != View.VISIBLE) {
            view.visibility = View.VISIBLE
        }

        // Measure and layout the view if needed
        view.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)

        // Create the bitmap and draw the view on it
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        return bitmap
    }

    private fun saveBitmapToCache(bitmap: Bitmap, name: String): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "itinerary_${name.replace(" ", "_")}_$timeStamp.png"

        val cachePath = File(requireContext().cacheDir, "images")
        cachePath.mkdirs() // Make sure the directory exists

        val filePath = File(cachePath, fileName)

        return try {
            val outputStream = FileOutputStream(filePath)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            filePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun showDeleteConfirmationDialog(itinerary: Itinerary) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Itinerary")
            .setMessage("Are you sure you want to delete '${itinerary.title}'? This action cannot be undone.")
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Delete") { _, _ ->
                itineraryViewModel.deleteItinerary(itinerary)
                Toast.makeText(context, "Itinerary deleted", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}