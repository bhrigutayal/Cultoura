package com.example.tourismapp.screens

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.tourismapp.R
import com.example.tourismapp.data.Images
import com.example.tourismapp.data.States
import com.example.tourismapp.utils.LocationUtils
import com.example.tourismapp.viewmodel.HomeViewModel
import com.example.tourismapp.viewmodel.ItineraryViewModel
import com.example.tourismapp.viewmodel.LocationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomePage(
    homeViewModel: HomeViewModel,
    locationUtils: LocationUtils,
    locationViewModel: LocationViewModel,
    navController: NavController,
    itineraryViewModel: ItineraryViewModel
) {
    val scope = rememberCoroutineScope()
    val selected by homeViewModel.selected.observeAsState()
    val state by homeViewModel.state.observeAsState()
    val scaffoldState = rememberScaffoldState()

    BackHandler(enabled = true) {
        // Handle back press if needed
    }

    Scaffold(
        topBar = { TopBar(scope, scaffoldState, selected) },
        bottomBar = { BottomNavigationBar(homeViewModel, selected) },
        scaffoldState = scaffoldState
    ) {
        when (selected) {
            "Home" -> HomeView(it, homeViewModel)
            "Search" -> SearchPage(it)
            "Location" -> LocationPage(it, state)
            "Profile" -> ProfilePage(it, homeViewModel, navController)
            "Itinerary" -> Itinerary(
                locationUtils = locationUtils,
                locationViewModel = locationViewModel,
                navController = navController,
                paddingValues = it,
                homeViewModel = homeViewModel,
                itineraryViewModel = itineraryViewModel
            )
        }
    }
}

@Composable
fun TopBar(scope: CoroutineScope, scaffoldState: ScaffoldState, selected: String?) {
    val navigationIcon: @Composable (() -> Unit) = {
        IconButton(onClick = {
            scope.launch {
                scaffoldState.drawerState.open()
            }
        }) {
            Icon(
                imageVector = Icons.Default.Menu,
                tint = Color.White,
                contentDescription = null
            )
        }
    }

    TopAppBar(
        title = {
            Text(
                text = selected ?: "",
                color = colorResource(id = R.color.white),
                modifier = Modifier
                    .padding(start = 4.dp)
                    .heightIn(max = 24.dp)
            )
        },
        elevation = 3.dp,
        backgroundColor = colorResource(id = R.color.text_field),
        navigationIcon = navigationIcon
    )
}

@Composable
fun BottomNavigationBar(homeViewModel: HomeViewModel, selected: String?) {
    androidx.compose.material3.BottomAppBar(
        containerColor = colorResource(id = R.color.text_field),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BottomNavItem(
                icon = Icons.Default.Home,
                contentDescription = "Home",
                selected = selected == "Home",
                onClick = { homeViewModel.Navigate("Home") }
            )
            BottomNavItem(
                icon = Icons.Default.Search,
                contentDescription = "Search",
                selected = selected == "Search",
                onClick = { homeViewModel.Navigate("Search") }
            )
            FloatingActionButton(onClick = { homeViewModel.Navigate("Itinerary") }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White)
            }
            BottomNavItem(
                icon = Icons.Default.LocationOn,
                contentDescription = "Location",
                selected = selected == "Location",
                onClick = { homeViewModel.Navigate("Location") }
            )
            BottomNavItem(
                icon = Icons.Default.Person,
                contentDescription = "Profile",
                selected = selected == "Profile",
                onClick = { homeViewModel.Navigate("Profile") }
            )
        }
    }
}

@Composable
fun BottomNavItem(icon: ImageVector, contentDescription: String, selected: Boolean, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = if (selected) Modifier
            .size(50.dp)
            .background(color = Color.White)
            .clip(CircleShape)
            .border(1.dp, Color.White) else Modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (selected) colorResource(id = R.color.text_field) else Color.White
        )
    }
}

@Composable
fun StateRowItem(state: States, homeViewModel: HomeViewModel) {
    Column(
        modifier = Modifier
            .size(150.dp)
            .clickable {
                homeViewModel.Navigate("Location")
                homeViewModel.setState(state.name)
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(state.image),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(2.dp, state.borderColor, CircleShape)
        )
        Text(text = state.name, color = Color.Black)
    }
}

@Composable
fun ListItems(heading: String, list: MutableList<Images> = mutableListOf()) {
    Column {
        Text(
            text = heading,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
            fontFamily = FontFamily.SansSerif,
            fontSize = 25.sp
        )

        if (list.isEmpty()) {
            list.addAll(
                listOf(
                    Images(),
                    Images("https://firebasestorage.googleapis.com/v0/b/tourism-3aef0.appspot.com/o/images%2Fimages.jpg?alt=media&token=c88298e1-5754-4f64-9ca8-e2131aad8ee8"),
                    Images("https://firebasestorage.googleapis.com/v0/b/tourism-3aef0.appspot.com/o/images%2Fimages%20(1).jpg?alt=media&token=d7d4c7f0-dbe7-4855-9cef-7aa3ba0321fc"),
                    Images("https://firebasestorage.googleapis.com/v0/b/tourism-3aef0.appspot.com/o/images%2Fimages%20(2).jpg?alt=media&token=c8142c7a-945f-47b5-be53-659080932261"),
                    Images("https://firebasestorage.googleapis.com/v0/b/tourism-3aef0.appspot.com/o/images%2Fimages%20(3).jpg?alt=media&token=3d5903ab-bd05-4e3c-8b7b-dc68568ceb48")
                )
            )
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(list) { image ->
                ImagesWidget(image = image)
            }
        }
    }
}

@Composable
fun ImagesWidget(image: Images) {
    Box(modifier = Modifier
        .padding(15.dp)
        .height(135.dp)
        .width(155.dp)
        .clip(RoundedCornerShape(45.dp))
        .border(2.dp,Color.Gray)
        .clickable { /*Add functionality later*/ },
        contentAlignment = Alignment.Center
        ) {
        Image(
            painter = rememberAsyncImagePainter(image.image),
            contentDescription = null,modifier = Modifier
                .fillMaxSize()



        )
    }
}
